package solipingen.progressivearchery.item;

import java.util.function.Predicate;
import java.util.stream.Stream;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
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
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import solipingen.progressivearchery.advancement.criterion.ModCriteria;
import solipingen.progressivearchery.item.arrows.KidArrowItem;
import solipingen.progressivearchery.item.arrows.ModArrowItem;
import solipingen.progressivearchery.sound.ModSoundEvents;


public class ModBowItem extends RangedWeaponItem implements Vanishable {
    public static final Predicate<ItemStack> MOD_BOW_PROJECTILES = stack -> stack.getItem() instanceof ModArrowItem || stack.isOf(Items.SPECTRAL_ARROW);
    public static final Predicate<ItemStack> TUBULAR_BOW_PROJECTILES = stack -> stack.getItem() instanceof KidArrowItem;
    public static final int RANGE = 15;
    private final ToolMaterial material;
    private final int bowType;
    private final float maxReleaseSpeed;
    private final float divergence;
    private boolean pulled;


    public ModBowItem(ToolMaterial material, int bowType, Item.Settings settings) {
        super(settings.maxDamageIfAbsent(MathHelper.ceil((bowType == 3 ? 0.85f : 1.0f + bowType*0.25f)*material.getDurability())));
        this.material = material;
        this.bowType = bowType;
        this.maxReleaseSpeed = 3.0f + 0.3f*(this.bowType >= 2 ? this.bowType + 1 : this.bowType) + 0.2f*this.material.getMiningLevel();
        this.divergence = 1.0f + (this.bowType >= 2 ? 0.25f*(4.0f - (float)this.bowType) : 0.0f) - 0.1f*this.material.getMiningLevel();
        this.pulled = false;
    }

    public Predicate<ItemStack> getHeldProjectiles() {
        return this.getProjectiles();
    }

