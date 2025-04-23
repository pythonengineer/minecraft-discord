package net.minecraft.client.render;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.md3.MD3Loader;
import net.minecraft.client.model.md3.MD3Model;
import net.minecraft.game.level.World;

public final class RenderManager {
	MD3Model[] model = new MD3Model[1];
	public World worldObj;

	public RenderManager() {
		new ModelBiped();

		try {
			this.model[0] = new MD3Model((new MD3Loader()).loadModel("/test2.md3"));
		} catch (Exception var1) {
			var1.printStackTrace();
		}
	}
}
