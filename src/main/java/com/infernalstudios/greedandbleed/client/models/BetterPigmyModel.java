package com.infernalstudios.greedandbleed.client.models;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinAction;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.util.math.MathHelper;

public class BetterPigmyModel<T extends MobEntity> extends BipedModel<T> {
	public final ModelRenderer earLeft;
	public final ModelRenderer earRight;
	private final ModelRenderer hair;
	private final ModelRenderer bodyDefault;
	private final ModelRenderer headDefault;
	private final ModelRenderer leftArmDefault;
	private final ModelRenderer rightArmDefault;

	public BetterPigmyModel(float modelSize, int texWidthIn, int textHeightIn) {
		super(modelSize);
		this.texWidth = texWidthIn;
		this.texHeight = textHeightIn;

		head = new ModelRenderer(this);
		head.setPos(0.0F, 8.0F, 0.0F);
		head.texOffs(0, 0)
				.addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, 0.0F, false);

		hair = new ModelRenderer(this);
		hair.setPos(0.0F, -8.0F, 0.0F);
		head.addChild(hair);
		hair.texOffs(0, 22)
				.addBox(0.0F, -6.0F, -4.0F, 0.0F, 12.0F, 10.0F, 0.0F, false);

		//swapped earLeft to earRight here
		earRight = new ModelRenderer(this);
		earRight.setPos(5.0F, -7.0F, 0.0F);
		head.addChild(earRight);