    public static ItemStack getHeldProjectile(LivingEntity entity, Predicate<ItemStack> predicate) {
        if (predicate.test(entity.getStackInHand(Hand.OFF_HAND))) {
            return entity.getStackInHand(Hand.OFF_HAND);
        }
        if (predicate.test(entity.getStackInHand(Hand.MAIN_HAND))) {
            return entity.getStackInHand(Hand.MAIN_HAND);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity playerEntity = (PlayerEntity)user;
        float f = this.getPullProgress(this.getMaxUseTime(stack) - remainingUseTicks, stack);
        ItemStack quiverItemStack = ModBowItem.getFilledQuiver(playerEntity);
        ItemStack itemStack = this.getModArrowType(playerEntity, stack);
        float randomf = user.getRandom().nextFloat();
        boolean bl = playerEntity.getAbilities().creativeMode;
        if (EnchantmentHelper.getLevel(Enchantments.INFINITY, quiverItemStack) > 0) {
            if (itemStack.isOf(ModItems.WOODEN_ARROW) || itemStack.isOf(ModItems.WOODEN_KID_ARROW)) {
                bl |= randomf <= 1.0f;
            }
            else if ((itemStack.isOf(ModItems.FLINT_ARROW) || itemStack.isOf(ModItems.FLINT_KID_ARROW))) {
                bl |= randomf <= 0.75f;
            }
            else if ((itemStack.isOf(ModItems.COPPER_ARROW) || itemStack.isOf(ModItems.COPPER_KID_ARROW))) {
                bl |= randomf <= 0.5f;
            }
            else if ((itemStack.isOf(ModItems.GOLDEN_ARROW) || itemStack.isOf(Items.SPECTRAL_ARROW) || itemStack.isOf(ModItems.TIPPED_ARROW) || itemStack.isOf(ModItems.GOLDEN_KID_ARROW) || itemStack.isOf(ModItems.SPECTRAL_KID_ARROW) || itemStack.isOf(ModItems.TIPPED_KID_ARROW))) {
                bl |= randomf <= 0.5f;
            }
            else if ((itemStack.isOf(ModItems.IRON_ARROW) || itemStack.isOf(ModItems.IRON_KID_ARROW))) {
                bl |= randomf <= 0.25f;
            }
            else if ((itemStack.isOf(ModItems.DIAMOND_ARROW) || itemStack.isOf(ModItems.DIAMOND_KID_ARROW))) {
                bl |= randomf <= 0.125f;
            }
        }
        if (itemStack.isEmpty() && !bl) {
            return;
        }
        if (itemStack.isEmpty()) {
            itemStack = new ItemStack(ModItems.WOODEN_ARROW);
            if (this.bowType == 3) {
                itemStack = new ItemStack(ModItems.WOODEN_KID_ARROW);
            }
        }
        if (f < 0.1f) {
            return;
        }
        boolean bl2 = bl && (itemStack.getItem() instanceof ModArrowItem || itemStack.getItem() instanceof KidArrowItem);
        if (!world.isClient) {
            ModArrowItem arrowItem = (ModArrowItem)(itemStack.getItem() instanceof ModArrowItem ? itemStack.getItem() : ModItems.WOODEN_ARROW);
            KidArrowItem kidArrowItem = (KidArrowItem)(itemStack.getItem() instanceof KidArrowItem ? itemStack.getItem() : ModItems.WOODEN_KID_ARROW);
            PersistentProjectileEntity persistentProjectileEntity = arrowItem.createModArrow(world, itemStack, playerEntity);
            if (this.bowType == 3) {
                persistentProjectileEntity = kidArrowItem.createKidArrow(world, itemStack, playerEntity);
            }
            int strengthLevel = user.hasStatusEffect(StatusEffects.STRENGTH) ? user.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
            int weaknessLevel = user.hasStatusEffect(StatusEffects.WEAKNESS) ? user.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
            persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, f*this.maxReleaseSpeed + 0.3f*strengthLevel - 0.3f*weaknessLevel, this.divergence);
            persistentProjectileEntity.addVelocity(playerEntity.getVelocity());
            if (f == 1.0f) {
                persistentProjectileEntity.setCritical(true);
            }
            int j = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
            if (j > 0) {
                persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() + 0.5*j);
            }
            int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
            if (k > 0) {
                persistentProjectileEntity.setPunch(k);
            }
            if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                persistentProjectileEntity.setOnFireFor(100);
            }
            int i = EnchantmentHelper.getLevel(Enchantments.PIERCING, stack);
            if (i > 0) {
                persistentProjectileEntity.setPierceLevel((byte)i);
            }
            stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(playerEntity.getActiveHand()));
            if (bl2 || (playerEntity.isCreative() && (itemStack.isOf(Items.SPECTRAL_ARROW) || itemStack.isOf(ModItems.TIPPED_ARROW) || itemStack.isOf(ModItems.SPECTRAL_KID_ARROW) || itemStack.isOf(ModItems.TIPPED_KID_ARROW)))) {
                persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
            }
            if (persistentProjectileEntity.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED && !playerEntity.isCreative()) {
                if (quiverItemStack != ItemStack.EMPTY) {
                    NbtCompound quiverNbtCompound = quiverItemStack.getOrCreateNbt();
                    if (!quiverNbtCompound.contains(QuiverItem.ITEMS_KEY)) {
                        quiverNbtCompound.put(QuiverItem.ITEMS_KEY, new NbtList());
                    }
                    NbtList nbtList = quiverNbtCompound.getList(QuiverItem.ITEMS_KEY, NbtElement.COMPOUND_TYPE);
                    NbtCompound nbtCompound = new NbtCompound();
                    itemStack.writeNbt(nbtCompound);
                    int index = nbtList.indexOf(nbtCompound);
                    nbtList.remove(nbtCompound);
                    NbtCompound nbtCompound2 = new NbtCompound();
                    itemStack.decrement(1);
                    itemStack.writeNbt(nbtCompound2);
                    nbtList.add(index, nbtCompound2);
                } 
                else {
                    itemStack.decrement(1);
                    if (itemStack.isEmpty()) {
                        playerEntity.getInventory().removeOne(itemStack);
                    }
                }
            }
            world.spawnEntity(persistentProjectileEntity);
            playerEntity.playSound(SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + f * 0.5f);
            int multishotLevel = EnchantmentHelper.getLevel(Enchantments.MULTISHOT, stack);
            if (multishotLevel > 0) {
                for (int index = 1; index <= multishotLevel; index++) {
                    PersistentProjectileEntity multishotProjectilePositive = arrowItem.createModArrow(world, itemStack, playerEntity);
                    if (this.bowType == 3) {
                        multishotProjectilePositive = kidArrowItem.createKidArrow(world, itemStack, playerEntity);
                    }
                    multishotProjectilePositive.setVelocity(playerEntity, playerEntity.getPitch() + 4.0f*index, playerEntity.getYaw(), 0.0f, f*this.maxReleaseSpeed + 0.3f*strengthLevel - 0.3f*weaknessLevel, this.divergence);
                    multishotProjectilePositive.addVelocity(playerEntity.getVelocity());
                    if (f == 1.0f) {
                        multishotProjectilePositive.setCritical(true);
                    }
                    if (j > 0) {
                        multishotProjectilePositive.setDamage(multishotProjectilePositive.getDamage() + 0.5*j);
                    }
                    if (k > 0) {
                        multishotProjectilePositive.setPunch(k);
                    }
                    if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                        multishotProjectilePositive.setOnFireFor(100);
                    }
                    if (i > 0) {
                        multishotProjectilePositive.setPierceLevel((byte)i);
                    }
                    multishotProjectilePositive.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                    stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(playerEntity.getActiveHand()));
                    world.spawnEntity(multishotProjectilePositive);
                }
                for (int index = 1; index <= multishotLevel; index++) {
                    PersistentProjectileEntity multishotProjectileNegative = arrowItem.createModArrow(world, itemStack, playerEntity);
                    if (this.bowType == 3) {
                        multishotProjectileNegative = kidArrowItem.createKidArrow(world, itemStack, playerEntity);
                    }
                    multishotProjectileNegative.setVelocity(playerEntity, playerEntity.getPitch() - 4.0f*index, playerEntity.getYaw(), 0.0f, f*this.maxReleaseSpeed + 0.3f*strengthLevel - 0.3f*weaknessLevel, this.divergence);
                    multishotProjectileNegative.addVelocity(playerEntity.getVelocity());
                    if (f == 1.0f) {
                        multishotProjectileNegative.setCritical(true);
                    }
                    if (j > 0) {
                        multishotProjectileNegative.setDamage(multishotProjectileNegative.getDamage() + 0.5*j);
                    }
                    if (k > 0) {
                        multishotProjectileNegative.setPunch(k);
                    }
                    if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                        multishotProjectileNegative.setOnFireFor(100);
                    }
                    if (i > 0) {
                        multishotProjectileNegative.setPierceLevel((byte)i);
                    }
                    multishotProjectileNegative.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                    stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(playerEntity.getActiveHand()));
                    world.spawnEntity(multishotProjectileNegative);
                }
            }
            if (playerEntity instanceof ServerPlayerEntity) {
                if (this.bowType == 0) {
                    ModCriteria.SHOT_BOW.trigger((ServerPlayerEntity)playerEntity, stack);
                }
                else if (this.bowType == 1) {
                    ModCriteria.SHOT_HORN_BOW.trigger((ServerPlayerEntity)playerEntity, stack);
                }
                else if (this.bowType == 2) {
                    ModCriteria.SHOT_LONGBOW.trigger((ServerPlayerEntity)playerEntity, stack);
                }
                else if (this.bowType == 3) {
                    ModCriteria.SHOT_TUBULAR_BOW.trigger((ServerPlayerEntity)playerEntity, stack);
                }
            }
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }
    }

    private ItemStack getModArrowType(PlayerEntity playerEntity, ItemStack stack) {
        if (!(stack.getItem() instanceof ModBowItem || stack.getItem() instanceof KidArrowItem)) {
            return ItemStack.EMPTY;
        }
        if (ModBowItem.getFilledQuiver(playerEntity) != ItemStack.EMPTY) {
            ItemStack quiverStack = ModBowItem.getFilledQuiver(playerEntity);
            Stream<ItemStack> quiverStream = QuiverItem.getStoredStacks(quiverStack);
            ItemStack itemStack3 = ItemStack.EMPTY;
            if (this.bowType == 3) {
                itemStack3 = quiverStream.filter(item -> item.getItem() instanceof KidArrowItem).findFirst().orElse(ItemStack.EMPTY);
            }
            else {
                itemStack3 = quiverStream.filter(item -> item.getItem() instanceof ModArrowItem).findFirst().orElse(ItemStack.EMPTY);
            }
            return itemStack3;
        }
        Predicate<ItemStack> predicate = ((ModBowItem)stack.getItem()).getHeldProjectiles();
        ItemStack itemStack = ModBowItem.getHeldProjectile(playerEntity, predicate);
        if (!itemStack.isEmpty()) {
            return itemStack;
        }
        predicate = ((ModBowItem)stack.getItem()).getProjectiles();
        for (int j = 0; j < playerEntity.getInventory().size(); ++j) {
            ItemStack itemStack2 = playerEntity.getInventory().getStack(j);
            if (predicate.test(itemStack2)) {
                return itemStack2;
            }
        }
        return playerEntity.getAbilities().creativeMode ? (this.bowType == 3 ? new ItemStack(ModItems.WOODEN_KID_ARROW) : new ItemStack(ModItems.WOODEN_ARROW)) : ItemStack.EMPTY;
    }

    private static ItemStack getFilledQuiver(PlayerEntity playerEntity) {
        for (int i = 0; i < playerEntity.getInventory().size(); ++i) {
            ItemStack itemStack = playerEntity.getInventory().getStack(i);
            if (itemStack.getItem() instanceof QuiverItem && QuiverItem.getQuiverOccupancy(itemStack) > 0) {
                return itemStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public float getPullProgress(int useTicks, ItemStack stack) {
        float maxPullTicks = 10.0f + 10.0f*this.bowType;
        int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
        float quickChargeModifier = 2.5f + 2.5f*this.bowType;
        float f = (float)useTicks / (maxPullTicks -  quickChargeModifier * i);
        if ((f = (f * f + f * 2.0f) / 3.0f) > 1.0f) {
            f = 1.0f;
        }
        return f;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public int getEnchantability() {
        return this.material.getEnchantability();
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        boolean bl = !this.getModArrowType(user, itemStack).isEmpty();
        if (user.getAbilities().creativeMode || bl) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient) {
            int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
            SoundEvent soundEvent = this.getQuickChargeSound(i);
            float f = this.getPullProgress(this.getMaxUseTime(stack) - remainingUseTicks, stack);
            if (f < 0.1f) {
                this.pulled = false;
            }
            if (f >= 0.1f && !this.pulled) {
                world.playSound(null, user.getBlockPos(), soundEvent, SoundCategory.PLAYERS, 0.5f, 1.0f);
                this.pulled = true;
            }
        }
    }

    private SoundEvent getQuickChargeSound(int stage) {
        switch (stage) {
            case 1: {
                return ModSoundEvents.BOW_PULL_QUICK_CHARGE_1;
            }
            case 2: {
                return ModSoundEvents.BOW_PULL_QUICK_CHARGE_2;
            }
            case 3: {
                return ModSoundEvents.BOW_PULL_QUICK_CHARGE_3;
            }
        }
        return ModSoundEvents.BOW_PULL;
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        if (this.bowType == 3) {
            return TUBULAR_BOW_PROJECTILES;
        }
        return MOD_BOW_PROJECTILES;
    }

    @Override
    public int getRange() {
        return RANGE;
    }

    public ToolMaterial getMaterial() {
        return this.material;
    }

    public int getBowType() {
        return this.bowType;
    }

    public float getMaxReleaseSpeed() {
        return this.maxReleaseSpeed;
    }



}

