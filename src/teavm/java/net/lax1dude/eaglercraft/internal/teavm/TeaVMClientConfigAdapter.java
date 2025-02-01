package net.lax1dude.eaglercraft.internal.teavm;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.EagRuntime;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArrayReader;

import net.lax1dude.eaglercraft.internal.IClientConfigAdapter;
import net.lax1dude.eaglercraft.internal.teavm.opts.JSMinecraftOptsRoot;

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
public class TeaVMClientConfigAdapter implements IClientConfigAdapter {

    public static final IClientConfigAdapter instance = new TeaVMClientConfigAdapter();

    private String resourcePacksDB = "resources";
    private boolean checkGLErrors = false;
    private String localStorageNamespace = "_minecraft";
    private boolean crashOnUncaughtExceptions = false;
    private boolean openDebugConsoleOnLaunch = false;
    private boolean fixDebugConsoleUnloadListener = false;
    private boolean forceWebGL1 = false;
    private boolean forceWebGL2 = false;
    private boolean allowExperimentalWebGL1 = true;
    private boolean useWebGLExt = true;
    private boolean useDelayOnSwap = false;
    private boolean useXHRFetch = false;
    private boolean useVisualViewport = true;
    private boolean deobfStackTraces = true;
    private boolean disableBlobURLs = false;
    private boolean ramdiskMode = false;
    private boolean singleThreadMode = false;

    public void loadNative(JSObject jsObject) {
        JSMinecraftOptsRoot minecraftOpts = (JSMinecraftOptsRoot)jsObject;

        resourcePacksDB = minecraftOpts.getResourcePacksDB(resourcePacksDB);
        checkGLErrors = minecraftOpts.getCheckGLErrors(false);
        localStorageNamespace = minecraftOpts.getLocalStorageNamespace(localStorageNamespace);
        crashOnUncaughtExceptions = minecraftOpts.getCrashOnUncaughtExceptions(false);
        openDebugConsoleOnLaunch = minecraftOpts.getOpenDebugConsoleOnLaunch(false);
        fixDebugConsoleUnloadListener = minecraftOpts.getFixDebugConsoleUnloadListener(false);
        forceWebGL1 = minecraftOpts.getForceWebGL1(false);
        forceWebGL2 = minecraftOpts.getForceWebGL2(false);
        allowExperimentalWebGL1 = minecraftOpts.getAllowExperimentalWebGL1(true);
        useWebGLExt = minecraftOpts.getUseWebGLExt(true);
        useDelayOnSwap = minecraftOpts.getUseDelayOnSwap(false);
        useXHRFetch = minecraftOpts.getUseXHRFetch(false);
        useVisualViewport = minecraftOpts.getUseVisualViewport(true);
        deobfStackTraces = minecraftOpts.getDeobfStackTraces(true);
        disableBlobURLs = minecraftOpts.getDisableBlobURLs(false);
        ramdiskMode = minecraftOpts.getRamdiskMode(false);
        singleThreadMode = minecraftOpts.getSingleThreadMode(false);
    }

    @Override
    public String getResourcePacksDB() {
        return resourcePacksDB;
    }

    @Override
    public boolean isCheckGLErrors() {
        return checkGLErrors;
    }

    @Override
    public String getLocalStorageNamespace() {
        return localStorageNamespace;
    }

    @Override
    public boolean isOpenDebugConsoleOnLaunch() {
        return openDebugConsoleOnLaunch;
    }

    public boolean isFixDebugConsoleUnloadListenerTeaVM() {
        return fixDebugConsoleUnloadListener;
    }

    public boolean isForceWebGL1TeaVM() {
        return forceWebGL1;
    }

    public boolean isForceWebGL2TeaVM() {
        return forceWebGL2;
    }

    public boolean isAllowExperimentalWebGL1TeaVM() {
        return allowExperimentalWebGL1;
    }

    public boolean isUseWebGLExtTeaVM() {
        return useWebGLExt;
    }

    public boolean isUseDelayOnSwapTeaVM() {
        return useDelayOnSwap;
    }

    public boolean isUseXHRFetchTeaVM() {
        return useXHRFetch;
    }

    public boolean isDeobfStackTracesTeaVM() {
        return deobfStackTraces;
    }

    public boolean isUseVisualViewportTeaVM() {
        return useVisualViewport;
    }

    public boolean isDisableBlobURLsTeaVM() {
        return disableBlobURLs;
    }

    public boolean isSingleThreadModeTeaVM() {
        return singleThreadMode;
    }

    @Override
    public boolean isRamdiskMode() {
        return ramdiskMode;
    }

    @Override
    public boolean isEnforceVSync() {
        return false;
    }
}
