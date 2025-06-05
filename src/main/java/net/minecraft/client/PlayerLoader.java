package net.minecraft.client;

import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.LevelLoader;
import net.minecraft.game.level.World;

public final class PlayerLoader extends LevelLoader {
    private Minecraft a;

    public PlayerLoader(Minecraft var1, IProgressUpdate var2) {
        super(var2);
        this.a = var1;
    }

    protected final Entity loadEntity(World var1, String var2) {
        return (Entity)(var2.equals("LocalPlayer") ? new EntityPlayerSP(this.a, var1) : super.loadEntity(var1, var2));
    }
}
