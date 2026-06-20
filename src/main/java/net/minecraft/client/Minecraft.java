package net.minecraft.client;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.EffectRenderer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiConflictWarning;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiGameOver;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.GuiIngameMenu;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiUnused;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.LoadingScreenRenderer;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.MouseHelper;
import net.minecraft.src.MovementInputFromOptions;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.OpenGlCapsChecker;
import net.minecraft.src.PlayerController;
import net.minecraft.src.PlayerControllerTest;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.RenderManager;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.Session;
import net.minecraft.src.SoundManager;
import net.minecraft.src.Teleporter;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TextureCompassFX;
import net.minecraft.src.TextureFlamesFX;
import net.minecraft.src.TextureLavaFX;
import net.minecraft.src.TextureLavaFlowFX;
import net.minecraft.src.TexturePackList;
import net.minecraft.src.TexturePortalFX;
import net.minecraft.src.TextureWatchFX;
import net.minecraft.src.TextureWaterFX;
import net.minecraft.src.TexureWaterFlowFX;
import net.minecraft.src.Timer;
import net.minecraft.src.UnexpectedThrowable;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
import net.minecraft.src.WorldProvider;
import net.minecraft.src.WorldProviderHell;
import net.minecraft.src.WorldRenderer;
import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EagUtils;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.Touch;
import net.lax1dude.eaglercraft.crash.CrashReport;
import net.lax1dude.eaglercraft.internal.ContextLostError;
import net.lax1dude.eaglercraft.internal.EnumPlatformType;
import net.lax1dude.eaglercraft.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.lwjgl.LWJGLException;
import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;
import net.lax1dude.eaglercraft.lwjgl.input.Mouse;
import net.lax1dude.eaglercraft.lwjgl.opengl.Display;
import net.lax1dude.eaglercraft.lwjgl.opengl.DisplayMode;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.lwjgl.util.glu.GLU;
import net.lax1dude.eaglercraft.minecraft.EnumInputEvent;
import net.lax1dude.eaglercraft.touch.TouchControls;
import net.lax1dude.eaglercraft.touch.TouchOverlayRenderer;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.lax1dude.eaglercraft.util.ReportedException;

public class Minecraft implements Runnable {
	public PlayerController field_6327_b;
	private boolean a = false;
	public int displayWidth;
	public int displayHeight;
	private OpenGlCapsChecker glCapabilities;
	private Timer timer = new Timer(20.0F);
	public World theWorld;
	public RenderGlobal field_6323_f;
	public EntityPlayerSP thePlayer;
	public EffectRenderer field_6321_h;
	public Session field_6320_i = null;
	public String field_6319_j;
	public boolean field_6317_l = true;
	public volatile boolean field_6316_m = false;
	public RenderEngine renderEngine;
	public FontRenderer fontRenderer;
	public GuiScreen currentScreen = null;
	public LoadingScreenRenderer loadingScreen = new LoadingScreenRenderer(this);
	public EntityRenderer field_9243_r = new EntityRenderer(this);
	private int ticksRan = 0;
	private int field_6282_S = 0;
	private int field_9236_T;
	private int field_9235_U;
	public String field_6310_s = null;
	public int field_6309_t = 0;
	public GuiIngame ingameGUI;
	public boolean field_6307_v = false;
	public ModelBiped field_9242_w = new ModelBiped(0.0F);
	public MovingObjectPosition objectMouseOver = null;
	public GameSettings gameSettings;
	public SoundManager sndManager = new SoundManager();
	public MouseHelper mouseHelper;
	public TexturePackList texturePackList;
	public static long[] field_9240_E = new long[512];
	public static long[] field_9239_F = new long[512];
	public static int field_9238_G = 0;
	public String field_9234_V;
	public int field_9233_W;
	private TextureWaterFX field_9232_X = new TextureWaterFX();
	private TextureLavaFX field_9231_Y = new TextureLavaFX();
	public volatile boolean running = true;
	public String field_6292_I = "";
	boolean field_6291_J = false;
	long field_6290_K = -1L;
	public boolean field_6289_L = false;
	private int field_6302_aa = 0;
	public boolean field_6288_M = false;
	long field_6287_N = EagRuntime.currentTimeMillis();
	private int field_6300_ab = 0;
    public boolean mouseGrabSupported = false;
    public int rightClickDelayTimer = 0;
    public float displayDPI = 1.0f;
    public ScaledResolution scaledResolution;
    public TouchOverlayRenderer touchOverlayRenderer;
    public static Minecraft minecraft;

	public Minecraft(int var4, int var5, boolean var6) {
		this.field_9236_T = var4;
		this.field_9235_U = var5;
		this.a = var6;
		this.displayWidth = var4;
		this.displayHeight = var5;
		this.a = var6;
        this.gameSettings = new GameSettings(this);
        this.sndManager.func_340_a(this.gameSettings);
        this.sndManager.registerSounds();
	}

	public void func_4007_a(UnexpectedThrowable var1) {
	}

	public void func_6258_a(String var1, int var2) {
		this.field_9234_V = var1;
		this.field_9233_W = var2;
	}

