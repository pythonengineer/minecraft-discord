package com.mojang.minecraft.level;

import com.mojang.minecraft.LevelLoaderListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public final class LevelIO {
    private LevelLoaderListener levelLoaderListener;

    public LevelIO(LevelLoaderListener levelLoaderListener) {
        this.levelLoaderListener = levelLoaderListener;
    }

    public final boolean save(Level level, String host, String userName, String sessionId, String name, int id) {
		return false;
	}

    public final Level load(String host, String owner, int id) {
		return null;
	}

    public final Level load(DataInputStream dataInputStream10) {
        if(this.levelLoaderListener != null) {
            this.levelLoaderListener.beginLevelLoading("Loading level");
        }

        if(this.levelLoaderListener != null) {
            this.levelLoaderListener.levelLoadUpdate("Reading..");
        }

		try {
            if(dataInputStream10.readInt() != 656127880) {
                return null;
            } else {
                byte b12;
                if((b12 = dataInputStream10.readByte()) > 2) {
                    return null;
                } else if (b12 == 1) {
                    String string14 = dataInputStream10.readUTF();
                    String string15 = dataInputStream10.readUTF();
                    long j7 = dataInputStream10.readLong();
                    short s3 = dataInputStream10.readShort();
                    short s4 = dataInputStream10.readShort();
                    short s5 = dataInputStream10.readShort();
                    byte[] b6 = new byte[s3 * s4 * s5];
                    dataInputStream10.readFully(b6);
                    dataInputStream10.close();
                    Level level11;
                    (level11 = new Level()).setData(s3, s5, s4, b6);
                    level11.name = string14;
                    level11.creator = string15;
                    level11.createTime = j7;
                    return level11;
                } else if (b12 == 2) {
                    String string14 = dataInputStream10.readUTF();
                    String string15 = dataInputStream10.readUTF();
                    long j7 = dataInputStream10.readLong();
                    short s3 = dataInputStream10.readShort();
                    short s4 = dataInputStream10.readShort();
                    short s5 = dataInputStream10.readShort();
                    int xSpawn = dataInputStream10.readInt();
                    int ySpawn = dataInputStream10.readInt();
                    int zSpawn = dataInputStream10.readInt();
                    byte[] b6 = new byte[s3 * s4 * s5];
                    dataInputStream10.readFully(b6);
                    dataInputStream10.close();
                    Level level11;
                    (level11 = new Level()).setData(s3, s5, s4, b6);
                    level11.name = string14;
                    level11.creator = string15;
                    level11.createTime = j7;
                    level11.xSpawn = xSpawn;
                    level11.ySpawn = ySpawn;
                    level11.zSpawn = zSpawn;
                    return level11;
                } else {
                    return null;
                }
            }
        } catch (Exception exception9) {
            exception9.printStackTrace();
            (new StringBuilder()).append("Failed to load level: ").append(exception9.toString()).toString();
            return null;
        }
	}

	public static void save(Level level0, DataOutputStream dataOutputStream3) {
	    if (level0 == null) {
	        return;
	    }
		try {
			dataOutputStream3.writeInt(656127880);
			dataOutputStream3.writeByte(2);
			dataOutputStream3.writeUTF(level0.name);
			dataOutputStream3.writeUTF(level0.creator);
			dataOutputStream3.writeLong(level0.createTime);
			dataOutputStream3.writeShort(level0.width);
			dataOutputStream3.writeShort(level0.height);
			dataOutputStream3.writeShort(level0.depth);
            dataOutputStream3.writeInt(level0.xSpawn);
            dataOutputStream3.writeInt(level0.ySpawn);
            dataOutputStream3.writeInt(level0.zSpawn);
			dataOutputStream3.write(level0.blocks);
			dataOutputStream3.close();
		} catch (Exception exception2) {
			exception2.printStackTrace();
		}
	}

    public static byte[] loadBlocks(InputStream inputStream0) {
        try {
            DataInputStream dataInputStream3;
            byte[] b1 = new byte[(dataInputStream3 = new DataInputStream(new GZIPInputStream(inputStream0))).readInt()];
            dataInputStream3.readFully(b1);
            dataInputStream3.close();
            return b1;
        } catch (Exception exception2) {
            throw new RuntimeException(exception2);
        }
    }
}
