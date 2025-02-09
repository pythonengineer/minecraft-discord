package com.mojang.minecraft;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.character.Vec3;
import com.mojang.minecraft.character.Zombie;
import com.mojang.minecraft.character.ZombieModel;
import com.mojang.minecraft.gui.ChatScreen;
import com.mojang.minecraft.gui.ErrorScreen;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.gui.InGameHud;
import com.mojang.minecraft.gui.InventoryScreen;
import com.mojang.minecraft.gui.PauseScreen;
import com.mojang.minecraft.gui.Screen;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelIO;
import com.mojang.minecraft.level.levelgen.LevelGen;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.net.ConnectionManager;
import com.mojang.minecraft.net.Packet;
import com.mojang.minecraft.particle.Particle;
import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.player.Inventory;
import com.mojang.minecraft.player.MovementInputFromOptions;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.renderer.Chunk;
import com.mojang.minecraft.renderer.DirtyChunkSorter;
import com.mojang.minecraft.renderer.Frustum;
import com.mojang.minecraft.renderer.LevelRenderer;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;
import com.mojang.minecraft.renderer.texture.TextureFX;
import com.mojang.minecraft.renderer.texture.TextureLavaFX;
import com.mojang.minecraft.renderer.texture.TextureWaterFX;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Collections;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.ScaledResolution;
import net.lax1dude.eaglercraft.Touch;
import net.lax1dude.eaglercraft.crash.CrashReport;
import net.lax1dude.eaglercraft.internal.EnumPlatformType;
import net.lax1dude.eaglercraft.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.LWJGLException;
import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;
import net.lax1dude.eaglercraft.lwjgl.input.Mouse;
import net.lax1dude.eaglercraft.lwjgl.opengl.Display;
import net.lax1dude.eaglercraft.lwjgl.opengl.DisplayMode;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.lwjgl.util.glu.GLU;
import net.lax1dude.eaglercraft.opengl.DefaultVertexFormats;
import net.lax1dude.eaglercraft.touch.TouchControls;
import net.lax1dude.eaglercraft.touch.TouchOverlayRenderer;
import net.lax1dude.eaglercraft.util.ReportedException;

public final class Minecraft implements Runnable {
    private boolean fullscreen = false;
	public int width;
	public int height;
    public float displayDPI = 1.0f;
	private FloatBuffer fogColor0 = BufferUtils.createFloatBuffer(4);
	private FloatBuffer fogColor1 = BufferUtils.createFloatBuffer(4);
	private Timer timer = new Timer(20.0F);
    public Level level;
	private LevelRenderer levelRenderer;
    public Player player;
	private ParticleEngine particleEngine;
    public User user = null;
    public String minecraftUri;
    public boolean appletMode = false;
    public volatile boolean pause = false;
    private int yMouseAxis = 1;
    public Textures textures;
    public Font font;
    public int editMode = 0;
    public Screen screen = null;
    public LevelIO levelIo = new LevelIO(this);
    private LevelGen levelGen = new LevelGen(this);
    private int ticksRan = 0;
    public String loadMapUser = null;
    public int loadMapId = 0;
    public InGameHud hud;
    public ConnectionManager connectionManager;
    String server = null;
    int port = 0;

    private float fogColorRed = 0.5F;
    private float fogColorGreen = 0.8F;
    private float fogColorBlue = 1.0F;
    volatile boolean running = false;
    public String fpsString = "";
    private int prevFrameTime = 0;
    private float renderDistance = 0.0F;
	private HitResult hitResult = null;
    private float fogColorMultiplier = 1.0F;
    private volatile int unusedInt1 = 0;
    private volatile int unusedInt2 = 0;
    public boolean mouseGrabSupported = false;
    public static ScaledResolution scaledResolution;
    public static TouchOverlayRenderer touchOverlayRenderer;
    public static Minecraft minecraft;
    FloatBuffer lb = BufferUtils.createFloatBuffer(16);
    private String title = "";
    private String text = "";
    public boolean hideGui = false;
    public ZombieModel playerModel = new ZombieModel();

    public Minecraft(int width, int height, boolean fullscreen) {
        this.width = width;
        this.height = height;
        this.fullscreen = fullscreen;
        this.textures = new Textures();
        this.textures.registerTextureFX(new TextureLavaFX());
        this.textures.registerTextureFX(new TextureWaterFX());
    }

    public final void setServer(String string1, int i2) {
        this.server = string1;
        this.port = i2;
    }

