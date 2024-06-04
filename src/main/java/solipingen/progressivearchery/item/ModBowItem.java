package solipingen.progressivearchery.item;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
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
import org.jetbrains.annotations.Nullable;
import solipingen.progressivearchery.advancement.criterion.ModCriteria;
import solipingen.progressivearchery.item.arrows.KidArrowItem;
import solipingen.progressivearchery.item.arrows.ModArrowItem;
import solipingen.progressivearchery.sound.ModSoundEvents;


public class ModBowItem extends RangedWeaponItem {
    public static final Predicate<ItemStack> MOD_BOW_PROJECTILES = stack -> stack.getItem() instanceof ArrowItem || stack.getItem() instanceof ModArrowItem;
    public static final Predicate<ItemStack> TUBULAR_BOW_PROJECTILES = stack -> stack.getItem() instanceof KidArrowItem;
    public static final int RANGE = 24;
    private final BowMaterial material;
    private final int bowType;
    private final float maxReleaseSpeed;
    private final float divergence;
    private boolean pulled;


    public ModBowItem(BowMaterial material, int bowType, Item.Settings settings) {
        super(settings.maxDamage(MathHelper.ceil((bowType == 3 ? 0.85f : 1.0f + bowType*0.25f)*material.getDurability())));
        this.material = material;
        this.bowType = bowType;
        this.maxReleaseSpeed = 3.0f + 0.3f*(this.bowType >= 2 ? this.bowType + 1 : this.bowType) + 0.2f*this.material.getMiningLevel();
        this.divergence = 1.0f + (this.bowType >= 2 ? 0.25f*(4.0f - (float)this.bowType) : 0.0f) - 0.1f*this.material.getMiningLevel();
        this.pulled = false;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity playerEntity = (PlayerEntity)user;
        float f = this.getPullProgress(this.getMaxUseTime(stack) - remainingUseTicks, stack);
        ItemStack itemStack = this.getModArrowType(playerEntity, stack);
        boolean bl = playerEntity.getAbilities().creativeMode;
        if (itemStack.isEmpty() && !bl) {
            return;
        }
        else if (itemStack.isEmpty() && bl) {
            itemStack = new ItemStack(ModItems.WOODEN_ARROW);
            if (this.bowType == 3) {
                itemStack = new ItemStack(ModItems.WOODEN_KID_ARROW);
            }
        }
        if (f < 0.1f) {
            return;
        }
        List<ItemStack> list = ModBowItem.load(stack, itemStack, playerEntity);
        if (!world.isClient() && !list.isEmpty()) {
            int strengthLevel = user.hasStatusEffect(StatusEffects.STRENGTH) ? user.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
            int weaknessLevel = user.hasStatusEffect(StatusEffects.WEAKNESS) ? user.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
            this.shootAll(world, playerEntity, playerEntity.getActiveHand(), stack, list, f*this.maxReleaseSpeed + 0.3f*strengthLevel - 0.3f*weaknessLevel, this.divergence, f >= 1.0f, null);
            world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + f * 0.5f);
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

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
        projectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw() + yaw, 0.0f, speed, divergence);
    }

    protected void shootPitch(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float pitch, @Nullable LivingEntity target) {
        projectile.setVelocity(shooter, shooter.getPitch() + pitch, shooter.getYaw(), 0.0f, speed, divergence);
    }

    @Override
    protected void shootAll(World world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles, float speed, float divergence, boolean critical, @Nullable LivingEntity target) {
        float g = projectiles.size() == 1 ? 0.0f : 20.0f / (float)(projectiles.size() - 1);
        float h = (float)((projectiles.size() - 1)%2)*g / 2.0F;
        float i = 1.0f;
        for(int j = 0; j < projectiles.size(); ++j) {
            ItemStack itemStack = projectiles.get(j);
            if (!itemStack.isEmpty()) {
                float k = h + i*(float)((j+1)/2)*g;
                i = -i;
                stack.damage(this.getWeaponStackDamage(itemStack), shooter, LivingEntity.getSlotForHand(hand));
                ProjectileEntity projectileEntity = this.createArrowEntity(world, shooter, stack, itemStack, critical);
                this.shootPitch(shooter, projectileEntity, j, speed, divergence, k, target);
                world.spawnEntity(projectileEntity);
            }
        }
    }

    private ItemStack getModArrowType(PlayerEntity playerEntity, ItemStack stack) {
        if (!(stack.getItem() instanceof ModBowItem || stack.getItem() instanceof BowItem)) {
            return ItemStack.EMPTY;
        }
        Predicate<ItemStack> predicate = ((ModBowItem)stack.getItem()).getHeldProjectiles();
        if (!ModBowItem.getFilledQuiver(playerEntity).isEmpty()) {
            ItemStack quiverStack = ModBowItem.getFilledQuiver(playerEntity);
            Stream<ItemStack> quiverStream = QuiverItem.getStoredStacks(quiverStack);
            ItemStack itemStack3;
            if (this.bowType == 3) {
                itemStack3 = quiverStream.filter(item -> item.getItem() instanceof KidArrowItem).findFirst().orElse(ItemStack.EMPTY);
            }
            else {
                itemStack3 = quiverStream.filter(item -> (item.getItem() instanceof ArrowItem || item.getItem() instanceof ModArrowItem)).findFirst().orElse(ItemStack.EMPTY);
            }
            if (!itemStack3.isEmpty()) {
                return itemStack3.copyWithCount(1);
            }
        }
        ItemStack itemStack = ModBowItem.getHeldProjectile(playerEntity, predicate);
        if (!itemStack.isEmpty()) {
            return itemStack.copyWithCount(1);
        }
        predicate = ((ModBowItem)stack.getItem()).getProjectiles();
        for (int j = 0; j < playerEntity.getInventory().size(); ++j) {
            ItemStack itemStack2 = playerEntity.getInventory().getStack(j);
            if (predicate.test(itemStack2)) {
                return itemStack2.copyWithCount(1);
            }
        }
        return playerEntity.getAbilities().creativeMode ? (this.bowType == 3 ? new ItemStack(ModItems.WOODEN_KID_ARROW) : new ItemStack(ModItems.WOODEN_ARROW)) : ItemStack.EMPTY;
    }

    public static ItemStack getFilledQuiver(PlayerEntity playerEntity) {
        ItemStack itemStack = ItemStack.EMPTY;
        for (int i = 0; i < playerEntity.getInventory().size(); i++) {
            if (playerEntity.getInventory().getStack(i).getItem() instanceof QuiverItem && QuiverItem.getAmountFilled(playerEntity.getInventory().getStack(i)) > 0.0f) {
                itemStack = playerEntity.getInventory().getStack(i);
                break;
            }
        }
        return itemStack;
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

    public static float getVanillaBowPullProgress(int useTicks, ItemStack stack) {
        float maxPullTicks = 10.0f;
        int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
        float quickChargeModifier = 2.5f;
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

    public BowMaterial getMaterial() {
        return this.material;
    }

    public int getBowType() {
        return this.bowType;
    }

    public float getMaxReleaseSpeed() {
        return this.maxReleaseSpeed;
    }



}

