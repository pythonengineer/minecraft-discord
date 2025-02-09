package net.lax1dude.eaglercraft.internal.teavm;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSArrayReader;
import org.teavm.jso.core.JSError;
import org.teavm.jso.dom.css.CSSStyleDeclaration;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.webgl.WebGLRenderingContext;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.internal.PlatformApplication;
import net.lax1dude.eaglercraft.internal.PlatformIncompatibleException;
import net.lax1dude.eaglercraft.internal.PlatformInput;
import net.lax1dude.eaglercraft.internal.PlatformOpenGL;
import net.lax1dude.eaglercraft.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.internal.teavm.opts.JSMinecraftOptsRoot;
import net.lax1dude.eaglercraft.log4j.ILogRedirector;
import net.lax1dude.eaglercraft.log4j.LogManager;
import com.mojang.minecraft.Minecraft;

/**
 * Copyright (c) 2022-2025 lax1dude. All Rights Reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */
public class ClientMain {

    @JSBody(params = {}, script = "if((typeof __isEaglerX188Running === \"string\") && __isEaglerX188Running === \"yes\") return true; __isEaglerX188Running = \"yes\"; return false;")
    private static native boolean getRunningFlag();

    private static final PrintStream systemOut = System.out;
    private static final PrintStream systemErr = System.err;

    private static JSObject windowErrorHandler = null;

