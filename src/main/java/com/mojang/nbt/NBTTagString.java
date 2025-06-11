package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;

public final class NBTTagString extends NBTBase {
	public String stringValue;

	public NBTTagString() {
	}

	public NBTTagString(String var1) {
		this.stringValue = var1;
	}

	final void writeTagContents(DataOutput var1) {
        try{
        byte[] var2 = this.stringValue.getBytes("UTF-8");
        var1.writeShort(var2.length);
        var1.write(var2);
    } catch (java.io.IOException exc) {}
	}

	final void readTagContents(DataInput var1) {
        try{
        short var2 = var1.readShort();
        byte[] var3 = new byte[var2];
        var1.readFully(var3);
        this.stringValue = new String(var3, "UTF-8");
    } catch (java.io.IOException exc) {}
	}

	public final byte getType() {
		return (byte)8;
	}

	public final String toString() {
		return "" + this.stringValue;
	}
}
