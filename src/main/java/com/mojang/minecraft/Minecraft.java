package com.mojang.minecraft;

import com.mojang.minecraft.character.Vec3;
import com.mojang.minecraft.character.Zombie;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.gui.PauseScreen;
import com.mojang.minecraft.gui.Screen;
import com.mojang.minecraft.level.Chunk;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelIO;
import com.mojang.minecraft.level.LevelLoaderListener;
import com.mojang.minecraft.level.LevelRenderer;
import com.mojang.minecraft.level.levelgen.LevelGen;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Frustum;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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

public class Minecraft implements Runnable, LevelLoaderListener {
    public static final String VERSION_STRING = "0.0.13a";
    private boolean fullscreen = false;
	public int width;
	public int height;
	private FloatBuffer fogColor0 = BufferUtils.createFloatBuffer(4);
	private FloatBuffer fogColor1 = BufferUtils.createFloatBuffer(4);
	private Timer timer = new Timer(20.0F);
	public Level level;
	private LevelRenderer levelRenderer;
    public Player player;
	private int paintTexture = 1;
	private ParticleEngine particleEngine;
    public User user = new User("noname");
    private ArrayList<Entity> entities = new ArrayList();
    public boolean appletMode = false;
    public volatile boolean pause = false;
    private int yMouseAxis = 1;
    public Textures textures;
    public Font font;
    public int editMode = 0;
    private Screen screen = null;
    public LevelIO levelIo = new LevelIO(this);
    private LevelGen levelGen = new LevelGen(this);
    private volatile boolean running = false;
    private String fpsString = "";
	private HitResult hitResult = null;
    public boolean mouseGrabSupported = false;
    public static ScaledResolution scaledResolution;
    public static TouchOverlayRenderer touchOverlayRenderer;
    public static Minecraft minecraft;
    private int startX = 0;
    private int startY = 0;
    FloatBuffer lb = BufferUtils.createFloatBuffer(16);
    private String title = "";

    public Minecraft(int width, int height, boolean fullscreen) {
        this.width = width;
        this.height = height;
        this.fullscreen = fullscreen;
        this.textures = new Textures();
    }

