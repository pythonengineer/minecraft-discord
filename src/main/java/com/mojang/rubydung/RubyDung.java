package com.mojang.rubydung;

import com.mojang.rubydung.character.Cube;
import com.mojang.rubydung.character.Vec3;
import com.mojang.rubydung.character.Zombie;
import com.mojang.rubydung.level.Chunk;
import com.mojang.rubydung.level.Level;
import com.mojang.rubydung.level.LevelRenderer;
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
import net.lax1dude.eaglercraft.touch.TouchControls;
import net.lax1dude.eaglercraft.touch.TouchOverlayRenderer;
import net.lax1dude.eaglercraft.util.ReportedException;

public class RubyDung implements Runnable {
	private static final boolean FULLSCREEN_MODE = false;
	private int width;
	private int height;
	private FloatBuffer fogColor = BufferUtils.createFloatBuffer(4);
	private Timer timer = new Timer(60.0F);
	public Level level;
	private LevelRenderer levelRenderer;
	public Player player;
	private ArrayList<Zombie> zombies = new ArrayList();
	private HitResult hitResult = null;
	public boolean mouseGrabSupported = false;
	public static ScaledResolution scaledResolution;
	public static TouchOverlayRenderer touchOverlayRenderer;
	public static RubyDung rubydung;
	private int startX = 0;
	private int startY = 0;

	public void init() throws LWJGLException, IOException {
		int col = 920330;
		float fr = 0.5F;
		float fg = 0.8F;
		float fb = 1.0F;
		this.fogColor.put(new float[]{(float)(col >> 16 & 255) / 255.0F, (float)(col >> 8 & 255) / 255.0F, (float)(col & 255) / 255.0F, 1.0F});
		this.fogColor.flip();
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
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		this.level = new Level(256, 256, 64);
		this.levelRenderer = new LevelRenderer(this.level);
		this.player = new Player(this.level);
		this.mouseGrabSupported = Mouse.isMouseGrabSupported();
		touchOverlayRenderer = new TouchOverlayRenderer();
		scaledResolution = new ScaledResolution(width, height);
		PointerInputAbstraction.init(this);

		for(int i = 0; i < 100; ++i) {
			this.zombies.add(new Zombie(this.level, 128.0F, 0.0F, 128.0F));
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
                    GL11.glMatrixMode(GL11.GL_PROJECTION);
                    GL11.glLoadIdentity();
                    GL11.glOrtho(0.0D, scaledResolution.getScaledWidth_double(),
                            scaledResolution.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);
                    GL11.glLoadIdentity();
                    GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
                    touchOverlayRenderer.render(width, height, scaledResolution);
                    GL11.disableBlend();
                    GL11.disableAlpha();
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
		for(int i = 0; i < this.zombies.size(); ++i) {
			((Zombie)this.zombies.get(i)).tick();
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

    private void pick(float a) {
        float f65 = this.player.xRotO + (this.player.xRot - this.player.xRotO) * a;
        float f72 = this.player.yRotO + (this.player.yRot - this.player.yRotO) * a;
        float f78 = this.player.xo + (this.player.x - this.player.xo) * a;
        float f57 = this.player.yo + (this.player.y - this.player.yo) * a;
        float f55 = this.player.zo + (this.player.z - this.player.zo) * a;
        Vec3 vec359 = new Vec3(f78, f57, f55);
        f55 = (float) Math.cos((double) (-f72) * Math.PI / 180.0D + Math.PI);
        float f66 = (float) Math.sin((double) (-f72) * Math.PI / 180.0D + Math.PI);
        f72 = (float) Math.cos((double) (-f65) * Math.PI / 180.0D);
        f65 = (float) Math.sin((double) (-f65) * Math.PI / 180.0D);
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
        float xo = (float) PointerInputAbstraction.getDX();
        float yo = (float) PointerInputAbstraction.getDY();
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

		while(Keyboard.next()) {
			if(Keyboard.getEventKey() == Keyboard.KEY_RETURN && Keyboard.getEventKeyState()) {
				this.level.save();
			}
		}

		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
		this.setupCamera(a);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_FOG);
		GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
		GL11.glFogf(GL11.GL_FOG_DENSITY, 0.2F);
		GL11.glFog(GL11.GL_FOG_COLOR, this.fogColor);
		GL11.glDisable(GL11.GL_FOG);
		this.levelRenderer.render(this.player, 0);

		for(int i = 0; i < this.zombies.size(); ++i) {
			((Zombie)this.zombies.get(i)).render(a);
		}

		GL11.glEnable(GL11.GL_FOG);
		this.levelRenderer.render(this.player, 1);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		if(this.hitResult != null) {
			this.levelRenderer.renderHit(this.hitResult);
		}

		new Cube(0, 0);
		GL11.glDisable(GL11.GL_FOG);
	}

    public void doPick(boolean pick) {
        if (pick) {
            this.level.setTile(this.hitResult.x, this.hitResult.y, this.hitResult.z, 0);
        } else {
            int x = this.hitResult.x;
            int y = this.hitResult.y;
            int z = this.hitResult.z;
            if (this.hitResult.f == 0) {
                --y;
            }

            if (this.hitResult.f == 1) {
                ++y;
            }

            if (this.hitResult.f == 2) {
                --z;
            }

            if (this.hitResult.f == 3) {
                ++z;
            }

            if (this.hitResult.f == 4) {
                --x;
            }

            if (this.hitResult.f == 5) {
                ++x;
            }

            this.level.setTile(x, y, z, 1);
        }
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
