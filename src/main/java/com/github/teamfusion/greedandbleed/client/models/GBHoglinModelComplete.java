package com.github.teamfusion.greedandbleed.client.models;

import com.github.teamfusion.greedandbleed.api.IHogEquipable;
import com.github.teamfusion.greedandbleed.common.entity.IHasMountInventory;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.IFlinging;
import net.minecraft.util.math.MathHelper;

@SuppressWarnings({ "FieldCanBeLocal", "NullableProblems" })
public class GBHoglinModelComplete<T extends MobEntity & IFlinging> extends AgeableModel<T> {
    private final ModelRenderer head;
    private final ModelRenderer tusks;
    private final ModelRenderer tusksHarness;
    private final ModelRenderer leftEar;
    private final ModelRenderer rightEar;
    private final ModelRenderer headHarness;
    //private final ModelRenderer headArmor;

    private final ModelRenderer body;
    private final ModelRenderer leftChest;
    private final ModelRenderer rightChest;
    private final ModelRenderer mane;
    //private final ModelRenderer bodyArmor;
    private final ModelRenderer saddle;

    private final ModelRenderer frontLeftLeg;
    private final ModelRenderer backLeftLeg;
    private final ModelRenderer frontRightLeg;
    private final ModelRenderer backRightLeg;

    public GBHoglinModelComplete(float modelSize) {
        super(true, 8.0F, 6.0F, 1.9F, 2.0F, 24.0F);
        this.texWidth = 128;
        this.texHeight = 128;

        head = new ModelRenderer(this);
        head.setPos(1.0F, 0.0F, -8.5F);
        setRotationAngle(head, 0.7854F, 0.0F, 0.0F);
        head.texOffs(61, 1).addBox(-8.0F, -1.0F, -20.0F, 14.0F, 6.0F, 19.0F, modelSize, false);

        headHarness = new ModelRenderer(this);
        headHarness.setPos(-1.0F, 24.0F, 8.5F);
        head.addChild(headHarness);
        headHarness.texOffs(62, 65).addBox(-7.0F, -25.0F, -28.5F, 14.0F, 6.0F, 19.0F, modelSize + 0.25F, false);

        /*
        headArmor = new ModelRenderer(this);
        headArmor.setPos(-1.0F, 24.0F, 8.5F);
        head.addChild(headArmor);
        headArmor.texOffs(61, 1).addBox(-7.0F, -25.0F, -28.5F, 14.0F, 6.0F, 19.0F, 0.5F, false);
         */

        tusks = new ModelRenderer(this);
        tusks.setPos(-1.0F, 24.0F, 8.5F);
        head.addChild(tusks);
        tusks.texOffs(1, 13).addBox(6.0F, -31.25F, -22.5F, 2.0F, 11.0F, 2.0F, modelSize, false);
        tusks.texOffs(120, 0).addBox(-8.0F, -28.25F, -22.5F, 2.0F, 5.0F, 2.0F, modelSize + 0.25F, true);

        tusksHarness = new ModelRenderer(this);
        tusksHarness.setPos(0.0F, 0.0F, 0.0F);
        tusks.addChild(tusksHarness);
        tusksHarness.texOffs(120, 0).addBox(6.0F, -28.25F, -22.5F, 2.0F, 5.0F, 2.0F, modelSize + 0.25F, false);
        tusksHarness.texOffs(10, 13).addBox(-8.0F, -31.25F, -22.5F, 2.0F, 11.0F, 2.0F, modelSize, false);

        leftEar = new ModelRenderer(this);
        leftEar.setPos(6.0F, -1.25F, -5.0F);
        head.addChild(leftEar);
        setRotationAngle(leftEar, 0.0F, 0.0F, 0.5236F);
        leftEar.texOffs(1, 6).addBox(0.125F, 0.7165F, -1.0F, 6.0F, 1.0F, 4.0F, modelSize, false);
        leftEar.texOffs(1, 6).addBox(0.125F, 0.7165F, -1.0F, 6.0F, 1.0F, 4.0F, modelSize + 0.25F, false);

        rightEar = new ModelRenderer(this);
        rightEar.setPos(-8.0F, -1.25F, -5.0F);
        head.addChild(rightEar);
        setRotationAngle(rightEar, 0.0F, 0.0F, -0.5236F);
        rightEar.texOffs(1, 1).addBox(-6.125F, 0.7165F, -1.0F, 6.0F, 1.0F, 4.0F, modelSize, false);
        rightEar.texOffs(1, 1).addBox(-6.125F, 0.7165F, -1.0F, 6.0F, 1.0F, 4.0F, modelSize + 0.25F, false);

        body = new ModelRenderer(this);
        body.setPos(0.0F, 6.0F, 17.0F);
        body.texOffs(1, 1).addBox(-8.0F, -6.0F, -25.5F, 16.0F, 14.0F, 26.0F, modelSize, false);


        saddle = new ModelRenderer(this);
        saddle.setPos(0.0F, 18.0F, -17.0F);
        body.addChild(saddle);
        saddle.texOffs(1, 63).addBox(-8.0F, -24.0F, -8.5F, 16.0F, 16.0F, 26.0F, 0.25F, false);

        /*
        bodyArmor = new ModelRenderer(this);
        bodyArmor.setPos(0.0F, 18.0F, -17.0F);
        body.addChild(bodyArmor);
        bodyArmor.texOffs(1, 1).addBox(-8.0F, -24.0F, -8.5F, 16.0F, 14.0F, 26.0F, 0.1F, false);
         */

        leftChest = new ModelRenderer(this);
        leftChest.setPos(0.0F, 18.0F, -17.0F);
        body.addChild(leftChest);
        leftChest.texOffs(0, 67).addBox(8.0F, -22.0F, 10.0F, 3.0F, 8.0F, 8.0F, 0.0F, false);

        rightChest = new ModelRenderer(this);
        rightChest.setPos(0.0F, 18.0F, -17.0F);
        body.addChild(rightChest);
        rightChest.texOffs(0, 67).addBox(-11.0F, -22.0F, 10.0F, 3.0F, 8.0F, 8.0F, 0.0F, true);

        mane = new ModelRenderer(this);
        mane.setPos(0.0F, 18.0F, -17.0F);
        body.addChild(mane);
        mane.texOffs(90, 33).addBox(0.0F, -32.0F, -11.5F, 0.0F, 10.0F, 19.0F, 0.0F, false);

        frontLeftLeg = new ModelRenderer(this);
        frontLeftLeg.setPos(4.0F, 13.0F, -5.5F);
        frontLeftLeg.texOffs(41, 42).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 14.0F, 6.0F, 0.0F, false);
        frontLeftLeg.texOffs(41, 42).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 14.0F, 6.0F, 0.25F, false);

        backLeftLeg = new ModelRenderer(this);
        backLeftLeg.setPos(4.5F, 18.5F, 12.0F);
        backLeftLeg.texOffs(0, 45).addBox(-2.5F, -5.5F, -0.5F, 5.0F, 11.0F, 5.0F, 0.0F, false);
        backLeftLeg.texOffs(0, 45).addBox(-2.5F, -5.5F, -0.5F, 5.0F, 11.0F, 5.0F, 0.25F, false);

        frontRightLeg = new ModelRenderer(this);
        frontRightLeg.setPos(-4.0F, 13.0F, -5.5F);
        frontRightLeg.texOffs(66, 42).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 14.0F, 6.0F, 0.0F, false);
        frontRightLeg.texOffs(66, 42).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 14.0F, 6.0F, 0.25F, false);

        backRightLeg = new ModelRenderer(this);
        backRightLeg.setPos(-4.5F, 13.0F, 13.0F);
        backRightLeg.texOffs(21, 45).addBox(-2.5F, 0.0F, -1.5F, 5.0F, 11.0F, 5.0F, 0.0F, false);
        backRightLeg.texOffs(21, 45).addBox(-2.5F, 0.0F, -1.5F, 5.0F, 11.0F, 5.0F, 0.25F, false);
    }

    @Override
    protected Iterable<ModelRenderer> headParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelRenderer> bodyParts() {
        return ImmutableList.of(this.body, this.frontRightLeg, this.frontLeftLeg, this.backRightLeg, this.backLeftLeg);
    }

    @Override
    public void setupAnim(T hoglin, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
        this.rightEar.zRot = -0.6981317F - p_225597_3_ * MathHelper.sin(p_225597_2_);
        this.leftEar.zRot = 0.6981317F + p_225597_3_ * MathHelper.sin(p_225597_2_);
        this.head.yRot = p_225597_5_ * ((float)Math.PI / 180F);
        int attackAnimTicksLeft = hoglin.getAttackAnimationRemainingTicks();
        float f = 1.0F - (float)MathHelper.abs(10 - 2 * attackAnimTicksLeft) / 10.0F;
        this.head.xRot = MathHelper.lerp(f, 0.87266463F, -0.34906584F);
        if (hoglin.isBaby()) {
            this.head.y = MathHelper.lerp(f,
                    0.0F, // was 2.0F
                    3.0F // was 5.0F
            );
            this.mane.z = -17.0F; // was -3.0F
        } else {
            this.head.y = 0.0F; // was 2.0F
            this.mane.z = -17.0F; // was -7.0F
        }

        this.frontRightLeg.xRot = MathHelper.cos(p_225597_2_) * 1.2F * p_225597_3_;
        this.frontLeftLeg.xRot = MathHelper.cos(p_225597_2_ + (float) Math.PI) * 1.2F * p_225597_3_;
        this.backRightLeg.xRot = this.frontLeftLeg.xRot;
        this.backLeftLeg.xRot = this.frontRightLeg.xRot;

        // SADDLE
        if (hoglin instanceof IHogEquipable) {
            IHogEquipable equipable = (IHogEquipable) hoglin;
            saddle.visible = equipable.isHogSaddled();
            headHarness.visible = equipable.isHogSaddled();
            tusksHarness.visible = equipable.isHogSaddled();
        } else {
            saddle.visible = false;
            headHarness.visible = false;
            tusksHarness.visible = false;
        }

        // CHEST
        boolean hasChest = hoglin instanceof IHasMountInventory && ((IHasMountInventory) hoglin).hasChest();
        if (hasChest) {
            this.leftChest.visible = true;
            this.rightChest.visible = true;
        } else {
            this.leftChest.visible = false;
            this.rightChest.visible = false;
        }

        // ARMOR
        /*boolean hasArmor = false;
        if(hasArmor){
            this.headArmor.visible = true;
            this.bodyArmor.visible = true;
        }
        else{
            this.headArmor.visible = false;
            this.bodyArmor.visible = false;
        }
         */
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
