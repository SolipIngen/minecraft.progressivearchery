package solipingen.progressivearchery.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.google.common.collect.Lists;

import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Vanishable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.projectile.arrow.SpectralArrowEntity;
import solipingen.progressivearchery.item.arrows.ModArrowItem;
import solipingen.progressivearchery.util.interfaces.mixin.server.network.ServerPlayerEntityInterface;


public class ModCrossbowItem extends RangedWeaponItem implements Vanishable {
    public static final Predicate<ItemStack> MOD_CROSSBOW_PROJECTILES = stack -> stack.getItem() instanceof ModArrowItem || stack.isOf(Items.SPECTRAL_ARROW) || stack.getItem() instanceof FireworkRocketItem;
    public static final Predicate<ItemStack> MOD_CROSSBOW_HELD_PROJECTILES = MOD_CROSSBOW_PROJECTILES;
    private static final String CHARGED_KEY = "Charged";
    private static final String CHARGED_PROJECTILES_KEY = "ChargedProjectiles";
    public static final int RANGE = 24;
    private boolean charged = false;
    private boolean loaded = false;
    private final ToolMaterial material;
    private final float arrowMaxReleaseSpeed;
    private final float rocketMaxReleaseSpeed;
    private final float divergence;

    
    public ModCrossbowItem(ToolMaterial material, Item.Settings settings) {
        super(settings.maxDamageIfAbsent(MathHelper.ceil(1.15f*material.getDurability())));
        this.material = material;
        this.arrowMaxReleaseSpeed = 3.6f + 0.2f*this.material.getMiningLevel();
        this.rocketMaxReleaseSpeed = 2.8f + 0.2f*this.material.getMiningLevel();
        this.divergence = 0.75f - 0.1f*this.material.getMiningLevel();
    }

