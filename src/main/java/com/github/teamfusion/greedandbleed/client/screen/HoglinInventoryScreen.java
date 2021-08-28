package com.github.teamfusion.greedandbleed.client.screen;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.entity.IHasMountArmor;
import com.github.teamfusion.greedandbleed.common.entity.IHasMountInventory;
import com.github.teamfusion.greedandbleed.common.inventory.HoglinInventoryContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.IEquipable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings({ "deprecation", "NullableProblems" })
@OnlyIn(Dist.CLIENT)
public class HoglinInventoryScreen extends ContainerScreen<HoglinInventoryContainer> {
   private static final ResourceLocation HOGLIN_INVENTORY_LOCATION = new ResourceLocation(GreedAndBleed.MOD_ID, "textures/gui/container/hoglin.png");
   private final AnimalEntity hoglin;
   private float xMouse;
   private float yMouse;

   public HoglinInventoryScreen(HoglinInventoryContainer hoglinInventoryContainer, PlayerInventory playerInventory, AnimalEntity hoglin) {
      super(hoglinInventoryContainer, playerInventory, hoglin.getDisplayName());
      if(!(hoglin instanceof IEquipable)
              || !(hoglin instanceof IHasMountArmor)
              || !(hoglin instanceof IHasMountInventory)){
         throw new IllegalArgumentException("This entity type " + hoglin.getType() + " is not valid for HoglinInventoryScreen!");
      }
      this.hoglin = hoglin;
      this.passEvents = false;
   }

   @Override
   protected void renderBg(MatrixStack matrixStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      if (this.minecraft != null) this.minecraft.getTextureManager().bind(HOGLIN_INVENTORY_LOCATION);
      int i = (this.width - this.imageWidth) / 2;
      int j = (this.height - this.imageHeight) / 2;
      this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
      IHasMountInventory chestedHoglin = (IHasMountInventory)this.hoglin;
      if (chestedHoglin.hasChest()) {
         this.blit(matrixStack, i + 79, j + 17, 0, this.imageHeight, chestedHoglin.getInventoryColumns() * 18, 54);
      }

      if (((IEquipable)this.hoglin).isSaddleable()) {
         this.blit(matrixStack, i + 7, j + 35 - 18, 18, this.imageHeight + 54, 18, 18);
      }

      if (((IHasMountArmor)this.hoglin).canWearArmor()) {
         if (this.hoglin instanceof LlamaEntity) {
            this.blit(matrixStack, i + 7, j + 35, 36, this.imageHeight + 54, 18, 18);
         } else {
            this.blit(matrixStack, i + 7, j + 35, 0, this.imageHeight + 54, 18, 18);
         }
      }

      InventoryScreen.renderEntityInInventory(i + 51, j + 60, 17, (float)(i + 51) - this.xMouse, (float)(j + 75 - 50) - this.yMouse, this.hoglin);
   }

   @Override
   public void render(MatrixStack matrixStack, int xMouseIn, int yMouseIn, float p_230430_4_) {
      this.renderBackground(matrixStack);
      this.xMouse = (float)xMouseIn;
      this.yMouse = (float)yMouseIn;
      super.render(matrixStack, xMouseIn, yMouseIn, p_230430_4_);
      this.renderTooltip(matrixStack, xMouseIn, yMouseIn);
   }
}
