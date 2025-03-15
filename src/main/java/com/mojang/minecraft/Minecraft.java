package com.mojang.minecraft;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.character.Cube;
import com.mojang.minecraft.character.Vec3;
import com.mojang.minecraft.gamemode.GameMode;
import com.mojang.minecraft.gamemode.SurvivalGameMode;
import com.mojang.minecraft.gui.ChatScreen;
import com.mojang.minecraft.gui.DeathScreen;
import com.mojang.minecraft.gui.ErrorScreen;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.gui.Gui;
import com.mojang.minecraft.gui.PauseScreen;
import com.mojang.minecraft.gui.Screen;
import com.mojang.minecraft.item.Arrow;
import com.mojang.minecraft.item.Sign;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelIO;
import com.mojang.minecraft.level.levelgen.LevelGen;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.net.Client;
import com.mojang.minecraft.net.Packet;
import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.player.Inventory;
import com.mojang.minecraft.player.KeyboardInput;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.renderer.Chunk;
import com.mojang.minecraft.renderer.DirtyChunkSorter;
import com.mojang.minecraft.renderer.Frustum;
import com.mojang.minecraft.renderer.GameRenderer;
import com.mojang.minecraft.renderer.LevelRenderer;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;
import com.mojang.minecraft.renderer.texture.DynamicTexture;
import com.mojang.minecraft.renderer.texture.LavaTexture;
import com.mojang.minecraft.renderer.texture.WaterTexture;
import com.mojang.minecraft.sound.SoundEngine;
import com.mojang.minecraft.sound.SoundPlayer;
import com.mojang.minecraft.tilerenderer.TileRenderer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Collections;
import java.util.List;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.ScaledResolution;
import net.lax1dude.eaglercraft.Touch;
import net.lax1dude.eaglercraft.crash.CrashReport;
import net.lax1dude.eaglercraft.internal.EnumPlatformType;
import net.lax1dude.eaglercraft.internal.PlatformRuntime;
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
    public GameMode gamemode = new SurvivalGameMode(this);
    private boolean fullscreen = false;
	public int width;
	public int height;
    public float displayDPI = 1.0f;
	private Timer timer = new Timer(20.0F);
    public Level level;
	public LevelRenderer levelRenderer;
    public Player player;
	public ParticleEngine particleEngine;
    public User user = null;
    public String host;
    public boolean appletMode = false;
    public volatile boolean pause = false;
    public Textures textures;
    public Font font;
    public Screen screen = null;
    public LevelLoaderListener loadingScreen = new LevelLoaderListener(this);
    public GameRenderer lighting = new GameRenderer(this);
    public LevelIO levelIo = new LevelIO(this.loadingScreen);
    private LevelGen levelGen = new LevelGen(this.loadingScreen);
    public SoundEngine soundEngine = new SoundEngine();
    private int frames = 0;
    public String loadMapUser = null;
    public int loadMapId = 0;
    public Gui gui;
    public boolean hideScreen = false;
    public Client networkClient;
    public SoundPlayer soundPlayer;
    public HitResult hitResult;
    public Options options;
    String server;
    int port;
    volatile boolean running;
    public String fpsString;
    private int oFrames;
    private int rightClickDelayTimer;
    public boolean mouseGrabSupported = false;
    public static ScaledResolution scaledResolution;
    public static TouchOverlayRenderer touchOverlayRenderer;
    public static Minecraft minecraft;

    public Minecraft(int width, int height, boolean fullscreen) {
        new HumanoidModel(0.0F);
        this.hitResult = null;
        this.server = null;
        this.port = 0;
        this.running = false;
        this.fpsString = "";
        this.oFrames = 0;
        this.width = width;
        this.height = height;
        this.fullscreen = fullscreen;
        this.options = new Options(this);
    }

    public final void setServer(String string1, int i2) {
        this.server = string1;
        this.port = i2;
    }

    public void init() throws LWJGLException {
        this.width = Display.getVisualViewportW() != 0 ? Display.getVisualViewportW() : this.width;
        this.height = Display.getVisualViewportH() != 0 ? Display.getVisualViewportH() : this.height;
        if(this.fullscreen) {
            Display.setFullscreen(true);
            this.width = Display.getDisplayMode().getWidth();
            this.height = Display.getDisplayMode().getHeight();
        } else {
            Display.setDisplayMode(new DisplayMode(this.width, this.height));
        }
        this.displayDPI = Math.max(Math.min(Display.getDPI(), 2.0f), 1.0f);

        Display.setTitle("Minecraft 0.24_SURVIVAL_TEST_03");

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
        this.textures = new Textures(this.options);
        this.textures.addDynamicTexture(new LavaTexture());
        this.textures.addDynamicTexture(new WaterTexture());
        this.font = new Font("/default.png", this.textures);
        IntBuffer imgData = BufferUtils.createIntBuffer(256);
        imgData.clear().limit(256);
        this.levelRenderer = new LevelRenderer(this, this.textures);
        GL11.glViewport(0, 0, this.width, this.height);
        this.mouseGrabSupported = Mouse.isMouseGrabSupported();
        touchOverlayRenderer = new TouchOverlayRenderer();
        scaledResolution = new ScaledResolution(this);
        PointerInputAbstraction.init(this);
        this.level = new Level();
        if(this.server != null && this.user != null) {
            this.level = null;
        } else {
            try {
                if(this.loadMapUser != null) {
                    this.loadLevel(this.loadMapUser, this.loadMapId);
                }
            } catch (Exception exception20) {
                exception20.printStackTrace();
            }

            this.generateLevel(1);
        }

        this.particleEngine = new ParticleEngine(this.level, this.textures);

        this.soundEngine.registerSounds();
        this.soundPlayer = new SoundPlayer(this.options);

        checkGlError("Post startup");
        this.gui = new Gui(this, this.width, this.height);
        if(this.server != null && this.user != null) {
            this.networkClient = new Client(this, this.server, this.port, this.user.name, this.user.mpPass);
        }
    }

    public final void setScreen(Screen screen) {
        if(!(this.screen instanceof ErrorScreen)) {
            if(this.screen != null) {
                this.screen.removed();
            }


            if(screen == null && this.player.health <= 0) {
                screen = new DeathScreen();
            }

            this.screen = (Screen)screen;
            if(screen != null) {
                this.releaseMouse();
                ((Screen)screen).init(this, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
                this.hideScreen = false;
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
                        Timer timer50 = this.timer;
                        long j54;
                        long j57 = (j54 = EagRuntime.currentTimeMillis()) - timer50.msPerTick;
                        long j62 = EagRuntime.nanoTime() / 1000000L;
                        double d77;
                        if(j57 > 1000L) {
                            long j71 = j62 - timer50.passedTime;
                            d77 = (double)j57 / (double)j71;
                            timer50.averageFrameTime += (d77 - timer50.averageFrameTime) * (double)0.2F;
                            timer50.msPerTick = j54;
                            timer50.passedTime = j62;
                        }

                        if(j57 < 0L) {
                            timer50.msPerTick = j54;
                            timer50.passedTime = j62;
                        }

                        double d72;
                        d77 = ((d72 = (double)j62 / 1000.0D) - timer50.lastTime) * timer50.averageFrameTime;
                        timer50.lastTime = d72;
                        if(d77 < 0.0D) {
                            d77 = 0.0D;
                        }

                        if(d77 > 1.0D) {
                            d77 = 1.0D;
                        }

                        timer50.ticks = (float)((double)timer50.ticks + d77 * (double)timer50.fps * (double)timer50.ticksPerSecond);
                        timer50.frames = (int)timer50.ticks;
                        if(timer50.frames > 100) {
                            timer50.frames = 100;
                        }

                        timer50.ticks -= (float)timer50.frames;
                        timer50.alpha = timer50.ticks;

                        PointerInputAbstraction.runGameLoop();

                        for(int i40 = 0; i40 < this.timer.frames; ++i40) {
                            ++this.frames;
        					this.tick();
                            if (i40 < this.timer.frames - 1) {
                                PointerInputAbstraction.runGameLoop();
                            }
        				}

                        if (!Display.contextLost()) {
                            GL11.optimize();
                            checkGlError("Pre render");
                            this.gamemode.render(this.timer.alpha);
                            this.soundPlayer.setListener(this.player, this.timer.alpha);
                            float f33 = this.timer.alpha;
                            if(this.lighting.displayActive && !Display.isActive()) {
                                this.lighting.minecraft.pauseScreen();
                            }

                            this.lighting.displayActive = Display.isActive();
                            int i5;
                            int i34;
                            int i38;
                            i34 = 0;
                            i38 = 0;
                            i34 = PointerInputAbstraction.getDX();
                            i38 = PointerInputAbstraction.getDY();

                            byte b57 = 1;
                            if(this.options.invertYMouse) {
                                b57 = -1;
                            }

                            this.player.turn((float)i34, (float)(i38 * b57));

                            if(!this.hideScreen) {
                                int i53 = scaledResolution.getScaledWidth();
                                int i56 = scaledResolution.getScaledHeight();
                                int i60 = Mouse.getX() * i53 / this.width;
                                int i62 = i56 - Mouse.getY() * i56 / this.height - 1;
                                if(this.level != null) {
                                    this.render(f33);
                                    this.gui.render(f33, this.screen != null, i60, i62);
                                } else {
                                    GL11.glViewport(0, 0, this.width, this.height);
                                    GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                                    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                                    GL11.glMatrixMode(GL11.GL_PROJECTION);
                                    GL11.glLoadIdentity();
                                    GL11.glMatrixMode(GL11.GL_MODELVIEW);
                                    GL11.glLoadIdentity();
                                    this.lighting.init();
                                }

                                if(this.screen != null) {
                                    GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                                    this.screen.render(i60, i62);
                                }
                            }

                            checkGlError("Post render");
                            GL11.glEnable(GL11.GL_TEXTURE_2D);
                            this.setupOrthoCamera();
                            touchOverlayRenderer.render(width, height, scaledResolution);
                            GL11.disableBlend();
                            GL11.glDisable(GL11.GL_TEXTURE_2D);
                        }

                        Thread.yield();
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
                && (Display.wasResized() || (dpiFetch = Math.max(Math.min(Display.getDPI(), 2.0f), 1.0f)) != this.displayDPI)) {
            int i = this.width;
            int j = this.height;
            float f = this.displayDPI;
            this.width = Display.getWidth();
            this.height = Display.getHeight();
            this.displayDPI = dpiFetch == -1.0f ? Math.max(Math.min(Display.getDPI(), 2.0f), 1.0f) : dpiFetch;
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
            this.oFrames = this.frames + 10000;
        }
    }

    public void releaseMouse() {
        if (Mouse.isActuallyGrabbed()) {
            this.player.releaseAllKeys();
            Mouse.setGrabbed(false);
        }
    }

    public final void pauseScreen() {
        if(this.screen == null) {
            this.setScreen(new PauseScreen());
        }
    }

    private void clickMouse(int id) {
        TileRenderer tileRenderer6;
        if(id == 0) {
            tileRenderer6 = this.lighting.tileRenderer;
            this.lighting.tileRenderer.rot = -1;
            tileRenderer6.move = true;
        } else {
            this.rightClickDelayTimer = 4;
        }

        int i2;
        if(id == 1 && (i2 = this.player.inventory.getSelected()) > 0 && this.gamemode.removeResource(this.player, i2)) {
            tileRenderer6 = this.lighting.tileRenderer;
            this.lighting.tileRenderer.progress = 0.0F;
        } else if(this.hitResult != null) {
            if(this.hitResult.type == 1) {
                this.hitResult.entity.hurt(this.player, 4);
            } else {
                if(this.hitResult.type == 0) {
                    i2 = this.hitResult.x;
                    int i3 = this.hitResult.y;
                    int i4 = this.hitResult.z;
                    if(id != 0) {
                        if(this.hitResult.f == 0) {
                            --i3;
                        }

                        if(this.hitResult.f == 1) {
                            ++i3;
                        }

                        if(this.hitResult.f == 2) {
                            --i4;
                        }

                        if(this.hitResult.f == 3) {
                            ++i4;
                        }

                        if(this.hitResult.f == 4) {
                            --i2;
                        }

                        if(this.hitResult.f == 5) {
                            ++i2;
                        }
                    }

                    Tile tile5 = Tile.tiles[this.level.getTile(i2, i3, i4)];
                    if(id == 0) {
                        if(tile5 != Tile.unbreakable || this.player.userType >= 100) {
                            this.gamemode.startDestroyBlock(i2, i3, i4);
                            return;
                        }
                    } else {
                        int i8;
                        if((i8 = this.player.inventory.getSelected()) <= 0) {
                            return;
                        }

                        Tile tile9;
                        AABB aABB10;
                        if(((tile9 = Tile.tiles[this.level.getTile(i2, i3, i4)]) == null || tile9 == Tile.water || tile9 == Tile.calmWater || tile9 == Tile.lava || tile9 == Tile.calmLava) && ((aABB10 = Tile.tiles[i8].getTileAABB(i2, i3, i4)) == null || (this.player.bb.intersects(aABB10) ? false : this.level.isFree(aABB10)))) {
                            if(!this.gamemode.removeResource(i8)) {
                                return;
                            }

                            if(this.isOnlineClient()) {
                                this.networkClient.sendTileUpdated(i2, i3, i4, id, i8);
                            }

                            this.level.netSetTile(i2, i3, i4, i8);
                            tileRenderer6 = this.lighting.tileRenderer;
                            this.lighting.tileRenderer.progress = 0.0F;
                            Tile.tiles[i8].onPlace(this.level, i2, i3, i4);
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
                if (hitResult != null && hitResult.entity != null) {
                    clickMouse(0);
                } else {
                    clickMouse(1);
                }
            }
            placeTouchStartTime = -1l;
        }
    }

	private void tick() {
        if (this.rightClickDelayTimer > 0) {
            --this.rightClickDelayTimer;
        }

	    if(this.screen == null && !PointerInputAbstraction.isTouchMode() && !Mouse.isMouseGrabbed()
	       && (this.networkClient == null || this.networkClient.connected)) {
	        this.releaseMouse();
	        this.pauseScreen();
	    }

        if(this.soundPlayer != null) {
            SoundPlayer soundPlayer2 = this.soundPlayer;
            if(System.currentTimeMillis() > this.soundEngine.lastMusic && this.soundEngine.playMusic(soundPlayer2, "calm")) {
                this.soundEngine.lastMusic = System.currentTimeMillis() + (long)this.soundEngine.random.nextInt(900000) + 300000L;
            }
        }

        Gui gui14 = this.gui;
        ++this.gui.tickCounter;

        int i17;
        for(i17 = 0; i17 < gui14.messages.size(); ++i17) {
            ++((GuiMessage)gui14.messages.get(i17)).counter;
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/terrain.png"));
        Textures textures15 = this.textures;

        for(i17 = 0; i17 < textures15.textureList.size(); ++i17) {
            DynamicTexture dynamicTexture3;
            (dynamicTexture3 = (DynamicTexture)textures15.textureList.get(i17)).anaglyph = textures15.options.anaglyph3d;
            dynamicTexture3.tick();
            textures15.pixels.clear();
            textures15.pixels.put(dynamicTexture3.pixels);
            textures15.pixels.position(0).limit(dynamicTexture3.pixels.length);
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, dynamicTexture3.tex % 16 << 4, dynamicTexture3.tex / 16 << 4, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textures15.pixels);
        }

        int i1;
        int i3;
        int i4;
        int i13;
        int i41;
        if(this.networkClient != null && !(this.screen instanceof ErrorScreen)) {
            if(!this.networkClient.isConnected()) {
                this.loadingScreen.beginLevelLoading("Connecting..");
                this.loadingScreen.setLoadingProgress(0);
            } else {
                if(this.networkClient.processData) {
                    if(this.networkClient.serverConnection.connected) {
                        try {
                            this.networkClient.serverConnection.processData();
                        } catch (Exception exception7) {
                            this.networkClient.minecraft.setScreen(new ErrorScreen("Disconnected!", "You\'ve lost connection to the server"));
                            this.networkClient.minecraft.hideScreen = false;
                            exception7.printStackTrace();
                            this.networkClient.serverConnection.disconnect();
                            this.networkClient.minecraft.networkClient = null;
                        }
                    }
                }

                Player player14 = this.player;
                if(this.networkClient.connected) {
                    i13 = (int)(player14.x * 32.0F);
                    i4 = (int)(player14.y * 32.0F);
                    int i5 = (int)(player14.z * 32.0F);
                    int i6 = (int)(player14.yRot * 256.0F / 360.0F) & 255;
                    int i2 = (int)(player14.xRot * 256.0F / 360.0F) & 255;
                    this.networkClient.serverConnection.sendPacket(Packet.PLAYER_TELEPORT, new Object[]{-1, i13, i4, i5, i6, i2});
                }
            }
        }

        if(this.screen == null && this.player.health <= 0) {
            this.setScreen((Screen)null);
        }

        if(this.screen == null && (this.networkClient == null
        || (this.networkClient.serverConnection != null && this.networkClient.connected))) {
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
                        if(this.screen != null) {
                            this.screen.touchEvent();
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

                    if(this.screen == null) {
                        if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
                            this.clickMouse(0);
                            this.oFrames = this.frames;
                        }

                        if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
                            this.clickMouse(1);
                            this.oFrames = this.frames;
                        }

                        if (Mouse.getEventButton() == 2 && Mouse.getEventButtonState() && this.hitResult != null) {
                            if((i17 = this.level.getTile(this.hitResult.x, this.hitResult.y, this.hitResult.z)) == Tile.grass.id) {
                                i17 = Tile.dirt.id;
                            }

                            Inventory inventory15 = this.player.inventory;
                            if((i41 = this.player.inventory.containsTileAt(i17)) >= 0) {
                                inventory15.selected = i41;
                            }
                        }
                    }

                    if ((i1 = Mouse.getEventDWheel()) != 0) {
                        this.player.inventory.swapPaint(i1);
                    }

                    if(this.screen != null) {
                        this.screen.mouseEvent();
                    }
                }

                if (this.screen == null && !(touch || Mouse.isActuallyGrabbed()) && (touch || Mouse.getEventButtonState())) {
                    this.grabMouse();
                }
            }

            processTouchMine();

            while(Keyboard.next()) {
                this.player.setKey(Keyboard.getEventKey(), Keyboard.getEventKeyState());
                if(Keyboard.getEventKeyState()) {
                    if(this.screen != null) {
                        this.screen.keyboardEvent();
                    }

                    if(this.screen == null) {
                        if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                            this.pauseScreen();
                        }

                        if(Keyboard.getEventKey() == this.options.build.key && this.networkClient == null) {
                            this.level.addEntity(new Sign(this, this.player.x, this.player.y, this.player.z, this.player.yRot));
                        }

                        if(Keyboard.getEventKey() == Keyboard.KEY_TAB && this.networkClient == null) {
                            this.shootArrow();
                        }

                        if(Keyboard.getEventKey() == this.options.chat.key && this.networkClient != null && this.networkClient.isConnected()) {
                            this.player.releaseAllKeys();
                            this.setScreen(new ChatScreen());
                        }
                    }

                    for(i1 = 0; i1 < 9; ++i1) {
                        if(Keyboard.getEventKey() == i1 + Keyboard.KEY_1) {
                            this.player.inventory.selected = i1;
                        }
                    }

                    if(Keyboard.getEventKey() == this.options.toggleFog.key) {
                        this.options.setOption(4, !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ? 1 : -1);
                    }
                }
            }

            boolean touchMode = PointerInputAbstraction.isTouchMode();
            boolean miningTouch = touchMode && isMiningTouch();
            boolean useTouch = touchMode && this.player.getItemShouldUseOnTouchEagler();
            if (miningTouch && !wasMiningTouch) {
                if ((hitResult != null && hitResult.entity != null) || useTouch) {
                    this.clickMouse(1);
                } else {
                    this.clickMouse(0);
                    this.oFrames = this.frames;
                }
                wasMiningTouch = true;
            }
            wasMiningTouch = miningTouch;

            if (miningTouch && useTouch && this.rightClickDelayTimer == 0) {
                this.clickMouse(1);
            }

            if(this.screen == null) {
                if((Mouse.isButtonDown(0) || miningTouch) && (float)(this.frames - this.oFrames) >= this.timer.ticksPerSecond / 4.0F) {
                    this.clickMouse(0);
                    this.oFrames = this.frames;
                }

                if((Mouse.isButtonDown(1) || miningTouch) && (float)(this.frames - this.oFrames) >= this.timer.ticksPerSecond / 4.0F) {
                    this.clickMouse(1);
                    this.oFrames = this.frames;
                }
            }

            boolean z27 = this.screen == null && (Mouse.isButtonDown(0) || miningTouch) && !useTouch;
            if(z27 && this.hitResult != null && this.hitResult.type == 0) {
                i4 = this.hitResult.x;
                i41 = this.hitResult.y;
                int i46 = this.hitResult.z;
                this.gamemode.stopDestroyBlock(i4, i41, i46, this.hitResult.f);
            } else {
                this.gamemode.tick();
            }
	    }

        if(this.screen != null) {
            this.oFrames = this.frames + 10000;
        }

        if(this.screen != null) {
            this.screen.updateEvents();
            if(this.screen != null) {
                this.screen.tick();
            }
        }

        if(this.level != null) {
            TileRenderer tileRenderer32 = this.lighting.tileRenderer;
            this.lighting.tileRenderer.oProgress = tileRenderer32.progress;
            if(tileRenderer32.move) {
                ++tileRenderer32.rot;
                if(tileRenderer32.rot == 7) {
                    tileRenderer32.rot = 0;
                    tileRenderer32.move = false;
                }
            }

            i4 = tileRenderer32.minecraft.player.inventory.getSelected();
            Tile tile48 = null;
            if(i4 > 0) {
                tile48 = Tile.tiles[i4];
            }

            float f50 = 0.4F;
            float f53;
            if((f53 = (tile48 == tileRenderer32.tile ? 1.0F : 0.0F) - tileRenderer32.progress) < -f50) {
                f53 = -f50;
            }

            if(f53 > f50) {
                f53 = f50;
            }

            tileRenderer32.progress += f53;
            if(tileRenderer32.progress < 0.1F) {
                tileRenderer32.tile = tile48;
            }

            LevelRenderer levelRenderer25 = this.levelRenderer;
            ++this.levelRenderer.cloudTickCounter;
            this.level.tickEntities();
            if(!this.isOnlineClient()) {
                this.level.tick();
            }

            this.particleEngine.tick();
        }
    }

    public final boolean isOnlineClient() {
        return this.networkClient != null;
    }

    private void orientCamera(float a) {
        GL11.glTranslatef(0.0F, 0.0F, -0.1F);
        GL11.glRotatef(this.player.xRotO + (this.player.xRot - this.player.xRotO) * a, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(this.player.yRotO + (this.player.yRot - this.player.yRotO) * a, 0.0F, 1.0F, 0.0F);
        float x = this.player.xo + (this.player.x - this.player.xo) * a;
        float y = this.player.yo + (this.player.y - this.player.yo) * a;
        float z = this.player.zo + (this.player.z - this.player.zo) * a;
        GL11.glTranslatef(-x, -y, -z);
    }

	public void setupOrthoCamera() {
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, scaledResolution.getScaledWidth_double(),
                scaledResolution.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
	}

    private void pick(float a) {
        float f19 = this.player.xRotO + (this.player.xRot - this.player.xRotO) * a;
        float f20 = this.player.yRotO + (this.player.yRot - this.player.yRotO) * a;
        float f21 = this.player.xo + (this.player.x - this.player.xo) * a;
        float f68 = this.player.yo + (this.player.y - this.player.yo) * a;
        float f98 = this.player.zo + (this.player.z - this.player.zo) * a;
        Vec3 vec3102 = new Vec3(f21, f68, f98);
        float f84 = (float)Math.cos((double)(-f20) * Math.PI / 180.0D - Math.PI);
        float f24 = (float)Math.sin((double)(-f20) * Math.PI / 180.0D - Math.PI);
        f20 = (float)Math.cos((double)(-f19) * Math.PI / 180.0D);
        float f25 = (float)Math.sin((double)(-f19) * Math.PI / 180.0D);
        f19 = f24 * f20;
        f24 = f25;
        f84 *= f20;
        f20 = this.lighting.minecraft.gamemode.getPickRange();
        Vec3 vec3104 = vec3102.addVector(f19 * f20, f25 * f20, f84 * f20);
        this.lighting.minecraft.hitResult = this.lighting.minecraft.level.clip(vec3102, vec3104);
        vec3102 = new Vec3(f21, f68, f98);
        f68 = f20;
        if(this.lighting.minecraft.hitResult != null) {
            Vec3 vec375 = this.lighting.minecraft.hitResult.vec;
            f21 = vec3102.x - vec375.x;
            f98 = vec3102.y - vec375.y;
            f68 = vec3102.z - vec375.z;
            f68 = (float)Math.sqrt((double)(f21 * f21 + f98 * f98 + f68 * f68));
        }

        List list85 = this.lighting.minecraft.level.blockMap.getEntities(this.player, this.player.bb.expand(f19 * f68, f25 * f68, f84 * f68));

        int i94;
        for(i94 = 0; i94 < list85.size(); ++i94) {
            Entity entity95;
            if((entity95 = (Entity)list85.get(i94)).isPickable()) {
                f98 = 0.1F;
                AABB aABB103 = entity95.bb.grow(f98, f98, f98);

                for(f25 = 0.0F; f25 < f68; f25 += 0.05F) {
                    if(aABB103.contains(vec3102.addVector(f19 * f25, f24 * f25, f84 * f25))) {
                        f68 = f25;
                        this.hitResult = new HitResult(entity95);
                        break;
                    }
                }
            }
        }
    }

	public void render(float a) {
        int i81 = 0;

        while(true) {
            if(i81 >= 2) {
                GL11.glColorMask(true, true, true, false);
                break;
            }

            if(this.options.anaglyph3d) {
                if(i81 == 0) {
                    GL11.glColorMask(false, true, true, false);
                } else {
                    GL11.glColorMask(true, false, false, false);
                }
            }

            GL11.glViewport(0, 0, this.width, this.height);
            float f19 = 1.0F / (float)(4 - this.options.viewDistance);
            f19 = 1.0F - (float)Math.pow((double)f19, 0.25D);
            float f20 = (float)(this.level.skyColor >> 16 & 255) / 255.0F;
            float f21 = (float)(this.level.skyColor >> 8 & 255) / 255.0F;
            float f68 = (float)(this.level.skyColor & 255) / 255.0F;
            this.lighting.fogRed = (float)(this.level.fogColor >> 16 & 255) / 255.0F;
            this.lighting.fogGreen = (float)(this.level.fogColor >> 8 & 255) / 255.0F;
            this.lighting.fogBlue = (float)(this.level.fogColor & 255) / 255.0F;
            this.lighting.fogRed += (f20 - this.lighting.fogRed) * f19;
            this.lighting.fogGreen += (f21 - this.lighting.fogGreen) * f19;
            this.lighting.fogBlue += (f68 - this.lighting.fogBlue) * f19;
            this.lighting.fogRed *= this.lighting.fogColorMultiplier;
            this.lighting.fogGreen *= this.lighting.fogColorMultiplier;
            this.lighting.fogBlue *= this.lighting.fogColorMultiplier;
            Tile tile71;
            if((tile71 = Tile.tiles[this.level.getTile((int)this.player.x, (int)(this.player.y + 0.12F), (int)this.player.z)]) != null && tile71.getLiquidType() != Liquid.none) {
                Liquid liquid17;
                if((liquid17 = tile71.getLiquidType()) == Liquid.water) {
                    this.lighting.fogRed = 0.02F;
                    this.lighting.fogGreen = 0.02F;
                    this.lighting.fogBlue = 0.2F;
                } else if(liquid17 == Liquid.lava) {
                    this.lighting.fogRed = 0.6F;
                    this.lighting.fogGreen = 0.1F;
                    this.lighting.fogBlue = 0.0F;
                }
            }

            float f24;
            float f84;
            float f101;
            if(this.lighting.minecraft.options.anaglyph3d) {
                f101 = (this.lighting.fogRed * 30.0F + this.lighting.fogGreen * 59.0F + this.lighting.fogBlue * 11.0F) / 100.0F;
                f84 = (this.lighting.fogRed * 30.0F + this.lighting.fogGreen * 70.0F) / 100.0F;
                f24 = (this.lighting.fogRed * 30.0F + this.lighting.fogBlue * 70.0F) / 100.0F;
                this.lighting.fogRed = f101;
                this.lighting.fogGreen = f84;
                this.lighting.fogBlue = f24;
            }

            GL11.glClearColor(this.lighting.fogRed, this.lighting.fogGreen, this.lighting.fogBlue, 0.0F);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            this.lighting.fogColorMultiplier = 1.0F;
            GL11.glEnable(GL11.GL_CULL_FACE);
            this.lighting.renderDistance = (float)(512 >> (this.lighting.minecraft.options.viewDistance << 1));
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            f19 = 0.07F;
            if(this.lighting.minecraft.options.anaglyph3d) {
                GL11.glTranslatef((float)(-((i81 << 1) - 1)) * f19, 0.0F, 0.0F);
            }

            Player player70 = this.lighting.minecraft.player;
            float f98 = 70.0F;
            if(player70.health <= 0) {
                f101 = (float)player70.deathTime + a;
                f98 /= (1.0F - 500.0F / (f101 + 500.0F)) * 2.0F + 1.0F;
            }

            GLU.gluPerspective(f98, (float)this.lighting.minecraft.width / (float)this.lighting.minecraft.height, 0.05F, this.lighting.renderDistance);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            if(this.lighting.minecraft.options.anaglyph3d) {
                GL11.glTranslatef((float)((i81 << 1) - 1) * 0.1F, 0.0F, 0.0F);
            }

            this.lighting.renderHurtFrames(a);
            if(this.lighting.minecraft.options.bobView) {
                this.lighting.cameraBob(a);
            }

            this.orientCamera(a);
            this.pick(a);

            Frustum frustum22 = Frustum.calculateFrustum();
            Frustum frustum23 = frustum22;
            LevelRenderer levelRenderer18 = this.levelRenderer;

            for(int i5 = 0; i5 < levelRenderer18.chunks.length; ++i5) {
                levelRenderer18.chunks[i5].isInFrustum(frustum23);
            }

            levelRenderer18 = this.levelRenderer;
            try {
                Collections.sort(this.levelRenderer.allDirtyChunks, new DirtyChunkSorter(this.player));
            } catch (IllegalArgumentException e) {
            }
            int i105 = this.levelRenderer.allDirtyChunks.size() - 1;
            int i108;
            if((i108 = this.levelRenderer.allDirtyChunks.size()) > 4) {
                i108 = 4;
            }

            int i109;
            for(i109 = 0; i109 < i108; ++i109) {
                ((Chunk)this.levelRenderer.allDirtyChunks.remove(i105 - i109)).rebuild(false);
            }

            this.lighting.setupFog();
            GL11.glEnable(GL11.GL_FOG);
            this.levelRenderer.render(this.player, 0);
            if(this.level.isSolid(this.player.x, this.player.y, this.player.z, 0.1F)) {
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

            this.lighting.toggleLight(true);
            this.levelRenderer.level.blockMap.render(frustum23, this.levelRenderer.textures, a);
            this.lighting.toggleLight(false);
            this.lighting.setupFog();
            this.particleEngine.render(this.player, a);
            GL11.glCallList(this.levelRenderer.surroundLists);
            GL11.glDisable(GL11.GL_LIGHTING);
            this.lighting.setupFog();
            this.levelRenderer.renderClouds(a);
            this.lighting.setupFog();
            GL11.glEnable(GL11.GL_LIGHTING);
            if(this.hitResult != null) {
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                this.levelRenderer.renderHit(this.hitResult, 0, this.player.inventory.getSelected());
                HitResult hitResult80 = this.hitResult;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
                GL11.glLineWidth(2.0F);
                GL11.glDepthMask(false);
                float f92 = 0.002F;
                (new AABB((float)hitResult80.x, (float)hitResult80.y, (float)hitResult80.z, (float)(hitResult80.x + 1), (float)(hitResult80.y + 1), (float)(hitResult80.z + 1))).grow(f92, f92, f92).render();
                GL11.glDepthMask(true);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_LIGHTING);
            }

            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.lighting.setupFog();
            GL11.glCallList(this.levelRenderer.surroundLists + 1);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColorMask(false, false, false, false);
            int i20 = this.levelRenderer.render(this.player, 1);
            GL11.glColorMask(true, true, true, true);
            if(this.options.anaglyph3d) {
                if(i81 == 0) {
                    GL11.glColorMask(false, true, true, false);
                } else {
                    GL11.glColorMask(true, false, false, false);
                }
            }

            if(i20 > 0) {
                levelRenderer18 = this.levelRenderer;
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, levelRenderer18.textures.loadTexture("/terrain.png"));
                GL11.glCallLists(levelRenderer18.ib);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            }

            GL11.glDepthMask(true);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_FOG);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            f84 = a;
            GL11.glLoadIdentity();
            if(this.options.anaglyph3d) {
                GL11.glTranslatef((float)((i81 << 1) - 1) * 0.1F, 0.0F, 0.0F);
            }

            this.lighting.renderHurtFrames(a);
            if(this.options.bobView) {
                this.lighting.cameraBob(a);
            }

            TileRenderer tileRenderer97 = this.lighting.tileRenderer;
            f21 = this.lighting.tileRenderer.oProgress + (tileRenderer97.progress - tileRenderer97.oProgress) * a;
            player70 = tileRenderer97.minecraft.player;
            GL11.glPushMatrix();
            GL11.glRotatef(player70.xRotO + (player70.xRot - player70.xRotO) * a, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(player70.yRotO + (player70.yRot - player70.yRotO) * a, 0.0F, 1.0F, 0.0F);
            tileRenderer97.minecraft.lighting.toggleLight(true);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            f98 = 0.8F;
            if(tileRenderer97.move) {
                f84 = (float)Math.sin((double)(f101 = ((float)tileRenderer97.rot + a) / 7.0F) * Math.PI);
                GL11.glTranslatef(-((float)Math.sin(Math.sqrt((double)f101) * Math.PI)) * 0.4F, (float)Math.sin(Math.sqrt((double)f101) * Math.PI * 2.0D) * 0.2F, -f84 * 0.2F);
            }

            GL11.glTranslatef(0.7F * f98, -0.65F * f98 - (1.0F - f21) * 0.6F, -0.9F * f98);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glEnable(GL11.GL_NORMALIZE);
            if(tileRenderer97.move) {
                f84 = (float)Math.sin((double)((f101 = ((float)tileRenderer97.rot + f84) / 7.0F) * f101) * Math.PI);
                GL11.glRotatef((float)Math.sin(Math.sqrt((double)f101) * Math.PI) * 80.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-f84 * 20.0F, 1.0F, 0.0F, 0.0F);
            }

            GL11.glColor4f(f101 = tileRenderer97.minecraft.level.getBrightness((int)player70.x, (int)player70.y, (int)player70.z), f101, f101, 1.0F);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            Tesselator tesselator93 = Tesselator.instance;
            if(tileRenderer97.tile != null) {
                f24 = 0.4F;
                GL11.glScalef(0.4F, f24, f24);
                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, tileRenderer97.minecraft.textures.loadTexture("/terrain.png"));
                tileRenderer97.tile.renderGuiTile(tesselator93);
            } else {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, tileRenderer97.minecraft.textures.loadTexture("/char.png"));
                GL11.glScalef(1.0F, -1.0F, -1.0F);
                GL11.glTranslatef(0.0F, 0.2F, 0.0F);
                GL11.glRotatef(-120.0F, 0.0F, 0.0F, 1.0F);
                GL11.glScalef(1.0F, 1.0F, 1.0F);
                f24 = 0.0625F;
                Cube cube100;
                if(!(cube100 = tileRenderer97.minecraft.player.getModel().leftArm).isHidden) {
                    if(!cube100.compiled) {
                        cube100.translateTo(f24);
                    }

                    GL11.glCallList(cube100.list);
                }
            }

            GL11.glDisable(GL11.GL_NORMALIZE);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();
            tileRenderer97.minecraft.lighting.toggleLight(false);
            if(!this.options.anaglyph3d) {
                break;
            }

            ++i81;
        }
	}

	public void shootArrow() {
        this.level.addEntity(new Arrow(this, this.player, this.player.x, this.player.y, this.player.z, this.player.yRot, this.player.xRot));
	}

    public final void generateLevel(int size) {
        String string2 = this.user != null ? this.user.name : "anonymous";
        Level size1 = (new LevelGen(this.loadingScreen)).generateLevel(string2, 128 << size, 128 << size, 64);
        this.loadLegacy(size1);
    }

    public final boolean loadLevel(String string1, int i2) {
        VFile2 f = new VFile2("level.dat");
        if (!f.exists()) {
            return false;
        }
        Level level3;
        try {
            if((level3 = this.levelIo.load(new DataInputStream(
                    PlatformRuntime.newGZIPInputStream(f.getInputStream())))) != null) {
                this.loadLegacy(level3);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
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

    public final void loadLegacy(Level level) {
        this.level = level;
        if(level != null) {
            level.rendererContext = this;
        }

        if(this.levelRenderer != null) {
            LevelRenderer levelRenderer2 = this.levelRenderer;
            if(this.levelRenderer.level != null) {
                levelRenderer2.level.removeListener(levelRenderer2);
            }

            levelRenderer2.level = level;
            if(level != null) {
                level.addListener(levelRenderer2);
                levelRenderer2.compileSurroundingGround();
            }
        }

        if(this.particleEngine != null) {
            level.particleEngine = this.particleEngine;
            this.particleEngine.particles.clear();
        }

        this.player = new Player(level, new KeyboardInput(this.options));
        this.player.resetPos();
        level.player = this.player;
        System.gc();
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