    public void init() throws LWJGLException {
        this.fogColor0.put(new float[]{this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F});
        this.fogColor0.flip();
        this.fogColor1.put(new float[]{(float)14 / 255.0F, (float)11 / 255.0F, (float)10 / 255.0F, 1.0F});
        this.fogColor1.flip();
        this.width = Display.getVisualViewportW() != 0 ? Display.getVisualViewportW() : this.width;
        this.height = Display.getVisualViewportH() != 0 ? Display.getVisualViewportH() : this.height;
        if(this.fullscreen) {
            Display.setFullscreen(true);
            this.width = Display.getDisplayMode().getWidth();
            this.height = Display.getDisplayMode().getHeight();
        } else {
            Display.setDisplayMode(new DisplayMode(this.width, this.height));
        }
        this.displayDPI = Math.min(Display.getDPI(), 2.0f);

        Display.setTitle("Minecraft 0.0.20a_02");

        try {
            Display.create();
        } catch (Exception var19) {
            var19.printStackTrace();

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException var18) {
            }

            Display.create();
        }

        Keyboard.create();
        Mouse.create();
        Display.update();
        checkGlError("Pre startup");
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glClearDepth(1.0D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        checkGlError("Startup");
        this.font = new Font("/default.png", this.textures);
        IntBuffer imgData = BufferUtils.createIntBuffer(256);
        imgData.clear().limit(256);
        GL11.glViewport(0, 0, this.width, this.height);
        this.mouseGrabSupported = Mouse.isMouseGrabSupported();
        touchOverlayRenderer = new TouchOverlayRenderer();
        scaledResolution = new ScaledResolution(this);
        PointerInputAbstraction.init(this);
        this.level = new Level();
        if(this.server != null && this.user != null) {
            this.connectionManager = new ConnectionManager(this, this.server, this.port, this.user.name, this.user.mpPass);
            this.level = null;
        } else {
            boolean success = false;
    
            try {
                if(this.loadMapUser != null) {
                    success = this.loadLevel(this.loadMapUser, this.loadMapId);
                } else {
                    VFile2 f = new VFile2("level.dat");
                    if (f.exists()) {
                        Level level10 = null;
                        if(!(success = (level10 = this.levelIo.load(new DataInputStream(
                                PlatformRuntime.newGZIPInputStream(f.getInputStream())))) != null)) {
                            success = (level10 = this.levelIo.loadLegacy(new DataInputStream(
                                    PlatformRuntime.newGZIPInputStream(f.getInputStream())))) != null;
                        }
    
                        this.setLevel(level10);
                    }
                }
            } catch (Exception exception20) {
                exception20.printStackTrace();
                success = false;
            }
    
            if(!success) {
                this.generateLevel(1);
            }
        }

        this.levelRenderer = new LevelRenderer(this.textures);
        this.particleEngine = new ParticleEngine(this.level, this.textures);
        this.player = new Player(this.level, new MovementInputFromOptions());
        this.player.resetPos();
        if(this.level != null) {
            this.setLevel(this.level);
        }

        checkGlError("Post startup");
        this.hud = new InGameHud(this);
    }

