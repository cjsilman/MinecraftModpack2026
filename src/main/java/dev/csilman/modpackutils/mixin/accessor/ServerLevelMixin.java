package dev.csilman.modpackutils.mixin.accessor;

import dev.csilman.modpackutils.mixin.iface.ChunkGeneratorStructureStateMixin;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        ServerLevel self = (ServerLevel)(Object)this;
        ChunkGeneratorStructureState state = self.getChunkSource()
                .getGeneratorState();
        ((ChunkGeneratorStructureStateMixin) state).setDimensionKey(self.dimension());
    }
}
