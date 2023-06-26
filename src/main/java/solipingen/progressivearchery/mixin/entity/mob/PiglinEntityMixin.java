package solipingen.progressivearchery.mixin.entity.mob;

import net.minecraft.item.*;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.PiglinActivity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import solipingen.progressivearchery.item.ModCrossbowItem;
import solipingen.progressivearchery.item.ModItems;


@Mixin(PiglinEntity.class)
public abstract class PiglinEntityMixin extends AbstractPiglinEntity implements CrossbowUser, InventoryOwner {
    @Shadow @Final private static TrackedData<Boolean> CHARGING;


    public PiglinEntityMixin(EntityType<? extends AbstractPiglinEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initialize", at = @At("TAIL"), cancellable = true)
    private void injectedInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cbireturn) {
        if (spawnReason == SpawnReason.STRUCTURE) {
            if (this.getMainHandStack().isOf(Items.CROSSBOW)) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GOLD_FUSED_CROSSBOW));
            }
            this.updateEnchantments(this.random, difficulty);
        }
    }

    @ModifyConstant(method = "equipAtChance", constant = @Constant(floatValue = 0.1f))
    private float injectedEquipAtChance(float f, EquipmentSlot slot, ItemStack stack, Random random) {
        return 0.1f*this.getWorld().getDifficulty().getId();
    }

    @Redirect(method = "makeInitialWeapon", at = @At(value = "FIELD", target = "Lnet/minecraft/item/Items;CROSSBOW:Lnet/minecraft/item/Item;", opcode = Opcodes.GETSTATIC))
    private Item redirectedMakeInitialWeapon() {
        if (this.random.nextFloat() > 0.67f) {
            return ModItems.WOODEN_CROSSBOW;
        }
        return ModItems.GOLD_FUSED_CROSSBOW;
    }

    @Inject(method = "getActivity", at = @At("TAIL"), cancellable = true)
    private void injectedGetActivity(CallbackInfoReturnable<PiglinActivity> cbireturn) {
        if (this.isAttacking() && (this.isHolding(ModItems.WOODEN_CROSSBOW) || this.isHolding(ModItems.GOLD_FUSED_CROSSBOW))) {
            cbireturn.setReturnValue(PiglinActivity.CROSSBOW_HOLD);
        }
        else {
            cbireturn.setReturnValue(PiglinActivity.DEFAULT);
        }
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void injectedAttack(LivingEntity target, float pullProgress, CallbackInfo cbi) {
        ItemStack handStack = this.getMainHandStack();
        int difficultyId = this.getWorld().getDifficulty().getId();
        if (handStack.isEmpty()) {
            handStack = this.getOffHandStack();
            if (handStack.isEmpty()) {
                cbi.cancel();
            }
        }
        if (handStack.isOf(ModItems.WOODEN_CROSSBOW)) {
            this.shoot(this, 3.6f - 0.6f*(3 - difficultyId));
            cbi.cancel();
        }
        else if (handStack.isOf(ModItems.GOLD_FUSED_CROSSBOW)) {
            this.shoot(this, 3.7f - 0.6f*(3 - difficultyId));
            cbi.cancel();
        }
    }

    @Inject(method = "shoot", at = @At("HEAD"), cancellable = true)
    private void injectedShoot(LivingEntity target, ItemStack crossbow, ProjectileEntity projectile, float multiShotSpray, CallbackInfo cbi) {
        int difficultyId = this.getWorld().getDifficulty().getId();
        if (crossbow.isOf(ModItems.WOODEN_CROSSBOW)) {
            this.shoot(this, target, projectile, multiShotSpray, 3.6f - 0.6f*(3 - difficultyId));
            cbi.cancel();
        }
        else if (crossbow.isOf(ModItems.GOLD_FUSED_CROSSBOW)) {
            this.shoot(this, target, projectile, multiShotSpray, 3.7f - 0.6f*(3 - difficultyId));
            cbi.cancel();
        }
    }

    @Inject(method = "prefersNewEquipment", at = @At("HEAD"), cancellable = true)
    private void injectedPrefersNewEquipment(ItemStack newStack, ItemStack oldStack, CallbackInfoReturnable<Boolean> cbireturn) {
        if (EnchantmentHelper.hasBindingCurse(oldStack)) {
            cbireturn.setReturnValue(false);
        }
        boolean bl = newStack.isIn(ItemTags.PIGLIN_LOVED);
        boolean bl2 = oldStack.isIn(ItemTags.PIGLIN_LOVED);
        if (bl && !bl2) {
            cbireturn.setReturnValue(true);
        }
        if (!bl && bl2) {
            cbireturn.setReturnValue(false);
        }
        if ((this.isAdult() && !newStack.isOf(ModItems.GOLD_FUSED_CROSSBOW) && oldStack.isOf(ModItems.GOLD_FUSED_CROSSBOW))) {
            cbireturn.setReturnValue(false);
        }
        else {
            cbireturn.setReturnValue(super.prefersNewEquipment(newStack, oldStack));
        }
    }

    @Inject(method = "canUseRangedWeapon", at = @At("HEAD"), cancellable = true)
    private void injectedCanUseRangedWeapon(RangedWeaponItem weapon, CallbackInfoReturnable<Boolean> cbireturn) {
        cbireturn.setReturnValue(weapon instanceof CrossbowItem || weapon instanceof ModCrossbowItem);
    }


    
}
