package solipingen.progressivearchery.mixin.item;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.progressivearchery.advancement.criterion.ModCriteria;
import solipingen.progressivearchery.item.ModBowItem;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.item.QuiverItem;
import solipingen.progressivearchery.item.arrows.ModArrowItem;
import solipingen.progressivearchery.sound.ModSoundEvents;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;


@Mixin(BowItem.class)
public abstract class BowItemMixin extends RangedWeaponItem {
    private boolean pulled;


    public BowItemMixin(Settings settings) {
        super(settings);
    }

//    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
//    private static Item.Settings modifiedInit(Item.Settings settings) {
//        return settings.maxDamage(184);
//    }

    @Inject(method = "onStoppedUsing", at = @At("HEAD"), cancellable = true)
    private void injectedOnStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo cbi) {
        if (!(user instanceof PlayerEntity)) {
            cbi.cancel();
        }
        PlayerEntity playerEntity = (PlayerEntity)user;
        float f = this.getModPullProgress(this.getMaxUseTime(stack, user) - remainingUseTicks, stack, world);
        ItemStack itemStack = this.getModArrowType(playerEntity, stack);
        boolean bl = playerEntity.getAbilities().creativeMode;
        if (itemStack.isEmpty() && !bl) {
            cbi.cancel();
        }
        else if (itemStack.isEmpty() && bl) {
            itemStack = new ItemStack(ModItems.WOODEN_ARROW);
        }
        if (f < 0.1f) {
            cbi.cancel();
        }
        List<ItemStack> list = ModBowItem.load(stack, itemStack, playerEntity);
        if (!world.isClient() && world instanceof ServerWorld serverWorld && !list.isEmpty()) {
            int strengthLevel = user.hasStatusEffect(StatusEffects.STRENGTH) ? user.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
            int weaknessLevel = user.hasStatusEffect(StatusEffects.WEAKNESS) ? user.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
            this.shootAll(serverWorld, playerEntity, playerEntity.getActiveHand(), stack, list, f*3.0f + 0.3f*strengthLevel - 0.3f*weaknessLevel, 1.0f, f >= 1.0f, null);
            world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + f * 0.5f);
            if (playerEntity instanceof ServerPlayerEntity) {
                ModCriteria.SHOT_BOW.trigger((ServerPlayerEntity)playerEntity, stack);
            }
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        cbi.cancel();
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void injectedUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cbireturn) {
        ItemStack itemStack = user.getStackInHand(hand);
        boolean bl = !this.getModArrowType(user, itemStack).isEmpty();
        if (user.getAbilities().creativeMode || bl) {
            user.setCurrentHand(hand);
            cbireturn.setReturnValue(TypedActionResult.consume(itemStack));
        }
        cbireturn.setReturnValue(TypedActionResult.fail(itemStack));
    }

    @Inject(method = "getProjectiles", at = @At("HEAD"), cancellable = true)
    private void injectedGetProjectiles(CallbackInfoReturnable<Predicate<ItemStack>> cbireturn) {
        cbireturn.setReturnValue(ModBowItem.MOD_BOW_PROJECTILES);
    }

    @Inject(method = "getRange", at = @At("HEAD"), cancellable = true)
    private void injectedGetRange(CallbackInfoReturnable<Integer> cbireturn) {
        cbireturn.setReturnValue(ModBowItem.RANGE);
    }

    @Override
    public int getEnchantability() {
        return 15;
    }

    @Override
    protected void shootAll(ServerWorld world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles, float speed, float divergence, boolean critical, @Nullable LivingEntity target) {
        float g = projectiles.size() == 1 ? 0.0f : 20.0f / (float)(projectiles.size() - 1);
        float h = (float)((projectiles.size() - 1)%2)*g / 2.0f;
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

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient()) {
            RegistryEntryLookup<Enchantment> enchantmentLookup = world.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            int i = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.QUICK_CHARGE), stack);
            SoundEvent soundEvent = this.getQuickChargeSound(i);
            float f = this.getModPullProgress(this.getMaxUseTime(stack, user) - remainingUseTicks, stack, world);
            if (f < 0.1f) {
                this.pulled = false;
            }
            if (f >= 0.1f && !this.pulled) {
                world.playSound(null, user.getBlockPos(), soundEvent, SoundCategory.PLAYERS, 0.5f, 1.0f);
                this.pulled = true;
            }
        }
    }

    @Unique
    private void shootPitch(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float pitch, @Nullable LivingEntity target) {
        projectile.setVelocity(shooter, shooter.getPitch() + pitch, shooter.getYaw(), 0.0f, speed, divergence);
    }

    @Unique
    private SoundEvent getQuickChargeSound(int stage) {
        switch (stage) {
            case 1: {
                return ModSoundEvents.BOW_PULL_QUICK_CHARGE_1.value();
            }
            case 2: {
                return ModSoundEvents.BOW_PULL_QUICK_CHARGE_2.value();
            }
            case 3: {
                return ModSoundEvents.BOW_PULL_QUICK_CHARGE_3.value();
            }
        }
        return ModSoundEvents.BOW_PULL;
    }

    @Unique
    private ItemStack getModArrowType(PlayerEntity playerEntity, ItemStack stack) {
        if (!ModBowItem.getFilledQuiver(playerEntity).isEmpty()) {
            ItemStack quiverStack = ModBowItem.getFilledQuiver(playerEntity);
            Stream<ItemStack> quiverStream = QuiverItem.getStoredStacks(quiverStack);
            ItemStack itemStack3 = quiverStream.filter(item -> (item.getItem() instanceof ArrowItem || item.getItem() instanceof ModArrowItem)).findFirst().orElse(ItemStack.EMPTY);
            if (!itemStack3.isEmpty()) {
                return itemStack3.copyWithCount(1);
            }
        }
        Predicate<ItemStack> predicate = ((BowItem)stack.getItem()).getHeldProjectiles();
        ItemStack itemStack = ModBowItem.getHeldProjectile(playerEntity, predicate);
        if (!itemStack.isEmpty()) {
            return itemStack.copyWithCount(1);
        }
        predicate = ((BowItem)stack.getItem()).getProjectiles();
        for (int j = 0; j < playerEntity.getInventory().size(); ++j) {
            ItemStack itemStack2 = playerEntity.getInventory().getStack(j);
            if (predicate.test(itemStack2)) {
                return itemStack2.copyWithCount(1);
            }
        }
        return playerEntity.getAbilities().creativeMode ? new ItemStack(ModItems.WOODEN_ARROW) : ItemStack.EMPTY;
    }

    @Unique
    private float getModPullProgress(int useTicks, ItemStack stack, World world) {
        float maxPullTicks = 10.0f;
        RegistryEntryLookup<Enchantment> enchantmentLookup = world.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
        int i = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.QUICK_CHARGE), stack);
        float quickChargeModifier = 2.5f;
        float f = (float)useTicks / (maxPullTicks -  quickChargeModifier * i);
        if ((f = (f * f + f * 2.0f) / 3.0f) > 1.0f) {
            f = 1.0f;
        }
        return f;
    }




}
