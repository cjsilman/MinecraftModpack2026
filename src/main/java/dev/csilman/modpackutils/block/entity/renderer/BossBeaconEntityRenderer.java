package dev.csilman.modpackutils.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.block.custom.BossBeaconBlock;
import dev.csilman.modpackutils.block.entity.custom.BossBeaconEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BossBeaconEntityRenderer implements BlockEntityRenderer<BossBeaconEntity> {
    private static final int BEAM_HEIGHT = 1000;
    private static final ResourceLocation BEAM_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ModpackUtilsMod.MOD_ID, "textures/entity/beacon_beam.png");

    public BossBeaconEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(BossBeaconEntity bossBeaconEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        if (!bossBeaconEntity.isActive()) {
            return;
        }

        int color = 255;
        
        if (bossBeaconEntity.getLevel() != null) {
            BlockState state = bossBeaconEntity.getLevel().getBlockState(bossBeaconEntity.getBlockPos());
            if (state.getBlock() instanceof BossBeaconBlock beaconBlock) {
                color = beaconBlock.getBeamColor();
            }
        }
        
        long gameTime = bossBeaconEntity.getLevel().getGameTime();

        renderBeam(
                poseStack,
                multiBufferSource,
                BEAM_TEXTURE,
                partialTick,
                gameTime, 0,
                BEAM_HEIGHT,
                color,
                0.3f,
                0.35f
        );

    }

    private void renderBeam(PoseStack poseStack, MultiBufferSource bufferSource, ResourceLocation texture,
                            float partialTick, long gameTime, int yOffset, int height, int color,
                            float beamRadius, float glowRadius) {
        BeaconRenderer.renderBeaconBeam(
                poseStack,
                bufferSource,
                texture,
                partialTick,
                1.0f,
                gameTime,
                yOffset,
                height,
                color,
                beamRadius,
                glowRadius
        );
    }

    @Override
    public boolean shouldRenderOffScreen(BossBeaconEntity blockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public boolean shouldRender(BossBeaconEntity blockEntity, Vec3 cameraPos) {
        return Vec3.atCenterOf(blockEntity.getBlockPos()).multiply((double)1.0F, (double)0.0F, (double)1.0F).closerThan(cameraPos.multiply((double)1.0F, (double)0.0F, (double)1.0F), (double)this.getViewDistance());
    }

    @Override
    public AABB getRenderBoundingBox(BossBeaconEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)pos.getX() + (double)1.0F, (double)1024.0F, (double)pos.getZ() + (double)1.0F);
    }
}
