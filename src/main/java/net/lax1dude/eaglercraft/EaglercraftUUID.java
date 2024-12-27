package net.lax1dude.eaglercraft;

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
public class EaglercraftUUID implements Comparable<EaglercraftUUID> {
    public final long msb;
    public final long lsb;
    private int hash = 0;
    private boolean hasHash;

    public EaglercraftUUID(long msb, long lsb) {
        this.msb = msb;
        this.lsb = lsb;
    }

    public EaglercraftUUID(byte[] uuid) {
        long msb = 0;
        long lsb = 0;
        for (int i = 0; i < 8; i++) msb = (msb << 8) | (uuid[i] & 0xff);
        for (int i = 8; i < 16; i++) lsb = (lsb << 8) | (uuid[i] & 0xff);
        this.msb = msb;
        this.lsb = lsb;
    }

    public EaglercraftUUID(String uuid) {
        String[] components = uuid.split("-");
        if (components.length != 5) throw new IllegalArgumentException("Invalid UUID string: " + uuid);
        for (int i = 0; i < 5; i++) components[i] = "0x" + components[i];
        long mostSigBits = Long.decode(components[0]).longValue();
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(components[1]).longValue();
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(components[2]).longValue();
        long leastSigBits = Long.decode(components[3]).longValue();
        leastSigBits <<= 48;
        leastSigBits |= Long.decode(components[4]).longValue();
        this.msb = mostSigBits;
        this.lsb = leastSigBits;
    }

    private static byte long7(long x) {
        return (byte)(x >> 56);
    }

    private static byte long6(long x) {
        return (byte)(x >> 48);
    }

    private static byte long5(long x) {
        return (byte)(x >> 40);
    }

    private static byte long4(long x) {
        return (byte)(x >> 32);
    }

    private static byte long3(long x) {
        return (byte)(x >> 24);
    }

    private static byte long2(long x) {
        return (byte)(x >> 16);
    }

    private static byte long1(long x) {
        return (byte)(x >> 8);
    }

    private static byte long0(long x) {
        return (byte)(x);
    }

    public byte[] getBytes() {
        byte[] ret = new byte[16];
        ret[0] = long7(msb);
        ret[1] = long6(msb);
        ret[2] = long5(msb);
        ret[3] = long4(msb);
        ret[4] = long3(msb);
        ret[5] = long2(msb);
        ret[6] = long1(msb);
        ret[7] = long0(msb);
        ret[8] = long7(lsb);
        ret[9] = long6(lsb);
        ret[10] = long5(lsb);
        ret[11] = long4(lsb);
        ret[12] = long3(lsb);
        ret[13] = long2(lsb);
        ret[14] = long1(lsb);
        ret[15] = long0(lsb);
        return ret;
    }

    @Override
    public String toString() {
        return (digits(msb >> 32, 8) + "-" + digits(msb >> 16, 4) + "-" + digits(msb, 4) + "-" + digits(lsb >> 48, 4)
                + "-" + digits(lsb, 12));
    }

    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }

    @Override
    public int hashCode() {
        if (hash == 0 && !hasHash) {
            long hilo = msb ^ lsb;
            hash = ((int)(hilo >> 32)) ^ (int)hilo;
            hasHash = true;
        }
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EaglercraftUUID)) return false;
        EaglercraftUUID oo = (EaglercraftUUID)o;
        return (hasHash && oo.hasHash) ? (hash == oo.hash && msb == oo.msb && lsb == oo.lsb)
                : (msb == oo.msb && lsb == oo.lsb);
    }

    public long getMostSignificantBits() {
        return msb;
    }

    public long getLeastSignificantBits() {
        return lsb;
    }

    public static EaglercraftUUID randomUUID() {
        byte[] randomBytes = new byte[16];
        (new EaglercraftRandom()).nextBytes(randomBytes);
        randomBytes[6] &= 0x0f; /* clear version */
        randomBytes[6] |= 0x40; /* set to version 4 */
        randomBytes[8] &= 0x3f; /* clear variant */
        randomBytes[8] |= 0x80; /* set to IETF variant */
        return new EaglercraftUUID(randomBytes);
    }

    @Override
    public int compareTo(EaglercraftUUID val) {
        return (this.msb < val.msb ? -1
                : (this.msb > val.msb ? 1 : (this.lsb < val.lsb ? -1 : (this.lsb > val.lsb ? 1 : 0))));
    }
}
