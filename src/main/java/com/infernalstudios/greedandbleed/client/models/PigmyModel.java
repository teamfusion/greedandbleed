package com.infernalstudios.greedandbleed.client.models;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinAction;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class PigmyModel<T extends MobEntity> extends BipedModel<T> {
	public final ModelRenderer earLeft;
	public final ModelRenderer earRight;
	private final ModelRenderer bodyDefault;
	private final ModelRenderer headDefault;
	private final ModelRenderer leftArmDefault;
	private final ModelRenderer rightArmDefault;

	public PigmyModel(float modelSize){
		this(modelSize, 64, 64);
	}

	public PigmyModel(float modelSize, int texWidthIn, int textHeightIn) {
		super(modelSize);
		this.texWidth = texWidthIn;
		this.texHeight = textHeightIn;

		head = new ModelRenderer(this);
		head.setPos(0.0F, 8.0F, 0.0F);
		head.texOffs(0, 0).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, modelSize, false);
		head.texOffs(42, 10).addBox(-3.0F, -4.0F, -5.0F, 6.0F, 4.0F, 1.0F, modelSize, false);
		head.texOffs(0, 3).addBox(3.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, modelSize, false);
		head.texOffs(0, 0).addBox(-4.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, modelSize, false);
		head.texOffs(0, 22).addBox(0.0F, -14.0F, -3.0F, 0.0F, 12.0F, 10.0F, modelSize, false);

		body = new ModelRenderer(this);
		body.setPos(0.0F, 8.0F, 0.0F);
		body.texOffs(0, 16).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 10.0F, 6.0F, modelSize, false);
		body.texOffs(0, 16).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 10.0F, 6.0F, modelSize + 0.25F, false);

		leftArm = new ModelRenderer(this);
		leftArm.setPos(5.0F, 10.0F, 0.0F);
		leftArm.texOffs(23, 27).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 10.0F, 5.0F, modelSize, false);
		leftArm.texOffs(23, 27).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 10.0F, 5.0F, modelSize + 0.25F, false);

		rightArm = new ModelRenderer(this);
		rightArm.setPos(-5.0F, 10.0F, 0.0F);
		rightArm.texOffs(31, 11).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 10.0F, 5.0F, modelSize, false);
		rightArm.texOffs(31, 11).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 10.0F, 5.0F, modelSize + 0.25F, false);

		leftLeg = new ModelRenderer(this);
		leftLeg.setPos(-2.0F, 18.0F, 0.0F);
		leftLeg.texOffs(35, 38).addBox(2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, modelSize, false);
		leftLeg.texOffs(35, 38).addBox(2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, modelSize + 0.25F, false);

		rightLeg = new ModelRenderer(this);
		rightLeg.setPos(2.0F, 18.0F, 0.0F);
		rightLeg.texOffs(36, 0).addBox(-6.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, modelSize, false);
		rightLeg.texOffs(36, 0).addBox(-6.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, modelSize + 0.25F, false);

		earLeft = new ModelRenderer(this);
		earLeft.setPos(5.5F, -6.0F, 0.0F);
		head.addChild(earLeft);
		earLeft.texOffs(14, 38).addBox(-0.5F, -1.0F, -3.0F, 1.0F, 6.0F, 6.0F, modelSize, false);

		earRight = new ModelRenderer(this);
		earRight.setPos(-5.5F, -6.0F, 0.0F);
		head.addChild(earRight);
		earRight.texOffs(39, 26).addBox(-0.5F, -1.0F, -3.0F, 1.0F, 6.0F, 6.0F, modelSize, false);

		//earLeft = new ModelRenderer(this);
		//earLeft.setPos(-5.25F + 2.0F, 2.0F - 4.0F, 0.0F);
		//earLeft.texOffs(14, 38).addBox(10.25F, -1.0F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);
		//this.head.addChild(this.earLeft);

		//earRight = new ModelRenderer(this);
		//earRight.setPos(5.5F - 2.0F, 2.0F - 4.0F, 0.0F);
		//earRight.texOffs(39, 26).addBox(-11.5F, -1.0F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);
		//this.head.addChild(this.earRight);

		this.hat = new ModelRenderer(this);

		this.bodyDefault = this.body.createShallowCopy();
		this.headDefault = this.head.createShallowCopy();
		this.leftArmDefault = this.leftArm.createShallowCopy();
		this.rightArmDefault = this.rightArm.createShallowCopy();
	}

	@Override
	public void setupAnim(T pigmy, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
		this.body.copyFrom(this.bodyDefault);
		this.head.copyFrom(this.headDefault);
		this.leftArm.copyFrom(this.leftArmDefault);
		this.rightArm.copyFrom(this.rightArmDefault);
		this.setupBipedAnim(pigmy, p_225597_2_, p_225597_3_, p_225597_4_, p_225597_5_, p_225597_6_);
		float f = ((float)Math.PI / 6F);
		float f1 = p_225597_4_ * 0.1F + p_225597_2_ * 0.5F;
		float f2 = 0.08F + p_225597_3_ * 0.4F;

		// Mojang has the ears named incorrectly, swapped them here
		this.earLeft.zRot = (-(float)Math.PI / 6F) - MathHelper.cos(f1 * 1.2F) * f2;
		this.earRight.zRot = ((float)Math.PI / 6F) + MathHelper.cos(f1) * f2;

		if (pigmy instanceof AbstractPiglinEntity) {
			AbstractPiglinEntity abstractpiglinentity = (AbstractPiglinEntity)pigmy;
			PiglinAction piglinaction = abstractpiglinentity.getArmPose();
			if (piglinaction == PiglinAction.DANCING) {
				float f3 = p_225597_4_ / 60.0F;

				// Mojang has the ears named incorrectly, swapped them here
				this.earRight.zRot = ((float)Math.PI / 6F) + ((float)Math.PI / 180F) * MathHelper.sin(f3 * 30.0F) * 10.0F;
				this.earLeft.zRot = (-(float)Math.PI / 6F) - ((float)Math.PI / 180F) * MathHelper.cos(f3 * 30.0F) * 10.0F;

				this.head.x = MathHelper.sin(f3 * 10.0F); //
				this.head.y = MathHelper.sin(f3 * 40.0F) + 0.4F; //
				this.rightArm.zRot = ((float)Math.PI / 180F) * (70.0F + MathHelper.cos(f3 * 40.0F) * 10.0F);
				this.leftArm.zRot = this.rightArm.zRot * -1.0F;
				this.rightArm.y = MathHelper.sin(f3 * 40.0F) * 0.5F + 1.5F; //
				this.leftArm.y = MathHelper.sin(f3 * 40.0F) * 0.5F + 1.5F; //
				this.body.y = MathHelper.sin(f3 * 40.0F) * 0.35F; //
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

	protected void setupBipedAnim(T pigmy, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_){
		boolean fallFlyingTicks = pigmy.getFallFlyingTicks() > 4;
		boolean isVisuallySwimming = pigmy.isVisuallySwimming();
		this.head.yRot = p_225597_5_ * ((float)Math.PI / 180F);
		if (fallFlyingTicks) {
			this.head.xRot = (-(float)Math.PI / 4F);
		} else if (this.swimAmount > 0.0F) {
			if (isVisuallySwimming) {
				this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, (-(float)Math.PI / 4F));
			} else {
				this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, p_225597_6_ * ((float)Math.PI / 180F));
			}
		} else {
			this.head.xRot = p_225597_6_ * ((float)Math.PI / 180F);
		}

		this.body.yRot = 0.0F;
		this.rightArm.z = 0.0F; // was 0.0F
		this.rightArm.x = -5.0F; // was -5.0F
		this.leftArm.z = 0.0F; // was 0.0F
		this.leftArm.x = 5.0F; // was 5.0F
		float f = 1.0F;
		if (fallFlyingTicks) {
			f = (float)pigmy.getDeltaMovement().lengthSqr();
			f = f / 0.2F;
			f = f * f * f;
		}

		if (f < 1.0F) {
			f = 1.0F;
		}

		this.rightArm.xRot = MathHelper.cos(p_225597_2_ * 0.6662F + (float)Math.PI) * 2.0F * p_225597_3_ * 0.5F / f;
		this.leftArm.xRot = MathHelper.cos(p_225597_2_ * 0.6662F) * 2.0F * p_225597_3_ * 0.5F / f;
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightLeg.xRot = MathHelper.cos(p_225597_2_ * 0.6662F) * 1.4F * p_225597_3_ / f;
		this.leftLeg.xRot = MathHelper.cos(p_225597_2_ * 0.6662F + (float)Math.PI) * 1.4F * p_225597_3_ / f;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
		this.rightLeg.zRot = 0.0F;
		this.leftLeg.zRot = 0.0F;
		if (this.riding) {
			this.rightArm.xRot += (-(float)Math.PI / 5F);
			this.leftArm.xRot += (-(float)Math.PI / 5F);
			this.rightLeg.xRot = -1.4137167F;
			this.rightLeg.yRot = ((float)Math.PI / 10F);
			this.rightLeg.zRot = 0.07853982F;
			this.leftLeg.xRot = -1.4137167F;
			this.leftLeg.yRot = (-(float)Math.PI / 10F);
			this.leftLeg.zRot = -0.07853982F;
		}

		this.rightArm.yRot = 0.0F;
		this.leftArm.yRot = 0.0F;
		boolean flag2 = pigmy.getMainArm() == HandSide.RIGHT;
		boolean flag3 = flag2 ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
		if (flag2 != flag3) {
			this.poseLeftArm(pigmy);
			this.poseRightArm(pigmy);
		} else {
			this.poseRightArm(pigmy);
			this.poseLeftArm(pigmy);
		}

		this.setupAttackAnimation(pigmy, p_225597_4_);
		if (this.crouching) {
			this.body.xRot = 0.5F;
			this.rightArm.xRot += 0.4F;
			this.leftArm.xRot += 0.4F;
			this.rightLeg.z = 4.0F; // was 4.0F
			this.leftLeg.z = 4.0F; // was 4.0F
			this.rightLeg.y = 18.2F; // was 12.2F
			this.leftLeg.y = 18.2F; // was 12.2F
			this.head.y = 12.2F; // was 4.2F
			this.body.y = 11.2F; // was 3.2F
			this.leftArm.y = 13.2F; // was 5.2F
			this.rightArm.y = 13.2F; // was 5.2F
		} else {
			this.body.xRot = 0.0F;
			this.rightLeg.z = 0.1F; // was 0.1F
			this.leftLeg.z = 0.1F; // was 0.1F
			this.rightLeg.y = 18.0F; // was 12.0F
			this.leftLeg.y = 18.0F; // was 12.0F
			this.head.y = 8.0F; // was 0.0F
			this.body.y = 8.0F; // was 0.0F
			this.leftArm.y = 10.0F; // was 2.0F
			this.rightArm.y = 10.0F; // was 2.0F
		}

		ModelHelper.bobArms(this.rightArm, this.leftArm, p_225597_4_);
		if (this.swimAmount > 0.0F) {
			float f1 = p_225597_2_ % 26.0F;
			HandSide handside = this.getAttackArm(pigmy);
			float f2 = handside == HandSide.RIGHT && this.attackTime > 0.0F ? 0.0F : this.swimAmount;
			float f3 = handside == HandSide.LEFT && this.attackTime > 0.0F ? 0.0F : this.swimAmount;
			if (f1 < 14.0F) {
				this.leftArm.xRot = this.rotlerpRad(f3, this.leftArm.xRot, 0.0F);
				this.rightArm.xRot = MathHelper.lerp(f2, this.rightArm.xRot, 0.0F);
				this.leftArm.yRot = this.rotlerpRad(f3, this.leftArm.yRot, (float)Math.PI);
				this.rightArm.yRot = MathHelper.lerp(f2, this.rightArm.yRot, (float)Math.PI);
				this.leftArm.zRot = this.rotlerpRad(f3, this.leftArm.zRot, (float)Math.PI + 1.8707964F * this.quadraticArmUpdate(f1) / this.quadraticArmUpdate(14.0F));
				this.rightArm.zRot = MathHelper.lerp(f2, this.rightArm.zRot, (float)Math.PI - 1.8707964F * this.quadraticArmUpdate(f1) / this.quadraticArmUpdate(14.0F));
			} else if (f1 >= 14.0F && f1 < 22.0F) {
				float f6 = (f1 - 14.0F) / 8.0F;
				this.leftArm.xRot = this.rotlerpRad(f3, this.leftArm.xRot, ((float)Math.PI / 2F) * f6);
				this.rightArm.xRot = MathHelper.lerp(f2, this.rightArm.xRot, ((float)Math.PI / 2F) * f6);
				this.leftArm.yRot = this.rotlerpRad(f3, this.leftArm.yRot, (float)Math.PI);
				this.rightArm.yRot = MathHelper.lerp(f2, this.rightArm.yRot, (float)Math.PI);
				this.leftArm.zRot = this.rotlerpRad(f3, this.leftArm.zRot, 5.012389F - 1.8707964F * f6);
				this.rightArm.zRot = MathHelper.lerp(f2, this.rightArm.zRot, 1.2707963F + 1.8707964F * f6);
			} else if (f1 >= 22.0F && f1 < 26.0F) {
				float f4 = (f1 - 22.0F) / 4.0F;
				this.leftArm.xRot = this.rotlerpRad(f3, this.leftArm.xRot, ((float)Math.PI / 2F) - ((float)Math.PI / 2F) * f4);
				this.rightArm.xRot = MathHelper.lerp(f2, this.rightArm.xRot, ((float)Math.PI / 2F) - ((float)Math.PI / 2F) * f4);
				this.leftArm.yRot = this.rotlerpRad(f3, this.leftArm.yRot, (float)Math.PI);
				this.rightArm.yRot = MathHelper.lerp(f2, this.rightArm.yRot, (float)Math.PI);
				this.leftArm.zRot = this.rotlerpRad(f3, this.leftArm.zRot, (float)Math.PI);
				this.rightArm.zRot = MathHelper.lerp(f2, this.rightArm.zRot, (float)Math.PI);
			}

			float f7 = 0.3F;
			float f5 = 0.33333334F;
			this.leftLeg.xRot = MathHelper.lerp(this.swimAmount, this.leftLeg.xRot, 0.3F * MathHelper.cos(p_225597_2_ * 0.33333334F + (float)Math.PI));
			this.rightLeg.xRot = MathHelper.lerp(this.swimAmount, this.rightLeg.xRot, 0.3F * MathHelper.cos(p_225597_2_ * 0.33333334F));
		}

		this.hat.copyFrom(this.head);
	}

	private void poseRightArm(T p_241654_1_) {
		switch(this.rightArmPose) {
			case EMPTY:
				this.rightArm.yRot = 0.0F;
				break;
			case BLOCK:
				this.rightArm.xRot = this.rightArm.xRot * 0.5F - 0.9424779F;
				this.rightArm.yRot = (-(float)Math.PI / 6F);
				break;
			case ITEM:
				this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float)Math.PI / 10F);
				this.rightArm.yRot = 0.0F;
				break;
			case THROW_SPEAR:
				this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float)Math.PI;
				this.rightArm.yRot = 0.0F;
				break;
			case BOW_AND_ARROW:
				this.rightArm.yRot = -0.1F + this.head.yRot;
				this.leftArm.yRot = 0.1F + this.head.yRot + 0.4F;
				this.rightArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
				this.leftArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
				break;
			case CROSSBOW_CHARGE:
				ModelHelper.animateCrossbowCharge(this.rightArm, this.leftArm, p_241654_1_, true);
				break;
			case CROSSBOW_HOLD:
				ModelHelper.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
		}

	}

	private void poseLeftArm(T p_241655_1_) {
		switch(this.leftArmPose) {
			case EMPTY:
				this.leftArm.yRot = 0.0F;
				break;
			case BLOCK:
				this.leftArm.xRot = this.leftArm.xRot * 0.5F - 0.9424779F;
				this.leftArm.yRot = ((float)Math.PI / 6F);
				break;
			case ITEM:
				this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float)Math.PI / 10F);
				this.leftArm.yRot = 0.0F;
				break;
			case THROW_SPEAR:
				this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float)Math.PI;
				this.leftArm.yRot = 0.0F;
				break;
			case BOW_AND_ARROW:
				this.rightArm.yRot = -0.1F + this.head.yRot - 0.4F;
				this.leftArm.yRot = 0.1F + this.head.yRot;
				this.rightArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
				this.leftArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
				break;
			case CROSSBOW_CHARGE:
				ModelHelper.animateCrossbowCharge(this.rightArm, this.leftArm, p_241655_1_, false);
				break;
			case CROSSBOW_HOLD:
				ModelHelper.animateCrossbowHold(this.rightArm, this.leftArm, this.head, false);
		}

	}

	private float quadraticArmUpdate(float p_203068_1_) {
		return -65.0F * p_203068_1_ + p_203068_1_ * p_203068_1_;
	}

}