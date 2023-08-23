package cz.nejakejtomas.bluemapminimap.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import cz.nejakejtomas.bluemapminimap.mc.ClientSetServerCallback;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Shadow
    @Nullable
    public abstract ServerData getServerData();

    // Server connect
    @Inject(method = "handleLogin", at = @At("HEAD"))
    void onDisconnect(ClientboundLoginPacket clientboundLoginPacket, CallbackInfo ci) {
        if (RenderSystem.isOnRenderThread()) {
            ServerData serverData = getServerData();
            ClientSetServerCallback.Companion.getEVENT().invoker().onSwitch(serverData == null ? null : serverData.ip);
        }
    }
}
