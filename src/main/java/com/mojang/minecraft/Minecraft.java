package com.mojang.minecraft;

import com.mojang.minecraft.gamemode.CreativeGameMode;
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
import com.mojang.minecraft.item.Item;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelIO;
import com.mojang.minecraft.level.levelgen.LevelGen;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.mob.Mob;
import com.mojang.minecraft.model.Cube;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.model.ModelCache;
import com.mojang.minecraft.model.Vec3;
import com.mojang.minecraft.net.Client;
import com.mojang.minecraft.net.Packet;
import com.mojang.minecraft.particle.Particle;
import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.particle.WaterDropParticle;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.player.KeyboardInput;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.renderer.Chunk;
import com.mojang.minecraft.renderer.DirtyChunkSorter;
import com.mojang.minecraft.renderer.Frustum;
import com.mojang.minecraft.renderer.FrustumCuller;
import com.mojang.minecraft.renderer.GameRenderer;
import com.mojang.minecraft.renderer.LevelRenderer;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;
import com.mojang.minecraft.renderer.TileRenderer;
import com.mojang.minecraft.renderer.texture.DynamicTexture;
import com.mojang.minecraft.renderer.texture.LavaTexture;
import com.mojang.minecraft.renderer.texture.WaterTexture;
import com.mojang.minecraft.sound.SoundEngine;
import com.mojang.minecraft.sound.SoundPlayer;

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
    public GameMode gamemode = new CreativeGameMode(this);
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
    public GameRenderer gameRenderer = new GameRenderer(this);
    public LevelIO levelIo = new LevelIO(this.loadingScreen);
    public SoundEngine soundEngine = new SoundEngine();
    private int frames = 0;
    private int clickCounter = 0;
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
    public boolean raining;
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
        this.raining = false;
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

        Display.setTitle("Minecraft 0.29_02");

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
        this.font = new Font(this.options, "/default.png", this.textures);
        IntBuffer imgData = BufferUtils.createIntBuffer(256);
        imgData.clear().limit(256);
        this.levelRenderer = new LevelRenderer(this, this.textures);
        Item.initModels();
        Mob.modelCache = new ModelCache();
        GL11.glViewport(0, 0, this.width, this.height);
        this.mouseGrabSupported = Mouse.isMouseGrabSupported();
        touchOverlayRenderer = new TouchOverlayRenderer();
        scaledResolution = new ScaledResolution(this);
        PointerInputAbstraction.init(this);
        if(this.server != null && this.user != null) {
            Level level63;
            (level63 = new Level()).setData(8, 8, 8, new byte[512]);
            this.loadLegacy(level63);
        } else {
            try {
                this.loadLevel(this.loadMapUser, this.loadMapId);
            } catch (Exception exception20) {
                exception20.printStackTrace();
            }

            if(this.level == null) {
                this.generateLevel(1);
            }
        }

        this.particleEngine = new ParticleEngine(this.level, this.textures);

        this.soundEngine.registerSounds(this.options);
        this.soundPlayer = new SoundPlayer(this.options);

        checkGlError("Post startup");
        this.gui = new Gui(this, this.width, this.height);
        (new PlayerTextureLoader(this)).start();
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
                            GL11.glEnable(GL11.GL_TEXTURE_2D);
                            if(!this.hideScreen) {
                                this.gamemode.render(this.timer.alpha);
                                this.soundPlayer.setListener(this.player, this.timer.alpha);
                                float f33 = this.timer.alpha;
                                if(this.gameRenderer.displayActive && !Display.isActive()) {
                                    this.gameRenderer.minecraft.pauseScreen();
                                }

                                this.gameRenderer.displayActive = Display.isActive();
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
                                        this.gameRenderer.render();
                                    }

                                    if(this.screen != null) {
                                        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                                        this.screen.render(i60, i62);
                                    }
                                }

                                GL11.glEnable(GL11.GL_TEXTURE_2D);
                                this.setupOrthoCamera();
                                touchOverlayRenderer.render(width, height, scaledResolution);
                                GL11.disableBlend();
                                GL11.glDisable(GL11.GL_TEXTURE_2D);
                            }
                        }

                        Thread.yield();
                        this.updateDisplay();

                        if(this.options.limitFramerate) {
                            Thread.sleep(5L);
                        }

                        checkGlError("Post render");
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
        if(id != 0 || this.clickCounter <= 0) {
            TileRenderer tileRenderer6;
            if(id == 0) {
                tileRenderer6 = this.gameRenderer.tileRenderer;
                this.gameRenderer.tileRenderer.rot = -1;
                tileRenderer6.move = true;
            } else {
                this.rightClickDelayTimer = 4;
            }

            int i2;
            if(id == 1 && (i2 = this.player.inventory.getSelected()) > 0 && this.gamemode.removeResource(this.player, i2)) {
                tileRenderer6 = this.gameRenderer.tileRenderer;
                this.gameRenderer.tileRenderer.progress = 0.0F;
            } else if(this.hitResult == null) {
                if(id == 0 && !(this.gamemode instanceof CreativeGameMode)) {
                    this.clickCounter = 10;
                }

            } else {
                if(this.hitResult.type == 1) {
                    if(id == 0) {
                        this.hitResult.entity.hurt(this.player, 4);
                        return;
                    }
                } else if(this.hitResult.type == 0) {
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
                            tileRenderer6 = this.gameRenderer.tileRenderer;
                            this.gameRenderer.tileRenderer.progress = 0.0F;
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

        this.gamemode.tick();
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

        if(this.screen == null && this.player != null && this.player.health <= 0) {
            this.setScreen((Screen)null);
        }

        if((this.screen == null || this.screen.allowUserInput) && (this.networkClient == null
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

                            if(i17 == Tile.slabFull.id) {
                                i17 = Tile.slabHalf.id;
                            }

                            this.player.inventory.grabTexture(i17, this.gamemode instanceof CreativeGameMode);
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

            if(this.clickCounter > 0) {
                --this.clickCounter;
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

                        if(this.gamemode instanceof CreativeGameMode) {
                            if(Keyboard.getEventKey() == this.options.load.key) {
                                this.player.resetPos();
                            }

                            if(Keyboard.getEventKey() == this.options.save.key) {
                                this.level.setSpawnPos((int)this.player.x, (int)this.player.y, (int)this.player.z, this.player.yRot);
                                this.player.resetPos();
                            }
                        }

                        if(Keyboard.getEventKey() == Keyboard.KEY_F5) {
                            this.raining = !this.raining;
                        }

                        if(Keyboard.getEventKey() == Keyboard.KEY_TAB && this.gamemode instanceof SurvivalGameMode) {
                            this.shootArrow();
                        }

                        if(Keyboard.getEventKey() == this.options.build.key) {
                            this.gamemode.handleOpenInventory();
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

            boolean z25 = this.screen == null && (Mouse.isButtonDown(0) || miningTouch) && !useTouch;
            boolean z38 = false;
            if(!this.gamemode.mode && this.clickCounter <= 0) {
                if(z25 && this.hitResult != null && this.hitResult.type == 0) {
                    i4 = this.hitResult.x;
                    int i39 = this.hitResult.y;
                    int i45 = this.hitResult.z;
                    this.gamemode.continueDestroyBlock(i4, i39, i45, this.hitResult.f);
                } else {
                    this.gamemode.stopDestroyBlock();
                }
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
            TileRenderer tileRenderer32 = this.gameRenderer.tileRenderer;
            ++this.gameRenderer.rainTicks;
            this.gameRenderer.tileRenderer.oProgress = tileRenderer32.progress;
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

            if(this.raining) {
                i41 = (int)this.player.x;
                int i45 = (int)this.player.y;
                int i46 = (int)this.player.z;

                for(int i8 = 0; i8 < 50; ++i8) {
                    int i53 = i41 + this.gameRenderer.random.nextInt(9) - 4;
                    int i55 = i46 + this.gameRenderer.random.nextInt(9) - 4;
                    int i60;
                    if((i60 = this.level.getHighestTile(i53, i55)) <= i45 + 4 && i60 >= i45 - 4) {
                        float f61 = this.gameRenderer.random.nextFloat();
                        float f63 = this.gameRenderer.random.nextFloat();
                        this.particleEngine.addParticle(new WaterDropParticle(this.level, (float)i53 + f61, (float)i60 + 0.1F, (float)i55 + f63));
                    }
                }
            }

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
        float f24 = (this.player = this.player).xRotO + (this.player.xRot - this.player.xRotO) * a;
        float f25 = this.player.yRotO + (this.player.yRot - this.player.yRotO) * a;
        Vec3 vec326 = this.gameRenderer.getPlayerRotVec(a);
        float f27 = (float)Math.cos(-f25 * 0.017453292F - (float)Math.PI);
        float f78 = (float)Math.sin(-f25 * 0.017453292F - (float)Math.PI);
        float f85 = (float)Math.cos(-f24 * 0.017453292F);
        float f17 = (float)Math.sin(-f24 * 0.017453292F);
        float f18 = f78 * f85;
        float f72 = f27 * f85;
        float f19 = this.gamemode.getPickRange();
        Vec3 vec380 = vec326.add(f18 * f19, f17 * f19, f72 * f19);
        this.hitResult = this.level.clip(vec326, vec380);
        f85 = f19;
        if(this.hitResult != null) {
            f85 = this.hitResult.vec.distanceTo(vec326);
        }

        vec326 = this.gameRenderer.getPlayerRotVec(a);
        if(this.gamemode instanceof CreativeGameMode) {
            f19 = 32.0F;
        } else {
            f19 = f85;
        }

        vec380 = vec326.add(f18 * f19, f17 * f19, f72 * f19);
        this.gameRenderer.entity = null;
        List list5 = this.level.blockMap.getEntities(this.player, this.player.bb.expand(f18 * f19, f17 * f19, f72 * f19));
        float f6 = 0.0F;

        for(int i61 = 0; i61 < list5.size(); ++i61) {
            Entity entity74;
            if((entity74 = (Entity)list5.get(i61)).isPickable()) {
                f85 = 0.1F;
                HitResult hitResult90;
                if((hitResult90 = entity74.bb.grow(f85, f85, f85).clip(vec326, vec380)) != null && ((f85 = vec326.distanceTo(hitResult90.vec)) < f6 || f6 == 0.0F)) {
                    this.gameRenderer.entity = entity74;
                    f6 = f85;
                }
            }
        }

        if(this.gameRenderer.entity != null && !(this.gamemode instanceof CreativeGameMode)) {
            this.hitResult = new HitResult(this.gameRenderer.entity);
        }
    }

	public void render(float a) {
        this.pick(a);

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
            this.gameRenderer.fogRed = (float)(this.level.fogColor >> 16 & 255) / 255.0F;
            this.gameRenderer.fogGreen = (float)(this.level.fogColor >> 8 & 255) / 255.0F;
            this.gameRenderer.fogBlue = (float)(this.level.fogColor & 255) / 255.0F;
            this.gameRenderer.fogRed += (f20 - this.gameRenderer.fogRed) * f19;
            this.gameRenderer.fogGreen += (f21 - this.gameRenderer.fogGreen) * f19;
            this.gameRenderer.fogBlue += (f68 - this.gameRenderer.fogBlue) * f19;
            this.gameRenderer.fogRed *= this.gameRenderer.fogColorMultiplier;
            this.gameRenderer.fogGreen *= this.gameRenderer.fogColorMultiplier;
            this.gameRenderer.fogBlue *= this.gameRenderer.fogColorMultiplier;
            Tile tile71;
            if((tile71 = Tile.tiles[this.level.getTile((int)this.player.x, (int)(this.player.y + 0.12F), (int)this.player.z)]) != null && tile71.getLiquidType() != Liquid.none) {
                Liquid liquid17;
                if((liquid17 = tile71.getLiquidType()) == Liquid.water) {
                    this.gameRenderer.fogRed = 0.02F;
                    this.gameRenderer.fogGreen = 0.02F;
                    this.gameRenderer.fogBlue = 0.2F;
                } else if(liquid17 == Liquid.lava) {
                    this.gameRenderer.fogRed = 0.6F;
                    this.gameRenderer.fogGreen = 0.1F;
                    this.gameRenderer.fogBlue = 0.0F;
                }
            }

            float f24;
            float f84;
            float f101;
            if(this.gameRenderer.minecraft.options.anaglyph3d) {
                f101 = (this.gameRenderer.fogRed * 30.0F + this.gameRenderer.fogGreen * 59.0F + this.gameRenderer.fogBlue * 11.0F) / 100.0F;
                f84 = (this.gameRenderer.fogRed * 30.0F + this.gameRenderer.fogGreen * 70.0F) / 100.0F;
                f24 = (this.gameRenderer.fogRed * 30.0F + this.gameRenderer.fogBlue * 70.0F) / 100.0F;
                this.gameRenderer.fogRed = f101;
                this.gameRenderer.fogGreen = f84;
                this.gameRenderer.fogBlue = f24;
            }

            GL11.glClearColor(this.gameRenderer.fogRed, this.gameRenderer.fogGreen, this.gameRenderer.fogBlue, 0.0F);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            this.gameRenderer.fogColorMultiplier = 1.0F;
            GL11.glEnable(GL11.GL_CULL_FACE);
            this.gameRenderer.renderDistance = (float)(512 >> (this.gameRenderer.minecraft.options.viewDistance << 1));
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            f19 = 0.07F;
            if(this.gameRenderer.minecraft.options.anaglyph3d) {
                GL11.glTranslatef((float)(-((i81 << 1) - 1)) * f19, 0.0F, 0.0F);
            }

            Player player70 = this.gameRenderer.minecraft.player;
            float f98 = 70.0F;
            if(player70.health <= 0) {
                f101 = (float)player70.deathTime + a;
                f98 /= (1.0F - 500.0F / (f101 + 500.0F)) * 2.0F + 1.0F;
            }

            GLU.gluPerspective(f98, (float)this.gameRenderer.minecraft.width / (float)this.gameRenderer.minecraft.height, 0.05F, this.gameRenderer.renderDistance);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            if(this.gameRenderer.minecraft.options.anaglyph3d) {
                GL11.glTranslatef((float)((i81 << 1) - 1) * 0.1F, 0.0F, 0.0F);
            }

            this.gameRenderer.renderHurtFrames(a);
            if(this.gameRenderer.minecraft.options.bobView) {
                this.gameRenderer.cameraBob(a);
            }

            this.orientCamera(a);

            Frustum frustum22 = FrustumCuller.calculateFrustum();
            Frustum frustum23 = frustum22;
            LevelRenderer levelRenderer18 = this.levelRenderer;

            for(int i5 = 0; i5 < levelRenderer18.chunks.length; ++i5) {
                levelRenderer18.chunks[i5].isInFrustum(frustum23);
            }

            Collections.sort(this.levelRenderer.allDirtyChunks, new DirtyChunkSorter(this.player));
            int i105 = this.levelRenderer.allDirtyChunks.size() - 1;
            int i108;
            if((i108 = this.levelRenderer.allDirtyChunks.size()) > 3) {
                i108 = 3;
            }

            int i109;
            for(i109 = 0; i109 < i108; ++i109) {
                Chunk chunk112;
                (chunk112 = (Chunk)this.levelRenderer.allDirtyChunks.remove(i105 - i109)).rebuild();
                chunk112.dirty = false;
            }

            this.gameRenderer.setupFog();
            GL11.glEnable(GL11.GL_FOG);
            this.levelRenderer.render(this.player, 0);
            int i59;
            int i90;
            int i91;
            int i92;
            int i94;
            int i96;
            Tesselator tesselator114;
            int i117;
            if(this.level.isSolid(this.player.x, this.player.y, this.player.z, 0.1F)) {
                i59 = (int)this.player.x;
                i90 = (int)this.player.y;
                i91 = (int)this.player.z;

                for(i92 = i59 - 1; i92 <= i59 + 1; ++i92) {
                    for(i94 = i90 - 1; i94 <= i90 + 1; ++i94) {
                        for(i96 = i91 - 1; i96 <= i91 + 1; ++i96) {
                            i108 = i96;
                            i105 = i94;
                            int i101 = i92;
                            if((i109 = this.levelRenderer.level.getTile(i92, i94, i96)) != 0 && Tile.tiles[i109].isSolid()) {
                                GL11.glColor4f(0.2F, 0.2F, 0.2F, 1.0F);
                                GL11.glDepthFunc(GL11.GL_LESS);
                                tesselator114 = Tesselator.instance;
                                Tesselator.instance.begin(DefaultVertexFormats.POSITION_TEX);

                                for(i117 = 0; i117 < 6; ++i117) {
                                    Tile.tiles[i109].renderFace(tesselator114, i101, i105, i108, i117);
                                }

                                tesselator114.end();
                                GL11.glCullFace(GL11.GL_FRONT);
                                tesselator114.begin(DefaultVertexFormats.POSITION_TEX);

                                for(i117 = 0; i117 < 6; ++i117) {
                                    Tile.tiles[i109].renderFace(tesselator114, i101, i105, i108, i117);
                                }

                                tesselator114.end();
                                GL11.glCullFace(GL11.GL_BACK);
                                GL11.glDepthFunc(GL11.GL_LEQUAL);
                            }
                        }
                    }
                }
            }

            this.gameRenderer.toggleLight(true);
            Vec3 vec3103 = this.gameRenderer.getPlayerRotVec(a);
            this.levelRenderer.level.blockMap.render(vec3103, frustum22, this.levelRenderer.textures, a);
            this.gameRenderer.toggleLight(false);
            this.gameRenderer.setupFog();
            float f107 = a;
            ParticleEngine particleEngine98 = this.particleEngine;
            f24 = (float)-Math.cos(this.player.yRot * (float)Math.PI / 180.0F);
            float f25;
            float f26 = (float)(-(f25 = (float)-Math.sin(this.player.yRot * (float)Math.PI / 180.0F)) * Math.sin(this.player.xRot * (float)Math.PI / 180.0F));
            float f27 = (float)(f24 * Math.sin(this.player.xRot * (float)Math.PI / 180.0F));
            float f77 = (float)Math.cos(this.player.xRot * (float)Math.PI / 180.0F);

            for(i90 = 0; i90 < 2; ++i90) {
                if(particleEngine98.particles[i90].size() != 0) {
                    i91 = 0;
                    if(i90 == 0) {
                        i91 = particleEngine98.textures.loadTexture("/particles.png");
                    }

                    if(i90 == 1) {
                        i91 = particleEngine98.textures.loadTexture("/terrain.png");
                    }

                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, i91);
                    Tesselator tesselator95 = Tesselator.instance;
                    Tesselator.instance.begin(DefaultVertexFormats.POSITION_TEX_COLOR);

                    for(i94 = 0; i94 < particleEngine98.particles[i90].size(); ++i94) {
                        ((Particle)particleEngine98.particles[i90].get(i94)).render(tesselator95, f107, f24, f77, f25, f26, f27);
                    }

                    tesselator95.end();
                }
            }

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.levelRenderer.textures.loadTexture("/rock.png"));
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glCallList(this.levelRenderer.surroundLists);
            this.gameRenderer.setupFog();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.levelRenderer.textures.loadTexture("/clouds.png"));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            f107 = (float)(this.levelRenderer.level.cloudColor >> 16 & 255) / 255.0F;
            f24 = (float)(this.levelRenderer.level.cloudColor >> 8 & 255) / 255.0F;
            f25 = (float)(this.levelRenderer.level.cloudColor & 255) / 255.0F;
            if(this.options.anaglyph3d) {
                f26 = (f107 * 30.0F + f24 * 59.0F + f25 * 11.0F) / 100.0F;
                f27 = (f107 * 30.0F + f24 * 70.0F) / 100.0F;
                f77 = (f107 * 30.0F + f25 * 70.0F) / 100.0F;
                f107 = f26;
                f24 = f27;
                f25 = f77;
            }

            tesselator114 = Tesselator.instance;
            float f82 = 0.0F;
            float f17 = 4.8828125E-4F;
            f82 = (float)(this.levelRenderer.level.depth + 2);
            float f18 = ((float)this.levelRenderer.cloudTickCounter + a) * f17 * 0.03F;
            f19 = 0.0F;
            tesselator114.begin(DefaultVertexFormats.POSITION_TEX_COLOR);
            tesselator114.color(f107, f24, f25);

            int i116;
            for(i96 = -2048; i96 < this.levelRenderer.level.width + 2048; i96 += 512) {
                for(i116 = -2048; i116 < this.levelRenderer.level.height + 2048; i116 += 512) {
                    tesselator114.vertexUV((float)i96, f82, (float)(i116 + 512), (float)i96 * f17 + f18, (float)(i116 + 512) * f17);
                    tesselator114.vertexUV((float)(i96 + 512), f82, (float)(i116 + 512), (float)(i96 + 512) * f17 + f18, (float)(i116 + 512) * f17);
                    tesselator114.vertexUV((float)(i96 + 512), f82, (float)i116, (float)(i96 + 512) * f17 + f18, (float)i116 * f17);
                    tesselator114.vertexUV((float)i96, f82, (float)i116, (float)i96 * f17 + f18, (float)i116 * f17);
                    tesselator114.vertexUV((float)i96, f82, (float)i116, (float)i96 * f17 + f18, (float)i116 * f17);
                    tesselator114.vertexUV((float)(i96 + 512), f82, (float)i116, (float)(i96 + 512) * f17 + f18, (float)i116 * f17);
                    tesselator114.vertexUV((float)(i96 + 512), f82, (float)(i116 + 512), (float)(i96 + 512) * f17 + f18, (float)(i116 + 512) * f17);
                    tesselator114.vertexUV((float)i96, f82, (float)(i116 + 512), (float)i96 * f17 + f18, (float)(i116 + 512) * f17);
                }
            }

            tesselator114.end();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tesselator114.begin(DefaultVertexFormats.POSITION_COLOR);
            f18 = (float)(this.levelRenderer.level.skyColor >> 16 & 255) / 255.0F;
            f19 = (float)(this.levelRenderer.level.skyColor >> 8 & 255) / 255.0F;
            f20 = (float)(this.levelRenderer.level.skyColor & 255) / 255.0F;
            if(this.levelRenderer.minecraft.options.anaglyph3d) {
                float f28 = (f18 * 30.0F + f19 * 59.0F + f20 * 11.0F) / 100.0F;
                f77 = (f18 * 30.0F + f19 * 70.0F) / 100.0F;
                f82 = (f18 * 30.0F + f20 * 70.0F) / 100.0F;
                f18 = f28;
                f19 = f77;
                f20 = f82;
            }

            tesselator114.color(f18, f19, f20);
            f82 = (float)(this.levelRenderer.level.depth + 10);

            for(i116 = -2048; i116 < this.levelRenderer.level.width + 2048; i116 += 512) {
                for(int i74 = -2048; i74 < this.levelRenderer.level.height + 2048; i74 += 512) {
                    tesselator114.vertex((float)i116, f82, (float)i74);
                    tesselator114.vertex((float)(i116 + 512), f82, (float)i74);
                    tesselator114.vertex((float)(i116 + 512), f82, (float)(i74 + 512));
                    tesselator114.vertex((float)i116, f82, (float)(i74 + 512));
                }
            }

            tesselator114.end();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            this.gameRenderer.setupFog();
            int i118;
            if(this.hitResult != null) {
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                HitResult hitResult10001 = this.hitResult;
                i108 = this.player.inventory.getSelected();
                boolean z111 = false;
                HitResult hitResult104 = hitResult10001;
                Tesselator tesselator115 = Tesselator.instance;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, (float)((Math.sin((float)EagRuntime.currentTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F));
                if(this.levelRenderer.hurtTime > 0.0F) {
                    GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
                    i118 = this.levelRenderer.textures.loadTexture("/terrain.png");
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, i118);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
                    GL11.glPushMatrix();
                    Tile tile10000 = (i117 = this.levelRenderer.level.getTile(hitResult104.x, hitResult104.y, hitResult104.z)) > 0 ? Tile.tiles[i117] : null;
                    Tile tile75 = tile10000;
                    f82 = (tile10000.xx0 + tile75.xx1) / 2.0F;
                    f17 = (tile75.yy0 + tile75.yy1) / 2.0F;
                    f18 = (tile75.zz0 + tile75.zz1) / 2.0F;
                    GL11.glTranslatef((float)hitResult104.x + f82, (float)hitResult104.y + f17, (float)hitResult104.z + f18);
                    f19 = 1.01F;
                    GL11.glScalef(1.01F, f19, f19);
                    GL11.glTranslatef(-((float)hitResult104.x + f82), -((float)hitResult104.y + f17), -((float)hitResult104.z + f18));
                    tesselator115.begin(DefaultVertexFormats.POSITION_TEX);
                    tesselator115.noColor();
                    GL11.glDepthMask(false);
                    if(tile75 == null) {
                        tile75 = Tile.rock;
                    }

                    for(i96 = 0; i96 < 6; ++i96) {
                        tile75.renderFaceNoTexture(tesselator115, hitResult104.x, hitResult104.y, hitResult104.z, i96, 240 + (int)(this.levelRenderer.hurtTime * 10.0F));
                    }

                    tesselator115.end();
                    GL11.glDepthMask(true);
                    GL11.glPopMatrix();
                }

                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                hitResult10001 = this.hitResult;
                this.player.inventory.getSelected();
                z111 = false;
                hitResult104 = hitResult10001;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
                GL11.glLineWidth(2.0F);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDepthMask(false);
                f24 = 0.002F;
                if((i109 = this.levelRenderer.level.getTile(hitResult104.x, hitResult104.y, hitResult104.z)) > 0) {
                    AABB aABB120 = Tile.tiles[i109].getAABB(hitResult104.x, hitResult104.y, hitResult104.z).grow(f24, f24, f24);
                    GL11.glBegin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
                    GL11.glVertex3f(aABB120.x0, aABB120.y0, aABB120.z0);
                    GL11.glVertex3f(aABB120.x1, aABB120.y0, aABB120.z0);
                    GL11.glVertex3f(aABB120.x1, aABB120.y0, aABB120.z1);
                    GL11.glVertex3f(aABB120.x0, aABB120.y0, aABB120.z1);
                    GL11.glVertex3f(aABB120.x0, aABB120.y0, aABB120.z0);
                    GL11.glEnd();
                    GL11.glBegin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
                    GL11.glVertex3f(aABB120.x0, aABB120.y1, aABB120.z0);
                    GL11.glVertex3f(aABB120.x1, aABB120.y1, aABB120.z0);
                    GL11.glVertex3f(aABB120.x1, aABB120.y1, aABB120.z1);
                    GL11.glVertex3f(aABB120.x0, aABB120.y1, aABB120.z1);
                    GL11.glVertex3f(aABB120.x0, aABB120.y1, aABB120.z0);
                    GL11.glEnd();
                    GL11.glBegin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
                    GL11.glVertex3f(aABB120.x0, aABB120.y0, aABB120.z0);
                    GL11.glVertex3f(aABB120.x0, aABB120.y1, aABB120.z0);
                    GL11.glVertex3f(aABB120.x1, aABB120.y0, aABB120.z0);
                    GL11.glVertex3f(aABB120.x1, aABB120.y1, aABB120.z0);
                    GL11.glVertex3f(aABB120.x1, aABB120.y0, aABB120.z1);
                    GL11.glVertex3f(aABB120.x1, aABB120.y1, aABB120.z1);
                    GL11.glVertex3f(aABB120.x0, aABB120.y0, aABB120.z1);
                    GL11.glVertex3f(aABB120.x0, aABB120.y1, aABB120.z1);
                    GL11.glEnd();
                }

                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            }

            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.gameRenderer.setupFog();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.levelRenderer.textures.loadTexture("/water.png"));
            GL11.glCallList(this.levelRenderer.surroundLists + 1);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColorMask(false, false, false, false);
            i59 = this.levelRenderer.render(this.player, 1);
            GL11.glColorMask(true, true, true, true);
            if(this.options.anaglyph3d) {
                if(i81 == 0) {
                    GL11.glColorMask(false, true, true, false);
                } else {
                    GL11.glColorMask(true, false, false, false);
                }
            }

            if(i59 > 0) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.levelRenderer.textures.loadTexture("/terrain.png"));
                GL11.glCallLists(this.levelRenderer.ib);
            }

            GL11.glDepthMask(true);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_FOG);
            if(this.raining) {
                float f106 = a;
                i109 = (int)this.player.x;
                i118 = (int)this.player.y;
                i117 = (int)this.player.z;
                Tesselator tesselator93 = Tesselator.instance;
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/rain.png"));
                i91 = i109 - 5;

                while(true) {
                    if(i91 > i109 + 5) {
                        GL11.glEnable(GL11.GL_CULL_FACE);
                        GL11.glDisable(GL11.GL_BLEND);
                        break;
                    }

                    for(i92 = i117 - 5; i92 <= i117 + 5; ++i92) {
                        i94 = this.level.getHighestTile(i91, i92);
                        i96 = i118 - 5;
                        i116 = i118 + 5;
                        if(i96 < i94) {
                            i96 = i94;
                        }

                        if(i116 < i94) {
                            i116 = i94;
                        }

                        if(i96 != i116) {
                            f82 = ((float)((this.gameRenderer.rainTicks + i91 * 3121 + i92 * 418711) % 32) + f106) / 32.0F;
                            float f122 = (float)i91 + 0.5F - this.player.x;
                            float f123 = (float)i92 + 0.5F - this.player.z;
                            float f124 = (float)(Math.sqrt(f122 * f122 + f123 * f123) / (float)5);
                            GL11.glColor4f(1.0F, 1.0F, 1.0F, (1.0F - f124 * f124) * 0.7F);
                            tesselator93.begin(DefaultVertexFormats.POSITION_TEX);
                            tesselator93.vertexUV((float)i91, (float)i96, (float)i92, 0.0F, (float)i96 * 2.0F / 8.0F + f82 * 2.0F);
                            tesselator93.vertexUV((float)(i91 + 1), (float)i96, (float)(i92 + 1), 2.0F, (float)i96 * 2.0F / 8.0F + f82 * 2.0F);
                            tesselator93.vertexUV((float)(i91 + 1), (float)i116, (float)(i92 + 1), 2.0F, (float)i116 * 2.0F / 8.0F + f82 * 2.0F);
                            tesselator93.vertexUV((float)i91, (float)i116, (float)i92, 0.0F, (float)i116 * 2.0F / 8.0F + f82 * 2.0F);
                            tesselator93.vertexUV((float)i91, (float)i96, (float)(i92 + 1), 0.0F, (float)i96 * 2.0F / 8.0F + f82 * 2.0F);
                            tesselator93.vertexUV((float)(i91 + 1), (float)i96, (float)i92, 2.0F, (float)i96 * 2.0F / 8.0F + f82 * 2.0F);
                            tesselator93.vertexUV((float)(i91 + 1), (float)i116, (float)i92, 2.0F, (float)i116 * 2.0F / 8.0F + f82 * 2.0F);
                            tesselator93.vertexUV((float)i91, (float)i116, (float)(i92 + 1), 0.0F, (float)i116 * 2.0F / 8.0F + f82 * 2.0F);
                            tesselator93.end();
                        }
                    }

                    ++i91;
                }
            }

            if(this.gameRenderer.entity != null) {
                this.gameRenderer.entity.renderHover(this.textures, a);
            }

            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glLoadIdentity();
            if(this.options.anaglyph3d) {
                GL11.glTranslatef((float)((i81 << 1) - 1) * 0.1F, 0.0F, 0.0F);
            }

            this.gameRenderer.renderHurtFrames(a);
            if(this.options.bobView) {
                this.gameRenderer.cameraBob(a);
            }

            TileRenderer tileRenderer97 = this.gameRenderer.tileRenderer;
            f21 = this.gameRenderer.tileRenderer.oProgress + (tileRenderer97.progress - tileRenderer97.oProgress) * a;
            player70 = tileRenderer97.minecraft.player;
            GL11.glPushMatrix();
            GL11.glRotatef(player70.xRotO + (player70.xRot - player70.xRotO) * a, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(player70.yRotO + (player70.yRot - player70.yRotO) * a, 0.0F, 1.0F, 0.0F);
            this.gameRenderer.toggleLight(true);
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
                f84 = (float)Math.sin((double)((f101 = ((float)tileRenderer97.rot + a) / 7.0F) * f101) * Math.PI);
                GL11.glRotatef((float)Math.sin(Math.sqrt((double)f101) * Math.PI) * 80.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-f84 * 20.0F, 1.0F, 0.0F, 0.0F);
            }

            GL11.glColor4f(f101 = tileRenderer97.minecraft.level.getBrightness((int)player70.x, (int)player70.y, (int)player70.z), f101, f101, 1.0F);
            Tesselator tesselator93 = Tesselator.instance;
            if(tileRenderer97.tile != null) {
                f24 = 0.4F;
                GL11.glScalef(0.4F, f24, f24);
                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, tileRenderer97.minecraft.textures.loadTexture("/terrain.png"));
                tileRenderer97.tile.renderGuiTile(tesselator93);
            } else {
                this.player.bindTexture(tileRenderer97.minecraft.textures);
                GL11.glScalef(1.0F, -1.0F, -1.0F);
                GL11.glTranslatef(0.0F, 0.2F, 0.0F);
                GL11.glRotatef(-120.0F, 0.0F, 0.0F, 1.0F);
                GL11.glScalef(1.0F, 1.0F, 1.0F);
                f24 = 0.0625F;
                Cube cube100;
                if(!(cube100 = tileRenderer97.minecraft.player.getModel().leftArm).compiled) {
                    cube100.translateTo(f24);
                }

                GL11.glCallList(cube100.list);
            }

            GL11.glDisable(GL11.GL_NORMALIZE);
            GL11.glPopMatrix();
            this.gameRenderer.toggleLight(false);
            if(!this.options.anaglyph3d) {
                break;
            }

            ++i81;
        }
	}

	public void shootArrow() {
	    if (this.player.arrows > 0) {
            this.level.addEntity(new Arrow(this.level, this.player, this.player.x, this.player.y, this.player.z, this.player.yRot, this.player.xRot, 1.2F));
            --this.player.arrows;
	    }
	}

    public final void generateLevel(int size) {
        String string2 = this.user != null ? this.user.name : "anonymous";
        Level size1 = (new LevelGen(this.loadingScreen)).generateLevel(string2, 128 << size, 128 << size, 64);
        this.gamemode.createPlayer(size1);
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
            level.initTransient();
            this.gamemode.initLevel(level);
            level.font = this.font;
            level.rendererContext = this;
            if(!this.isOnlineClient()) {
                this.player = (Player)level.findSubclassOf(Player.class);
            } else if(this.player != null) {
                this.player.resetPos();
                this.gamemode.initPlayer(this.player);
                if(level != null) {
                    level.player = this.player;
                    level.addEntity(this.player);
                }
            }
        }

        if(this.player == null) {
            this.player = new Player(level);
            this.player.resetPos();
            this.gamemode.initPlayer(this.player);
            if(level != null) {
                level.player = this.player;
            }
        }

        if(this.player != null) {
            this.player.input = new KeyboardInput(this.options);
            this.gamemode.adjustPlayer(this.player);
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
            ParticleEngine particleEngine5 = this.particleEngine;
            if(level != null) {
                level.particleEngine = particleEngine5;
            }

            for(int i4 = 0; i4 < 2; ++i4) {
                particleEngine5.particles[i4].clear();
            }
        }

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
