package solipingen.progressivearchery.mixin.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.progressivearchery.item.ModCrossbowItem;
import solipingen.progressivearchery.util.interfaces.mixin.entity.projectile.FireworkRocketEntityInterface;
import solipingen.progressivearchery.util.interfaces.mixin.server.network.ServerPlayerEntityInterface;

import java.util.List;
import java.util.function.Predicate;


@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin extends RangedWeaponItem {
    @Shadow private boolean charged;
    @Shadow private boolean loaded;


    public CrossbowItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "getHeldProjectiles", at = @At("HEAD"), cancellable = true)
    private void injectedGetHeldProjectiles(CallbackInfoReturnable<Predicate<ItemStack>> cbireturn) {
        cbireturn.setReturnValue(ModCrossbowItem.MOD_CROSSBOW_PROJECTILES);
    }

    @Inject(method = "getProjectiles", at = @At("HEAD"), cancellable = true)
    private void injectedGetProjectiles(CallbackInfoReturnable<Predicate<ItemStack>> cbireturn) {
        cbireturn.setReturnValue(ModCrossbowItem.MOD_CROSSBOW_PROJECTILES);
    }

    @Inject(method = "getPullTime", at = @At("HEAD"), cancellable = true)
    private static void injectedGetPullTime(ItemStack stack, LivingEntity user, CallbackInfoReturnable<Integer> cbireturn) {
        cbireturn.setReturnValue(ModCrossbowItem.getPullTime(stack, user));
    }

    @Inject(method = "getRange", at = @At("HEAD"), cancellable = true)
    private void injectedGetRange(CallbackInfoReturnable<Integer> cbireturn) {
        cbireturn.setReturnValue(ModCrossbowItem.RANGE);
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void injectedUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cbireturn) {
        ItemStack itemStack = user.getStackInHand(hand);
        ChargedProjectilesComponent chargedProjectilesComponent = itemStack.getOrDefault(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
        if (chargedProjectilesComponent != null && !chargedProjectilesComponent.isEmpty()) {
            ((CrossbowItem)(Object)this).shootAll(world, user, hand, itemStack, (chargedProjectilesComponent.contains(Items.FIREWORK_ROCKET) ? 2.8f : 3.6f), 0.75f, (LivingEntity)null);
            cbireturn.setReturnValue(ActionResult.CONSUME);
        }
        else if (!ModCrossbowItem.getModProjectileType(user, itemStack).isEmpty()) {
            this.charged = false;
            this.loaded = false;
            user.setCurrentHand(hand);
            cbireturn.setReturnValue(ActionResult.CONSUME);
        }
        else {
            cbireturn.setReturnValue(ActionResult.FAIL);
        }
    }

    @Inject(method = "onStoppedUsing", at = @At("HEAD"), cancellable = true)
    private void injectedOnStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfoReturnable<Boolean> cbireturn) {
        if (world instanceof ServerWorld) {
            int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
            if (user instanceof ServerPlayerEntity serverPlayerEntity) {
                if (((ServerPlayerEntityInterface)serverPlayerEntity).getMultishotKilledEntities() != null && !((ServerPlayerEntityInterface) serverPlayerEntity).getMultishotKilledEntities().isEmpty()) {
                    ((ServerPlayerEntityInterface)serverPlayerEntity).clearMultishotKilledEntities();
                }
            }
            cbireturn.setReturnValue(CrossbowItemMixin.getPullProgress(i, stack, user) >= 1.0f && ModCrossbowItem.isCharged(stack));
        }
        else {
            cbireturn.setReturnValue(false);
        }
    }

    @ModifyConstant(method = "shoot", constant = @Constant(doubleValue = 0.20000000298023224))
    private double modifiedShootDirection(double originalD) {
        return 0.10;
    }

    @Inject(method = "createArrowEntity", at = @At("HEAD"), cancellable = true)
    private void injectedCreateArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical, CallbackInfoReturnable<ProjectileEntity> cbireturn) {
        ProjectileEntity projectileEntity;
        if (projectileStack.isOf(Items.FIREWORK_ROCKET)) {
            RegistryEntryLookup<Enchantment> enchantmentLookup = world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
            projectileEntity = new FireworkRocketEntity(world, projectileStack, shooter, shooter.getX(), shooter.getEyeY() - 0.15, shooter.getZ(), true);
            ((FireworkRocketEntityInterface)projectileEntity).setPower(EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.POWER), weaponStack));
            ((FireworkRocketEntityInterface)projectileEntity).setPunch(EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.PUNCH), weaponStack));
            ((FireworkRocketEntityInterface)projectileEntity).setFlame(EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.FLAME), weaponStack) > 0);
        }
        else {
            projectileEntity = super.createArrowEntity(world, shooter, weaponStack, projectileStack, critical);
        }
        cbireturn.setReturnValue(projectileEntity);
    }

    @Inject(method = "getSpeed", at = @At("HEAD"), cancellable = true)
    private static void injectedGetSpeed(ChargedProjectilesComponent stack, CallbackInfoReturnable<Float> cbireturn) {
        cbireturn.setReturnValue(stack.contains(Items.FIREWORK_ROCKET) ? 2.8f : 3.6f);
    }

    @Inject(method = "loadProjectiles", at = @At("HEAD"), cancellable = true)
    private static void injectedLoadProjectiles(LivingEntity shooter, ItemStack crossbow, CallbackInfoReturnable<Boolean> cbireturn) {
        cbireturn.setReturnValue(CrossbowItemMixin.loadModProjectiles(shooter, crossbow));
    }

    @Unique
    private static boolean loadModProjectiles(LivingEntity shooter, ItemStack crossbow) {
        List<ItemStack> list = CrossbowItem.load(crossbow, ModCrossbowItem.getModProjectileType(shooter, crossbow), shooter);
        if (!list.isEmpty()) {
            crossbow.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.of(list));
            return true;
        }
        else {
            return false;
        }
    }

    @Unique
    private static float getPullProgress(int useTicks, ItemStack stack, LivingEntity user) {
        float f = (float)useTicks / (float)ModCrossbowItem.getPullTime(stack, user);
        if (f > 1.0f) {
            f = 1.0f;
        }
        return f;
    }





}
