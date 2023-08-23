package cz.nejakejtomas.bluemapminimap.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import cz.nejakejtomas.bluemapminimap.mc.ClientSetServerCallback;
import cz.nejakejtomas.bluemapminimap.mc.ClientSetWorldCallback;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public abstract class ConnectionMixin {
    // Level leave + server disconnect
    @Inject(method = "disconnect", at = @At("HEAD"))
    void onDisconnect(Component component, CallbackInfo ci) {
        if (RenderSystem.isOnRenderThread()) {
            ClientSetWorldCallback.Companion.getEVENT().invoker().onSwitch(null);
            ClientSetServerCallback.Companion.getEVENT().invoker().onSwitch(null);
        }
    }


}
