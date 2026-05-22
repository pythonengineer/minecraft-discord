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
        dataOutput.writeUTF(this.stringValue);
	}

	final void readTagContents(DataInput dataInput) throws IOException {
        this.stringValue = dataInput.readUTF();
	}

	public final byte getType() {
		return (byte)8;
	}

	public final String toString() {
		return "" + this.stringValue;
	}
}