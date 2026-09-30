package net.tokyosu.apocalypselib.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import java.util.function.Consumer;
import java.util.function.Supplier;

/** Common-side networking helpers. Packet IDs and authorization remain with the caller. */
public final class NetworkUtils {
    private NetworkUtils() {}

    public static SimpleChannel createChannel(ResourceLocation id, String protocol) {
        return NetworkRegistry.newSimpleChannel(id, () -> protocol, protocol::equals, protocol::equals);
    }

    /** Dispatch a client-to-server packet on the server thread, ignoring missing senders. */
    public static void handleServer(Supplier<NetworkEvent.Context> supplier, Consumer<ServerPlayer> action) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) action.accept(player);
        });
        context.setPacketHandled(true);
    }
}
