package net.minecraft.client.render.entity;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemArmor;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.block.Block;

public class RenderPlayer extends RenderLiving {
	private ModelBiped modelBipedMain = (ModelBiped)this.mainModel;
	private ModelBiped modelArmorChestplate = new ModelBiped(1.0F);
	private ModelBiped modelArmor = new ModelBiped(0.5F);
	private static final String[] armorFilenamePrefix = new String[]{"cloth", "chain", "iron", "diamond", "gold"};

	public RenderPlayer() {
		super(new ModelBiped(0.0F), 0.5F);
	}

    protected boolean setArmorModel(EntityPlayer player, int flag) {
        ItemStack itemStack4 = player.inventory.armorItemInSlot[3 - flag];
        if(itemStack4 != null) {
            Item item5 = itemStack4.getItem();
            if(item5 instanceof ItemArmor) {
                ItemArmor itemArmor6 = (ItemArmor)item5;
                this.loadTexture("/armor/" + armorFilenamePrefix[itemArmor6.renderIndex] + "_" + (flag == 2 ? 2 : 1) + ".png");
                ModelBiped modelBiped7 = flag == 2 ? this.modelArmor : this.modelArmorChestplate;
                modelBiped7.bipedHead.showModel = flag == 0;
                modelBiped7.bipedHeadwear.showModel = flag == 0;
                modelBiped7.bipedBody.showModel = flag == 1 || flag == 2;
                modelBiped7.bipedRightArm.showModel = flag == 1;
                modelBiped7.bipedLeftArm.showModel = flag == 1;
                modelBiped7.bipedRightLeg.showModel = flag == 2 || flag == 3;
                modelBiped7.bipedLeftLeg.showModel = flag == 2 || flag == 3;
                this.setRenderPassModel(modelBiped7);
                return true;
            }
        }

        return false;
    }

	private void renderPlayer(EntityPlayer playerEntity, double x, double y, double z, float yaw, float partialTicks) {
		super.doRenderLiving(playerEntity, x, y - (double)playerEntity.yOffset, z, yaw, partialTicks);
	}

    protected void renderSpecials(EntityPlayer playerEntity, float partialTicks) {
        ItemStack itemStack3 = playerEntity.inventory.getCurrentItem();
        if(itemStack3 != null) {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedRightArm.renderWithRotation(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
            float f4 = 0.625F;
            if(itemStack3.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemStack3.itemID].getRenderType())) {
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                f4 *= 0.75F;
                GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
                GL11.glScalef(f4, -f4, f4);
            } else {
                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                GL11.glScalef(f4, -f4, f4);
                GL11.glRotatef(-120.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            }

            this.renderManager.itemRenderer.renderItem(itemStack3);
            GL11.glPopMatrix();
        }

    }

	public void drawFirstPersonHand() {
        this.modelBipedMain.bipedRightArm.render(0.0625F);
	}

    protected boolean shouldRenderPass(EntityLiving livingEntity, int flag) {
        return this.setArmorModel((EntityPlayer)livingEntity, flag);
    }

    protected void renderEquippedItems(EntityLiving livingEntity, float partialTicks) {
        this.renderSpecials((EntityPlayer)livingEntity, partialTicks);
    }

	public void doRenderLiving(EntityLiving entityLiving, double x, double y, double z, float yaw, float partialTicks) {
		this.renderPlayer((EntityPlayer)entityLiving, x, y, z, yaw, partialTicks);
	}

	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
		this.renderPlayer((EntityPlayer)entity, x, y, z, yaw, partialTicks);
	}
}
