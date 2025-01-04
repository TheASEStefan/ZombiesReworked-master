package net.skeletons.skeletons_reworked.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.List;

public class SkeletonsReworkedConfig
{

    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final DataGen DATAGEN;
    public static final ForgeConfigSpec DATAGEN_SPEC;


    static
    {

        Pair<Server, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER = commonSpecPair.getLeft();
        SERVER_SPEC = commonSpecPair.getRight();

        Pair<DataGen , ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(DataGen::new);
        DATAGEN = commonPair.getLeft();
        DATAGEN_SPEC = commonPair.getRight();

    }
    public static class Server
    {

        public final ForgeConfigSpec.ConfigValue<Boolean> skeleton_accurate_attack_range;
        public final ForgeConfigSpec.ConfigValue<Boolean> skeleton_leaps;
        public final ForgeConfigSpec.ConfigValue<Boolean> heavy_armor_on_skeletons;
        public final ForgeConfigSpec.ConfigValue<Boolean> fast_at_night;
        public final ForgeConfigSpec.ConfigValue<Boolean> skeletonsImmuneToSun;
        public final ForgeConfigSpec.ConfigValue<Boolean> zombiesTurnToSkeletons;

        public Server(ForgeConfigSpec.Builder builder)
        {

            builder.push("AI Changes for the vanilla skeletons & strays");
            this.skeleton_accurate_attack_range = builder.comment("Default true").define("Should the skeletons have accurate-melee range?",true);
            this.skeleton_leaps = builder.comment("Default true").define("Should the skeletons with swords have a chance to leap forward?",true);
            this.heavy_armor_on_skeletons = builder.comment("Default true").define("Should skeletons have upgraded armor and equipment and spawn with it less rarely?",true);
            this.fast_at_night = builder.comment("Default true").define("Should skeletons be faster when encountering the player at night time?",true);
            this.skeletonsImmuneToSun = builder.comment("Default true").define("Should the skeletons now be IMMUNE to sunlight damage?",true);
            this.zombiesTurnToSkeletons = builder.comment("Default true").define("Should the zombies have a chance to turn into skeletons on death?",true);
            builder.pop();


        }
    }
    public static class DataGen
    {
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> skeleton_helmet;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> skeleton_chestplate;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> skeleton_legs;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> skeleton_feet;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> skeleton_main_hand;
        public final ForgeConfigSpec.ConfigValue<Boolean> improved_equipment;
        public final ForgeConfigSpec.ConfigValue<Integer> improved_equipment_chance;

        public DataGen(ForgeConfigSpec.Builder builder)
        {

            builder.push("Skeleton Equipment");
            builder.comment("Items / chance of spawning with it");
            this.skeleton_helmet = builder.defineList("Head Slot",
                    Lists.newArrayList("minecraft:chainmail_helmet|50","minecraft:iron_helmet|20","minecraft:leather_helmet|20") , o -> o instanceof String);
            this.skeleton_chestplate = builder.defineList("Chest Slot",
                    Lists.newArrayList("minecraft:chainmail_chestplate|50","minecraft:iron_chestplate|20","minecraft:leather_chestplate|20") , o -> o instanceof String);
            this.skeleton_legs = builder.defineList("Legs Slot",
                    Lists.newArrayList("minecraft:leather_leggings|50","minecraft:iron_leggings|20","minecraft:chainmail_leggings|20") , o -> o instanceof String);
            this.skeleton_feet = builder.defineList("Boots Slot",
                    Lists.newArrayList("minecraft:leather_boots|50","minecraft:iron_boots|20","minecraft:chainmail_boots|20") , o -> o instanceof String);
            this.skeleton_main_hand = builder.defineList("Main Hand Slot",
                    Lists.newArrayList("minecraft:stone_sword|25" , "minecraft:iron_sword|25","minecraft:bow|200") , o -> o instanceof String);
            this.improved_equipment = builder.comment("Default true").define("Should skeletons spawn with more armor and tools and more commonly",true);
            this.improved_equipment_chance = builder.comment("Default 250").define("Chance of the skeletons spawning with improved equipment, the bigger the number the smaller the chance",250);
            builder.pop();

        }

    }

    public static void loadConfig(ForgeConfigSpec config, String path)
    {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave()
                .writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }

}