		ModelRenderer leftEar = new ModelRenderer(this);
		leftEar.setPos(2.0F, 6.0F, -3.0F);
		earRight.addChild(leftEar);
		setRotationAngle(leftEar, 0.0F, 0.0F, -0.1745F);
		leftEar.texOffs(14, 38)
				.addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);

		// swapped earRight to earLeft here
		earLeft = new ModelRenderer(this);
		earLeft.setPos(-5.0F, -7.0F, 0.0F);
		head.addChild(earLeft);

		ModelRenderer rightEar = new ModelRenderer(this);
		rightEar.setPos(-1.0F, 6.0F, -3.0F);
		earLeft.addChild(rightEar);
		setRotationAngle(rightEar, 0.0F, 0.0F, 0.1745F);
		rightEar.texOffs(39, 26)
				.addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);

		ModelRenderer snout = new ModelRenderer(this);
		snout.setPos(0.0F, -2.0F, -4.0F);
		head.addChild(snout);
		snout.texOffs(42, 10)
				.addBox(-3.0F, -2.0F, -1.0F, 6.0F, 4.0F, 1.0F, 0.0F, false);

		ModelRenderer tusks = new ModelRenderer(this);
		tusks.setPos(0.0F, 1.0F, -1.0F);
		snout.addChild(tusks);
		tusks.texOffs(0, 0)
				.addBox(3.0F, -1.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		tusks.texOffs(0, 3)
				.addBox(-4.0F, -1.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		body = new ModelRenderer(this);
		body.setPos(0.0F, 11.0F, 0.0F);
		body.texOffs(0, 16)
				.addBox(-4.0F, -3.0F, -3.0F, 8.0F, 10.0F, 6.0F, 0.0F, false);

		leftArm = new ModelRenderer(this);
		leftArm.setPos(4.0F, 8.0F, 0.0F);
		leftArm.texOffs(31, 11)
				.addBox(0.0F, 0.0F, -2.0F, 3.0F, 10.0F, 5.0F, 0.0F, false);

		rightArm = new ModelRenderer(this);
		rightArm.setPos(-4.0F, 8.0F, 0.0F);
		rightArm.texOffs(23, 27)
				.addBox(-3.0F, 0.0F, -2.0F, 3.0F, 10.0F, 5.0F, 0.0F, false);

		leftLeg = new ModelRenderer(this);
		leftLeg.setPos(2.0F, 18.0F, 0.0F);
		leftLeg.texOffs(35, 38)
				.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

		rightLeg = new ModelRenderer(this);
		rightLeg.setPos(-2.0F, 18.0F, 0.0F);
		rightLeg.texOffs(36, 0)
				.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

		this.hat = new ModelRenderer(this);

		this.bodyDefault = this.body.createShallowCopy();
		this.headDefault = this.head.createShallowCopy();
		this.leftArmDefault = this.leftArm.createShallowCopy();
		this.rightArmDefault = this.leftArm.createShallowCopy();
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
	@Override
	public void setupAnim(T pigmy, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
		this.body.copyFrom(this.bodyDefault);
		this.head.copyFrom(this.headDefault);
		this.leftArm.copyFrom(this.leftArmDefault);
		this.rightArm.copyFrom(this.rightArmDefault);
		super.setupAnim(pigmy, p_225597_2_, p_225597_3_, p_225597_4_, p_225597_5_, p_225597_6_);
		float f = ((float)Math.PI / 6F);
		float f1 = p_225597_4_ * 0.1F + p_225597_2_ * 0.5F;
		float f2 = 0.08F + p_225597_3_ * 0.4F;
		this.earRight.zRot = (-(float)Math.PI / 6F) - MathHelper.cos(f1 * 1.2F) * f2;
		this.earLeft.zRot = ((float)Math.PI / 6F) + MathHelper.cos(f1) * f2;
		if (pigmy instanceof AbstractPiglinEntity) {
			AbstractPiglinEntity abstractpiglinentity = (AbstractPiglinEntity)pigmy;
			PiglinAction piglinaction = abstractpiglinentity.getArmPose();
			if (piglinaction == PiglinAction.DANCING) {
				float f3 = p_225597_4_ / 60.0F;
				this.earLeft.zRot = ((float)Math.PI / 6F) + ((float)Math.PI / 180F) * MathHelper.sin(f3 * 30.0F) * 10.0F;
				this.earRight.zRot = (-(float)Math.PI / 6F) - ((float)Math.PI / 180F) * MathHelper.cos(f3 * 30.0F) * 10.0F;
				this.head.x = MathHelper.sin(f3 * 10.0F);
				this.head.y = MathHelper.sin(f3 * 40.0F) + 0.4F;
				this.rightArm.zRot = ((float)Math.PI / 180F) * (70.0F + MathHelper.cos(f3 * 40.0F) * 10.0F);
				this.leftArm.zRot = this.rightArm.zRot * -1.0F;
				this.rightArm.y = MathHelper.sin(f3 * 40.0F) * 0.5F + 1.5F;
				this.leftArm.y = MathHelper.sin(f3 * 40.0F) * 0.5F + 1.5F;
				this.body.y = MathHelper.sin(f3 * 40.0F) * 0.35F;
			} else if (piglinaction == PiglinAction.ATTACKING_WITH_MELEE_WEAPON && this.attackTime == 0.0F) {
				this.holdWeaponHigh(pigmy);
			} else if (piglinaction == PiglinAction.CROSSBOW_HOLD) {
				ModelHelper.animateCrossbowHold(this.rightArm, this.leftArm, this.head, !pigmy.isLeftHanded());
			} else if (piglinaction == PiglinAction.CROSSBOW_CHARGE) {
				ModelHelper.animateCrossbowCharge(this.rightArm, this.leftArm, pigmy, !pigmy.isLeftHanded());
			} else if (piglinaction == PiglinAction.ADMIRING_ITEM) {
				this.head.xRot = 0.5F;
				this.head.yRot = 0.0F;
				if (pigmy.isLeftHanded()) {
					this.rightArm.yRot = -0.5F;
					this.rightArm.xRot = -0.9F;
				} else {
					this.leftArm.yRot = 0.5F;
					this.leftArm.xRot = -0.9F;
				}
			}
		}
		// TODO: Zombified Pigmies?
		else if (pigmy.getType() == EntityType.ZOMBIFIED_PIGLIN) {
			ModelHelper.animateZombieArms(this.leftArm, this.rightArm, pigmy.isAggressive(), this.attackTime, p_225597_4_);
		}

		//this.leftPants.copyFrom(this.leftLeg);
		//this.rightPants.copyFrom(this.rightLeg);
		//this.leftSleeve.copyFrom(this.leftArm);
		//this.rightSleeve.copyFrom(this.rightArm);
		//this.jacket.copyFrom(this.body);
		this.hat.copyFrom(this.head);
	}

	@Override
	protected void setupAttackAnimation(T pygmy, float p_230486_2_) {
		if (this.attackTime > 0.0F && pygmy instanceof PiglinEntity && ((PiglinEntity)pygmy).getArmPose() == PiglinAction.ATTACKING_WITH_MELEE_WEAPON) {
			ModelHelper.swingWeaponDown(this.rightArm, this.leftArm, pygmy, this.attackTime, p_230486_2_);
		} else {
			super.setupAttackAnimation(pygmy, p_230486_2_);
		}
	}

	private void holdWeaponHigh(T pygmy) {
		if (pygmy.isLeftHanded()) {
			this.leftArm.xRot = -1.8F;
		} else {
			this.rightArm.xRot = -1.8F;
		}

	}
}