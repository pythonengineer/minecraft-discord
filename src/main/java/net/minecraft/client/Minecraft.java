package net.minecraft.client;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.ChunkProviderLoadOrGenerate;
import net.minecraft.src.EffectRenderer;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.EnumOptions;
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
import net.minecraft.src.GuiSleepMP;
import net.minecraft.src.GuiUnused;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.LoadingScreenRenderer;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.MouseHelper;
import net.minecraft.src.MovementInputFromOptions;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.OpenGlCapsChecker;
import net.minecraft.src.PlayerController;
import net.minecraft.src.PlayerControllerTest;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.RenderManager;
import net.minecraft.src.SaveConverterMcRegion;
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
import net.lax1dude.eaglercraft.lwjgl.LWJGLException;
import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;
import net.lax1dude.eaglercraft.lwjgl.input.Mouse;
import net.lax1dude.eaglercraft.lwjgl.opengl.Display;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.lwjgl.util.glu.GLU;
import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EagUtils;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.Touch;
import net.lax1dude.eaglercraft.crash.CrashReport;
import net.lax1dude.eaglercraft.internal.ContextLostError;
import net.lax1dude.eaglercraft.internal.EnumPlatformType;
import net.lax1dude.eaglercraft.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.lwjgl.opengl.DisplayMode;
import net.lax1dude.eaglercraft.minecraft.EnumInputEvent;
import net.lax1dude.eaglercraft.touch.TouchControls;
import net.lax1dude.eaglercraft.touch.TouchOverlayRenderer;
import net.lax1dude.eaglercraft.util.ReportedException;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

