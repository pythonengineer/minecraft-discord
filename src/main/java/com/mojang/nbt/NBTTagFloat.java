package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;

public final class NBTTagFloat extends NBTBase {
	public float floatValue;

	public NBTTagFloat() {
	}

	public NBTTagFloat(float var1) {
		this.floatValue = var1;
	}

	final void writeTagContents(DataOutput var1) {
        try{
        var1.writeFloat(this.floatValue);
    } catch (java.io.IOException exc) {}
	}

	final void readTagContents(DataInput var1) {
        try{
        this.floatValue = var1.readFloat();
    } catch (java.io.IOException exc) {}
	}

	public final byte getType() {
		return (byte)5;
	}

	public final String toString() {
		return "" + this.floatValue;
	}
}
