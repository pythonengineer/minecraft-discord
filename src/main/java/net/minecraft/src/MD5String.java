package net.minecraft.src;

import java.math.BigInteger;

import net.lax1dude.eaglercraft.crypto.MD5Digest;

public class MD5String {
	private String field_27370_a;

	public MD5String(String var1) {
		this.field_27370_a = var1;
	}

	public String func_27369_a(String var1) {
		try {
			String var2 = this.field_27370_a + var1;
			MD5Digest var3 = new MD5Digest();
			var3.update(var2.getBytes(), 0, var2.length());
	        byte[] md5Bytes = new byte[16];
	        var3.doFinal(md5Bytes, 0);
			return (new BigInteger(1, md5Bytes)).toString(16);
		} catch (Exception var4) {
			throw new RuntimeException(var4);
		}
	}
}
