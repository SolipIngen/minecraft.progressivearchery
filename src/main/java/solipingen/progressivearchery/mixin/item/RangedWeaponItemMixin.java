package solipingen.progressivearchery.mixin.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.util.Unit;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

import java.util.ArrayList;
import java.util.List;


@Mixin(RangedWeaponItem.class)
public abstract class RangedWeaponItemMixin extends Item {

    @Shadow
    protected static ItemStack getProjectile(ItemStack weaponStack, ItemStack projectileStack, LivingEntity shooter, boolean multishot) {
        throw new AssertionError();
    }


    public RangedWeaponItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "createArrowEntity", at = @At("HEAD"), cancellable = true)
    private void injectedCreateArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical, CallbackInfoReturnable<ProjectileEntity> cbireturn) {
        Item item = projectileStack.getItem();
        PersistentProjectileEntity persistentProjectileEntity;
        switch (item) {
            case ModArrowItem modArrowItem ->
                    persistentProjectileEntity = modArrowItem.createModArrow(world, projectileStack, shooter);
            case KidArrowItem kidArrowItem ->
                    persistentProjectileEntity = kidArrowItem.createKidArrow(world, projectileStack, shooter);
            case ArrowItem arrowItem ->
                    persistentProjectileEntity = arrowItem.createArrow(world, projectileStack, shooter);
            default -> {
                if (weaponStack.getItem() instanceof ModBowItem modBowItem && modBowItem.getBowType() == 3) {
                    persistentProjectileEntity = ((KidArrowItem) ModItems.WOODEN_KID_ARROW).createKidArrow(world, projectileStack, shooter);
                }
                else {
                    persistentProjectileEntity = ((ModArrowItem) ModItems.WOODEN_ARROW).createModArrow(world, projectileStack, shooter);
                }
            }
        }
        if (critical) {
            persistentProjectileEntity.setCritical(true);
        }
        int i = EnchantmentHelper.getLevel(Enchantments.POWER, weaponStack);
        if (i > 0) {
            persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() + 0.5*i);
        }
        int j = EnchantmentHelper.getLevel(Enchantments.PUNCH, weaponStack);
        if (j > 0) {
            persistentProjectileEntity.setPunch(j);
        }
        if (EnchantmentHelper.getLevel(Enchantments.FLAME, weaponStack) > 0) {
            persistentProjectileEntity.setOnFireFor(100);
        }
        int k = EnchantmentHelper.getLevel(Enchantments.PIERCING, weaponStack);
        if (k > 0) {
            persistentProjectileEntity.setPierceLevel((byte)k);
        }
        cbireturn.setReturnValue(persistentProjectileEntity);
    }

    @Inject(method = "load", at = @At("HEAD"), cancellable = true)
    private static void injetcedLoad(ItemStack weaponStack, ItemStack projectileStack, LivingEntity shooter, CallbackInfoReturnable<List<ItemStack>> cbireturn) {
        if (projectileStack.isEmpty()) {
            cbireturn.setReturnValue(List.of());
        }
        else {
            int i = EnchantmentHelper.getLevel(Enchantments.MULTISHOT, weaponStack);
            int j = 2*i + 1;
            List<ItemStack> list = new ArrayList(j);
            ItemStack itemStack = projectileStack.copy();
            for(int k = 0; k < j; ++k) {
                list.add(getProjectile(weaponStack, k == 0 ? projectileStack : itemStack, shooter, k > 0));
            }
            cbireturn.setReturnValue(list);
        }
    }

    @Inject(method = "getProjectile", at = @At("HEAD"), cancellable = true)
    private static void injectedGetProjectile(ItemStack weaponStack, ItemStack projectileStack, LivingEntity shooter, boolean multishot, CallbackInfoReturnable<ItemStack> cbireturn) {
        if (shooter instanceof PlayerEntity playerEntity) {
            ItemStack quiverItemStack = ModBowItem.getFilledQuiver(playerEntity);
            boolean bl2 = playerEntity.getAbilities().creativeMode;
            float randomf = playerEntity.getRandom().nextFloat();
            if (EnchantmentHelper.getLevel(Enchantments.INFINITY, quiverItemStack) > 0) {
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
