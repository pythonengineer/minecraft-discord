package net.minecraft.client;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.ScaledResolution;
import net.lax1dude.eaglercraft.Touch;
import net.lax1dude.eaglercraft.crash.CrashReport;
import net.lax1dude.eaglercraft.internal.EnumPlatformType;
import net.lax1dude.eaglercraft.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
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
import net.lax1dude.eaglercraft.util.MathHelper;
import net.lax1dude.eaglercraft.util.ReportedException;
import net.minecraft.client.controller.PlayerController;
import net.minecraft.client.controller.PlayerControllerCreative;
import net.minecraft.client.controller.PlayerControllerSP;
import net.minecraft.client.effect.EffectRenderer;
import net.minecraft.client.effect.EntityDiggingFX;
import net.minecraft.client.effect.EntityFX;
import net.minecraft.client.effect.EntityRainFX;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.player.MovementInputFromKeys;
import net.minecraft.client.render.ClippingHelper;
import net.minecraft.client.render.ClippingHelperImplementation;
import net.minecraft.client.render.EntityRenderer;
import net.minecraft.client.render.ItemRenderer;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.texture.TextureFX;
import net.minecraft.client.render.texture.TextureLavaFX;
import net.minecraft.client.render.texture.TextureWaterFX;
import net.minecraft.client.render.texture.TextureWaterFlowFX;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.entity.player.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.generator.LevelGenerator;
import net.minecraft.game.level.generator.noise.NoiseGeneratorDistort;
import net.minecraft.game.level.generator.noise.NoiseGeneratorOctaves;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;

public final class Minecraft implements Runnable {
	public PlayerController playerController = new PlayerControllerSP(this);
	private boolean fullScreen = false;
	public int displayWidth;
	public int displayHeight;
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
	private LoadingScreenRenderer loadingScreen = new LoadingScreenRenderer(this);
	public EntityRenderer entityRenderer = new EntityRenderer(this);
	private int ticksRan = 0;
	private int leftClickCounter = 0;
	public GuiIngame ingameGUI;
	public MovingObjectPosition objectMouseOver;
	public GameSettings options;
	String serverIp;
    volatile boolean running;
	public String debug;
	private boolean inventoryScreen;
	private int prevFrameTime;
	public boolean thirdPersonView;
    public boolean mouseGrabSupported = false;
    private int rightClickDelayTimer;
    public float displayDPI = 1.0f;
    public ScaledResolution scaledResolution;
    public TouchOverlayRenderer touchOverlayRenderer;
    public static Minecraft minecraft;

	public Minecraft(int width, int height, boolean fullScreen) {
		new ModelBiped(0.0F);
		this.objectMouseOver = null;
		this.serverIp = null;
		this.running = false;
		this.debug = "";
		this.inventoryScreen = false;
		this.prevFrameTime = 0;
		this.thirdPersonView = false;
		this.displayWidth = width;
		this.displayHeight = height;
		this.fullScreen = fullScreen;
        this.options = new GameSettings(this);
	}

	public final void displayGuiScreen(GuiScreen var1) {
		if(!(this.currentScreen instanceof GuiErrorScreen)) {
			if(this.currentScreen != null) {
                this.currentScreen.onGuiClosed();
			}

			if(var1 == null && this.thePlayer.health <= 0) {
				var1 = new GuiGameOver();
			}

			this.currentScreen = (GuiScreen)var1;
			if(var1 != null) {
				if(this.inventoryScreen) {
                    this.thePlayer.movementInput.resetKeyState();
					this.inventoryScreen = false;
					Mouse.setGrabbed(false);
				}

				((GuiScreen)var1).setWorldAndResolution(this,
				        this.scaledResolution.getScaledWidth(), this.scaledResolution.getScaledHeight());
			} else {
				this.setIngameFocus();
			}
		}
	}

	private static void checkGLError(String var0) {
		int var1 = GL11.glGetError();
		if(var1 != 0) {
			String var2 = GLU.gluErrorString(var1);
			System.out.println("########## GL ERROR ##########");
			System.out.println("@ " + var0);
			System.out.println(var1 + ": " + var2);
		}

	}

	public final void shutdownMinecraftApplet() {
		Mouse.destroy();
		Keyboard.destroy();
		Display.destroy();
	}

	public final void run() {
		this.running = true;

		try {
			Minecraft var4 = this;
	        this.displayWidth = Display.getVisualViewportW() != 0 ? Display.getVisualViewportW() : this.displayWidth;
	        this.displayHeight = Display.getVisualViewportH() != 0 ? Display.getVisualViewportH() : this.displayHeight;
			if(this.fullScreen) {
				Display.setFullscreen(true);
				this.displayWidth = Display.getDisplayMode().getWidth();
				this.displayHeight = Display.getDisplayMode().getHeight();
			} else {
				Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
			}

	        this.displayDPI = Math.max(Math.min(Display.getDPI(), 2.0f), 1.0f);

			Display.setTitle("Minecraft 0.31");

			try {
				Display.create();
			} catch (Exception var31) {
				var31.printStackTrace();

				try {
					Thread.sleep(1000L);
				} catch (InterruptedException var30) {
				}

				Display.create();
			}

			Keyboard.create();
			Mouse.create();
			Display.update();

			checkGLError("Pre startup");
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
			checkGLError("Startup");

			this.renderEngine = new RenderEngine(this.options);
			this.renderEngine.registerTextureFX(new TextureLavaFX());
			this.renderEngine.registerTextureFX(new TextureWaterFX());
			this.renderEngine.registerTextureFX(new TextureWaterFlowFX());
			this.fontRenderer = new FontRenderer(this.options, "/default.png", this.renderEngine);
			IntBuffer var8 = BufferUtils.createIntBuffer(256);
			var8.clear().limit(256);
			this.renderGlobal = new RenderGlobal(this, this.renderEngine);
			GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);

            this.mouseGrabSupported = Mouse.isMouseGrabSupported();
            this.touchOverlayRenderer = new TouchOverlayRenderer();
            this.scaledResolution = new ScaledResolution(this);
            PointerInputAbstraction.init(this);

			if(this.serverIp != null && this.session != null) {
				World var43 = new World();
				var43.generate(8, 8, 8, new byte[512]);
				this.setLevel(var43);
			} else {
				if(this.theWorld == null) {
					this.generateNewLevel(0);
				}
			}

			this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);

