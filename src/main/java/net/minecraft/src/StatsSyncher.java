package net.minecraft.src;

import java.io.BufferedReader;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Map;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EagUtils;
import net.lax1dude.eaglercraft.EaglerInputStream;
import net.lax1dude.eaglercraft.EaglerOutputStream;

public class StatsSyncher {
	private volatile boolean field_27438_a = false;
	private volatile Map field_27437_b = null;
	private volatile Map field_27436_c = null;
	private StatFileWriter field_27435_d;
	private VFile2 field_27434_e;
	private VFile2 field_27433_f;
	private VFile2 field_27432_g;
	private VFile2 field_27431_h;
	private VFile2 field_27430_i;
	private VFile2 field_27429_j;
	private Session field_27428_k;
	private int field_27427_l = 0;
	private int field_27426_m = 0;

	public StatsSyncher(Session var1, StatFileWriter var2, VFile2 var3) {
		this.field_27434_e = new VFile2(var3, "stats_" + var1.username.toLowerCase() + "_unsent.dat");
		this.field_27433_f = new VFile2(var3, "stats_" + var1.username.toLowerCase() + ".dat");
		this.field_27430_i = new VFile2(var3, "stats_" + var1.username.toLowerCase() + "_unsent.old");
		this.field_27429_j = new VFile2(var3, "stats_" + var1.username.toLowerCase() + ".old");
		this.field_27432_g = new VFile2(var3, "stats_" + var1.username.toLowerCase() + "_unsent.tmp");
		this.field_27431_h = new VFile2(var3, "stats_" + var1.username.toLowerCase() + ".tmp");
        if(!var1.username.toLowerCase().equals(var1.username)) {
            this.func_28214_a(var3, "stats_" + var1.username + "_unsent.dat", this.field_27434_e);
            this.func_28214_a(var3, "stats_" + var1.username + ".dat", this.field_27433_f);
            this.func_28214_a(var3, "stats_" + var1.username + "_unsent.old", this.field_27430_i);
            this.func_28214_a(var3, "stats_" + var1.username + ".old", this.field_27429_j);
            this.func_28214_a(var3, "stats_" + var1.username + "_unsent.tmp", this.field_27432_g);
            this.func_28214_a(var3, "stats_" + var1.username + ".tmp", this.field_27431_h);
        }

		this.field_27435_d = var2;
		this.field_27428_k = var1;
		if(this.field_27434_e.exists() || EagRuntime.getStorage(this.field_27434_e.getName()) != null) {
			var2.func_27179_a(this.func_27415_a(this.field_27434_e, this.field_27432_g, this.field_27430_i));
		}

		this.func_27418_a();
	}

    private void func_28214_a(VFile2 var1, String var2, VFile2 var3) {
        VFile2 var4 = new VFile2(var1, var2);
        if(var4.exists() && !var3.exists()) {
            var4.renameTo(var3);
        }

        if(EagRuntime.getStorage(var4.getName()) != null) {
            EagRuntime.setStorage(var3.getName(), EagRuntime.getStorage(var4.getName()));
            EagRuntime.setStorage(var4.getName(), null);
        }
    }

	private Map func_27415_a(VFile2 var1, VFile2 var2, VFile2 var3) {
		return (var1.exists() || EagRuntime.getStorage(var1.getName()) != null) ? this.func_27408_a(var1) : ((var3.exists() || EagRuntime.getStorage(var3.getName()) != null) ? this.func_27408_a(var3) : ((var2.exists() || EagRuntime.getStorage(var2.getName()) != null) ? this.func_27408_a(var2) : null));
	}

	private Map func_27408_a(VFile2 var1) {
		BufferedReader var2 = null;
		InputStream fis;
		if (var1.exists()) {
		    fis = var1.getInputStream();
		} else {
		    fis = new EaglerInputStream(EagRuntime.getStorage(var1.getName()));
		}

        try {
			var2 = new BufferedReader(new InputStreamReader(fis));
			String var3 = "";
			StringBuilder var4 = new StringBuilder();

			while(true) {
				var3 = var2.readLine();
				if(var3 == null) {
					Map var5 = StatFileWriter.func_27177_a(var4.toString());
					return var5;
				}

				var4.append(var3);
			}
		} catch (Exception var15) {
			var15.printStackTrace();
		} finally {
			if(var2 != null) {
				try {
					var2.close();
				} catch (Exception var14) {
					var14.printStackTrace();
				}
			}

			try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
		}

		return null;
	}

