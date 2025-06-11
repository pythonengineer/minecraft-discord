package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;

public final class NBTTagEnd extends NBTBase {
	final void readTagContents(DataInput var1) {
	}

	final void writeTagContents(DataOutput var1) {
	}

	public final byte getType() {
		return (byte)0;
	}

	public final String toString() {
		return "END";
	}
}
