package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class NBTBase {
	private String key = null;

	abstract void writeTagContents(DataOutput dataOutput1) throws IOException;

	abstract void readTagContents(DataInput dataInput1) throws IOException;

	public abstract byte getType();

	public final String getKey() {
		return this.key == null ? "" : this.key;
	}

	public final NBTBase setKey(String key) {
		this.key = key;
		return this;
	}

    public static NBTBase read(DataInput dataInput, byte var1) throws IOException {
        if(var1 == 0) {
            return new NBTTagEnd();
        } else {
            NBTBase nBTBase3 = createTagOfType(var1);
            short var2 = dataInput.readShort();
            byte[] var4 = new byte[var2];
            dataInput.readFully(var4);
            nBTBase3.key = new String(var4, "UTF-8");
            nBTBase3.readTagContents(dataInput);
            return nBTBase3;
        }
    }

    public static NBTBase readNamedTag(DataInput dataInput) throws IOException {
        return read(dataInput, dataInput.readByte());
    }

	public static void writeNamedTag(NBTBase baseTag, DataOutput dataOutput) throws IOException {
		dataOutput.writeByte(baseTag.getType());
		if(baseTag.getType() != 0) {
			byte[] b2 = baseTag.getKey().getBytes("UTF-8");
			dataOutput.writeShort(b2.length);
			dataOutput.write(b2);
			baseTag.writeTagContents(dataOutput);
		}
	}

	public static NBTBase createTagOfType(byte type) {
		switch(type) {
		case 0:
			return new NBTTagEnd();
		case 1:
			return new NBTTagByte();
		case 2:
			return new NBTTagShort();
		case 3:
			return new NBTTagInt();
		case 4:
			return new NBTTagLong();
		case 5:
			return new NBTTagFloat();
		case 6:
			return new NBTTagDouble();
		case 7:
			return new NBTTagByteArray();
		case 8:
			return new NBTTagString();
		case 9:
			return new NBTTagList();
		case 10:
			return new NBTTagCompound();
		default:
			return null;
		}
	}
}