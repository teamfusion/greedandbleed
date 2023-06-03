package com.github.teamfusion.greedandbleed.client.layers;

import com.github.teamfusion.greedandbleed.client.ClientSetup;
import com.github.teamfusion.greedandbleed.client.models.GBHoglinModelComplete;
import com.github.teamfusion.greedandbleed.common.entity.HasMountArmor;
import com.github.teamfusion.greedandbleed.common.item.HoglinArmorItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.item.ItemStack;

public class HoglinArmorLayer<T extends EntityModel<Hoglin>> extends RenderLayer<Hoglin, T> {
    private final GBHoglinModelComplete<Hoglin> model;

    public HoglinArmorLayer(RenderLayerParent<Hoglin, T> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.model = new GBHoglinModelComplete(entityModelSet.bakeLayer(ClientSetup.HOGLIN_ARMOR));
    }

    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, Hoglin hoglin, float f, float g, float h, float j, float k, float l) {
        if (hoglin instanceof HasMountArmor armor) {
            ItemStack itemStack = armor.getArmor();
            if (itemStack.getItem() instanceof HoglinArmorItem && this.getParentModel() instanceof GBHoglinModelComplete gbHoglinModelComplete) {
                HoglinArmorItem hoglinArmorItem = (HoglinArmorItem) itemStack.getItem();
                (gbHoglinModelComplete).copyPropertiesTo(this.model);
                this.model.prepareMobModel(hoglin, f, g, h);
                this.model.setupAnim(hoglin, f, g, j, k, l);
                float n;
                float o;
                float p;

                n = 1.0F;
                o = 1.0F;
                p = 1.0F;


                VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(hoglinArmorItem.getTexture()));
                this.model.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, n, o, p, 1.0F);
            }
        }
    }
}