    public final void setScreen(Screen screen1) {
        if(!(this.screen instanceof ErrorScreen)) {
            if(this.screen != null) {
                this.screen.closeScreen();
            }
    
            this.screen = screen1;
            if(screen1 != null) {
                this.releaseMouse();
                screen1.init(this, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
            } else {
                this.grabMouse();
            }
        }
    }

    private static void checkGlError(String string) {
        int errorCode = GL11.glGetError();
        if(errorCode != 0) {
            String errorString = GLU.gluErrorString(errorCode);
            System.out.println("########## GL ERROR ##########");
            System.out.println("@ " + string);
            System.out.println(errorCode + ": " + errorString);
        }

    }

    public final void destroy() {
        this.saveLevel();
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }

	public void run() {
        this.running = true;

        try {
            this.init();
        } catch (Exception var9) {
            var9.printStackTrace();
            EagRuntime.showPopup("Failed to start Minecraft");
            return;
        }

        long lastTime = EagRuntime.currentTimeMillis();
        int frames = 0;

		try {
            while(this.running) {
                if(this.pause) {
                    Thread.sleep(100L);
                } else {
                    if(Display.isCloseRequested()) {
                        this.stop();
                    }

                    try {
                        Timer timer39 = this.timer;
                        long j7;
                        long j43 = (j7 = EagRuntime.currentTimeMillis()) - timer39.lastSyncSysClock;
                        long j11 = EagRuntime.nanoTime() / 1000000L;
                        double d15;
                        if(j43 > 1000L) {
                            long j13 = j11 - timer39.lastSyncHRClock;
                            d15 = (double)j43 / (double)j13;
                            timer39.timeSyncAdjustment += (d15 - timer39.timeSyncAdjustment) * (double)0.2F;
                            timer39.lastSyncSysClock = j7;
                            timer39.lastSyncHRClock = j11;
                        }

                        if(j43 < 0L) {
                            timer39.lastSyncSysClock = j7;
                            timer39.lastSyncHRClock = j11;
                        }

                        double d48;
                        d15 = ((d48 = (double)j11 / 1000.0D) - timer39.lastHRTime) * timer39.timeSyncAdjustment;
                        timer39.lastHRTime = d48;
                        if(d15 < 0.0D) {
                            d15 = 0.0D;
                        }

                        if(d15 > 1.0D) {
                            d15 = 1.0D;
                        }

                        timer39.fps = (float)((double)timer39.fps + d15 * (double)timer39.timeScale * (double)timer39.ticksPerSecond);
                        timer39.ticks = (int)timer39.fps;
                        if(timer39.ticks > 100) {
                            timer39.ticks = 100;
                        }

                        timer39.fps -= (float)timer39.ticks;
                        timer39.a = timer39.fps;

                        PointerInputAbstraction.runGameLoop();

                        for(int i40 = 0; i40 < this.timer.ticks; ++i40) {
                            ++this.ticksRan;
        					this.tick();
                            if (i40 < this.timer.ticks - 1) {
                                PointerInputAbstraction.runGameLoop();
                            }
        				}
        
                        if (!Display.contextLost()) {
                            GL11.optimize();
                            checkGlError("Pre render");
                            float f33 = this.timer.a;
                            if(!Display.isActive()) {
                                this.pauseGame();
                            }

                            int i5;
                            int i34;
                            int i38;
                            i34 = 0;
                            i38 = 0;
                            i34 = PointerInputAbstraction.getDX();
                            i38 = PointerInputAbstraction.getDY();
                            this.player.turn((float)i34, (float)(i38 * this.yMouseAxis));

                            if(!this.hideGui) {
                                if(this.level != null) {
                                    this.render(f33);
                                    this.hud.render();
                                    checkGlError("Rendered gui");
                                } else {
                                    GL11.glViewport(0, 0, this.width, this.height);
                                    GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                                    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                                    GL11.glMatrixMode(GL11.GL_PROJECTION);
                                    GL11.glLoadIdentity();
                                    GL11.glMatrixMode(GL11.GL_MODELVIEW);
                                    GL11.glLoadIdentity();
                                    this.setupOrthoCamera();
                                }

                                if(this.screen != null) {
                                    GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                                    i34 = scaledResolution.getScaledWidth();
                                    i38 = scaledResolution.getScaledHeight();
                                    int i37 = Mouse.getX() * i34 / this.width;
                                    i5 = i38 - Mouse.getY() * i38 / this.height - 1;
                                    this.screen.render(i37, i5);
                                }
                            }
                            checkGlError("Post render");
                            GL11.glEnable(GL11.GL_TEXTURE_2D);
                            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                            this.setupOrthoCamera();
                            touchOverlayRenderer.render(width, height, scaledResolution);
                            GL11.disableBlend();
                            GL11.glDisable(GL11.GL_TEXTURE_2D);
                        }

                        this.updateDisplay();
                        ++frames;
                    } catch (Exception exception27) {
                        this.setScreen(new ErrorScreen("Client error", "The game broke! [" + exception27 + "]"));
                        exception27.printStackTrace();
                    }

                    while(EagRuntime.currentTimeMillis() >= lastTime + 1000L) {
                        this.fpsString = frames + " fps, " + Chunk.updates + " chunk updates";
                        Chunk.updates = 0;
                        lastTime += 1000L;
                        frames = 0;
                    }
                }
			}
        } catch (StopGameException stopGameException27) {
            return;
        } catch (ReportedException reportedexception) {
            this.displayCrashReport(reportedexception.getCrashReport());
        } catch (Throwable throwable1) {
            CrashReport crashreport1 = new CrashReport("Unexpected error", throwable1);
            this.displayCrashReport(crashreport1);
		} finally {
			this.destroy();
		}

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
        return this.level == null && this.screen != null ? 30 : 260;
    }

    public boolean isFramerateLimitBelowMax() {
        return (float) this.getLimitFramerate() < 260;
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
                && (Display.wasResized() || (dpiFetch = Math.min(Display.getDPI(), 2.0f)) != this.displayDPI)) {
            int i = this.width;
            int j = this.height;
            float f = this.displayDPI;
            this.width = Display.getWidth();
            this.height = Display.getHeight();
            this.displayDPI = dpiFetch == -1.0f ? Math.min(Display.getDPI(), 2.0f) : dpiFetch;
            if (this.width != i || this.height != j || this.displayDPI != f) {
                if (this.width <= 0) {
                    this.width = 1;
                }

                if (this.height <= 0) {
                    this.height = 1;
                }

                this.resize(this.width, this.height);
            }
        }

    }

