package net.lax1dude.eaglercraft.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLAudioElement;
import org.teavm.jso.dom.html.HTMLSourceElement;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Int8Array;
import org.teavm.jso.webaudio.AudioBuffer;
import org.teavm.jso.webaudio.AudioBufferSourceNode;
import org.teavm.jso.webaudio.AudioContext;
import org.teavm.jso.webaudio.AudioListener;
import org.teavm.jso.webaudio.DecodeErrorCallback;
import org.teavm.jso.webaudio.DecodeSuccessCallback;
import org.teavm.jso.webaudio.GainNode;
import org.teavm.jso.webaudio.MediaEvent;
import org.teavm.jso.webaudio.MediaStream;
import org.teavm.jso.webaudio.MediaStreamAudioDestinationNode;
import org.teavm.jso.webaudio.PannerNode;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.internal.teavm.JOrbisAudioBufferDecoder;
import net.lax1dude.eaglercraft.internal.teavm.TeaVMBlobURLManager;
import net.lax1dude.eaglercraft.internal.teavm.TeaVMClientConfigAdapter;
import net.lax1dude.eaglercraft.internal.teavm.TeaVMUtils;
import net.lax1dude.eaglercraft.log4j.LogManager;
import net.lax1dude.eaglercraft.log4j.Logger;
import net.lax1dude.eaglercraft.util.MathHelper;

import net.minecraft.client.sound.SoundPoolEntry;

/**
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */
public class PlatformAudio {

    static final Logger logger = LogManager.getLogger("BrowserAudio");

    static AudioContext audioctx = null;
    static HTMLAudioElement silenceElement = null;
    private static final Map<String, BrowserAudioResource> soundCache = new HashMap<>();
    private static final List<BrowserAudioHandle> activeSounds = new LinkedList<>();

    private static long cacheFreeTimer = 0l;
    private static long activeFreeTimer = 0l;
    private static boolean oggSupport = false;
    private static boolean loadViaAudioBufferSupport = false;
    private static boolean loadViaWAV32FSupport = false;
    private static boolean loadViaWAV16Support = false;

    protected static class BrowserAudioResource implements IAudioResource {

        protected AudioBuffer buffer;
        protected long cacheHit = 0l;

        protected BrowserAudioResource(AudioBuffer buffer) {
            this.buffer = buffer;
        }

    }

    protected static class BrowserAudioHandle implements IAudioHandle, EventListener<MediaEvent> {

        protected final BrowserAudioResource resource;
        protected AudioBufferSourceNode source;
        protected final PannerNode panner;
        protected final GainNode gain;
        protected float pitch;
        protected boolean repeat;
        protected boolean isPaused = false;
        protected boolean isEnded = false;
        protected boolean isDisposed = false;

        public BrowserAudioHandle(BrowserAudioResource resource, AudioBufferSourceNode source, PannerNode panner,
                GainNode gain, float pitch, boolean repeat) {
            this.resource = resource;
            this.source = source;
            this.panner = panner;
            this.gain = gain;
            this.pitch = pitch;
            this.repeat = repeat;
            TeaVMUtils.addEventListener(source, "ended", this);
        }

        @Override
        public void pause(boolean setPaused) {
            if (setPaused) {
                if (!isPaused) {
                    isPaused = true;
                    source.getPlaybackRate().setValue(0.0f);
                }
            } else {
                if (isPaused) {
                    isPaused = false;
                    source.getPlaybackRate().setValue(pitch);
                }
            }
        }

        @Override
        public void repeat(boolean en) {
            repeat = en;
            if (!isEnded) {
                source.setLoop(en);
            }
        }

        @Override
        public void restart() {
            if (isEnded) {
                isEnded = false;
                isPaused = false;
                AudioBufferSourceNode src = audioctx.createBufferSource();
                resource.cacheHit = PlatformRuntime.steadyTimeMillis();
                src.setBuffer(resource.buffer);
                src.getPlaybackRate().setValue(pitch);
                src.setLoop(repeat);
                source.disconnect();
                src.connect(panner == null ? gain : panner);
                if (isDisposed) {
                    isDisposed = false;
                    activeSounds.add(this);
                    gain.connect(audioctx.getDestination());
                }
                source = src;
                source.start();
            } else {
                isPaused = false;
                source.getPlaybackRate().setValue(pitch);
                source.start(0.0);
            }
        }