	private void func_27410_a(Map var1, VFile2 var2, VFile2 var3, VFile2 var4) throws IOException {
		try {
            EaglerOutputStream bao = new EaglerOutputStream();
	        PrintWriter var5 = new PrintWriter(new OutputStreamWriter(bao));
			var5.print(StatFileWriter.func_27185_a(this.field_27428_k.username, "local", var1));
			var5.close();
            bao.close();
            byte[] stats = bao.toByteArray();
            if (stats != null) {
                EagRuntime.setStorage(var3.getName(), stats);
            }
        } catch (Exception var14) {
            var14.printStackTrace();
        }

		if(var4.exists()) {
			var4.delete();
		} else if(EagRuntime.getStorage(var4.getName()) != null) {
            EagRuntime.setStorage(var4.getName(), null);
		}

		if(var2.exists()) {
			var2.renameTo(var4);
		} else if(EagRuntime.getStorage(var2.getName()) != null) {
            EagRuntime.setStorage(var4.getName(), EagRuntime.getStorage(var2.getName()));
            EagRuntime.setStorage(var2.getName(), null);
        }

		var3.renameTo(var2);
        EagRuntime.setStorage(var2.getName(), EagRuntime.getStorage(var3.getName()));
        EagRuntime.setStorage(var3.getName(), null);
	}

	public void func_27418_a() {
		if(this.field_27438_a) {
			throw new IllegalStateException("Can\'t get stats from server while StatsSyncher is busy!");
		} else {
			this.field_27427_l = 100;
			this.field_27438_a = true;
			(new ThreadStatSyncherReceive(this)).start();
		}
	}

	public void func_27424_a(Map var1) {
		if(this.field_27438_a) {
			throw new IllegalStateException("Can\'t save stats while StatsSyncher is busy!");
		} else {
			this.field_27427_l = 100;
			this.field_27438_a = true;
			(new ThreadStatSyncherSend(this, var1)).start();
		}
	}

	public void syncStatsFileWithMap(Map var1) {
		int var2 = 30;

		while(this.field_27438_a) {
			--var2;
			if(var2 <= 0) {
				break;
			}

			EagUtils.sleep(100L);
		}

		this.field_27438_a = true;

		try {
			this.func_27410_a(var1, this.field_27434_e, this.field_27432_g, this.field_27430_i);
		} catch (Exception var8) {
			var8.printStackTrace();
		} finally {
			this.field_27438_a = false;
		}

	}

	public boolean func_27420_b() {
		return this.field_27427_l <= 0 && !this.field_27438_a && this.field_27436_c == null;
	}

	public void func_27425_c() {
		if(this.field_27427_l > 0) {
			--this.field_27427_l;
		}

		if(this.field_27426_m > 0) {
			--this.field_27426_m;
		}

		if(this.field_27436_c != null) {
			this.field_27435_d.func_27187_c(this.field_27436_c);
			this.field_27436_c = null;
		}

		if(this.field_27437_b != null) {
			this.field_27435_d.func_27180_b(this.field_27437_b);
			this.field_27437_b = null;
		}

	}

	static Map func_27422_a(StatsSyncher var0) {
		return var0.field_27437_b;
	}

	static VFile2 func_27423_b(StatsSyncher var0) {
		return var0.field_27433_f;
	}

	static VFile2 func_27411_c(StatsSyncher var0) {
		return var0.field_27431_h;
	}

	static VFile2 func_27413_d(StatsSyncher var0) {
		return var0.field_27429_j;
	}

	static void func_27412_a(StatsSyncher var0, Map var1, VFile2 var2, VFile2 var3, VFile2 var4) throws IOException {
		var0.func_27410_a(var1, var2, var3, var4);
	}

	static Map func_27421_a(StatsSyncher var0, Map var1) {
		return var0.field_27437_b = var1;
	}

	static Map func_27409_a(StatsSyncher var0, VFile2 var1, VFile2 var2, VFile2 var3) {
		return var0.func_27415_a(var1, var2, var3);
	}

	static boolean func_27416_a(StatsSyncher var0, boolean var1) {
		return var0.field_27438_a = var1;
	}

	static VFile2 func_27414_e(StatsSyncher var0) {
		return var0.field_27434_e;
	}

	static VFile2 func_27417_f(StatsSyncher var0) {
		return var0.field_27432_g;
	}

	static VFile2 func_27419_g(StatsSyncher var0) {
		return var0.field_27430_i;
	}
}