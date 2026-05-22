package net.minecraft.game.world.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.game.world.ChunkPosition;
import net.minecraft.game.world.World;

final class MinecartTrackLogic {
    private World worldObj;
    private int trackX;
    private int trackY;
    private int trackZ;
    private int trackMetadata;
    private List connectedTracks;
    private BlockMinecartTrack minecartTrack;

    public MinecartTrackLogic(BlockMinecartTrack blockMinecartTrack1, World world2, int i3, int i4, int i5) {
        this.minecartTrack = blockMinecartTrack1;
        this.connectedTracks = new ArrayList();
        this.worldObj = world2;
        this.trackX = i3;
        this.trackY = i4;
        this.trackZ = i5;
        this.trackMetadata = world2.getBlockMetadata(i3, i4, i5);
        this.calculateConnectedTracks();
    }

    private void calculateConnectedTracks() {
        this.connectedTracks.clear();
        if(this.trackMetadata == 0) {
            this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ - 1));
            this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ + 1));
        } else if(this.trackMetadata == 1) {
            this.connectedTracks.add(new ChunkPosition(this.trackX - 1, this.trackY, this.trackZ));
            this.connectedTracks.add(new ChunkPosition(this.trackX + 1, this.trackY, this.trackZ));
        } else if(this.trackMetadata == 2) {
            this.connectedTracks.add(new ChunkPosition(this.trackX - 1, this.trackY, this.trackZ));
            this.connectedTracks.add(new ChunkPosition(this.trackX + 1, this.trackY + 1, this.trackZ));
        } else if(this.trackMetadata == 3) {
            this.connectedTracks.add(new ChunkPosition(this.trackX - 1, this.trackY + 1, this.trackZ));
            this.connectedTracks.add(new ChunkPosition(this.trackX + 1, this.trackY, this.trackZ));
        } else if(this.trackMetadata == 4) {
            this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY + 1, this.trackZ - 1));
            this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ + 1));
        } else if(this.trackMetadata == 5) {
            this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ - 1));
            this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY + 1, this.trackZ + 1));
        } else if(this.trackMetadata == 6) {
            this.connectedTracks.add(new ChunkPosition(this.trackX + 1, this.trackY, this.trackZ));
            this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ + 1));
        } else if(this.trackMetadata == 7) {
            this.connectedTracks.add(new ChunkPosition(this.trackX - 1, this.trackY, this.trackZ));
            this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ + 1));
        } else if(this.trackMetadata == 8) {
            this.connectedTracks.add(new ChunkPosition(this.trackX - 1, this.trackY, this.trackZ));
            this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ - 1));
        } else {
            if(this.trackMetadata == 9) {
                this.connectedTracks.add(new ChunkPosition(this.trackX + 1, this.trackY, this.trackZ));
                this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ - 1));
            }

        }
    }

    private void refreshConnectedTracks() {
        for(int i1 = 0; i1 < this.connectedTracks.size(); ++i1) {
            MinecartTrackLogic minecartTrackLogic2;
            if((minecartTrackLogic2 = this.getMinecartTrackLogic((ChunkPosition)this.connectedTracks.get(i1))) != null && minecartTrackLogic2.isConnectedTo(this)) {
                this.connectedTracks.set(i1, new ChunkPosition(minecartTrackLogic2.trackX, minecartTrackLogic2.trackY, minecartTrackLogic2.trackZ));
            } else {
                this.connectedTracks.remove(i1--);
            }
        }

    }

    private MinecartTrackLogic getMinecartTrackLogic(ChunkPosition chunkPosition1) {
        return this.worldObj.getBlockId(chunkPosition1.x, chunkPosition1.y, chunkPosition1.z) == this.minecartTrack.blockID ? new MinecartTrackLogic(this.minecartTrack, this.worldObj, chunkPosition1.x, chunkPosition1.y, chunkPosition1.z) : (this.worldObj.getBlockId(chunkPosition1.x, chunkPosition1.y + 1, chunkPosition1.z) == this.minecartTrack.blockID ? new MinecartTrackLogic(this.minecartTrack, this.worldObj, chunkPosition1.x, chunkPosition1.y + 1, chunkPosition1.z) : (this.worldObj.getBlockId(chunkPosition1.x, chunkPosition1.y - 1, chunkPosition1.z) == this.minecartTrack.blockID ? new MinecartTrackLogic(this.minecartTrack, this.worldObj, chunkPosition1.x, chunkPosition1.y - 1, chunkPosition1.z) : null));
    }

    private boolean isConnectedTo(MinecartTrackLogic minecartTrackLogic1) {
        for(int i2 = 0; i2 < this.connectedTracks.size(); ++i2) {
            ChunkPosition chunkPosition3;
            if((chunkPosition3 = (ChunkPosition)this.connectedTracks.get(i2)).x == minecartTrackLogic1.trackX && chunkPosition3.z == minecartTrackLogic1.trackZ) {
                return true;
            }
        }

        return false;
    }

    private boolean isInTrack(int i1, int i2) {
        for(int i3 = 0; i3 < this.connectedTracks.size(); ++i3) {
            ChunkPosition chunkPosition4;
            if((chunkPosition4 = (ChunkPosition)this.connectedTracks.get(i3)).x == i1 && chunkPosition4.z == i2) {
                return true;
            }
        }

        return false;
    }

    private boolean handleKeyPress(MinecartTrackLogic minecartTrackLogic1) {
        if(this.isConnectedTo(minecartTrackLogic1)) {
            return true;
        } else if(this.connectedTracks.size() == 2) {
            return false;
        } else if(this.connectedTracks.size() == 0) {
            return true;
        } else {
            ChunkPosition chunkPosition2 = (ChunkPosition)this.connectedTracks.get(0);
            return minecartTrackLogic1.trackY == this.trackY && chunkPosition2.y == this.trackY ? true : true;
        }
    }

    private boolean canConnectFrom(int i1, int i2, int i3) {
        MinecartTrackLogic minecartTrackLogic4;
        if((minecartTrackLogic4 = this.getMinecartTrackLogic(new ChunkPosition(i1, i2, i3))) == null) {
            return false;
        } else {
            minecartTrackLogic4.refreshConnectedTracks();
            return minecartTrackLogic4.handleKeyPress(this);
        }
    }

    public final void place() {
        boolean z1 = this.canConnectFrom(this.trackX, this.trackY, this.trackZ - 1);
        boolean z2 = this.canConnectFrom(this.trackX, this.trackY, this.trackZ + 1);
        boolean z3 = this.canConnectFrom(this.trackX - 1, this.trackY, this.trackZ);
        boolean z4 = this.canConnectFrom(this.trackX + 1, this.trackY, this.trackZ);
        byte b5 = -1;
        if(z1 || z2) {
            b5 = 0;
        }

        if(z3 || z4) {
            b5 = 1;
        }

        if(z2 && z4 && !z1 && !z3) {
            b5 = 6;
        }

        if(z2 && z3 && !z1 && !z4) {
            b5 = 7;
        }

        if(z1 && z3 && !z2 && !z4) {
            b5 = 8;
        }

        if(z1 && z4 && !z2 && !z3) {
            b5 = 9;
        }

        if(b5 == 0) {
            if(this.worldObj.getBlockId(this.trackX, this.trackY + 1, this.trackZ - 1) == this.minecartTrack.blockID) {
                b5 = 4;
            }

            if(this.worldObj.getBlockId(this.trackX, this.trackY + 1, this.trackZ + 1) == this.minecartTrack.blockID) {
                b5 = 5;
            }
        }

        if(b5 == 1) {
            if(this.worldObj.getBlockId(this.trackX + 1, this.trackY + 1, this.trackZ) == this.minecartTrack.blockID) {
                b5 = 2;
            }

            if(this.worldObj.getBlockId(this.trackX - 1, this.trackY + 1, this.trackZ) == this.minecartTrack.blockID) {
                b5 = 3;
            }
        }

        if(b5 < 0) {
            b5 = 0;
        }

        this.trackMetadata = b5;
        this.calculateConnectedTracks();
        this.worldObj.setBlockMetadata(this.trackX, this.trackY, this.trackZ, b5);

        for(int i8 = 0; i8 < this.connectedTracks.size(); ++i8) {
            MinecartTrackLogic minecartTrackLogic9;
            if((minecartTrackLogic9 = this.getMinecartTrackLogic((ChunkPosition)this.connectedTracks.get(i8))) != null) {
                minecartTrackLogic9.refreshConnectedTracks();
                if(minecartTrackLogic9.handleKeyPress(this)) {
                    (minecartTrackLogic9 = minecartTrackLogic9).connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ));
                    z3 = minecartTrackLogic9.isInTrack(minecartTrackLogic9.trackX, minecartTrackLogic9.trackZ - 1);
                    z4 = minecartTrackLogic9.isInTrack(minecartTrackLogic9.trackX, minecartTrackLogic9.trackZ + 1);
                    boolean z10 = minecartTrackLogic9.isInTrack(minecartTrackLogic9.trackX - 1, minecartTrackLogic9.trackZ);
                    boolean z6 = minecartTrackLogic9.isInTrack(minecartTrackLogic9.trackX + 1, minecartTrackLogic9.trackZ);
                    byte b7 = -1;
                    if(z3 || z4) {
                        b7 = 0;
                    }

                    if(z10 || z6) {
                        b7 = 1;
                    }

                    if(z4 && z6 && !z3 && !z10) {
                        b7 = 6;
                    }

                    if(z4 && z10 && !z3 && !z6) {
                        b7 = 7;
                    }

                    if(z3 && z10 && !z4 && !z6) {
                        b7 = 8;
                    }

                    if(z3 && z6 && !z4 && !z10) {
                        b7 = 9;
                    }

                    if(b7 == 0) {
                        if(minecartTrackLogic9.worldObj.getBlockId(minecartTrackLogic9.trackX, minecartTrackLogic9.trackY + 1, minecartTrackLogic9.trackZ - 1) == minecartTrackLogic9.minecartTrack.blockID) {
                            b7 = 4;
                        }

                        if(minecartTrackLogic9.worldObj.getBlockId(minecartTrackLogic9.trackX, minecartTrackLogic9.trackY + 1, minecartTrackLogic9.trackZ + 1) == minecartTrackLogic9.minecartTrack.blockID) {
                            b7 = 5;
                        }
                    }

                    if(b7 == 1) {
                        if(minecartTrackLogic9.worldObj.getBlockId(minecartTrackLogic9.trackX + 1, minecartTrackLogic9.trackY + 1, minecartTrackLogic9.trackZ) == minecartTrackLogic9.minecartTrack.blockID) {
                            b7 = 2;
                        }

                        if(minecartTrackLogic9.worldObj.getBlockId(minecartTrackLogic9.trackX - 1, minecartTrackLogic9.trackY + 1, minecartTrackLogic9.trackZ) == minecartTrackLogic9.minecartTrack.blockID) {
                            b7 = 3;
                        }
                    }

                    if(b7 < 0) {
                        b7 = 0;
                    }

                    minecartTrackLogic9.worldObj.setBlockMetadata(minecartTrackLogic9.trackX, minecartTrackLogic9.trackY, minecartTrackLogic9.trackZ, b7);
                }
            }
        }

    }
}