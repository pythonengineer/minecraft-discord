package net.minecraft.client;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.Touch;
import net.lax1dude.eaglercraft.crash.CrashReport;
import net.lax1dude.eaglercraft.internal.EnumPlatformType;
import net.lax1dude.eaglercraft.internal.ContextLostError;
import net.lax1dude.eaglercraft.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.lwjgl.LWJGLException;
import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;
import net.lax1dude.eaglercraft.lwjgl.input.Mouse;
import net.lax1dude.eaglercraft.lwjgl.opengl.Display;
import net.lax1dude.eaglercraft.lwjgl.opengl.DisplayMode;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.touch.TouchControls;
import net.lax1dude.eaglercraft.touch.TouchOverlayRenderer;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.lax1dude.eaglercraft.util.ReportedException;
import net.minecraft.client.controller.PlayerController;
import net.minecraft.client.controller.PlayerControllerCreative;
import net.minecraft.client.controller.PlayerControllerSP;
import net.minecraft.client.effect.EffectRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.container.GuiInventory;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.net.GuiConnecting;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.player.MovementInputFromOptions;
import net.minecraft.client.render.EntityRenderer;
import net.minecraft.client.render.ItemRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.client.render.texture.TextureFlamesFX;
import net.minecraft.client.render.texture.TextureLavaFX;
import net.minecraft.client.render.texture.TextureLavaFlowFX;
import net.minecraft.client.render.texture.TextureWaterFX;
import net.minecraft.client.render.texture.TextureWaterFlowFX;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public class Minecraft implements Runnable {
	public PlayerController playerController = new PlayerControllerSP(this);
	private boolean fullscreen = false;
	public int displayWidth;
	public int displayHeight;
    private OpenGlCapsChecker glCapabilities;
	private Timer timer = new Timer(20.0F);
	public World theWorld;
	public RenderGlobal renderGlobal;
    public EntityPlayerSP thePlayer;
	public EffectRenderer effectRenderer;
	public Session session = null;
	public String minecraftUri;
	public boolean appletMode = false;
	public volatile boolean isGamePaused = false;
	public RenderEngine renderEngine;
	public FontRenderer fontRenderer;
    public GuiScreen currentScreen = null;
    public LoadingScreenRenderer loadingScreen = new LoadingScreenRenderer(this);
	public EntityRenderer entityRenderer = new EntityRenderer(this);
	private int ticksRan = 0;
	private int leftClickCounter = 0;
    public String loadMapUser = null;
    public int loadMapID = 0;
	public GuiIngame ingameGUI;
    public boolean skipRenderWorld = false;
	public ModelBiped playerModelBiped = new ModelBiped(0.0F);
	public MovingObjectPosition objectMouseOver;
    public GameSettings options;
    public SoundManager sndManager;
    public MouseHelper mouseHelper;
    private static long[] frameTimes = new long[512];
    private static int numRecordedFrameTimes = 0;
    public String serverName;
    public int serverPort;
    private TextureWaterFX textureWaterFX;
    private TextureLavaFX textureLavaFX;
    public volatile boolean running;
	public String debug;
    private long prevFrameTime;
    public boolean inGameHasFocus;
    private long systemTime;
    private int mouseTicksRan;
    public boolean isRaining;
    public boolean mouseGrabSupported = false;
    public int rightClickDelayTimer = 0;
    public float displayDPI = 1.0f;
    public ScaledResolution scaledResolution;
    public TouchOverlayRenderer touchOverlayRenderer;
    public static Minecraft minecraft;

	public Minecraft(int width, int height, boolean fullscreen) {
		this.objectMouseOver = null;
        this.sndManager = new SoundManager();
        this.textureWaterFX = new TextureWaterFX();
        this.textureLavaFX = new TextureLavaFX();
		this.running = true;
		this.debug = "";
        this.prevFrameTime = -1L;
        this.inGameHasFocus = false;
        this.systemTime = EagRuntime.currentTimeMillis();
        this.mouseTicksRan = 0;
        this.isRaining = false;
		this.displayWidth = width;
		this.displayHeight = height;
		this.fullscreen = fullscreen;
        this.options = new GameSettings(this);
        this.sndManager.loadSoundSettings(this.options);
        this.sndManager.registerSounds();
	}

    public void displayUnexpectedThrowable(CompressedStreamTools compressedStreamTools) {
    }

    public void setServer(String string1, int i2) {
        this.serverName = string1;
        this.serverPort = i2;
    }

    public void startGame() {
        this.running = true;

        int i11;
        int i12;
        Minecraft minecraft1 = this;
        this.displayWidth = Display.getVisualViewportW() != 0 ? Display.getVisualViewportW() : this.displayWidth;
        this.displayHeight = Display.getVisualViewportH() != 0 ? Display.getVisualViewportH() : this.displayHeight;
        if(this.fullscreen) {
            Display.setFullscreen(true);
            this.displayWidth = Display.getDisplayMode().getWidth();
            this.displayHeight = Display.getDisplayMode().getHeight();
        } else {
            Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
        }

        this.displayDPI = Math.max(Math.min(Display.getDPI(), 2.0f), 1.0f);

        Display.setTitle("Minecraft Alpha v1.0.10");

        try {
            Display.create();
            Mouse.destroy();
        } catch (Exception lWJGLException17) {
            lWJGLException17.printStackTrace();

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException interruptedException16) {
            }

            Display.create();
        }

        RenderManager.instance.itemRenderer = new ItemRenderer(this);
        this.renderEngine = new RenderEngine(this.options);
        this.fontRenderer = new FontRenderer(this.options, "/default.png", this.renderEngine);
        Keyboard.create();
        Mouse.create();
        this.mouseHelper = new MouseHelper();
        Display.update();
        this.loadScreen();

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

        this.glCapabilities = new OpenGlCapsChecker();
        this.renderEngine.registerTextureFX(this.textureLavaFX);
        this.renderEngine.registerTextureFX(this.textureWaterFX);
        this.renderEngine.registerTextureFX(new TextureWaterFlowFX());
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

        this.ingameGUI = new GuiIngame(this);
        this.playerController.init();
        if(this.serverName != null) {
            this.displayGuiScreen(new GuiConnecting(this, this.serverName, this.serverPort));
        } else {
            this.displayGuiScreen(new GuiMainMenu());
        }

    }

    private void loadScreen() {
        ScaledResolution scaledResolution8;
        int i11 = (scaledResolution8 = new ScaledResolution(this)).getScaledWidth();
        int i12 = scaledResolution8.getScaledHeight();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, (double)i11, (double)i12, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
        GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
        GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Tessellator tessellator13 = Tessellator.instance;
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/title/mojang.png"));
        tessellator13.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
        tessellator13.setColorOpaque_I(0xFFFFFF);
        tessellator13.addVertexWithUV(0.0D, (double)this.displayHeight, 0.0D, 0.0D, 0.0D);
        tessellator13.addVertexWithUV((double)this.displayWidth, (double)this.displayHeight, 0.0D, 0.0D, 0.0D);
        tessellator13.addVertexWithUV((double)this.displayWidth, 0.0D, 0.0D, 0.0D, 0.0D);
        tessellator13.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        tessellator13.draw();
        short s5 = 256;
        short s6 = 256;
        int logoX = (i11 - s5) / 2;
        int logoY = (i12 - s6) / 2;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator13.setColorOpaque_I(0xFFFFFF);
        this.scaledTessellator(logoX, logoY, 0, 0, s5, s6);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        Display.swapBuffers();
    }

    public void scaledTessellator(int i1, int i2, int i3, int i4, int i5, int i6) {
        float f7 = 0.00390625F;
        float f8 = 0.00390625F;
        Tessellator tessellator9 = Tessellator.instance;
        tessellator9.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
        tessellator9.addVertexWithUV((double)(i1 + 0), (double)(i2 + i6), 0.0D, (double)((float)(i3 + 0) * f7), (double)((float)(i4 + i6) * f8));
        tessellator9.addVertexWithUV((double)(i1 + i5), (double)(i2 + i6), 0.0D, (double)((float)(i3 + i5) * f7), (double)((float)(i4 + i6) * f8));
        tessellator9.addVertexWithUV((double)(i1 + i5), (double)(i2 + 0), 0.0D, (double)((float)(i3 + i5) * f7), (double)((float)(i4 + 0) * f8));
        tessellator9.addVertexWithUV((double)(i1 + 0), (double)(i2 + 0), 0.0D, (double)((float)(i3 + 0) * f7), (double)((float)(i4 + 0) * f8));
        tessellator9.draw();
    }

	public void displayGuiScreen(GuiScreen screen) {
		if(!(this.currentScreen instanceof GuiErrorScreen)) {
			if(this.currentScreen != null) {
                this.currentScreen.onGuiClosed();
			}

            if(screen == null && this.theWorld == null) {
                screen = new GuiMainMenu();
            } else if(screen == null && this.thePlayer.health <= 0) {
                screen = new GuiGameOver();
            }

			this.currentScreen = (GuiScreen)screen;
			if(screen != null) {
                this.setIngameNotInFocus();
				((GuiScreen)screen).setWorldAndResolution(this,
				        this.scaledResolution.getScaledWidth(), this.scaledResolution.getScaledHeight());
                this.skipRenderWorld = false;
			} else {
				this.setIngameFocus();
			}
		}
	}

	public void shutdownMinecraftApplet() {
        try {
            System.out.println("Stopping!");
            this.changeWorld((World)null, "");

            try {
                GLAllocation.deleteTexturesAndDisplayLists();
            } catch (Exception exception6) {
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
        } catch (Exception exception20) {
            CrashReport crashreport1 = new CrashReport("Failed to start game", exception20);
            this.displayCrashReport(crashreport1);
            return;
        }

        try {
            long j23 = EagRuntime.currentTimeMillis();
            int i3 = 0;

            while(this.running) {
                AxisAlignedBB.clearBoundingBoxPool();
                Vec3D.initialize();
                if(Display.isCloseRequested()) {
                    this.running = false;
                }

                if(this.isGamePaused && this.theWorld != null) {
                    float f4 = this.timer.renderPartialTicks;
                    this.timer.updateTimer();
                    this.timer.renderPartialTicks = f4;
                } else {
                    this.timer.updateTimer();
                }

                Display.checkContextLost();

                PointerInputAbstraction.runGameLoop();
                this.options.touchscreen = PointerInputAbstraction.isTouchMode();

                for(int i26 = 0; i26 < this.timer.elapsedTicks; ++i26) {
                    ++this.ticksRan;
                    this.runTick();
                    if (i26 < this.timer.elapsedTicks - 1) {
                        PointerInputAbstraction.runGameLoop();
                    }
                }

                GL11.optimize();
                this.sndManager.setListener(this.thePlayer, this.timer.renderPartialTicks);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                if(this.theWorld != null) {
                    while(this.theWorld.updatingLighting()) {
                    }
                }

                if(!this.skipRenderWorld) {
                    this.playerController.setPartialTime(this.timer.renderPartialTicks);
                    this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);
                }

                this.entityRenderer.setupOverlayRendering();
                touchOverlayRenderer.render(this.displayWidth, this.displayHeight, this.scaledResolution);
                GL11.disableBlend();

                if(!Display.isActive()) {
                    if(this.fullscreen) {
                        this.toggleFullscreen();
                    }

                    Thread.sleep(10L);
                }

                if(Keyboard.isKeyDown(Keyboard.KEY_F6)) {
                    this.displayDebugInfo();
                } else {
                    this.prevFrameTime = EagRuntime.nanoTime();
                }

                Thread.yield();
                this.updateDisplay();

                if(this.options.limitFramerate) {
                    Thread.sleep(5L);
                }

                ++i3;

                for(this.isGamePaused = !this.isMultiplayerWorld() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame(); EagRuntime.currentTimeMillis() >= j23 + 1000L; i3 = 0) {
                    this.debug = i3 + " fps, " + WorldRenderer.chunksUpdated + " chunk updates";
                    WorldRenderer.chunksUpdated = 0;
                    j23 += 1000L;
                    i3 = 0;
                }
            }

            return;
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

    private void displayDebugInfo() {
        if(this.prevFrameTime == -1L) {
            this.prevFrameTime = EagRuntime.nanoTime();
        }

        long j1 = EagRuntime.nanoTime();
        frameTimes[numRecordedFrameTimes++ & frameTimes.length - 1] = j1 - this.prevFrameTime;
        this.prevFrameTime = j1;
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, (double)this.displayWidth, (double)this.displayHeight, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
        GL11.glLineWidth(1.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator tessellator13 = Tessellator.instance;
        Tessellator.instance.startDrawing(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        tessellator13.setColorOpaque_I(0x20200000);
        tessellator13.addVertex(0.0D, (double)(this.displayHeight - 100), 0.0D);
        tessellator13.addVertex(0.0D, (double)this.displayHeight, 0.0D);
        tessellator13.addVertex((double)frameTimes.length, (double)this.displayHeight, 0.0D);
        tessellator13.addVertex((double)frameTimes.length, (double)(this.displayHeight - 100), 0.0D);
        tessellator13.draw();
        long j4 = 0L;

        int i2;
        for(i2 = 0; i2 < frameTimes.length; ++i2) {
            j4 += frameTimes[i2];
        }

        i2 = (int)(j4 / 200000L / (long)frameTimes.length);
        tessellator13.startDrawing(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        tessellator13.setColorOpaque_I(0x20400000);
        tessellator13.addVertex(0.0D, (double)(this.displayHeight - i2), 0.0D);
        tessellator13.addVertex(0.0D, (double)this.displayHeight, 0.0D);
        tessellator13.addVertex((double)frameTimes.length, (double)this.displayHeight, 0.0D);
        tessellator13.addVertex((double)frameTimes.length, (double)(this.displayHeight - i2), 0.0D);
        tessellator13.draw();
        tessellator13.startDrawing(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        for(i2 = 0; i2 < frameTimes.length; ++i2) {
            int i3;
            int i14;
            int i5 = (i5 = (i14 = (i14 = (i3 = (i2 - numRecordedFrameTimes & frameTimes.length - 1) * 255 / frameTimes.length) * i3 / 255) * i14 / 255) * i14 / 255) * i5 / 255;
            tessellator13.setColorOpaque_I(i5 + 0xFF000000 + (i14 << 8) + (i3 << 16));
            long j11 = frameTimes[i2] / 200000L;
            tessellator13.addVertex((double)((float)i2 + 0.5F), (double)((float)((long)this.displayHeight - j11) + 0.5F), 0.0D);
            tessellator13.addVertex((double)((float)i2 + 0.5F), (double)((float)this.displayHeight + 0.5F), 0.0D);
        }

        tessellator13.draw();
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

    public void toggleFullscreen() {
        try {
            this.fullscreen = !this.fullscreen;
            System.out.println("Toggle fullscreen!");
            if(this.fullscreen) {
                Display.setDisplayMode(Display.getDesktopDisplayMode());
                this.displayWidth = Display.getDisplayMode().getWidth();
                this.displayHeight = Display.getDisplayMode().getHeight();
            } else {
                this.displayWidth = Display.getWidth();
                this.displayHeight = Display.getHeight();

                Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
            }

            this.setIngameNotInFocus();
            Display.setFullscreen(this.fullscreen);
            this.resize(this.displayWidth, this.displayHeight);
            this.updateDisplay();
            Thread.sleep(1000L);
            if(this.fullscreen) {
                this.setIngameFocus();
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

    public void shutdown() {
        this.running = false;
    }

	public void setIngameFocus() {
        boolean touch = PointerInputAbstraction.isTouchMode();
        if (touch || Display.isActive()) {
            if(!this.inGameHasFocus) {
                this.inGameHasFocus = true;
                if (!touch && mouseGrabSupported) {
                    this.mouseHelper.grabMouseCursor();
                }
                this.displayGuiScreen((GuiScreen) null);
                this.mouseTicksRan = this.ticksRan + 10000;
            }
        }
	}

    public void setIngameNotInFocus() {
        if (this.inGameHasFocus) {
            if(this.thePlayer != null) {
                this.thePlayer.movementInput.resetKeyState();
            }

            this.inGameHasFocus = false;
            if (!PointerInputAbstraction.isTouchMode() && mouseGrabSupported) {
                this.mouseHelper.ungrabMouseCursor();
            }
        }
    }

	public void displayInGameMenu() {
		if(this.currentScreen == null) {
			this.displayGuiScreen(new GuiIngameMenu());
		}
	}

    private void sendClickBlockToController(int i1, boolean z2) {
        if(!this.playerController.isInTestMode) {
            if(i1 != 0 || this.leftClickCounter <= 0) {
                if(z2 && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == 0 && i1 == 0) {
                    int i3 = this.objectMouseOver.blockX;
                    int i4 = this.objectMouseOver.blockY;
                    int i5 = this.objectMouseOver.blockZ;
                    this.playerController.sendBlockRemoving(i3, i4, i5, this.objectMouseOver.sideHit);
                    this.effectRenderer.addBlockHitEffects(i3, i4, i5, this.objectMouseOver.sideHit);
                } else {
                    this.playerController.resetBlockRemoving();
                }
            }
        }
    }

    private void clickMouse(int mouseButton) {
        if(mouseButton != 0 || this.leftClickCounter <= 0) {
            if(mouseButton == 0) {
                this.entityRenderer.itemRenderer.swing();
            }

            ItemStack itemStack2;
            int i13;
            if(this.objectMouseOver == null) {
                if(mouseButton == 0 && !(this.playerController instanceof PlayerControllerCreative)) {
                    this.leftClickCounter = 10;
                }
            } else if(this.objectMouseOver.typeOfHit == 1) {
                if(mouseButton == 0) {
                    this.thePlayer.attackEntity(this.objectMouseOver.entityHit);
                }

                if(mouseButton == 1) {
                    this.thePlayer.interactWithEntity(this.objectMouseOver.entityHit);
                }
            } else if(this.objectMouseOver.typeOfHit == 0) {
                int i11 = this.objectMouseOver.blockX;
                i13 = this.objectMouseOver.blockY;
                int i14 = this.objectMouseOver.blockZ;
                int i16 = this.objectMouseOver.sideHit;
                Block block6 = Block.blocksList[this.theWorld.getBlockId(i11, i13, i14)];
                if(mouseButton == 0) {
                    this.theWorld.extinguishFire(i11, i13, i14, this.objectMouseOver.sideHit);
                    if(block6 != Block.bedrock || this.thePlayer.unusedMiningCooldown >= 100) {
                        this.playerController.clickBlock(i11, i13, i14);
                    }
                } else {
                    ItemStack itemStack19 = this.thePlayer.inventory.getCurrentItem();
                    int i7 = this.theWorld.getBlockId(i11, i13, i14);
                    if(i7 > 0 && Block.blocksList[i7].blockActivated(this.theWorld, i11, i13, i14, this.thePlayer)) {
                        return;
                    }

                    if(itemStack19 == null) {
                        return;
                    }

                    int i9 = itemStack19.stackSize;
                    if(this.playerController.onPlayerRightClick(this.thePlayer, this.theWorld, itemStack19, i11, i13, i14, i16)) {
                        this.entityRenderer.itemRenderer.swing();
                    }

                    if(itemStack19.stackSize == 0) {
                        this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
                    } else if(itemStack19.stackSize != i9) {
                        this.entityRenderer.itemRenderer.resetEquippedProgress();
                    }
                }
            }

            if(mouseButton == 1) {
                ItemStack itemStack10 = this.thePlayer.inventory.getCurrentItem();
                if(itemStack10 != null) {
                    i13 = itemStack10.stackSize;
                    ItemStack itemStack11 = itemStack10.useItemRightClick(this.theWorld, this.thePlayer);
                    if(itemStack11 != itemStack10 || itemStack11 != null && itemStack11.stackSize != i13) {
                        this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = itemStack11;
                        this.entityRenderer.itemRenderer.resetEquippedProgress();
                        if(itemStack11.stackSize == 0) {
                            this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
                        }
                    }
                }
            }

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
            int i1 = this.theWorld.getBlockId(this.objectMouseOver.blockX, this.objectMouseOver.blockY, this.objectMouseOver.blockZ);
            if(i1 == Block.grass.blockID) {
                i1 = Block.dirt.blockID;
            }

            if(i1 == Block.stairDouble.blockID) {
                i1 = Block.stairSingle.blockID;
            }

            if(i1 == Block.bedrock.blockID) {
                i1 = Block.stone.blockID;
            }

            this.thePlayer.inventory.setCurrentItem(i1, this.playerController instanceof PlayerControllerCreative);
        }
    }

	public void runTick() {
        if (this.rightClickDelayTimer > 0) {
            --this.rightClickDelayTimer;
        }

        if(this.currentScreen == null && !PointerInputAbstraction.isTouchMode() && !Mouse.isMouseGrabbed()) {
            this.setIngameNotInFocus();
            this.displayInGameMenu();
        }

        this.ingameGUI.updateTick();
        if(!this.isGamePaused && this.theWorld != null) {
            this.playerController.onUpdate();
        }

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png")); 
		if(!this.isGamePaused) {
            this.renderEngine.updateDynamicTextures();
        }

		if(this.currentScreen == null && this.thePlayer != null && this.thePlayer.health <= 0) {
			this.displayGuiScreen((GuiScreen)null);
		}

		int i1;
		if(this.currentScreen == null || this.currentScreen.allowUserInput) {
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
                        int i2 = i1;
                        InventoryPlayer inventoryPlayer5 = this.thePlayer.inventory;
                        if(i1 > 0) {
                            i2 = 1;
                        }

                        if(i2 < 0) {
                            i2 = -1;
                        }

                        for(inventoryPlayer5.currentItem -= i2; inventoryPlayer5.currentItem < 0; inventoryPlayer5.currentItem += 9) {
                        }

                        while(inventoryPlayer5.currentItem >= 9) {
                            inventoryPlayer5.currentItem -= 9;
                        }
                    }

                    if(this.currentScreen == null) {
                        if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
                            this.clickMouse(0);
                            this.mouseTicksRan = this.ticksRan;
                        }

                        if(Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
                            this.clickMouse(1);
                            this.mouseTicksRan = this.ticksRan;
                        }

                        if(Mouse.getEventButton() == 2 && Mouse.getEventButtonState()) {
                            this.clickMiddleMouseButton();
                        }
                    } else if(this.currentScreen != null) {
                        this.currentScreen.handleMouseInput();
                    }
                }

                if(this.currentScreen == null && !(touch || Mouse.isActuallyGrabbed()) && (touch || Mouse.getEventButtonState())) {
                    this.setIngameFocus();
                }
            }

            if(this.leftClickCounter > 0) {
                --this.leftClickCounter;
            }

            processTouchMine();

            while(Keyboard.next()) {
                this.thePlayer.movementInput.checkKeyForMovementInput(Keyboard.getEventKey(), Keyboard.getEventKeyState());
                if(Keyboard.getEventKeyState()) {
                    if(Keyboard.getEventKey() == Keyboard.KEY_F11) {
                        this.toggleFullscreen();
                    } else {
                        if(this.currentScreen != null) {
                            this.currentScreen.handleKeyboardInput();
                        } else {
                            if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                                this.displayInGameMenu();
                            }

                            if(Keyboard.getEventKey() == Keyboard.KEY_F5) {
                                this.options.thirdPersonView = !this.options.thirdPersonView;
                            }

                            if(Keyboard.getEventKey() == this.options.keyBindInventory.keyCode) {
                                this.displayGuiScreen(new GuiInventory(this.thePlayer.inventory));
                            }

                            if(Keyboard.getEventKey() == this.options.keyBindDrop.keyCode) {
                                this.thePlayer.dropPlayerItemWithRandomChoice(this.thePlayer.inventory.decrStackSize(this.thePlayer.inventory.currentItem, 1), false);
                            }

                            if(this.isMultiplayerWorld() && Keyboard.getEventKey() == this.options.keyBindChat.keyCode) {
                                this.displayGuiScreen(new GuiChat());
                            }
                        }

                        for(i1 = 0; i1 < 9; ++i1) {
                            if(Keyboard.getEventKey() == i1 + Keyboard.KEY_1) {
                                this.thePlayer.inventory.currentItem = i1;
                            }
                        }

                        if(Keyboard.getEventKey() == this.options.keyBindToggleFog.keyCode) {
                            this.options.setOptionValue(4, !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ? 1 : -1);
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
                    this.mouseTicksRan = this.ticksRan;
                }
                wasMiningTouch = true;
            }
            wasMiningTouch = miningTouch;

            if (miningTouch && useTouch && this.rightClickDelayTimer == 0) {
                this.clickMouse(1);
            }

            if(this.currentScreen == null) {
                if((Mouse.isButtonDown(0) || miningTouch) && (float)(this.ticksRan - this.mouseTicksRan) >= this.timer.ticksPerSecond / 4.0F && this.inGameHasFocus) {
                    this.clickMouse(0);
                    this.mouseTicksRan = this.ticksRan;
                }

                if((Mouse.isButtonDown(1) || miningTouch) && (float)(this.ticksRan - this.mouseTicksRan) >= this.timer.ticksPerSecond / 4.0F && this.inGameHasFocus) {
                    this.clickMouse(1);
                    this.mouseTicksRan = this.ticksRan;
                }
            }

            this.sendClickBlockToController(0, this.currentScreen == null && (Mouse.isButtonDown(0) || miningTouch) && !useTouch && this.inGameHasFocus);
		}

		if(this.currentScreen != null) {
			this.mouseTicksRan = this.ticksRan + 10000;
		}

		if(this.currentScreen != null) {
            this.currentScreen.handleInput();
			if(this.currentScreen != null) {
				this.currentScreen.updateScreen();
			}
		}

        if(this.theWorld != null) {
            this.theWorld.difficultySetting = this.options.difficulty;
            if(!this.isGamePaused) {
                this.entityRenderer.updateRenderer();
            }

            if(!this.isGamePaused) {
                this.renderGlobal.updateClouds();
            }

            if(!this.isGamePaused) {
                this.theWorld.updateEntities();
            }

            if(!this.isGamePaused || this.isMultiplayerWorld()) {
                this.theWorld.tick();
            }

            if(!this.isGamePaused && this.theWorld != null) {
                this.theWorld.randomDisplayUpdates(MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
            }

            if(!this.isGamePaused) {
                this.effectRenderer.updateEffects();
            }
        }

        this.systemTime = EagRuntime.currentTimeMillis();
    }

    public boolean isMultiplayerWorld() {
        return this.theWorld != null && this.theWorld.multiplayerWorld;
    }

    public void startWorld(String worldName) {
        this.changeWorld((World)null, "");
        System.gc();
        World world3 = new World(null, worldName);
        if(world3.isNewWorld) {
            this.changeWorld(world3, "Generating level");
        } else {
            this.changeWorld(world3, "Loading level");
        }
    }

    public void changeWorld1(World world) {
        this.changeWorld(world, "");
    }

    public void changeWorld(World world, String title) {
        if(this.theWorld != null) {
            this.theWorld.saveWorldIndirectly(this.loadingScreen);
        }

		this.theWorld = world;
		if(world != null) {
            this.playerController.onWorldChange(world);
            world.fontRenderer = this.fontRenderer;
            if(!this.isMultiplayerWorld()) {
                this.thePlayer = (EntityPlayerSP)world.createDebugPlayer(EntityPlayerSP.class);
            } else if(this.thePlayer != null) {
                this.thePlayer.preparePlayerToSpawn();
                if(world != null) {
                    world.spawnEntityInWorld(this.thePlayer);
                }
            }

            if(!world.multiplayerWorld) {
                this.preloadWorld(title);
            }

            if(this.thePlayer == null) {
                this.thePlayer = (EntityPlayerSP)this.playerController.createPlayer(world);
                this.thePlayer.preparePlayerToSpawn();
                this.playerController.flipPlayer(this.thePlayer);
            }

            this.thePlayer.movementInput = new MovementInputFromOptions(this.options);
            if(this.renderGlobal != null) {
                this.renderGlobal.changeWorld(world);
            }

            if(this.effectRenderer != null) {
                this.effectRenderer.clearEffects(world);
            }

            this.playerController.onRespawn(this.thePlayer);
            world.spawnPlayerWithLoadedChunks(this.thePlayer);
            if(world.isNewWorld) {
                world.saveWorldIndirectly(this.loadingScreen);
            }
        } else {
            this.thePlayer = null;
        }

		System.gc();
        this.systemTime = 0L;
	}

    private void preloadWorld(String title) {
        this.loadingScreen.resetProgressAndMessage(title);
        this.loadingScreen.displayLoadingString("Building terrain");
        short s2 = 128;
        int i3 = 0;
        int i4 = s2 * 2 / 16 + 1;
        i4 *= i4;

        for(int i5 = -s2; i5 <= s2; i5 += 16) {
            int i6 = this.theWorld.spawnX;
            int i7 = this.theWorld.spawnZ;
            if(this.thePlayer != null) {
                i6 = (int)this.thePlayer.posX;
                i7 = (int)this.thePlayer.posZ;
            }

            for(int i8 = -s2; i8 <= s2; i8 += 16) {
                this.loadingScreen.setLoadingProgress(i3++ * 100 / i4);
                this.theWorld.getBlockId(i6 + i5, 64, i7 + i8);

                while(this.theWorld.updatingLighting()) {
                }
            }
        }

        this.loadingScreen.displayLoadingString("Simulating world for a bit");
        boolean z9 = true;
        this.theWorld.dropOldChunks();
    }

    public void installResource(String resource, String resourceDir) {
        int i3 = resource.indexOf("/");
        String string4 = resource.substring(0, i3);
        resource = resource.substring(i3 + 1);
        if(string4.equalsIgnoreCase("sound")) {
            this.sndManager.addSound(resource, resourceDir);
        } else if(string4.equalsIgnoreCase("newsound")) {
            this.sndManager.addSound(resource, resourceDir);
        } else {
            if(string4.equalsIgnoreCase("music")) {
                this.sndManager.addMusic(resource, resourceDir);
            }

        }
    }

    public OpenGlCapsChecker getOpenGlCapsChecker() {
        return this.glCapabilities;
    }

    public String debugInfoRenders() {
        return this.renderGlobal.getDebugInfoRenders();
    }

    public String getEntityDebug() {
        return this.renderGlobal.getDebugInfoEntities();
    }

    public String debugInfoEntities() {
        return "P: " + this.effectRenderer.getStatistics() + ". T: " + this.theWorld.getDebugLoadedEntities();
    }

    public void respawn() {
        this.theWorld.setSpawnLocation();
        if(this.thePlayer != null) {
            this.theWorld.setEntityDead(this.thePlayer);
        }

        this.thePlayer = (EntityPlayerSP)this.playerController.createPlayer(this.theWorld);
        this.thePlayer.preparePlayerToSpawn();
        this.playerController.flipPlayer(this.thePlayer);
        this.theWorld.spawnPlayerWithLoadedChunks(this.thePlayer);
        this.thePlayer.movementInput = new MovementInputFromOptions(this.options);
        this.playerController.onRespawn(this.thePlayer);
        this.preloadWorld("Respawning");
    }

    public static void main(String[] args) throws LWJGLException {
        PlatformRuntime.setThreadName("Client thread");
        (minecraft = new Minecraft(854, 480, false)).run();
    }

    public static void main(String[] args, String username, String server, int port, String mpPass) throws LWJGLException {
        PlatformRuntime.setThreadName("Client thread");
        minecraft = new Minecraft(854, 480, false);
        if(username != null && username != null) {
            minecraft.session = new Session(username, mpPass);
        } else {
            minecraft.session = new Session("Player" + EagRuntime.currentTimeMillis() % 1000L, mpPass);
        }

        minecraft.setServer(server, port);
        minecraft.run();
    }
}
