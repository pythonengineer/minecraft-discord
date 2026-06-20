package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;

public class TexturePackList {
	private List availableTexturePacks = new ArrayList();
	private TexturePackBase defaultTexturePack = new TexturePackDefault();
	public TexturePackBase selectedTexturePack;
	private Map field_6538_d = new HashMap();
	private Minecraft mc;
	private String field_6535_g;

	public TexturePackList(Minecraft var1) {
		this.mc = var1;
		this.field_6535_g = var1.gameSettings.skin;
		this.func_6532_a();
		this.selectedTexturePack.func_6482_a();
	}

	public boolean setTexturePack(TexturePackBase var1) {
		if(var1 == this.selectedTexturePack) {
			return false;
		} else {
			this.selectedTexturePack.closeTexturePackFile();
			this.field_6535_g = var1.texturePackFileName;
			this.selectedTexturePack = var1;
			this.mc.gameSettings.skin = this.field_6535_g;
			this.mc.gameSettings.saveOptions();
			this.selectedTexturePack.func_6482_a();
			return true;
		}
	}

	public void func_6532_a() {
		ArrayList var1 = new ArrayList();
		this.selectedTexturePack = null;
		var1.add(this.defaultTexturePack);
		if(this.selectedTexturePack == null) {
			this.selectedTexturePack = this.defaultTexturePack;
		}

		this.availableTexturePacks.removeAll(var1);
		Iterator var10 = this.availableTexturePacks.iterator();

		while(var10.hasNext()) {
			TexturePackBase var11 = (TexturePackBase)var10.next();
			var11.func_6484_b(this.mc);
			this.field_6538_d.remove(var11.field_6488_d);
		}

		this.availableTexturePacks = var1;
	}

	public List availableTexturePacks() {
		return new ArrayList(this.availableTexturePacks);
	}
}
