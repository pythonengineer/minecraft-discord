package net.minecraft.game.world;

public final class NextTickListEntry implements Comparable {
    private static long nextTickEntryID = 0L;
    public int xCoord;
    public int yCoord;
    public int zCoord;
    public int blockID;
    public long scheduledTime;
    private long tickEntryID = nextTickEntryID++;

    public NextTickListEntry(int x, int y, int z, int blockID) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        this.blockID = blockID;
    }

    public final boolean equals(Object nextTickListEntry) {
        if(nextTickListEntry instanceof NextTickListEntry) {
            NextTickListEntry nextTickListEntry1 = (NextTickListEntry)nextTickListEntry;
            return this.xCoord == nextTickListEntry1.xCoord && this.yCoord == nextTickListEntry1.yCoord && this.zCoord == nextTickListEntry1.zCoord && this.blockID == nextTickListEntry1.blockID;
        } else {
            return false;
        }
    }

    public final int hashCode() {
        return ((this.xCoord << 7 << 10) + (this.zCoord << 7) + this.yCoord << 8) + this.blockID;
    }

    public final NextTickListEntry setScheduledTime(long scheduledTime) {
        this.scheduledTime = scheduledTime;
        return this;
    }

    public final int compareTo(Object nextTickListEntry) {
        NextTickListEntry nextTickListEntry2 = (NextTickListEntry)nextTickListEntry;
        return this.scheduledTime < nextTickListEntry2.scheduledTime ? -1 : (this.scheduledTime > nextTickListEntry2.scheduledTime ? 1 : (this.tickEntryID < nextTickListEntry2.tickEntryID ? -1 : (this.tickEntryID > nextTickListEntry2.tickEntryID ? 1 : 0)));
    }
}