    private void resize(int width, int height) {
        this.width = Math.max(1, width);
        this.height = Math.max(1, height);
        scaledResolution = new ScaledResolution(this);
        if (this.screen != null) {
            this.screen.init(this, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
        }
    }

    public void stop() {
        this.running = false;
    }

    public void grabMouse() {
        if (!Mouse.isActuallyGrabbed()) {
            Mouse.setGrabbed(true);
            this.setScreen((Screen)null);
            this.prevFrameTime = this.ticksRan + 10000;
        }
    }

    public void releaseMouse() {
        if (Mouse.isActuallyGrabbed()) {
            this.player.releaseAllKeys();
            Mouse.setGrabbed(false);
        }
    }

    public void pauseGame() {
        if(!(this.screen instanceof PauseScreen)) {
            this.setScreen(new PauseScreen());
        }
    }

    private void clickMouse() {
        if(this.hitResult != null) {
            int i1 = this.hitResult.x;
            int i2 = this.hitResult.y;
            int i3 = this.hitResult.z;
            if(this.editMode != 0) {
                if(this.hitResult.f == 0) {
                    --i2;
                }

                if(this.hitResult.f == 1) {
                    ++i2;
                }

                if(this.hitResult.f == 2) {
                    --i3;
                }

                if(this.hitResult.f == 3) {
                    ++i3;
                }

                if(this.hitResult.f == 4) {
                    --i1;
                }

                if(this.hitResult.f == 5) {
                    ++i1;
                }
            }

            Tile tile4 = Tile.tiles[this.level.getTile(i1, i2, i3)];
            if(this.editMode == 0) {
                if(tile4 != Tile.unbreakable || this.player.userType >= 100) {
                    boolean z8 = this.level.netSetTile(i1, i2, i3, 0);
                    if(tile4 != null && z8) {
                        if(this.isMultiplayer()) {
                            this.connectionManager.sendBlockChange(i1, i2, i3, this.editMode, this.player.inventory.getSelected());
                        }

                        tile4.destroy(this.level, i1, i2, i3, this.particleEngine);
                    }

                    return;
                }
            } else {
                int i5 = this.player.inventory.getSelected();
                AABB aABB7;
                if(((tile4 = Tile.tiles[this.level.getTile(i1, i2, i3)]) == null || tile4 == Tile.water || tile4 == Tile.calmWater || tile4 == Tile.lava || tile4 == Tile.calmLava) && ((aABB7 = Tile.tiles[i5].getAABB(i1, i2, i3)) == null || (this.player.bb.intersects(aABB7) ? false : this.level.isFree(aABB7)))) {
                    if(this.isMultiplayer()) {
                        this.connectionManager.sendBlockChange(i1, i2, i3, this.editMode, i5);
                    }

                    this.level.netSetTile(i1, i2, i3, this.player.inventory.getSelected());
                    Tile.tiles[i5].onBlockAdded(this.level, i1, i2, i3);
                }
            }
        }
    }

    private long placeTouchStartTime = -1l;
    private long mineTouchStartTime = -1l;
    private boolean wasMiningTouch = false;

    private void processTouchMine() {
        if ((screen == null)
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
                if (hitResult != null) {
                    clickMouse();
                }
            }
            placeTouchStartTime = -1l;
        }
    }

	private void tick() {
	    if(this.screen == null && !PointerInputAbstraction.isTouchMode() && !Mouse.isMouseGrabbed()) {
	        this.releaseMouse();
	        this.pauseGame();
	    }

        InGameHud inGameHud1 = this.hud;

        int i2;
        for(i2 = 0; i2 < inGameHud1.messages.size(); ++i2) {
            ++((ChatLine)inGameHud1.messages.get(i2)).counter;
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.getTextureId("/terrain.png"));
        Textures textures8 = this.textures;

        for(i2 = 0; i2 < textures8.textureList.size(); ++i2) {
            TextureFX textureFX3;
            (textureFX3 = (TextureFX)textures8.textureList.get(i2)).onTick();
            textures8.textureBuffer.clear();
            textures8.textureBuffer.put(textureFX3.imageData);
            textures8.textureBuffer.position(0).limit(textureFX3.imageData.length);
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, textureFX3.iconIndex % 16 << 4, textureFX3.iconIndex / 16 << 4, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textures8.textureBuffer);
        }

        int i1;
        int i3;
        int i4;
        int i13;
        if(this.connectionManager != null) {
            if(!this.connectionManager.isConnected()) {
                this.beginLevelLoading("Connecting..");
                this.setLoadingProgress(0);
            } else {
                ConnectionManager connectionManager8 = this.connectionManager;
                if(this.connectionManager.processData) {
                    SocketConnection socketConnection2 = connectionManager8.connection;
                    if(connectionManager8.connection.connected) {
                        try {
                            connectionManager8.connection.processData();
                        } catch (Exception exception7) {
                            connectionManager8.minecraft.setScreen(new ErrorScreen("Disconnected!", "You\'ve lost connection to the server"));
                            connectionManager8.minecraft.hideGui = false;
                            exception7.printStackTrace();
                            connectionManager8.connection.disconnect();
                            connectionManager8.minecraft.connectionManager = null;
                        }
                    }
                }

                Player player14 = this.player;
                if(this.connectionManager.connected) {
                    i13 = (int)(player14.x * 32.0F);
                    i4 = (int)(player14.y * 32.0F);
                    int i5 = (int)(player14.z * 32.0F);
                    int i6 = (int)(player14.yRot * 256.0F / 360.0F) & 255;
                    i2 = (int)(player14.xRot * 256.0F / 360.0F) & 255;
                    this.connectionManager.connection.sendPacket(Packet.PLAYER_TELEPORT, new Object[]{-1, i13, i4, i5, i6, i2});
                }
            }
        }