        @Override
        public void move(float x, float y, float z) {
            if (panner != null) {
                panner.setPosition(x, y, z);
            }
        }

        @Override
        public void pitch(float f) {
            pitch = f;
            if (!isPaused) {
                source.getPlaybackRate().setValue(pitch);
            }
        }

        @Override
        public void gain(float f) {
            if (panner != null) {
                float v1 = f * 16.0f;
                if (v1 < 16.0f) v1 = 16.0f;
                panner.setMaxDistance(v1);
            }
            float v2 = f;
            if (v2 > 1.0f) v2 = 1.0f;
            gain.getGain().setValue(v2);
        }

        @Override
        public void end() {
            if (!isEnded) {
                isEnded = true;
                source.stop();
            }
        }

        @Override
        public boolean shouldFree() {
            return isEnded;
        }

        @Override
        public void handleEvent(MediaEvent evt) {
            isEnded = true;
        }

        private void dispose() {
            if (!isDisposed) {
                isDisposed = true;
                gain.disconnect();
            }
        }
    }

    static void initialize() {
        oggSupport = false;
        loadViaAudioBufferSupport = false;
        loadViaWAV32FSupport = false;
        loadViaWAV16Support = false;

        try {
            audioctx = AudioContext.create();
        } catch(Throwable t) {
            audioctx = null;
            logger.error("Could not initialize audio context!");
            logger.error(t);
            return;
        }

        detectOGGSupport();

        if (!oggSupport) {
            loadViaAudioBufferSupport = detectLoadViaAudioBufferSupport(audioctx);
            if (!loadViaAudioBufferSupport) {
                logger.warn("Missing AudioContext buffer from Float32Array support, attempting to use WAV files as a container to load raw PCM data");
                detectWAVFallbackSupport();
                if (!loadViaWAV32FSupport && !loadViaWAV16Support) {
                    try {
                        audioctx.close();
                    } catch (Throwable t) {
                    }
                    audioctx = null;
                    logger.error("Audio context is missing some required features!");
                }
            }
        }

        PlatformInput.clearEventBuffers();

        if (((TeaVMClientConfigAdapter)PlatformRuntime.getClientConfigAdapter()).isKeepAliveHackTeaVM()) {
            byte[] silenceFile = PlatformAssets.getResourceBytes("/assets/eagler/silence_loop.wav");
            if (silenceFile != null) {
                HTMLAudioElement audio = (HTMLAudioElement) PlatformRuntime.doc.createElement("audio");
                audio.getClassList().add("_eaglercraftX_keepalive_hack");
                audio.setAttribute("style", "display:none;");
                audio.setAutoplay(false);
                audio.setLoop(true);
                audio.addEventListener("seeked", (e) -> {
                    // NOP, wakes up the browser's event loop
                });
                audio.addEventListener("canplay", (e) -> {
                    if (PlatformRuntime.doc != null && silenceElement != null &&
                            !PlatformInput.getVisibilityState(PlatformRuntime.doc)) {
                        silenceElement.play();
                    }
                });
                HTMLSourceElement source = (HTMLSourceElement) PlatformRuntime.doc.createElement("source");
                source.setType("audio/wav");
                source.setSrc(TeaVMBlobURLManager.registerNewURLByte(silenceFile, "audio/wav").toExternalForm());
                audio.appendChild(source);
                PlatformRuntime.parent.appendChild(audio);
                silenceElement = audio;
            }
        }
    }

    static void handleVisibilityChange() {
        if (silenceElement != null) {
            if (!PlatformInput.getVisibilityState(PlatformRuntime.doc)) {
                silenceElement.play();
            } else {
                silenceElement.pause();
            }
        }
    }

