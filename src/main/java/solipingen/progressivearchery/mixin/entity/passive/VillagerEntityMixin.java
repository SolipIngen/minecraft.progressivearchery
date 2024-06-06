package solipingen.progressivearchery.mixin.entity.passive;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityInteraction;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.InteractionObserver;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.CelebrateRaidWinTask;
import net.minecraft.entity.ai.brain.task.EndRaidTask;
import net.minecraft.entity.ai.brain.task.FindWalkTargetTask;
import net.minecraft.entity.ai.brain.task.LookAtMobTask;
import net.minecraft.entity.ai.brain.task.RandomTask;
import net.minecraft.entity.ai.brain.task.SeekSkyTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.entity.ai.brain.task.Tasks;
import net.minecraft.entity.ai.brain.task.VillagerTaskListProvider;
import net.minecraft.entity.ai.brain.task.VillagerWalkTowardsTask;
import net.minecraft.entity.ai.brain.task.WaitTask;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.TradeOffers.Factory;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import solipingen.progressivearchery.entity.ai.goal.PassiveBowAttackGoal;
import solipingen.progressivearchery.entity.ai.goal.VillagerTrackTargetGoal;
import solipingen.progressivearchery.entity.passive.AngerableArcherVillager;
import solipingen.progressivearchery.entity.projectile.kid_arrow.GoldenKidArrowEntity;
import solipingen.progressivearchery.entity.projectile.kid_arrow.KidArrowEntity;
import solipingen.progressivearchery.item.ModBowItem;
import solipingen.progressivearchery.item.ModItems;
import solipingen.progressivearchery.item.arrows.KidArrowItem;
import solipingen.progressivearchery.sound.ModSoundEvents;
import solipingen.progressivearchery.village.ModVillagerProfessions;


