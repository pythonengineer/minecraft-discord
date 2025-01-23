package com.mojang.minecraft;

import com.mojang.minecraft.character.Vec3;
import com.mojang.minecraft.character.Zombie;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.gui.PauseScreen;
import com.mojang.minecraft.gui.Screen;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelIO;
import com.mojang.minecraft.level.levelgen.LevelGen;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.particle.Particle;
import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.player.MovementInputFromOptions;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.renderer.Chunk;
import com.mojang.minecraft.renderer.DirtyChunkSorter;
import com.mojang.minecraft.renderer.Frustum;
import com.mojang.minecraft.renderer.LevelRenderer;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;

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
	private FloatBuffer fogColor0 = BufferUtils.createFloatBuffer(4);
	private FloatBuffer fogColor1 = BufferUtils.createFloatBuffer(4);
	private Timer timer = new Timer(20.0F);
	private Level level;
	private LevelRenderer levelRenderer;
    public Player player;
	private int paintTexture = 1;
	private ParticleEngine particleEngine;
    public User user = null;
    public String minecraftUri;
    public boolean appletMode = false;
    public volatile boolean pause = false;
    private int yMouseAxis = 1;
    public Textures textures;
    public Font font;
    public int editMode = 0;
    private Screen screen = null;
    private LevelIO levelIo = new LevelIO(this);
    private LevelGen levelGen = new LevelGen(this);
    private int ticksRan = 0;
    public String loadMapUser = null;
    public int loadMapID = 0;
    private static final int[] creativeTiles = new int[]{Tile.rock.id, Tile.dirt.id, Tile.stoneBrick.id, Tile.wood.id, Tile.bush.id, Tile.log.id, Tile.leaf.id, Tile.sand.id, Tile.gravel.id};
    private float fogColorRed = 0.5F;
    private float fogColorGreen = 0.8F;
    private float fogColorBlue = 1.0F;
    private volatile boolean running = false;
    private String fpsString = "";
    private int prevFrameTime = 0;
    private float renderDistance = 0.0F;
	private HitResult hitResult = null;
    private volatile int unusedInt1 = 0;
    private volatile int unusedInt2 = 0;
    public boolean mouseGrabSupported = false;
    public static ScaledResolution scaledResolution;
    public static TouchOverlayRenderer touchOverlayRenderer;
    public static Minecraft minecraft;
    FloatBuffer lb = BufferUtils.createFloatBuffer(16);
    private String title = "";
    private String text = "";

    public Minecraft(int width, int height, boolean fullscreen) {
        this.width = width;
        this.height = height;
        this.fullscreen = fullscreen;
        this.textures = new Textures();
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

        Display.setTitle("Minecraft 0.0.14a_08");

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
        this.font = new Font("/default.gif", this.textures);
        IntBuffer imgData = BufferUtils.createIntBuffer(256);
        imgData.clear().limit(256);
        GL11.glViewport(0, 0, this.width, this.height);
        this.mouseGrabSupported = Mouse.isMouseGrabSupported();
        touchOverlayRenderer = new TouchOverlayRenderer();
        scaledResolution = new ScaledResolution(this.width, this.height);
        PointerInputAbstraction.init(this);
        this.level = new Level();
        boolean success = false;

        try {
            if(this.loadMapUser != null) {
                success = this.loadLevel(this.loadMapUser, this.loadMapID);
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

        this.levelRenderer = new LevelRenderer(this.textures);
        this.particleEngine = new ParticleEngine(this.level, this.textures);
        this.player = new Player(this.level, new MovementInputFromOptions());
        this.player.resetPos();
        if(this.level != null) {
            this.levelRenderer.setLevel(this.level);
        }

        checkGlError("Post startup");
    }

    public final void setScreen(Screen screen1) {
        if(this.screen != null) {
            this.screen.closeScreen();
        }

        this.screen = screen1;
        if(screen1 != null) {
            screen1.init(this, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
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

    public void destroy() {
        this.saveLevel(0, "");
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

                    this.width = Display.getVisualViewportW();
                    this.height = Display.getVisualViewportH();
                    scaledResolution = new ScaledResolution(this.width, this.height);

                    Timer timer27 = this.timer;
                    long j7;
                    long j29 = (j7 = EagRuntime.nanoTime()) - timer27.lastTime;
                    timer27.lastTime = j7;
                    if(j29 < 0L) {
                        j29 = 0L;
                    }

                    if(j29 > 1000000000L) {
                        j29 = 1000000000L;
                    }

                    timer27.fps += (float)j29 * timer27.timeScale * timer27.ticksPerSecond / 1.0E9F;
                    timer27.ticks = (int)timer27.fps;
                    if(timer27.ticks > 100) {
                        timer27.ticks = 100;
                    }

                    timer27.fps -= (float)timer27.ticks;
                    timer27.a = timer27.fps;

                    PointerInputAbstraction.runGameLoop();

                    for(int i28 = 0; i28 < this.timer.ticks; ++i28) {
                        ++this.ticksRan;
    					this.tick();
                        if (i28 < this.timer.ticks - 1) {
                            PointerInputAbstraction.runGameLoop();
                        }
    				}
    
                    if (!Display.contextLost()) {
                        GL11.optimize();
                        checkGlError("Pre render");
                        this.render(this.timer.a);
                        checkGlError("Post render");
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                        this.setupOrthoCamera();
                        touchOverlayRenderer.render(width, height, scaledResolution);
                        GL11.disableBlend();
                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                    }
    
                    Display.update();
                    ++frames;

                    while(EagRuntime.currentTimeMillis() >= lastTime + 1000L) {
                        this.fpsString = frames + " fps, " + Chunk.updates + " chunk updates";
                        Chunk.updates = 0;
                        lastTime += 1000L;
                        frames = 0;
                    }
                }
			}
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

    public void stop() {
        this.running = false;
    }

    public void grabMouse() {
        Mouse.setGrabbed(true);
        this.setScreen((Screen)null);
    }

    public void releaseMouse() {
        this.player.releaseAllKeys();
        Mouse.setGrabbed(false);
        this.setScreen(new PauseScreen());
    }

    private void clickMouse() {
        if (this.hitResult == null) {
            return;
        }

        Tile tile1 = Tile.tiles[this.level.getTile(this.hitResult.x, this.hitResult.y, this.hitResult.z)];
        if(this.editMode == 0) {
            boolean z7 = this.level.setTile(this.hitResult.x, this.hitResult.y, this.hitResult.z, 0);
            if(tile1 != null && z7) {
                tile1.destroy(this.level, this.hitResult.x, this.hitResult.y, this.hitResult.z, this.particleEngine);
            }

        } else {
            int i2 = this.hitResult.x;
            int i6 = this.hitResult.y;
            int i3 = this.hitResult.z;
            if(this.hitResult.f == 0) {
                --i6;
            }

            if(this.hitResult.f == 1) {
                ++i6;
            }

            if(this.hitResult.f == 2) {
                --i3;
            }

            if(this.hitResult.f == 3) {
                ++i3;
            }

            if(this.hitResult.f == 4) {
                --i2;
            }

            if(this.hitResult.f == 5) {
                ++i2;
            }

            Tile tile4;
            AABB aABB8;
            if(((tile4 = Tile.tiles[this.level.getTile(i2, i6, i3)]) == null || tile4 == Tile.water || tile4 == Tile.calmWater || tile4 == Tile.lava || tile4 == Tile.calmLava) && ((aABB8 = Tile.tiles[this.paintTexture].getAABB(i2, i6, i3)) == null || (this.player.bb.intersects(aABB8) ? false : this.level.isFree(aABB8)))) {
                this.level.setTile(i2, i6, i3, this.paintTexture);
                Tile.tiles[this.paintTexture].onBlockAdded(this.level, i2, i6, i3);
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
                                    int scaledX = x / scaledResolution.getScaleFactor();
                                    int scaledY = y / scaledResolution.getScaleFactor();
                                    if ((scaledX < 57 && scaledX >= 6) &&
                                        (scaledY < (scaledResolution.getScaledHeight_double() - 38) &&
                                         scaledY >= (scaledResolution.getScaledHeight_double() - 89))) {
                                        int i2 = 1;
                                        int i3 = 0;
                                        for(int i4 = 0; i4 < creativeTiles.length; ++i4) {
                                            if(creativeTiles[i4] == this.paintTexture) {
                                                i3 = i4;
                                            }
                                        }

                                        for(i3 += i2; i3 < 0; i3 += creativeTiles.length) {
                                        }

                                        while(i3 >= creativeTiles.length) {
                                            i3 -= creativeTiles.length;
                                        }

                                        this.paintTexture = creativeTiles[i3];
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
                    int i1;
                    int i2;
                    int i3;
                    Minecraft minecraft5;
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

                    if (Mouse.getEventButton() == 2 && Mouse.getEventButtonState()) {
                        minecraft5 = this;
                        if(this.hitResult != null) {
                            if((i2 = this.level.getTile(this.hitResult.x, this.hitResult.y, this.hitResult.z)) == Tile.grass.id) {
                                i2 = Tile.dirt.id;
                            }

                            for(i3 = 0; i3 < creativeTiles.length; ++i3) {
                                if(i2 == creativeTiles[i3]) {
                                    minecraft5.paintTexture = creativeTiles[i3];
                                }
                            }
                        }
                    }

                    if ((i1 = Mouse.getEventDWheel()) != 0) {
                        i2 = i1;
                        minecraft5 = this;
                        if(i1 > 0) {
                            i2 = 1;
                        }

                        if(i2 < 0) {
                            i2 = -1;
                        }

                        i3 = 0;

                        for(int i4 = 0; i4 < creativeTiles.length; ++i4) {
                            if(creativeTiles[i4] == minecraft5.paintTexture) {
                                i3 = i4;
                            }
                        }

                        for(i3 += i2; i3 < 0; i3 += creativeTiles.length) {
                        }

                        while(i3 >= creativeTiles.length) {
                            i3 -= creativeTiles.length;
                        }

                        minecraft5.paintTexture = creativeTiles[i3];
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
                        this.releaseMouse();
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_R) {
                        this.player.resetPos();
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
                        this.saveSpawn();
                    }

                    for(int i1 = 0; i1 < 9; ++i1) {
                        if(Keyboard.getEventKey() == i1 + Keyboard.KEY_1) {
                            this.paintTexture = creativeTiles[i1];
                        }
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_Y) {
                        this.yMouseAxis = -this.yMouseAxis;
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_G && this.level.entities.size() < 256) {
                        this.addZombie();
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_F) {
                        LevelRenderer levelRenderer8 = this.levelRenderer;
                        this.levelRenderer.drawDistance = (levelRenderer8.drawDistance + 1) % 4;
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

        ++this.levelRenderer.cloudTickCounter;
        this.level.tick();
        ParticleEngine particleEngine7 = this.particleEngine;

        for(int i2 = 0; i2 < particleEngine7.particles.size(); ++i2) {
            Particle particle8;
            (particle8 = (Particle)particleEngine7.particles.get(i2)).tick();
            if(particle8.removed) {
                particleEngine7.particles.remove(i2--);
            }
        }

        this.player.tick();
    }

    private void orientCamera(float a) {
        GL11.glTranslatef(0.0F, 0.0F, -0.3F);
        GL11.glRotatef(this.player.xRot - this.player.xRotI * (1.0F - a), 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(this.player.yRot - this.player.yRotI * (1.0F - a), 0.0F, 1.0F, 0.0F);
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
        if(!Display.isActive()) {
            this.releaseMouse();
        }

        float frustum = 0.0F;
        float i = 0.0F;
        frustum = (float)PointerInputAbstraction.getDX();
        i = (float)PointerInputAbstraction.getDY();
        this.player.turn(frustum, i * (float)this.yMouseAxis);

        GL11.glViewport(0, 0, this.width, this.height);
        checkGlError("Set viewport");
        this.pick(a);
        checkGlError("Picked");
        this.fogColorRed = 0.92F;
        this.fogColorGreen = 0.98F;
        this.fogColorBlue = 1.0F;
        GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
        this.renderDistance = (float)(1024 >> (this.levelRenderer.drawDistance << 1));
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
        this.setupFog(0);
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
        this.levelRenderer.renderEntities(frustum22, a);
        checkGlError("Rendered entities");
        this.particleEngine.render(this.player, a);
        checkGlError("Rendered particles");
        GL11.glCallList(this.levelRenderer.surroundLists);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.setupFog(-1);
        this.levelRenderer.renderClouds(a);
        this.setupFog(1);
        GL11.glEnable(GL11.GL_LIGHTING);
        if(this.hitResult != null) {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            this.levelRenderer.renderHit(this.player, this.hitResult, this.editMode, this.paintTexture);
            LevelRenderer.renderHitOutline(this.hitResult, this.editMode);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.setupFog(0);
        GL11.glCallList(this.levelRenderer.surroundLists + 1);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColorMask(false, false, false, false);
        this.levelRenderer.render(this.player, 1);
        GL11.glColorMask(true, true, true, true);
        this.levelRenderer.render(this.player, 1);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        if(this.hitResult != null) {
            GL11.glDepthFunc(GL11.GL_LESS);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.doPolygonOffset(99.0F, -99.0F);
            GL11.enablePolygonOffset();
            this.levelRenderer.renderHit(this.player, this.hitResult, this.editMode, this.paintTexture);
            LevelRenderer.renderHitOutline(this.hitResult, this.editMode);
            GL11.disablePolygonOffset();
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
        }

        this.drawGui(a);
        checkGlError("Rendered gui");
	}

    public void addZombie() {
        this.level.entities.add(new Zombie(this.level, this.player.x, this.player.y, this.player.z));
    }

    public void saveSpawn() {
        this.level.setSpawnPos((int)this.player.x, (int)this.player.y, (int)this.player.z, this.player.yRot);
        this.player.resetPos();
    }

	private void drawGui(float a) {
        int screenWidth = scaledResolution.getScaledWidth();
        int screenHeight = scaledResolution.getScaledHeight();
        int xMouse = Mouse.getX() * screenWidth / this.width;
        int yMouse = screenHeight - Mouse.getY() * screenHeight / this.height - 1;
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		this.setupOrthoCamera();
        checkGlError("GUI: Init");
		GL11.glPushMatrix();
        GL11.glTranslatef(32.0F, 64.0F, -50.0F);
        Tesselator t = Tesselator.instance;
        GL11.glScalef(32.0F, 32.0F, 32.0F);
        GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-1.5F, 0.5F, 0.5F);
        GL11.glScalef(-1.0F, -1.0F, -1.0F);
        int id = this.textures.loadTexture("/terrain.png", 9728);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
        t.begin(DefaultVertexFormats.POSITION_TEX_COLOR);
        Tile.tiles[this.paintTexture].render(t, this.level, 0, -2, 0, 0);
        t.end();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
        checkGlError("GUI: Draw selected");
        this.font.drawShadow("0.0.14a_08", 2, 2, 0xFFFFFF);
        this.font.drawShadow(this.fpsString, 2, 12, 16777215);
        checkGlError("GUI: Draw text");
        int wc = screenWidth / 2;
        int hc = screenHeight / 2;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        t.begin(DefaultVertexFormats.POSITION);
        t.vertex((float)(wc + 1), (float)(hc - 4), 0.0F);
        t.vertex((float)(wc - 0), (float)(hc - 4), 0.0F);
        t.vertex((float)(wc - 0), (float)(hc + 5), 0.0F);
        t.vertex((float)(wc + 1), (float)(hc + 5), 0.0F);
        t.vertex((float)(wc + 5), (float)(hc - 0), 0.0F);
        t.vertex((float)(wc - 4), (float)(hc - 0), 0.0F);
        t.vertex((float)(wc - 4), (float)(hc + 1), 0.0F);
        t.vertex((float)(wc + 5), (float)(hc + 1), 0.0F);
        t.end();
        checkGlError("GUI: Draw crosshair");
        if(this.screen != null) {
            this.screen.render(xMouse, yMouse);
        }
	}

    private void setupFog(int i) {
        if(i == -1) {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
            GL11.glFogf(GL11.GL_FOG_START, 0.0F);
            GL11.glFogf(GL11.GL_FOG_END, this.renderDistance);
            GL11.glFog(GL11.GL_FOG_COLOR, this.getBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
            GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(1.0F, 1.0F, 1.0F, 1.0F));
        } else {
            Tile currentTile = Tile.tiles[this.level.getTile((int)this.player.x, (int)(this.player.y + 0.12F), (int)this.player.z)];
            if(currentTile != null && currentTile.getLiquidType() == 1) {
                GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
                GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
                GL11.glFog(GL11.GL_FOG_COLOR, this.getBuffer(0.02F, 0.02F, 0.2F, 1.0F));
                GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(0.3F, 0.3F, 0.7F, 1.0F));
            } else if(currentTile != null && currentTile.getLiquidType() == 2) {
                GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
                GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
                GL11.glFog(GL11.GL_FOG_COLOR, this.getBuffer(0.6F, 0.1F, 0.0F, 1.0F));
                GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(0.4F, 0.3F, 0.3F, 1.0F));
            } else if(i == 0) {
                GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
                GL11.glFogf(GL11.GL_FOG_START, 0.0F);
                GL11.glFogf(GL11.GL_FOG_END, this.renderDistance);
                GL11.glFog(GL11.GL_FOG_COLOR, this.getBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
                GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(1.0F, 1.0F, 1.0F, 1.0F));
            } else if(i == 1) {
                GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
                GL11.glFogf(GL11.GL_FOG_DENSITY, 0.01F);
                GL11.glFog(GL11.GL_FOG_COLOR, this.fogColor1);
                float br = 0.6F;
                GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(br, br, br, 1.0F));
            }

            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
            GL11.glEnable(GL11.GL_LIGHTING);
        }
    }

    private FloatBuffer getBuffer(float a, float b, float c, float d) {
        this.lb.clear();
        this.lb.put(a).put(b).put(c).put(1.0F);
        this.lb.flip();
        return this.lb;
    }

    public void beginLevelLoading(String title) {
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

    public final void levelLoadUpdate(String string1) {
        this.text = string1;
        this.setLoadingProgress(-1);
    }

    public final void setLoadingProgress(int i1) {
        if(i1 >= 0) {
            return;
        }
        int i2 = scaledResolution.getScaledWidth();
        int i3 = scaledResolution.getScaledHeight();
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
        Tesselator tesselator4 = Tesselator.instance;
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        int i5 = this.textures.loadTexture("/dirt.png", 9728);
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

    public final void generateLevel(int i1) {
        String string2 = this.user != null ? this.user.name : "anonymous";
        this.setLevel(this.levelGen.generateLevel(string2, 128 << i1, 128 << i1, 64));
    }

    public final boolean saveLevel(int i1, String string2) {
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

    private void setLevel(Level level1) {
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

	public static void main(String[] args) throws LWJGLException {
        PlatformRuntime.setThreadName("Client thread");
        (minecraft = new Minecraft(854, 480, false)).run();
	}
}