    @JSBody(params = { "ctx" }, script = "var tmpBuf = ctx.createBuffer(2, 16, 16000); return (typeof tmpBuf.copyToChannel === \"function\");")
    private static native boolean detectLoadViaAudioBufferSupport(AudioContext ctx);

    private static void detectOGGSupport() {
        if (((TeaVMClientConfigAdapter)PlatformRuntime.getClientConfigAdapter()).isUseJOrbisAudioDecoderTeaVM()
            || EagRuntime.getPlatformOS() == EnumPlatformOS.IPHONE) {
            logger.info("Note: Using embedded JOrbis OGG decoder");
            oggSupport = false;
        } else {
            byte[] fileData = EagRuntime.getRequiredResourceBytes("/assets/eagler/audioctx_test_ogg.dat");
            try {
                Int8Array arr = Int8Array.create(fileData.length);
                arr.set(TeaVMUtils.unwrapByteArray(fileData), 0);
                AudioBuffer buffer = decodeAudioBrowserSync(arr.getBuffer(), null);
                if (buffer == null || buffer.getLength() == 0) {
                    throw new RuntimeException();
                }
                oggSupport = true;
            } catch (Throwable t) {
                oggSupport = false;
                logger.error("OGG file support detected as false! Using embedded JOrbis OGG decoder");
            }
        }
    }

    private static void detectWAVFallbackSupport() {
        byte[] fileData = EagRuntime.getRequiredResourceBytes("/assets/eagler/audioctx_test_wav32f.dat");

        try {
            Int8Array arr = Int8Array.create(fileData.length);
            arr.set(TeaVMUtils.unwrapByteArray(fileData), 0);
            AudioBuffer buffer = decodeAudioBrowserSync(arr.getBuffer(), null);
            if (buffer == null || buffer.getLength() == 0) {
                throw new RuntimeException();
            }
            loadViaWAV32FSupport = true;
            return;
        } catch (Throwable t) {
            loadViaWAV32FSupport = false;
            logger.error("Could not load a 32-bit floating point WAV file, trying to use 16-bit integers");
        }

        fileData = EagRuntime.getRequiredResourceBytes("/assets/eagler/audioctx_test_wav16.dat");

        try {
            Int8Array arr = Int8Array.create(fileData.length);
            arr.set(TeaVMUtils.unwrapByteArray(fileData), 0);
            AudioBuffer buffer = decodeAudioBrowserSync(arr.getBuffer(), null);
            if (buffer == null || buffer.getLength() == 0) {
                throw new RuntimeException();
            }
            loadViaWAV16Support = true;
            return;
        } catch(Throwable t) {
            loadViaWAV16Support = false;
            logger.error("Could not load a 16-bit integer WAV file, this browser is not supported");
        }
    }

    public static IAudioResource loadAudioData(String filename, boolean holdInCache) {
        return null;
    }

    public static void loadAudioDataNew(SoundPoolEntry sound, boolean holdInCache, IAudioCacheLoader loader) {
        String filename = sound.soundUrl;
        BrowserAudioResource buffer = soundCache.get(filename);
        if (buffer == null) {
            byte[] file = loader.loadFile(filename);
            if (file == null) return;
            AudioBuffer b = decodeAudioData(sound, holdInCache, file, filename);
            if (!isAsyncSupported()) {
                buffer = new BrowserAudioResource(b);
                finishAudioData(sound, filename, holdInCache, buffer);
            }
        } else {
            finishAudioData(sound, filename, false, buffer);
        }
    }

    private static void finishAudioData(SoundPoolEntry sound, String filename, boolean holdInCache, BrowserAudioResource buffer) {
        if (buffer != null) {
            if (holdInCache) {
                soundCache.put(filename, buffer);
            }

            if (buffer.buffer != null) {
                buffer.cacheHit = PlatformRuntime.steadyTimeMillis();
            }
        }

        sound.finish(buffer);
    }

    public static boolean isAsyncSupported() {
        return oggSupport || !loadViaAudioBufferSupport;
    }

