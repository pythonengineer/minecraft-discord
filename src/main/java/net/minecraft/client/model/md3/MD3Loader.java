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

		while(true) {
			int var4 = var5.read(var3);
			if(var4 < 0) {
				var5.close();
				var2.close();
				ByteBuffer var6 = ByteBuffer.wrap(var2.toByteArray());
				return this.loadMD3Data(var6);
			}

			var2.write(var3, 0, var4);
		}
	}

	private MD3Vertices loadMD3Data(ByteBuffer var1) throws IOException {
		var1.order(ByteOrder.LITTLE_ENDIAN);
		String var2 = loadMD3Info(var1, 4);
		if(!var2.equals("IDP3")) {
			throw new IOException("Not a valid MD3 file (bad magic number)");
		} else {
			MD3Vertices var14 = new MD3Vertices();
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
			var14.totalFrames = var3;
			var14.frameArray = new MD3FrameArray[var3];
			var14.modelMap = new HashMap();
			var14.buffersMD3 = new MD3Buffers[var5];
			var1.position(var6);

			for(var6 = 0; var6 < var3; ++var6) {
				MD3FrameArray[] var10000 = var14.frameArray;
				MD3FrameArray var12 = new MD3FrameArray();
				getMD3Vec(var1);
				getMD3Vec(var1);
				getMD3Vec(var1);
				var1.getFloat();
				loadMD3Info(var1, 16);
				var10000[var6] = var12;
			}

			MD3Data[] var15 = new MD3Data[var4];

			int var8;
			for(var8 = 0; var8 < var4; ++var8) {
				var15[var8] = new MD3Data(var3);
			}

			for(var8 = 0; var8 < var3; ++var8) {
				for(int var9 = 0; var9 < var4; ++var9) {
					MD3Data var11 = var15[var9];
					var11.name = loadMD3Info(var1, 64);
					var11.b[var8] = getMD3Vec(var1);
					var11.c[var8] = getMD3Vec(var1);
					var11.d[var8] = getMD3Vec(var1);
					var11.e[var8] = getMD3Vec(var1);
				}
			}

			for(var8 = 0; var8 < var4; ++var8) {
				var14.modelMap.put(var15[var8].name, var15[var8]);
			}

			var1.position(var7);

			for(var8 = 0; var8 < var5; ++var8) {
				var14.buffersMD3[var8] = this.getMD3Buffer(var1);
			}

			return var14;
		}
	}

	private MD3Buffers getMD3Buffer(ByteBuffer var1) throws IOException {
		int var2 = var1.position();
		String var3 = loadMD3Info(var1, 4);
		if(!var3.equals("IDP3")) {
			throw new IOException("Not a valid MD3 file (bad surface magic number)");
		} else {
			var3 = loadMD3Info(var1, 64);
			System.out.println("Name: " + var3);
			var1.getInt();
			int var20 = var1.getInt();
			int var4 = var1.getInt();
			int var5 = var1.getInt();
			int var6 = var1.getInt();
			MD3Buffers var7 = new MD3Buffers(var6, var5, var20);
			int var8 = var1.getInt() + var2;
			int var9 = var1.getInt() + var2;
			int var10 = var1.getInt() + var2;
			var2 += var1.getInt();
			var1.getInt();
			var7.verts = var5;
			var7.shaders = new MD3Shader[var4];
			System.out.println("Triangles: " + var6);
			System.out.println("OFS_SHADERS: " + var9 + " (current location: " + var1.position() + ")");
			var1.position(var9);

			for(var9 = 0; var9 < var4; ++var9) {
				MD3Shader[] var10000 = var7.shaders;
				MD3Shader var11 = new MD3Shader();
				loadMD3Info(var1, 64);
				var1.getInt();
				var10000[var9] = var11;
			}

			System.out.println("OFS_TRIANGLES: " + var8 + " (current location: " + var1.position() + ")");
			var1.position(var8);

			for(var9 = 0; var9 < var6 * 3; ++var9) {
				var7.triangles.put(var1.getInt());
			}

			System.out.println("OFS_ST: " + var10 + " (current location: " + var1.position() + ")");
			var1.position(var10);

			for(var9 = 0; var9 < var5 << 1; ++var9) {
				var7.xBuffer.put(var1.getFloat());
			}

			System.out.println("OFS_XYZ_NORMAL: " + var2 + " (current location: " + var1.position() + ")");
			var1.position(var2);

			for(var9 = 0; var9 < var5 * var20; ++var9) {
				var7.vertices.put((float)var1.getShort() / 64.0F);
				var7.vertices.put((float)var1.getShort() / 64.0F);
				var7.vertices.put((float)var1.getShort() / 64.0F);
				double var15 = (double)(var1.get() & 255) * Math.PI * 2.0D / 255.0D;
				double var17 = (double)(var1.get() & 255) * Math.PI * 2.0D / 255.0D;
				float var19 = (float)(Math.cos(var17) * Math.sin(var15));
				float var21 = (float)(Math.sin(var17) * Math.sin(var15));
				float var22 = (float)Math.cos(var15);
				var7.normals.put(var19);
				var7.normals.put(var21);
				var7.normals.put(var22);
			}

			return var7;
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
