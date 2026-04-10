package net.minecraft.client;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
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
import net.lax1dude.eaglercraft.util.MathHelper;
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
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.container.GuiInventory;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.player.MovementInputFromOptions;
import net.minecraft.client.render.EntityRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.texture.TextureFlamesFX;
import net.minecraft.client.render.texture.TextureGearsFX;
import net.minecraft.client.render.texture.TextureLavaFX;
import net.minecraft.client.render.texture.TextureWaterFX;
import net.minecraft.client.render.texture.TextureWaterFlowFX;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public final class Minecraft implements Runnable {
	public PlayerController playerController = new PlayerControllerSP(this);
	private boolean fullscreen = false;
	public int displayWidth;
	public int displayHeight;
    private OpenGlCapsChecker glCapabilities;
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
    public String loadMapUser = null;
    public int loadMapID = 0;
	public GuiIngame ingameGUI;
    public boolean skipRenderWorld = false;
	public MovingObjectPosition objectMouseOver;
    public GameSettings gameSettings;
    public SoundManager sndManager;
    private String serverIP;
    private TextureWaterFX textureWaterFX;
    private TextureLavaFX textureLavaFX;
    volatile boolean running;
	public String debug;
    public boolean inGameHasFocus;
    private long systemTime;
    private int mouseTicksRan;
    public boolean isRaining;
    public boolean mouseGrabSupported = false;
    private int rightClickDelayTimer;
    public float displayDPI = 1.0f;
    public ScaledResolution scaledResolution;
    public TouchOverlayRenderer touchOverlayRenderer;
    public static Minecraft minecraft;

	public Minecraft(int width, int height, boolean fullscreen) {
		new ModelBiped(0.0F);
		this.objectMouseOver = null;
        this.sndManager = new SoundManager();
		this.serverIP = null;
        this.textureWaterFX = new TextureWaterFX();
        this.textureLavaFX = new TextureLavaFX();
		this.running = false;
		this.debug = "";
        this.inGameHasFocus = false;
        this.systemTime = EagRuntime.currentTimeMillis();
        this.mouseTicksRan = 0;
        this.isRaining = false;
		this.displayWidth = width;
		this.displayHeight = height;
		this.fullscreen = fullscreen;
        this.gameSettings = new GameSettings(this);
        this.sndManager.loadSoundSettings(this.gameSettings);
        this.sndManager.registerSounds();
	}

    public final void setServer(String host, int port) {
        this.serverIP = host;
    }

	public final void setGuiScreen(GuiScreen screen) {
		if(!(this.currentScreen instanceof GuiErrorScreen)) {
			if(this.currentScreen != null) {
                this.currentScreen.onGuiClosed();
			}

            if(screen == null && this.theWorld == null) {
                screen = new GuiMainMenu();
            } else if(screen == null && this.thePlayer.health <= 0) {
                screen = new GuiGameOver();
            }

			this.currentScreen = (GuiScreen)screen;
			if(screen != null) {
                this.setIngameNotInFocus();
				((GuiScreen)screen).setWorldAndResolution(this,
				        this.scaledResolution.getScaledWidth(), this.scaledResolution.getScaledHeight());
                this.skipRenderWorld = false;
			} else {
				this.setIngameFocus();
			}
		}
	}

	public final void shutdownMinecraftApplet() {
        try {
            System.out.println("Stopping!");
            this.changeWorld((World)null, "");
            this.sndManager.closeMinecraft();
            Mouse.destroy();
            Keyboard.destroy();
        } finally {
            Display.destroy();
        }
	}

	public final void run() {
		this.running = true;

		try {
			Minecraft minecraft1 = this;
	        this.displayWidth = Display.getVisualViewportW() != 0 ? Display.getVisualViewportW() : this.displayWidth;
	        this.displayHeight = Display.getVisualViewportH() != 0 ? Display.getVisualViewportH() : this.displayHeight;
			if(this.fullscreen) {
				Display.setFullscreen(true);
				this.displayWidth = Display.getDisplayMode().getWidth();
				this.displayHeight = Display.getDisplayMode().getHeight();
			} else {
				Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
			}

	        this.displayDPI = Math.max(Math.min(Display.getDPI(), 2.0f), 1.0f);

			Display.setTitle("Minecraft Infdev");

			try {
				Display.create();
                System.out.println("GLES version: " + GL11.checkOpenGLESVersion());
                System.out.println("GL RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
                System.out.println("GL VENDOR: " + GL11.glGetString(GL11.GL_VENDOR));
                System.out.println("GL VERSION: " + GL11.glGetString(GL11.GL_VERSION));
                Mouse.destroy();
			} catch (Exception lWJGLException17) {
			    lWJGLException17.printStackTrace();

				try {
					Thread.sleep(1000L);
				} catch (InterruptedException interruptedException16) {
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
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			GL11.glCullFace(GL11.GL_BACK);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);

            this.glCapabilities = new OpenGlCapsChecker();
			this.renderEngine = new RenderEngine(this.gameSettings);
            this.renderEngine.registerTextureFX(this.textureLavaFX);
            this.renderEngine.registerTextureFX(this.textureWaterFX);
            this.renderEngine.registerTextureFX(new TextureWaterFlowFX());
            this.renderEngine.registerTextureFX(new TextureFlamesFX(0));
            this.renderEngine.registerTextureFX(new TextureFlamesFX(1));
            this.renderEngine.registerTextureFX(new TextureGearsFX(0));
            this.renderEngine.registerTextureFX(new TextureGearsFX(1));
			this.fontRenderer = new FontRenderer(this.gameSettings, "/default.png", this.renderEngine);
			BufferUtils.createIntBuffer(256).clear().limit(256);
			this.renderGlobal = new RenderGlobal(this, this.renderEngine);
			GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);

            this.mouseGrabSupported = Mouse.isMouseGrabSupported();
            this.touchOverlayRenderer = new TouchOverlayRenderer();
            this.scaledResolution = new ScaledResolution(this);
            PointerInputAbstraction.init(this);

			if(this.serverIP != null && this.session != null) {
                this.changeWorld((World)null, "");
			} else {
                this.setGuiScreen(new GuiMainMenu());
			}

			this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);

			this.ingameGUI = new GuiIngame(this);
		} catch (Exception exception22) {
		    exception22.printStackTrace();
            EagRuntime.showPopup("Failed to start Minecraft");
			return;
		}

		long j23 = EagRuntime.currentTimeMillis();
		int i3 = 0;

		try {
			while(this.running) {
				if(Display.isCloseRequested()) {
					this.running = false;
				}

                if(this.isGamePaused) {
                    float f4 = this.timer.renderPartialTicks;
                    this.timer.updateTimer();
                    this.timer.renderPartialTicks = f4;
                } else {
                    this.timer.updateTimer();
                }

                Display.checkContextLost();

                PointerInputAbstraction.runGameLoop();
                this.gameSettings.touchscreen = PointerInputAbstraction.isTouchMode();

                int i26 = 0;

                while(true) {
                    if(i26 >= this.timer.elapsedTicks) {
                        if(this.isGamePaused) {
                            this.timer.renderPartialTicks = 1.0F;
                        }

                        GL11.optimize();
                        this.sndManager.setListener(this.thePlayer, this.timer.renderPartialTicks);
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        if(this.theWorld != null) {
                            while(this.theWorld.updatingLighting()) {
                            }
                        }

                        this.playerController.setPartialTime(this.timer.renderPartialTicks);
                        this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);
                        this.entityRenderer.setupOverlayRendering();
                        touchOverlayRenderer.render(this.displayWidth, this.displayHeight, this.scaledResolution);
                        GL11.disableBlend();

                        Thread.yield();
                        this.updateDisplay();

                        if(!Display.isActive()) {
                            if(this.fullscreen) {
                                this.toggleFullscreen();
                            }

                            Thread.sleep(10L);
                        }

                        if(this.gameSettings.limitFramerate) {
                            Thread.sleep(5L);
                        }

                        ++i3;
                        this.isGamePaused = this.currentScreen != null && this.currentScreen.doesGuiPauseGame();
                        break;
                    }

                    ++this.ticksRan;
                    this.tick();
                    if (i26 < this.timer.elapsedTicks - 1) {
                        PointerInputAbstraction.runGameLoop();
                    }
                    ++i26;
                }

				while(EagRuntime.currentTimeMillis() >= j23 + 1000L) {
					this.debug = i3 + " fps, " + WorldRenderer.chunkUpdates + " chunk updates";
					WorldRenderer.chunkUpdates = 0;
					j23 += 1000L;
					i3 = 0;
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
        return (float)this.getLimitFramerate() < 260;
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

                this.setResolution(this.displayWidth, this.displayHeight);
            }
        }

    }

    public final void toggleFullscreen() {
        try {
            this.fullscreen = !this.fullscreen;
            System.out.println("Toggle fullscreen!");
            if(this.fullscreen) {
                Display.setDisplayMode(Display.getDesktopDisplayMode());
                this.displayWidth = Display.getDisplayMode().getWidth();
                this.displayHeight = Display.getDisplayMode().getHeight();
            } else {
                this.displayWidth = Display.getWidth();
                this.displayHeight = Display.getHeight();

                Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
            }

            this.setIngameNotInFocus();
            Display.setFullscreen(this.fullscreen);
            this.setResolution(this.displayWidth, this.displayHeight);
            this.updateDisplay();
            Thread.sleep(1000L);
            if(this.fullscreen) {
                this.setIngameFocus();
            }

            System.out.println("Size: " + this.displayWidth + ", " + this.displayHeight);
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    private void setResolution(int width, int height) {
        this.displayWidth = Math.max(1, width);
        this.displayHeight = Math.max(1, height);
        this.scaledResolution = new ScaledResolution(this);
        if (this.currentScreen != null) {
            this.currentScreen.setWorldAndResolution(this, 
                    this.scaledResolution.getScaledWidth(), this.scaledResolution.getScaledHeight());
        }
    }

    public final void shutdown() {
        this.running = false;
    }

	public final void setIngameFocus() {
        boolean touch = PointerInputAbstraction.isTouchMode();
        if (touch || Display.isActive()) {
            if(!this.inGameHasFocus) {
                this.inGameHasFocus = true;
                if (!touch && mouseGrabSupported) {
                    Mouse.setGrabbed(true);
                }
                this.setGuiScreen((GuiScreen) null);
                this.mouseTicksRan = this.ticksRan + 10000;
            }
        }
	}

    public void setIngameNotInFocus() {
        if (this.inGameHasFocus) {
            if(this.thePlayer != null) {
                this.thePlayer.movementInput.resetPlayerKeyState();
            }

            this.inGameHasFocus = false;
            if (!PointerInputAbstraction.isTouchMode() && mouseGrabSupported) {
                Mouse.setGrabbed(false);
            }
        }
    }

	public final void displayInGameMenu() {
		if(this.currentScreen == null) {
			this.setGuiScreen(new GuiIngameMenu());
		}
	}

    private void clickMouse(int mouseButton) {
        if(mouseButton != 0 || this.leftClickCounter <= 0) {
            if(mouseButton == 0) {
                this.entityRenderer.itemRenderer.swing();
            }

            ItemStack itemStack2;
            int i3;
            if(mouseButton == 1 && (itemStack2 = this.thePlayer.inventory.getCurrentItem()) != null) {
                i3 = itemStack2.stackSize;
                EntityPlayerSP entityPlayerSP7 = this.thePlayer;
                World world5 = this.theWorld;
                ItemStack itemStack4;
                if((itemStack4 = itemStack2.getItem().onItemRightClick(itemStack2, world5, entityPlayerSP7)) != itemStack2 || itemStack4 != null && itemStack4.stackSize != i3) {
                    this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = itemStack4;
                    this.entityRenderer.itemRenderer.resetEquippedProgress();
                    if(itemStack4.stackSize == 0) {
                        this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
                    }
                }
            }

            if(this.objectMouseOver == null) {
                if(mouseButton == 0 && !(this.playerController instanceof PlayerControllerCreative)) {
                    this.leftClickCounter = 10;
                }

            } else {
                if(this.objectMouseOver.typeOfHit == 1) {
                    if(mouseButton == 0) {
                        Entity entity15 = this.objectMouseOver.entityHit;
                        EntityPlayerSP entityPlayerSP13 = this.thePlayer;
                        ItemStack itemStack9;
                        InventoryPlayer inventoryPlayer12;
                        int i10000 = (itemStack9 = (inventoryPlayer12 = this.thePlayer.inventory).getStackInSlot(inventoryPlayer12.currentItem)) != null ? Item.itemsList[itemStack9.itemID].getDamageVsEntity() : 1;
                        int i19 = i10000;
                        if(i10000 > 0) {
                            entity15.attackEntityFrom(entityPlayerSP13, i19);
                            if((itemStack2 = entityPlayerSP13.inventory.getCurrentItem()) != null && entity15 instanceof EntityLiving) {
                                EntityLiving entityLiving8 = (EntityLiving)entity15;
                                Item.itemsList[itemStack2.itemID].hitEntity(itemStack2);
                                if(itemStack2.stackSize <= 0) {
                                    entityPlayerSP13.destroyCurrentEquippedItem();
                                }
                            }
                        }

                        return;
                    }
                } else if(this.objectMouseOver.typeOfHit == 0) {
                    int i11 = this.objectMouseOver.blockX;
                    i3 = this.objectMouseOver.blockY;
                    int i14 = this.objectMouseOver.blockZ;
                    int i16 = this.objectMouseOver.sideHit;
                    Block block6 = Block.blocksList[this.theWorld.getBlockId(i11, i3, i14)];
                    if(mouseButton == 0) {
                        this.theWorld.onBlockHit(i11, i3, i14, this.objectMouseOver.sideHit);
                        if(block6 != Block.bedrock) {
                            this.playerController.clickBlock(i11, i3, i14);
                            return;
                        }
                    } else {
                        ItemStack mouseButton1 = this.thePlayer.inventory.getCurrentItem();
                        int i18;
                        if((i18 = this.theWorld.getBlockId(i11, i3, i14)) > 0 && Block.blocksList[i18].blockActivated(this.theWorld, i11, i3, i14, this.thePlayer)) {
                            return;
                        }

                        if(mouseButton1 == null) {
                            return;
                        }

                        i18 = mouseButton1.stackSize;
                        int i21 = i16;
                        World world20 = this.theWorld;
                        EntityPlayerSP entityPlayerSP17 = this.thePlayer;
                        if(mouseButton1.getItem().onItemUse(mouseButton1, entityPlayerSP17, world20, i11, i3, i14, i21)) {
                            this.entityRenderer.itemRenderer.swing();
                        }

                        if(mouseButton1.stackSize == 0) {
                            this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
                            return;
                        }

                        if(mouseButton1.stackSize != i18) {
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

	private void tick() {
        if (this.rightClickDelayTimer > 0) {
            --this.rightClickDelayTimer;
        }

        if(this.currentScreen == null && !PointerInputAbstraction.isTouchMode() && !Mouse.isMouseGrabbed()) {
            this.setIngameNotInFocus();
            this.displayInGameMenu();
        }

        this.ingameGUI.updateTick();
        if(!this.isGamePaused && this.theWorld != null) {
            this.playerController.updateController();
        }

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png")); 
		if(!this.isGamePaused) {
            this.renderEngine.updateDynamicTextures();
        }

		if(this.currentScreen == null && this.thePlayer != null && this.thePlayer.health <= 0) {
			this.setGuiScreen((GuiScreen)null);
		}

		int i4;
		int i1;
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

                long var1 = EagRuntime.currentTimeMillis() - this.systemTime;
                if (!touch && var1 <= 200L) {
                    if (Mouse.getEventButtonState()) {
                        PointerInputAbstraction.enterMouseModeHook();
                    }

                    i1 = Mouse.getEventDWheel();
                    if(i1 != 0) {
                        int i2 = i1;
                        InventoryPlayer inventoryPlayer5 = this.thePlayer.inventory;
                        if(i1 > 0) {
                            i2 = 1;
                        }

                        if(i2 < 0) {
                            i2 = -1;
                        }

                        for(inventoryPlayer5.currentItem -= i2; inventoryPlayer5.currentItem < 0; inventoryPlayer5.currentItem += 9) {
                        }

                        while(inventoryPlayer5.currentItem >= 9) {
                            inventoryPlayer5.currentItem -= 9;
                        }
                    }

                    if(this.currentScreen == null) {
                        if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
                            this.clickMouse(0);
                            this.mouseTicksRan = this.ticksRan;
                        }

                        if(Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
                            this.clickMouse(1);
                            this.mouseTicksRan = this.ticksRan;
                        }

                        if(Mouse.getEventButton() == 2 && Mouse.getEventButtonState() && this.objectMouseOver != null) {
                            int i2 = this.theWorld.getBlockId(this.objectMouseOver.blockX, this.objectMouseOver.blockY, this.objectMouseOver.blockZ);
                            if(i2 == Block.grass.blockID) {
                                i2 = Block.dirt.blockID;
                            }

                            if(i2 == Block.stairDouble.blockID) {
                                i2 = Block.stairSingle.blockID;
                            }

                            if(i2 == Block.bedrock.blockID) {
                                i2 = Block.stone.blockID;
                            }

                            this.thePlayer.inventory.changeCurrentItem(i2);
                        }
                    } else if(this.currentScreen != null) {
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
                        this.toggleFullscreen();
                    } else {
                        if(this.currentScreen != null) {
                            this.currentScreen.handleKeyboardInput();
                        } else {
                            if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                                this.displayInGameMenu();
                            }

                            if(this.playerController instanceof PlayerControllerCreative) {
                            }

                            if(Keyboard.getEventKey() == Keyboard.KEY_F5) {
                                this.gameSettings.thirdPersonView = !this.gameSettings.thirdPersonView;
                                this.isRaining = !this.isRaining;
                            }

                            if(Keyboard.getEventKey() == this.gameSettings.keyBindInventory.keyCode) {
                                this.setGuiScreen(new GuiInventory(this.thePlayer.inventory));
                            }

                            if(Keyboard.getEventKey() == this.gameSettings.keyBindDrop.keyCode) {
                                this.thePlayer.dropItem(this.thePlayer.inventory.decrStackSize(this.thePlayer.inventory.currentItem, 1), false);
                            }
                        }

                        for(i1 = 0; i1 < 9; ++i1) {
                            if(Keyboard.getEventKey() == i1 + 2) {
                                this.thePlayer.inventory.currentItem = i1;
                            }
                        }

                        if(Keyboard.getEventKey() == this.gameSettings.keyBindToggleFog.keyCode) {
                            this.gameSettings.setOptionValue(4, !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ? 1 : -1);
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
                    this.mouseTicksRan = this.ticksRan;
                }
                wasMiningTouch = true;
            }
            wasMiningTouch = miningTouch;

            if (miningTouch && useTouch && this.rightClickDelayTimer == 0) {
                this.clickMouse(1);
            }

            if(this.currentScreen == null) {
                if((Mouse.isButtonDown(0) || miningTouch) && (float)(this.ticksRan - this.mouseTicksRan) >= this.timer.ticksPerSecond / 4.0F && this.inGameHasFocus) {
                    this.clickMouse(0);
                    this.mouseTicksRan = this.ticksRan;
                }

                if((Mouse.isButtonDown(1) || miningTouch) && (float)(this.ticksRan - this.mouseTicksRan) >= this.timer.ticksPerSecond / 4.0F && this.inGameHasFocus) {
                    this.clickMouse(1);
                    this.mouseTicksRan = this.ticksRan;
                }
            }

            boolean z3 = this.currentScreen == null && (Mouse.isButtonDown(0) || miningTouch) && !useTouch && this.inGameHasFocus;
            if(!this.playerController.isInTestMode && this.leftClickCounter <= 0) {
                if(z3 && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == 0) {
                    int i2 = this.objectMouseOver.blockX;
                    int i8 = this.objectMouseOver.blockY;
                    i4 = this.objectMouseOver.blockZ;
                    this.playerController.sendBlockRemoving(i2, i8, i4, this.objectMouseOver.sideHit);
                    this.effectRenderer.addBlockHitEffects(i2, i8, i4, this.objectMouseOver.sideHit);
                } else {
                    this.playerController.resetBlockRemoving();
                }
            }
		}

		if(this.currentScreen != null) {
			this.mouseTicksRan = this.ticksRan + 10000;
		}

		if(this.currentScreen != null) {
            this.currentScreen.handleInput();
			if(this.currentScreen != null) {
				this.currentScreen.updateScreen();
			}
		}

        if(this.theWorld != null) {
            this.theWorld.difficultySetting = this.gameSettings.difficulty;
            if(!this.isGamePaused) {
                this.entityRenderer.updateRenderer();
            }

            if(!this.isGamePaused) {
                this.renderGlobal.updateClouds();
            }

            if(!this.isGamePaused) {
                this.theWorld.updateEntities();
            }

            if(!this.isGamePaused) {
                this.theWorld.tickUpdates();
            }

            if(!this.isGamePaused) {
                this.theWorld.randomDisplayUpdates(MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
            }

            if(!this.isGamePaused) {
                this.effectRenderer.updateEffects();
            }
        }

        this.systemTime = EagRuntime.currentTimeMillis();
    }

    public final void startWorld(String worldName) {
        this.changeWorld((World)null, "");
        System.gc();
        World world3 = new World(worldName);
        if(world3.isNewWorld) {
            this.changeWorld(world3, "Generating level");
        } else {
            this.changeWorld(world3, "Loading level");
        }

        world3.saveWorld(false);

        this.loadingScreen.displayLoadingString("Preparing lights");
        int i4 = 0;

        while(world3.lightUpdatesNeeded() > 0) {
            this.loadingScreen.setLoadingProgress(i4++ % 100);
            world3.updatingLighting();
        }

    }

    public final void changeWorld1(World world) {
        this.changeWorld((World)null, "");
    }

    private void changeWorld(World world, String worldName) {
        if(this.theWorld != null) {
            this.theWorld.saveWorldIndirectly();
        }

		this.theWorld = world;
		if(world != null) {
            this.thePlayer = null;
            world.playerEntity = this.thePlayer;
            this.preloadWorld(worldName);
            if(this.thePlayer == null) {
                this.thePlayer = new EntityPlayerSP(this, world, this.session);
                this.thePlayer.preparePlayerToSpawn();
            }

            this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
            if(this.renderGlobal != null) {
                this.renderGlobal.changeWorld(world);
            }

            if(this.effectRenderer != null) {
                this.effectRenderer.clearEffects(world);
            }

            this.playerController.onRespawn(this.thePlayer);
            world.playerEntity = this.thePlayer;
            world.joinPlayerInWorld();
        }

		System.gc();
        this.systemTime = 0L;
	}

    private void preloadWorld(String worldName) {
        this.loadingScreen.setTitle(worldName);
        this.loadingScreen.displayLoadingString("Preparing chunks");

        for(int worldName1 = -196; worldName1 <= 196; worldName1 += 16) {
            this.loadingScreen.setLoadingProgress((worldName1 + 196) * 100 / 392);
            int i2 = this.theWorld.spawnX;
            int i3 = this.theWorld.spawnZ;
            if(this.theWorld.playerEntity != null) {
                i2 = (int)this.theWorld.playerEntity.posX;
                i3 = (int)this.theWorld.playerEntity.posZ;
            }

            for(int i4 = -196; i4 <= 196; i4 += 16) {
                this.theWorld.getBlockId(i2 + worldName1, 64, i3 + i4);
            }
        }

        this.theWorld.dropOldChunks();
    }

    public final void installResource(String resource, String resourceDir) {
        int i3 = resource.indexOf("/");
        String string4 = resource.substring(0, i3);
        resource = resource.substring(i3 + 1);
        if(string4.equalsIgnoreCase("sound")) {
            this.sndManager.addSound(resource, resourceDir);
        } else if(string4.equalsIgnoreCase("newsound")) {
            this.sndManager.addSound(resource, resourceDir);
        } else {
            if(string4.equalsIgnoreCase("music")) {
                this.sndManager.addMusic(resource, resourceDir);
            }

        }
    }

    public final String debugInfoRenders() {
        return this.renderGlobal.getDebugInfoRenders();
    }

    public final String getEntityDebug() {
        return this.renderGlobal.getDebugInfoEntities();
    }

    public final String debugInfoEntities() {
        return "P: " + this.effectRenderer.getStatistics() + ". T: " + this.theWorld.getDebugLoadedEntities();
    }

    public final void respawn() {
        if(this.thePlayer != null && this.theWorld != null) {
            World.setEntityDead(this.thePlayer);
        }

        this.thePlayer = new EntityPlayerSP(this, this.theWorld, this.session);
        this.thePlayer.preparePlayerToSpawn();
        if(this.theWorld != null) {
            this.theWorld.playerEntity = this.thePlayer;
            this.theWorld.joinPlayerInWorld();
        }

        this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
        this.playerController.onRespawn(this.thePlayer);
        this.preloadWorld("Respawning");
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