    private static AudioBuffer decodeAudioData(SoundPoolEntry sound, boolean holdInCache, byte[] data, String errorFileName) {
        if (data == null) {
            return null;
        }
        if (oggSupport) {
            // browsers complain if we don't copy the array
            Int8Array arr = Int8Array.create(data.length);
            arr.set(TeaVMUtils.unwrapByteArray(data), 0);
            decodeAudioBrowserAsync(sound, holdInCache, arr.getBuffer(), errorFileName);
            return null;
        } else {
            if (data.length > 4 && data[0] == (byte)0x4F && data[1] == (byte)0x67 && data[2] == (byte)0x67 && data[3] == (byte)0x53) {
                return JOrbisAudioBufferDecoder.decodeAudioJOrbis(sound, holdInCache, audioctx, data, errorFileName,
                        loadViaAudioBufferSupport ? JOrbisAudioBufferDecoder.LOAD_VIA_AUDIOBUFFER
                                : (loadViaWAV32FSupport ? JOrbisAudioBufferDecoder.LOAD_VIA_WAV32F
                                        : JOrbisAudioBufferDecoder.LOAD_VIA_WAV16));
            } else {
                Int8Array arr = Int8Array.create(data.length);
                arr.set(TeaVMUtils.unwrapByteArray(data), 0);
                decodeAudioBrowserAsync(sound, holdInCache, arr.getBuffer(), errorFileName);
                return null;
            }
        }
    }

    @Async
    public static native AudioBuffer decodeAudioBrowserAsync(SoundPoolEntry sound, boolean holdInCache, ArrayBuffer buffer, String errorFileName);

    private static void decodeAudioBrowserAsync(SoundPoolEntry sound, boolean holdInCache, ArrayBuffer buffer,
             final String errorFileName, final AsyncCallback<AudioBuffer> cb) {
        audioctx.decodeAudioData(buffer, new DecodeSuccessCallback() {
            @Override
            public void onSuccess(AudioBuffer decodedData) {
                finishAudioData(sound, errorFileName, holdInCache, new BrowserAudioResource(decodedData));
            }
        }, new DecodeErrorCallback() {
            @Override
            public void onError(JSObject error) {
                if (errorFileName != null) {
                    logger.error("Could not load audio: {}", errorFileName);
                }
                finishAudioData(sound, errorFileName, holdInCache, null);
            }
        });
        cb.complete(null);
    }

    @Async
    public static native AudioBuffer decodeAudioBrowserSync(ArrayBuffer buffer, String errorFileName);

    private static void decodeAudioBrowserSync(ArrayBuffer buffer, final String errorFileName, final AsyncCallback<AudioBuffer> cb) {
        audioctx.decodeAudioData(buffer, new DecodeSuccessCallback() {
            @Override
            public void onSuccess(AudioBuffer decodedData) {
                cb.complete(decodedData);
            }
        }, new DecodeErrorCallback() {
            @Override
            public void onError(JSObject error) {
                if (errorFileName != null) {
                    logger.error("Could not load audio: {}", errorFileName);
                }
                cb.complete(null);
            }
        });
    }

    public static void clearAudioCache() {
        long millis = PlatformRuntime.steadyTimeMillis();
        if(millis - cacheFreeTimer > 30000l) {
            cacheFreeTimer = millis;
            Iterator<BrowserAudioResource> itr = soundCache.values().iterator();
            while(itr.hasNext()) {
                if(millis - itr.next().cacheHit > 600000l) { // 10 minutes
                    itr.remove();
                }
            }
        }
        if(millis - activeFreeTimer > 700l) {
            activeFreeTimer = millis;
            Iterator<BrowserAudioHandle> itr = activeSounds.iterator();
            while(itr.hasNext()) {
                BrowserAudioHandle h = itr.next();
                if(h.shouldFree()) {
                    itr.remove();
                    h.dispose();
                }
            }
        }
    }

    public static void flushAudioCache() {
        soundCache.clear();
        Iterator<BrowserAudioHandle> itr = activeSounds.iterator();
        while(itr.hasNext()) {
            itr.next().dispose();
        }
        activeSounds.clear();
    }

