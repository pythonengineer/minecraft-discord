package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemArmor;
import net.minecraft.game.item.ItemStack;

public class RenderPlayer extends RenderLiving {
	private ModelBiped modelBipedMain = (ModelBiped)this.mainModel;
	private ModelBiped modelArmorChestplate = new ModelBiped(1.0F);
	private ModelBiped modelArmor = new ModelBiped(0.5F);
	private static final String[] armorFilenamePrefix = new String[]{"cloth", "chain", "iron", "diamond", "gold"};

	public RenderPlayer() {
		super(new ModelBiped(0.0F), 0.5F);
	}

    protected boolean shouldRenderPass(EntityPlayer player, int flag) {
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
		super.renderLiving(playerEntity, x, y - (double)playerEntity.yOffset, z, yaw, partialTicks);
	}

	public void drawFirstPersonHand() {
        this.modelBipedMain.bipedRightArm.render(0.0625F);
	}

    protected boolean shouldRenderPass(EntityLiving livingEntity, int flag) {
        return this.shouldRenderPass((EntityPlayer)livingEntity, flag);
    }

	public void renderLiving(EntityLiving entityLiving1, double d2, double d4, double d6, float f8, float f9) {
		this.renderPlayer((EntityPlayer)entityLiving1, d2, d4, d6, f8, f9);
	}

	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
		this.renderPlayer((EntityPlayer)entity, x, y, z, yaw, partialTicks);
	}
}
