package net.skeletons.skeletons_reworked.core;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.skeletons.skeletons_reworked.config.SkeletonsReworkedConfig;
import net.skeletons.skeletons_reworked.intel.EscapeFromVehicleGoal;
import net.skeletons.skeletons_reworked.mod.SkeletonsReworked;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = SkeletonsReworked.MODID)
public class SkeletonIntelligenceRevamp
{

    @SubscribeEvent(receiveCanceled = true)
    public static void damageEvent(LivingDamageEvent event)
    {
        if (event.getEntity() instanceof Skeleton || event.getEntity() instanceof Stray)
        {
            if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof Arrow arrow)
            {
                Entity owner = arrow.getOwner();
                if (owner instanceof Skeleton || owner instanceof Stray)
                {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public static void hurtEvent(LivingHurtEvent event)
    {
        if (event.getEntity() instanceof Skeleton || event.getEntity() instanceof Stray)
        {
            if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof Arrow arrow)
            {
                Entity owner = arrow.getOwner();
                if (owner instanceof Skeleton || owner instanceof Stray)
                {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void tickEvent(LivingEvent.LivingTickEvent event)
    {
        if(event.getEntity() instanceof Skeleton || event.getEntity() instanceof Stray)
        {
            Level level = event.getEntity().level();
            boolean day = level.isDay();
            boolean config = SkeletonsReworkedConfig.SERVER.skeletonsImmuneToSun.get();
            BlockPos blockpos = event.getEntity().blockPosition();
            if (level.canSeeSky(blockpos) && config)
            {
                if (day)
                {
                    if (event.getEntity().isOnFire() && !event.getEntity().isInLava())
                    {
                        event.getEntity().clearFire();
                    }
                }
            }


        }


    }

    @SubscribeEvent
    public static void deathZombie(LivingDeathEvent event)
    {
        if (event.getEntity() != null && event.getEntity() instanceof Zombie && Math.random() <= 0.15F && SkeletonsReworkedConfig.SERVER.zombiesTurnToSkeletons.get())
        {
            Entity entity = event.getEntity();
            Skeleton skeleton = new Skeleton(EntityType.SKELETON, entity.level());
            skeleton.moveTo(entity.getX(), entity.getY(), entity.getZ());
            entity.level().addFreshEntity(skeleton);
            skeleton.setItemSlot(EquipmentSlot.MAINHAND, entity.getSlot(0).get());
            skeleton.setItemSlot(EquipmentSlot.OFFHAND, entity.getSlot(1).get());
            skeleton.setItemSlot(EquipmentSlot.FEET, entity.getSlot(0).get());
            skeleton.setItemSlot(EquipmentSlot.LEGS, entity.getSlot(1).get());
            skeleton.setItemSlot(EquipmentSlot.CHEST, entity.getSlot(2).get());
            skeleton.setItemSlot(EquipmentSlot.HEAD, entity.getSlot(3).get());
            entity.level().playSound(null, entity.blockPosition(), SoundEvents.ZOMBIE_INFECT, SoundSource.HOSTILE, 1.0F, 1.0F);
            if (entity.level() instanceof ServerLevel world)
            {
                world.sendParticles(ParticleTypes.EXPLOSION, entity.getX(), entity.getY(), entity.getZ(), 3, 0, 0, 0, 0.01);
            }
        }

    }


    @SubscribeEvent()
    public static void addSpawn(EntityJoinLevelEvent event)
    {

        ItemStack helmetG = ItemStack.EMPTY;
        ItemStack chestG = ItemStack.EMPTY;
        ItemStack legsG = ItemStack.EMPTY;
        ItemStack bootG = ItemStack.EMPTY;
        ItemStack mainG = ItemStack.EMPTY;

        if (event.getEntity() instanceof Skeleton || event.getEntity() instanceof Stray)
        {

            /**
             * /// AI Stuff
             * zombie is all the extra AI the zombies use as of now
             */

            AbstractSkeleton skeleton = (AbstractSkeleton) event.getEntity();
            skeleton.targetSelector.addGoal(1, (new HurtByTargetGoal(skeleton)).setAlertOthers(Skeleton.class));
            skeleton.targetSelector.addGoal(1, (new HurtByTargetGoal(skeleton)).setAlertOthers(Stray.class));
            skeleton.targetSelector.addGoal(0, (new EscapeFromVehicleGoal(skeleton)));

            Objects.requireNonNull(skeleton.getAttribute(Attributes.FOLLOW_RANGE)).setBaseValue((double) 32.0);

            if (SkeletonsReworkedConfig.SERVER.fast_at_night.get() && skeleton.level().isNight())
            {
                Objects.requireNonNull(skeleton.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue((double) 0.29F);
            }


            if (SkeletonsReworkedConfig.SERVER.skeleton_leaps.get() && skeleton.getMainHandItem().getItem() instanceof SwordItem)
            {
                skeleton.goalSelector.addGoal(0, new LeapAtTargetGoal(skeleton, 0.2F));
            }

            if (SkeletonsReworkedConfig.SERVER.skeleton_accurate_attack_range.get())
            {
                skeleton.goalSelector.addGoal(4, new MeleeAttackGoal(skeleton, 1.5, false)
                {
                    @Override
                    protected double getAttackReachSqr(LivingEntity entity)
                    {
                        return 1.5 + entity.getBbWidth() * entity.getBbWidth();
                    }
                });
            }


            if (SkeletonsReworkedConfig.DATAGEN.improved_equipment.get())
            {

                for (String str : SkeletonsReworkedConfig.DATAGEN.skeleton_helmet.get())
                {
                    String[] string = str.split("\\|");
                    ItemStack helmet = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(string[0]))));
                    if (Math.random() < Integer.parseUnsignedInt(string[1]) / (float) SkeletonsReworkedConfig.DATAGEN.improved_equipment_chance.get())
                    {
                        helmetG = helmet;
                    }
                }

                for (String str : SkeletonsReworkedConfig.DATAGEN.skeleton_chestplate.get())
                {
                    String[] string = str.split("\\|");
                    ItemStack chest = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(string[0]))));
                    if (Math.random() < Integer.parseUnsignedInt(string[1]) / (float) SkeletonsReworkedConfig.DATAGEN.improved_equipment_chance.get())
                    {
                        chestG = chest;
                    }
                }

                for (String str : SkeletonsReworkedConfig.DATAGEN.skeleton_legs.get())
                {
                    String[] string = str.split("\\|");
                    ItemStack legs = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(string[0]))));
                    if (Math.random() < Integer.parseUnsignedInt(string[1]) / (float) SkeletonsReworkedConfig.DATAGEN.improved_equipment_chance.get())
                    {
                        legsG = legs;
                    }
                }

                for (String str : SkeletonsReworkedConfig.DATAGEN.skeleton_feet.get())
                {
                    String[] string = str.split("\\|");
                    ItemStack boot = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(string[0]))));
                    if (Math.random() < Integer.parseUnsignedInt(string[1]) / (float) SkeletonsReworkedConfig.DATAGEN.improved_equipment_chance.get())
                    {
                        bootG = boot;
                    }
                }

                for (String str : SkeletonsReworkedConfig.DATAGEN.skeleton_main_hand.get())
                {
                    String[] string = str.split("\\|");
                    ItemStack main = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(string[0]))));
                    if (Math.random() < Integer.parseUnsignedInt(string[1]) / (float) SkeletonsReworkedConfig.DATAGEN.improved_equipment_chance.get())
                    {
                        mainG = main;
                    }
                }

                skeleton.setItemSlot(EquipmentSlot.MAINHAND, mainG);
                skeleton.setItemSlot(EquipmentSlot.HEAD, helmetG);
                skeleton.setItemSlot(EquipmentSlot.CHEST, chestG);
                skeleton.setItemSlot(EquipmentSlot.LEGS, legsG);
                skeleton.setItemSlot(EquipmentSlot.FEET, bootG);

            }
        }
    }


}
