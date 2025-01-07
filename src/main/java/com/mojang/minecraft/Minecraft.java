package com.mojang.minecraft;

import com.mojang.minecraft.character.Vec3;
import com.mojang.minecraft.character.Zombie;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.level.Chunk;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelRenderer;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Frustum;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;
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

public class Minecraft implements Runnable {
    public static final String VERSION_STRING = "0.0.11a";
    private boolean fullscreen = false;
	private int width;
	private int height;
	private FloatBuffer fogColor0 = BufferUtils.createFloatBuffer(4);
	private FloatBuffer fogColor1 = BufferUtils.createFloatBuffer(4);
	private Timer timer = new Timer(20.0F);
	public Level level;
	private LevelRenderer levelRenderer;
	public Player player;
	private int paintTexture = 1;
	private ParticleEngine particleEngine;
    private ArrayList<Entity> entities = new ArrayList();
    public boolean appletMode = false;
    public volatile boolean pause = false;
    private int yMouseAxis = 1;
    public Textures textures;
    private Font font;
    public int editMode = 0;
    private volatile boolean running = false;
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
	FloatBuffer lb = BufferUtils.createFloatBuffer(16);

    public Minecraft(int width, int height, boolean fullscreen) {
        this.width = width;
        this.height = height;
        this.fullscreen = fullscreen;
        this.textures = new Textures();
    }

	public void init() throws LWJGLException, IOException {
		int col0 = 16710650;
		int col1 = 920330;
		float fr = 0.5F;
		float fg = 0.8F;
		float fb = 1.0F;
		this.fogColor0.put(new float[]{(float)(col0 >> 16 & 255) / 255.0F, (float)(col0 >> 8 & 255) / 255.0F, (float)(col0 & 255) / 255.0F, 1.0F});
		this.fogColor0.flip();
		this.fogColor1.put(new float[]{(float)(col1 >> 16 & 255) / 255.0F, (float)(col1 >> 8 & 255) / 255.0F, (float)(col1 & 255) / 255.0F, 1.0F});
		this.fogColor1.flip();
		if(this.fullscreen) {
            Display.setFullscreen(true);
            this.width = Display.getDisplayMode().getWidth();
            this.height = Display.getDisplayMode().getHeight();
        } else {
            Display.setDisplayMode(new DisplayMode(this.width, this.height));
        }

        Display.setTitle("Minecraft 0.0.11a");
        try {
            Display.create();
        } catch (Exception var10) {
            var10.printStackTrace();

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException var9) {
            }

            Display.create();
        }

		Keyboard.create();
		Mouse.create();
        this.checkGlError("Pre startup");

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(fr, fg, fb, 0.0F);
		GL11.glClearDepth(1.0D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
        this.checkGlError("Startup");
		this.level = new Level(256, 256, 64);
        this.levelRenderer = new LevelRenderer(this.level, this.textures);
		this.player = new Player(this.level);
        this.particleEngine = new ParticleEngine(this.level, this.textures);
        this.font = new Font("/default.gif", this.textures);
        this.mouseGrabSupported = Mouse.isMouseGrabSupported();
        touchOverlayRenderer = new TouchOverlayRenderer();
        scaledResolution = new ScaledResolution(width, height);
        PointerInputAbstraction.init(this);

        for(int imgData = 0; imgData < 10; ++imgData) {
            Zombie e = new Zombie(this.level, this.textures, 128.0F, 0.0F, 128.0F);
            e.resetPos();
            this.entities.add(e);
        }

        this.checkGlError("Post startup");
    }

