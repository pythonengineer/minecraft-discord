package com.mojang.minecraft;

import com.mojang.minecraft.character.Vec3;
import com.mojang.minecraft.character.Zombie;
import com.mojang.minecraft.level.Chunk;
import com.mojang.minecraft.level.Frustum;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelRenderer;
import com.mojang.minecraft.level.Tesselator;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.particle.ParticleEngine;
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

public class RubyDung implements Runnable {
	private static final boolean FULLSCREEN_MODE = false;
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
	private ArrayList<Zombie> zombies = new ArrayList();
	private HitResult hitResult = null;
    public boolean mouseGrabSupported = false;
    public static ScaledResolution scaledResolution;
    public static TouchOverlayRenderer touchOverlayRenderer;
    public static RubyDung rubydung;
    private int startX = 0;
    private int startY = 0;
	FloatBuffer lb = BufferUtils.createFloatBuffer(16);

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
		Display.setDisplayMode(new DisplayMode(1024, 768));
		Display.create();
		Keyboard.create();
		Mouse.create();
		this.width = Display.getDisplayMode().getWidth();
		this.height = Display.getDisplayMode().getHeight();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(fr, fg, fb, 0.0F);
		GL11.glClearDepth(1.0D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		this.level = new Level(256, 256, 64);
		this.levelRenderer = new LevelRenderer(this.level);
		this.player = new Player(this.level);
		this.particleEngine = new ParticleEngine(this.level);
        this.mouseGrabSupported = Mouse.isMouseGrabSupported();
        touchOverlayRenderer = new TouchOverlayRenderer();
        scaledResolution = new ScaledResolution(width, height);
        PointerInputAbstraction.init(this);

		for(int i = 0; i < 10; ++i) {
			Zombie zombie = new Zombie(this.level, 128.0F, 0.0F, 128.0F);
			zombie.resetPos();
			this.zombies.add(zombie);
		}

	}

	public void destroy() {
		this.level.save();
		Mouse.destroy();
		Keyboard.destroy();
		Display.destroy();
	}

