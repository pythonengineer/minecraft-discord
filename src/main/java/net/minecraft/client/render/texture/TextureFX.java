package net.minecraft.client.render.texture;

public class TextureFX {
	public byte[] imageData = new byte[1024];
	public int iconIndex;
	public boolean anaglyphEnabled = false;
    public int textureId = 0;
    public int tileSize = 1;

    public TextureFX(int iconIndex) {
        this.iconIndex = iconIndex;
    }

	public void onTick() {
	}
}
