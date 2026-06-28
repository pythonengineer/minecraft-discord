package net.minecraft.src;

import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.lwjgl.input.Mouse;

public class MouseHelper {
	public int field_1114_a;
	public int field_1113_b;

	public MouseHelper() {
	}

	public void func_774_a() {
		Mouse.setGrabbed(true);
		this.field_1114_a = 0;
		this.field_1113_b = 0;
	}

	public void func_773_b() {
		Mouse.setGrabbed(false);
	}

	public void mouseXYChange() {
		this.field_1114_a = PointerInputAbstraction.getDX();
		this.field_1113_b = PointerInputAbstraction.getDY();
	}
}
