package net.lax1dude.eaglercraft.internal.lwjgl;

import net.lax1dude.eaglercraft.internal.IClientConfigAdapter;

/**
 * Copyright (c) 2022 lax1dude. All Rights Reserved.
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
public class DesktopClientConfigAdapter implements IClientConfigAdapter {

    public static final IClientConfigAdapter instance = new DesktopClientConfigAdapter();

    @Override
    public String getUsername() {
        return "guest";
    }

    @Override
    public String getServer() {
        return "localhost";
    }

    @Override
    public int getServerPort() {
        return 3000;
    }

    @Override
    public String getResourcePacksDB() {
        return "resources";
    }

    @Override
    public boolean isCheckGLErrors() {
        return false;
    }

    @Override
    public String getLocalStorageNamespace() {
        return "_minecraft";
    }

    @Override
    public boolean isOpenDebugConsoleOnLaunch() {
        return false;
    }

    @Override
    public boolean isRamdiskMode() {
        return false;
    }

    @Override
    public boolean isEnforceVSync() {
        return false;
    }
}