    @Override
    public Predicate<ItemStack> getHeldProjectiles() {
        return MOD_CROSSBOW_HELD_PROJECTILES;
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return MOD_CROSSBOW_PROJECTILES;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (ModCrossbowItem.isCharged(itemStack)) {
            ModCrossbowItem.shootAll(world, user, hand, itemStack, this.getSpeed(itemStack), this.divergence);
            ModCrossbowItem.setCharged(itemStack, false);
            return TypedActionResult.consume(itemStack);
        }
        if (!ModCrossbowItem.getModProjectileType(user, itemStack).isEmpty()) {
            if (!ModCrossbowItem.isCharged(itemStack)) {
                this.charged = false;
                this.loaded = false;
                user.setCurrentHand(hand);
            }
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }

    public static ItemStack getModProjectileType(LivingEntity shooter, ItemStack stack) {
        if (!(stack.getItem() instanceof ModCrossbowItem)) {
            return ItemStack.EMPTY;
        }
        Predicate<ItemStack> predicate = ((ModCrossbowItem)stack.getItem()).getHeldProjectiles();
        ItemStack itemStack = ModCrossbowItem.getHeldProjectile(shooter, predicate);
        if (!itemStack.isEmpty()) {
            return itemStack;
        }
        predicate = ((ModCrossbowItem)stack.getItem()).getProjectiles();
        if (shooter instanceof PlayerEntity) {
        PlayerEntity playerEntity = (PlayerEntity)shooter;
            if (ModCrossbowItem.getFilledQuiver(playerEntity) != ItemStack.EMPTY) {
                ItemStack quiverStack = ModCrossbowItem.getFilledQuiver(playerEntity);
                Stream<ItemStack> quiverStream = QuiverItem.getStoredStacks(quiverStack);
                ItemStack itemStack3 = quiverStream.filter(a -> a.getItem() instanceof ModArrowItem || a.isOf(Items.SPECTRAL_ARROW) || a.getItem() instanceof FireworkRocketItem).findFirst().orElse(ItemStack.EMPTY);
                return itemStack3;
            }
            if (!itemStack.isEmpty()) {
                return itemStack;
            }
            predicate = ((ModCrossbowItem)stack.getItem()).getProjectiles();
            for (int j = 0; j < playerEntity.getInventory().size(); ++j) {
                ItemStack itemStack2 = playerEntity.getInventory().getStack(j);
                if (predicate.test(itemStack2)) {
                    return itemStack2;
                }
            }
            return playerEntity.getAbilities().creativeMode ? new ItemStack(ModItems.WOODEN_ARROW) : ItemStack.EMPTY;
        }
        else if (shooter instanceof HostileEntity) {
            return shooter.getProjectileType(stack);
        }
        else {
            return ItemStack.EMPTY;
        }
    }

    private float getSpeed(ItemStack stack) {
        if (ModCrossbowItem.hasProjectile(stack, Items.FIREWORK_ROCKET)) {
            return this.rocketMaxReleaseSpeed;
        }
        return this.arrowMaxReleaseSpeed;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!world.isClient) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            float f = ModCrossbowItem.getPullProgress(i, stack);
            if (f >= 1.0f && !ModCrossbowItem.isCharged(stack) && ModCrossbowItem.loadProjectiles(user, stack)) {
                ModCrossbowItem.setCharged(stack, true);
                SoundCategory soundCategory = user instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_CROSSBOW_LOADING_END, soundCategory, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.5f + 1.0f) + 0.2f);
            }
            if (user instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)user;
                if (((ServerPlayerEntityInterface)serverPlayerEntity).getMultishotKilledEntities() != null && ((ServerPlayerEntityInterface)serverPlayerEntity).getMultishotKilledEntities().size() > 0) {
                    ((ServerPlayerEntityInterface)serverPlayerEntity).clearMultishotKilledEntities();
                }
            }
        }
    }

    private static boolean loadProjectiles(LivingEntity shooter, ItemStack projectile) {
        int i = EnchantmentHelper.getLevel(Enchantments.MULTISHOT, projectile);
        boolean bl = shooter instanceof PlayerEntity && ((PlayerEntity)shooter).getAbilities().creativeMode;
        ItemStack itemStack = ModCrossbowItem.getModProjectileType(shooter, projectile);
        ItemStack itemStack2 = itemStack.copy();
        for (int k = 0; k < 2*i + 1; ++k) {
            if (k > 0) {
                itemStack = itemStack2.copy();
            }
            if (itemStack.isEmpty() && bl) {
                itemStack = new ItemStack(ModItems.WOODEN_ARROW);
                itemStack2 = itemStack.copy();
            }
            if (ModCrossbowItem.loadProjectile(shooter, projectile, itemStack, k > 0, bl)) continue;
            return false;
        }
        return true;
    }

    private static boolean loadProjectile(LivingEntity shooter, ItemStack crossbow, ItemStack projectile, boolean simulated, boolean creative) {
        ItemStack itemStack;
        boolean bl;
        if (projectile.isEmpty()) {
            return false;
        }
        ItemStack quiverItemStack = shooter instanceof PlayerEntity ? ModCrossbowItem.getFilledQuiver((PlayerEntity)shooter) : ItemStack.EMPTY;
        Boolean crossbowProjectileBl = projectile.getItem() instanceof ModArrowItem || projectile.getItem() instanceof FireworkRocketItem;
        float randomf = shooter.getRandom().nextFloat();
        bl = (creative || EnchantmentHelper.getLevel(Enchantments.INFINITY, quiverItemStack) > 0) && crossbowProjectileBl;
        if (projectile.isOf(ModItems.FLINT_ARROW) && randomf > 0.75f) {
            bl = creative && crossbowProjectileBl;
        }
        else if (projectile.isOf(ModItems.COPPER_ARROW) && randomf > 0.5f) {
            bl = creative && crossbowProjectileBl;
        }
        else if ((projectile.isOf(ModItems.GOLDEN_ARROW) || projectile.isOf(ModItems.TIPPED_ARROW) || projectile.isOf(Items.SPECTRAL_ARROW)) && randomf > 0.5f) {
            bl = creative && crossbowProjectileBl;
        }
        else if (projectile.isOf(ModItems.IRON_ARROW) && randomf > 0.25f) {
            bl = creative && crossbowProjectileBl;
        }
        else if (projectile.isOf(ModItems.DIAMOND_ARROW) && randomf > 0.125f) {
            bl = creative && crossbowProjectileBl;
        }
        else if (projectile.isOf(Items.FIREWORK_ROCKET)) {
            NbtCompound nbtCompound = projectile.getSubNbt(FireworkRocketItem.FIREWORKS_KEY);
            NbtList nbtList = nbtCompound.getList(FireworkRocketItem.EXPLOSIONS_KEY, NbtElement.COMPOUND_TYPE);
            if (randomf > 1.0f - 0.125f*nbtList.size()) {
                bl = creative && crossbowProjectileBl;
            }
        }
        if (!(bl || creative || simulated)) {
            itemStack = projectile.split(1);
            if (shooter instanceof PlayerEntity) {
                if (quiverItemStack != ItemStack.EMPTY) {
                    NbtCompound quivernbtCompound = quiverItemStack.getOrCreateNbt();
                    if (!quivernbtCompound.contains(QuiverItem.ITEMS_KEY)) {
                        quivernbtCompound.put(QuiverItem.ITEMS_KEY, new NbtList());
                    }
                NbtList nbtList = quivernbtCompound.getList(QuiverItem.ITEMS_KEY, NbtElement.COMPOUND_TYPE);
                projectile.increment(1);
                NbtCompound nbtCompound = new NbtCompound();
                projectile.writeNbt(nbtCompound);
                int index = nbtList.indexOf(nbtCompound);
                nbtList.remove(nbtCompound);
                projectile.decrement(1);
                NbtCompound nbtCompound2 = new NbtCompound();
                projectile.writeNbt(nbtCompound2);
                nbtList.add(index, nbtCompound2);
                }
                else {
                    if (projectile.isEmpty()) {
                        ((PlayerEntity)shooter).getInventory().removeOne(projectile);
                    }
                }
            }
        }
        else {
            itemStack = projectile.copy();
        }
        ModCrossbowItem.putProjectile(crossbow, itemStack);
        return true;
    }

    private static ItemStack getFilledQuiver(PlayerEntity playerEntity) {
        for (int i = 0; i < playerEntity.getInventory().size(); ++i) {
            ItemStack itemStack = playerEntity.getInventory().getStack(i);
            if (itemStack.isOf(ModItems.QUIVER) && QuiverItem.getQuiverOccupancy(itemStack) > 0) {
                return itemStack;
            }
            continue;
        }
        return ItemStack.EMPTY;
    }

    public static boolean isCharged(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        return nbtCompound != null && nbtCompound.getBoolean(CHARGED_KEY);
    }

    public static void setCharged(ItemStack stack, boolean charged) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putBoolean(CHARGED_KEY, charged);
    }

    private static void putProjectile(ItemStack crossbow, ItemStack projectile) {
        NbtCompound nbtCompound = crossbow.getOrCreateNbt();
        NbtList nbtList = nbtCompound.contains(CHARGED_PROJECTILES_KEY, NbtElement.LIST_TYPE) ? nbtCompound.getList(CHARGED_PROJECTILES_KEY, NbtElement.COMPOUND_TYPE) : new NbtList();
        NbtCompound nbtCompound2 = new NbtCompound();
        projectile.writeNbt(nbtCompound2);
        nbtList.add(nbtCompound2);
        nbtCompound.put(CHARGED_PROJECTILES_KEY, nbtList);
    }

    private static List<ItemStack> getProjectiles(ItemStack crossbow) {
        NbtList nbtList;
        ArrayList<ItemStack> list = Lists.newArrayList();
        NbtCompound nbtCompound = crossbow.getNbt();
        if (nbtCompound != null && nbtCompound.contains(CHARGED_PROJECTILES_KEY, NbtElement.LIST_TYPE) && (nbtList = nbtCompound.getList(CHARGED_PROJECTILES_KEY, NbtElement.COMPOUND_TYPE)) != null) {
            for (int i = 0; i < nbtList.size(); ++i) {
                NbtCompound nbtCompound2 = nbtList.getCompound(i);
                list.add(ItemStack.fromNbt(nbtCompound2));
            }
        }
        return list;
    }

    private static void clearProjectiles(ItemStack crossbow) {
        NbtCompound nbtCompound = crossbow.getNbt();
        if (nbtCompound != null) {
            NbtList nbtList = nbtCompound.getList(CHARGED_PROJECTILES_KEY, NbtElement.LIST_TYPE);
            nbtList.clear();
            nbtCompound.put(CHARGED_PROJECTILES_KEY, nbtList);
        }
    }

    public static boolean hasProjectile(ItemStack crossbow, Item projectile) {
        return ModCrossbowItem.getProjectiles(crossbow).stream().anyMatch(s -> s.isOf(projectile));
    }

    private static void shoot(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated) {
        ProjectileEntity projectileEntity;
        if (world.isClient) {
            return;
        }
        ModCrossbowItem modCrossbow = (ModCrossbowItem)crossbow.getItem();
        boolean bl = projectile.isOf(Items.FIREWORK_ROCKET);
        if (bl) {
            projectileEntity = new FireworkRocketEntity(world, projectile, shooter, shooter.getX(), shooter.getEyeY() - 0.15, shooter.getZ(), true);
        } 
        else {
            ModArrowItem arrowItem = (ModArrowItem)(projectile.getItem() instanceof ModArrowItem ? projectile.getItem() : ModItems.WOODEN_ARROW);
            projectileEntity = arrowItem.createModArrow(world, projectile, shooter);
            if (projectile.isOf(Items.SPECTRAL_ARROW)) {
                projectileEntity = new SpectralArrowEntity(world, shooter);
            }
            if (shooter instanceof PlayerEntity) {
                ((PersistentProjectileEntity)projectileEntity).setCritical(true);
            }
            int j = EnchantmentHelper.getLevel(Enchantments.POWER, crossbow);
            if (j > 0) {
                ((PersistentProjectileEntity)projectileEntity).setDamage(((PersistentProjectileEntity)projectileEntity).getDamage() + 0.5*j);
            }
            int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, crossbow);
            if (k > 0) {
                ((PersistentProjectileEntity)projectileEntity).setPunch(k);
            }
            if (EnchantmentHelper.getLevel(Enchantments.FLAME, crossbow) > 0) {
                ((PersistentProjectileEntity)projectileEntity).setOnFireFor(100);
            }
            ((PersistentProjectileEntity)projectileEntity).setSound(SoundEvents.ITEM_CROSSBOW_HIT);
            ((PersistentProjectileEntity)projectileEntity).setShotFromCrossbow(true);
            int i = EnchantmentHelper.getLevel(Enchantments.PIERCING, crossbow);
            if (i > 0) {
                ((PersistentProjectileEntity)projectileEntity).setPierceLevel((byte)i);
            }
            if (creative || EnchantmentHelper.getLevel(Enchantments.INFINITY, crossbow) > 0) {
                ((PersistentProjectileEntity)projectileEntity).pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
            }
            if (simulated != 0.0f) {
                ((PersistentProjectileEntity)projectileEntity).pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
            }
        }
        if (shooter instanceof CrossbowUser) {
            CrossbowUser crossbowUser = (CrossbowUser)shooter;
            crossbowUser.shoot(crossbowUser.getTarget(), crossbow, projectileEntity, simulated);
        } 
        else {
            Vec3d vec3d = shooter.getOppositeRotationVector(1.0f);
            Quaternionf quaternionf = new Quaternionf().setAngleAxis((double)(simulated * ((float)Math.PI / 180)), vec3d.x, vec3d.y, vec3d.z);
            Vec3d vec3d2 = shooter.getRotationVec(1.0f);
            Vector3f vector3f = vec3d2.toVector3f().rotate(quaternionf);
            projectileEntity.setVelocity(vector3f.x(), vector3f.y(), vector3f.z(), speed, divergence);
            projectileEntity.addVelocity(shooter.getVelocity());
        }
        world.spawnEntity(projectileEntity);
        boolean bl2 = bl && modCrossbow.material.getMiningLevel() < MiningLevels.IRON;
        boolean bl3 = bl && modCrossbow.material.getMiningLevel() < MiningLevels.DIAMOND;
        crossbow.damage(bl2 ? 3 : (bl3 ? 2 : 1), shooter, e -> e.sendToolBreakStatus(hand));
        world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0f, soundPitch);
    }

    public static void shootAll(World world, LivingEntity entity, Hand hand, ItemStack stack, float speed, float divergence) {
        List<ItemStack> list = ModCrossbowItem.getProjectiles(stack);
        float[] fs = ModCrossbowItem.getSoundPitches(entity.getRandom());
        for (int i = 0; i < list.size(); ++i) {
            boolean bl;
            ItemStack itemStack = list.get(i);
            bl = entity instanceof PlayerEntity && ((PlayerEntity)entity).getAbilities().creativeMode;
            if (itemStack.isEmpty()) continue;
            if (i == 0) {
                ModCrossbowItem.shoot(world, entity, hand, stack, itemStack, fs[i], bl, speed, divergence, 0.0f);
                continue;
            }
            if (i == 1) {
                ModCrossbowItem.shoot(world, entity, hand, stack, itemStack, fs[i], bl, speed, divergence, -10.0f);
                if (EnchantmentHelper.getLevel(Enchantments.MULTISHOT, stack) > 1) {
                    ModCrossbowItem.shoot(world, entity, hand, stack, itemStack, fs[i], bl, speed, divergence, -20.0f);
                    if (EnchantmentHelper.getLevel(Enchantments.MULTISHOT, stack) > 2) {
                        ModCrossbowItem.shoot(world, entity, hand, stack, itemStack, fs[i], bl, speed, divergence, -30.0f);
                    }
                }
                continue;
            }
            if (i != 2) continue;
            ModCrossbowItem.shoot(world, entity, hand, stack, itemStack, fs[i], bl, speed, divergence, 10.0f);
            if (EnchantmentHelper.getLevel(Enchantments.MULTISHOT, stack) > 1) {
                ModCrossbowItem.shoot(world, entity, hand, stack, itemStack, fs[i], bl, speed, divergence, 20.0f);
                if (EnchantmentHelper.getLevel(Enchantments.MULTISHOT, stack) > 2) {
                    ModCrossbowItem.shoot(world, entity, hand, stack, itemStack, fs[i], bl, speed, divergence, 30.0f);
                }
            }
        }
        ModCrossbowItem.postShoot(world, entity, stack);
    }

    private static float[] getSoundPitches(Random random) {
        boolean bl = random.nextBoolean();
        return new float[]{1.0f, ModCrossbowItem.getSoundPitch(bl, random), ModCrossbowItem.getSoundPitch(!bl, random)};
    }

    private static float getSoundPitch(boolean flag, Random random) {
        float f = flag ? 0.63f : 0.43f;
        return 1.0f / (random.nextFloat() * 0.5f + 1.8f) + f;
    }

    private static void postShoot(World world, LivingEntity entity, ItemStack stack) {
        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)entity;
            if (!world.isClient) {
                Criteria.SHOT_CROSSBOW.trigger(serverPlayerEntity, stack);
            }
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
        }
        ModCrossbowItem.clearProjectiles(stack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient) {
            int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
            SoundEvent soundEvent = this.getQuickChargeSound(i);
            SoundEvent soundEvent2 = i == 0 ? SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE : null;
            float f = (float)(stack.getMaxUseTime() - remainingUseTicks) / (float)ModCrossbowItem.getPullTime(stack);
            if (f < 0.2f) {
                this.charged = false;
                this.loaded = false;
            }
            if (f >= 0.2f && !this.charged) {
                this.charged = true;
                world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent, SoundCategory.PLAYERS, 0.5f, 1.0f);
            }
            if (f >= 0.5f && soundEvent2 != null && !this.loaded) {
                this.loaded = true;
                world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent2, SoundCategory.PLAYERS, 0.5f, 1.0f);
            }
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return ModCrossbowItem.getPullTime(stack) + 3;
    }

    public static int getPullTime(ItemStack stack) {
        int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
        return i == 0 ? 20 : 20 - 5 * i;
    }

    @Override
    public int getEnchantability() {
        return this.material.getEnchantability();
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    private SoundEvent getQuickChargeSound(int stage) {
        switch (stage) {
            case 1: {
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_1;
            }
            case 2: {
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_2;
            }
            case 3: {
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_3;
            }
        }
        return SoundEvents.ITEM_CROSSBOW_LOADING_START;
    }

    private static float getPullProgress(int useTicks, ItemStack stack) {
        float f = (float)useTicks / (float)ModCrossbowItem.getPullTime(stack);
        if (f > 1.0f) {
            f = 1.0f;
        }
        return f;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        List<ItemStack> list = ModCrossbowItem.getProjectiles(stack);
        if (!ModCrossbowItem.isCharged(stack) || list.isEmpty()) {
            return;
        }
        ItemStack itemStack = list.get(0);
        tooltip.add(Text.translatable("item.progressivearchery.wooden_crossbow.projectile").append(" ").append(itemStack.toHoverableText()));
        if (context.isAdvanced() && itemStack.isOf(Items.FIREWORK_ROCKET)) {
            ArrayList<Text> list2 = Lists.newArrayList();
            Items.FIREWORK_ROCKET.appendTooltip(itemStack, world, list2, context);
            if (!list2.isEmpty()) {
                for (int i = 0; i < list2.size(); ++i) {
                    list2.set(i, Text.literal("  ").append((Text)list2.get(i)).formatted(Formatting.GRAY));
                }
                tooltip.addAll(list2);
            }
        }
    }

    @Override
    public boolean isUsedOnRelease(ItemStack stack) {
        return stack.isOf(this);
    }

    @Override
    public int getRange() {
        return RANGE;
    }

}

