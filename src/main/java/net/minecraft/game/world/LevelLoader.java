package net.minecraft.game.world;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.client.IProgressUpdate;

public abstract class LevelLoader {
	private IProgressUpdate guiLoading;

	public LevelLoader(IProgressUpdate var1) {
		this.guiLoading = var1;
	}

	public final void load() {
		if(this.guiLoading != null) {
			this.guiLoading.displayProgressMessage("Saving level");
		}

		if(this.guiLoading != null) {
			this.guiLoading.displayLoadingString("Preparing level..");
		}

		new NBTTagCompound();
	}
}
