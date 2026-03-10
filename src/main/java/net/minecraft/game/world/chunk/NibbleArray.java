package net.minecraft.game.world.chunk;

final class NibbleArray {
    public final byte[] data;

    public NibbleArray(int size) {
        this.data = new byte[size >> 1];
    }

    public NibbleArray(byte[] data) {
        this.data = data;
    }

    public final int getNibble(int x, int y, int z) {
        y = (x = x << 11 | z << 7 | y) >> 1;
        return (x &= 1) == 0 ? this.data[y] & 15 : this.data[y] >> 4 & 15;
    }

    public final void setNibble(int x, int y, int z, int value) {
        y = (x = x << 11 | z << 7 | y) >> 1;
        if((x &= 1) == 0) {
            this.data[y] = (byte)(this.data[y] & 240 | value & 15);
        } else {
            this.data[y] = (byte)(this.data[y] & 15 | (value & 15) << 4);
        }
    }

    public final boolean isValid() {
        return this.data != null;
    }
}