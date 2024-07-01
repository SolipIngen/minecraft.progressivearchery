package solipingen.progressivearchery.mixin.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Unit;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.progressivearchery.component.QuiverContentsComponent;
import solipingen.progressivearchery.component.type.ModDataComponentTypes;
import solipingen.progressivearchery.item.ModBowItem;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.item.arrows.KidArrowItem;
import solipingen.progressivearchery.item.arrows.ModArrowItem;
import solipingen.progressivearchery.registry.tag.ModItemTags;

import java.util.List;


@Mixin(RangedWeaponItem.class)
public abstract class RangedWeaponItemMixin extends Item {


    public RangedWeaponItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "createArrowEntity", at = @At("HEAD"), cancellable = true)
    private void injectedCreateArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical, CallbackInfoReturnable<ProjectileEntity> cbireturn) {
        Item item = projectileStack.getItem();
        PersistentProjectileEntity persistentProjectileEntity;
        switch (item) {
            case ModArrowItem modArrowItem ->
                    persistentProjectileEntity = modArrowItem.createModArrow(world, projectileStack, shooter, weaponStack);
            case KidArrowItem kidArrowItem ->
                    persistentProjectileEntity = kidArrowItem.createKidArrow(world, projectileStack, shooter, weaponStack);
            case ArrowItem arrowItem ->
                    persistentProjectileEntity = arrowItem.createArrow(world, projectileStack, shooter, weaponStack);
            default -> {
                if (weaponStack.getItem() instanceof ModBowItem modBowItem && modBowItem.getBowType() == 3) {
                    persistentProjectileEntity = ((KidArrowItem) ModItems.WOODEN_KID_ARROW).createKidArrow(world, projectileStack, shooter, weaponStack);
                }
                else {
                    persistentProjectileEntity = ((ModArrowItem) ModItems.WOODEN_ARROW).createModArrow(world, projectileStack, shooter, weaponStack);
                }
            }
        }
        if (critical) {
            persistentProjectileEntity.setCritical(true);
        }
        cbireturn.setReturnValue(persistentProjectileEntity);
    }

    @Inject(method = "getProjectile", at = @At("HEAD"), cancellable = true)
    private static void injectedGetProjectile(ItemStack weaponStack, ItemStack projectileStack, LivingEntity shooter, boolean multishot, CallbackInfoReturnable<ItemStack> cbireturn) {
        if (shooter instanceof PlayerEntity playerEntity) {
            RegistryEntryLookup<Enchantment> enchantmentLookup = shooter.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            ItemStack quiverItemStack = ModBowItem.getFilledQuiver(playerEntity);
            boolean bl2 = playerEntity.getAbilities().creativeMode;
            float randomf = playerEntity.getRandom().nextFloat();
            if (EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.INFINITY), quiverItemStack) > 0) {
                if (projectileStack.isIn(ModItemTags.INFINITY_100P)) {
                    bl2 |= randomf <= 1.0f;
                }
                else if (projectileStack.isIn(ModItemTags.INFINITY_75P)) {
                    bl2 |= randomf <= 0.75f;
                }
                else if (projectileStack.isIn(ModItemTags.INFINITY_50P)) {
                    bl2 |= randomf <= 0.5f;
                }
                else if (projectileStack.isIn(ModItemTags.INFINITY_25P)) {
                    bl2 |= randomf <= 0.25f;
                }
                else if (projectileStack.isIn(ModItemTags.INFINITY_12p5P)) {
                    bl2 |= randomf <= 0.125f;
                }
                else if (projectileStack.isOf(Items.FIREWORK_ROCKET) && projectileStack.get(DataComponentTypes.FIREWORKS).explosions() != null) {
                    List<FireworkExplosionComponent> explosionComponents = projectileStack.get(DataComponentTypes.FIREWORKS).explosions();
                    bl2 |= randomf > 0.125f*explosionComponents.size();
                }
            }
            boolean bl = !multishot && !bl2;
            ItemStack itemStack;
            if (!bl) {
                itemStack = projectileStack.copyWithCount(1);
                itemStack.set(DataComponentTypes.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
                cbireturn.setReturnValue(itemStack);
            }
            else {
                if (!quiverItemStack.isEmpty()) {
                    QuiverContentsComponent quiverContentsComponent = quiverItemStack.getOrDefault(ModDataComponentTypes.QUIVER_CONTENTS, QuiverContentsComponent.DEFAULT);
                    for (ItemStack storedStack : quiverContentsComponent.iterate()) {
                        if (ItemStack.areItemsAndComponentsEqual(storedStack, projectileStack)) {
                            QuiverContentsComponent.Builder builder = new QuiverContentsComponent.Builder(quiverContentsComponent);
                            int index = builder.getItemIndex(storedStack);
                            itemStack = storedStack.copy();
                            itemStack.decrement(1);
                            builder.set(index, itemStack);
                            quiverItemStack.set(ModDataComponentTypes.QUIVER_CONTENTS, builder.build());
                            cbireturn.setReturnValue(itemStack);
                        }
                    }
                }
                else {
                    itemStack = projectileStack.split(1);
                    if (projectileStack.isEmpty()) {
                        playerEntity.getInventory().removeOne(projectileStack);
                    }
                    cbireturn.setReturnValue(itemStack);
                }
            }
        }
    }



}
