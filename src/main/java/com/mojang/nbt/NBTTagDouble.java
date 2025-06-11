package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;

public final class NBTTagDouble extends NBTBase {
	private double a;

	final void writeTagContents(DataOutput var1) {
        try{
        var1.writeDouble(this.a);
    } catch (java.io.IOException exc) {}
	}

	final void readTagContents(DataInput var1) {
        try{
        this.a = var1.readDouble();
    } catch (java.io.IOException exc) {}
	}

	public final byte getType() {
		return (byte)6;
	}

	public final String toString() {
		return "" + this.a;
	}
}