    public void init() throws LWJGLException, IOException {
        int col1 = 920330;
        float fr = 0.5F;
        float fg = 0.8F;
        float fb = 1.0F;
        this.fogColor0.put(new float[]{fr, fg, fb, 1.0F});
        this.fogColor0.flip();
        this.fogColor1.put(new float[]{(float)(col1 >> 16 & 255) / 255.0F, (float)(col1 >> 8 & 255) / 255.0F, (float)(col1 & 255) / 255.0F, 1.0F});
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

        Display.setTitle("Minecraft 0.0.13a");

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
        this.checkGlError("Pre startup");
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glClearColor(fr, fg, fb, 0.0F);
        GL11.glClearDepth(1.0D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        this.checkGlError("Startup");
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
            VFile2 f = new VFile2("level.dat");
            if (f.exists()) {
                success = this.levelIo.load(this.level, new DataInputStream(
                        PlatformRuntime.newGZIPInputStream(f.getInputStream())));
                if(!success) {
                    success = this.levelIo.loadLegacy(this.level, new DataInputStream(
                            PlatformRuntime.newGZIPInputStream(f.getInputStream())));
                }
            }
        } catch (Exception var10) {
            success = false;
        }

        if(!success) {
            this.levelGen.generateLevel(this.level, this.user.name, 256, 256, 64);
        }

        this.levelRenderer = new LevelRenderer(this.level, this.textures);
        this.player = new Player(this.level);
        this.particleEngine = new ParticleEngine(this.level, this.textures);

        for(int e = 0; e < 10; ++e) {
            Zombie zombie = new Zombie(this.level, this.textures, 128.0F, 0.0F, 128.0F);
            zombie.resetPos();
            this.entities.add(zombie);
        }

        this.checkGlError("Post startup");
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
        if(screen != null) {
            screen.init(this, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
        }

    }

    private void checkGlError(String string) {
        int errorCode = GL11.glGetError();
        if(errorCode != 0) {
            String errorString = GLU.gluErrorString(errorCode);
            System.out.println("########## GL ERROR ##########");
            System.out.println("@ " + string);
            System.out.println(errorCode + ": " + errorString);
        }

    }

    public void attemptSaveLevel() {
        try {
            this.levelIo.save(this.level, new DataOutputStream(
                    PlatformRuntime.newGZIPOutputStream(new VFile2("level.dat").getOutputStream())));
        } catch (Exception var2) {
        }
    }

    public void destroy() {
        this.attemptSaveLevel();
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

                    this.timer.advanceTime();
                    PointerInputAbstraction.runGameLoop();

                    for(int e = 0; e < this.timer.ticks; ++e) {
    					this.tick();
                        if (e < this.timer.ticks - 1) {
                            PointerInputAbstraction.runGameLoop();
                        }
    				}
    
                    if (!Display.contextLost()) {
                        GL11.optimize();
                        this.checkGlError("Pre render");
                        this.render(this.timer.a);
                        this.checkGlError("Post render");
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

    private void handleMouseClick() {
        if(this.editMode == 0) {
            if(this.hitResult != null) {
                Tile x = Tile.tiles[this.level.getTile(this.hitResult.x, this.hitResult.y, this.hitResult.z)];
                boolean y = this.level.setTile(this.hitResult.x, this.hitResult.y, this.hitResult.z, 0);
                if(x != null && y) {
                    x.destroy(this.level, this.hitResult.x, this.hitResult.y, this.hitResult.z, this.particleEngine);
                }
            }
        } else if(this.hitResult != null) {
            int var5 = this.hitResult.x;
            int var6 = this.hitResult.y;
            int z = this.hitResult.z;
            if(this.hitResult.f == 0) {
                --var6;
            }

            if(this.hitResult.f == 1) {
                ++var6;
            }

            if(this.hitResult.f == 2) {
                --z;
            }

            if(this.hitResult.f == 3) {
                ++z;
            }

            if(this.hitResult.f == 4) {
                --var5;
            }

            if(this.hitResult.f == 5) {
                ++var5;
            }

            AABB aabb = Tile.tiles[this.paintTexture].getAABB(var5, var6, z);
            if(aabb == null || this.isFree(aabb)) {
                this.level.setTile(var5, var6, z, this.paintTexture);
            }
        }

    }

	public void tick() {
	    if(this.screen == null && !PointerInputAbstraction.isTouchMode() && !Mouse.isMouseGrabbed()) {
	        this.releaseMouse();
	    }

	    if(this.screen == null) {
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
                                        ++this.paintTexture;
                                        if (this.paintTexture == 2) {
                                            this.paintTexture = 3;
                                        } else if (this.paintTexture == 5) {
                                            this.paintTexture = 6;
                                        } else if (this.paintTexture > 6) {
                                            this.paintTexture = 1;
                                        }
                                        break;
                                    }
                                    startX = x;
                                    startY = y;
                                    touch = true;
                                    break;
                                case TOUCHEND:
                                    if (TouchControls.handleTouchEnd(uid, x, y)) {
                                        touch = true;
                                        break;
                                    }
                                    if ((Math.abs(startX - x) <= 10) && (Math.abs(startY - y) <= 10)) {
                                        this.handleMouseClick();
                                    }
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
                        this.handleMouseClick();
                    }

                    if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
                        this.editMode = (this.editMode + 1) % 2;
                    }
                }

                if (!(touch || Mouse.isActuallyGrabbed()) && (touch || Mouse.getEventButtonState())) {
                    this.grabMouse();
                }
            }

