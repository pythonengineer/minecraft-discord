package com.mojang.minecraft.level;

import com.mojang.minecraft.Minecraft;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public final class LevelIO {
	private Minecraft minecraft;

	public LevelIO(Minecraft minecraft1) {
		this.minecraft = minecraft1;
	}

	public final boolean save(Level level1, String string2, String string3, String string4, String string5, int i6) {
		return false;
	}

	public final boolean load(Level level1, String string2, String string3, int i4) {
		return false;
	}

	public final boolean load(Level level1, DataInputStream dataInputStream11) {
		this.minecraft.beginLevelLoading("Loading level");
		this.minecraft.levelLoadUpdate("Reading..");

		try {
			if(dataInputStream11.readInt() != 656127880) {
				return false;
			} else if(dataInputStream11.readByte() > 1) {
				return false;
			} else {
				String string12 = dataInputStream11.readUTF();
				String string3 = dataInputStream11.readUTF();
				long j8 = dataInputStream11.readLong();
				short s4 = dataInputStream11.readShort();
				short s5 = dataInputStream11.readShort();
				short s6 = dataInputStream11.readShort();
				byte[] b7 = new byte[s4 * s5 * s6];
				dataInputStream11.readFully(b7);
				dataInputStream11.close();
				level1.setData(s4, s6, s5, b7);
				level1.name = string12;
				level1.creator = string3;
				level1.createTime = j8;
				return true;
			}
		} catch (Exception exception10) {
			exception10.printStackTrace();
			(new StringBuilder()).append("Failed to load level: ").append(exception10.toString()).toString();
			return false;
		}
	}

	public final boolean loadLegacy(Level level1, DataInputStream dataInputStream6) {
		this.minecraft.beginLevelLoading("Loading level");
		this.minecraft.levelLoadUpdate("Reading..");

		try {
			String string7 = "--";
			String string3 = "unknown";
			byte[] b4 = new byte[256 << 8 << 6];
			dataInputStream6.readFully(b4);
			dataInputStream6.close();
			level1.setData(256, 64, 256, b4);
			level1.name = string7;
			level1.creator = string3;
			level1.createTime = 0L;
			return true;
		} catch (Exception exception5) {
			exception5.printStackTrace();
			(new StringBuilder()).append("Failed to load level: ").append(exception5.toString()).toString();
			return false;
		}
	}

	public static void save(Level level0, DataOutputStream dataOutputStream3) {
		try {
			dataOutputStream3.writeInt(656127880);
			dataOutputStream3.writeByte(1);
			dataOutputStream3.writeUTF(level0.name);
			dataOutputStream3.writeUTF(level0.creator);
			dataOutputStream3.writeLong(level0.createTime);
			dataOutputStream3.writeShort(level0.width);
			dataOutputStream3.writeShort(level0.height);
			dataOutputStream3.writeShort(level0.depth);
			dataOutputStream3.write(level0.blocks);
			dataOutputStream3.close();
		} catch (Exception exception2) {
			exception2.printStackTrace();
		}
	}
}
