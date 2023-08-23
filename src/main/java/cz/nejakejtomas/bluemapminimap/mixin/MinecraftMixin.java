package cz.nejakejtomas.bluemapminimap.mixin;

import cz.nejakejtomas.bluemapminimap.mc.ClientSetWorldCallback;
import cz.nejakejtomas.bluemapminimap.mc.McUtilsKt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    // Level leave
    @Inject(method = "setLevel", at = @At("HEAD"))
    void setLevel(ClientLevel clientLevel, CallbackInfo ci) {
        ClientSetWorldCallback.Companion.getEVENT().invoker().onSwitch(McUtilsKt.getCurrentWorldName(clientLevel));
    }
}