	public void startGame() throws LWJGLException {
        this.displayWidth = Display.getVisualViewportW() != 0 ? Display.getVisualViewportW() : this.displayWidth;
        this.displayHeight = Display.getVisualViewportH() != 0 ? Display.getVisualViewportH() : this.displayHeight;
		if(this.a) {
			Display.setFullscreen(true);
			this.displayWidth = Display.getDisplayMode().getWidth();
			this.displayHeight = Display.getDisplayMode().getHeight();
			if(this.displayWidth <= 0) {
				this.displayWidth = 1;
			}

			if(this.displayHeight <= 0) {
				this.displayHeight = 1;
			}
		} else {
			Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
		}

        this.displayDPI = Math.max(Math.min(Display.getDPI(), 2.0f), 1.0f);

		Display.setTitle("Minecraft Alpha v1.2.6");

		try {
			Display.create();
		} catch (Exception var6) {
			var6.printStackTrace();

			try {
				EagUtils.sleep(1000L);
			} catch (Exception var5) {
			}

			Display.create();
		}

		RenderManager.instance.field_4236_f = new ItemRenderer(this);
		this.texturePackList = new TexturePackList(this);
		this.renderEngine = new RenderEngine(this.texturePackList, this.gameSettings);
		this.fontRenderer = new FontRenderer(this.gameSettings, "/font/default.png", this.renderEngine);
        Display.update();
		this.loadScreen();
		Keyboard.create();
		Mouse.create();
        this.mouseHelper = new MouseHelper();

		this.checkGLError("Pre startup");
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearDepth(1.0D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		this.checkGLError("Startup");
		this.glCapabilities = new OpenGlCapsChecker();
		this.renderEngine.registerTextureFX(this.field_9231_Y);
		this.renderEngine.registerTextureFX(this.field_9232_X);
		this.renderEngine.registerTextureFX(new TexturePortalFX());
		this.renderEngine.registerTextureFX(new TextureCompassFX(this));
		this.renderEngine.registerTextureFX(new TextureWatchFX(this));
		this.renderEngine.registerTextureFX(new TexureWaterFlowFX());
		this.renderEngine.registerTextureFX(new TextureLavaFlowFX());
		this.renderEngine.registerTextureFX(new TextureFlamesFX(0));
		this.renderEngine.registerTextureFX(new TextureFlamesFX(1));
		this.field_6323_f = new RenderGlobal(this, this.renderEngine);
		GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);

        this.mouseGrabSupported = Mouse.isMouseGrabSupported();
        this.touchOverlayRenderer = new TouchOverlayRenderer();
        this.scaledResolution = new ScaledResolution(this);
        PointerInputAbstraction.initController(this);
		this.field_6321_h = new EffectRenderer(this.theWorld, this.renderEngine);

		this.checkGLError("Post startup");
		this.ingameGUI = new GuiIngame(this);
		if(this.field_9234_V != null) {
			this.displayGuiScreen(new GuiConnecting(this, this.field_9234_V, this.field_9233_W));
		} else {
			this.displayGuiScreen(new GuiMainMenu());
		}

	}