@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntity implements AngerableArcherVillager, InteractionObserver, RangedAttackMob, VillagerDataContainer {
    private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(60, 90);
    private int angerTime;
    @Nullable
    private UUID angryAt;
    private final VillagerTrackTargetGoal villagerTrackTargetGoal = new VillagerTrackTargetGoal((VillagerEntity)(Object)this);
    private final PassiveBowAttackGoal<VillagerEntity> bowAttackGoal = new PassiveBowAttackGoal<VillagerEntity>((VillagerEntity)(Object)this, 0.67, 20, 24.0f);

    
    public VillagerEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "reinitializeBrain", at = @At("TAIL"))
    private void injectedReinitializeBrain(ServerWorld world, CallbackInfo cbi) {
        VillagerProfession villagerProfession = this.getVillagerData().getProfession();
        if (villagerProfession == ModVillagerProfessions.ARCHER) {
            this.initArcherGoals();
            if (this.getMainHandStack().isEmpty()) {
                float randomf = 0.75f*this.getRandom().nextFloat() + 0.25f*world.getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty();
                if (randomf >= 0.4f && randomf < 0.7f) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.HORN_BOW));
                }
                else if (randomf >= 0.7f && randomf < 0.9f) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.LONGBOW));
                }
                else if (randomf >= 0.9f) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.TUBULAR_BOW));
                }
                else {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
                }
            }
        }
    }

    @Inject(method = "initBrain", at = @At("HEAD"), cancellable = true)
    private void injectedSetTaskList(Brain<VillagerEntity> brain, CallbackInfo cbi) {
        VillagerProfession villagerProfession = this.getVillagerData().getProfession();
        if (villagerProfession == ModVillagerProfessions.ARCHER) {
            brain.setSchedule(ModVillagerProfessions.VILLAGER_SCHEDULE_ARCHER);
            brain.setTaskList(Activity.WORK, VillagerTaskListProvider.createWorkTasks(villagerProfession, 0.5f), ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryModuleState.VALUE_PRESENT)));
            brain.setTaskList(Activity.CORE, VillagerTaskListProvider.createCoreTasks(villagerProfession, 0.5f));
            brain.setTaskList(Activity.MEET, VillagerTaskListProvider.createMeetTasks(villagerProfession, 0.5f), ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryModuleState.VALUE_PRESENT)));
            if (this.getArcherTarget() == null && (this.random.nextInt(4) > this.getWorld().getDifficulty().getId() - 1 || this.getHealth() <= this.getMaxHealth()/2.0f)) {
                brain.setTaskList(Activity.REST, VillagerTaskListProvider.createRestTasks(villagerProfession, 0.5f));
            }
            if (this.getArcherTarget() == null || (this.getArcherTarget() != null && !this.getArcherTarget().isAlive())) {
                brain.setTaskList(Activity.IDLE, VillagerTaskListProvider.createIdleTasks(villagerProfession, 0.5f));
            }
            brain.setTaskList(Activity.PRE_RAID, VillagerTaskListProvider.createPreRaidTasks(villagerProfession, 0.5f));
            if (this.getWorld() instanceof ServerWorld) {
                brain.setTaskList(Activity.RAID, VillagerEntityMixin.createArcherRaidTasks((ServerWorld)this.getWorld(), ((VillagerEntity)(Object)this), 0.67f));
            }
            brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
            brain.setDefaultActivity(Activity.IDLE);
            brain.doExclusively(Activity.IDLE);
            brain.refreshActivities(this.getWorld().getTimeOfDay(), this.getWorld().getTime());
            cbi.cancel();
        }
    }

    private void initArcherGoals() {
        super.initGoals();
        World world = this.getWorld();
        if (world == null || world.isClient) {
            return;
        }
        this.goalSelector.remove(this.bowAttackGoal);
        Item mainHandItem = this.getMainHandStack().getItem();
        int level = this.getVillagerData().getLevel();
        if (mainHandItem instanceof ModBowItem) {
            int i = 20 + 5*((ModBowItem)mainHandItem).getBowType() - (2 + ((ModBowItem)mainHandItem).getBowType())*(level - 1);
            if (world.getDifficulty() != Difficulty.HARD) {
                i = 40 + 5*((ModBowItem)mainHandItem).getBowType() - (4 + ((ModBowItem)mainHandItem).getBowType())*(level - 1);
            }
            this.bowAttackGoal.setAttackInterval(i);
            this.goalSelector.add(4, this.bowAttackGoal);
        }
        else if (mainHandItem instanceof BowItem) {
            int i = 20 - 2*(level - 1);
            if (world.getDifficulty() != Difficulty.HARD) {
                i = 40 - 4*(level - 1);
            }
            this.bowAttackGoal.setAttackInterval(i);
            this.goalSelector.add(4, this.bowAttackGoal);
        }
        this.targetSelector.add(1, this.villagerTrackTargetGoal);
        this.targetSelector.add(2, new RevengeGoal(this, MerchantEntity.class, IronGolemEntity.class).setGroupRevenge(new Class[0]));
        this.targetSelector.add(3, new ActiveTargetGoal<PlayerEntity>((MobEntity)this, PlayerEntity.class, false, this::shouldArcherAngerAt));
        this.targetSelector.add(3, new ActiveTargetGoal<RaiderEntity>((MobEntity)this, RaiderEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<ZombieEntity>((MobEntity)this, ZombieEntity.class, true, this::shouldBeTargetedMob));
        this.targetSelector.add(3, new ActiveTargetGoal<VexEntity>((MobEntity)this, VexEntity.class, false, this::shouldBeTargetedMob));
    }

    private static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> createArcherRaidTasks(ServerWorld world, VillagerEntity villager, float speed) {
        return ImmutableList.of(Pair.of(0, TaskTriggerer.runIf(TaskTriggerer.predicate(VillagerEntityMixin::wonRaid), Tasks.pickRandomly(ImmutableList.of(Pair.of(SeekSkyTask.create(speed), 5), Pair.of(FindWalkTargetTask.create(speed * 1.1f), 2))))), 
            Pair.of(0, new CelebrateRaidWinTask(600, 600)), 
            Pair.of(2, TaskTriggerer.runIf(TaskTriggerer.predicate(VillagerEntityMixin::hasActiveRaid), VillagerWalkTowardsTask.create(MemoryModuleType.JOB_SITE, speed, 1, 100, 1200))), 
            VillagerEntityMixin.createBusyFollowTask(), Pair.of(99, EndRaidTask.create()));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Pair<Integer, Task<LivingEntity>> createBusyFollowTask() {
        return Pair.of(5, new RandomTask(ImmutableList.of(Pair.of(LookAtMobTask.create(EntityType.VILLAGER, 8.0f), 2), Pair.of(LookAtMobTask.create(EntityType.PLAYER, 8.0f), 2), Pair.of(new WaitTask(30, 60), 8))));
    }

    private static boolean hasActiveRaid(ServerWorld world, LivingEntity entity) {
        Raid raid = world.getRaidAt(entity.getBlockPos());
        return raid != null && raid.isActive() && !raid.hasWon() && !raid.hasLost();
    }

    private static boolean wonRaid(ServerWorld world, LivingEntity livingEntity) {
        Raid raid = world.getRaidAt(livingEntity.getBlockPos());
        return raid != null && raid.hasWon();
    }

    @Override
    public void shootAt(LivingEntity target, float pullProgress) {
        ItemStack itemStack = this.getMainHandStack();
        if (itemStack.isEmpty()) {
            itemStack = this.getOffHandStack();
            if (itemStack.isEmpty()) {
                return;
            }
        }
        ItemStack arrowStack = this.getProjectileType(itemStack);
        PersistentProjectileEntity persistentProjectileEntity = arrowStack.getItem() instanceof KidArrowItem ? this.createKidArrowProjectile(arrowStack, pullProgress) : this.createArrowProjectile(arrowStack, pullProgress);
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333) - persistentProjectileEntity.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        int difficultyLevel = this.getWorld().getDifficulty().getId();
        int level = this.getVillagerData().getLevel();
        int strengthLevel = this.hasStatusEffect(StatusEffects.STRENGTH) ? this.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
        int weaknessLevel = this.hasStatusEffect(StatusEffects.WEAKNESS) ? this.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
        if (itemStack.isOf(Items.BOW)) {
            persistentProjectileEntity.setVelocity(d, e + g*0.15, f, 3.0f + 0.2f*level + 0.15f*strengthLevel - 0.15f*weaknessLevel - 0.3f*(3 - difficultyLevel), 11.0f - difficultyLevel * 3);
            this.getWorld().spawnEntity(persistentProjectileEntity);
            this.playSound(ModSoundEvents.VILLAGER_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        }
        if (itemStack.getItem() instanceof ModBowItem) {
            float releaseSpeed = ((ModBowItem)itemStack.getItem()).getMaxReleaseSpeed() + 0.15f*strengthLevel - 0.15f*weaknessLevel;;
            if (((ModBowItem)itemStack.getItem()).getBowType() == 3) {
                persistentProjectileEntity = this.createKidArrowProjectile(itemStack, pullProgress);
            }
            persistentProjectileEntity.setVelocity(d, e + g*(0.15 - 0.005*(((ModBowItem)itemStack.getItem()).getBowType() + ((ModBowItem)itemStack.getItem()).getMaterial().getMiningLevel())), f, releaseSpeed + 0.2f*level - 0.3f*(3 - difficultyLevel), 10.5f - difficultyLevel * 3);
            this.getWorld().spawnEntity(persistentProjectileEntity);
            this.playSound(ModSoundEvents.VILLAGER_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        }
    }

    @Override
    public ItemStack getProjectileType(ItemStack stack) {
        Predicate<ItemStack> predicate = ((RangedWeaponItem)stack.getItem()).getHeldProjectiles();
        ItemStack itemStack = RangedWeaponItem.getHeldProjectile(this, predicate);
        int level = this.getVillagerData().getLevel();
        if (stack.getItem() instanceof ModBowItem && ((ModBowItem)stack.getItem()).getBowType() == 3) {
            if (itemStack.isEmpty()) {
                if (level == 2) {
                    return new ItemStack(ModItems.COPPER_KID_ARROW);
                }
                else if (level == 3) {
                    return new ItemStack(ModItems.GOLDEN_KID_ARROW);
                }
                else if (level == 4) {
                    return new ItemStack(ModItems.IRON_KID_ARROW);
                }
                else if (level == 5) {
                    return new ItemStack(ModItems.DIAMOND_KID_ARROW);
                }
                return new ItemStack(ModItems.FLINT_KID_ARROW);
            }
            return itemStack;
        }
        else {
            if (itemStack.isEmpty()) {
                if (level == 2) {
                    return new ItemStack(ModItems.COPPER_ARROW);
                }
                else if (level == 3) {
                    return new ItemStack(ModItems.GOLDEN_ARROW);
                }
                else if (level == 4) {
                    return new ItemStack(ModItems.IRON_ARROW);
                }
                else if (level == 5) {
                    return new ItemStack(ModItems.DIAMOND_ARROW);
                }
                return new ItemStack(ModItems.FLINT_ARROW);
            }
            return itemStack;
        }
    }

    private PersistentProjectileEntity createArrowProjectile(ItemStack arrow, float damageModifier) {
        return ProjectileUtil.createArrowProjectile(this, arrow, damageModifier);
    }

    private KidArrowEntity createKidArrowProjectile(ItemStack stack, float damageModifier) {
        KidArrowItem kidArrowItem = (KidArrowItem)(stack.getItem() instanceof KidArrowItem ? stack.getItem() : ModItems.WOODEN_KID_ARROW);
        KidArrowEntity kiadArrowEntity = kidArrowItem.createKidArrow(this.getWorld(), stack, this);
        kiadArrowEntity.applyEnchantmentEffects(this, damageModifier);
        if (stack.isOf(ModItems.TIPPED_KID_ARROW) && kiadArrowEntity instanceof GoldenKidArrowEntity) {
            ((GoldenKidArrowEntity)kiadArrowEntity).initFromStack(stack);
        }
        return kiadArrowEntity;
    }

    @Inject(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/VillagerEntity;getOffers()Lnet/minecraft/village/TradeOfferList;"), cancellable = true)
    private void injectedInteractMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cbireturn) {
        World world = this.getWorld();
        if (world instanceof ServerWorld && this.getVillagerData().getProfession() == ModVillagerProfessions.ARCHER) {
            ItemStack itemStack = player.getStackInHand(hand);
            ItemStack villagerStack = this.getMainHandStack();
            if ((itemStack.getItem() instanceof BowItem || itemStack.getItem() instanceof ModBowItem) && this.isAlive() && !this.hasCustomer() && !this.isSleeping()) {
                this.equipStack(EquipmentSlot.MAINHAND, itemStack);
                ItemUsage.exchangeStack(itemStack, player, villagerStack);
                ((VillagerEntity)(Object)this).reinitializeBrain((ServerWorld)world);
                world.sendEntityStatus(this, EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES);
                this.playSound(SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
                cbireturn.setReturnValue(ActionResult.success(world.isClient));
            }
        }
    }

    @Inject(method = "onInteractionWith", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/passive/VillagerEntity;gossip:Lnet/minecraft/village/VillagerGossips;", opcode = Opcodes.GETFIELD))
    private void onInteractionWith(EntityInteraction interaction, Entity entity, CallbackInfo cbi) {
        if (interaction == EntityInteraction.VILLAGER_HURT) {
            this.setArcherAngryAt(entity.getUuid());
            List<VillagerEntity> hurtNearbyVillagers = this.getWorld().getEntitiesByClass(VillagerEntity.class, this.getBoundingBox().expand(this.isBaby() ? 16.0 : 8.0), VillagerEntity::isAlive);
            for (VillagerEntity nearbyVillager : hurtNearbyVillagers) {
                ((AngerableArcherVillager)nearbyVillager).setArcherAngryAt(entity.getUuid());
            }
            List<IronGolemEntity> hurtNeabyIronGolems = this.getWorld().getEntitiesByClass(IronGolemEntity.class, this.getBoundingBox().expand(this.isBaby() ? 16.0 : 8.0), IronGolemEntity::isAlive);
            for (IronGolemEntity nearbyIronGolem : hurtNeabyIronGolems) {
                ((Angerable)nearbyIronGolem).setAngryAt(entity.getUuid());
            }
        }
        else if (interaction == EntityInteraction.VILLAGER_KILLED) {
            List<VillagerEntity> killedNearbyVillagers = this.getWorld().getEntitiesByClass(VillagerEntity.class, this.getBoundingBox().expand(this.isBaby() ? 32.0 : 16.0), VillagerEntity::isAlive);
            for (VillagerEntity nearbyVillager : killedNearbyVillagers) {
                ((AngerableArcherVillager)nearbyVillager).setArcherAngryAt(entity.getUuid());
            }
            List<IronGolemEntity> killedNeabyIronGolems = this.getWorld().getEntitiesByClass(IronGolemEntity.class, this.getBoundingBox().expand(this.isBaby() ? 32.0 : 16.0), IronGolemEntity::isAlive);
            for (IronGolemEntity nearbyIronGolem : killedNeabyIronGolems) {
                ((Angerable)nearbyIronGolem).setAngryAt(entity.getUuid());
            }
        }
    }

    @Inject(method = "mobTick", at = @At("TAIL"))
    private void injectedMobTick(CallbackInfo cbi) {
        World world = this.getWorld();
        if (!world.isClient) {
            this.tickArcherAngerLogic((ServerWorld)world, true);
        }
        VillagerProfession villagerProfession = this.getVillagerData().getProfession();
        long currentTimeOfDay = world.getTimeOfDay();
        boolean timeOfDayBl = currentTimeOfDay > 60L && currentTimeOfDay%12000L >= 0 && currentTimeOfDay%12000L < 60L;
        if (timeOfDayBl && (villagerProfession == ModVillagerProfessions.ARCHER)) {
            ((VillagerEntity)(Object)this).reinitializeBrain((ServerWorld)world);
        }
    }

    @ModifyVariable(method = "fillRecipes", at = @At("STORE"), ordinal = 0)
    private Int2ObjectMap<TradeOffers.Factory[]> modifiedFilledRecipes(Int2ObjectMap<TradeOffers.Factory[]> int2ObjectMap) {
        VillagerData villagerData = this.getVillagerData();
        Map<VillagerProfession, Int2ObjectMap<Factory[]>> tradeOffers = TradeOffers.PROFESSION_TO_LEVELED_TRADE;
        if (villagerData.getProfession() == VillagerProfession.FLETCHER) {
            ModVillagerProfessions.replaceFletcherProfessionToLeveledTrade(tradeOffers);
        }
        return tradeOffers.get(villagerData.getProfession());
    }

    @Inject(method = "talkWithVillager", at = @At("TAIL"))
    private void injectedTalkWithVillager(ServerWorld world, VillagerEntity villager, long time, CallbackInfo cbi) {
        if (this.getVillagerData().getProfession() == ModVillagerProfessions.ARCHER && villager.getVillagerData().getProfession() == VillagerProfession.FLETCHER) {
            ItemStack mainHandStack = this.getMainHandStack();
            if (mainHandStack.getItem() instanceof ModBowItem && !(mainHandStack.hasEnchantments() || mainHandStack.get(DataComponentTypes.CUSTOM_NAME) != null)) {
                ModBowItem modBowItem = (ModBowItem)mainHandStack.getItem();
                int level = this.getVillagerData().getLevel();
                int fletcherLevel = villager.getVillagerData().getLevel();
                int materialLevel = modBowItem.getMaterial().getMiningLevel();
                if (materialLevel < level - 1) {
                    if (modBowItem.getBowType() == 1) {
                        if (level >= 2 && fletcherLevel >= 2) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_FUSED_HORN_BOW));
                        }
                        if (level >= 3 && fletcherLevel >= 3) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GOLD_FUSED_HORN_BOW));
                        }
                    }
                    else if (modBowItem.getBowType () == 2) {
                        if (level >= 2 && fletcherLevel >= 2) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_FUSED_LONGBOW));
                        }
                        if (level >= 3 && fletcherLevel >= 3) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GOLD_FUSED_LONGBOW));
                        }
                    }
                    else if (modBowItem.getBowType () == 3) {
                        if (level >= 2 && fletcherLevel >= 2) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_FUSED_TUBULAR_BOW));
                        }
                        if (level >= 3 && fletcherLevel >= 3) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GOLD_FUSED_TUBULAR_BOW));
                        }
                    }
                    else {
                        if (level >= 2 && fletcherLevel >= 2) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_FUSED_BOW));
                        }
                        if (level >= 3 && fletcherLevel >= 3) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GOLD_FUSED_BOW));
                        }
                    }
                }
                if (materialLevel < level - 2) {
                    if (modBowItem.getBowType() == 1) {
                        if (level >= 4 && fletcherLevel >= 4) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_FUSED_HORN_BOW));
                        }
                        if (level >= 5 && fletcherLevel == 5) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.DIAMOND_FUSED_HORN_BOW));
                        }
                    }
                    else if (modBowItem.getBowType () == 2) {
                        if (level >= 4 && fletcherLevel >= 4) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_FUSED_LONGBOW));
                        }
                        if (level >= 5 && fletcherLevel == 5) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.DIAMOND_FUSED_LONGBOW));
                        }
                    }
                    else if (modBowItem.getBowType () == 3) {
                        if (level >= 4 && fletcherLevel >= 4) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_FUSED_TUBULAR_BOW));
                        }
                        if (level >= 5 && fletcherLevel == 5) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.DIAMOND_FUSED_TUBULAR_BOW));
                        }
                    }
                    else {
                        if (level >= 4 && fletcherLevel >= 4) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_FUSED_BOW));
                        }
                        if (level >= 5 && fletcherLevel == 5) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.DIAMOND_FUSED_BOW));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void chooseRandomArcherAngerTime() {
        this.setArcherAngerTime(ANGER_TIME_RANGE.get(this.random));
    }

    @Override
    public void setArcherAngerTime(int angerTime) {
        this.angerTime = angerTime;
    }

    @Override
    public int getArcherAngerTime() {
        return this.angerTime;
    }

    @Override
    public void setArcherAngryAt(@Nullable UUID angryAt) {
        VillagerProfession profession = this.getVillagerData().getProfession();
        World world = this.getWorld();
        if (world instanceof ServerWorld && profession == ModVillagerProfessions.ARCHER) {
            if (angryAt != null && this.getArcherAngryAt() == angryAt) return;
            this.angryAt = angryAt;
            ((VillagerEntity)(Object)this).reinitializeBrain((ServerWorld)world);
        }
    }

    @Override
    @Nullable
    public UUID getArcherAngryAt() {
        return this.angryAt;
    }

    @Override
    public boolean canArcherTarget(LivingEntity target) {
        return target instanceof PlayerEntity ? this.shouldBeTargetedMob(target) && !(target.isSpectator() || ((PlayerEntity)target).isCreative()) : this.shouldBeTargetedMob(target);
    }

    @Override
    @Nullable
    public LivingEntity getArcherTarget() {
        return this.getTarget();
    }

    @Override
    public void setArcherTarget(@Nullable LivingEntity target) {
        this.setTarget(target);
    }

    @Override
    @Nullable
    public LivingEntity getArcherAttacker() {
        return this.getAttacker();
    }

    @Override
    public void setArcherAttacker(@Nullable LivingEntity attacker) {
        this.setAttacker(attacker);
    }

    private boolean shouldBeTargetedMob(LivingEntity livingEntity) {
        if (livingEntity instanceof ZombieEntity) {
            return !(livingEntity instanceof ZombifiedPiglinEntity);
        }
        else if (livingEntity instanceof VexEntity) {
            return ((VexEntity)livingEntity).getOwner() instanceof RaiderEntity;
        }
        return false;
    }

    
}
