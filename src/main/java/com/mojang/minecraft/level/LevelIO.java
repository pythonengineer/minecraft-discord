package com.mojang.minecraft.level;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

public class LevelIO {
	private static final int MAGIC_NUMBER = 656127880;
	private static final int CURRENT_VERSION = 1;
	private LevelLoaderListener levelLoaderListener;
	public String error = null;

	public LevelIO(LevelLoaderListener levelLoaderListener) {
		this.levelLoaderListener = levelLoaderListener;
	}

	public boolean load(Level level, DataInputStream in) {
		this.levelLoaderListener.beginLevelLoading("Loading level");
		this.levelLoaderListener.levelLoadUpdate("Reading..");

		try {
			int magic = in.readInt();
			if(magic != 656127880) {
				this.error = "Bad level file format";
				return false;
			} else {
				byte version = in.readByte();
				if(version > 1) {
					this.error = "Bad level file format";
					return false;
				} else {
					String name = in.readUTF();
					String creator = in.readUTF();
					long createTime = in.readLong();
					short width = in.readShort();
					short height = in.readShort();
					short depth = in.readShort();
					byte[] blocks = new byte[width * height * depth];
					in.readFully(blocks);
					in.close();
					level.setData(width, depth, height, blocks);
					level.name = name;
					level.creator = creator;
					level.createTime = createTime;
					return true;
				}
			}
		} catch (Exception var14) {
			var14.printStackTrace();
			this.error = "Failed to load level: " + var14.toString();
			return false;
		}
	}

	public boolean loadLegacy(Level level, DataInputStream in) {
		this.levelLoaderListener.beginLevelLoading("Loading level");
		this.levelLoaderListener.levelLoadUpdate("Reading..");

		try {
			String name = "--";
			String creator = "unknown";
			long createTime = 0L;
			short width = 256;
			short height = 256;
			byte depth = 64;
			byte[] blocks = new byte[width * height * depth];
			in.readFully(blocks);
			in.close();
			level.setData(width, depth, height, blocks);
			level.name = name;
			level.creator = creator;
			level.createTime = createTime;
			return true;
		} catch (Exception var12) {
			var12.printStackTrace();
			this.error = "Failed to load level: " + var12.toString();
			return false;
		}
	}

	public void save(Level level, DataOutputStream out) {
		try {
		    out.writeInt(656127880);
		    out.writeByte(1);
		    out.writeUTF(level.name);
		    out.writeUTF(level.creator);
		    out.writeLong(level.createTime);
		    out.writeShort(level.width);
		    out.writeShort(level.height);
		    out.writeShort(level.depth);
		    out.write(level.blocks);
		    out.close();
		} catch (Exception var4) {
			var4.printStackTrace();
		}

	}

    public void delete() {
        VFile2 f = new VFile2("level.dat");
        if (f.exists()) {
            f.delete();
        }
    }
}