public class Minecraft implements Runnable {
	private static Minecraft field_21900_a;
	public PlayerController playerController;
	private boolean fullscreen = false;
	public int displayWidth;
	public int displayHeight;
	private OpenGlCapsChecker glCapabilities;
	private Timer timer = new Timer(20.0F);
	public World theWorld;
	public RenderGlobal renderGlobal;
	public EntityPlayerSP thePlayer;
	public EntityLiving field_22009_h;
	public EffectRenderer effectRenderer;
	public Session session = null;
	public String minecraftUri;
	public boolean hideQuitButton = true;
	public volatile boolean isWorldLoaded = false;
	public RenderEngine renderEngine;
	public FontRenderer fontRenderer;
	public GuiScreen currentScreen = null;
	public LoadingScreenRenderer loadingScreen = new LoadingScreenRenderer(this);
	public EntityRenderer entityRenderer = new EntityRenderer(this);
	private int ticksRan = 0;
	private int field_6282_S = 0;
	private int field_9236_T;
	private int field_9235_U;
	public GuiIngame ingameGUI;
	public boolean field_6307_v = false;
	public ModelBiped field_9242_w = new ModelBiped(0.0F);
	public MovingObjectPosition objectMouseOver = null;
	public GameSettings gameSettings;
	public SoundManager sndManager = new SoundManager();
	public MouseHelper mouseHelper;
	public TexturePackList texturePackList;
	private ISaveFormat field_22008_V;
	public static long[] frameTimes = new long[512];
	public static long[] tickTimes = new long[512];
	public static int numRecordedFrameTimes = 0;
	public String serverName;
	public int serverPort;
	private TextureWaterFX textureWaterFX = new TextureWaterFX();
	private TextureLavaFX textureLavaFX = new TextureLavaFX();
	public volatile boolean running = true;
	public String debug = "";
	boolean isTakingScreenshot = false;
	long prevFrameTime = -1L;
	public boolean field_6289_L = false;
	private int field_6302_aa = 0;
	public boolean isRaining = false;
	long systemTime = EagRuntime.currentTimeMillis();
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
		this.fullscreen = var6;
		this.displayWidth = var4;
		this.displayHeight = var5;
		this.fullscreen = var6;
		field_21900_a = this;
        this.gameSettings = new GameSettings(this);
        this.sndManager.loadSoundSettings(this.gameSettings);
        this.sndManager.registerSounds();
	}

	public void displayUnexpectedThrowable(UnexpectedThrowable var1) {
	}

	public void setServer(String var1, int var2) {
		this.serverName = var1;
		this.serverPort = var2;
	}

	public void startGame() throws LWJGLException {
        this.displayWidth = Display.getVisualViewportW() != 0 ? Display.getVisualViewportW() : this.displayWidth;
        this.displayHeight = Display.getVisualViewportH() != 0 ? Display.getVisualViewportH() : this.displayHeight;
		if(this.fullscreen) {
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

		Display.setTitle("Minecraft Beta 1.3_01");

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

		RenderManager.instance.itemRenderer = new ItemRenderer(this);
		this.field_22008_V = new SaveConverterMcRegion(new VFile2("saves"));
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
		this.renderEngine.registerTextureFX(this.textureLavaFX);
		this.renderEngine.registerTextureFX(this.textureWaterFX);
		this.renderEngine.registerTextureFX(new TexturePortalFX());
		this.renderEngine.registerTextureFX(new TextureCompassFX(this));
		this.renderEngine.registerTextureFX(new TextureWatchFX(this));
		this.renderEngine.registerTextureFX(new TexureWaterFlowFX());
		this.renderEngine.registerTextureFX(new TextureLavaFlowFX());
		this.renderEngine.registerTextureFX(new TextureFlamesFX(0));
		this.renderEngine.registerTextureFX(new TextureFlamesFX(1));
		this.renderGlobal = new RenderGlobal(this, this.renderEngine);
		GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
        this.mouseGrabSupported = Mouse.isMouseGrabSupported();
        this.touchOverlayRenderer = new TouchOverlayRenderer();
        this.scaledResolution = new ScaledResolution(this);
        PointerInputAbstraction.initController(this);
		this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);

		this.checkGLError("Post startup");
		this.ingameGUI = new GuiIngame(this);
		if(this.serverName != null) {
			this.displayGuiScreen(new GuiConnecting(this, this.serverName, this.serverPort));
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

	public ISaveFormat func_22004_c() {
		return this.field_22008_V;
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

	public void shutdownMinecraftApplet() {
		try {
			System.out.println("Stopping!");

			try {
				this.changeWorld1((World)null);
			} catch (Throwable var8) {
			}

			try {
				GLAllocation.deleteTexturesAndDisplayLists();
			} catch (Throwable var7) {
			}

			this.sndManager.closeMinecraft();
			Mouse.destroy();
			Keyboard.destroy();
		} finally {
			Display.destroy();
			EagRuntime.exit();
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
			long var1 = EagRuntime.currentTimeMillis();
			int var3 = 0;

			while(this.running) {
				AxisAlignedBB.clearBoundingBoxPool();
				Vec3D.initialize();
				if(Display.isCloseRequested()) {
					this.shutdown();
				}

				if(this.isWorldLoaded && this.theWorld != null) {
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
						this.changeWorld1((World)null);
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
					this.theWorld.func_6465_g();
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
					if(this.playerController != null) {
						this.playerController.setPartialTime(this.timer.renderPartialTicks);
					}

					this.entityRenderer.func_4136_b(this.timer.renderPartialTicks);
				}

                this.entityRenderer.func_905_b();
                touchOverlayRenderer.render(this.displayWidth, this.displayHeight, this.scaledResolution);
                GL11.disableBlend();

				if(!Display.isActive()) {
					if(this.fullscreen) {
						this.toggleFullscreen();
					}

					EagUtils.sleep(10L);
				}

				if(this.gameSettings.showDebugInfo) {
					this.displayDebugInfo(var20);
				} else {
					this.prevFrameTime = EagRuntime.nanoTime();
				}

				Thread.yield();
				if(Keyboard.isKeyDown(Keyboard.KEY_F7)) {
                    this.updateDisplay();
				}

				this.screenshotListener();

				this.checkGLError("Post render");
				++var3;

				for(this.isWorldLoaded = !this.isMultiplayerWorld() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame(); EagRuntime.currentTimeMillis() >= var1 + 1000L; var3 = 0) {
					this.debug = var3 + " fps, " + WorldRenderer.chunksUpdated + " chunk updates";
					WorldRenderer.chunksUpdated = 0;
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
            this.shutdownMinecraftApplet();
        }

	}

	private void screenshotListener() {
		if(Keyboard.isKeyDown(Keyboard.KEY_F2)) {
			if(!this.isTakingScreenshot) {
				this.isTakingScreenshot = true;
			}
		} else {
			this.isTakingScreenshot = false;
		}

	}

	private void displayDebugInfo(long var1) {
		long var3 = 16666666L;
		if(this.prevFrameTime == -1L) {
			this.prevFrameTime = EagRuntime.nanoTime();
		}

		long var5 = EagRuntime.nanoTime();
		tickTimes[numRecordedFrameTimes & frameTimes.length - 1] = var1;
		frameTimes[numRecordedFrameTimes++ & frameTimes.length - 1] = var5 - this.prevFrameTime;
		this.prevFrameTime = var5;
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
		var7.addVertex((double)frameTimes.length, (double)this.displayHeight, 0.0D);
		var7.addVertex((double)frameTimes.length, (double)(this.displayHeight - var8), 0.0D);
		var7.setColorOpaque_I(538968064);
		var7.addVertex(0.0D, (double)(this.displayHeight - var8 * 2), 0.0D);
		var7.addVertex(0.0D, (double)(this.displayHeight - var8), 0.0D);
		var7.addVertex((double)frameTimes.length, (double)(this.displayHeight - var8), 0.0D);
		var7.addVertex((double)frameTimes.length, (double)(this.displayHeight - var8 * 2), 0.0D);
		var7.draw();
		long var9 = 0L;

		int var11;
		for(var11 = 0; var11 < frameTimes.length; ++var11) {
			var9 += frameTimes[var11];
		}

		var11 = (int)(var9 / 200000L / (long)frameTimes.length);
		var7.startDrawing(7);
		var7.setColorOpaque_I(541065216);
		var7.addVertex(0.0D, (double)(this.displayHeight - var11), 0.0D);
		var7.addVertex(0.0D, (double)this.displayHeight, 0.0D);
		var7.addVertex((double)frameTimes.length, (double)this.displayHeight, 0.0D);
		var7.addVertex((double)frameTimes.length, (double)(this.displayHeight - var11), 0.0D);
		var7.draw();
		var7.startDrawing(1);

		for(int var12 = 0; var12 < frameTimes.length; ++var12) {
			int var13 = (var12 - numRecordedFrameTimes & frameTimes.length - 1) * 255 / frameTimes.length;
			int var14 = var13 * var13 / 255;
			var14 = var14 * var14 / 255;
			int var15 = var14 * var14 / 255;
			var15 = var15 * var15 / 255;
			if(frameTimes[var12] > var3) {
				var7.setColorOpaque_I(-16777216 + var14 * 65536);
			} else {
				var7.setColorOpaque_I(-16777216 + var14 * 256);
			}

			long var16 = frameTimes[var12] / 200000L;
			long var18 = tickTimes[var12] / 200000L;
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
        if (!this.fullscreen
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
				this.thePlayer.resetPlayerKeyState();
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
		if(!this.playerController.field_1064_b) {
			if(var1 != 0 || this.field_6282_S <= 0) {
				if(var2 && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE && var1 == 0) {
					int var3 = this.objectMouseOver.blockX;
					int var4 = this.objectMouseOver.blockY;
					int var5 = this.objectMouseOver.blockZ;
					this.playerController.sendBlockRemoving(var3, var4, var5, this.objectMouseOver.sideHit);
					this.effectRenderer.addBlockHitEffects(var3, var4, var5, this.objectMouseOver.sideHit);
				} else {
					this.playerController.func_6468_a();
				}

			}
		}
	}

	private void clickMouse(int var1) {
		if(var1 != 0 || this.field_6282_S <= 0) {
			if(var1 == 0) {
				this.thePlayer.swingItem();
			}

			boolean var2 = true;
			if(this.objectMouseOver == null) {
				if(var1 == 0 && !(this.playerController instanceof PlayerControllerTest)) {
					this.field_6282_S = 10;
				}
			} else if(this.objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY) {
				if(var1 == 0) {
					this.playerController.func_6472_b(this.thePlayer, this.objectMouseOver.entityHit);
				}

				if(var1 == 1) {
					this.playerController.func_6475_a(this.thePlayer, this.objectMouseOver.entityHit);
				}
			} else if(this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE) {
				int var3 = this.objectMouseOver.blockX;
				int var4 = this.objectMouseOver.blockY;
				int var5 = this.objectMouseOver.blockZ;
				int var6 = this.objectMouseOver.sideHit;
				Block var7 = Block.blocksList[this.theWorld.getBlockId(var3, var4, var5)];
				if(var1 == 0) {
					this.theWorld.onBlockHit(var3, var4, var5, this.objectMouseOver.sideHit);
					if(var7 != Block.bedrock || this.thePlayer.field_9371_f >= 100) {
						this.playerController.clickBlock(var3, var4, var5, this.objectMouseOver.sideHit);
					}
				} else {
					ItemStack var8 = this.thePlayer.inventory.getCurrentItem();
					int var9 = var8 != null ? var8.stackSize : 0;
					if(this.playerController.sendPlaceBlock(this.thePlayer, this.theWorld, var8, var3, var4, var5, var6)) {
						var2 = false;
						this.thePlayer.swingItem();
					}

					if(var8 == null) {
						return;
					}

					if(var8.stackSize == 0) {
						this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
					} else if(var8.stackSize != var9) {
						this.entityRenderer.itemRenderer.func_9449_b();
					}
				}
			}

			if(var2 && var1 == 1) {
				ItemStack var10 = this.thePlayer.inventory.getCurrentItem();
				if(var10 != null && this.playerController.sendUseItem(this.thePlayer, this.theWorld, var10)) {
					this.entityRenderer.itemRenderer.func_9450_c();
				}
			}

		}
	}

    public void toggleFullscreen() {
        try {
            this.fullscreen = !this.fullscreen;
            System.out.println("Toggle fullscreen!");
            if(this.fullscreen) {
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
            Display.setFullscreen(this.fullscreen);
            this.updateDisplay();
            EagUtils.sleep(1000L);
            if(this.fullscreen) {
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

			this.thePlayer.inventory.setCurrentItem(var1, this.playerController instanceof PlayerControllerTest);
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

		this.ingameGUI.updateTick();
		this.entityRenderer.getMouseOver(1.0F);
		int var3;
		if(this.thePlayer != null) {
			IChunkProvider var1 = this.theWorld.func_21118_q();
			if(var1 instanceof ChunkProviderLoadOrGenerate) {
				ChunkProviderLoadOrGenerate var2 = (ChunkProviderLoadOrGenerate)var1;
				var3 = MathHelper.floor_float((float)((int)this.thePlayer.posX)) >> 4;
				int var4 = MathHelper.floor_float((float)((int)this.thePlayer.posZ)) >> 4;
				var2.func_21110_c(var3, var4);
			}
		}

		if(!this.isWorldLoaded && this.theWorld != null) {
			this.playerController.updateController();
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
		if(!this.isWorldLoaded) {
			this.renderEngine.func_1067_a();
		}

		if(this.currentScreen == null && this.thePlayer != null) {
			if(this.thePlayer.health <= 0) {
				this.displayGuiScreen((GuiScreen)null);
			} else if(this.thePlayer.isPlayerSleeping() && this.theWorld != null && this.theWorld.multiplayerWorld) {
				this.displayGuiScreen(new GuiSleepMP());
			}
		} else if(this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !this.thePlayer.isPlayerSleeping()) {
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

                long var1 = EagRuntime.currentTimeMillis() - this.systemTime;
                if (!touch && var1 <= 200L) {
                    if (Mouse.getEventButtonState()) {
                        PointerInputAbstraction.enterMouseModeHook();
                    }

                    i1 = Mouse.getEventDWheel();
                    if(i1 != 0) {
                        this.thePlayer.inventory.changeCurrentItem(i1);
                        if(this.gameSettings.field_22275_C) {
                            if(i1 > 0) {
                                i1 = 1;
                            }

                            if(i1 < 0) {
                                i1 = -1;
                            }

                            this.gameSettings.field_22272_F += (float)i1 * 0.25F;
                        }
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
                this.thePlayer.handleKeyPress(Keyboard.getEventKey(), Keyboard.getEventKeyState());
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

                            if(Keyboard.getEventKey() == Keyboard.KEY_F1) {
                                this.gameSettings.field_22277_y = !this.gameSettings.field_22277_y;
                            }

                            if(Keyboard.getEventKey() == Keyboard.KEY_F3) {
                                this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
                            }

                            if(Keyboard.getEventKey() == Keyboard.KEY_F5) {
                                this.gameSettings.thirdPersonView = !this.gameSettings.thirdPersonView;
                            }

                            if(Keyboard.getEventKey() == Keyboard.KEY_F8) {
                                this.gameSettings.field_22274_D = !this.gameSettings.field_22274_D;
                            }

                            if(Keyboard.getEventKey() == this.gameSettings.keyBindInventory.keyCode) {
                                this.displayGuiScreen(new GuiInventory(this.thePlayer));
                            }

                            if(Keyboard.getEventKey() == this.gameSettings.keyBindDrop.keyCode) {
                                this.thePlayer.dropCurrentItem();
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
                            this.gameSettings.setOptionValue(EnumOptions.RENDER_DISTANCE, !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ? 1 : -1);
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
					this.theWorld.joinEntityInSurroundings(this.thePlayer);
				}
			}

			this.theWorld.difficultySetting = this.gameSettings.difficulty;
			if(this.theWorld.multiplayerWorld) {
				this.theWorld.difficultySetting = 3;
			}

			if(!this.isWorldLoaded) {
				this.entityRenderer.updateRenderer();
			}

			if(!this.isWorldLoaded) {
				this.renderGlobal.func_945_d();
			}

			if(!this.isWorldLoaded) {
				this.theWorld.func_633_c();
			}

			if(!this.isWorldLoaded || this.isMultiplayerWorld()) {
				this.theWorld.func_21114_a(this.gameSettings.difficulty > 0, true);
				this.theWorld.tick();
			}

			if(!this.isWorldLoaded && this.theWorld != null) {
				this.theWorld.randomDisplayUpdates(MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
			}

			if(!this.isWorldLoaded) {
				this.effectRenderer.updateEffects();
			}
		}

		this.systemTime = EagRuntime.currentTimeMillis();
	}

	private void forceReload() {
		System.out.println("FORCING RELOAD!");
		//this.sndManager = new SoundManager();
		//this.sndManager.loadSoundSettings(this.gameSettings);
	}

	public boolean isMultiplayerWorld() {
		return this.theWorld != null && this.theWorld.multiplayerWorld;
	}

	public void startWorld(String var1, String var2, long var3) {
		this.changeWorld1((World)null);
		System.gc();
		if(this.field_22008_V.func_22175_a(var1)) {
			this.func_22002_b(var1, var2);
		} else {
			ISaveHandler var5 = this.field_22008_V.func_22174_a(var1, false);
			World var6 = new World(var5, var2, var3);
			if(var6.isNewWorld) {
				this.changeWorld2(var6, "Generating level");
			} else {
				this.changeWorld2(var6, "Loading level");
			}
		}

	}

	public void usePortal() {
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
			this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
			var7 = new World(this.theWorld, new WorldProviderHell());
			this.changeWorld(var7, "Entering the Nether", this.thePlayer);
		} else {
			var1 *= var5;
			var3 *= var5;
			this.thePlayer.setLocationAndAngles(var1, this.thePlayer.posY, var3, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
			this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
			var7 = new World(this.theWorld, new WorldProvider());
			this.changeWorld(var7, "Leaving the Nether", this.thePlayer);
		}

		this.thePlayer.worldObj = this.theWorld;
		this.thePlayer.setLocationAndAngles(var1, this.thePlayer.posY, var3, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
		this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
		(new Teleporter()).func_4107_a(this.theWorld, this.thePlayer);
	}

	public void changeWorld1(World var1) {
		this.changeWorld2(var1, "");
	}

	public void changeWorld2(World var1, String var2) {
		this.changeWorld(var1, var2, (EntityPlayer)null);
	}

	public void changeWorld(World var1, String var2, EntityPlayer var3) {
		this.field_22009_h = null;
		this.loadingScreen.printText(var2);
		this.loadingScreen.displayLoadingString("");
		this.sndManager.func_331_a((String)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		if(this.theWorld != null) {
			this.theWorld.func_651_a(this.loadingScreen);
		}

		this.theWorld = var1;
		if(var1 != null) {
			this.playerController.func_717_a(var1);
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

			if(this.thePlayer == null) {
				this.thePlayer = (EntityPlayerSP)this.playerController.func_4087_b(var1);
				this.thePlayer.preparePlayerToSpawn();
				this.playerController.flipPlayer(this.thePlayer);
			}

			this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
			if(this.renderGlobal != null) {
				this.renderGlobal.func_946_a(var1);
			}

			if(this.effectRenderer != null) {
				this.effectRenderer.clearEffects(var1);
			}

			this.playerController.func_6473_b(this.thePlayer);
			if(var3 != null) {
				var1.func_6464_c();
			}

			IChunkProvider var4 = var1.func_21118_q();
			if(var4 instanceof ChunkProviderLoadOrGenerate) {
				ChunkProviderLoadOrGenerate var5 = (ChunkProviderLoadOrGenerate)var4;
				int var6 = MathHelper.floor_float((float)((int)this.thePlayer.posX)) >> 4;
				int var7 = MathHelper.floor_float((float)((int)this.thePlayer.posZ)) >> 4;
				var5.func_21110_c(var6, var7);
			}

			var1.spawnPlayerWithLoadedChunks(this.thePlayer);
			if(var1.isNewWorld) {
				var1.func_651_a(this.loadingScreen);
			}

			this.field_22009_h = this.thePlayer;
		} else {
			this.thePlayer = null;
		}

		System.gc();
		this.systemTime = 0L;
	}

	private void func_22002_b(String var1, String var2) {
		this.loadingScreen.printText("Converting World to " + this.field_22008_V.func_22178_a());
		this.loadingScreen.displayLoadingString("This may take a while :)");
		this.field_22008_V.func_22171_a(var1, this.loadingScreen);
		this.startWorld(var1, var2, 0L);
	}

	private void func_6255_d(String var1) {
		this.loadingScreen.printText(var1);
		this.loadingScreen.displayLoadingString("Building terrain");
		short var2 = 128;
		int var3 = 0;
		int var4 = var2 * 2 / 16 + 1;
		var4 *= var4;
		IChunkProvider var5 = this.theWorld.func_21118_q();
		ChunkCoordinates var6 = this.theWorld.func_22137_s();
		if(this.thePlayer != null) {
			var6.field_22395_a = (int)this.thePlayer.posX;
			var6.field_22396_c = (int)this.thePlayer.posZ;
		}

		if(var5 instanceof ChunkProviderLoadOrGenerate) {
			ChunkProviderLoadOrGenerate var7 = (ChunkProviderLoadOrGenerate)var5;
			var7.func_21110_c(var6.field_22395_a >> 4, var6.field_22396_c >> 4);
		}

		for(int var10 = -var2; var10 <= var2; var10 += 16) {
			for(int var8 = -var2; var8 <= var2; var8 += 16) {
				this.loadingScreen.setLoadingProgress(var3++ * 100 / var4);
				this.theWorld.getBlockId(var6.field_22395_a + var10, 64, var6.field_22396_c + var8);

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
		return this.renderGlobal.func_953_b();
	}

	public String func_6262_n() {
		return this.renderGlobal.func_957_c();
	}

	public String func_21002_o() {
		return this.theWorld.func_21119_g();
	}

	public String func_6245_o() {
		return "P: " + this.effectRenderer.getStatistics() + ". T: " + this.theWorld.func_687_d();
	}

	public void respawn() {
		if(!this.theWorld.worldProvider.canRespawnHere()) {
			this.usePortal();
		}

		ChunkCoordinates var1 = this.theWorld.func_22137_s();
		IChunkProvider var2 = this.theWorld.func_21118_q();
		if(var2 instanceof ChunkProviderLoadOrGenerate) {
			ChunkProviderLoadOrGenerate var3 = (ChunkProviderLoadOrGenerate)var2;
			var3.func_21110_c(var1.field_22395_a >> 4, var1.field_22396_c >> 4);
		}

		this.theWorld.setSpawnLocation();
		this.theWorld.updateEntityList();
		int var4 = 0;
		if(this.thePlayer != null) {
			var4 = this.thePlayer.entityId;
			this.theWorld.setEntityDead(this.thePlayer);
		}

		this.field_22009_h = null;
		this.thePlayer = (EntityPlayerSP)this.playerController.func_4087_b(this.theWorld);
		this.field_22009_h = this.thePlayer;
		this.thePlayer.preparePlayerToSpawn();
		this.playerController.flipPlayer(this.thePlayer);
		this.theWorld.spawnPlayerWithLoadedChunks(this.thePlayer);
		this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
		this.thePlayer.entityId = var4;
		this.thePlayer.func_6420_o();
		this.playerController.func_6473_b(this.thePlayer);
		this.func_6255_d("Respawning");
		if(this.currentScreen instanceof GuiGameOver) {
			this.displayGuiScreen((GuiScreen)null);
		}

	}

	public NetClientHandler func_20001_q() {
		return this.thePlayer instanceof EntityClientPlayerMP ? ((EntityClientPlayerMP)this.thePlayer).sendQueue : null;
	}

    public static void main(String[] args) throws LWJGLException {
        PlatformRuntime.setThreadName("Client thread");
        minecraft = new Minecraft(854, 480, false);
        minecraft.session = new Session("Player" + EagRuntime.currentTimeMillis() % 1000L, "Password");
        minecraft.run();
    }

    public static void main(String[] args, String username, String server, int port, String mpPass) throws LWJGLException {
        PlatformRuntime.setThreadName("Client thread");
        minecraft = new Minecraft(854, 480, false);
        if(username != null) {
            minecraft.session = new Session(username, mpPass);
        } else {
            minecraft.session = new Session("Player" + EagRuntime.currentTimeMillis() % 1000L, mpPass);
        }

        minecraft.setServer(server, port);
        minecraft.run();
    }

	public static boolean func_22006_t() {
		return field_21900_a == null || !field_21900_a.gameSettings.field_22277_y;
	}

	public static boolean func_22001_u() {
		return field_21900_a != null && field_21900_a.gameSettings.fancyGraphics;
	}

	public static boolean func_22005_v() {
		return field_21900_a != null && field_21900_a.gameSettings.field_22278_j;
	}

	public static boolean func_22007_w() {
		return field_21900_a != null && field_21900_a.gameSettings.showDebugInfo;
	}

	public boolean func_22003_b(String var1) {
		if(var1.startsWith("/")) {
		}

		return false;
	}
}
