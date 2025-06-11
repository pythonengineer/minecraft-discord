package net.minecraft.client;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.ScaledResolution;
import net.lax1dude.eaglercraft.Touch;
import net.lax1dude.eaglercraft.crash.CrashReport;
import net.lax1dude.eaglercraft.internal.EnumPlatformType;
import net.lax1dude.eaglercraft.internal.ContextLostError;
import net.lax1dude.eaglercraft.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.lwjgl.BufferUtils;
import net.lax1dude.eaglercraft.lwjgl.LWJGLException;
import net.lax1dude.eaglercraft.lwjgl.input.Keyboard;
import net.lax1dude.eaglercraft.lwjgl.input.Mouse;
import net.lax1dude.eaglercraft.lwjgl.opengl.Display;
import net.lax1dude.eaglercraft.lwjgl.opengl.DisplayMode;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.lax1dude.eaglercraft.touch.TouchControls;
import net.lax1dude.eaglercraft.touch.TouchOverlayRenderer;
import net.lax1dude.eaglercraft.util.ReportedException;
import net.minecraft.client.controller.PlayerController;
import net.minecraft.client.controller.PlayerControllerCreative;
import net.minecraft.client.controller.PlayerControllerSP;
import net.minecraft.client.effect.EffectRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.player.MovementInputFromOptions;
import net.minecraft.client.render.EntityRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.texture.TextureFlamesFX;
import net.minecraft.client.render.texture.TextureLavaFX;
import net.minecraft.client.render.texture.TextureWaterFX;
import net.minecraft.client.render.texture.TextureWaterFlowFX;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.generator.LevelGenerator;
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
    public IProgressUpdate loadingScreen = new LoadingScreenRenderer(this);
	public EntityRenderer entityRenderer = new EntityRenderer(this);
	private int ticksRan = 0;
	private int leftClickCounter = 0;
    public String loadMapUser = null;
    public int loadMapID = 0;
	public GuiIngame ingameGUI;
    public boolean skipRenderWorld = false;
	public MovingObjectPosition objectMouseOver;
	public GameSettings options;
    public SoundManager sndManager;
    private String server;
    private TextureWaterFX textureWaterFX;
    private TextureLavaFX textureLavaFX;
    volatile boolean running;
	public String debug;
    public boolean ingameFocus;
    private int prevFrameTime;
    public boolean renderRain;
    public boolean mouseGrabSupported = false;
    private int rightClickDelayTimer;
    public float displayDPI = 1.0f;
    public ScaledResolution scaledResolution;
    public TouchOverlayRenderer touchOverlayRenderer;
    public static Minecraft minecraft;

	public Minecraft(int width, int height, boolean fullScreen) {
		new ModelBiped(0.0F);
		this.objectMouseOver = null;
        this.sndManager = new SoundManager();
		this.server = null;
        this.textureWaterFX = new TextureWaterFX();
        this.textureLavaFX = new TextureLavaFX();
		this.running = false;
		this.debug = "";
        this.ingameFocus = false;
        this.prevFrameTime = 0;
        this.renderRain = false;
		this.displayWidth = width;
		this.displayHeight = height;
		this.fullScreen = fullScreen;
        this.options = new GameSettings(this);
        this.sndManager.loadSoundSettings(this.options);
        this.sndManager.registerSounds();
	}

    public final void setServer(String var1, int var2) {
        this.server = var1;
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
                this.setIngameNotInFocus();
				((GuiScreen)var1).setWorldAndResolution(this,
				        this.scaledResolution.getScaledWidth(), this.scaledResolution.getScaledHeight());
                this.skipRenderWorld = false;
			} else {
				this.setIngameFocus();
			}
		}
	}

	public final void shutdownMinecraftApplet() {
        this.sndManager.closeMinecraft();
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

			this.renderEngine = new RenderEngine(this.options);
            this.renderEngine.registerTextureFX(this.textureLavaFX);
            this.renderEngine.registerTextureFX(this.textureWaterFX);
            this.renderEngine.registerTextureFX(new TextureWaterFlowFX());
            this.renderEngine.registerTextureFX(new TextureFlamesFX(0));
            this.renderEngine.registerTextureFX(new TextureFlamesFX(1));
			this.fontRenderer = new FontRenderer(this.options, "/default.png", this.renderEngine);
			IntBuffer var8 = BufferUtils.createIntBuffer(256);
			var8.clear().limit(256);
			this.renderGlobal = new RenderGlobal(this, this.renderEngine);
			GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);

            this.mouseGrabSupported = Mouse.isMouseGrabSupported();
            this.touchOverlayRenderer = new TouchOverlayRenderer();
            this.scaledResolution = new ScaledResolution(this);
            PointerInputAbstraction.init(this);

			if(this.server != null && this.session != null) {
				World var43 = new World();
				var43.generate(8, 8, 8, new byte[512]);
				this.setLevel(var43);
			} else {
                this.generateNewLevel(1, 0, 1, 0);
			}

			this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);

			this.ingameGUI = new GuiIngame(this);
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
                        this.timer.updateTimer();

                        Display.checkContextLost();

                        PointerInputAbstraction.runGameLoop();

						for(int var38 = 0; var38 < this.timer.elapsedTicks; ++var38) {
							++this.ticksRan;
							this.runTick();
                            if (var38 < this.timer.elapsedTicks - 1) {
                                PointerInputAbstraction.runGameLoop();
                            }
						}

                        GL11.optimize();
                        this.sndManager.setListener(this.thePlayer, this.timer.renderPartialTicks);
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						this.playerController.setPartialTime(this.timer.renderPartialTicks);
                        this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);
                        this.entityRenderer.setupOverlayRendering();
                        touchOverlayRenderer.render(this.displayWidth, this.displayHeight, this.scaledResolution);
                        GL11.disableBlend();

						Thread.yield();
                        this.updateDisplay();

                        if(!Display.isActive()) {
                            if(this.fullScreen) {
                                this.toggleFullScreen();
                            }

                            Thread.sleep(10L);
                        }

						if(this.options.limitFramerate) {
							Thread.sleep(5L);
						}

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
        } catch (ContextLostError err) {
            throw err;
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

    public final void toggleFullScreen() {
        try {
            this.fullScreen = !this.fullScreen;
            System.out.println("Toggle fullscreen!");
            if(this.fullScreen) {
                Display.setDisplayMode(Display.getDesktopDisplayMode());
                this.displayWidth = Display.getDisplayMode().getWidth();
                this.displayHeight = Display.getDisplayMode().getHeight();
            } else {
                this.displayWidth = Display.getWidth();
                this.displayHeight = Display.getHeight();

                Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
            }

            this.setIngameNotInFocus();
            Display.setFullscreen(this.fullScreen);
            this.resize(this.displayWidth, this.displayHeight);
            this.updateDisplay();
            Thread.sleep(1000L);
            if(this.fullScreen) {
                this.setIngameFocus();
            }

            System.out.println("Size: " + this.displayWidth + ", " + this.displayHeight);
        } catch (Exception var2) {
            var2.printStackTrace();
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
            if(!this.ingameFocus) {
                this.ingameFocus = true;
                if (!touch && mouseGrabSupported) {
                    Mouse.setGrabbed(true);
                }
                this.displayGuiScreen((GuiScreen) null);
                this.prevFrameTime = this.ticksRan + 10000;
            }
        }
	}

    public void setIngameNotInFocus() {
        if (this.ingameFocus) {
            this.thePlayer.movementInput.resetKeyState();
            this.ingameFocus = false;
            if (!PointerInputAbstraction.isTouchMode() && mouseGrabSupported) {
                Mouse.setGrabbed(false);
            }
        }
    }

	public final void displayInGameMenu() {
		if(this.currentScreen == null) {
			this.displayGuiScreen(new GuiIngameMenu());
		}
	}

    private void clickMouse(int var1) {
        if(var1 != 0 || this.leftClickCounter <= 0) {
            if(var1 == 0) {
                this.entityRenderer.itemRenderer.swingItem();
            } else {
                this.rightClickDelayTimer = 4;
            }

            World var4;
            if(var1 == 1) {
                ItemStack var2 = this.thePlayer.inventory.getCurrentItem();
                if(var2 != null) {
                    EntityPlayerSP var5 = this.thePlayer;
                    var4 = this.theWorld;
                    if(var2.getItem().onItemRightClick(var2, var4, var5)) {
                        if(var2.stackSize == 0) {
                            this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
                        }

                        this.entityRenderer.itemRenderer.resetEquippedProgress2();
                    }
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
                    int var10 = this.objectMouseOver.blockX;
                    int var3 = this.objectMouseOver.blockY;
                    int var11 = this.objectMouseOver.blockZ;
                    int var12 = this.objectMouseOver.sideHit;
                    Block var6 = Block.blocksList[this.theWorld.getBlockId(var10, var3, var11)];
                    if(var1 == 0) {
                        this.theWorld.extinguishFire(var10, var3, var11, this.objectMouseOver.sideHit);
                        if(var6 != Block.bedrock) {
                            this.playerController.clickBlock(var10, var3, var11);
                            return;
                        }
                    } else {
                        ItemStack var9 = this.thePlayer.inventory.getCurrentItem();
                        int var13 = this.theWorld.getBlockId(var10, var3, var11);
                        if(var13 > 0 && Block.blocksList[var13].blockActivated(this.theWorld, var10, var3, var11, this.thePlayer)) {
                            return;
                        }

                        if(var9 == null) {
                            return;
                        }

                        var13 = var9.stackSize;
                        int var7 = var11;
                        var4 = this.theWorld;
                        var9.getItem().onItemUse(var9, var4, var10, var3, var7, var12);
                        if(var9.stackSize == 0) {
                            this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
                            return;
                        }

                        if(var9.stackSize != var13) {
                            this.entityRenderer.itemRenderer.resetEquippedProgress();
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
        this.ingameGUI.addChatMessage();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
        this.renderEngine.updateDynamicTextures();

		if(this.currentScreen == null && this.thePlayer != null && this.thePlayer.health <= 0) {
			this.displayGuiScreen((GuiScreen)null);
		}

		int var4;
		int var14;
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
                            int var2 = this.theWorld.getBlockId(this.objectMouseOver.blockX, this.objectMouseOver.blockY, this.objectMouseOver.blockZ);
                            if(var2 == Block.grass.blockID) {
                                var2 = Block.dirt.blockID;
                            }

                            if(var2 == Block.stairDouble.blockID) {
                                var2 = Block.stairSingle.blockID;
                            }

                            if(var2 == Block.bedrock.blockID) {
                                var2 = Block.stone.blockID;
                            }

                            this.thePlayer.inventory.getFirstEmptyStack(var2);
                        }
                    }

                    var14 = Mouse.getEventDWheel();
                    if(var14 != 0) {
                        int var2 = var14;
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

                if(this.currentScreen == null && !(touch || Mouse.isActuallyGrabbed()) && (touch || Mouse.getEventButtonState())) {
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
                    if(Keyboard.getEventKey() == Keyboard.KEY_F11) {
                        this.toggleFullScreen();
                    } else {
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
                                this.renderRain = !this.renderRain;
                            }

                            if(Keyboard.getEventKey() == this.options.keyBindInventory.keyCode) {
                                this.playerController.openInventory();
                            }

                            if(Keyboard.getEventKey() == this.options.keyBindDrop.keyCode) {
                                this.thePlayer.dropPlayerItemWithRandomChoice(this.thePlayer.inventory.decrStackSize(this.thePlayer.inventory.currentItem, 1));
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
                if((Mouse.isButtonDown(0) || miningTouch) && (float)(this.ticksRan - this.prevFrameTime) >= this.timer.ticksPerSecond / 4.0F && this.ingameFocus) {
                    this.clickMouse(0);
                    this.prevFrameTime = this.ticksRan;
                }

                if((Mouse.isButtonDown(1) || miningTouch) && (float)(this.ticksRan - this.prevFrameTime) >= this.timer.ticksPerSecond / 4.0F && this.ingameFocus) {
                    this.clickMouse(1);
                    this.prevFrameTime = this.ticksRan;
                }
            }

            boolean var20 = this.currentScreen == null && (Mouse.isButtonDown(0) || miningTouch) && !useTouch && this.ingameFocus;
            if(!this.playerController.isInTestMode && this.leftClickCounter <= 0) {
                if(var20 && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == 0) {
                    int var2 = this.objectMouseOver.blockX;
                    int var8 = this.objectMouseOver.blockY;
                    var4 = this.objectMouseOver.blockZ;
                    this.playerController.sendBlockRemoving(var2, var8, var4, this.objectMouseOver.sideHit);
                    this.effectRenderer.addBlockHitEffects(var2, var8, var4, this.objectMouseOver.sideHit);
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
            this.entityRenderer.updateRenderer();
            this.renderGlobal.updateClouds();
            this.theWorld.updateEntities();
            this.theWorld.tick();
            this.theWorld.randomDisplayUpdates((int)this.thePlayer.posX, (int)this.thePlayer.posY, (int)this.thePlayer.posZ);
            this.effectRenderer.updateEffects();
        }

	}

    public final void generateNewLevel(int var1, int var2, int var3, int var4) {
        String var5 = this.session != null ? this.session.username : "anonymous";
        LevelGenerator var6 = new LevelGenerator(this.loadingScreen);
        var6.islandGen = var3 == 1;
        var6.floatingGen = var3 == 2;
        var6.flatGen = var3 == 3;
        var6.levelType = var4;
        var1 = 128 << var1;
        var3 = var1;
        short var8 = 64;
        if(var2 == 1) {
            var1 /= 2;
            var3 <<= 1;
        } else if(var2 == 2) {
            var1 /= 2;
            var3 = var1;
            var8 = 256;
        }

        World var7 = var6.generate(var5, var1, var3, var8);
        this.setLevel(var7);
    }

    public final void setLevel(World var1) {
		this.theWorld = var1;
		if(var1 != null) {
			var1.load();
			this.playerController.onWorldChange(var1);
			this.thePlayer = (EntityPlayerSP)var1.findSubclassOf(EntityPlayerSP.class);
            var1.playerEntity = this.thePlayer;
		}

		if(this.thePlayer == null) {
            this.thePlayer = new EntityPlayerSP(this, var1);
			this.thePlayer.preparePlayerToSpawn();
            this.playerController.flipPlayer(this.thePlayer);
			if(var1 != null) {
                var1.spawnEntityInWorld(this.thePlayer);
				var1.playerEntity = this.thePlayer;
			}
		}

		if(this.thePlayer != null) {
            this.thePlayer.movementInput = new MovementInputFromOptions(this.options);
            this.playerController.onRespawn(this.thePlayer);
		}

		if(this.renderGlobal != null) {
            this.renderGlobal.changeWorld(var1);
		}

		if(this.effectRenderer != null) {
            this.effectRenderer.clearEffects(var1);
		}

        this.textureWaterFX.textureId = 0;
        this.textureLavaFX.textureId = 0;
        int var2 = this.renderEngine.getTexture("/water.png");
        if(var1.defaultFluid == Block.waterMoving.blockID) {
            this.textureWaterFX.textureId = var2;
        } else {
            this.textureLavaFX.textureId = var2;
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
        minecraft.session = new Session(username, "");
        minecraft.run();
    }
}
