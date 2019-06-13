package tschallacka.mods.rats.render.model.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.entity.RenderItemFrame;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tschallacka.mods.rats.items.Items;
import tschallacka.mods.rats.items.RatCorpse;

@SideOnly(Side.CLIENT)
public class ModelRat extends ModelBase
{
  //fields
	ModelRenderer rat;
    ModelRenderer tail;
    ModelRenderer body;
    ModelRenderer head;
    ModelRenderer leftfrontleg;
    ModelRenderer rightfrontleg;
    ModelRenderer leftbehindleg;
    ModelRenderer rightbehindleg;
    protected float childYOffset = 8.0F;
    protected float childZOffset = 4.0F;
    
  public ModelRat()
  {
	  textureWidth = 32;
	    textureHeight = 32;
	    setTextureOffset("head.face", 0, 14);
	    setTextureOffset("head.nose", 0, 18);
	    setTextureOffset("head.rightear", 0, 21);
	    setTextureOffset("head.leftear", 2, 21);
	    setTextureOffset("tail.tailbody", 0, 0);
	    setTextureOffset("tail.tailend", 6, 0);
	    setTextureOffset("tail.tailmiddle", 0, 0);
	    setTextureOffset("body.bodychest", 12, 0);
	    setTextureOffset("body.bodyneck", 0, 3);
	    setTextureOffset("body.ass", 0, 6);
	    setTextureOffset("rightfrontleg.leg", 4, 3);
	    setTextureOffset("leftfrontleg.leg", 8, 3);
	    setTextureOffset("leftbehindleg.leftleg", 0, 8);
	    setTextureOffset("leftbehindleg.leftfoot", 0, 12);
	    setTextureOffset("rightbehindleg.rightleg", 6, 8);
	    setTextureOffset("rightbehindleg.leftfoot", 4, 12);
	    
	    rat = new ModelRenderer(this, "rat");
	    rat.setRotationPoint(-2F, 21F, 0F);
	    setRotation(rat, 0F, 0F, 0F);
	    rat.mirror = true;
	    head = new ModelRenderer(this, "head");
	    head.setRotationPoint(1F, 0F, -5F);
	    setRotation(head, 0F, 0F, 0F);
	    head.mirror = true;
	      head.addBox("face", -1F, 0F, -1F, 3, 2, 2);
	      head.addBox("nose", 0F, 1F, -3F, 1, 1, 2);
	      head.addBox("rightear", -1F, -1F, 1F, 1, 1, 0);
	      head.addBox("leftear", 1F, -1F, 1F, 1, 1, 0);
	      rat.addChild(head);
	    tail = new ModelRenderer(this, "tail");
	    tail.setRotationPoint(1F, 2F, 4F);
	    setRotation(tail, 0F, 0F, 0F);
	    tail.mirror = true;
	      tail.addBox("tailbody", 0F, 0F, -1F, 1, 1, 2);
	      tail.addBox("tailend", 0F, 0F, 2F, 1, 1, 2);
	      tail.addBox("tailmiddle", 1F, 0F, 1F, 1, 1, 1);
	      rat.addChild(tail);
	    body = new ModelRenderer(this, "body");
	    body.setRotationPoint(1F, 0F, -2F);
	    setRotation(body, 0F, 0F, 0F);
	    body.mirror = true;
	      body.addBox("bodychest", -1F, 0F, -1F, 3, 2, 5);
	      body.addBox("bodyneck", 0F, 1F, -3F, 1, 1, 2);
	      body.addBox("ass", -1F, 1F, 4F, 3, 1, 1);
	    rightfrontleg = new ModelRenderer(this, "rightfrontleg");
	    rightfrontleg.setRotationPoint(-1F, 1F, 0F);
	    setRotation(rightfrontleg, 0F, 0F, 0F);
	    rightfrontleg.mirror = true;
	      rightfrontleg.addBox("leg", -1F, 0F, -1F, 1, 2, 1);
	      body.addChild(rightfrontleg);
	    leftfrontleg = new ModelRenderer(this, "leftfrontleg");
	    leftfrontleg.setRotationPoint(2F, 1F, -1F);
	    setRotation(leftfrontleg, 0F, 0F, 0F);
	    leftfrontleg.mirror = true;
	      leftfrontleg.addBox("leg", 0F, 0F, 0F, 1, 2, 1);
	      body.addChild(leftfrontleg);
	    leftbehindleg = new ModelRenderer(this, "leftbehindleg");
	    leftbehindleg.setRotationPoint(2F, 2F, 3F);
	    setRotation(leftbehindleg, 0F, 0F, 0F);
	    leftbehindleg.mirror = true;
	      leftbehindleg.addBox("leftleg", 0F, -1F, 0F, 1, 2, 2);
	      leftbehindleg.addBox("leftfoot", 0F, 0F, -1F, 1, 1, 1);
	      body.addChild(leftbehindleg);
	    rightbehindleg = new ModelRenderer(this, "rightbehindleg");
	    rightbehindleg.setRotationPoint(-1F, 1F, 3F);
	    setRotation(rightbehindleg, 0F, 0F, 0F);
	    rightbehindleg.mirror = true;
	      rightbehindleg.addBox("rightleg", -1F, 0F, 0F, 1, 2, 2);
	      rightbehindleg.addBox("leftfoot", -1F, 1F, -1F, 1, 1, 1);
	      body.addChild(rightbehindleg);
	      rat.addChild(body);
  }
  
  public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
  {
	  this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
	  if(this.isChild) {
		  GlStateManager.pushMatrix();
	      GlStateManager.scale(0.5F, 0.5F, 0.5F);
	      GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
	      this.rat.render(scale);
	      GlStateManager.popMatrix();
	  }
	  else {
		  rat.render(scale);
	  }
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  /**
   * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
   * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
   * "far" arms and legs can swing at most.
   */
  public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
  {
	  
      	  super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
	      this.head.rotateAngleX = headPitch * 0.017453292F;
          this.head.rotateAngleY = netHeadYaw * 0.017453292F;
          this.head.rotateAngleZ = 0.0F;
          head.setRotationPoint(1F, 0F, -6F);
          //this.body.rotateAngleX = ((float)Math.PI / 2F);
          this.leftfrontleg.rotateAngleX = MathHelper.cos(limbSwing * 4.6662F) * 1.4F * limbSwingAmount;
          this.leftbehindleg.rotateAngleX = MathHelper.cos(limbSwing * 4.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
          this.rightbehindleg.rotateAngleX = MathHelper.cos(limbSwing * 4.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
          this.rightfrontleg.rotateAngleX = MathHelper.cos(limbSwing * 4.6662F) * 1.4F * limbSwingAmount;
          this.tail.rotateAngleY = MathHelper.cos(limbSwing * 2.6662F) * 0.8F * limbSwingAmount;
  }

}