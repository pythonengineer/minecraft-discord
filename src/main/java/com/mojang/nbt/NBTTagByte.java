package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagByte extends NBTBase {
	public byte byteValue;

	public NBTTagByte() {
	}

	public NBTTagByte(byte byteValue) {
		this.byteValue = byteValue;
	}

	final void writeTagContents(DataOutput dataOutput) throws IOException {
		dataOutput.writeByte(this.byteValue);
	}

	final void readTagContents(DataInput dataInput) throws IOException {
		this.byteValue = dataInput.readByte();
	}

	public final byte getType() {
		return (byte)1;
	}

	public final String toString() {
		return "" + this.byteValue;
	}
}