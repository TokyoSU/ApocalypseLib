package net.tokyosu.apocalypselib.handler;

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

/**
 * Abstract the mod handler creation to simplify its use.
 * Be sure to use @Mod.EventBusSubscriber with Bus=MOD and Dist=CLIENT.
 * Also be sure to add @SubscribeEvent to each function you @Override.
 */
public class AbstractModHandler {
    /**
     * Allows users to register custom key mappings.
     */
    //@SubscribeEvent
    public void onRegisterKeyMapping(RegisterKeyMappingsEvent event) {}

    /**
     * Most non-specific mod setup will be performed here.
     */
    //@SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {}

    /**
     * When mod is constructing, almost unused.
     */
    //@SubscribeEvent
    public void onConstructMod(FMLConstructModEvent event) {}

    /**
     * Do client only setup with this event, such as KeyBindings or Menu registry.
     */
    //@SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {}

    /**
     * When forge finished loading all mods.
     */
    //@SubscribeEvent
    public void onLoadCompleted(FMLLoadCompleteEvent event) {}


    /**
     * When you need to register chat commands.
     */
    //@SubscribeEvent
    public void onChatCommand(RegisterCommandsEvent event) {}
}
