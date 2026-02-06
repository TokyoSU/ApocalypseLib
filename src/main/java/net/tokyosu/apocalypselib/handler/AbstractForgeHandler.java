package net.tokyosu.apocalypselib.handler;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Abstract forge handler for easy setup.
 * Be sure to call Mod.EventBusSubscriber with Bus=FORGE and Dist=CLIENT.
 * Also be sure to add @SubscribeEvent to each function you @Override.
 */
public class AbstractForgeHandler {
    /**
     * Called when a player join the world.
     */
    //@SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {}

    /**
     * This is called each tick with player information.
     * @param event Player information on that tick.
     */
    //@SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {}
}
