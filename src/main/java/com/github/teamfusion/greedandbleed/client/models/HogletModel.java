package com.github.teamfusion.greedandbleed.client.models;// Made with Blockbench 4.1.1
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports


import com.github.teamfusion.greedandbleed.common.entity.piglin.HogletEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class HogletModel<T extends HogletEntity> extends EntityModel<T> {
	private final ModelRenderer Body;
	private final ModelRenderer LeftArm;
	private final ModelRenderer RightArm;
	private final ModelRenderer Tail;
	private final ModelRenderer head;
	private final ModelRenderer LeftEar;
	private final ModelRenderer RightEar;
	private final ModelRenderer Bandana;
	private final ModelRenderer Pelvis;
	private final ModelRenderer LeftLeg;
	private final ModelRenderer RightLeg;

	public HogletModel() {
		texWidth = 64;
		texHeight = 64;

		Body = new ModelRenderer(this);
		Body.setPos(0.0F, 17.0F, -1.2857F);
		Body.texOffs(0, 0).addBox(-4.0F, -5.0F, -4.7143F, 8.0F, 7.0F, 12.0F, 0.0F, false);
		Body.texOffs(20, 9).addBox(0.0F, -8.0F, -7.7143F, 0.0F, 5.0F, 10.0F, 0.0F, false);

		LeftArm = new ModelRenderer(this);
		LeftArm.setPos(-2.0F, 3.0F, -2.7143F);
		Body.addChild(LeftArm);
		LeftArm.texOffs(12, 34).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F, 0.0F, false);

		RightArm = new ModelRenderer(this);
		RightArm.setPos(2.0F, 2.0F, -2.7143F);
		Body.addChild(RightArm);
		RightArm.texOffs(20, 34).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, 0.0F, false);

		Tail = new ModelRenderer(this);
		Tail.setPos(0.0F, -3.0F, 7.2857F);
		Body.addChild(Tail);
		Tail.texOffs(0, 21).addBox(0.0F, -2.0F, 0.0F, 0.0F, 3.0F, 10.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setPos(0.0F, -0.6429F, -4.3857F);
		Body.addChild(head);
		head.texOffs(0, 19).addBox(-3.5F, -4.8571F, -6.3286F, 7.0F, 6.0F, 6.0F, 0.0F, false);
		head.texOffs(21, 26).addBox(-2.0F, -2.3571F, -11.3286F, 4.0F, 3.0F, 5.0F, 0.0F, false);
		head.texOffs(28, 6).addBox(-2.0F, -3.8571F, -11.2286F, 4.0F, 4.0F, 1.0F, 0.0F, false);
		head.texOffs(36, 34).addBox(2.0F, -3.8571F, -11.2286F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		head.texOffs(0, 19).addBox(-3.0F, -3.8571F, -11.2286F, 1.0F, 4.0F, 1.0F, 0.0F, false);

		LeftEar = new ModelRenderer(this);
		LeftEar.setPos(-3.5F, -4.3571F, -0.8286F);
		head.addChild(LeftEar);
		LeftEar.texOffs(34, 24).addBox(-0.5F, -2.0F, -1.5F, 1.0F, 2.0F, 3.0F, 0.0F, false);

		RightEar = new ModelRenderer(this);
		RightEar.setPos(0.0F, 7.6429F, 4.6714F);
		head.addChild(RightEar);
		RightEar.texOffs(28, 34).addBox(3.0F, -14.0F, -7.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);

		Bandana = new ModelRenderer(this);
		Bandana.setPos(0.0F, 1.0F, -4.8143F);
		Body.addChild(Bandana);
		setRotationAngle(Bandana, -0.2182F, 0.0F, 0.0F);
		Bandana.texOffs(28, 0).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 6.0F, 0.0F, 0.0F, false);

		Pelvis = new ModelRenderer(this);
		Pelvis.setPos(0.0F, 19.0F, 2.0F);


		LeftLeg = new ModelRenderer(this);
		LeftLeg.setPos(-2.0F, 0.0F, 0.5F);
		Pelvis.addChild(LeftLeg);
		LeftLeg.texOffs(0, 34).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 6.0F, 3.0F, 0.0F, false);

		RightLeg = new ModelRenderer(this);
		RightLeg.setPos(2.0F, 0.0F, 0.5F);
		Pelvis.addChild(RightLeg);
		RightLeg.texOffs(0, 0).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 6.0F, 3.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.head.xRot = headPitch * ((float) Math.PI / 180F);

		this.RightArm.xRot = MathHelper.cos(limbSwing) * 1.2F * limbSwingAmount;
		this.LeftArm.xRot = MathHelper.cos(limbSwing + (float) Math.PI) * 1.2F * limbSwingAmount;
		this.RightLeg.xRot = this.LeftArm.xRot;
		this.LeftLeg.xRot = this.RightArm.xRot;
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body.render(matrixStack, buffer, packedLight, packedOverlay);
		Pelvis.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}