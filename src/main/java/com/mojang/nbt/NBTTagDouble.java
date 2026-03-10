package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagDouble extends NBTBase {
	public double doubleValue;

	public NBTTagDouble() {
	}

	public NBTTagDouble(double doubleValue) {
		this.doubleValue = doubleValue;
	}

	final void writeTagContents(DataOutput dataOutput) throws IOException {
		dataOutput.writeDouble(this.doubleValue);
	}

	final void readTagContents(DataInput dataInput) throws IOException {
		this.doubleValue = dataInput.readDouble();
	}

	public final byte getType() {
		return (byte)6;
	}

	public final String toString() {
		return "" + this.doubleValue;
	}
}