			checkGLError("Post startup");
			this.ingameGUI = new GuiIngame(this, this.displayWidth, this.displayHeight);
			(new ThreadDownloadSkin(this)).start();
		} catch (Exception var36) {
			var36.printStackTrace();
            EagRuntime.showPopup("Failed to start Minecraft");
			return;
		}

		long var1 = EagRuntime.currentTimeMillis();
		int var3 = 0;

		try {
			while(this.running) {
				if(this.isGamePaused) {
					Thread.sleep(100L);
				} else {
					if(Display.isCloseRequested()) {
						this.running = false;
					}

					try {
						Timer var37 = this.timer;
						long var40 = EagRuntime.currentTimeMillis();
						long var44 = var40 - var37.lastSyncSysClock;
						long var47 = EagRuntime.nanoTime() / 1000000L;
						double var54;
						if(var44 > 1000L) {
							long var51 = var47 - var37.lastSyncHRClock;
							var54 = (double)var44 / (double)var51;
							var37.timeSyncAdjustment += (var54 - var37.timeSyncAdjustment) * (double)0.2F;
							var37.lastSyncSysClock = var40;
							var37.lastSyncHRClock = var47;
						}

						if(var44 < 0L) {
							var37.lastSyncSysClock = var40;
							var37.lastSyncHRClock = var47;
						}

						double var52 = (double)var47 / 1000.0D;
						var54 = (var52 - var37.lastHRTime) * var37.timeSyncAdjustment;
						var37.lastHRTime = var52;
						if(var54 < 0.0D) {
							var54 = 0.0D;
						}

						if(var54 > 1.0D) {
							var54 = 1.0D;
						}

						var37.elapsedPartialTicks = (float)((double)var37.elapsedPartialTicks + var54 * (double)var37.timerSpeed * (double)var37.ticksPerSecond);
						var37.elapsedTicks = (int)var37.elapsedPartialTicks;
						if(var37.elapsedTicks > 100) {
							var37.elapsedTicks = 100;
						}

						var37.elapsedPartialTicks -= (float)var37.elapsedTicks;
						var37.renderPartialTicks = var37.elapsedPartialTicks;

                        PointerInputAbstraction.runGameLoop();

						for(int var38 = 0; var38 < this.timer.elapsedTicks; ++var38) {
							++this.ticksRan;
							this.runTick();
                            if (var38 < var37.elapsedTicks - 1) {
                                PointerInputAbstraction.runGameLoop();
                            }
						}

                        if (!Display.contextLost()) {
                            GL11.optimize();
    						checkGLError("Pre render");
    						GL11.glEnable(GL11.GL_TEXTURE_2D);
    						this.playerController.setPartialTime(this.timer.renderPartialTicks);
    						float var41 = this.timer.renderPartialTicks;
    						EntityRenderer var39 = this.entityRenderer;
    						if(var39.displayActive && !Display.isActive()) {
    							var39.mc.displayInGameMenu();
    						}
    
    						var39.displayActive = Display.isActive();
    						int var42;
    						int var45;
    						int var49;
    						if(var39.mc.inventoryScreen) {
    							var42 = 0;
    							var45 = 0;
    							var42 = PointerInputAbstraction.getDX();
    							var45 = PointerInputAbstraction.getDY();
    
    							byte var46 = 1;
    							if(var39.mc.options.invertMouse) {
    								var46 = -1;
    							}
    
    							float var10001 = (float)var42;
    							float var56 = (float)(var45 * var46);
    							float var57 = var10001;
    							EntityPlayerSP var55 = var39.mc.thePlayer;
    							float var5 = var55.rotationPitch;
    							float var6 = var55.rotationYaw;
    							var55.rotationYaw = (float)((double)var55.rotationYaw + (double)var57 * 0.15D);
    							var55.rotationPitch = (float)((double)var55.rotationPitch - (double)var56 * 0.15D);
    							if(var55.rotationPitch < -90.0F) {
    								var55.rotationPitch = -90.0F;
    							}
    
    							if(var55.rotationPitch > 90.0F) {
    								var55.rotationPitch = 90.0F;
    							}
    
    							var55.prevRotationPitch += var55.rotationPitch - var5;
    							var55.prevRotationYaw += var55.rotationYaw - var6;
    						}
    
    						var42 = this.scaledResolution.getScaledWidth();
    						var45 = this.scaledResolution.getScaledHeight();
                            int var48 = PointerInputAbstraction.getX() * var42 / var39.mc.displayWidth;
                            var49 = var45 - PointerInputAbstraction.getY() * var45 / var39.mc.displayHeight - 1;
    						if(var39.mc.theWorld != null) {
    							var39.updateCameraAndRender(var41);
                                var39.mc.ingameGUI.renderGameOverlay();
    						} else {
    							GL11.glViewport(0, 0, var39.mc.displayWidth, var39.mc.displayHeight);
    							GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
    							GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
    							GL11.glMatrixMode(GL11.GL_PROJECTION);
    							GL11.glLoadIdentity();
    							GL11.glMatrixMode(GL11.GL_MODELVIEW);
    							GL11.glLoadIdentity();
    							var39.setupOverlayRendering();
    						}
    
    						if(var39.mc.currentScreen != null) {
                                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                                float f = 1.0f;
                                final float[] ff = new float[]{1.0f};
                                f = this.currentScreen.getEaglerScale();
                                int mx, my;
                                if (f == 1.0f) {
                                    mx = var48;
                                    my = var49;
                                } else {
                                    mx = GuiScreen.applyEaglerScale(f, var48, var42);
                                    my = GuiScreen.applyEaglerScale(f, var49, var45);
                                    GL11.glPushMatrix();
                                    float fff = (1.0f - f) * 0.5f;
                                    GL11.glTranslatef(fff * var42, fff * var45, 0.0f);
                                    GL11.glScalef(f, f, f);
                                }
                                ff[0] = f;
                                this.currentScreen.drawScreen(mx, my);
                                if (f != 1.0f) {
                                    GL11.glPopMatrix();
                                }
    						}

                            var39.setupOverlayRendering();
                            touchOverlayRenderer.render(this.displayWidth, this.displayHeight, this.scaledResolution);
                            GL11.disableBlend();
                        }

						Thread.yield();
                        this.updateDisplay();
						if(this.options.limitFramerate) {
							Thread.sleep(5L);
						}

						checkGLError("Post render");
						++var3;
					} catch (Exception var32) {
						this.displayGuiScreen(new GuiErrorScreen("Client error", "The game broke! [" + var32 + "]"));
						var32.printStackTrace();
					}

					while(EagRuntime.currentTimeMillis() >= var1 + 1000L) {
						this.debug = var3 + " fps, " + WorldRenderer.chunksUpdated + " chunk updates";
						WorldRenderer.chunksUpdated = 0;
						var1 += 1000L;
						var3 = 0;
					}
				}
			}

			return;
		} catch (MinecraftError var33) {
			return;
        } catch (ReportedException reportedexception) {
            this.displayCrashReport(reportedexception.getCrashReport());
        } catch (Throwable throwable1) {
            CrashReport crashreport1 = new CrashReport("Unexpected error", throwable1);
            this.displayCrashReport(crashreport1);
		} finally {
			this.shutdownMinecraftApplet();
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
        return this.theWorld == null && this.currentScreen != null ? 30 : 260;
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
        if (!this.fullScreen
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

    private void resize(int width, int height) {
        this.displayWidth = Math.max(1, width);
        this.displayHeight = Math.max(1, height);
        this.scaledResolution = new ScaledResolution(this);
        if (this.currentScreen != null) {
            this.currentScreen.setWorldAndResolution(this, 
                    this.scaledResolution.getScaledWidth(), this.scaledResolution.getScaledHeight());
        }
    }

	public final void setIngameFocus() {
        boolean touch = PointerInputAbstraction.isTouchMode();
        if (touch || Display.isActive()) {
            if (!this.inventoryScreen) {
                this.inventoryScreen = true;
                if (!touch && mouseGrabSupported) {
                    Mouse.setGrabbed(true);
                }
                this.displayGuiScreen((GuiScreen) null);
                this.prevFrameTime = this.ticksRan + 10000;
            }
        }
	}

    public void setIngameNotInFocus() {
        if (this.inventoryScreen) {
            this.thePlayer.movementInput.resetKeyState();
            this.inventoryScreen = false;
            if (!PointerInputAbstraction.isTouchMode() && mouseGrabSupported) {
                Mouse.setGrabbed(false);
            }
        }
    }

	public void displayInGameMenu() {
		if(this.currentScreen == null) {
			this.displayGuiScreen(new GuiIngameMenu());
		}
	}

    private void clickMouse(int var1) {
        if(var1 != 0 || this.leftClickCounter <= 0) {
            if(var1 == 0) {
                ItemRenderer var5 = this.entityRenderer.itemRenderer;
                var5.swingProgress = -1;
                var5.itemSwingState = true;
            } else {
                this.rightClickDelayTimer = 4;
            }

            Object var2 = null;
            if(this.objectMouseOver == null) {
                if(var1 == 0 && !(this.playerController instanceof PlayerControllerCreative)) {
                    this.leftClickCounter = 10;
                }

            } else {
                if(this.objectMouseOver.typeOfHit == 1) {
                    if(var1 == 0) {
                        this.objectMouseOver.entityHit.attackEntityFrom(this.thePlayer, 4);
                        return;
                    }
                } else if(this.objectMouseOver.typeOfHit == 0) {
                    int var10 = this.objectMouseOver.blockX;
                    int var3 = this.objectMouseOver.blockY;
                    int var4 = this.objectMouseOver.blockZ;
                    if(var1 != 0) {
                        if(this.objectMouseOver.sideHit == 0) {
                            --var3;
                        }

                        if(this.objectMouseOver.sideHit == 1) {
                            ++var3;
                        }

                        if(this.objectMouseOver.sideHit == 2) {
                            --var4;
                        }

                        if(this.objectMouseOver.sideHit == 3) {
                            ++var4;
                        }

                        if(this.objectMouseOver.sideHit == 4) {
                            --var10;
                        }

                        if(this.objectMouseOver.sideHit == 5) {
                            ++var10;
                        }
                    }

                    Block var11 = Block.blocksList[this.theWorld.getBlockId(var10, var3, var4)];
                    if(var1 == 0) {
                        if(var11 != Block.bedrock) {
                            this.playerController.clickBlock(var10, var3, var4);
                            return;
                        }
                    } else {
                        ItemStack var9 = this.thePlayer.inventory.getCurrentItem();
                        if(var9 == null) {
                            return;
                        }

                        var11 = Block.blocksList[this.theWorld.getBlockId(var10, var3, var4)];
                        if(var9.itemID > 0 && var11 == null || var11 == Block.waterMoving || var11 == Block.waterStill || var11 == Block.lavaMoving || var11 == Block.lavaStill) {
                            var1 = var9.itemID;
                            AxisAlignedBB var12 = Block.blocksList[var1].getCollisionBoundingBoxFromPool(var10, var3, var4);
                            if(var12 != null) {
                                AxisAlignedBB var7 = this.thePlayer.boundingBox;
                                if(!((var12.x1 > var7.x0 && var12.x0 < var7.x1 ? (var12.y1 > var7.y0 && var12.y0 < var7.y1 ? var12.z1 > var7.z0 && var12.z0 < var7.z1 : false) : false) ? false : this.theWorld.checkIfAABBIsClear(var12))) {
                                    return;
                                }
                            }

                            if(!this.playerController.canPlace(var1)) {
                                return;
                            }

                            this.theWorld.setBlockWithNotify(var10, var3, var4, var1);
                            this.entityRenderer.itemRenderer.equippedProgress = 0.0F;
                            Block.blocksList[var1].onBlockPlaced(this.theWorld, var10, var3, var4);
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

	private void runTick() {
        if (this.rightClickDelayTimer > 0) {
            --this.rightClickDelayTimer;
        }

        if(this.currentScreen == null && !PointerInputAbstraction.isTouchMode() && !Mouse.isMouseGrabbed()) {
            this.setIngameNotInFocus();
            this.displayInGameMenu();
        }

		this.playerController.onUpdate();
		GuiIngame var1 = this.ingameGUI;
		++var1.updateCounter;

		int var2;
		for(var2 = 0; var2 < var1.chatMessageList.size(); ++var2) {
			++((ChatLine)var1.chatMessageList.get(var2)).updateCounter;
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
		RenderEngine var13 = this.renderEngine;

		for(var2 = 0; var2 < var13.textureList.size(); ++var2) {
			TextureFX var3 = (TextureFX)var13.textureList.get(var2);
			var3.anaglyphEnabled = var13.options.anaglyph;
			var3.onTick();
			var13.imageData.clear();
			var13.imageData.put(var3.imageData);
			var13.imageData.position(0).limit(var3.imageData.length);
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, var3.iconIndex % 16 << 4, var3.iconIndex / 16 << 4, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, var13.imageData);
		}

		if(this.currentScreen == null && this.thePlayer != null && this.thePlayer.health <= 0) {
			this.displayGuiScreen((GuiScreen)null);
		}

		int var4;
		int var6;
		int var14;
		EffectRenderer var16;
		int var21;
		int var24;
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

                if (!touch) {
                    if (Mouse.getEventButtonState()) {
                        PointerInputAbstraction.enterMouseModeHook();
                    }

                    if(this.currentScreen == null) {
                        if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
                            this.clickMouse(0);
                            this.prevFrameTime = this.ticksRan;
                        }

                        if(Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
                            this.clickMouse(1);
                            this.prevFrameTime = this.ticksRan;
                        }

                        if(Mouse.getEventButton() == 2 && Mouse.getEventButtonState() && this.objectMouseOver != null) {
                            var2 = this.theWorld.getBlockId(this.objectMouseOver.blockX, this.objectMouseOver.blockY, this.objectMouseOver.blockZ);
                            if(var2 == Block.grass.blockID) {
                                var2 = Block.dirt.blockID;
                            }

                            if(var2 == Block.stairDouble.blockID) {
                                var2 = Block.stairSingle.blockID;
                            }

                            if(var2 == Block.bedrock.blockID) {
                                var2 = Block.stone.blockID;
                            }

                            InventoryPlayer var26 = this.thePlayer.inventory;
                            var4 = var26.getInventorySlotContainItem(var2);
                            if(var4 >= 0 && var4 < 9) {
                                var26.currentItem = var4;
                            }
                        }
                    }

                    var14 = Mouse.getEventDWheel();
                    if(var14 != 0) {
                        var2 = var14;
                        InventoryPlayer var15 = this.thePlayer.inventory;
                        if(var14 > 0) {
                            var2 = 1;
                        }

                        if(var2 < 0) {
                            var2 = -1;
                        }

                        for(var15.currentItem -= var2; var15.currentItem < 0; var15.currentItem += 9) {
                        }

                        while(var15.currentItem >= 9) {
                            var15.currentItem -= 9;
                        }
                    }

                    if(this.currentScreen != null) {
                        this.currentScreen.handleMouseInput();
                    }
                }

                if (this.currentScreen == null && !(touch || Mouse.isActuallyGrabbed()) && (touch || Mouse.getEventButtonState())) {
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
                    if(this.currentScreen != null) {
                        this.currentScreen.handleKeyboardInput();
                    } else {
                        if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                            this.displayInGameMenu();
                        }

                        if(this.playerController instanceof PlayerControllerCreative) {
                            if(Keyboard.getEventKey() == this.options.keyBindLoad.keyCode) {
                                this.thePlayer.preparePlayerToSpawn();
                            }

                            if(Keyboard.getEventKey() == this.options.keyBindSave.keyCode) {
                                this.theWorld.setSpawnLocation((int)this.thePlayer.posX, (int)this.thePlayer.posY, (int)this.thePlayer.posZ, this.thePlayer.rotationYaw);
                                this.thePlayer.preparePlayerToSpawn();
                            }
                        }

                        if(Keyboard.getEventKey() == Keyboard.KEY_F5) {
                            this.thirdPersonView = !this.thirdPersonView;
                        }

                        if(Keyboard.getEventKey() == this.options.keyBindInventory.keyCode) {
                            this.playerController.displayInventoryGUI();
                        }

                        if(Keyboard.getEventKey() == this.options.keyBindDrop.keyCode) {
                            this.thePlayer.dropPlayerItemWithRandomChoice(this.thePlayer.inventory.currentItem);
                        }
                    }

                    for(var14 = 0; var14 < 9; ++var14) {
                        if(Keyboard.getEventKey() == var14 + 2) {
                            this.thePlayer.inventory.currentItem = var14;
                        }
                    }

                    if(Keyboard.getEventKey() == this.options.keyBindToggleFog.keyCode) {
                        this.options.setOptionValue(4, !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ? 1 : -1);
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
                    this.prevFrameTime = this.ticksRan;
                }
                wasMiningTouch = true;
            }
            wasMiningTouch = miningTouch;

            if (miningTouch && useTouch && this.rightClickDelayTimer == 0) {
                this.clickMouse(1);
            }

            if(this.currentScreen == null) {
                if((Mouse.isButtonDown(0) || miningTouch) && (float)(this.ticksRan - this.prevFrameTime) >= this.timer.ticksPerSecond / 4.0F) {
                    this.clickMouse(0);
                    this.prevFrameTime = this.ticksRan;
                }

                if((Mouse.isButtonDown(1) || miningTouch) && (float)(this.ticksRan - this.prevFrameTime) >= this.timer.ticksPerSecond / 4.0F) {
                    this.clickMouse(1);
                    this.prevFrameTime = this.ticksRan;
                }
            }

            boolean var20 = this.currentScreen == null && (Mouse.isButtonDown(0) || miningTouch) && !useTouch;
            if(this.leftClickCounter <= 0) {
                if(var20 && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == 0) {
                    var4 = this.objectMouseOver.blockX;
                    var24 = this.objectMouseOver.blockY;
                    var6 = this.objectMouseOver.blockZ;
                    this.playerController.sendBlockRemoving(var4, var24, var6);
                    int var10001 = var4;
                    int var10002 = var24;
                    var24 = this.objectMouseOver.sideHit;
                    var4 = var6;
                    var21 = var10002;
                    var2 = var10001;
                    var16 = this.effectRenderer;
                    var6 = var16.worldObj.getBlockId(var2, var21, var6);
                    if(var6 != 0) {
                        Block var28 = Block.blocksList[var6];
                        float var7 = 0.1F;
                        float var8 = (float)var2 + var16.rand.nextFloat() * (var28.maxX - var28.minX - var7 * 2.0F) + var7 + var28.minX;
                        float var9 = (float)var21 + var16.rand.nextFloat() * (var28.maxY - var28.minY - var7 * 2.0F) + var7 + var28.minY;
                        float var10 = (float)var4 + var16.rand.nextFloat() * (var28.maxZ - var28.minZ - var7 * 2.0F) + var7 + var28.minZ;
                        if(var24 == 0) {
                            var9 = (float)var21 + var28.minY - var7;
                        }

                        if(var24 == 1) {
                            var9 = (float)var21 + var28.maxY + var7;
                        }

                        if(var24 == 2) {
                            var10 = (float)var4 + var28.minZ - var7;
                        }

                        if(var24 == 3) {
                            var10 = (float)var4 + var28.maxZ + var7;
                        }

                        if(var24 == 4) {
                            var8 = (float)var2 + var28.minX - var7;
                        }

                        if(var24 == 5) {
                            var8 = (float)var2 + var28.maxX + var7;
                        }

                        var16.addEffect((new EntityDiggingFX(var16.worldObj, var8, var9, var10, 0.0F, 0.0F, 0.0F, var28)).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
                    }
                } else {
                    this.playerController.resetBlockRemoving();
                }
            }
		}

		if(this.currentScreen != null) {
			this.prevFrameTime = this.ticksRan + 10000;
		}

		if(this.currentScreen != null) {
            this.currentScreen.handleInput();
			if(this.currentScreen != null) {
				this.currentScreen.updateScreen();
			}
		}

		if(this.theWorld != null) {
            EntityRenderer var25 = this.entityRenderer;
            this.entityRenderer.prevFogColor = var25.fogColor;
            float var41 = var25.mc.theWorld.getBlockLightValue((int)var25.mc.thePlayer.posX, (int)var25.mc.thePlayer.posY, (int)var25.mc.thePlayer.posZ);
            float var31 = (float)(3 - var25.mc.options.renderDistance) / 3.0F;
            var41 = var41 * (1.0F - var31) + var31;
            var25.fogColor += (var41 - var25.fogColor) * 0.1F;
            ++var25.entityRendererInt1;
            ItemRenderer var32 = var25.itemRenderer;
            var25.itemRenderer.prevEquippedProgress = var32.equippedProgress;
            if(var32.itemSwingState) {
                ++var32.swingProgress;
                if(var32.swingProgress == 7) {
                    var32.swingProgress = 0;
                    var32.itemSwingState = false;
                }
            }

            ItemStack var35 = var32.mc.thePlayer.inventory.getCurrentItem();
            float var36 = 0.4F;
            float var42 = (var35 == var32.itemToRender ? 1.0F : 0.0F) - var32.equippedProgress;
            if(var42 < -var36) {
                var42 = -var36;
            }

            if(var42 > var36) {
                var42 = var36;
            }

            var32.equippedProgress += var42;
            if(var32.equippedProgress < 0.1F) {
                var32.itemToRender = var35;
            }

            if(var25.mc.thirdPersonView) {
                var25.addRainParticles();
            }

            ++this.renderGlobal.cloudOffsetX;
            this.theWorld.updateEntities();
            this.theWorld.tick();
            this.effectRenderer.updateEffects();
		}

	}

    public final void generateNewLevel(int var1) {
        String var2 = this.session != null ? this.session.username : "anonymous";
        LevelGenerator var10000 = new LevelGenerator(this.loadingScreen);
        int var10002 = 128 << var1;
        int var10003 = 128 << var1;
        boolean var5 = true;
        int var4 = var10003;
        int var3 = var10002;
        LevelGenerator var31 = var10000;
        var31.progressBar.displayProgressMessage("Generating level");
        var31.width = var3;
        var31.depth = var4;
        var31.height = 64;
        var31.waterLevel = 32;
        var31.blocksByteArray = new byte[var3 * var4 << 6];
        var31.progressBar.displayLoadingString("Raising..");
        LevelGenerator var6 = var31;
        NoiseGeneratorDistort var7 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(var31.rand, 8), new NoiseGeneratorOctaves(var31.rand, 8));
        NoiseGeneratorDistort var8 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(var31.rand, 8), new NoiseGeneratorOctaves(var31.rand, 8));
        NoiseGeneratorOctaves var9 = new NoiseGeneratorOctaves(var31.rand, 6);
        int[] var10 = new int[var31.width * var31.depth];
        float var11 = 1.3F;

        int var12;
        int var17;
        for(var17 = 0; var17 < var6.width; ++var17) {
            var6.setNextPhase(var17 * 100 / (var6.width - 1));

            for(var12 = 0; var12 < var6.depth; ++var12) {
                double var19 = var7.generateNoise((double)((float)var17 * var11), (double)((float)var12 * var11)) / 6.0D + (double)-4;
                double var21 = var8.generateNoise((double)((float)var17 * var11), (double)((float)var12 * var11)) / 5.0D + 10.0D + (double)-4;
                double var64 = var9.generateNoise((double)var17, (double)var12) / 8.0D;
                double var23 = 0.0D;
                if(var64 > 0.0D) {
                    var21 = var19;
                }

                double var25 = Math.max(var19, var21) / 2.0D;
                if(var25 < 0.0D) {
                    var25 *= 0.8D;
                }

                var10[var17 + var12 * var6.width] = (int)var25;
            }
        }

        var31.progressBar.displayLoadingString("Eroding..");
        int[] var35 = var10;
        var6 = var31;
        var8 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(var31.rand, 8), new NoiseGeneratorOctaves(var31.rand, 8));
        NoiseGeneratorDistort var40 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(var31.rand, 8), new NoiseGeneratorOctaves(var31.rand, 8));

        int var43;
        int var45;
        boolean var50;
        int var51;
        for(var43 = 0; var43 < var6.width; ++var43) {
            var6.setNextPhase(var43 * 100 / (var6.width - 1));

            for(var45 = 0; var45 < var6.depth; ++var45) {
                double var16 = var8.generateNoise((double)(var43 << 1), (double)(var45 << 1)) / 8.0D;
                var12 = var40.generateNoise((double)(var43 << 1), (double)(var45 << 1)) > 0.0D ? 1 : 0;
                if(var16 > 2.0D) {
                    int var65 = var35[var43 + var45 * var6.width];
                    var50 = false;
                    var51 = ((var65 - var12) / 2 << 1) + var12;
                    var35[var43 + var45 * var6.width] = var51;
                }
            }
        }

        var31.progressBar.displayLoadingString("Soiling..");
        var35 = var10;
        var6 = var31;
        int var38 = var31.width;
        int var41 = var31.depth;
        var43 = var31.height;
        NoiseGeneratorOctaves var46 = new NoiseGeneratorOctaves(var31.rand, 8);

        int var20;
        int var22;
        int var48;
        int var55;
        int var57;
        for(var48 = 0; var48 < var38; ++var48) {
            var6.setNextPhase(var48 * 100 / (var6.width - 1));

            for(var17 = 0; var17 < var41; ++var17) {
                var12 = (int)(var46.generateNoise((double)var48, (double)var17) / 24.0D) - 4;
                var51 = var35[var48 + var17 * var38] + var6.waterLevel;
                var20 = var51 + var12;
                var35[var48 + var17 * var38] = Math.max(var51, var20);
                if(var35[var48 + var17 * var38] > var43 - 2) {
                    var35[var48 + var17 * var38] = var43 - 2;
                }

                if(var35[var48 + var17 * var38] < 1) {
                    var35[var48 + var17 * var38] = 1;
                }

                for(var55 = 0; var55 < var43; ++var55) {
                    var22 = (var55 * var6.depth + var17) * var6.width + var48;
                    var57 = 0;
                    if(var55 <= var51) {
                        var57 = Block.dirt.blockID;
                    }

                    if(var55 <= var20) {
                        var57 = Block.stone.blockID;
                    }

                    if(var55 == 0) {
                        var57 = Block.lavaMoving.blockID;
                    }

                    var6.blocksByteArray[var22] = (byte)var57;
                }
            }
        }

        var31.progressBar.displayLoadingString("Carving..");
        boolean var36 = false;
        var6 = var31;
        var41 = var31.width;
        var43 = var31.depth;
        var45 = var31.height;
        var48 = var41 * var43 * var45 / 256 / 64 << 1;

        for(var17 = 0; var17 < var48; ++var17) {
            var6.setNextPhase(var17 * 100 / (var48 - 1) / 4);
            float var47 = var6.rand.nextFloat() * (float)var41;
            float var52 = var6.rand.nextFloat() * (float)var45;
            float var53 = var6.rand.nextFloat() * (float)var43;
            var55 = (int)((var6.rand.nextFloat() + var6.rand.nextFloat()) * 200.0F);
            float var56 = var6.rand.nextFloat() * (float)Math.PI * 2.0F;
            float var59 = 0.0F;
            float var24 = var6.rand.nextFloat() * (float)Math.PI * 2.0F;
            float var60 = 0.0F;
            float var26 = var6.rand.nextFloat() * var6.rand.nextFloat();

            for(int var33 = 0; var33 < var55; ++var33) {
                var47 += MathHelper.sin(var56) * MathHelper.cos(var24);
                var53 += MathHelper.cos(var56) * MathHelper.cos(var24);
                var52 += MathHelper.sin(var24);
                var56 += var59 * 0.2F;
                var59 = var59 * 0.9F + (var6.rand.nextFloat() - var6.rand.nextFloat());
                var24 = (var24 + var60 * 0.5F) * 0.5F;
                var60 = var60 * (12.0F / 16.0F) + (var6.rand.nextFloat() - var6.rand.nextFloat());
                if(var6.rand.nextFloat() >= 0.25F) {
                    float var37 = var47 + (var6.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
                    float var39 = var52 + (var6.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
                    float var13 = var53 + (var6.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
                    float var14 = ((float)var6.height - var39) / (float)var6.height;
                    var14 = 1.2F + (var14 * 3.5F + 1.0F) * var26;
                    var14 = MathHelper.sin((float)var33 * (float)Math.PI / (float)var55) * var14;

                    for(int var15 = (int)(var37 - var14); var15 <= (int)(var37 + var14); ++var15) {
                        for(int var18 = (int)(var39 - var14); var18 <= (int)(var39 + var14); ++var18) {
                            for(int var27 = (int)(var13 - var14); var27 <= (int)(var13 + var14); ++var27) {
                                float var28 = (float)var15 - var37;
                                float var29 = (float)var18 - var39;
                                float var30 = (float)var27 - var13;
                                if(var28 * var28 + var29 * var29 * 2.0F + var30 * var30 < var14 * var14 && var15 >= 1 && var18 >= 1 && var27 >= 1 && var15 < var6.width - 1 && var18 < var6.height - 1 && var27 < var6.depth - 1) {
                                    int var63 = (var18 * var6.depth + var27) * var6.width + var15;
                                    if(var6.blocksByteArray[var63] == Block.stone.blockID) {
                                        var6.blocksByteArray[var63] = 0;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        var31.populateOre(Block.oreCoal.blockID, 90, 1, 4);
        var31.populateOre(Block.oreIron.blockID, 70, 2, 4);
        var31.populateOre(Block.oreGold.blockID, 50, 3, 4);
        var31.progressBar.displayLoadingString("Watering..");
        var6 = var31;
        var43 = Block.waterStill.blockID;
        var31.setNextPhase(0);

        for(var45 = 0; var45 < var6.width; ++var45) {
            var6.floodFill(var45, var6.height / 2 - 1, 0, 0, var43);
            var6.floodFill(var45, var6.height / 2 - 1, var6.depth - 1, 0, var43);
        }

        for(var45 = 0; var45 < var6.depth; ++var45) {
            var6.floodFill(0, var6.height / 2 - 1, var45, 0, var43);
            var6.floodFill(var6.width - 1, var6.height / 2 - 1, var45, 0, var43);
        }

        var45 = var6.width * var6.depth / 8000;

        for(var48 = 0; var48 < var45; ++var48) {
            if(var48 % 100 == 0) {
                var6.setNextPhase(var48 * 100 / (var45 - 1));
            }

            var17 = var6.rand.nextInt(var6.width);
            var12 = var6.waterLevel - 1 - var6.rand.nextInt(2);
            var51 = var6.rand.nextInt(var6.depth);
            if(var6.blocksByteArray[(var12 * var6.depth + var51) * var6.width + var17] == 0) {
                var6.floodFill(var17, var12, var51, 0, var43);
            }
        }

        var6.setNextPhase(100);
        var31.progressBar.displayLoadingString("Melting..");
        var6 = var31;
        var38 = var31.width * var31.depth * var31.height / 20000;

        for(var41 = 0; var41 < var38; ++var41) {
            if(var41 % 100 == 0) {
                var6.setNextPhase(var41 * 100 / (var38 - 1));
            }

            var43 = var6.rand.nextInt(var6.width);
            var45 = (int)(var6.rand.nextFloat() * var6.rand.nextFloat() * (float)(var6.waterLevel - 3));
            var48 = var6.rand.nextInt(var6.depth);
            if(var6.blocksByteArray[(var45 * var6.depth + var48) * var6.width + var43] == 0) {
                var6.floodFill(var43, var45, var48, 0, Block.lavaStill.blockID);
            }
        }

        var6.setNextPhase(100);
        var31.progressBar.displayLoadingString("Growing..");
        var35 = var10;
        var6 = var31;
        var38 = var31.width;
        var41 = var31.depth;
        var43 = var31.height;
        var46 = new NoiseGeneratorOctaves(var31.rand, 8);
        NoiseGeneratorOctaves var49 = new NoiseGeneratorOctaves(var31.rand, 8);

        int var58;
        for(var17 = 0; var17 < var38; ++var17) {
            var6.setNextPhase(var17 * 100 / (var6.width - 1));

            for(var12 = 0; var12 < var41; ++var12) {
                var50 = var46.generateNoise((double)var17, (double)var12) > 8.0D;
                boolean var54 = var49.generateNoise((double)var17, (double)var12) > 12.0D;
                var55 = var35[var17 + var12 * var38];
                var22 = (var55 * var6.depth + var12) * var6.width + var17;
                var57 = var6.blocksByteArray[((var55 + 1) * var6.depth + var12) * var6.width + var17] & 255;
                if((var57 == Block.waterMoving.blockID || var57 == Block.waterStill.blockID) && var55 <= var43 / 2 - 1 && var54) {
                    var6.blocksByteArray[var22] = (byte)Block.gravel.blockID;
                }

                if(var57 == 0) {
                    var58 = Block.grass.blockID;
                    if(var55 <= var43 / 2 - 1 && var50) {
                        var58 = Block.sand.blockID;
                    }

                    var6.blocksByteArray[var22] = (byte)var58;
                }
            }
        }

        var31.progressBar.displayLoadingString("Planting..");
        var35 = var10;
        var6 = var31;
        var38 = var31.width;
        var41 = var31.width * var31.depth / 3000;

        for(var43 = 0; var43 < var41; ++var43) {
            var45 = var6.rand.nextInt(2);
            var6.setNextPhase(var43 * 50 / (var41 - 1));
            var48 = var6.rand.nextInt(var6.width);
            var17 = var6.rand.nextInt(var6.depth);

            for(var12 = 0; var12 < 10; ++var12) {
                var51 = var48;
                var20 = var17;

                for(var55 = 0; var55 < 5; ++var55) {
                    var51 += var6.rand.nextInt(6) - var6.rand.nextInt(6);
                    var20 += var6.rand.nextInt(6) - var6.rand.nextInt(6);
                    if((var45 < 2 || var6.rand.nextInt(4) == 0) && var51 >= 0 && var20 >= 0 && var51 < var6.width && var20 < var6.depth) {
                        var22 = var35[var51 + var20 * var38] + 1;
                        boolean var66 = (var6.blocksByteArray[(var22 * var6.depth + var20) * var6.width + var51] & 255) == 0;
                        boolean var61 = false;
                        if(var66) {
                            var58 = (var22 * var6.depth + var20) * var6.width + var51;
                            if((var6.blocksByteArray[((var22 - 1) * var6.depth + var20) * var6.width + var51] & 255) == Block.grass.blockID) {
                                if(var45 == 0) {
                                    var6.blocksByteArray[var58] = (byte)Block.plantYellow.blockID;
                                } else if(var45 == 1) {
                                    var6.blocksByteArray[var58] = (byte)Block.plantRed.blockID;
                                }
                            }
                        }
                    }
                }
            }
        }

        var35 = var10;
        var6 = var31;
        var38 = var31.width;
        var43 = var31.width * var31.depth * var31.height / 2000;

        for(var45 = 0; var45 < var43; ++var45) {
            var48 = var6.rand.nextInt(2);
            var6.setNextPhase(var45 * 50 / (var43 - 1) + 50);
            var17 = var6.rand.nextInt(var6.width);
            var12 = var6.rand.nextInt(var6.height);
            var51 = var6.rand.nextInt(var6.depth);

            for(var20 = 0; var20 < 20; ++var20) {
                var55 = var17;
                var22 = var12;
                var57 = var51;

                for(var58 = 0; var58 < 5; ++var58) {
                    var55 += var6.rand.nextInt(6) - var6.rand.nextInt(6);
                    var22 += var6.rand.nextInt(2) - var6.rand.nextInt(2);
                    var57 += var6.rand.nextInt(6) - var6.rand.nextInt(6);
                    if((var48 < 2 || var6.rand.nextInt(4) == 0) && var55 >= 0 && var57 >= 0 && var22 >= 1 && var55 < var6.width && var57 < var6.depth && var22 < var35[var55 + var57 * var38] - 1 && (var6.blocksByteArray[(var22 * var6.depth + var57) * var6.width + var55] & 255) == 0) {
                        int var62 = (var22 * var6.depth + var57) * var6.width + var55;
                        if((var6.blocksByteArray[((var22 - 1) * var6.depth + var57) * var6.width + var55] & 255) == Block.stone.blockID) {
                            if(var48 == 0) {
                                var6.blocksByteArray[var62] = (byte)Block.mushroomBrown.blockID;
                            } else if(var48 == 1) {
                                var6.blocksByteArray[var62] = (byte)Block.mushroomRed.blockID;
                            }
                        }
                    }
                }
            }
        }

        World var34 = new World();
        var34.waterLevel = var31.waterLevel;
        var34.generate(var3, 64, var4, var31.blocksByteArray);
        EagRuntime.currentTimeMillis();
        int[] var44 = var10;
        World var42 = var34;
        var6 = var31;
        var41 = var31.width;
        var43 = var31.width * var31.depth / 4000;

        for(var45 = 0; var45 < var43; ++var45) {
            var6.setNextPhase(var45 * 50 / (var43 - 1) + 50);
            var48 = var6.rand.nextInt(var6.width);
            var17 = var6.rand.nextInt(var6.depth);

            for(var12 = 0; var12 < 20; ++var12) {
                var51 = var48;
                var20 = var17;

                for(var55 = 0; var55 < 20; ++var55) {
                    var51 += var6.rand.nextInt(6) - var6.rand.nextInt(6);
                    var20 += var6.rand.nextInt(6) - var6.rand.nextInt(6);
                    if(var51 >= 0 && var20 >= 0 && var51 < var6.width && var20 < var6.depth) {
                        var22 = var44[var51 + var20 * var41] + 1;
                        if(var6.rand.nextInt(4) == 0) {
                            var42.growTrees(var51, var22, var20);
                        }
                    }
                }
            }
        }

        this.setLevel(var34);
    }

	private void setLevel(World var1) {
		this.theWorld = var1;
		if(var1 != null) {
			var1.load();
			this.playerController.onWorldChange(var1);
			this.thePlayer = (EntityPlayerSP)var1.findSubclassOf(EntityPlayerSP.class);
		}

		if(this.thePlayer == null) {
			this.thePlayer = new EntityPlayerSP(var1);
			this.thePlayer.preparePlayerToSpawn();
			this.playerController.preparePlayer(this.thePlayer);
			if(var1 != null) {
				var1.playerEntity = this.thePlayer;
			}
		}

		if(this.thePlayer != null) {
			this.thePlayer.movementInput = new MovementInputFromKeys(this.options);
			this.playerController.flipPlayer(this.thePlayer);
		}

		if(this.renderGlobal != null) {
			RenderGlobal var2 = this.renderGlobal;
			if(var2.worldObj != null) {
				var2.worldObj.removeRenderer(var2);
			}

			var2.renderManager.worldObj = var1;
			var2.worldObj = var1;
			var2.globalRenderBlocks = new RenderBlocks(Tessellator.instance, var1);
			if(var1 != null) {
				var1.addRenderer(var2);
				var2.loadRenderers();
			}
		}

		if(this.effectRenderer != null) {
			EffectRenderer var5 = this.effectRenderer;
			var5.worldObj = var1;

			for(int var6 = 0; var6 < 2; ++var6) {
				var5.fxLayers[var6].clear();
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
        minecraft = new Minecraft(854, 480, false);
        minecraft.session = new Session(username);
        minecraft.run();
    }
}