    public static boolean available() {
        return audioctx != null;
    }

    public static IAudioHandle beginPlayback(IAudioResource track, float x, float y, float z,
            float volume, float pitch, float finalVolume, boolean repeat) {
        BrowserAudioResource internalTrack = (BrowserAudioResource) track;
        internalTrack.cacheHit = PlatformRuntime.steadyTimeMillis();

        AudioBufferSourceNode src = audioctx.createBufferSource();
        src.setBuffer(internalTrack.buffer);
        src.getPlaybackRate().setValue(pitch);
        src.setLoop(repeat);

        PannerNode panner = audioctx.createPanner();
        panner.setPosition(x, y, z);
        float v1 = 16.0f;
        if(volume > 1.0f) v1 *= volume;
        panner.setMaxDistance(v1);
        panner.setRolloffFactor(1.0f);
        panner.setDistanceModel("linear");
        panner.setPanningModel("equalpower");//panner.setPanningModel("HRTF");
        panner.setConeInnerAngle(360.0f);
        panner.setConeOuterAngle(0.0f);
        panner.setConeOuterGain(0.0f);
        panner.setOrientation(0.0f, 0.0f, 0.0f);

        GainNode gain = audioctx.createGain();
        float v2 = volume;
        if(v2 > 1.0f) v2 = 1.0f;
        if(finalVolume >= 0.0f) v2 = finalVolume;
        gain.getGain().setValue(v2);

        src.connect(panner);
        panner.connect(gain);
        gain.connect(audioctx.getDestination());

        src.start();

        BrowserAudioHandle ret = new BrowserAudioHandle(internalTrack, src, panner, gain, pitch, repeat);
        activeSounds.add(ret);
        return ret;
    }

    public static IAudioHandle beginPlaybackStatic(IAudioResource track, float volume, float pitch, boolean repeat) {
        BrowserAudioResource internalTrack = (BrowserAudioResource) track;
        internalTrack.cacheHit = PlatformRuntime.steadyTimeMillis();

        AudioBufferSourceNode src = audioctx.createBufferSource();
        src.setBuffer(internalTrack.buffer);
        src.getPlaybackRate().setValue(pitch);
        src.setLoop(repeat);

        GainNode gain = audioctx.createGain();
        float v2 = volume;
        if(v2 > 1.0f) v2 = 1.0f;
        gain.getGain().setValue(v2);

        src.connect(gain);
        gain.connect(audioctx.getDestination());

        src.start();

        BrowserAudioHandle ret = new BrowserAudioHandle(internalTrack, src, null, gain, pitch, repeat);
        activeSounds.add(ret);
        return ret;
    }

    public static void setListener(float x, float y, float z, float pitchDegrees, float yawDegrees) {
        float upX = MathHelper.sin(-yawDegrees * ((float)Math.PI / 180.0f) - (float)Math.PI);
        float upY = MathHelper.cos(-pitchDegrees * ((float)Math.PI / 180.0f));
        float upZ = MathHelper.cos(-yawDegrees * ((float)Math.PI / 180.0f) - (float)Math.PI);
        float lookX = -upX * upY;
        float lookY = MathHelper.sin(-pitchDegrees * ((float)Math.PI / 180.0f));
        float lookZ = -upZ * upY;
        upX = -upX * lookY;
        upZ = -upZ * lookY;
        AudioListener l = audioctx.getListener();
        l.setPosition(x, y, z);
        // WebAudio is very, very broken
        //l.setOrientation(lookX, lookY, lookZ, upX, upY, upZ);
        l.setOrientation(lookX, 0.0f, lookZ, 0.0f, 0.0f, 0.0f);
    }

    static void destroy() {
        flushAudioCache();
        if(audioctx != null) {
            audioctx.close();
            audioctx = null;
        }
        if(silenceElement != null) {
            silenceElement.pause();
            silenceElement.delete();
            silenceElement = null;
        }
    }
}
