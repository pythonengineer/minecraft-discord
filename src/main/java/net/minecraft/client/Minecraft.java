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
import net.minecraft.client.player.EntityPlayer;
import net.minecraft.client.player.InventoryPlayer;
import net.minecraft.client.player.MovementInputFromKeys;
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
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.generator.LevelGenerator;
import net.minecraft.game.level.generator.noise.NoiseGeneratorDistort;
import net.minecraft.game.level.generator.noise.NoiseGeneratorOctaves;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;

public final class Minecraft implements Runnable {
	public PlayerController playerController = new PlayerControllerCreative(this);
	private boolean fullScreen = false;
	public int displayWidth;
	public int displayHeight;
	private Timer timer = new Timer(20.0F);
	public World theWorld;
	public RenderGlobal renderGlobal;
	public EntityPlayer thePlayer;
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
	public volatile boolean running;
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
				this.currentScreen.onClose();
			}

			if(var1 == null && this.thePlayer.health <= 0) {
				var1 = new GuiGameOver();
			}

			this.currentScreen = (GuiScreen)var1;
			if(var1 != null) {
				if(this.inventoryScreen) {
					this.thePlayer.resetKeyState();
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
				boolean var9 = false;
				if(this.theWorld == null) {
					this.generateLevel(0);
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
    							EntityPlayer var55 = var39.mc.thePlayer;
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
    						int var48 = Mouse.getX() * var42 / var39.mc.displayWidth;
    						var49 = var45 - Mouse.getY() * var45 / var39.mc.displayHeight - 1;
    						if(var39.mc.theWorld != null) {
    							var39.updateCameraAndRender(var41);
    							var39.mc.ingameGUI.renderGameOverlay(var41);
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
    							var39.mc.currentScreen.drawScreen(var48, var49);
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
            this.thePlayer.resetKeyState();
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
			ItemRenderer var5;
			if(var1 == 0) {
				var5 = this.entityRenderer.itemRenderer;
				var5.swingProgress = -1;
				var5.itemSwingState = true;
            } else {
                this.rightClickDelayTimer = 4;
            }

			int var2;
			if(var1 == 1) {
				var2 = this.thePlayer.inventory.getCurrentItem();
				if(var2 > 0 && this.playerController.sendUseItem(this.thePlayer, var2)) {
					var5 = null;
					this.entityRenderer.itemRenderer.equippedProgress = 0.0F;
					return;
				}
			}

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
					var2 = this.objectMouseOver.blockX;
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
							--var2;
						}

						if(this.objectMouseOver.sideHit == 5) {
							++var2;
						}
					}

					Block var9 = Block.blocksList[this.theWorld.getBlockId(var2, var3, var4)];
					if(var1 == 0) {
						if(var9 != Block.bedrock) {
							this.playerController.clickBlock(var2, var3, var4);
							return;
						}
					} else {
						var1 = this.thePlayer.inventory.getCurrentItem();
						if(var1 <= 0) {
							return;
						}

						var9 = Block.blocksList[this.theWorld.getBlockId(var2, var3, var4)];
						if(var9 == null || var9 == Block.waterMoving || var9 == Block.waterStill || var9 == Block.lavaMoving || var9 == Block.lavaStill) {
							AxisAlignedBB var10 = Block.blocksList[var1].getCollisionBoundingBoxFromPool(var2, var3, var4);
							if(var10 != null) {
								AxisAlignedBB var7 = this.thePlayer.boundingBox;
								if(!((var10.x1 > var7.x0 && var10.x0 < var7.x1 ? (var10.y1 > var7.y0 && var10.y0 < var7.y1 ? var10.z1 > var7.z0 && var10.z0 < var7.z1 : false) : false) ? false : this.theWorld.checkIfAABBIsClear(var10))) {
									return;
								}
							}

							if(!this.playerController.canPlace(var1)) {
								return;
							}

							this.theWorld.setBlockWithNotify(var2, var3, var4, var1);
							this.entityRenderer.itemRenderer.equippedProgress = 0.0F;
							Block.blocksList[var1].onBlockPlaced(this.theWorld, var2, var3, var4);
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

		TextureFX var3;
		for(var2 = 0; var2 < var13.textureList.size(); ++var2) {
			var3 = (TextureFX)var13.textureList.get(var2);
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

                            boolean var5 = this.playerController instanceof PlayerControllerCreative;
                            InventoryPlayer var18 = this.thePlayer.inventory;
                            var6 = var18.getInventorySlotContainItem(var2);
                            if(var6 >= 0) {
                                var18.currentItem = var6;
                            } else if(var5 && var2 > 0 && Session.allowedBlocks.contains(Block.blocksList[var2])) {
                                var18.replaceSlot(Block.blocksList[var2]);
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

                        for(var15.currentItem -= var2; var15.currentItem < 0; var15.currentItem += var15.mainInventory.length) {
                        }

                        while(var15.currentItem >= var15.mainInventory.length) {
                            var15.currentItem -= var15.mainInventory.length;
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
                this.thePlayer.checkKeyForMovementInput(Keyboard.getEventKey(), Keyboard.getEventKeyState());
                if(Keyboard.getEventKeyState()) {
                    if(this.currentScreen != null) {
                        this.currentScreen.handleKeyboardInput();
                    }

                    if(this.currentScreen == null) {
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
            if(!this.playerController.isInTestMode && this.leftClickCounter <= 0) {
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
            this.currentScreen.updateEvents();
			if(this.currentScreen != null) {
				this.currentScreen.updateScreen();
			}
		}

		if(this.theWorld != null) {
			EntityRenderer var19 = this.entityRenderer;
			++var19.entityRendererInt1;
			ItemRenderer var31 = var19.itemRenderer;
			var19.itemRenderer.prevEquippedProgress = var31.equippedProgress;
			if(var31.itemSwingState) {
				++var31.swingProgress;
				if(var31.swingProgress == 7) {
					var31.swingProgress = 0;
					var31.itemSwingState = false;
				}
			}

			var3 = null;
			var4 = var31.minecraft.thePlayer.inventory.getCurrentItem();
			Block var26 = null;
			if(var4 > 0) {
				var26 = Block.blocksList[var4];
			}

			float var29 = 0.4F;
			float var10000 = var26 == var31.itemToRender ? 1.0F : 0.0F;
			float var22 = 0.0F;
			var22 = var10000 - var31.equippedProgress;
			if(var22 < -var29) {
				var22 = -var29;
			}

			if(var22 > var29) {
				var22 = var29;
			}

			var31.equippedProgress += var22;
			if(var31.equippedProgress < 0.1F) {
				var31.itemToRender = var26;
			}

			if(var19.mc.thirdPersonView) {
				EntityRenderer var32 = var19;
				EntityPlayer var27 = var19.mc.thePlayer;
				World var23 = var19.mc.theWorld;
				var24 = (int)var27.posX;
				var6 = (int)var27.posY;
				var21 = (int)var27.posZ;

				for(var14 = 0; var14 < 50; ++var14) {
					int var30 = var24 + var32.random.nextInt(9) - 4;
					int var33 = var21 + var32.random.nextInt(9) - 4;
					int var34 = var23.getMapHeight(var30, var33);
					int var35 = var23.getBlockId(var30, var34 - 1, var33);
					if(var34 <= var6 + 4 && var34 >= var6 - 4) {
						float var11 = var32.random.nextFloat();
						float var12 = var32.random.nextFloat();
						if(var35 > 0) {
							var32.mc.effectRenderer.addEffect(new EntityRainFX(var23, (float)var30 + var11, (float)var34 + 0.1F - Block.blocksList[var35].minY, (float)var33 + var12));
						}
					}
				}
			}

			var1 = null;
			++this.renderGlobal.cloudOffsetX;
			this.theWorld.updateEntities();
			this.theWorld.tick();
			var16 = this.effectRenderer;

			for(var2 = 0; var2 < 2; ++var2) {
				for(var21 = 0; var21 < var16.fxLayers[var2].size(); ++var21) {
					EntityFX var25 = (EntityFX)var16.fxLayers[var2].get(var21);
					var25.onEntityUpdate();
					if(var25.isDead) {
						var16.fxLayers[var2].remove(var21--);
					}
				}
			}
		}

	}

	public final void generateLevel(int var1) {
		String var2 = this.session != null ? this.session.username : "anonymous";
		LevelGenerator var10000 = new LevelGenerator(this.loadingScreen);
		int var10002 = 128 << var1;
		int var10003 = 128 << var1;
		boolean var5 = true;
		int var4 = var10003;
		int var3 = var10002;
		LevelGenerator var31 = var10000;
		String var7 = "Generating level";
		LoadingScreenRenderer var6 = var31.progressBar;
		if(!var6.minecraft.running) {
			throw new MinecraftError();
		} else {
			var6.title = var7;
			int var8 = this.scaledResolution.getScaledWidth();
			int var9 = this.scaledResolution.getScaledHeight();
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, (double)var8, (double)var9, 0.0D, 100.0D, 300.0D);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -200.0F);
			var31.width = var3;
			var31.depth = var4;
			var31.height = 64;
			var31.waterLevel = 32;
			var31.blocksByteArray = new byte[var3 * var4 << 6];
			var31.progressBar.displayProgressMessage("Raising..");
			LevelGenerator var35 = var31;
			NoiseGeneratorDistort var36 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(var31.rand, 8), new NoiseGeneratorOctaves(var31.rand, 8));
			NoiseGeneratorDistort var38 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(var31.rand, 8), new NoiseGeneratorOctaves(var31.rand, 8));
			NoiseGeneratorOctaves var41 = new NoiseGeneratorOctaves(var31.rand, 6);
			int[] var10 = new int[var31.width * var31.depth];
			float var11 = 1.3F;

			int var12;
			int var17;
			for(var17 = 0; var17 < var35.width; ++var17) {
				var35.setNextPhase(var17 * 100 / (var35.width - 1));

				for(var12 = 0; var12 < var35.depth; ++var12) {
					double var19 = var36.generateNoise((double)((float)var17 * var11), (double)((float)var12 * var11)) / 6.0D + (double)-4;
					double var21 = var38.generateNoise((double)((float)var17 * var11), (double)((float)var12 * var11)) / 5.0D + 10.0D + (double)-4;
					double var66 = var41.generateNoise((double)var17, (double)var12) / 8.0D;
					double var23 = 0.0D;
					if(var66 > 0.0D) {
						var21 = var19;
					}

					double var25 = Math.max(var19, var21) / 2.0D;
					if(var25 < 0.0D) {
						var25 *= 0.8D;
					}

					var10[var17 + var12 * var35.width] = (int)var25;
				}
			}

			var31.progressBar.displayProgressMessage("Eroding..");
			int[] var37 = var10;
			var35 = var31;
			var38 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(var31.rand, 8), new NoiseGeneratorOctaves(var31.rand, 8));
			NoiseGeneratorDistort var43 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(var31.rand, 8), new NoiseGeneratorOctaves(var31.rand, 8));

			int var45;
			int var47;
			boolean var52;
			int var53;
			for(var45 = 0; var45 < var35.width; ++var45) {
				var35.setNextPhase(var45 * 100 / (var35.width - 1));

				for(var47 = 0; var47 < var35.depth; ++var47) {
					double var16 = var38.generateNoise((double)(var45 << 1), (double)(var47 << 1)) / 8.0D;
					var12 = var43.generateNoise((double)(var45 << 1), (double)(var47 << 1)) > 0.0D ? 1 : 0;
					if(var16 > 2.0D) {
						int var67 = var37[var45 + var47 * var35.width];
						var52 = false;
						var53 = ((var67 - var12) / 2 << 1) + var12;
						var37[var45 + var47 * var35.width] = var53;
					}
				}
			}

			var31.progressBar.displayProgressMessage("Soiling..");
			var37 = var10;
			var35 = var31;
			var8 = var31.width;
			var9 = var31.depth;
			var45 = var31.height;
			NoiseGeneratorOctaves var48 = new NoiseGeneratorOctaves(var31.rand, 8);

			int var20;
			int var22;
			int var50;
			int var57;
			int var59;
			for(var50 = 0; var50 < var8; ++var50) {
				var35.setNextPhase(var50 * 100 / (var35.width - 1));

				for(var17 = 0; var17 < var9; ++var17) {
					var12 = (int)(var48.generateNoise((double)var50, (double)var17) / 24.0D) - 4;
					var53 = var37[var50 + var17 * var8] + var35.waterLevel;
					var20 = var53 + var12;
					var37[var50 + var17 * var8] = Math.max(var53, var20);
					if(var37[var50 + var17 * var8] > var45 - 2) {
						var37[var50 + var17 * var8] = var45 - 2;
					}

					if(var37[var50 + var17 * var8] < 1) {
						var37[var50 + var17 * var8] = 1;
					}

					for(var57 = 0; var57 < var45; ++var57) {
						var22 = (var57 * var35.depth + var17) * var35.width + var50;
						var59 = 0;
						if(var57 <= var53) {
							var59 = Block.dirt.blockID;
						}

						if(var57 <= var20) {
							var59 = Block.stone.blockID;
						}

						if(var57 == 0) {
							var59 = Block.lavaMoving.blockID;
						}

						var35.blocksByteArray[var22] = (byte)var59;
					}
				}
			}

			var31.progressBar.displayProgressMessage("Carving..");
			boolean var39 = false;
			var35 = var31;
			var9 = var31.width;
			var45 = var31.depth;
			var47 = var31.height;
			var50 = var9 * var45 * var47 / 256 / 64 << 1;

			for(var17 = 0; var17 < var50; ++var17) {
				var35.setNextPhase(var17 * 100 / (var50 - 1) / 4);
				float var49 = var35.rand.nextFloat() * (float)var9;
				float var54 = var35.rand.nextFloat() * (float)var47;
				float var55 = var35.rand.nextFloat() * (float)var45;
				var57 = (int)((var35.rand.nextFloat() + var35.rand.nextFloat()) * 200.0F);
				float var58 = var35.rand.nextFloat() * (float)Math.PI * 2.0F;
				float var61 = 0.0F;
				float var24 = var35.rand.nextFloat() * (float)Math.PI * 2.0F;
				float var62 = 0.0F;
				float var26 = var35.rand.nextFloat() * var35.rand.nextFloat();

				for(int var33 = 0; var33 < var57; ++var33) {
					var49 += MathHelper.sin(var58) * MathHelper.cos(var24);
					var55 += MathHelper.cos(var58) * MathHelper.cos(var24);
					var54 += MathHelper.sin(var24);
					var58 += var61 * 0.2F;
					var61 = var61 * 0.9F + (var35.rand.nextFloat() - var35.rand.nextFloat());
					var24 = (var24 + var62 * 0.5F) * 0.5F;
					var62 = var62 * (12.0F / 16.0F) + (var35.rand.nextFloat() - var35.rand.nextFloat());
					if(var35.rand.nextFloat() >= 0.25F) {
						float var40 = var49 + (var35.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
						float var42 = var54 + (var35.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
						float var13 = var55 + (var35.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
						float var14 = ((float)var35.height - var42) / (float)var35.height;
						var14 = 1.2F + (var14 * 3.5F + 1.0F) * var26;
						var14 = MathHelper.sin((float)var33 * (float)Math.PI / (float)var57) * var14;

						for(int var15 = (int)(var40 - var14); var15 <= (int)(var40 + var14); ++var15) {
							for(int var18 = (int)(var42 - var14); var18 <= (int)(var42 + var14); ++var18) {
								for(int var27 = (int)(var13 - var14); var27 <= (int)(var13 + var14); ++var27) {
									float var28 = (float)var15 - var40;
									float var29 = (float)var18 - var42;
									float var30 = (float)var27 - var13;
									if(var28 * var28 + var29 * var29 * 2.0F + var30 * var30 < var14 * var14 && var15 >= 1 && var18 >= 1 && var27 >= 1 && var15 < var35.width - 1 && var18 < var35.height - 1 && var27 < var35.depth - 1) {
										int var65 = (var18 * var35.depth + var27) * var35.width + var15;
										if(var35.blocksByteArray[var65] == Block.stone.blockID) {
											var35.blocksByteArray[var65] = 0;
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
			var31.progressBar.displayProgressMessage("Watering..");
			var35 = var31;
			var45 = Block.waterStill.blockID;
			var31.setNextPhase(0);

			for(var47 = 0; var47 < var35.width; ++var47) {
				var35.floodFill(var47, var35.height / 2 - 1, 0, 0, var45);
				var35.floodFill(var47, var35.height / 2 - 1, var35.depth - 1, 0, var45);
			}

			for(var47 = 0; var47 < var35.depth; ++var47) {
				var35.floodFill(0, var35.height / 2 - 1, var47, 0, var45);
				var35.floodFill(var35.width - 1, var35.height / 2 - 1, var47, 0, var45);
			}

			var47 = var35.width * var35.depth / 8000;

			for(var50 = 0; var50 < var47; ++var50) {
				if(var50 % 100 == 0) {
					var35.setNextPhase(var50 * 100 / (var47 - 1));
				}

				var17 = var35.rand.nextInt(var35.width);
				var12 = var35.waterLevel - 1 - var35.rand.nextInt(2);
				var53 = var35.rand.nextInt(var35.depth);
				if(var35.blocksByteArray[(var12 * var35.depth + var53) * var35.width + var17] == 0) {
					var35.floodFill(var17, var12, var53, 0, var45);
				}
			}

			var35.setNextPhase(100);
			var31.progressBar.displayProgressMessage("Melting..");
			var35 = var31;
			var8 = var31.width * var31.depth * var31.height / 20000;

			for(var9 = 0; var9 < var8; ++var9) {
				if(var9 % 100 == 0) {
					var35.setNextPhase(var9 * 100 / (var8 - 1));
				}

				var45 = var35.rand.nextInt(var35.width);
				var47 = (int)(var35.rand.nextFloat() * var35.rand.nextFloat() * (float)(var35.waterLevel - 3));
				var50 = var35.rand.nextInt(var35.depth);
				if(var35.blocksByteArray[(var47 * var35.depth + var50) * var35.width + var45] == 0) {
					var35.floodFill(var45, var47, var50, 0, Block.lavaStill.blockID);
				}
			}

			var35.setNextPhase(100);
			var31.progressBar.displayProgressMessage("Growing..");
			var37 = var10;
			var35 = var31;
			var8 = var31.width;
			var9 = var31.depth;
			var45 = var31.height;
			var48 = new NoiseGeneratorOctaves(var31.rand, 8);
			NoiseGeneratorOctaves var51 = new NoiseGeneratorOctaves(var31.rand, 8);

			int var60;
			for(var17 = 0; var17 < var8; ++var17) {
				var35.setNextPhase(var17 * 100 / (var35.width - 1));

				for(var12 = 0; var12 < var9; ++var12) {
					var52 = var48.generateNoise((double)var17, (double)var12) > 8.0D;
					boolean var56 = var51.generateNoise((double)var17, (double)var12) > 12.0D;
					var57 = var37[var17 + var12 * var8];
					var22 = (var57 * var35.depth + var12) * var35.width + var17;
					var59 = var35.blocksByteArray[((var57 + 1) * var35.depth + var12) * var35.width + var17] & 255;
					if((var59 == Block.waterMoving.blockID || var59 == Block.waterStill.blockID) && var57 <= var45 / 2 - 1 && var56) {
						var35.blocksByteArray[var22] = (byte)Block.gravel.blockID;
					}

					if(var59 == 0) {
						var60 = Block.grass.blockID;
						if(var57 <= var45 / 2 - 1 && var52) {
							var60 = Block.sand.blockID;
						}

						var35.blocksByteArray[var22] = (byte)var60;
					}
				}
			}

			var31.progressBar.displayProgressMessage("Planting..");
			var37 = var10;
			var35 = var31;
			var8 = var31.width;
			var9 = var31.width * var31.depth / 3000;

			for(var45 = 0; var45 < var9; ++var45) {
				var47 = var35.rand.nextInt(2);
				var35.setNextPhase(var45 * 50 / (var9 - 1));
				var50 = var35.rand.nextInt(var35.width);
				var17 = var35.rand.nextInt(var35.depth);

				for(var12 = 0; var12 < 10; ++var12) {
					var53 = var50;
					var20 = var17;

					for(var57 = 0; var57 < 5; ++var57) {
						var53 += var35.rand.nextInt(6) - var35.rand.nextInt(6);
						var20 += var35.rand.nextInt(6) - var35.rand.nextInt(6);
						if((var47 < 2 || var35.rand.nextInt(4) == 0) && var53 >= 0 && var20 >= 0 && var53 < var35.width && var20 < var35.depth) {
							var22 = var37[var53 + var20 * var8] + 1;
							boolean var68 = (var35.blocksByteArray[(var22 * var35.depth + var20) * var35.width + var53] & 255) == 0;
							boolean var63 = false;
							if(var68) {
								var60 = (var22 * var35.depth + var20) * var35.width + var53;
								if((var35.blocksByteArray[((var22 - 1) * var35.depth + var20) * var35.width + var53] & 255) == Block.grass.blockID) {
									if(var47 == 0) {
										var35.blocksByteArray[var60] = (byte)Block.plantYellow.blockID;
									} else if(var47 == 1) {
										var35.blocksByteArray[var60] = (byte)Block.plantRed.blockID;
									}
								}
							}
						}
					}
				}
			}

			var37 = var10;
			var35 = var31;
			var8 = var31.width;
			var45 = var31.width * var31.depth * var31.height / 2000;

			for(var47 = 0; var47 < var45; ++var47) {
				var50 = var35.rand.nextInt(2);
				var35.setNextPhase(var47 * 50 / (var45 - 1) + 50);
				var17 = var35.rand.nextInt(var35.width);
				var12 = var35.rand.nextInt(var35.height);
				var53 = var35.rand.nextInt(var35.depth);

				for(var20 = 0; var20 < 20; ++var20) {
					var57 = var17;
					var22 = var12;
					var59 = var53;

					for(var60 = 0; var60 < 5; ++var60) {
						var57 += var35.rand.nextInt(6) - var35.rand.nextInt(6);
						var22 += var35.rand.nextInt(2) - var35.rand.nextInt(2);
						var59 += var35.rand.nextInt(6) - var35.rand.nextInt(6);
						if((var50 < 2 || var35.rand.nextInt(4) == 0) && var57 >= 0 && var59 >= 0 && var22 >= 1 && var57 < var35.width && var59 < var35.depth && var22 < var37[var57 + var59 * var8] - 1 && (var35.blocksByteArray[(var22 * var35.depth + var59) * var35.width + var57] & 255) == 0) {
							int var64 = (var22 * var35.depth + var59) * var35.width + var57;
							if((var35.blocksByteArray[((var22 - 1) * var35.depth + var59) * var35.width + var57] & 255) == Block.stone.blockID) {
								if(var50 == 0) {
									var35.blocksByteArray[var64] = (byte)Block.mushroomBrown.blockID;
								} else if(var50 == 1) {
									var35.blocksByteArray[var64] = (byte)Block.mushroomRed.blockID;
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
			int[] var46 = var10;
			World var44 = var34;
			var35 = var31;
			var9 = var31.width;
			var45 = var31.width * var31.depth / 4000;

			for(var47 = 0; var47 < var45; ++var47) {
				var35.setNextPhase(var47 * 50 / (var45 - 1) + 50);
				var50 = var35.rand.nextInt(var35.width);
				var17 = var35.rand.nextInt(var35.depth);

				for(var12 = 0; var12 < 20; ++var12) {
					var53 = var50;
					var20 = var17;

					for(var57 = 0; var57 < 20; ++var57) {
						var53 += var35.rand.nextInt(6) - var35.rand.nextInt(6);
						var20 += var35.rand.nextInt(6) - var35.rand.nextInt(6);
						if(var53 >= 0 && var20 >= 0 && var53 < var35.width && var20 < var35.depth) {
							var22 = var46[var53 + var20 * var9] + 1;
							if(var35.rand.nextInt(4) == 0) {
								var44.growTrees(var53, var22, var20);
							}
						}
					}
				}
			}

			this.setLevel(var34);
		}
	}

	private void setLevel(World var1) {
		this.theWorld = var1;
		if(var1 != null) {
			var1.load();
			this.playerController.onWorldChange(var1);
			this.thePlayer = (EntityPlayer)var1.findSubclassOf(EntityPlayer.class);
		}

		if(this.thePlayer == null) {
			this.thePlayer = new EntityPlayer(var1);
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
