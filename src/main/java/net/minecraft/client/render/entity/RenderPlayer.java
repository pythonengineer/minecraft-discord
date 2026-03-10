package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemArmor;
import net.minecraft.game.item.ItemStack;

public final class RenderPlayer extends RenderLiving {
	private ModelBiped modelBipedMain = (ModelBiped)this.mainModel;
	private ModelBiped modelArmorChestplate = new ModelBiped(1.0F);
	private ModelBiped modelArmor = new ModelBiped(0.5F);
	private static final String[] armorFilenamePrefix = new String[]{"cloth", "chain", "iron", "diamond", "gold"};

	public RenderPlayer() {
		super(new ModelBiped(0.0F), 0.5F);
	}

	private void renderPlayer(EntityPlayer playerEntity, double x, double y, double z, float yaw, float partialTicks) {
		super.renderLiving(playerEntity, x, y - (double)playerEntity.yOffset, z, yaw, partialTicks);
	}

	public final void drawFirstPersonHand() {
		this.modelBipedMain.bipedRightArm.render(1.0F);
	}

	protected final boolean shouldRenderPass(EntityLiving livingEntity, int flag) {
		EntityPlayer entityPlayer10001 = (EntityPlayer)livingEntity;
		int i3 = flag;
		EntityPlayer flag1 = entityPlayer10001;
		int i4 = 3 - i3;
		InventoryPlayer inventoryPlayer10 = flag1.inventory;
		ItemStack flag2;
		Item flag3;
		if((flag2 = flag1.inventory.armorInventory[i4]) != null && (flag3 = flag2.getItem()) instanceof ItemArmor) {
			ItemArmor flag4 = (ItemArmor)flag3;
			this.loadTexture("/armor/" + armorFilenamePrefix[flag4.renderIndex] + "_" + (i3 == 2 ? 2 : 1) + ".png");
			ModelBiped flag5 = i3 == 2 ? this.modelArmor : this.modelArmorChestplate;
			(flag5 = i3 == 2 ? this.modelArmor : this.modelArmorChestplate).bipedHead.showModel = i3 == 0;
			flag5.bipedHeadwear.showModel = i3 == 0;
			flag5.bipedBody.showModel = i3 == 1 || i3 == 2;
			flag5.bipedRightArm.showModel = i3 == 1;
			flag5.bipedLeftArm.showModel = i3 == 1;
			flag5.bipedRightLeg.showModel = i3 == 2 || i3 == 3;
			flag5.bipedLeftLeg.showModel = i3 == 2 || i3 == 3;
			this.setRenderPassModel(flag5);
			return true;
		} else {
			return false;
		}
	}

	public final void renderLiving(EntityLiving entityLiving1, double d2, double d4, double d6, float f8, float f9) {
		this.renderPlayer((EntityPlayer)entityLiving1, d2, d4, d6, f8, f9);
	}

	public final void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
		this.renderPlayer((EntityPlayer)entity, x, y, z, yaw, partialTicks);
	}
}