    private void checkGlError(String string) {
        int errorCode = GL11.glGetError();
        if(errorCode != 0) {
            String errorString = GLU.gluErrorString(errorCode);
            System.out.println("########## GL ERROR ##########");
            System.out.println("@ " + string);
            System.out.println(errorCode + ": " + errorString);
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

	public void run() {
        this.running = true;

		try {
			this.init();
        } catch (Exception var9) {
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

                    width = Display.getVisualViewportW();
                    height = Display.getVisualViewportH();
                    scaledResolution = new ScaledResolution(width, height);
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
    }

    public void releaseMouse() {
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
                    this.level.tick();
                    this.particleEngine.tick();

                    for(int i = 0; i < this.entities.size(); ++i) {
                        ((Entity)this.entities.get(i)).tick();
                        if(((Entity)this.entities.get(i)).removed) {
                            this.entities.remove(i--);
                        }
                    }

                    this.player.tick();
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
                this.yMouseAxis *= -1;
            }

			if(Keyboard.getEventKey() == Keyboard.KEY_G) {
			    this.addZombie();
            }
        }
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
		GLU.gluPerspective(70.0F, (float)this.width / (float)this.height, 0.05F, 1000.0F);
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
        this.player.turn(frustum, i * (float)this.yMouseAxis);

        this.checkGlError("Set viewport");
		this.pick(a);
        this.checkGlError("Picked");

		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
		this.setupCamera(a);
        this.checkGlError("Set up camera");
		GL11.glEnable(GL11.GL_CULL_FACE);
        Frustum var5 = Frustum.getFrustum();
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
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_FOG);
        this.checkGlError("Rendered rest");
		if(this.hitResult != null) {
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            this.levelRenderer.renderHit(this.hitResult, this.editMode, this.paintTexture);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
		}

        this.checkGlError("Rendered hit");
		this.drawGui(a);
        this.checkGlError("Rendered gui");
	}

    public void addZombie() {
        this.entities.add(new Zombie(this.level, this.textures, this.player.x, this.player.y, this.player.z));
    }

	private void drawGui(float a) {
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		this.setupOrthoCamera();
        this.checkGlError("GUI: Init");
		GL11.glPushMatrix();
		GL11.glTranslatef(32.0F, 64.0F, 0.0F);
		Tesselator t = Tesselator.instance;
        GL11.glScalef(32.0F, 32.0F, 32.0F);
		GL11.glRotatef(30.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-1.5F, 0.5F, -0.5F);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        int id = this.textures.loadTexture("/terrain.png", 9728);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		t.init(DefaultVertexFormats.POSITION_TEX_COLOR);
		Tile.tiles[this.paintTexture].render(t, this.level, 0, -2, 0, 0);
		t.flush();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
        this.checkGlError("GUI: Draw selected");
        this.font.drawShadow("0.0.11a", 2, 2, 16777215);
        this.font.drawShadow(this.fpsString, 2, 12, 16777215);
        this.checkGlError("GUI: Draw text");
		int wc = (int)scaledResolution.getScaledWidth_double() / 2;
		int hc = (int)scaledResolution.getScaledHeight_double() / 2;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		t.init(DefaultVertexFormats.POSITION);
        t.vertex((float)(wc + 1), (float)(hc - 4), 0.0F);
        t.vertex((float)(wc - 0), (float)(hc - 4), 0.0F);
        t.vertex((float)(wc - 0), (float)(hc + 5), 0.0F);
        t.vertex((float)(wc + 1), (float)(hc + 5), 0.0F);
        t.vertex((float)(wc + 5), (float)(hc - 0), 0.0F);
        t.vertex((float)(wc - 4), (float)(hc - 0), 0.0F);
        t.vertex((float)(wc - 4), (float)(hc + 1), 0.0F);
        t.vertex((float)(wc + 5), (float)(hc + 1), 0.0F);
		t.flush();
        this.checkGlError("GUI: Draw crosshair");
	}

	private void setupFog(int i) {
		if(i == 0) {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
			GL11.glFogf(GL11.GL_FOG_DENSITY, 0.001F);
			GL11.glFog(GL11.GL_FOG_COLOR, this.fogColor0);
			GL11.glDisable(GL11.GL_LIGHTING);
		} else if(i == 1) {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            GL11.glFogf(GL11.GL_FOG_DENSITY, 0.01F);
			GL11.glFog(GL11.GL_FOG_COLOR, this.fogColor1);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			float br = 0.6F;
			GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(br, br, br, 1.0F));
		}

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

	public static void main(String[] args) throws LWJGLException {
        PlatformRuntime.setThreadName("Client thread");
        (minecraft = new Minecraft(854, 480, false)).run();
	}
}