    public static void _main() {
        if (getRunningFlag()) {
            systemErr.println("ClientMain: [ERROR] Minecraft is already running!");
            return;
        }
        try {
            systemOut.println("ClientMain: [INFO] Minecraft is starting...");
            JSObject opts = getMinecraftOpts();
            String username = "guest";
            String server = "";
            int serverPort = 0;
            String mpPass = "";

            if (opts == null) {
                systemErr.println("ClientMain: [ERROR] the \"window.minecraftOpts\" variable is undefined");
                systemErr.println("ClientMain: [ERROR] Minecraft cannot start");
                Window.alert("ERROR: game cannot start, the \"window.minecraftOpts\" variable is undefined");
                return;
            }

            try {
                JSMinecraftOptsRoot minecraftOpts = (JSMinecraftOptsRoot)opts;
                crashOnUncaughtExceptions = minecraftOpts.getCrashOnUncaughtExceptions(false);
                PlatformRuntime.isDeobfStackTraces = minecraftOpts.getDeobfStackTraces(true);

                configRootElementId = minecraftOpts.getContainer();
                configRootElement = Window.current().getDocument().getElementById(configRootElementId);

                HTMLElement oldContent;
                while ((oldContent = configRootElement.querySelector("._eaglercraftX_wrapper_element")) != null) {
                    oldContent.delete();
                }

                username = minecraftOpts.getUsername(username);
                server = minecraftOpts.getServer(server);
                serverPort = minecraftOpts.getServerPort(serverPort);
                mpPass = minecraftOpts.getMpPass(mpPass);

                ((TeaVMClientConfigAdapter)TeaVMClientConfigAdapter.instance).loadNative(minecraftOpts);

                systemOut.println("ClientMain: [INFO] configuration was successful");
            } catch (Throwable t) {
                systemErr.println("ClientMain: [ERROR] the \"window.minecraftOpts\" variable is invalid");
                EagRuntime.debugPrintStackTraceToSTDERR(t);
                systemErr.println("ClientMain: [ERROR] Minecraft cannot start");
                Window.alert("ERROR: game cannot start, the \"window.minecraftOpts\" variable is invalid: "
                        + t.toString());
                return;
            }

            if (crashOnUncaughtExceptions) {
                systemOut.println("ClientMain: [INFO] registering crash handlers");

                windowErrorHandler = setWindowErrorHandler(Window.current(), new WindowErrorHandler() {

                    @Override
                    public void call(String message, String file, int line, int col, JSError error) {
                        if (windowErrorHandler != null) {
                            error = TeaVMUtils.ensureDefined(error);
                            if (error == null) {
                                systemErr.println(
                                        "ClientMain: [ERROR] recieved error event, but the error is null, ignoring");
                                return;
                            }

                            StringBuilder str = new StringBuilder();

                            str.append("Native Browser Exception\n");
                            str.append("----------------------------------\n");
                            str.append("  Line: ").append((file == null ? "unknown" : file) + ":" + line + ":" + col)
                                    .append('\n');
                            str.append("  Type: ").append(error.getName()).append('\n');
                            str.append("  Desc: ").append(error.getMessage() == null ? "null" : error.getMessage())
                                    .append('\n');

                            if (message != null) {
                                if (error.getMessage() == null || !message.endsWith(error.getMessage())) {
                                    str.append("  Desc: ").append(message).append('\n');
                                }
                            }

                            str.append("----------------------------------\n\n");
                            String stack = TeaVMUtils.getStackSafe(error);
                            if (PlatformRuntime.isDeobfStackTraces && !stack.isEmpty()) {
                                TeaVMRuntimeDeobfuscator.initialize();
                                stack = TeaVMRuntimeDeobfuscator.deobfExceptionStack(stack);
                            }
                            str.append(stack == null ? "No stack trace is available" : stack).append('\n');

                            showCrashScreen(str.toString());
                        }
                    }

                });
            }

            systemOut.println("ClientMain: [INFO] initializing Minecraft runtime");

            LogManager.logRedirector = new ILogRedirector() {
                @Override
                public void log(String txt, boolean err) {
                    PlatformApplication.addLogMessage(txt, err);
                }
            };

            try {
                EagRuntime.create();
            } catch (PlatformIncompatibleException ex) {
                systemErr.println("ClientMain: [ERROR] this browser is incompatible with Minecraft!");
                systemErr.println("ClientMain: [ERROR] Reason: " + ex.getMessage());
                try {
                    showIncompatibleScreen(ex.getMessage());
                } catch (Throwable t) {
                }
                return;
            } catch (Throwable t) {
                systemErr.println("ClientMain: [ERROR] Minecraft's runtime could not be initialized!");
                EagRuntime.debugPrintStackTraceToSTDERR(t);
                showCrashScreen("Minecraft's runtime could not be initialized!", t);
                systemErr.println("ClientMain: [ERROR] Minecraft cannot start");
                return;
            }

            systemOut.println("ClientMain: [INFO] launching Minecraft main thread");

            try {
                Minecraft.main(new String[0], username, server, serverPort, mpPass);
            } catch (Throwable t) {
                systemErr.println("ClientMain: [ERROR] unhandled exception caused main thread to exit");
                EagRuntime.debugPrintStackTraceToSTDERR(t);
                showCrashScreen("Unhandled exception caused main thread to exit!", t);
            }

        } finally {
            systemErr.println("ClientMain: [ERROR] Minecraft main thread has exited");
        }
    }

    @JSBody(params = {}, script = "if(typeof minecraftOpts === \"undefined\") {return null;}"
            + "else if(typeof minecraftOpts === \"string\") {return JSON.parse(minecraftOpts);}"
            + "else {return minecraftOpts;}")
    private static native JSObject getMinecraftOpts();

    public static String configRootElementId = null;
    public static HTMLElement configRootElement = null;
    public static boolean crashOnUncaughtExceptions = false;

    @JSFunctor
    private static interface WindowErrorHandler extends JSObject {
        void call(String message, String file, int line, int col, JSError error);
    }

    @JSBody(params = {"win", "handler"}, script = "var evtHandler = function(e) { handler("
            + "(typeof e.message === \"string\") ? e.message : null,"
            + "(typeof e.filename === \"string\") ? e.filename : null,"
            + "(typeof e.lineno === \"number\") ? e.lineno : 0," + "(typeof e.colno === \"number\") ? e.colno : 0,"
            + "(typeof e.error === \"undefined\") ? null : e.error);}; win.addEventListener(\"error\", evtHandler);"
            + "return evtHandler;")
    private static native JSObject setWindowErrorHandler(Window win, WindowErrorHandler handler);

