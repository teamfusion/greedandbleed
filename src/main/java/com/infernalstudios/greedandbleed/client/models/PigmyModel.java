package com.infernalstudios.greedandbleed.client.models;
// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinAction;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.util.math.MathHelper;

public class PigmyModel<T extends MobEntity> extends BipedModel<T> {
	public final ModelRenderer earRight;
	public final ModelRenderer earLeft;
	private final ModelRenderer bodyDefault;
	private final ModelRenderer headDefault;
	private final ModelRenderer leftArmDefault;
	private final ModelRenderer rightArmDefault;
	private final ModelRenderer hair;

	public PigmyModel(float modelSize, int texWidthIn, int textHeightIn) {
		super(modelSize);
		this.texWidth = texWidthIn;
		this.texHeight = textHeightIn;

		this.body = new ModelRenderer(this);
		this.body.setPos(0.0F, 24.0F, 0.0F);
		ModelRenderer upperbody = new ModelRenderer(this);
		upperbody.setPos(0.0F, 0.0F, 0.0F);
		this.body.addChild(upperbody);
		upperbody.texOffs(0, 16).addBox(-4.0F, -16.0F, -3.0F, 8.0F, 10.0F, 6.0F, 0.0F, false);

		this.head = new ModelRenderer(this);
		this.head.setPos(0.0F, 0.0F, 0.0F);
		this.head.texOffs(0, 0).addBox(-5.0F, -24.0F, -4.0F, 10.0F, 8.0F, 8.0F, 0.0F, false);
		//this.body.addChild(headpieces);

		this.hair = new ModelRenderer(this);
		this.hair.setPos(0.0F, 0.0F, 0.0F);
		this.head.addChild(this.hair);
		this.hair.texOffs(0, 22).addBox(0.0F, -30.0F, -4.0F, 0.0F, 12.0F, 10.0F, 0.0F, false);

		//ears = new ModelRenderer(this);
		//ears.setPos(0.0F, 0.0F, 0.0F);
		//this.head.addChild(ears);

		this.earLeft = new ModelRenderer(this);
		this.earLeft.setPos(-6.0F, -17.0F, -3.0F);
		//ears.addChild(earRight);
		this.head.addChild(this.earLeft);
		setRotationAngle(this.earLeft, 0.0F, 0.0F, 0.1745F);
		this.earLeft.texOffs(39, 26).addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);

		this.earRight = new ModelRenderer(this);
		this.earRight.setPos(7.0F, -17.0F, -3.0F);
		//ears.addChild(earLeft);
		this.head.addChild(this.earRight);
		setRotationAngle(this.earRight, 0.0F, 0.0F, -0.1745F);
		this.earRight.texOffs(14, 38).addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);

		ModelRenderer snout = new ModelRenderer(this);
		snout.setPos(0.0F, 0.0F, 0.0F);
		this.head.addChild(snout);
		snout.texOffs(42, 10).addBox(-3.0F, -20.0F, -5.0F, 6.0F, 4.0F, 1.0F, 0.0F, false);

		ModelRenderer teeth = new ModelRenderer(this);
		teeth.setPos(0.0F, 0.0F, 0.0F);
		snout.addChild(teeth);
		teeth.texOffs(0, 0).addBox(3.0F, -18.0F, -5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		teeth.texOffs(0, 3).addBox(-4.0F, -18.0F, -5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		//arms = new ModelRenderer(this);
		//arms.setPos(0.0F, 0.0F, 0.0F);
		//upperbody.addChild(arms);
		//arms.texOffs(31, 11).addBox(4.0F, -16.0F, -2.0F, 3.0F, 10.0F, 5.0F, 0.0F, false);
		//arms.texOffs(23, 27).addBox(-7.0F, -16.0F, -2.0F, 3.0F, 10.0F, 5.0F, 0.0F, false);
		this.leftArm = new ModelRenderer(this);
		//upperbody.addChild(leftArm);
		this.leftArm.texOffs(31, 11).addBox(4.0F, -16.0F, -2.0F, 3.0F, 10.0F, 5.0F, 0.0F, false);

		this.rightArm = new ModelRenderer(this);
		//upperbody.addChild(rightArm);
		this.rightArm.texOffs(23, 27).addBox(-7.0F, -16.0F, -2.0F, 3.0F, 10.0F, 5.0F, 0.0F, false);

		//legs = new ModelRenderer(this);
		//legs.setPos(0.0F, 0.0F, 0.0F);
		//body.addChild(legs);
		//legs.texOffs(35, 38).addBox(0.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
		//legs.texOffs(36, 0).addBox(-4.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
		this.leftLeg = new ModelRenderer(this);
		//upperbody.addChild(leftLeg);
		this.leftLeg.texOffs(35, 38).addBox(0.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

		this.rightLeg = new ModelRenderer(this);
		//upperbody.addChild(rightLeg);
		this.rightLeg.texOffs(36, 0).addBox(-4.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

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