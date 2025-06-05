package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagDouble extends NBTBase {
	private double a;

	final void writeTagContents(DataOutput var1) throws IOException {
		var1.writeDouble(this.a);
	}

	final void readTagContents(DataInput var1) throws IOException {
		this.a = var1.readDouble();
	}

	public final byte getType() {
		return (byte)6;
	}

	public final String toString() {
		return "" + this.a;
	}
}