    @JSBody(params = {"win", "handler"}, script = "win.removeEventListener(\"error\", evtHandler);")
    private static native void removeWindowErrorHandler(Window win, JSObject handler);

    public static void removeErrorHandler(Window win) {
        if (windowErrorHandler != null) {
            removeWindowErrorHandler(win, windowErrorHandler);
            windowErrorHandler = null;
        }
    }

    public static void showCrashScreen(String message, Throwable t) {
        try {
            showCrashScreen(message + "\n\n" + EagRuntime.getStackTrace(t));
        } catch (Throwable tt) {
        }
    }

    private static boolean isCrashed = false;

    public static void showCrashScreen(String t) {
        StringBuilder strBeforeBuilder = new StringBuilder();
        strBeforeBuilder.append("Game Crashed! I have fallen and I can't get up!\n\n");
        strBeforeBuilder.append(t);
        strBeforeBuilder.append('\n').append('\n');
        String strBefore = strBeforeBuilder.toString();

        HTMLDocument doc = Window.current().getDocument();
        HTMLElement el;
        if (PlatformRuntime.parent != null) {
            el = PlatformRuntime.parent;
        } else {
            if (configRootElement == null) {
                configRootElement = doc.getElementById(configRootElementId);
            }
            el = configRootElement;
        }

        StringBuilder str = new StringBuilder();
        str.append("minecraft.version = \"0.0.20a_02\"\n");
        str.append('\n');
        str.append(addWebGLToCrash());
        str.append('\n');
        str.append(addShimsToCrash());
        str.append('\n');
        str.append("currentTime = ");
        str.append((new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z")).format(new Date())).append('\n');
        str.append('\n');
        addDebugNav(str, "userAgent");
        addDebugNav(str, "vendor");
        addDebugNav(str, "language");
        addDebugNav(str, "hardwareConcurrency");
        addDebugNav(str, "deviceMemory");
        addDebugNav(str, "platform");
        addDebugNav(str, "product");
        addDebugNavPlugins(str);
        str.append('\n');
        addDebug(str, "localStorage");
        addDebug(str, "sessionStorage");
        addDebug(str, "indexedDB");
        str.append('\n');
        str.append("rootElement.clientWidth = ").append(el == null ? "undefined" : el.getClientWidth()).append('\n');
        str.append("rootElement.clientHeight = ").append(el == null ? "undefined" : el.getClientHeight()).append('\n');
        addDebug(str, "innerWidth");
        addDebug(str, "innerHeight");
        addDebug(str, "outerWidth");
        addDebug(str, "outerHeight");
        addDebug(str, "devicePixelRatio");
        addDebugScreen(str, "availWidth");
        addDebugScreen(str, "availHeight");
        addDebugScreen(str, "colorDepth");
        addDebugScreen(str, "pixelDepth");
        str.append('\n');
        addDebugLocation(str, "href");
        str.append('\n');
        String strAfter = str.toString();

        String strFinal = strBefore + strAfter;
        List<String> additionalInfo = new LinkedList<>();

        if (!isCrashed) {
            isCrashed = true;

            if (additionalInfo.size() > 0) {
                try {
                    StringBuilder builderFinal = new StringBuilder();
                    builderFinal.append(strBefore);
                    builderFinal.append(
                            "Got the following messages from the crash report hook registered in minecraftOpts:\n\n");
                    for (String str2 : additionalInfo) {
                        builderFinal.append("----------[ CRASH HOOK ]----------\n");
                        builderFinal.append(str2).append('\n');
                        builderFinal.append("----------------------------------\n\n");
                    }
                    builderFinal.append(strAfter);
                    strFinal = builderFinal.toString();
                } catch (Throwable tt) {
                    systemErr.println("Uncaught exception concatenating crash report hook messages!");
                    EagRuntime.debugPrintStackTraceToSTDERR(tt);
                }
            }

            if (el == null) {
                Window.alert("Root element not found, crash report was printed to console");
                systemErr.println(strFinal);
                return;
            }

            HTMLElement div = doc.createElement("div");
            div.setAttribute("style",
                    "z-index:100;position:absolute;top:135px;left:10%;right:10%;bottom:50px;background-color:white;border:1px solid #cccccc;overflow-x:hidden;overflow-y:scroll;overflow-wrap:break-word;white-space:pre-wrap;font: 14px monospace;padding:10px;");
            div.getClassList().add("_eaglercraftX_crash_element");
            el.appendChild(div);
            div.appendChild(doc.createTextNode(strFinal));

            PlatformRuntime.removeEventHandlers();

        } else {
            systemErr.println();
            systemErr.println("An additional crash report was supressed:");
            String[] s = t.split("[\\r\\n]+");
            for (int i = 0; i < s.length; ++i) {
                systemErr.println("  " + s[i]);
            }
            if (additionalInfo.size() > 0) {
                for (String str2 : additionalInfo) {
                    if (str2 != null) {
                        systemErr.println();
                        systemErr.println("  ----------[ CRASH HOOK ]----------");
                        s = str2.split("[\\r\\n]+");
                        for (int i = 0; i < s.length; ++i) {
                            systemErr.println("  " + s[i]);
                        }
                        systemErr.println("  ----------------------------------");
                    }
                }
            }
        }
    }

    private static String webGLCrashStringCache = null;

    private static String addWebGLToCrash() {
        if (webGLCrashStringCache != null) {
            return webGLCrashStringCache;
        }

        try {
            StringBuilder ret = new StringBuilder();

            WebGLRenderingContext ctx = PlatformRuntime.webgl;
            boolean experimental = PlatformRuntime.webglExperimental;

            if (ctx == null) {
                experimental = false;
                HTMLCanvasElement cvs = (HTMLCanvasElement)Window.current().getDocument().createElement("canvas");

                cvs.setWidth(64);
                cvs.setHeight(64);

                ctx = (WebGLRenderingContext)cvs.getContext("webgl2");

                if (ctx == null) {
                    ctx = (WebGLRenderingContext)cvs.getContext("webgl");
                    if (ctx == null) {
                        experimental = true;
                        ctx = (WebGLRenderingContext)cvs.getContext("experimental-webgl");
                    }
                }
            }

            if (ctx != null) {
                if (PlatformRuntime.webgl != null) {
                    ret.append("webgl.version = ").append(ctx.getParameterString(WebGLRenderingContext.VERSION))
                            .append('\n');
                }
                if (ctx.getExtension("WEBGL_debug_renderer_info") != null) {
                    ret.append("webgl.renderer = ").append(ctx.getParameterString(/* UNMASKED_RENDERER_WEBGL */ 0x9246))
                            .append('\n');
                    ret.append("webgl.vendor = ").append(ctx.getParameterString(/* UNMASKED_VENDOR_WEBGL */ 0x9245))
                            .append('\n');
                } else {
                    ret.append("webgl.renderer = ").append(ctx.getParameterString(WebGLRenderingContext.RENDERER))
                            .append(" [masked]").append('\n');
                    ret.append("webgl.vendor = ").append(ctx.getParameterString(WebGLRenderingContext.VENDOR))
                            .append(" [masked]").append('\n');
                }
                // ret.append('\n').append("\nwebgl.anisotropicGlitch =
                // ").append(DetectAnisotropicGlitch.hasGlitch()).append('\n'); //TODO
                int id = PlatformOpenGL.checkOpenGLESVersion();
                if (id > 0) {
                    ret.append('\n').append("webgl.version.id = ").append(id).append('\n');
                    ret.append("webgl.experimental = ").append(experimental).append('\n');
                    if (id == 200) {
                        ret.append("webgl.ext.ANGLE_instanced_arrays = ")
                                .append(ctx.getExtension("ANGLE_instanced_arrays") != null).append('\n');
                        ret.append("webgl.ext.EXT_color_buffer_half_float = ")
                                .append(ctx.getExtension("EXT_color_buffer_half_float") != null).append('\n');
                        ret.append("webgl.ext.EXT_shader_texture_lod = ")
                                .append(ctx.getExtension("EXT_shader_texture_lod") != null).append('\n');
                        ret.append("webgl.ext.OES_fbo_render_mipmap = ")
                                .append(ctx.getExtension("OES_fbo_render_mipmap") != null).append('\n');
                        ret.append("webgl.ext.OES_texture_float = ")
                                .append(ctx.getExtension("OES_texture_float") != null).append('\n');
                        ret.append("webgl.ext.OES_texture_half_float = ")
                                .append(ctx.getExtension("OES_texture_half_float") != null).append('\n');
                        ret.append("webgl.ext.OES_texture_half_float_linear = ")
                                .append(ctx.getExtension("OES_texture_half_float_linear") != null).append('\n');
                    } else if (id >= 300) {
                        ret.append("webgl.ext.EXT_color_buffer_float = ")
                                .append(ctx.getExtension("EXT_color_buffer_float") != null).append('\n');
                        ret.append("webgl.ext.EXT_color_buffer_half_float = ")
                                .append(ctx.getExtension("EXT_color_buffer_half_float") != null).append('\n');
                        ret.append("webgl.ext.OES_texture_float_linear = ")
                                .append(ctx.getExtension("OES_texture_float_linear") != null).append('\n');
                    }
                    ret.append("webgl.ext.EXT_texture_filter_anisotropic = ")
                            .append(ctx.getExtension("EXT_texture_filter_anisotropic") != null).append('\n');
                } else {
                    ret.append("webgl.ext.ANGLE_instanced_arrays = ")
                            .append(ctx.getExtension("ANGLE_instanced_arrays") != null).append('\n');
                    ret.append("webgl.ext.EXT_color_buffer_float = ")
                            .append(ctx.getExtension("EXT_color_buffer_float") != null).append('\n');
                    ret.append("webgl.ext.EXT_color_buffer_half_float = ")
                            .append(ctx.getExtension("EXT_color_buffer_half_float") != null).append('\n');
                    ret.append("webgl.ext.EXT_shader_texture_lod = ")
                            .append(ctx.getExtension("EXT_shader_texture_lod") != null).append('\n');
                    ret.append("webgl.ext.OES_fbo_render_mipmap = ")
                            .append(ctx.getExtension("OES_fbo_render_mipmap") != null).append('\n');
                    ret.append("webgl.ext.OES_texture_float = ").append(ctx.getExtension("OES_texture_float") != null)
                            .append('\n');
                    ret.append("webgl.ext.OES_texture_float_linear = ")
                            .append(ctx.getExtension("OES_texture_float_linear") != null).append('\n');
                    ret.append("webgl.ext.OES_texture_half_float = ")
                            .append(ctx.getExtension("OES_texture_half_float") != null).append('\n');
                    ret.append("webgl.ext.OES_texture_half_float_linear = ")
                            .append(ctx.getExtension("OES_texture_half_float_linear") != null).append('\n');
                    ret.append("webgl.ext.EXT_texture_filter_anisotropic = ")
                            .append(ctx.getExtension("EXT_texture_filter_anisotropic") != null).append('\n');
                }
            } else {
                ret.append("Failed to query GPU info!\n");
            }

            return webGLCrashStringCache = ret.toString();
        } catch (Throwable tt) {
            return webGLCrashStringCache = "ERROR: could not query webgl info - " + tt.toString() + "\n";
        }
    }

    private static String shimsCrashStringCache = null;

    private static String addShimsToCrash() {
        if (shimsCrashStringCache != null) {
            return shimsCrashStringCache;
        }

        try {
            StringBuilder ret = new StringBuilder();

            ES6ShimStatus status = ES6ShimStatus.getRuntimeStatus();
            ret.append("eaglercraft.es6shims.status = ").append(status.getStatus()).append('\n');
            ret.append("eaglercraft.es6shims.shims = [ ");
            Set<EnumES6Shims> shims = status.getShims();
            boolean b = false;
            for (EnumES6Shims shim : shims) {
                if (b) {
                    ret.append(", ");
                }
                ret.append(shim);
                b = true;
            }
            ret.append(" ]\n");

            return shimsCrashStringCache = ret.toString();
        } catch (Throwable tt) {
            return shimsCrashStringCache = "ERROR: could not query ES6 shim info - " + tt.toString() + "\n";
        }
    }

    public static void showIncompatibleScreen(String t) {
        if (!isCrashed) {
            isCrashed = true;

            HTMLDocument doc = Window.current().getDocument();
            HTMLElement el;
            if (PlatformRuntime.parent != null) {
                el = PlatformRuntime.parent;
            } else {
                if (configRootElement == null) {
                    configRootElement = doc.getElementById(configRootElementId);
                }
                el = configRootElement;
            }

            if (el == null) {
                Window.alert("Compatibility error: " + t);
                System.err.println("Compatibility error: " + t);
                return;
            }

            String s = el.getAttribute("style");
            el.setAttribute("style", (s == null ? "" : s) + "position:relative;");
            HTMLElement div = doc.createElement("div");
            div.setAttribute("style",
                    "z-index:100;position:absolute;top:135px;left:10%;right:10%;bottom:50px;background-color:white;border:1px solid #cccccc;overflow-x:hidden;overflow-y:scroll;font:18px sans-serif;padding:40px;");
            div.getClassList().add("_eaglercraftX_incompatible_element");
            el.appendChild(div);
            div.setInnerHTML(
                    "<h2><svg style=\"vertical-align:middle;margin:0px 16px 8px 8px;\" xmlns=\"http://www.w3.org/2000/svg\" width=\"48\" height=\"48\" viewBox=\"0 0 48 48\" fill=\"none\"><path stroke=\"#000000\" stroke-width=\"3\" stroke-linecap=\"square\" d=\"M1.5 8.5v34h45v-28m-3-3h-10v-3m-3-3h-10m15 6h-18v-3m-3-3h-10\"/><path stroke=\"#000000\" stroke-width=\"2\" stroke-linecap=\"square\" d=\"M12 21h0m0 4h0m4 0h0m0-4h0m-2 2h0m20-2h0m0 4h0m4 0h0m0-4h0m-2 2h0\"/><path stroke=\"#000000\" stroke-width=\"2\" stroke-linecap=\"square\" d=\"M20 30h0 m2 2h0 m2 2h0 m2 2h0 m2 -2h0 m2 -2h0 m2 -2h0\"/></svg>+ This device is incompatible with Minecraft&ensp;:(</h2>"
                            + "<div style=\"margin-left:40px;\">"
                            + "<p style=\"font-size:1.2em;\"><b style=\"font-size:1.1em;\">Issue:</b> <span style=\"color:#BB0000;\" id=\"_eaglercraftX_crashReason\"></span><br /></p>"
                            + "<p style=\"margin-left:10px;font:0.9em monospace;\" id=\"_eaglercraftX_crashUserAgent\"></p>"
                            + "<p style=\"margin-left:10px;font:0.9em monospace;\" id=\"_eaglercraftX_crashWebGL\"></p>"
                            + "<p style=\"margin-left:10px;font:0.9em monospace;\">Current Date: "
                            + (new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z")).format(new Date()) + "</p>"
                            + "<p><br /><span style=\"font-size:1.1em;border-bottom:1px dashed #AAAAAA;padding-bottom:5px;\">Things you can try:</span></p>"
                            + "<ol>"
                            + "<li><span style=\"font-weight:bold;\">Just try using Minecraft on a different device</span>, it isn't a bug it's common sense</li>"
                            + "<li style=\"margin-top:7px;\">If this screen just appeared randomly, try restarting your browser or device</li>"
                            + "<li style=\"margin-top:7px;\">If you are not using Chrome/Edge, try installing the latest Google Chrome</li>"
                            + "<li style=\"margin-top:7px;\">If your browser is out of date, please update it to the latest version</li>"
                            + "</ol>" + "</div>");

            div.querySelector("#_eaglercraftX_crashReason").appendChild(doc.createTextNode(t));
            div.querySelector("#_eaglercraftX_crashUserAgent")
                    .appendChild(doc.createTextNode(getStringNav("userAgent")));

            PlatformRuntime.removeEventHandlers();

            String webGLRenderer = "No GL_RENDERER string could be queried";

            try {
                HTMLCanvasElement cvs = (HTMLCanvasElement)Window.current().getDocument().createElement("canvas");

                cvs.setWidth(64);
                cvs.setHeight(64);

                WebGLRenderingContext ctx = (WebGLRenderingContext)cvs.getContext("webgl");

                if (ctx != null) {
                    String r;
                    if (ctx.getExtension("WEBGL_debug_renderer_info") != null) {
                        r = ctx.getParameterString(/* UNMASKED_RENDERER_WEBGL */ 0x9246);
                    } else {
                        r = ctx.getParameterString(WebGLRenderingContext.RENDERER);
                        if (r != null) {
                            r += " [masked]";
                        }
                    }
                    if (r != null) {
                        webGLRenderer = r;
                    }
                }
            } catch (Throwable tt) {
            }

            div.querySelector("#_eaglercraftX_crashWebGL").appendChild(doc.createTextNode(webGLRenderer));

        }
    }

    @JSBody(params = {"el", "str"}, script = "el.innerText = str;")
    private static native void setInnerText(HTMLElement el, String str);

    @JSBody(params = {"v"}, script = "try { return \"\"+window[v]; } catch(e) { return \"<error>\"; }")
    private static native String getString(String var);

    @JSBody(params = {"v"}, script = "try { return \"\"+window.navigator[v]; } catch(e) { return \"<error>\"; }")
    private static native String getStringNav(String var);

    @JSBody(params = {"v"}, script = "try { return \"\"+window.screen[v]; } catch(e) { return \"<error>\"; }")
    private static native String getStringScreen(String var);

    @JSBody(params = {"v"}, script = "try { return \"\"+window.location[v]; } catch(e) { return \"<error>\"; }")
    private static native String getStringLocation(String var);

    @JSBody(params = {}, script = "try { var retObj = new Array; if(typeof navigator.plugins === \"object\")"
            + "{ var len = navigator.plugins.length; if(len > 0) { for(var idx = 0; idx < len; ++idx) {"
            + "var thePlugin = navigator.plugins[idx]; retObj.push({ name: thePlugin.name,"
            + "filename: thePlugin.filename, desc: thePlugin.description }); } } } return JSON.stringify(retObj);"
            + "} catch(e) { return \"<error>\"; }")
    private static native String getStringNavPlugins();

    private static void addDebug(StringBuilder str, String var) {
        str.append("window.").append(var).append(" = ").append(getString(var)).append('\n');
    }

    private static void addDebugNav(StringBuilder str, String var) {
        str.append("window.navigator.").append(var).append(" = ").append(getStringNav(var)).append('\n');
    }

    private static void addDebugNavPlugins(StringBuilder str) {
        str.append("window.navigator.plugins = ").append(getStringNavPlugins()).append('\n');
    }

    private static void addDebugScreen(StringBuilder str, String var) {
        str.append("window.screen.").append(var).append(" = ").append(getStringScreen(var)).append('\n');
    }

    private static void addDebugLocation(StringBuilder str, String var) {
        str.append("window.location.").append(var).append(" = ").append(getStringLocation(var)).append('\n');
    }

    private static void addArray(StringBuilder str, String var) {
        str.append("window.").append(var).append(" = ").append(getArray(var)).append('\n');
    }

    @JSBody(params = {
            "v"}, script = "try { return (typeof window[v] !== \"undefined\") ? JSON.stringify(window[v]) : \"[\\\"<error>\\\"]\"; } catch(e) { return \"[\\\"<error>\\\"]\"; }")
    private static native String getArray(String var);
}