        if(this.screen != null) {
            this.prevFrameTime = this.ticksRan + 10000;
        } else {
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

                if (!touch) {
                    if (Mouse.getEventButtonState()) {
                        PointerInputAbstraction.enterMouseModeHook();
                    }

                    if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
                        this.clickMouse();
                        this.prevFrameTime = this.ticksRan;
                    }

                    if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
                        this.editMode = (this.editMode + 1) % 2;
                    }

                    if (Mouse.getEventButton() == 2 && Mouse.getEventButtonState() && this.hitResult != null) {
                        if((i2 = this.level.getTile(this.hitResult.x, this.hitResult.y, this.hitResult.z)) == Tile.grass.id) {
                            i2 = Tile.dirt.id;
                        }

                        Inventory inventory15 = this.player.inventory;
                        if((i3 = this.player.inventory.getSlotContainsID(i2)) >= 0) {
                            inventory15.selectedSlot = i3;
                        } else if(i2 > 0 && User.creativeTiles.contains(Tile.tiles[i2])) {
                            inventory15.getSlotContainsTile(Tile.tiles[i2]);
                        }
                    }

                    if ((i1 = Mouse.getEventDWheel()) != 0) {
                        i2 = i1;
                        Inventory inventory11 = this.player.inventory;
                        if(i1 > 0) {
                            i2 = 1;
                        }

                        if(i2 < 0) {
                            i2 = -1;
                        }

                        for(inventory11.selectedSlot -= i2; inventory11.selectedSlot < 0; inventory11.selectedSlot += inventory11.slots.length) {
                        }

                        while(inventory11.selectedSlot >= inventory11.slots.length) {
                            inventory11.selectedSlot -= inventory11.slots.length;
                        }
                    }
                }

                if (!(touch || Mouse.isActuallyGrabbed()) && (touch || Mouse.getEventButtonState())) {
                    this.grabMouse();
                }
            }

            processTouchMine();

            while(Keyboard.next()) {
                this.player.setKey(Keyboard.getEventKey(), Keyboard.getEventKeyState());
                if(Keyboard.getEventKeyState()) {
                    if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                        this.pauseGame();
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_R) {
                        this.player.resetPos();
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
                        this.saveSpawn();
                    }

                    for(i1 = 0; i1 < 9; ++i1) {
                        if(Keyboard.getEventKey() == i1 + Keyboard.KEY_1) {
                            this.player.inventory.selectedSlot = i1;
                        }
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_Y) {
                        this.yMouseAxis = -this.yMouseAxis;
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_G && this.connectionManager == null && this.level.entities.size() < 256) {
                        this.addZombie();
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_F) {
                        LevelRenderer levelRenderer10000 = this.levelRenderer;
                        boolean z15 = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
                        levelRenderer10000.drawDistance = levelRenderer10000.drawDistance + (z15 ? -1 : 1) & 3;
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_B) {
                        this.setScreen(new InventoryScreen());
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_T && this.connectionManager != null && this.connectionManager.isConnected()) {
                        this.player.releaseAllKeys();
                        this.setScreen(new ChatScreen());
                    }
                }
            }

            boolean touchMode = PointerInputAbstraction.isTouchMode();
            boolean miningTouch = touchMode && isMiningTouch();
            if (miningTouch && !wasMiningTouch) {
                if (hitResult != null || touchMode) {
                    this.clickMouse();
                    this.prevFrameTime = this.ticksRan;
                }
                wasMiningTouch = true;
            }
            wasMiningTouch = miningTouch;

            if((Mouse.isButtonDown(0) || miningTouch) && (float)(this.ticksRan - this.prevFrameTime) >= this.timer.ticksPerSecond / 4.0F) {
                this.clickMouse();
                this.prevFrameTime = this.ticksRan;
            }
	    }

        if(this.screen != null) {
            this.screen.updateEvents();
            if(this.screen != null) {
                this.screen.tick();
            }
        }

        if(this.level != null) {
            ++this.levelRenderer.cloudTickCounter;
            this.level.tickEntities();
            if(!this.isMultiplayer()) {
                this.level.tick();
            }

            ParticleEngine particleEngine13 = this.particleEngine;

            for(int i10 = 0; i10 < particleEngine13.particles.size(); ++i10) {
                Particle particle14;
                (particle14 = (Particle)particleEngine13.particles.get(i10)).tick();
                if(particle14.removed) {
                    particleEngine13.particles.remove(i10--);
                }
            }

            this.player.tick();
        }
    }

    private boolean isMultiplayer() {
        return this.connectionManager != null;
    }

    private void orientCamera(float a) {
        GL11.glTranslatef(0.0F, 0.0F, -0.3F);
        GL11.glRotatef(this.player.xRotO + (this.player.xRot - this.player.xRotO) * a, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(this.player.yRotO + (this.player.yRot - this.player.yRotO) * a, 0.0F, 1.0F, 0.0F);
        float x = this.player.xo + (this.player.x - this.player.xo) * a;
        float y = this.player.yo + (this.player.y - this.player.yo) * a;
        float z = this.player.zo + (this.player.z - this.player.zo) * a;
        GL11.glTranslatef(-x, -y, -z);
    }

    private void setupCamera(float a) {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(70.0F, (float)this.width / (float)this.height, 0.05F, this.renderDistance);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        this.orientCamera(a);
    }

	private void setupOrthoCamera() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, scaledResolution.getScaledWidth_double(),
                scaledResolution.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
	}

    private void pick(float a) {
        float f65 = this.player.xRotO + (this.player.xRot - this.player.xRotO) * a;
        float f72 = this.player.yRotO + (this.player.yRot - this.player.yRotO) * a;
        float f78 = this.player.xo + (this.player.x - this.player.xo) * a;
        float f57 = this.player.yo + (this.player.y - this.player.yo) * a;
        float f55 = this.player.zo + (this.player.z - this.player.zo) * a;
        Vec3 vec359 = new Vec3(f78, f57, f55);
        f55 = (float)Math.cos((double)(-f72) * Math.PI / 180.0D + Math.PI);
        float f66 = (float)Math.sin((double)(-f72) * Math.PI / 180.0D + Math.PI);
        f72 = (float)Math.cos((double)(-f65) * Math.PI / 180.0D);
        f65 = (float)Math.sin((double)(-f65) * Math.PI / 180.0D);
        f66 *= f72;
        f55 *= f72;
        f72 = 4.0F;
        float f10001 = f66 * f72;
        float f10002 = f65 * f72;
        f72 = f55 * f72;
        f65 = f10002;
        f66 = f10001;
        Vec3 vec361 = new Vec3(vec359.x + f66, vec359.y + f65, vec359.z + f72);
        this.hitResult = this.level.clip(vec359, vec361);
    }

	public void render(float a) {
        GL11.glViewport(0, 0, this.width, this.height);
        float f4 = (float)Math.pow((double)(f4 = 1.0F / (float)(4 - this.levelRenderer.drawDistance)), 0.25D);
        this.fogColorRed = 0.6F * (1.0F - f4) + f4;
        this.fogColorGreen = 0.8F * (1.0F - f4) + f4;
        this.fogColorBlue = 1.0F * (1.0F - f4) + f4;
        this.fogColorRed *= this.fogColorMultiplier;
        this.fogColorGreen *= this.fogColorMultiplier;
        this.fogColorBlue *= this.fogColorMultiplier;
        Tile tile5;
        if((tile5 = Tile.tiles[this.level.getTile((int)this.player.x, (int)(this.player.y + 0.12F), (int)this.player.z)]) != null && tile5.getLiquidType() != Liquid.none) {
            Liquid liquid21;
            if((liquid21 = tile5.getLiquidType()) == Liquid.water) {
                this.fogColorRed = 0.02F;
                this.fogColorGreen = 0.02F;
                this.fogColorBlue = 0.2F;
            } else if(liquid21 == Liquid.lava) {
                this.fogColorRed = 0.6F;
                this.fogColorGreen = 0.1F;
                this.fogColorBlue = 0.0F;
            }
        }

        GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        checkGlError("Set viewport");
        this.pick(a);
        checkGlError("Picked");
        this.fogColorMultiplier = 1.0F;
        this.renderDistance = (float)(512 >> (this.levelRenderer.drawDistance << 1));
        this.setupCamera(a);
        checkGlError("Set up camera");
        GL11.glEnable(GL11.GL_CULL_FACE);
        Frustum frustum22 = Frustum.getFrustum();
        Frustum frustum23 = frustum22;
        LevelRenderer levelRenderer18 = this.levelRenderer;

        for(int i5 = 0; i5 < levelRenderer18.sortedChunks.length; ++i5) {
            levelRenderer18.sortedChunks[i5].isInFrustum(frustum23);
        }

        levelRenderer18 = this.levelRenderer;
        Collections.sort(this.levelRenderer.dirtyChunks, new DirtyChunkSorter(this.player));
        int i105 = this.levelRenderer.dirtyChunks.size() - 1;
        int i108;
        if((i108 = this.levelRenderer.dirtyChunks.size()) > 4) {
            i108 = 4;
        }

        int i109;
        for(i109 = 0; i109 < i108; ++i109) {
            ((Chunk)this.levelRenderer.dirtyChunks.remove(i105 - i109)).rebuild();
        }

        checkGlError("Update chunks");
        boolean z21 = this.level.isSolid(this.player.x, this.player.y, this.player.z, 0.1F);
        this.setupFog();
        GL11.glEnable(GL11.GL_FOG);
        this.levelRenderer.render(this.player, 0);
        if(z21) {
            int i4 = (int)this.player.x;
            int i5 = (int)this.player.y;
            int i24 = (int)this.player.z;

            for(int i2 = i4 - 1; i2 <= i4 + 1; ++i2) {
                for(int i7 = i5 - 1; i7 <= i5 + 1; ++i7) {
                    for(int i8 = i24 - 1; i8 <= i24 + 1; ++i8) {
                        this.levelRenderer.render(i2, i7, i8);
                    }
                }
            }
        }

        checkGlError("Rendered level");
        this.toggleLight(true);
        this.levelRenderer.renderEntities(frustum22, a);
        this.toggleLight(false);
        this.setupFog();
        checkGlError("Rendered entities");
        this.particleEngine.render(this.player, a);
        checkGlError("Rendered particles");
        GL11.glCallList(this.levelRenderer.surroundLists);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.setupFog();
        this.levelRenderer.renderClouds(a);
        this.setupFog();
        GL11.glEnable(GL11.GL_LIGHTING);
        if(this.hitResult != null) {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            this.levelRenderer.renderHit(this.player, this.hitResult, this.editMode, this.player.inventory.getSelected());
            LevelRenderer.renderHitOutline(this.hitResult, this.editMode);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.setupFog();
        GL11.glCallList(this.levelRenderer.surroundLists + 1);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColorMask(false, false, false, false);
        int i20 = this.levelRenderer.render(this.player, 1);
        GL11.glColorMask(true, true, true, true);
        if(i20 > 0) {
            levelRenderer18 = this.levelRenderer;
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, levelRenderer18.textures.getTextureId("/terrain.png"));
            GL11.glCallLists(levelRenderer18.dummyBuffer);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
        }

        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        if(this.hitResult != null) {
            GL11.glDepthFunc(GL11.GL_LESS);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.doPolygonOffset(99.0F, -99.0F);
            GL11.enablePolygonOffset();
            this.levelRenderer.renderHit(this.player, this.hitResult, this.editMode, this.player.inventory.getSelected());
            LevelRenderer.renderHitOutline(this.hitResult, this.editMode);
            GL11.disablePolygonOffset();
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
        }

	}

    private void toggleLight(boolean z1) {
        if(!z1) {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_LIGHT0);
        } else {
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_LIGHT0);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
            float f4 = 0.7F;
            float f2 = 0.3F;
            Vec3 vec33 = (new Vec3(0.0F, -1.0F, 0.5F)).normalize();
            GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, this.getBuffer(vec33.x, vec33.y, vec33.z, 0.0F));
            GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, this.getBuffer(f2, f2, f2, 1.0F));
            GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, this.getBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(f4, f4, f4, 1.0F));
        }
    }

    public final void initGui() {
        this.setupOrthoCamera();
    }

    public void addZombie() {
        this.level.entities.add(new Zombie(this.level, this.player.x, this.player.y, this.player.z));
    }

    public void saveSpawn() {
        this.level.setSpawnPos((int)this.player.x, (int)this.player.y, (int)this.player.z, this.player.yRot);
        this.player.resetPos();
    }

    private void setupFog() {
        GL11.glFog(GL11.GL_FOG_COLOR, this.getBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
        Tile tile1;
        if((tile1 = Tile.tiles[this.level.getTile((int)this.player.x, (int)(this.player.y + 0.12F), (int)this.player.z)]) != null && tile1.getLiquidType() != Liquid.none) {
            Liquid liquid2 = tile1.getLiquidType();
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            if(liquid2 == Liquid.water) {
                GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
                GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(0.4F, 0.4F, 0.9F, 1.0F));
            } else if(liquid2 == Liquid.lava) {
                GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
                GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(0.4F, 0.3F, 0.3F, 1.0F));
            }
        } else {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
            GL11.glFogf(GL11.GL_FOG_START, 0.0F);
            GL11.glFogf(GL11.GL_FOG_END, this.renderDistance);
            GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(1.0F, 1.0F, 1.0F, 1.0F));
        }

        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private FloatBuffer getBuffer(float f1, float f2, float f3, float f4) {
        this.lb.clear();
        this.lb.put(f1).put(f2).put(f3).put(f4);
        this.lb.flip();
        return this.lb;
    }

    public void beginLevelLoading(String title) {
        if(!this.running) {
            throw new StopGameException();
        } else {
            this.title = title;
            int screenWidth = scaledResolution.getScaledWidth();
            int screenHeight = scaledResolution.getScaledHeight();
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0D, (double)screenWidth, (double)screenHeight, 0.0D, 100.0D, 300.0D);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, 0.0F, -200.0F);
        }
    }

    public final void levelLoadUpdate(String string1) {
        if(!this.running) {
            throw new StopGameException();
        } else {
            this.text = string1;
            this.setLoadingProgress(-1);
        }
    }

    public final void setLoadingProgress(int i1) {
        if(!this.running) {
            throw new StopGameException();
        } else {
            if(i1 >= 0) {
                return;
            }
            int i2 = scaledResolution.getScaledWidth();
            int i3 = scaledResolution.getScaledHeight();
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
            Tesselator tesselator4 = Tesselator.instance;
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            int i5 = this.textures.getTextureId("/dirt.png");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, i5);
            float f8 = 32.0F;
            tesselator4.begin(DefaultVertexFormats.POSITION_TEX_COLOR);
            tesselator4.color(4210752);
            tesselator4.vertexUV(0.0F, (float)i3, 0.0F, 0.0F, (float)i3 / f8);
            tesselator4.vertexUV((float)i2, (float)i3, 0.0F, (float)i2 / f8, (float)i3 / f8);
            tesselator4.vertexUV((float)i2, 0.0F, 0.0F, (float)i2 / f8, 0.0F);
            tesselator4.vertexUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            tesselator4.end();
            if(i1 >= 0) {
                i5 = i2 / 2 - 50;
                int i6 = i3 / 2 + 16;
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                tesselator4.begin(DefaultVertexFormats.POSITION_COLOR);
                tesselator4.color(8421504);
                tesselator4.vertex((float)i5, (float)i6, 0.0F);
                tesselator4.vertex((float)i5, (float)(i6 + 2), 0.0F);
                tesselator4.vertex((float)(i5 + 100), (float)(i6 + 2), 0.0F);
                tesselator4.vertex((float)(i5 + 100), (float)i6, 0.0F);
                tesselator4.color(8454016);
                tesselator4.vertex((float)i5, (float)i6, 0.0F);
                tesselator4.vertex((float)i5, (float)(i6 + 2), 0.0F);
                tesselator4.vertex((float)(i5 + i1), (float)(i6 + 2), 0.0F);
                tesselator4.vertex((float)(i5 + i1), (float)i6, 0.0F);
                tesselator4.end();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }

            this.font.drawShadow(this.title, (i2 - this.font.width(this.title)) / 2, i3 / 2 - 4 - 16, 0xFFFFFF);
            this.font.drawShadow(this.text, (i2 - this.font.width(this.text)) / 2, i3 / 2 - 4 + 8, 0xFFFFFF);
            Display.update();

            try {
                Thread.sleep(200L);
            } catch (Exception var8) {
            }
        }
    }

    public final void generateLevel(int i1) {
        String string2 = this.user != null ? this.user.name : "anonymous";
        this.setLevel(this.levelGen.generateLevel(string2, 128 << i1, 128 << i1, 64));
    }

    public final boolean saveLevel() {
        try {
            LevelIO.save(this.level, new DataOutputStream(
                    PlatformRuntime.newGZIPOutputStream(new VFile2("level.dat").getOutputStream())));
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public final boolean loadLevel(String string1, int i2) {
        VFile2 f = new VFile2("level.dat");
        if (!f.exists()) {
            return false;
        }
        Level level3;
        try {
            if((level3 = this.levelIo.load(new DataInputStream(
                    PlatformRuntime.newGZIPInputStream(f.getInputStream())))) == null) {
                level3 = this.levelIo.loadLegacy(new DataInputStream(
                        PlatformRuntime.newGZIPInputStream(f.getInputStream())));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if(level3 == null) {
            return false;
        } else {
            this.setLevel(level3);
            return true;
        }
    }

    public final void setLevel(Level level1) {
        this.level = level1;
        if(this.levelRenderer != null) {
            this.levelRenderer.setLevel(level1);
        }

        if(this.particleEngine != null) {
            this.particleEngine.particles.clear();
        }

        if(this.player != null) {
            this.player.setLevel(level1);
            this.player.resetPos();
        }

        System.gc();
    }

    public final void addChatMessage(String string1) {
        InGameHud inGameHud2;
        (inGameHud2 = this.hud).messages.add(0, new ChatLine(string1));

        while(inGameHud2.messages.size() > 50) {
            inGameHud2.messages.remove(inGameHud2.messages.size() - 1);
        }

    }

	public static void main(String[] args) throws LWJGLException {
        PlatformRuntime.setThreadName("Client thread");
        (minecraft = new Minecraft(854, 480, false)).run();
	}

    public static void main(String[] args, String username, String server, int port, String mpPass) throws LWJGLException {
        PlatformRuntime.setThreadName("Client thread");
        (minecraft = new Minecraft(854, 480, false)).setServer(server, port);
        minecraft.user = new User(username, "");
        minecraft.user.mpPass = mpPass;
        minecraft.run();
    }
}
