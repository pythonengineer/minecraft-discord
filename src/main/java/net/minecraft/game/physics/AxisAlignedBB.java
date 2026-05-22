package net.minecraft.game.physics;

import java.util.ArrayList;
import java.util.List;

public final class AxisAlignedBB {
    private static List boundingBoxes = new ArrayList();
    private static int numBoundingBoxesInUse = 0;
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;

    public static AxisAlignedBB getBoundingBox(double aabbMinX, double aabbMinY, double aabbMinZ, double aabbMaxX, double aabbMaxY, double aabbMaxZ) {
        return new AxisAlignedBB(aabbMinX, aabbMinY, aabbMinZ, aabbMaxX, aabbMaxY, aabbMaxZ);
    }

    public static void clearBoundingBoxPool() {
        numBoundingBoxesInUse = 0;
    }

    public static AxisAlignedBB getBoundingBoxFromPool(double aabbMinX, double aabbMinY, double aabbMinZ, double aabbMaxX, double aabbMaxY, double aabbMaxZ) {
        if(numBoundingBoxesInUse >= boundingBoxes.size()) {
            boundingBoxes.add(getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D));
        }

        return ((AxisAlignedBB)boundingBoxes.get(numBoundingBoxesInUse++)).setBounds(aabbMinX, aabbMinY, aabbMinZ, aabbMaxX, aabbMaxY, aabbMaxZ);
    }

    private AxisAlignedBB(double aabbMinX, double aabbMinY, double aabbMinZ, double aabbMaxX, double aabbMaxY, double aabbMaxZ) {
        this.minX = aabbMinX;
        this.minY = aabbMinY;
        this.minZ = aabbMinZ;
        this.maxX = aabbMaxX;
        this.maxY = aabbMaxY;
        this.maxZ = aabbMaxZ;
    }

    public final AxisAlignedBB setBounds(double aabbMinX, double aabbMinY, double aabbMinZ, double aabbMaxX, double aabbMaxY, double aabbMaxZ) {
        this.minX = aabbMinX;
        this.minY = aabbMinY;
        this.minZ = aabbMinZ;
        this.maxX = aabbMaxX;
        this.maxY = aabbMaxY;
        this.maxZ = aabbMaxZ;
        return this;
    }

    public final AxisAlignedBB addCoord(double x, double y, double z) {
        double d7 = this.minX;
        double d9 = this.minY;
        double d11 = this.minZ;
        double d13 = this.maxX;
        double d15 = this.maxY;
        double d17 = this.maxZ;
        if(x < 0.0D) {
            d7 += x;
        }

        if(x > 0.0D) {
            d13 += x;
        }

        if(y < 0.0D) {
            d9 += y;
        }

        if(y > 0.0D) {
            d15 += y;
        }

        if(z < 0.0D) {
            d11 += z;
        }

        if(z > 0.0D) {
            d17 += z;
        }

        return getBoundingBoxFromPool(d7, d9, d11, d13, d15, d17);
    }

    public final AxisAlignedBB expand(double x, double y, double z) {
        double d7 = this.minX - x;
        double d9 = this.minY - y;
        double d11 = this.minZ - z;
        double d13 = this.maxX + x;
        double d15 = this.maxY + y;
        double d17 = this.maxZ + z;
        return getBoundingBoxFromPool(d7, d9, d11, d13, d15, d17);
    }

    public final AxisAlignedBB getOffsetBoundingBox(double x, double y, double z) {
        return getBoundingBoxFromPool(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

    public final double calculateXOffset(AxisAlignedBB aabb, double xOffset) {
        if(aabb.maxY > this.minY && aabb.minY < this.maxY) {
            if(aabb.maxZ > this.minZ && aabb.minZ < this.maxZ) {
                double d4;
                if(xOffset > 0.0D && aabb.maxX <= this.minX && (d4 = this.minX - aabb.maxX) < xOffset) {
                    xOffset = d4;
                }

                if(xOffset < 0.0D && aabb.minX >= this.maxX && (d4 = this.maxX - aabb.minX) > xOffset) {
                    xOffset = d4;
                }

                return xOffset;
            } else {
                return xOffset;
            }
        } else {
            return xOffset;
        }
    }

    public final double calculateYOffset(AxisAlignedBB aabb, double yOffset) {
        if(aabb.maxX > this.minX && aabb.minX < this.maxX) {
            if(aabb.maxZ > this.minZ && aabb.minZ < this.maxZ) {
                double d4;
                if(yOffset > 0.0D && aabb.maxY <= this.minY && (d4 = this.minY - aabb.maxY) < yOffset) {
                    yOffset = d4;
                }

                if(yOffset < 0.0D && aabb.minY >= this.maxY && (d4 = this.maxY - aabb.minY) > yOffset) {
                    yOffset = d4;
                }

                return yOffset;
            } else {
                return yOffset;
            }
        } else {
            return yOffset;
        }
    }

    public final double calculateZOffset(AxisAlignedBB aabb, double zOffset) {
        if(aabb.maxX > this.minX && aabb.minX < this.maxX) {
            if(aabb.maxY > this.minY && aabb.minY < this.maxY) {
                double d4;
                if(zOffset > 0.0D && aabb.maxZ <= this.minZ && (d4 = this.minZ - aabb.maxZ) < zOffset) {
                    zOffset = d4;
                }

                if(zOffset < 0.0D && aabb.minZ >= this.maxZ && (d4 = this.maxZ - aabb.minZ) > zOffset) {
                    zOffset = d4;
                }

                return zOffset;
            } else {
                return zOffset;
            }
        } else {
            return zOffset;
        }
    }

    public final boolean intersectsWith(AxisAlignedBB aabb) {
        return aabb.maxX > this.minX && aabb.minX < this.maxX ? (aabb.maxY > this.minY && aabb.minY < this.maxY ? aabb.maxZ > this.minZ && aabb.minZ < this.maxZ : false) : false;
    }

    public final AxisAlignedBB offset(double x, double y, double z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return this;
    }

    public final double getAverageEdgeLength() {
        double d1 = this.maxX - this.minX;
        double d3 = this.maxY - this.minY;
        double d5 = this.maxZ - this.minZ;
        return (d1 + d3 + d5) / 3.0D;
    }

    public final AxisAlignedBB copy() {
        return getBoundingBoxFromPool(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public final MovingObjectPosition calculateIntercept(Vec3D vector1, Vec3D vector2) {
        Vec3D vec3D3 = vector1.getIntermediateWithXValue(vector2, this.minX);
        Vec3D vec3D4 = vector1.getIntermediateWithXValue(vector2, this.maxX);
        Vec3D vec3D5 = vector1.getIntermediateWithYValue(vector2, this.minY);
        Vec3D vec3D6 = vector1.getIntermediateWithYValue(vector2, this.maxY);
        Vec3D vec3D7 = vector1.getIntermediateWithZValue(vector2, this.minZ);
        vector2 = vector1.getIntermediateWithZValue(vector2, this.maxZ);
        if(!this.isVecInYZ(vec3D3)) {
            vec3D3 = null;
        }

        if(!this.isVecInYZ(vec3D4)) {
            vec3D4 = null;
        }

        if(!this.isVecInXZ(vec3D5)) {
            vec3D5 = null;
        }

        if(!this.isVecInXZ(vec3D6)) {
            vec3D6 = null;
        }

        if(!this.isVecInXY(vec3D7)) {
            vec3D7 = null;
        }

        if(!this.isVecInXY(vector2)) {
            vector2 = null;
        }

        Vec3D vec3D8 = null;
        if(vec3D3 != null) {
            vec3D8 = vec3D3;
        }

        if(vec3D4 != null && (vec3D8 == null || vector1.squaredDistanceTo(vec3D4) < vector1.squaredDistanceTo(vec3D8))) {
            vec3D8 = vec3D4;
        }

        if(vec3D5 != null && (vec3D8 == null || vector1.squaredDistanceTo(vec3D5) < vector1.squaredDistanceTo(vec3D8))) {
            vec3D8 = vec3D5;
        }

        if(vec3D6 != null && (vec3D8 == null || vector1.squaredDistanceTo(vec3D6) < vector1.squaredDistanceTo(vec3D8))) {
            vec3D8 = vec3D6;
        }

        if(vec3D7 != null && (vec3D8 == null || vector1.squaredDistanceTo(vec3D7) < vector1.squaredDistanceTo(vec3D8))) {
            vec3D8 = vec3D7;
        }

        if(vector2 != null && (vec3D8 == null || vector1.squaredDistanceTo(vector2) < vector1.squaredDistanceTo(vec3D8))) {
            vec3D8 = vector2;
        }

        if(vec3D8 == null) {
            return null;
        } else {
            byte vector11 = -1;
            if(vec3D8 == vec3D3) {
                vector11 = 4;
            }

            if(vec3D8 == vec3D4) {
                vector11 = 5;
            }

            if(vec3D8 == vec3D5) {
                vector11 = 0;
            }

            if(vec3D8 == vec3D6) {
                vector11 = 1;
            }

            if(vec3D8 == vec3D7) {
                vector11 = 2;
            }

            if(vec3D8 == vector2) {
                vector11 = 3;
            }

            return new MovingObjectPosition(0, 0, 0, vector11, vec3D8);
        }
    }

    private boolean isVecInYZ(Vec3D vector) {
        return vector == null ? false : vector.yCoord >= this.minY && vector.yCoord <= this.maxY && vector.zCoord >= this.minZ && vector.zCoord <= this.maxZ;
    }

    private boolean isVecInXZ(Vec3D vector) {
        return vector == null ? false : vector.xCoord >= this.minX && vector.xCoord <= this.maxX && vector.zCoord >= this.minZ && vector.zCoord <= this.maxZ;
    }

    private boolean isVecInXY(Vec3D vector) {
        return vector == null ? false : vector.xCoord >= this.minX && vector.xCoord <= this.maxX && vector.yCoord >= this.minY && vector.yCoord <= this.maxY;
    }

    public final void setBB(AxisAlignedBB aabb) {
        this.minX = aabb.minX;
        this.minY = aabb.minY;
        this.minZ = aabb.minZ;
        this.maxX = aabb.maxX;
        this.maxY = aabb.maxY;
        this.maxZ = aabb.maxZ;
    }
}