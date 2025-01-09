package com.mojang.minecraft;

import com.mojang.minecraft.character.Vec3;
import com.mojang.minecraft.character.Zombie;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.level.Chunk;
import com.mojang.minecraft.level.DirtyChunkSorter;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelRenderer;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.particle.Particle;
import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Frustum;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;
import java.util.ArrayList;
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
	public Level level;
	private LevelRenderer levelRenderer;
    public Player thePlayer;
	private int paintTexture = 1;
	private ParticleEngine particleEngine;
    private ArrayList entities = new ArrayList();
    public boolean appletMode = false;
    public volatile boolean pause = false;
    private int yMouseAxis = 1;
    public Textures textureManager;
    private Font font;
    public int editMode = 0;
    volatile boolean running = false;
    private String fpsString = "";
    private boolean mouseGrabbed = false;
	private HitResult hitResult = null;
    public boolean mouseGrabSupported = false;
    private long sinceDelete;
    public static ScaledResolution scaledResolution;
    public static TouchOverlayRenderer touchOverlayRenderer;
    public static Minecraft minecraft;
    private int startX = 0;
    private int startY = 0;
    private FloatBuffer lb = BufferUtils.createFloatBuffer(16);

    public Minecraft(int width, int height, boolean fullscreen) {
        this.width = width;
        this.height = height;
        this.fullscreen = fullscreen;
        this.textureManager = new Textures();
    }

    private static void reportGLError(String var0) {
        int var1 = GL11.glGetError();
        if(var1 != 0) {
            String var2 = GLU.gluErrorString(var1);
            System.out.println("########## GL ERROR ##########");
            System.out.println("@ " + var0);
            System.out.println(var1 + ": " + var2);
            EagRuntime.exit();
        }

    }

    public void destroy() {
        if (EagRuntime.currentTimeMillis() - this.sinceDelete >= 5000) {
            try {
                this.level.save();
            } catch (Exception var2) {
            }
        }

        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }

	public final void run() {
        this.running = true;

        try {
            Minecraft var4 = this;
            float var8 = 0.5F;
            float var9 = 0.8F;
            this.fogColor0.put(new float[]{var8, var9, 1.0F, 1.0F});
            this.fogColor0.flip();
            this.fogColor1.put(new float[]{(float)14 / 255.0F, (float)11 / 255.0F, (float)10 / 255.0F, 1.0F});
            this.fogColor1.flip();
            this.width = Display.getVisualViewportW();
            this.height = Display.getVisualViewportH();
            if(this.fullscreen) {
                Display.setFullscreen(true);
                this.width = Display.getDisplayMode().getWidth();
                this.height = Display.getDisplayMode().getHeight();
            } else {
                Display.setDisplayMode(new DisplayMode(this.width, this.height));
            }

            Display.setTitle("Minecraft 0.0.12a_03");
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
            reportGLError("Pre startup");
    		GL11.glEnable(GL11.GL_TEXTURE_2D);
    		GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glClearColor(var8, var9, 1.0F, 0.0F);
    		GL11.glClearDepth(1.0D);
    		GL11.glEnable(GL11.GL_DEPTH_TEST);
    		GL11.glDepthFunc(GL11.GL_LEQUAL);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
            GL11.glCullFace(GL11.GL_BACK);
    		GL11.glMatrixMode(GL11.GL_PROJECTION);
    		GL11.glLoadIdentity();
    		GL11.glMatrixMode(GL11.GL_MODELVIEW);
            reportGLError("Startup");
            this.font = new Font("/default.gif", this.textureManager);
            IntBuffer var1 = BufferUtils.createIntBuffer(256);
            var1.clear().limit(256);
            GL11.glViewport(0, 0, this.width, this.height);
            this.mouseGrabSupported = Mouse.isMouseGrabSupported();
            touchOverlayRenderer = new TouchOverlayRenderer();
            scaledResolution = new ScaledResolution(width, height);
            PointerInputAbstraction.init(this);
            this.level = new Level(this, 256, 256, 64);
            this.levelRenderer = new LevelRenderer(this.level, this.textureManager);
            this.thePlayer = new Player(this.level);
            this.particleEngine = new ParticleEngine(this.level, this.textureManager);
            int var2 = 0;
    
            while(true) {
                if(var2 >= 10) {
                    reportGLError("Post startup");
                    break;
                }

                Zombie var3 = new Zombie(var4.level, var4.textureManager, 128.0F, 0.0F, 128.0F);
                var3.resetPos();
                var4.entities.add(var3);
                ++var2;
            }
        } catch (Exception var22) {
            var22.printStackTrace();
            EagRuntime.showPopup("Failed to start Minecraft");
            return;
        }

        long var23 = EagRuntime.currentTimeMillis();
        int var24 = 0;

		try {
            while(this.running) {
                if(this.pause) {
                    Thread.sleep(100L);
                } else {
                    if(Display.isCloseRequested()) {
                        this.running = false;
                    }

                    this.width = Display.getVisualViewportW();
                    this.height = Display.getVisualViewportH();
                    scaledResolution = new ScaledResolution(this.width, this.height);
    
                    Timer var25 = this.timer;
                    long var7 = EagRuntime.nanoTime();
                    long var27 = var7 - var25.lastTime;
                    var25.lastTime = var7;
                    if(var27 < 0L) {
                        var27 = 0L;
                    }

                    if(var27 > 1000000000L) {
                        var27 = 1000000000L;
                    }

                    var25.passedTime += (float)var27 * var25.timeScale * var25.ticksPerSecond / 1.0E9F;
                    var25.ticks = (int)var25.passedTime;
                    if(var25.ticks > 100) {
                        var25.ticks = 100;
                    }

                    var25.passedTime -= (float)var25.ticks;
                    var25.a = var25.passedTime;

                    PointerInputAbstraction.runGameLoop();

                    for(int var26 = 0; var26 < this.timer.ticks; ++var26) {
    					this.tick();
                        if (var26 < this.timer.ticks - 1) {
                            PointerInputAbstraction.runGameLoop();
                        }
    				}
    
                    if (!Display.contextLost()) {
                        GL11.optimize();
                        reportGLError("Pre render");
                        this.render(this.timer.a);
                        reportGLError("Post render");
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                        this.setupOrthoCamera();
                        touchOverlayRenderer.render(width, height, scaledResolution);
                        GL11.disableBlend();
                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                    }
    
                    Display.update();
                    ++var24;
    
                    while(EagRuntime.currentTimeMillis() >= var23 + 1000L) {
                        this.fpsString = var24 + " fps, " + Chunk.updates + " chunk updates";
                        Chunk.updates = 0;
                        var23 += 1000L;
                        var24 = 0;
    				}
                }
			}

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

    private void grabMouse() {
        Mouse.setGrabbed(true);
    }

    private void releaseMouse() {
        Mouse.setGrabbed(false);
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
            int var12 = this.hitResult.x;
            int var13 = this.hitResult.y;
            int var4 = this.hitResult.z;
            if(this.hitResult.f == 0) {
                --var13;
            }

            if(this.hitResult.f == 1) {
                ++var13;
            }

            if(this.hitResult.f == 2) {
                --var4;
            }

            if(this.hitResult.f == 3) {
                ++var4;
            }

            if(this.hitResult.f == 4) {
                --var12;
            }

            if(this.hitResult.f == 5) {
                ++var12;
            }

            AABB var5 = Tile.tiles[this.paintTexture].getBoundingBox(var12, var13, var4);
            if(var5 != null) {
                AABB var7 = var5;
                Minecraft var6 = this;
                boolean var10000;
                if(this.thePlayer.boundingBox.intersects(var5)) {
                    var10000 = false;
                } else {
                    int var14 = 0;

                    while(true) {
                        if(var14 >= var6.entities.size()) {
                            var10000 = true;
                            break;
                        }

                        if(((Entity)var6.entities.get(var14)).boundingBox.intersects(var7)) {
                            var10000 = false;
                            break;
                        }

                        ++var14;
                    }
                }

                if(!var10000) {
                    return;
                }
            }

            this.level.setTile(var12, var13, var4, this.paintTexture);
        }

    }

	private void tick() {
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
            do {
                if(!Keyboard.next()) {
                    Level var9 = this.level;
                    var9.unprocessed += var9.width * var9.height * var9.depth;
                    int var12 = var9.unprocessed / 200;
                    var9.unprocessed -= var12 * 200;

                    for(int var13 = 0; var13 < var12; ++var13) {
                        var9.randValue = var9.randValue * 1664525 + 1013904223;
                        int var4 = var9.randValue >> 16 & var9.width - 1;
                        var9.randValue = var9.randValue * 1664525 + 1013904223;
                        int var14 = var9.randValue >> 16 & var9.depth - 1;
                        var9.randValue = var9.randValue * 1664525 + 1013904223;
                        int var16 = var9.randValue >> 16 & var9.height - 1;
                        byte var17 = var9.blocks[(var14 * var9.height + var16) * var9.width + var4];
                        if(Tile.shouldTick[var17]) {
                            Tile.tiles[var17].tick(var9, var4, var14, var16, var9.random);
                        }
                    }

                    ParticleEngine var10 = this.particleEngine;

                    for(var12 = 0; var12 < var10.particles.size(); ++var12) {
                        Particle var15 = (Particle)var10.particles.get(var12);
                        var15.tick();
                        if(var15.removed) {
                            var10.particles.remove(var12--);
                        }
                    }

                    for(int var11 = 0; var11 < this.entities.size(); ++var11) {
                        ((Entity)this.entities.get(var11)).tick();
                        if(((Entity)this.entities.get(var11)).removed) {
                            this.entities.remove(var11--);
                        }
                    }

                    this.thePlayer.tick();
                    return;
                }
            } while(!Keyboard.getEventKeyState());

			if(Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
				this.level.save();
			}

            if(Keyboard.getEventKey() == Keyboard.KEY_BACK) {
                this.level.delete();
                this.sinceDelete = EagRuntime.currentTimeMillis();
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
                this.yMouseAxis = -this.yMouseAxis;
            }

			if(Keyboard.getEventKey() == Keyboard.KEY_G) {
			    this.addZombie();
            }

            if(Keyboard.getEventKey() == Keyboard.KEY_N) {
                this.level.generateLevel();
                this.thePlayer.resetPos();
                boolean var1 = false;

                while(0 < this.entities.size()) {
                    this.entities.remove(0);
                }
            }

            if(Keyboard.getEventKey() == Keyboard.KEY_F) {
                LevelRenderer var8 = this.levelRenderer;
                var8.drawDistance = (var8.drawDistance + 1) % 4;
            }
        }
    }

    private void focusPlayerCamera(float var1) {
        GL11.glTranslatef(0.0F, 0.0F, -0.3F);
        GL11.glRotatef(this.thePlayer.pitch, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(this.thePlayer.yaw, 0.0F, 1.0F, 0.0F);
        float var2 = this.thePlayer.xo + (this.thePlayer.x - this.thePlayer.xo) * var1;
        float var3 = this.thePlayer.yo + (this.thePlayer.y - this.thePlayer.yo) * var1;
        float var4 = this.thePlayer.zo + (this.thePlayer.z - this.thePlayer.zo) * var1;
        GL11.glTranslatef(-var2, -var3, -var4);
    }

	private void setupCamera(float a) {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(70.0F, (float)this.width / (float)this.height, 0.05F, 1000.0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
        this.focusPlayerCamera(a);
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
        float f65 = this.thePlayer.xRotO + (this.thePlayer.pitch - this.thePlayer.xRotO) * a;
        float f72 = this.thePlayer.yRotO + (this.thePlayer.yaw - this.thePlayer.yRotO) * a;
        float f78 = this.thePlayer.xo + (this.thePlayer.x - this.thePlayer.xo) * a;
        float f57 = this.thePlayer.yo + (this.thePlayer.y - this.thePlayer.yo) * a;
        float f55 = this.thePlayer.zo + (this.thePlayer.z - this.thePlayer.zo) * a;
        Vec3 vec359 = new Vec3(f78, f57, f55);
        f55 = (float)Math.cos((double)(-f72) * Math.PI / 180.0D + Math.PI);
        float f66 = (float)Math.sin((double)(-f72) * Math.PI / 180.0D + Math.PI);
        f72 = (float)Math.cos((double)(-f65) * Math.PI / 180.0D);
        f65 = (float)Math.sin((double)(-f65) * Math.PI / 180.0D);
        f66 *= f72;
        f55 *= f72;
        f72 = 5.0F;
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
        this.thePlayer.turn(frustum, i * (float)this.yMouseAxis);

        reportGLError("Set viewport");
		this.pick(a);
        reportGLError("Picked");

		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
		this.setupCamera(a);
        reportGLError("Set up camera");
		GL11.glEnable(GL11.GL_CULL_FACE);
        Frustum var18 = Frustum.calculateFrustum();
        Frustum var21 = var18;
        LevelRenderer var22 = this.levelRenderer;

        for(int var26 = 0; var26 < var22.chunks.length; ++var26) {
            var22.chunks[var26].visible = var21.isVisible(var22.chunks[var26].aabb);
        }

        Player var4 = this.thePlayer;
        var22 = this.levelRenderer;
        LevelRenderer var35 = var22;
        ArrayList var37 = null;

        for(int var9 = 0; var9 < var35.chunks.length; ++var9) {
            Chunk var40 = var35.chunks[var9];
            if(var40.isDirty()) {
                if(var37 == null) {
                    var37 = new ArrayList();
                }

                var37.add(var40);
            }
        }

        ArrayList var32 = var37;
        if(var37 != null) {
            Collections.sort(var37, new DirtyChunkSorter(var4));

            for(int var28 = 0; var28 < 4 && var28 < var32.size(); ++var28) {
                ((Chunk)var32.get(var28)).rebuild();
            }
        }

        reportGLError("Update chunks");
		this.setupFog(0);
		GL11.glEnable(GL11.GL_FOG);
        this.levelRenderer.render(this.thePlayer, 0);
        reportGLError("Rendered level");

        Entity var24;
        int var25;
        for(var25 = 0; var25 < this.entities.size(); ++var25) {
            var24 = (Entity)this.entities.get(var25);
            if(var24.isLit() && var18.isVisible(var24.boundingBox)) {
                ((Entity)this.entities.get(var25)).render(a);
            }
        }

        reportGLError("Rendered entities");
        this.particleEngine.render(this.thePlayer, a, 0);
        reportGLError("Rendered particles");
        this.setupFog(1);
        this.levelRenderer.render(this.thePlayer, 1);

        for(var25 = 0; var25 < this.entities.size(); ++var25) {
            var24 = (Entity)this.entities.get(var25);
            if(!var24.isLit() && var18.isVisible(var24.boundingBox)) {
                ((Entity)this.entities.get(var25)).render(a);
            }
        }

        this.particleEngine.render(this.thePlayer, a, 1);
        var22 = this.levelRenderer;
        GL11.glCallList(var22.surroundLists);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.setupFog(0);
        GL11.glCallList(var22.surroundLists + 1);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthMask(false);
        this.levelRenderer.render(this.thePlayer, 2);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_FOG);
        reportGLError("Rendered rest");
		if(this.hitResult != null) {
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            this.levelRenderer.renderHit(this.thePlayer, this.hitResult, this.editMode, this.paintTexture);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
		}

        reportGLError("Rendered hit");
		this.drawGui(a);
        reportGLError("Rendered gui");
	}

    public void addZombie() {
        this.entities.add(new Zombie(this.level, this.textureManager, this.thePlayer.x, this.thePlayer.y, this.thePlayer.z));
    }

	private void drawGui(float a) {
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		this.setupOrthoCamera();
        reportGLError("GUI: Init");
		GL11.glPushMatrix();
		GL11.glTranslatef(32.0F, 64.0F, 0.0F);
		Tesselator t = Tesselator.tesselator;
        GL11.glScalef(32.0F, 32.0F, 32.0F);
		GL11.glRotatef(30.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-1.5F, 0.5F, -0.5F);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        int id = this.textureManager.loadTexture("/terrain.png", 9728);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		t.begin(DefaultVertexFormats.POSITION_TEX_COLOR);
		Tile.tiles[this.paintTexture].render(t, this.level, 0, -2, 0, 0);
		t.end();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
        reportGLError("GUI: Draw selected");
        this.font.drawShadow("0.0.12a_03", 2, 2, 16777215);
        this.font.drawShadow(this.fpsString, 2, 12, 16777215);
        reportGLError("GUI: Draw text");
		int wc = scaledResolution.getScaledWidth() / 2;
		int hc = scaledResolution.getScaledHeight() / 2;
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
        reportGLError("GUI: Draw crosshair");
	}

    private void setupFog(int var1) {
        Tile var2 = Tile.tiles[this.level.getTile((int)this.thePlayer.x, (int)this.thePlayer.y, (int)this.thePlayer.z)];
        if(var2 != null && var2.getLiquidType() == 1) {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
            GL11.glFog(GL11.GL_FOG_COLOR, this.getBuffer(0.02F, 0.02F, 0.2F, 1.0F));
            GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(0.3F, 0.3F, 0.5F, 1.0F));
        } else if(var2 != null && var2.getLiquidType() == 2) {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            GL11.glFogf(GL11.GL_FOG_DENSITY, 0.2F);
            GL11.glFog(GL11.GL_FOG_COLOR, this.getBuffer(0.5F, 0.3F, 0.0F, 1.0F));
            GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(0.4F, 0.3F, 0.3F, 1.0F));
        } else if(var1 == 0) {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            GL11.glFogf(GL11.GL_FOG_DENSITY, 0.001F);
            GL11.glFog(GL11.GL_FOG_COLOR, this.fogColor0);
            GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(1.0F, 1.0F, 1.0F, 1.0F));
        } else if(var1 == 1) {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            GL11.glFogf(GL11.GL_FOG_DENSITY, 0.01F);
            GL11.glFog(GL11.GL_FOG_COLOR, this.fogColor1);
            float var3 = 0.6F;
            GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(var3, var3, var3, 1.0F));
        }

        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private FloatBuffer getBuffer(float var1, float var2, float var3, float var4) {
        this.lb.clear();
        this.lb.put(var1).put(var2).put(var3).put(1.0F);
        this.lb.flip();
        return this.lb;
    }

    public final void showLoadingScreen(String var1, String var2) {
        int var3 = scaledResolution.getScaledWidth();
        int var4 = scaledResolution.getScaledHeight();
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
        Tesselator var5 = Tesselator.tesselator;
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        int var6 = this.textureManager.loadTexture("/dirt.png", 9728);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var6);
        var5.begin(DefaultVertexFormats.POSITION_TEX_COLOR);
        var5.color(8421504);
        float var8 = 32.0F;
        var5.vertexUV(0.0F, (float)var4, 0.0F, 0.0F, (float)var4 / var8);
        var5.vertexUV((float)var3, (float)var4, 0.0F, (float)var3 / var8, (float)var4 / var8);
        var5.vertexUV((float)var3, 0.0F, 0.0F, (float)var3 / var8, 0.0F);
        var5.vertexUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        var5.end();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        this.font.drawShadow(var1, (var3 - this.font.getWidth(var1)) / 2, var4 / 2 - 4 - 8, 16777215);
        this.font.drawShadow(var2, (var3 - this.font.getWidth(var2)) / 2, var4 / 2 - 4 + 4, 16777215);
        Display.update();

        try {
            Thread.sleep(200L);
        } catch (Exception var7) {
        }
    }

	public static void main(String[] args) throws LWJGLException {
        PlatformRuntime.setThreadName("Client thread");
        (minecraft = new Minecraft(854, 480, false)).run();
	}
}
