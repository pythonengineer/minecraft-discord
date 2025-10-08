package com.mojang.nbt;

import java.util.HashMap;
import java.util.Map;

public final class NBTTagCompound extends NBTBase {
	private Map tagMap = new HashMap();

	public final String toString() {
		return "" + this.tagMap.size() + " entries";
	}
}
