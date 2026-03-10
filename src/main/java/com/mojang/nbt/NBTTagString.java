package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagString extends NBTBase {
	public String stringValue;

	public NBTTagString() {
	}

	public NBTTagString(String stringValue) {
		this.stringValue = stringValue;
		if(stringValue == null) {
			throw new IllegalArgumentException("Empty string not allowed");
		}
	}

	final void writeTagContents(DataOutput dataOutput) throws IOException {
		byte[] b2 = this.stringValue.getBytes("UTF-8");
		dataOutput.writeShort(b2.length);
		dataOutput.write(b2);
	}

	final void readTagContents(DataInput dataInput) throws IOException {
		byte[] b2 = new byte[dataInput.readShort()];
		dataInput.readFully(b2);
		this.stringValue = new String(b2, "UTF-8");
	}

	public final byte getType() {
		return (byte)8;
	}

	public final String toString() {
		return "" + this.stringValue;
	}
}