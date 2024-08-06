package solipingen.progressivearchery.item;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.google.common.collect.Lists;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import solipingen.progressivearchery.item.arrows.ModArrowItem;
import solipingen.progressivearchery.util.interfaces.mixin.entity.projectile.FireworkRocketEntityInterface;
import solipingen.progressivearchery.util.interfaces.mixin.server.network.ServerPlayerEntityInterface;


public class ModCrossbowItem extends RangedWeaponItem {
    public static final Predicate<ItemStack> MOD_CROSSBOW_PROJECTILES = stack -> stack.getItem() instanceof ArrowItem || stack.getItem() instanceof ModArrowItem || stack.getItem() instanceof FireworkRocketItem;
    private static final String CHARGED_KEY = "Charged";
    private static final String CHARGED_PROJECTILES_KEY = "ChargedProjectiles";
    public static final int RANGE = 24;
    private boolean charged = false;
    private boolean loaded = false;
    private final BowMaterial material;
    private final float arrowMaxReleaseSpeed;
    private final float rocketMaxReleaseSpeed;
    private final float divergence;

    
    public ModCrossbowItem(BowMaterial material, Item.Settings settings) {
        super(settings.maxDamage(MathHelper.ceil(1.15f*material.getDurability())));
        this.material = material;
        this.arrowMaxReleaseSpeed = 3.6f + 0.2f*this.material.getMiningLevel();
        this.rocketMaxReleaseSpeed = 2.8f + 0.2f*this.material.getMiningLevel();
        this.divergence = 0.75f - 0.1f*this.material.getMiningLevel();
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return MOD_CROSSBOW_PROJECTILES;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        ChargedProjectilesComponent chargedProjectilesComponent = itemStack.getOrDefault(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
        if (chargedProjectilesComponent != null && !chargedProjectilesComponent.isEmpty()) {
            this.shootAll(world, user, hand, itemStack, this.getSpeed(chargedProjectilesComponent), this.divergence, null);
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
        Predicate<ItemStack> predicate = stack.isOf(Items.CROSSBOW) ? ((CrossbowItem)stack.getItem()).getHeldProjectiles() : ((ModCrossbowItem)stack.getItem()).getHeldProjectiles();
        ItemStack itemStack = ModCrossbowItem.getHeldProjectile(shooter, predicate);
        if (!itemStack.isEmpty()) {
            return itemStack;
        }
        if (shooter instanceof PlayerEntity) {
        PlayerEntity playerEntity = (PlayerEntity)shooter;
            if (!ModCrossbowItem.getFilledQuiver(playerEntity).isEmpty()) {
                ItemStack quiverStack = ModCrossbowItem.getFilledQuiver(playerEntity);
                Stream<ItemStack> quiverStream = QuiverItem.getStoredStacks(quiverStack);
                ItemStack itemStack3 = quiverStream.filter(predicate).findFirst().orElse(ItemStack.EMPTY);
                return itemStack3;
            }
            if (!itemStack.isEmpty()) {
                return itemStack;
            }
            predicate = stack.isOf(Items.CROSSBOW) ? ((CrossbowItem)stack.getItem()).getProjectiles() : ((ModCrossbowItem)stack.getItem()).getProjectiles();
            for (int j = 0; j < playerEntity.getInventory().size(); ++j) {
                ItemStack itemStack2 = playerEntity.getInventory().getStack(j);
                if (predicate.test(itemStack2)) {
                    return itemStack2;
                }
            }
            return playerEntity.getAbilities().creativeMode ? new ItemStack(ModItems.WOODEN_ARROW) : ItemStack.EMPTY;
        }
        else if (shooter instanceof MobEntity) {
            return shooter.getProjectileType(stack);
        }
        else {
            return ItemStack.EMPTY;
        }
    }

    private float getSpeed(ChargedProjectilesComponent chargedProjectilesComponent) {
        if (chargedProjectilesComponent.contains(Items.FIREWORK_ROCKET)) {
            return this.rocketMaxReleaseSpeed;
        }
        return this.arrowMaxReleaseSpeed;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!world.isClient()) {
            int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
            float f = ModCrossbowItem.getPullProgress(i, stack, world);
            if (f >= 1.0f && !ModCrossbowItem.isCharged(stack) && ModCrossbowItem.loadProjectiles(user, stack)) {
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

    private static boolean loadProjectiles(LivingEntity shooter, ItemStack crossbow) {
        List<ItemStack> list = load(crossbow, ModCrossbowItem.getModProjectileType(shooter, crossbow), shooter);
        if (!list.isEmpty()) {
            crossbow.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.of(list));
            return true;
        }
        else {
            return false;
        }
    }

    private static ItemStack getFilledQuiver(PlayerEntity playerEntity) {
        ItemStack itemStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinketComponentOptional = TrinketsApi.getTrinketComponent(playerEntity);
        if (trinketComponentOptional.isPresent()) {
            Map<String, Map<String, TrinketInventory>> trinketInventoryMap = trinketComponentOptional.get().getInventory();
            if (trinketInventoryMap.containsKey("chest") && trinketInventoryMap.get("chest").containsKey("back")) {
                TrinketInventory trinketInventory = trinketInventoryMap.get("chest").get("back");
                for (int j = 0; j < trinketInventory.size(); j++) {
                    if (trinketInventory.getStack(j).getItem() instanceof QuiverItem && QuiverItem.getAmountFilled(trinketInventory.getStack(j)) > 0) {
                        itemStack = trinketInventory.getStack(j);
                        break;
                    }
                }
            }
            if (itemStack.isEmpty()) {
                for (int i = 0; i < playerEntity.getInventory().size(); i++) {
                    if (playerEntity.getInventory().getStack(i).getItem() instanceof QuiverItem && QuiverItem.getAmountFilled(playerEntity.getInventory().getStack(i)) > 0) {
                        itemStack = playerEntity.getInventory().getStack(i);
                        break;
                    }
                }
            }
        }
        else {
            for (int i = 0; i < playerEntity.getInventory().size(); i++) {
                if (playerEntity.getInventory().getStack(i).getItem() instanceof QuiverItem && QuiverItem.getAmountFilled(playerEntity.getInventory().getStack(i)) > 0.0f) {
                    itemStack = playerEntity.getInventory().getStack(i);
                    break;
                }
            }
        }
        return itemStack;
    }

    public static boolean isCharged(ItemStack stack) {
        ChargedProjectilesComponent chargedProjectilesComponent = stack.getOrDefault(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
        return !chargedProjectilesComponent.isEmpty();
    }

    public static boolean hasProjectile(ItemStack crossbow, Item projectile) {
        ChargedProjectilesComponent chargedProjectilesComponent = crossbow.getOrDefault(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
        return chargedProjectilesComponent.contains(projectile);
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
        Vector3f vector3f;
        if (target != null) {
            double d = target.getX() - shooter.getX();
            double e = target.getZ() - shooter.getZ();
            double f = Math.sqrt(d * d + e * e);
            double g = target.getBodyY(0.3333333333333333) - projectile.getY() + f*(0.10 - 0.01*this.material.getMiningLevel());
            vector3f = ModCrossbowItem.calcVelocity(shooter, new Vec3d(d, g, e), yaw);
        }
        else {
            Vec3d vec3d = shooter.getOppositeRotationVector(1.0f);
            Quaternionf quaternionf = (new Quaternionf()).setAngleAxis((double)(yaw*0.017453292f), vec3d.x, vec3d.y, vec3d.z);
            Vec3d vec3d2 = shooter.getRotationVec(1.0f);
            vector3f = vec3d2.toVector3f().rotate(quaternionf);
        }
        projectile.setVelocity(vector3f.x(), vector3f.y(), vector3f.z(), speed, divergence);
        float h = ModCrossbowItem.getSoundPitch(shooter.getRandom(), index);
        shooter.getWorld().playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.ITEM_CROSSBOW_SHOOT, shooter.getSoundCategory(), 1.0f, h);
    }

    public void shootAll(World world, LivingEntity shooter, Hand hand, ItemStack stack, float speed, float divergence, @Nullable LivingEntity livingEntity) {
        if (!world.isClient() && world instanceof ServerWorld serverWorld) {
            ChargedProjectilesComponent chargedProjectilesComponent = stack.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
            if (chargedProjectilesComponent != null && !chargedProjectilesComponent.isEmpty()) {
                this.shootAll(serverWorld, shooter, hand, stack, chargedProjectilesComponent.getProjectiles(), speed, divergence, shooter instanceof PlayerEntity, livingEntity);
                if (shooter instanceof ServerPlayerEntity) {
                    ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)shooter;
                    Criteria.SHOT_CROSSBOW.trigger(serverPlayerEntity, stack);
                    serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                }
            }
        }
    }

    private static float getSoundPitch(Random random, int index) {
        return index == 0 ? 1.0F : ModCrossbowItem.getSoundPitch((index & 1) == 1, random);
    }

    private static float getSoundPitch(boolean flag, Random random) {
        float f = flag ? 0.63f : 0.43f;
        return 1.0f / (random.nextFloat() * 0.5f + 1.8f) + f;
    }

    @Override
    protected int getWeaponStackDamage(ItemStack projectile) {
        boolean bl = projectile.isOf(Items.FIREWORK_ROCKET);
        boolean bl2 = bl && this.material.getMiningLevel() < BowMaterials.IRON.getMiningLevel();
        boolean bl3 = bl && this.material.getMiningLevel() < BowMaterials.DIAMOND.getMiningLevel();
        return bl2 ? 3 : (bl3 ? 2 : 1);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient()) {
            RegistryEntryLookup<Enchantment> enchantmentLookup = world.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            int i = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.QUICK_CHARGE), stack);
            SoundEvent soundEvent = this.getQuickChargeSound(i);
            SoundEvent soundEvent2 = i == 0 ? SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE.value() : null;
            float f = (float)(stack.getMaxUseTime(user) - remainingUseTicks) / (float)ModCrossbowItem.getPullTime(stack, world);
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
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return ModCrossbowItem.getPullTime(stack, user.getWorld()) + 3;
    }

    public static int getPullTime(ItemStack stack, World world) {
        RegistryEntryLookup<Enchantment> enchantmentLookup = world.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
        int i = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.QUICK_CHARGE), stack);
        return i == 0 ? 20 : 20 - 5*i;
    }

    @Override
    public int getEnchantability() {
        return this.material.getEnchantability();
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    private static Vector3f calcVelocity(LivingEntity shooter, Vec3d direction, float yaw) {
        Vector3f vector3f = direction.toVector3f().normalize();
        Vector3f vector3f2 = (new Vector3f(vector3f)).cross(new Vector3f(0.0F, 1.0F, 0.0F));
        if ((double)vector3f2.lengthSquared() <= 1.0E-7) {
            Vec3d vec3d = shooter.getOppositeRotationVector(1.0F);
            vector3f2 = (new Vector3f(vector3f)).cross(vec3d.toVector3f());
        }

        Vector3f vector3f3 = (new Vector3f(vector3f)).rotateAxis(1.5707964F, vector3f2.x, vector3f2.y, vector3f2.z);
        return (new Vector3f(vector3f)).rotateAxis(yaw * 0.017453292F, vector3f3.x, vector3f3.y, vector3f3.z);
    }

    @Override
    protected ProjectileEntity createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
        ProjectileEntity projectileEntity;
        if (projectileStack.isOf(Items.FIREWORK_ROCKET)) {
            RegistryEntryLookup<Enchantment> enchantmentLookup = world.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            projectileEntity = new FireworkRocketEntity(world, projectileStack, shooter, shooter.getX(), shooter.getEyeY() - 0.15000000596046448, shooter.getZ(), true);
            ((FireworkRocketEntityInterface)projectileEntity).setPower(EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.POWER), weaponStack));
            ((FireworkRocketEntityInterface)projectileEntity).setPunch(EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.PUNCH), weaponStack));
            ((FireworkRocketEntityInterface)projectileEntity).setFlame(EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.FLAME), weaponStack) > 0);
        }
        else {
            projectileEntity = super.createArrowEntity(world, shooter, weaponStack, projectileStack, critical);
        }
        return projectileEntity;
    }

    private SoundEvent getQuickChargeSound(int stage) {
        switch (stage) {
            case 1: {
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_1.value();
            }
            case 2: {
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_2.value();
            }
            case 3: {
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_3.value();
            }
        }
        return SoundEvents.ITEM_CROSSBOW_LOADING_START.value();
    }

    private static float getPullProgress(int useTicks, ItemStack stack, World world) {
        float f = (float)useTicks / (float)ModCrossbowItem.getPullTime(stack, world);
        if (f > 1.0f) {
            f = 1.0f;
        }
        return f;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        ChargedProjectilesComponent chargedProjectilesComponent = (ChargedProjectilesComponent)stack.get(DataComponentTypes.CHARGED_PROJECTILES);
        if (chargedProjectilesComponent != null && !chargedProjectilesComponent.isEmpty()) {
            ItemStack itemStack = (ItemStack)chargedProjectilesComponent.getProjectiles().get(0);
            tooltip.add(Text.translatable("item.progressivearchery.wooden_crossbow.projectile").append(ScreenTexts.SPACE).append(itemStack.toHoverableText()));
            if (type.isAdvanced() && itemStack.isOf(Items.FIREWORK_ROCKET)) {
                List<Text> list = Lists.newArrayList();
                Items.FIREWORK_ROCKET.appendTooltip(itemStack, context, list, type);
                if (!list.isEmpty()) {
                    for(int i = 0; i < list.size(); ++i) {
                        list.set(i, Text.literal("  ").append((Text)list.get(i)).formatted(Formatting.GRAY));
                    }
                    tooltip.addAll(list);
                }
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

