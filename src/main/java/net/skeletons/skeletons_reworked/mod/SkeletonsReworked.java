package net.skeletons.skeletons_reworked.mod;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.skeletons.skeletons_reworked.config.SkeletonsReworkedConfig;
import org.slf4j.Logger;


@Mod(SkeletonsReworked.MODID)
public class SkeletonsReworked
{
    public static final String MODID = "skeletons_reworked";
    private static final Logger LOGGER = LogUtils.getLogger();

    public SkeletonsReworked()
    {

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SkeletonsReworkedConfig.DATAGEN_SPEC ,"SkeletonsReworked-data.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SkeletonsReworkedConfig.SERVER_SPEC ,"SkeletonsReworked-config.toml");
        SkeletonsReworkedConfig.loadConfig(SkeletonsReworkedConfig.SERVER_SPEC,
                FMLPaths.CONFIGDIR.get().resolve("SkeletonsReworked-config.toml").toString());

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext context = ModLoadingContext.get();

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::commonSetup);



    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {



        }
    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {


    }


}