            while(true) {
                if(!Keyboard.next()) {
                    break;
                }

                this.player.setKey(Keyboard.getEventKey(), Keyboard.getEventKeyState());
                if(Keyboard.getEventKeyState()) {
                    if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                        this.releaseMouse();
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
                        this.attemptSaveLevel();
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_R) {
                        this.player.resetPos();
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_1) {
                        this.paintTexture = 1;
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_2) {
                        this.paintTexture = 3;
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_3) {
                        this.paintTexture = 4;
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_4) {
                        this.paintTexture = 5;
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_6) {
                        this.paintTexture = 6;
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_Y) {
                        this.yMouseAxis *= -1;
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_G) {
                        this.addZombie();
                    }

                    if(Keyboard.getEventKey() == Keyboard.KEY_F) {
                        this.levelRenderer.toggleDrawDistance();
                    }
                }
            }
	    }

        if(this.screen != null) {
            this.screen.updateEvents();
            if(this.screen != null) {
                this.screen.tick();
            }
        }

        this.level.tick();
        this.particleEngine.tick();

        for(int i = 0; i < this.entities.size(); ++i) {
            ((Entity)this.entities.get(i)).tick();
            if(((Entity)this.entities.get(i)).removed) {
                this.entities.remove(i--);
            }
        }

        this.player.tick();
    }

    private boolean isFree(AABB aabb) {
        if(this.player.bb.intersects(aabb)) {
            return false;
        } else {
            for(int i = 0; i < this.entities.size(); ++i) {
                if(((Entity)this.entities.get(i)).bb.intersects(aabb)) {
                    return false;
                }
            }

            return true;
        }
    }

    private void moveCameraToPlayer(float a) {
        GL11.glTranslatef(0.0F, 0.0F, -0.3F);
        GL11.glRotatef(this.player.xRot, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(this.player.yRot, 0.0F, 1.0F, 0.0F);
        float x = this.player.xo + (this.player.x - this.player.xo) * a;
        float y = this.player.yo + (this.player.y - this.player.yo) * a;
        float z = this.player.zo + (this.player.z - this.player.zo) * a;
        GL11.glTranslatef(-x, -y, -z);
    }

    private void setupCamera(float a) {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(70.0F, (float)this.width / (float)this.height, 0.05F, 1024.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        this.moveCameraToPlayer(a);
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

        GL11.glViewport(0, 0, this.width, this.height);
        float frustum = 0.0F;
        float i = 0.0F;
        frustum = (float)PointerInputAbstraction.getDX();
        i = (float)PointerInputAbstraction.getDY();
        this.player.turn(frustum, i * (float)this.yMouseAxis);

        this.checkGlError("Set viewport");
        this.pick(a);
        this.checkGlError("Picked");
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
        this.setupCamera(a);
        this.checkGlError("Set up camera");
        GL11.glEnable(GL11.GL_CULL_FACE);
        Frustum var5 = Frustum.getFrustum();
        this.levelRenderer.cull(var5);
        this.levelRenderer.updateDirtyChunks(this.player);
        this.checkGlError("Update chunks");
        this.setupFog(0);
        GL11.glEnable(GL11.GL_FOG);
        this.levelRenderer.render(this.player, 0);
        this.checkGlError("Rendered level");

        Entity zombie;
        int var6;
        for(var6 = 0; var6 < this.entities.size(); ++var6) {
            zombie = (Entity)this.entities.get(var6);
            if(zombie.isLit() && var5.isVisible(zombie.bb)) {
                ((Entity)this.entities.get(var6)).render(a);
            }
        }

        this.checkGlError("Rendered entities");
        this.particleEngine.render(this.player, a, 0);
        this.checkGlError("Rendered particles");
        this.setupFog(1);
        this.levelRenderer.render(this.player, 1);

        for(var6 = 0; var6 < this.entities.size(); ++var6) {
            zombie = (Entity)this.entities.get(var6);
            if(!zombie.isLit() && var5.isVisible(zombie.bb)) {
                ((Entity)this.entities.get(var6)).render(a);
            }
        }

        this.particleEngine.render(this.player, a, 1);
        this.levelRenderer.renderSurroundingGround();
        if(this.hitResult != null) {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            this.levelRenderer.renderHit(this.player, this.hitResult, this.editMode, this.paintTexture);
            this.levelRenderer.renderHitOutline(this.player, this.hitResult, this.editMode, this.paintTexture);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.setupFog(0);
        this.levelRenderer.renderSurroundingWater();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColorMask(false, false, false, false);
        this.levelRenderer.render(this.player, 2);
        GL11.glColorMask(true, true, true, true);
        this.levelRenderer.render(this.player, 2);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_FOG);
        if(this.hitResult != null) {
            GL11.glDepthFunc(GL11.GL_LESS);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.doPolygonOffset(99.0F, -99.0F);
            GL11.enablePolygonOffset();
            this.levelRenderer.renderHit(this.player, this.hitResult, this.editMode, this.paintTexture);
            this.levelRenderer.renderHitOutline(this.player, this.hitResult, this.editMode, this.paintTexture);
            GL11.disablePolygonOffset();
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
        }

        this.drawGui(a);
        this.checkGlError("Rendered gui");
	}

    public void addZombie() {
        this.entities.add(new Zombie(this.level, this.textures, this.player.x, this.player.y, this.player.z));
    }

	private void drawGui(float a) {
        int screenWidth = scaledResolution.getScaledWidth();
        int screenHeight = scaledResolution.getScaledHeight();
        int xMouse = Mouse.getX() * screenWidth / this.width;
        int yMouse = screenHeight - Mouse.getY() * screenHeight / this.height - 1;
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		this.setupOrthoCamera();
        this.checkGlError("GUI: Init");
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
        this.checkGlError("GUI: Draw selected");
        this.font.drawShadow("0.0.13a", 2, 2, 16777215);
        this.font.drawShadow(this.fpsString, 2, 12, 16777215);
        this.checkGlError("GUI: Draw text");
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
        this.checkGlError("GUI: Draw crosshair");
        if(this.screen != null) {
            this.screen.render(xMouse, yMouse);
        }
	}

    private void setupFog(int i) {
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
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            GL11.glFogf(GL11.GL_FOG_DENSITY, 0.001F);
            GL11.glFog(GL11.GL_FOG_COLOR, this.fogColor0);
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

    private FloatBuffer getBuffer(float a, float b, float c, float d) {
        this.lb.clear();
        this.lb.put(a).put(b).put(c).put(d);
        this.lb.flip();
        return this.lb;
    }

    public static void checkError() {
        int e = GL11.glGetError();
        if(e != 0) {
            throw new IllegalStateException(GLU.gluErrorString(e));
        }
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

    public void levelLoadUpdate(String status) {
        int screenWidth = scaledResolution.getScaledWidth();
        int screenHeight = scaledResolution.getScaledHeight();
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
        Tesselator t = Tesselator.instance;
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        int id = this.textures.loadTexture("/dirt.png", 9728);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        t.begin(DefaultVertexFormats.POSITION_TEX_COLOR);
        t.color(8421504);
        float s = 32.0F;
        t.vertexUV(0.0F, (float)screenHeight, 0.0F, 0.0F, (float)screenHeight / s);
        t.vertexUV((float)screenWidth, (float)screenHeight, 0.0F, (float)screenWidth / s, (float)screenHeight / s);
        t.vertexUV((float)screenWidth, 0.0F, 0.0F, (float)screenWidth / s, 0.0F);
        t.vertexUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        t.end();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        this.font.drawShadow(this.title, (screenWidth - this.font.width(this.title)) / 2, screenHeight / 2 - 4 - 8, 16777215);
        this.font.drawShadow(status, (screenWidth - this.font.width(status)) / 2, screenHeight / 2 - 4 + 4, 16777215);
        Display.update();

        try {
            Thread.sleep(200L);
        } catch (Exception var8) {
        }

    }

    public void generateNewLevel() {
        this.levelGen.generateLevel(this.level, this.user.name, 32, 512, 64);
        this.player.resetPos();

        for(int i = 0; i < this.entities.size(); ++i) {
            this.entities.remove(i--);
        }

    }

	public static void main(String[] args) throws LWJGLException {
        PlatformRuntime.setThreadName("Client thread");
        (minecraft = new Minecraft(854, 480, false)).run();
	}
}