	private void loadScreen() throws LWJGLException {
		ScaledResolution var1 = new ScaledResolution(this);
		int var2 = var1.getScaledWidth();
		int var3 = var1.getScaledHeight();
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)var2, (double)var3, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
		GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
		Tessellator var4 = Tessellator.instance;
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/title/mojang.png"));
		var4.startDrawingQuads();
		var4.setColorOpaque_I(16777215);
		var4.addVertexWithUV(0.0D, (double)this.displayHeight, 0.0D, 0.0D, 0.0D);
		var4.addVertexWithUV((double)this.displayWidth, (double)this.displayHeight, 0.0D, 0.0D, 0.0D);
		var4.addVertexWithUV((double)this.displayWidth, 0.0D, 0.0D, 0.0D, 0.0D);
		var4.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		var4.draw();
		short var5 = 256;
		short var6 = 256;
        int logoX = (var2 - var5) / 2;
        int logoY = (var3 - var6) / 2;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var4.setColorOpaque_I(16777215);
		this.func_6274_a(logoX, logoY, 0, 0, var5, var6);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		Display.swapBuffers();
	}

	public void func_6274_a(int var1, int var2, int var3, int var4, int var5, int var6) {
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;
		Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads();
		var9.addVertexWithUV((double)(var1 + 0), (double)(var2 + var6), 0.0D, (double)((float)(var3 + 0) * var7), (double)((float)(var4 + var6) * var8));
		var9.addVertexWithUV((double)(var1 + var5), (double)(var2 + var6), 0.0D, (double)((float)(var3 + var5) * var7), (double)((float)(var4 + var6) * var8));
		var9.addVertexWithUV((double)(var1 + var5), (double)(var2 + 0), 0.0D, (double)((float)(var3 + var5) * var7), (double)((float)(var4 + 0) * var8));
		var9.addVertexWithUV((double)(var1 + 0), (double)(var2 + 0), 0.0D, (double)((float)(var3 + 0) * var7), (double)((float)(var4 + 0) * var8));
		var9.draw();
	}

	public void displayGuiScreen(GuiScreen var1) {
		if(!(this.currentScreen instanceof GuiUnused)) {
			if(this.currentScreen != null) {
				this.currentScreen.onGuiClosed();
			}

			if(var1 == null && this.theWorld == null) {
				var1 = new GuiMainMenu();
			} else if(var1 == null && this.thePlayer.health <= 0) {
				var1 = new GuiGameOver();
			}

			this.currentScreen = (GuiScreen)var1;
			if(var1 != null) {
				this.func_6273_f();
				ScaledResolution var2 = new ScaledResolution(this);
				int var3 = var2.getScaledWidth();
				int var4 = var2.getScaledHeight();
				((GuiScreen)var1).setWorldAndResolution(this, var3, var4);
				this.field_6307_v = false;
			} else {
				this.func_6259_e();
			}

		}
	}

	private void checkGLError(String var1) {
		int var2 = GL11.glGetError();
		if(var2 != 0) {
			String var3 = GLU.gluErrorString(var2);
			System.out.println("########## GL ERROR ##########");
			System.out.println("@ " + var1);
			System.out.println(var2 + ": " + var3);
			EagRuntime.exit();
		}

	}

	public void func_6266_c() {
		try {
			System.out.println("Stopping!");
			this.func_6261_a((World)null);

			try {
				GLAllocation.deleteTexturesAndDisplayLists();
			} catch (Exception var6) {
			}

			this.sndManager.closeMinecraft();
			Mouse.destroy();
			Keyboard.destroy();
		} finally {
			Display.destroy();
		}

		System.gc();
	}

	public void run() {
		this.running = true;

		try {
			this.startGame();
		} catch (Exception var15) {
            CrashReport crashreport1 = new CrashReport("Failed to start game", var15);
            this.displayCrashReport(crashreport1);
			return;
		}

		try {
			try {
				long var1 = EagRuntime.currentTimeMillis();
				int var3 = 0;

				while(this.running) {
					AxisAlignedBB.clearBoundingBoxPool();
					Vec3D.initialize();
					if(Display.isCloseRequested()) {
						this.shutdown();
					}

					if(this.field_6316_m && this.theWorld != null) {
						float var4 = this.timer.renderPartialTicks;
						this.timer.updateTimer();
						this.timer.renderPartialTicks = var4;
					} else {
						this.timer.updateTimer();
					}

					long var19 = EagRuntime.nanoTime();

	                Display.checkContextLost();

	                PointerInputAbstraction.runGameLoop();
	                this.gameSettings.touchscreen = PointerInputAbstraction.isTouchMode();

					for(int var6 = 0; var6 < this.timer.elapsedTicks; ++var6) {
						++this.ticksRan;

						try {
							this.runTick();
						} catch (MinecraftException var14) {
							this.theWorld = null;
							this.func_6261_a((World)null);
							this.displayGuiScreen(new GuiConflictWarning());
						}

	                    if (var6 < this.timer.elapsedTicks - 1) {
	                        PointerInputAbstraction.runGameLoop();
	                    }
					}

					long var20 = EagRuntime.nanoTime() - var19;
					this.checkGLError("Pre render");
	                GL11.optimize();
					this.sndManager.func_338_a(this.thePlayer, this.timer.renderPartialTicks);
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					if(this.theWorld != null && !this.theWorld.multiplayerWorld) {
						while(this.theWorld.func_6465_g()) {
						}
					}

					if(this.theWorld != null && this.theWorld.multiplayerWorld) {
						this.theWorld.func_6465_g();
					}

					if(this.gameSettings.limitFramerate) {
						EagUtils.sleep(5L);
					}

					if(!Keyboard.isKeyDown(Keyboard.KEY_F7)) {
                        this.updateDisplay();
					}

					if(!this.field_6307_v) {
						if(this.field_6327_b != null) {
							this.field_6327_b.func_6467_a(this.timer.renderPartialTicks);
						}

						this.field_9243_r.func_4136_b(this.timer.renderPartialTicks);
					}

	                this.field_9243_r.func_905_b();
	                touchOverlayRenderer.render(this.displayWidth, this.displayHeight, this.scaledResolution);
	                GL11.disableBlend();

					if(!Display.isActive()) {
						if(this.a) {
							this.toggleFullscreen();
						}

						EagUtils.sleep(10L);
					}

					if(Keyboard.isKeyDown(Keyboard.KEY_F3)) {
						this.func_6238_a(var20);
					} else {
						this.field_6290_K = EagRuntime.nanoTime();
					}

					Thread.yield();
					if(Keyboard.isKeyDown(Keyboard.KEY_F7)) {
		                this.updateDisplay();
					}

					this.func_6248_s();

					this.checkGLError("Post render");
					++var3;

					for(this.field_6316_m = !this.isMultiplayerWorld() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame(); EagRuntime.currentTimeMillis() >= var1 + 1000L; var3 = 0) {
						this.field_6292_I = var3 + " fps, " + WorldRenderer.field_1762_b + " chunk updates";
						WorldRenderer.field_1762_b = 0;
						var1 += 1000L;
					}
				}
	        } catch (ContextLostError err) {
	            throw err;
	        } catch (ReportedException reportedexception) {
	            this.displayCrashReport(reportedexception.getCrashReport());
	        } catch (Throwable throwable1) {
	            this.theWorld = null;
	            CrashReport crashreport1 = new CrashReport("Unexpected error", throwable1);
	            this.displayCrashReport(crashreport1);
	        } finally {
	            this.func_6266_c();
	        }

		} finally {
		}
	}

	private void func_6248_s() {
		if(Keyboard.isKeyDown(Keyboard.KEY_F2)) {
			if(!this.field_6291_J) {
				this.field_6291_J = true;
			}
		} else {
			this.field_6291_J = false;
		}

	}

	private void func_6238_a(long var1) {
		long var3 = 16666666L;
		if(this.field_6290_K == -1L) {
			this.field_6290_K = EagRuntime.nanoTime();
		}

		long var5 = EagRuntime.nanoTime();
		field_9239_F[field_9238_G & field_9240_E.length - 1] = var1;
		field_9240_E[field_9238_G++ & field_9240_E.length - 1] = var5 - this.field_6290_K;
		this.field_6290_K = var5;
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)this.displayWidth, (double)this.displayHeight, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glLineWidth(1.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Tessellator var7 = Tessellator.instance;
		var7.startDrawing(7);
		int var8 = (int)(var3 / 200000L);
		var7.setColorOpaque_I(536870912);
		var7.addVertex(0.0D, (double)(this.displayHeight - var8), 0.0D);
		var7.addVertex(0.0D, (double)this.displayHeight, 0.0D);
		var7.addVertex((double)field_9240_E.length, (double)this.displayHeight, 0.0D);
		var7.addVertex((double)field_9240_E.length, (double)(this.displayHeight - var8), 0.0D);
		var7.setColorOpaque_I(538968064);
		var7.addVertex(0.0D, (double)(this.displayHeight - var8 * 2), 0.0D);
		var7.addVertex(0.0D, (double)(this.displayHeight - var8), 0.0D);
		var7.addVertex((double)field_9240_E.length, (double)(this.displayHeight - var8), 0.0D);
		var7.addVertex((double)field_9240_E.length, (double)(this.displayHeight - var8 * 2), 0.0D);
		var7.draw();
		long var9 = 0L;

		int var11;
		for(var11 = 0; var11 < field_9240_E.length; ++var11) {
			var9 += field_9240_E[var11];
		}

		var11 = (int)(var9 / 200000L / (long)field_9240_E.length);
		var7.startDrawing(7);
		var7.setColorOpaque_I(541065216);
		var7.addVertex(0.0D, (double)(this.displayHeight - var11), 0.0D);
		var7.addVertex(0.0D, (double)this.displayHeight, 0.0D);
		var7.addVertex((double)field_9240_E.length, (double)this.displayHeight, 0.0D);
		var7.addVertex((double)field_9240_E.length, (double)(this.displayHeight - var11), 0.0D);
		var7.draw();
		var7.startDrawing(1);

		for(int var12 = 0; var12 < field_9240_E.length; ++var12) {
			int var13 = (var12 - field_9238_G & field_9240_E.length - 1) * 255 / field_9240_E.length;
			int var14 = var13 * var13 / 255;
			var14 = var14 * var14 / 255;
			int var15 = var14 * var14 / 255;
			var15 = var15 * var15 / 255;
			if(field_9240_E[var12] > var3) {
				var7.setColorOpaque_I(-16777216 + var14 * 65536);
			} else {
				var7.setColorOpaque_I(-16777216 + var14 * 256);
			}

			long var16 = field_9240_E[var12] / 200000L;
			long var18 = field_9239_F[var12] / 200000L;
			var7.addVertex((double)((float)var12 + 0.5F), (double)((float)((long)this.displayHeight - var16) + 0.5F), 0.0D);
			var7.addVertex((double)((float)var12 + 0.5F), (double)((float)this.displayHeight + 0.5F), 0.0D);
			var7.setColorOpaque_I(-16777216 + var14 * 65536 + var14 * 256 + var14 * 1);
			var7.addVertex((double)((float)var12 + 0.5F), (double)((float)((long)this.displayHeight - var16) + 0.5F), 0.0D);
			var7.addVertex((double)((float)var12 + 0.5F), (double)((float)((long)this.displayHeight - (var16 - var18)) + 0.5F), 0.0D);
		}

		var7.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

    public void displayCrashReport(CrashReport crashReportIn) {
        String report = crashReportIn.getCompleteReport();
        System.out.println(report);
        PlatformRuntime.writeCrashReport(report);
        if (PlatformRuntime.getPlatformType() == EnumPlatformType.JAVASCRIPT) {
            System.err.println(
                    "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.err.println("NATIVE BROWSER EXCEPTION:");
            if (!PlatformRuntime.printJSExceptionIfBrowser(crashReportIn.getCrashCause())) {
                System.err.println("<undefined>");
            }
            System.err.println(
                    "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        }
    }

    public int getLimitFramerate() {
        return this.theWorld == null && this.currentScreen != null ? 30 : 260;
    }

    public boolean isFramerateLimitBelowMax() {
        return (float)this.getLimitFramerate() < 260;
    }

    public void updateDisplay() {
        if (Display.isVSyncSupported() && this.isFramerateLimitBelowMax()) {
            Display.update(this.getLimitFramerate());
        } else {
            Display.update(0);
        }
        this.checkWindowResize();
    }

    protected void checkWindowResize() {
        float dpiFetch = -1.0f;
        if (!this.a
                && (Display.wasResized() || (dpiFetch = Math.max(Math.min(Display.getDPI(), 2.0f), 1.0f)) != this.displayDPI)) {
            int i = this.displayWidth;
            int j = this.displayHeight;
            float f = this.displayDPI;
            this.displayWidth = Display.getWidth();
            this.displayHeight = Display.getHeight();
            this.displayDPI = dpiFetch == -1.0f ? Math.max(Math.min(Display.getDPI(), 2.0f), 1.0f) : dpiFetch;
            if (this.displayWidth != i || this.displayHeight != j || this.displayDPI != f) {
                if (this.displayWidth <= 0) {
                    this.displayWidth = 1;
                }

                if (this.displayHeight <= 0) {
                    this.displayHeight = 1;
                }

                this.resize(this.displayWidth, this.displayHeight);
            }
        }

    }

	public void shutdown() {
		this.running = false;
	}

	public void func_6259_e() {
        boolean touch = PointerInputAbstraction.isTouchMode();
		if(touch || Display.isActive()) {
			if(!this.field_6289_L) {
				this.field_6289_L = true;
                if (!touch && mouseGrabSupported) {
                    this.mouseHelper.func_774_a();
                }
				this.displayGuiScreen((GuiScreen)null);
				this.field_6302_aa = this.ticksRan + 10000;
			}
		}
	}

	public void func_6273_f() {
		if(this.field_6289_L) {
			if(this.thePlayer != null) {
				this.thePlayer.func_458_k();
			}

			this.field_6289_L = false;
            if (!PointerInputAbstraction.isTouchMode() && mouseGrabSupported) {
			    this.mouseHelper.func_773_b();
            }
		}
	}

	public void func_6252_g() {
		if(this.currentScreen == null) {
			this.displayGuiScreen(new GuiIngameMenu());
		}
	}

	private void func_6254_a(int var1, boolean var2) {
		if(!this.field_6327_b.field_1064_b) {
			if(var1 != 0 || this.field_6282_S <= 0) {
				if(var2 && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == 0 && var1 == 0) {
					int var3 = this.objectMouseOver.blockX;
					int var4 = this.objectMouseOver.blockY;
					int var5 = this.objectMouseOver.blockZ;
					this.field_6327_b.sendBlockRemoving(var3, var4, var5, this.objectMouseOver.sideHit);
					this.field_6321_h.func_1191_a(var3, var4, var5, this.objectMouseOver.sideHit);
				} else {
					this.field_6327_b.func_6468_a();
				}

			}
		}
	}

	private void clickMouse(int var1) {
		if(var1 != 0 || this.field_6282_S <= 0) {
			if(var1 == 0) {
				this.thePlayer.func_457_w();
			}

			boolean var2 = true;
			if(this.objectMouseOver == null) {
				if(var1 == 0 && !(this.field_6327_b instanceof PlayerControllerTest)) {
					this.field_6282_S = 10;
				}
			} else if(this.objectMouseOver.typeOfHit == 1) {
				if(var1 == 0) {
					this.field_6327_b.func_6472_b(this.thePlayer, this.objectMouseOver.entityHit);
				}

				if(var1 == 1) {
					this.field_6327_b.func_6475_a(this.thePlayer, this.objectMouseOver.entityHit);
				}
			} else if(this.objectMouseOver.typeOfHit == 0) {
				int var3 = this.objectMouseOver.blockX;
				int var4 = this.objectMouseOver.blockY;
				int var5 = this.objectMouseOver.blockZ;
				int var6 = this.objectMouseOver.sideHit;
				Block var7 = Block.blocksList[this.theWorld.getBlockId(var3, var4, var5)];
				if(var1 == 0) {
					this.theWorld.onBlockHit(var3, var4, var5, this.objectMouseOver.sideHit);
					if(var7 != Block.bedrock || this.thePlayer.field_9371_f >= 100) {
						this.field_6327_b.clickBlock(var3, var4, var5, this.objectMouseOver.sideHit);
					}
				} else {
					ItemStack var8 = this.thePlayer.inventory.getCurrentItem();
					int var9 = var8 != null ? var8.stackSize : 0;
					if(this.field_6327_b.sendPlaceBlock(this.thePlayer, this.theWorld, var8, var3, var4, var5, var6)) {
						var2 = false;
						this.thePlayer.func_457_w();
					}

					if(var8 == null) {
						return;
					}

					if(var8.stackSize == 0) {
						this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
					} else if(var8.stackSize != var9) {
						this.field_9243_r.field_1395_a.func_9449_b();
					}
				}
			}

			if(var2 && var1 == 1) {
				ItemStack var10 = this.thePlayer.inventory.getCurrentItem();
				if(var10 != null && this.field_6327_b.sendUseItem(this.thePlayer, this.theWorld, var10)) {
					this.field_9243_r.field_1395_a.func_9450_c();
				}
			}

		}
	}

    public void toggleFullscreen() {
        try {
            this.a = !this.a;
            System.out.println("Toggle fullscreen!");
            if(this.a) {
                Display.setDisplayMode(Display.getDesktopDisplayMode());
                this.displayWidth = Display.getDisplayMode().getWidth();
                this.displayHeight = Display.getDisplayMode().getHeight();
                if(this.displayWidth <= 0) {
                    this.displayWidth = 1;
                }

                if(this.displayHeight <= 0) {
                    this.displayHeight = 1;
                }
            } else {
                this.displayWidth = Display.getWidth();
                this.displayHeight = Display.getHeight();

                if(this.displayWidth <= 0) {
                    this.displayWidth = 1;
                }

                if(this.displayHeight <= 0) {
                    this.displayHeight = 1;
                }

                Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
            }

            this.func_6273_f();
            Display.setFullscreen(this.a);
            this.updateDisplay();
            EagUtils.sleep(1000L);
            if(this.a) {
                this.func_6259_e();
            }

            if(this.currentScreen != null) {
                this.func_6273_f();
                this.resize(this.displayWidth, this.displayHeight);
            }

            System.out.println("Size: " + this.displayWidth + ", " + this.displayHeight);
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    private void resize(int width, int height) {
        this.displayWidth = Math.max(1, width);
        this.displayHeight = Math.max(1, height);
        this.scaledResolution = new ScaledResolution(this);
        if (this.currentScreen != null) {
            this.currentScreen.setWorldAndResolution(this, 
                    this.scaledResolution.getScaledWidth(), this.scaledResolution.getScaledHeight());
        }
    }

    private long placeTouchStartTime = -1l;
    private long mineTouchStartTime = -1l;
    private boolean wasMiningTouch = false;

    private void processTouchMine() {
        if ((currentScreen == null)
                && PointerInputAbstraction.isTouchingScreenNotButton()) {
            if (PointerInputAbstraction.isDraggingNotTouching()) {
                if (mineTouchStartTime != -1l) {
                    long l = EagRuntime.currentTimeMillis();
                    if ((placeTouchStartTime == -1l || (l - placeTouchStartTime) < 350l)
                            || (l - mineTouchStartTime) < 350l) {
                        mineTouchStartTime = -1l;
                    }
                }
            } else {
                if (mineTouchStartTime == -1l) {
                    mineTouchStartTime = EagRuntime.currentTimeMillis();
                }
            }
        } else {
            mineTouchStartTime = -1l;
        }
    }

    private boolean isMiningTouch() {
        if (mineTouchStartTime == -1l)
            return false;
        long l = EagRuntime.currentTimeMillis();
        return (placeTouchStartTime == -1l || (l - placeTouchStartTime) >= 350l) && (l - mineTouchStartTime) >= 350l;
    }

    private void handlePlaceTouchStart() {
        if (placeTouchStartTime == -1l) {
            placeTouchStartTime = EagRuntime.currentTimeMillis();
        }
    }

    private void handlePlaceTouchEnd() {
        if (placeTouchStartTime != -1l) {
            int len = (int) (EagRuntime.currentTimeMillis() - placeTouchStartTime);
            if (len < 350l && !PointerInputAbstraction.isDraggingNotTouching()) {
                if (objectMouseOver != null && objectMouseOver.entityHit != null) {
                    clickMouse(0);
                } else {
                    clickMouse(1);
                }
            }
            placeTouchStartTime = -1l;
        }
    }

	public void clickMiddleMouseButton() {
		if(this.objectMouseOver != null) {
			int var1 = this.theWorld.getBlockId(this.objectMouseOver.blockX, this.objectMouseOver.blockY, this.objectMouseOver.blockZ);
			if(var1 == Block.grass.blockID) {
				var1 = Block.dirt.blockID;
			}

			if(var1 == Block.stairDouble.blockID) {
				var1 = Block.stairSingle.blockID;
			}

			if(var1 == Block.bedrock.blockID) {
				var1 = Block.stone.blockID;
			}

			this.thePlayer.inventory.setCurrentItem(var1, this.field_6327_b instanceof PlayerControllerTest);
		}

	}

	public void runTick() {
        if (this.rightClickDelayTimer > 0) {
            --this.rightClickDelayTimer;
        }

        if(this.currentScreen == null && !PointerInputAbstraction.isTouchMode() && !Mouse.isMouseGrabbed()) {
            this.func_6273_f();
            this.func_6252_g();
        }

		this.ingameGUI.func_555_a();
		this.field_9243_r.func_910_a(1.0F);
		if(this.thePlayer != null) {
			this.thePlayer.func_6420_o();
		}

		if(!this.field_6316_m && this.theWorld != null) {
			this.field_6327_b.func_6474_c();
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
		if(!this.field_6316_m) {
			this.renderEngine.func_1067_a();
		}

		if(this.currentScreen == null && this.thePlayer != null && this.thePlayer.health <= 0) {
			this.displayGuiScreen((GuiScreen)null);
		}

		if(this.currentScreen != null) {
			this.field_6302_aa = this.ticksRan + 10000;
		}

		if(this.currentScreen != null) {
			this.currentScreen.handleInput();
			if(this.currentScreen != null) {
				this.currentScreen.updateScreen();
			}
		}

        String pastedStr;
        while ((pastedStr = Touch.getPastedString()) != null) {
            if (this.currentScreen != null) {
                this.currentScreen.fireInputEvent(EnumInputEvent.CLIPBOARD_PASTE, pastedStr);
            }
        }

        int i1;
		if(this.currentScreen == null || this.currentScreen.field_948_f) {
            boolean touched;
            boolean moused = false;
            while ((touched = Touch.next()) || (moused = Mouse.next())) {
                boolean touch = false;
                if (touched) {
                    PointerInputAbstraction.enterTouchModeHook();
                    boolean mouse = moused;
                    moused = false;
                    int tc = Touch.getEventTouchPointCount();
                    if (tc > 0) {
                        for (int i = 0; i < tc; ++i) {
                            final int uid = Touch.getEventTouchPointUID(i);
                            int x = Touch.getEventTouchX(i);
                            int y = Touch.getEventTouchY(i);
                            switch (Touch.getEventType()) {
                                case TOUCHSTART:
                                    if (TouchControls.handleTouchBegin(uid, x, y)) {
                                        break;
                                    }
                                    touch = true;
                                    handlePlaceTouchStart();
                                    break;
                                case TOUCHEND:
                                    if (TouchControls.handleTouchEnd(uid, x, y)) {
                                        touch = true;
                                        break;
                                    }
                                    handlePlaceTouchEnd();
                                    break;
                                default:
                                    break;
                            }
                        }
                        if(this.currentScreen != null) {
                            this.currentScreen.touchEvent();
                        }
                        TouchControls.handleInput();
                        if (!touch) {
                            continue;
                        }
                    } else {
                        if (!mouse) {
                            continue;
                        }
                    }
                }

                long var1 = EagRuntime.currentTimeMillis() - this.field_6287_N;
                if (!touch && var1 <= 200L) {
                    if (Mouse.getEventButtonState()) {
                        PointerInputAbstraction.enterMouseModeHook();
                    }

                    i1 = Mouse.getEventDWheel();
                    if(i1 != 0) {
                        this.thePlayer.inventory.changeCurrentItem(i1);
                    }

                    if(this.currentScreen == null) {
                        if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
                            this.clickMouse(0);
                            this.field_6302_aa = this.ticksRan;
                        }

                        if(Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
                            this.clickMouse(1);
                            this.field_6302_aa = this.ticksRan;
                        }

                        if(Mouse.getEventButton() == 2 && Mouse.getEventButtonState()) {
                            this.clickMiddleMouseButton();
                        }
                    } else if(this.currentScreen != null) {
                        this.currentScreen.handleMouseInput();
                    }
                }

                if(this.currentScreen == null && !(touch || Mouse.isActuallyGrabbed()) && (touch || Mouse.getEventButtonState())) {
                    this.func_6259_e();
                }
            }

            if(this.field_6282_S > 0) {
                --this.field_6282_S;
            }

            processTouchMine();

            while(Keyboard.next()) {
                this.thePlayer.func_460_a(Keyboard.getEventKey(), Keyboard.getEventKeyState());
                if(Keyboard.getEventKeyState()) {
                    if(Keyboard.getEventKey() == Keyboard.KEY_F11) {
                        this.toggleFullscreen();
                    } else {
                        if(this.currentScreen != null) {
                            this.currentScreen.handleKeyboardInput();
                        } else {
                            if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                                this.func_6252_g();
                            }

                            if(Keyboard.getEventKey() == Keyboard.KEY_S && Keyboard.isKeyDown(Keyboard.KEY_F3)) {
                                this.forceReload();
                            }

                            if(Keyboard.getEventKey() == Keyboard.KEY_F5) {
                                this.gameSettings.thirdPersonView = !this.gameSettings.thirdPersonView;
                            }

                            if(Keyboard.getEventKey() == this.gameSettings.keyBindInventory.keyCode) {
                                this.displayGuiScreen(new GuiInventory(this.thePlayer.inventory, this.thePlayer.inventory.craftingInventory));
                            }

                            if(Keyboard.getEventKey() == this.gameSettings.keyBindDrop.keyCode) {
                                this.thePlayer.dropPlayerItemWithRandomChoice(this.thePlayer.inventory.decrStackSize(this.thePlayer.inventory.currentItem, 1), false);
                            }

                            if(this.isMultiplayerWorld() && Keyboard.getEventKey() == this.gameSettings.keyBindChat.keyCode) {
                                this.displayGuiScreen(new GuiChat());
                            }
                        }

                        for(i1 = 0; i1 < 9; ++i1) {
                            if(Keyboard.getEventKey() == i1 + Keyboard.KEY_1) {
                                this.thePlayer.inventory.currentItem = i1;
                            }
                        }

                        if(Keyboard.getEventKey() == this.gameSettings.keyBindToggleFog.keyCode) {
                            this.gameSettings.setOptionValue(4, !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ? 1 : -1);
                        }
                    }
                }
            }

            boolean touchMode = PointerInputAbstraction.isTouchMode();
            boolean miningTouch = touchMode && isMiningTouch();
            boolean useTouch = touchMode && this.thePlayer.getItemShouldUseOnTouchEagler();
            if (miningTouch && !wasMiningTouch) {
                if ((objectMouseOver != null && objectMouseOver.entityHit != null) || useTouch) {
                    this.clickMouse(1);
                } else {
                    this.clickMouse(0);
                    this.field_6302_aa = this.ticksRan;
                }
                wasMiningTouch = true;
            }
            wasMiningTouch = miningTouch;

            if (miningTouch && useTouch && this.rightClickDelayTimer == 0) {
                this.clickMouse(1);
            }

            if(this.currentScreen == null) {
                if((Mouse.isButtonDown(0) || miningTouch) && (float)(this.ticksRan - this.field_6302_aa) >= this.timer.ticksPerSecond / 4.0F && this.field_6289_L) {
                    this.clickMouse(0);
                    this.field_6302_aa = this.ticksRan;
                }

                if((Mouse.isButtonDown(1) || miningTouch) && (float)(this.ticksRan - this.field_6302_aa) >= this.timer.ticksPerSecond / 4.0F && this.field_6289_L) {
                    this.clickMouse(1);
                    this.field_6302_aa = this.ticksRan;
                }
            }

            this.func_6254_a(0, this.currentScreen == null && (Mouse.isButtonDown(0) || miningTouch) && !useTouch && this.field_6289_L);
		}

		if(this.theWorld != null) {
			if(this.thePlayer != null) {
				++this.field_6300_ab;
				if(this.field_6300_ab == 30) {
					this.field_6300_ab = 0;
					this.theWorld.func_705_f(this.thePlayer);
				}
			}

			this.theWorld.difficultySetting = this.gameSettings.difficulty;
			if(this.theWorld.multiplayerWorld) {
				this.theWorld.difficultySetting = 3;
			}

			if(!this.field_6316_m) {
				this.field_9243_r.func_911_a();
			}

			if(!this.field_6316_m) {
				this.field_6323_f.func_945_d();
			}

			if(!this.field_6316_m) {
				this.theWorld.func_633_c();
			}

			if(!this.field_6316_m || this.isMultiplayerWorld()) {
				this.theWorld.tick();
			}

			if(!this.field_6316_m && this.theWorld != null) {
				this.theWorld.randomDisplayUpdates(MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
			}

			if(!this.field_6316_m) {
				this.field_6321_h.func_1193_a();
			}
		}

		this.field_6287_N = EagRuntime.currentTimeMillis();
	}

	private void forceReload() {
		System.out.println("FORCING RELOAD!");
		this.sndManager = new SoundManager();
		this.sndManager.func_340_a(this.gameSettings);
	}

	public boolean isMultiplayerWorld() {
		return this.theWorld != null && this.theWorld.multiplayerWorld;
	}

	public void func_6247_b(String var1) {
		this.func_6261_a((World)null);
		System.gc();
		World var2 = new World(var1);
		if(var2.field_1033_r) {
			this.func_6263_a(var2, "Generating level");
		} else {
			this.func_6263_a(var2, "Loading level");
		}

	}

	public void func_6237_k() {
		if(this.thePlayer.dimension == -1) {
			this.thePlayer.dimension = 0;
		} else {
			this.thePlayer.dimension = -1;
		}

		this.theWorld.setEntityDead(this.thePlayer);
		this.thePlayer.isDead = false;
		double var1 = this.thePlayer.posX;
		double var3 = this.thePlayer.posZ;
		double var5 = 8.0D;
		World var7;
		if(this.thePlayer.dimension == -1) {
			var1 /= var5;
			var3 /= var5;
			this.thePlayer.setLocationAndAngles(var1, this.thePlayer.posY, var3, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
			this.theWorld.func_4084_a(this.thePlayer, false);
			var7 = new World(this.theWorld, new WorldProviderHell());
			this.func_6256_a(var7, "Entering the Nether", this.thePlayer);
		} else {
			var1 *= var5;
			var3 *= var5;
			this.thePlayer.setLocationAndAngles(var1, this.thePlayer.posY, var3, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
			this.theWorld.func_4084_a(this.thePlayer, false);
			var7 = new World(this.theWorld, new WorldProvider());
			this.func_6256_a(var7, "Leaving the Nether", this.thePlayer);
		}

		this.thePlayer.worldObj = this.theWorld;
		this.thePlayer.setLocationAndAngles(var1, this.thePlayer.posY, var3, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
		this.theWorld.func_4084_a(this.thePlayer, false);
		(new Teleporter()).func_4107_a(this.theWorld, this.thePlayer);
	}

	public void func_6261_a(World var1) {
		this.func_6263_a(var1, "");
	}

	public void func_6263_a(World var1, String var2) {
		this.func_6256_a(var1, var2, (EntityPlayer)null);
	}

	public void func_6256_a(World var1, String var2, EntityPlayer var3) {
		this.loadingScreen.func_596_a(var2);
		this.loadingScreen.displayLoadingString("");
		this.sndManager.func_331_a((String)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		if(this.theWorld != null) {
			this.theWorld.func_651_a(this.loadingScreen);
		}

		this.theWorld = var1;
		System.out.println("Player is " + this.thePlayer);
		if(var1 != null) {
			this.field_6327_b.func_717_a(var1);
			if(!this.isMultiplayerWorld()) {
				if(var3 == null) {
					this.thePlayer = (EntityPlayerSP)var1.func_4085_a(EntityPlayerSP.class);
				}
			} else if(this.thePlayer != null) {
				this.thePlayer.preparePlayerToSpawn();
				if(var1 != null) {
					var1.entityJoinedWorld(this.thePlayer);
				}
			}

			if(!var1.multiplayerWorld) {
				this.func_6255_d(var2);
			}

			System.out.println("Player is now " + this.thePlayer);
			if(this.thePlayer == null) {
				this.thePlayer = (EntityPlayerSP)this.field_6327_b.func_4087_b(var1);
				this.thePlayer.preparePlayerToSpawn();
				this.field_6327_b.flipPlayer(this.thePlayer);
			}

			this.thePlayer.field_787_a = new MovementInputFromOptions(this.gameSettings);
			if(this.field_6323_f != null) {
				this.field_6323_f.func_946_a(var1);
			}

			if(this.field_6321_h != null) {
				this.field_6321_h.func_1188_a(var1);
			}

			this.field_6327_b.func_6473_b(this.thePlayer);
			if(var3 != null) {
				var1.func_6464_c();
			}

			var1.func_608_a(this.thePlayer);
			if(var1.field_1033_r) {
				var1.func_651_a(this.loadingScreen);
			}
		} else {
			this.thePlayer = null;
		}

		System.gc();
		this.field_6287_N = 0L;
	}

	private void func_6255_d(String var1) {
		this.loadingScreen.func_596_a(var1);
		this.loadingScreen.displayLoadingString("Building terrain");
		short var2 = 128;
		int var3 = 0;
		int var4 = var2 * 2 / 16 + 1;
		var4 *= var4;

		for(int var5 = -var2; var5 <= var2; var5 += 16) {
			int var6 = this.theWorld.spawnX;
			int var7 = this.theWorld.spawnZ;
			if(this.thePlayer != null) {
				var6 = (int)this.thePlayer.posX;
				var7 = (int)this.thePlayer.posZ;
			}

			for(int var8 = -var2; var8 <= var2; var8 += 16) {
				this.loadingScreen.setLoadingProgress(var3++ * 100 / var4);
				this.theWorld.getBlockId(var6 + var5, 64, var7 + var8);

				while(this.theWorld.func_6465_g()) {
				}
			}
		}

		this.loadingScreen.displayLoadingString("Simulating world for a bit");
		boolean var9 = true;
		this.theWorld.func_656_j();
	}

	public OpenGlCapsChecker func_6251_l() {
		return this.glCapabilities;
	}

	public String func_6241_m() {
		return this.field_6323_f.func_953_b();
	}

	public String func_6262_n() {
		return this.field_6323_f.func_957_c();
	}

	public String func_6245_o() {
		return "P: " + this.field_6321_h.func_1190_b() + ". T: " + this.theWorld.func_687_d();
	}

	public void respawn() {
		if(!this.theWorld.worldProvider.func_6477_d()) {
			this.func_6237_k();
		}

		this.theWorld.func_4076_b();
		this.theWorld.func_9424_o();
		int var1 = 0;
		if(this.thePlayer != null) {
			var1 = this.thePlayer.field_620_ab;
			this.theWorld.setEntityDead(this.thePlayer);
		}

		this.thePlayer = (EntityPlayerSP)this.field_6327_b.func_4087_b(this.theWorld);
		this.thePlayer.preparePlayerToSpawn();
		this.field_6327_b.flipPlayer(this.thePlayer);
		this.theWorld.func_608_a(this.thePlayer);
		this.thePlayer.field_787_a = new MovementInputFromOptions(this.gameSettings);
		this.thePlayer.field_620_ab = var1;
		this.field_6327_b.func_6473_b(this.thePlayer);
		this.func_6255_d("Respawning");
		if(this.currentScreen instanceof GuiGameOver) {
			this.displayGuiScreen((GuiScreen)null);
		}

	}

    public static void main(String[] args) throws LWJGLException {
        PlatformRuntime.setThreadName("Client thread");
        minecraft = new Minecraft(854, 480, false);
        minecraft.field_6320_i = new Session("Player" + EagRuntime.currentTimeMillis() % 1000L, "Password");
        minecraft.run();
    }

    public static void main(String[] args, String username, String server, int port, String mpPass) throws LWJGLException {
        PlatformRuntime.setThreadName("Client thread");
        minecraft = new Minecraft(854, 480, false);
        if(username != null) {
            minecraft.field_6320_i = new Session(username, mpPass);
        } else {
            minecraft.field_6320_i = new Session("Player" + EagRuntime.currentTimeMillis() % 1000L, mpPass);
        }

        minecraft.func_6258_a(server, port);
        minecraft.run();
    }
}