	public void run() {
		try {
			this.init();
        } catch (Exception var9) {
            EagRuntime.showPopup("Failed to start RubyDung");
            EagRuntime.exit();
		}

		long lastTime = EagRuntime.currentTimeMillis();
		int frames = 0;

		try {
			while(!Display.isCloseRequested()) {
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
                    this.render(this.timer.a);
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
					System.out.println(frames + " fps, " + Chunk.updates);
					Chunk.updates = 0;
					lastTime += 1000L;
					frames = 0;
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

	public void tick() {
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
					this.level.save();
				}

                if(Keyboard.getEventKey() == Keyboard.KEY_BACK) {
                    this.level.delete();
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

				if(Keyboard.getEventKey() == Keyboard.KEY_G) {
				    this.addZombie();
				}
			}
		}

		this.level.tick();
		this.particleEngine.tick();

		for(int i = 0; i < this.zombies.size(); ++i) {
			((Zombie)this.zombies.get(i)).tick();
			if(((Zombie)this.zombies.get(i)).removed) {
				this.zombies.remove(i--);
			}
		}

		this.player.tick();
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
        float xo = (float)PointerInputAbstraction.getDX();
        float yo = (float)PointerInputAbstraction.getDY();
		this.player.turn(xo, yo);
		this.pick(a);

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
                                    (scaledY < (scaledResolution.getScaledHeight_double() - 6) &&
                                     scaledY >= (scaledResolution.getScaledHeight_double() - 57))) {
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
                                    if (this.hitResult != null) {
                                        if (TouchControls.getPickToggled()) {
                                            this.doPick(true);
                                        } else {
                                            this.doPick(false);
                                        }
                                    }
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

                if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState() && this.hitResult != null) {
                    this.doPick(true);
                }

                if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && this.hitResult != null) {
                    this.doPick(false);
                }
            }

            if (!(touch || Mouse.isActuallyGrabbed()) && (touch || Mouse.getEventButtonState())) {
                Mouse.setGrabbed(true);
            }
        }

		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
		this.setupCamera(a);
		GL11.glEnable(GL11.GL_CULL_FACE);
		Frustum var9 = Frustum.getFrustum();
		this.levelRenderer.updateDirtyChunks(this.player);
		this.setupFog(0);
		GL11.glEnable(GL11.GL_FOG);
		this.levelRenderer.render(this.player, 0);

		int var8;
		Zombie var10;
		for(var8 = 0; var8 < this.zombies.size(); ++var8) {
			var10 = (Zombie)this.zombies.get(var8);
			if(var10.isLit() && var9.isVisible(var10.bb)) {
				((Zombie)this.zombies.get(var8)).render(a);
			}
		}

		this.particleEngine.render(this.player, a, 0);
		this.setupFog(1);
		this.levelRenderer.render(this.player, 1);

		for(var8 = 0; var8 < this.zombies.size(); ++var8) {
			var10 = (Zombie)this.zombies.get(var8);
			if(!var10.isLit() && var9.isVisible(var10.bb)) {
				((Zombie)this.zombies.get(var8)).render(a);
			}
		}

		this.particleEngine.render(this.player, a, 1);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_FOG);
		if(this.hitResult != null) {
            GL11.glDisable(GL11.GL_ALPHA_TEST);
			this.levelRenderer.renderHit(this.hitResult);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
		}

		this.drawGui(a);
	}

    public void doPick(boolean pick) {
        if (pick) {
            Tile frustum = Tile.tiles[this.level.getTile(this.hitResult.x, this.hitResult.y, this.hitResult.z)];
            boolean i = this.level.setTile(this.hitResult.x, this.hitResult.y, this.hitResult.z, 0);
            if(frustum != null && i) {
                frustum.destroy(this.level, this.hitResult.x, this.hitResult.y, this.hitResult.z, this.particleEngine);
            }
        } else {
            int var7 = this.hitResult.x;
            int var8 = this.hitResult.y;
            int zombie = this.hitResult.z;
            if(this.hitResult.f == 0) {
                --var8;
            }

            if(this.hitResult.f == 1) {
                ++var8;
            }

            if(this.hitResult.f == 2) {
                --zombie;
            }

            if(this.hitResult.f == 3) {
                ++zombie;
            }

            if(this.hitResult.f == 4) {
                --var7;
            }

            if(this.hitResult.f == 5) {
                ++var7;
            }

            this.level.setTile(var7, var8, zombie, this.paintTexture);
        }
    }

    public void addZombie() {
        this.zombies.add(new Zombie(this.level, this.player.x, this.player.y, this.player.z));
    }

	private void drawGui(float a) {
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		this.setupOrthoCamera();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPushMatrix();
		GL11.glTranslatef(32.0F, 32.0F, 0.0F);
		Tesselator t = Tesselator.instance;
        GL11.glScalef(32.0F, 32.0F, 32.0F);
		GL11.glRotatef(30.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-1.5F, 0.5F, -0.5F);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
		int id = Textures.loadTexture("/terrain.png", 9728);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		t.init(DefaultVertexFormats.POSITION_TEX_COLOR);
		Tile.tiles[this.paintTexture].render(t, this.level, 0, -2, 0, 0);
		t.flush();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
		int wc = (int)scaledResolution.getScaledWidth_double() / 2;
		int hc = (int)scaledResolution.getScaledHeight_double() / 2;
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
	}

	private void setupFog(int i) {
		if(i == 0) {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
			GL11.glFogf(GL11.GL_FOG_DENSITY, 0.001F);
			GL11.glFog(GL11.GL_FOG_COLOR, this.fogColor0);
			GL11.glDisable(GL11.GL_LIGHTING);
		} else if(i == 1) {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
			GL11.glFogf(GL11.GL_FOG_DENSITY, 0.06F);
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
        (rubydung = new RubyDung()).run();
	}
}
