package net.minecraft.client.model.md3;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

import net.lax1dude.eaglercraft.EagRuntime;
import net.minecraft.game.physics.Vec3D;

public class MD3Loader {
	public final MD3Vertices loadModel(String var1) throws IOException {
		DataInputStream var5 = new DataInputStream(EagRuntime.getResourceStream("assets/" + var1));
		ByteArrayOutputStream var2 = new ByteArrayOutputStream();
		byte[] var3 = new byte[4096];
		boolean var4 = false;

		while(true) {
			int var7 = var5.read(var3);
			if(var7 < 0) {
				var5.close();
				var2.close();
				ByteBuffer var6 = ByteBuffer.wrap(var2.toByteArray());
				return this.loadMD3Data(var6);
			}

			var2.write(var3, 0, var7);
		}
	}

	private MD3Vertices loadMD3Data(ByteBuffer var1) throws IOException {
		var1.order(ByteOrder.LITTLE_ENDIAN);
		String var10000 = loadMD3Info(var1, 4);
		MD3Vertices var2 = null;
		if(!var10000.equals("IDP3")) {
			throw new IOException("Not a valid MD3 file (bad magic number)");
		} else {
			var2 = new MD3Vertices();
			var1.getInt();
			loadMD3Info(var1, 64);
			var1.getInt();
			int var3 = var1.getInt();
			System.out.println(var3 + " frames");
			int var4 = var1.getInt();
			int var5 = var1.getInt();
			var1.getInt();
			int var6 = var1.getInt();
			var1.getInt();
			int var7 = var1.getInt();
			var1.getInt();
			var2.totalFrames = var3;
			var2.frameArray = new MD3FrameArray[var3];
			var2.modelMap = new HashMap();
			var2.buffersMD3 = new MD3Buffers[var5];
			var1.position(var6);

			for(var6 = 0; var6 < var3; ++var6) {
				MD3FrameArray[] var14 = var2.frameArray;
				MD3FrameArray var11 = new MD3FrameArray();
				getMD3Vec(var1);
				getMD3Vec(var1);
				getMD3Vec(var1);
				var1.getFloat();
				loadMD3Info(var1, 16);
				var14[var6] = var11;
			}

			MD3Data[] var13 = new MD3Data[var4];

			int var8;
			for(var8 = 0; var8 < var4; ++var8) {
				var13[var8] = new MD3Data(var3);
			}

			for(var8 = 0; var8 < var3; ++var8) {
				for(int var9 = 0; var9 < var4; ++var9) {
					MD3Data var10 = var13[var9];
					var10.name = loadMD3Info(var1, 64);
					var10.b[var8] = getMD3Vec(var1);
					var10.c[var8] = getMD3Vec(var1);
					var10.d[var8] = getMD3Vec(var1);
					var10.e[var8] = getMD3Vec(var1);
				}
			}

			for(var8 = 0; var8 < var4; ++var8) {
				var2.modelMap.put(var13[var8].name, var13[var8]);
			}

			var1.position(var7);

			for(var8 = 0; var8 < var5; ++var8) {
				var2.buffersMD3[var8] = this.getMD3Buffer(var1, var7);
			}

			return var2;
		}
	}

	private MD3Buffers getMD3Buffer(ByteBuffer var1, int var2) throws IOException {
		var2 = var1.position();
		String var10000 = loadMD3Info(var1, 4);
		String var3 = null;
		if(!var10000.equals("IDP3")) {
			throw new IOException("Not a valid MD3 file (bad surface magic number)");
		} else {
			var3 = loadMD3Info(var1, 64);
			System.out.println("Name: " + var3);
			var1.getInt();
			int var20 = var1.getInt();
			int var4 = var1.getInt();
			int var5 = var1.getInt();
			int var17 = var1.getInt();
			MD3Buffers var6 = new MD3Buffers(var17, var5, var20);
			int var7 = var1.getInt() + var2;
			int var8 = var1.getInt() + var2;
			int var9 = var1.getInt() + var2;
			var2 += var1.getInt();
			var1.getInt();
			var6.verts = var5;
			var6.shaders = new MD3Shader[var4];
			System.out.println("Triangles: " + var17);
			System.out.println("OFS_SHADERS: " + var8 + " (current location: " + var1.position() + ")");
			var1.position(var8);

			for(var8 = 0; var8 < var4; ++var8) {
				MD3Shader[] var22 = var6.shaders;
				MD3Shader var10 = null;
				var10 = new MD3Shader();
				loadMD3Info(var1, 64);
				var1.getInt();
				var22[var8] = var10;
			}

			System.out.println("OFS_TRIANGLES: " + var7 + " (current location: " + var1.position() + ")");
			var1.position(var7);

			for(var8 = 0; var8 < var17 * 3; ++var8) {
				var6.triangles.put(var1.getInt());
			}

			System.out.println("OFS_ST: " + var9 + " (current location: " + var1.position() + ")");
			var1.position(var9);

			for(var8 = 0; var8 < var5 << 1; ++var8) {
				var6.xBuffer.put(var1.getFloat());
			}

			System.out.println("OFS_XYZ_NORMAL: " + var2 + " (current location: " + var1.position() + ")");
			var1.position(var2);

			for(var8 = 0; var8 < var5 * var20; ++var8) {
				var6.vertices.put((float)var1.getShort() / 64.0F);
				var6.vertices.put((float)var1.getShort() / 64.0F);
				var6.vertices.put((float)var1.getShort() / 64.0F);
				double var13 = (double)(var1.get() & 255) * Math.PI * 2.0D / 255.0D;
				double var15 = (double)(var1.get() & 255) * Math.PI * 2.0D / 255.0D;
				float var18 = (float)(Math.cos(var15) * Math.sin(var13));
				float var19 = (float)(Math.sin(var15) * Math.sin(var13));
				float var21 = (float)Math.cos(var13);
				var6.normals.put(var18);
				var6.normals.put(var19);
				var6.normals.put(var21);
			}

			return var6;
		}
	}

	private static Vec3D getMD3Vec(ByteBuffer var0) {
		float var1 = var0.getFloat();
		float var2 = var0.getFloat();
		float var3 = var0.getFloat();
		return new Vec3D(var1, var2, var3);
	}

	private static String loadMD3Info(ByteBuffer var0, int var1) {
		byte[] var3 = new byte[var1];
		var0.get(var3);

		for(int var2 = 0; var2 < var3.length; ++var2) {
			if(var3[var2] == 0) {
				return new String(var3, 0, var2);
			}
		}

		return new String(var3);
	}
}
