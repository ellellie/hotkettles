package com.matthewperiut.hotkettles.client.render.blockentity;

import com.matthewperiut.hotkettles.blockentity.KettleBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class KettleBlockEntityRenderer implements BlockEntityRenderer<KettleBlockEntity> {
    // Custom texture path
    private static final Identifier LIQUIDS_TEXTURE = Identifier.of("hotkettles", "textures/blockentity/liquids.png");

    public KettleBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    private static class KettleDraw {
        MatrixStack matrices;
        VertexConsumer vertexConsumer;
        int light;
        int overlay;
        MatrixStack.Entry entry;
        Matrix4f positionMatrix;
        Matrix3f normalMatrix;
        KettleDraw(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay) {
            entry = matrices.peek();
            positionMatrix = entry.getPositionMatrix();
            normalMatrix = entry.getNormalMatrix();
            this.matrices = matrices;
            this.vertexConsumer = vertexConsumer;
            this.light = light;
            this.overlay = overlay;
        }

        public void drawPoint(float x, float y, float z, float u, float v) {
            vertexConsumer.vertex(positionMatrix, x, y, z) // Bottom-left
                    .color(255, 255, 255, 255)
                    .texture(u, v)
                    .overlay(overlay)
                    .light(light)
                    .normal(entry, 0.0f, 0.0f, 1.0f); // Flip normal for back face
        }
    }

    private static final float Z_FIGHT_OFFSET = 0.001F;

    @Override
    public void render(KettleBlockEntity kettle, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {
        matrices.push();

        // Translate to the block's position
        matrices.translate(0, 1, 0);

        // Use the custom texture with RenderLayer.getEntityCutout
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(LIQUIDS_TEXTURE));
        KettleDraw drawer = new KettleDraw(matrices, vertexConsumer, light, overlay);


        float uv_x_offset = kettle.liquidHorizontalOffset;;
        int uv_y_offset = kettle.liquidLevel;

        float TEXTURE_WIDTH = 2;
        float TEXTURE_HEIGHT = 5;

        //double Z_FIGHT_OFFSET = 0.001;
        if (kettle.dirX) {
            drawer.drawPoint(1 - (3 / 16.f) + Z_FIGHT_OFFSET, -(13 / 16.f), 0.4375F, uv_x_offset / 64.F, (TEXTURE_HEIGHT + uv_y_offset) / 32.F); // Bottom-left UV
            drawer.drawPoint(1 - (3 / 16.f) + Z_FIGHT_OFFSET, -(8 / 16.f), 0.4375F, uv_x_offset / 64.F, uv_y_offset / 32.F); // Top-left UV
            drawer.drawPoint(1 - (3 / 16.f) + Z_FIGHT_OFFSET, -(8 / 16.f), 0.5625F, (TEXTURE_WIDTH + uv_x_offset) / 64.F, uv_y_offset / 32.F); // Top-right UV
            drawer.drawPoint(1 - (3 / 16.f) + Z_FIGHT_OFFSET, -(13 / 16.f), 0.5625F, (TEXTURE_WIDTH + uv_x_offset) / 64.F, (TEXTURE_HEIGHT + uv_y_offset) / 32.F); // Bottom-right UV

            drawer.drawPoint((3 / 16.f) - Z_FIGHT_OFFSET, -(13 / 16.f), 0.5625F, uv_x_offset / 64.F, (TEXTURE_HEIGHT + uv_y_offset) / 32.F); // Bottom-left UV
            drawer.drawPoint((3 / 16.f) - Z_FIGHT_OFFSET, -(8 / 16.f), 0.5625F, uv_x_offset / 64.F, uv_y_offset / 32.F); // Top-left UV
            drawer.drawPoint((3 / 16.f) - Z_FIGHT_OFFSET, -(8 / 16.f), 0.4375F, (TEXTURE_WIDTH + uv_x_offset) / 64.F, uv_y_offset / 32.F); // Top-right UV
            drawer.drawPoint((3 / 16.f) - Z_FIGHT_OFFSET, -(13 / 16.f), 0.4375F, (TEXTURE_WIDTH + uv_x_offset) / 64.F, (TEXTURE_HEIGHT + uv_y_offset) / 32.F); // Bottom-right UV

        } else {
            drawer.drawPoint(0.4375F, -(13 / 16.f), (3 / 16.f) - Z_FIGHT_OFFSET, uv_x_offset / 64.F, (TEXTURE_HEIGHT + uv_y_offset) / 32.F); // Bottom-left UV
            drawer.drawPoint(0.4375F, -(8 / 16.f),  (3 / 16.f) - Z_FIGHT_OFFSET, uv_x_offset / 64.F, uv_y_offset / 32.F); // Top-left UV
            drawer.drawPoint(0.5625F, -(8 / 16.f),  (3 / 16.f) - Z_FIGHT_OFFSET, (TEXTURE_WIDTH + uv_x_offset) / 64.F, uv_y_offset / 32.F); // Top-right UV
            drawer.drawPoint(0.5625F, -(13 / 16.f), (3 / 16.f) - Z_FIGHT_OFFSET, (TEXTURE_WIDTH + uv_x_offset) / 64.F, (TEXTURE_HEIGHT + uv_y_offset) / 32.F); // Bottom-right UV

            drawer.drawPoint(0.5625F, -(13 / 16.f), 1 - (3 / 16.f) + Z_FIGHT_OFFSET, uv_x_offset / 64.F, (TEXTURE_HEIGHT + uv_y_offset) / 32.F); // Bottom-left UV
            drawer.drawPoint(0.5625F, -(8 / 16.f),  1 - (3 / 16.f) + Z_FIGHT_OFFSET, uv_x_offset / 64.F, uv_y_offset / 32.F); // Top-left UV
            drawer.drawPoint(0.4375F, -(8 / 16.f),  1 - (3 / 16.f) + Z_FIGHT_OFFSET, (TEXTURE_WIDTH + uv_x_offset) / 64.F, uv_y_offset / 32.F); // Top-right UV
            drawer.drawPoint(0.4375F, -(13 / 16.f), 1 - (3 / 16.f) + Z_FIGHT_OFFSET, (TEXTURE_WIDTH + uv_x_offset) / 64.F, (TEXTURE_HEIGHT + uv_y_offset) / 32.F); // Bottom-right UV
        }
        
        matrices.pop();
    }
}
