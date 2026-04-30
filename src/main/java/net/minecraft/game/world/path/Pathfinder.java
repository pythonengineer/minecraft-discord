package net.minecraft.game.world.path;

import java.util.HashMap;
import java.util.Map;

import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.world.IBlockAccess;
import net.minecraft.game.world.material.Material;

public final class Pathfinder {
	private IBlockAccess worldMap;
	private Path path = new Path();
	private Map pointMap = new HashMap();
	private PathPoint[] pathOptions = new PathPoint[32];

	public Pathfinder(IBlockAccess iBlockAccess) {
		this.worldMap = iBlockAccess;
	}

	public final PathEntity createEntityPathTo(Entity entity1, Entity entity2, float distance) {
		return this.createEntityPathTo(entity1, entity2.posX, entity2.boundingBox.minY, entity2.posZ, distance);
	}

	public final PathEntity createEntityPathTo(Entity entity, int x, int y, int z, float distance) {
		return this.createEntityPathTo(entity, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), distance);
	}

	private PathEntity createEntityPathTo(Entity entity, double x, double y, double z, float distance) {
		this.path.clearPath();
		this.pointMap.clear();
		PathPoint object9 = this.openPoint(MathHelper.floor_double(entity.boundingBox.minX), MathHelper.floor_double(entity.boundingBox.minY), MathHelper.floor_double(entity.boundingBox.minZ));
		PathPoint pathPoint21 = this.openPoint(MathHelper.floor_double(x - (double)(entity.width / 2.0F)), MathHelper.floor_double(y), MathHelper.floor_double(z - (double)(entity.width / 2.0F)));
		PathPoint pathPoint3 = new PathPoint(MathHelper.floor_float(entity.width + 1.0F), MathHelper.floor_float(entity.height + 1.0F), MathHelper.floor_float(entity.width + 1.0F));
		float f24 = distance;
		PathPoint pathPoint5 = pathPoint3;
		PathPoint pathPoint23 = pathPoint21;
		Entity entity22 = entity;
		Pathfinder pathfinder20 = this;
		((PathPoint)object9).totalPathDistance = 0.0F;
		((PathPoint)object9).distanceToNext = ((PathPoint)object9).distanceTo(pathPoint21);
		((PathPoint)object9).distanceToTarget = ((PathPoint)object9).distanceToNext;
		this.path.clearPath();
		this.path.addPoint((PathPoint)object9);
		Object object7 = object9;

		PathEntity pathEntity10000;
		while(true) {
			if(pathfinder20.path.isPathEmpty()) {
				pathEntity10000 = object7 == object9 ? null : createEntityPath((PathPoint)object7);
				break;
			}

			PathPoint pathPoint25;
			if((pathPoint25 = pathfinder20.path.dequeue()).hash == pathPoint23.hash) {
				pathEntity10000 = createEntityPath(pathPoint23);
				break;
			}

			if(pathPoint25.distanceTo(pathPoint23) < ((PathPoint)object7).distanceTo(pathPoint23)) {
				object7 = pathPoint25;
			}

			pathPoint25.isFirst = true;
			int object15 = 0;
			byte b16 = 0;
			if(pathfinder20.findPathOptions(pathPoint25.xCoord, pathPoint25.yCoord + 1, pathPoint25.zCoord, pathPoint5) > 0) {
				b16 = 1;
			}

			PathPoint pathPoint17 = pathfinder20.getSafePoint(entity22, pathPoint25.xCoord, pathPoint25.yCoord, pathPoint25.zCoord + 1, pathPoint5, b16);
			PathPoint pathPoint18 = pathfinder20.getSafePoint(entity22, pathPoint25.xCoord - 1, pathPoint25.yCoord, pathPoint25.zCoord, pathPoint5, b16);
			PathPoint pathPoint19 = pathfinder20.getSafePoint(entity22, pathPoint25.xCoord + 1, pathPoint25.yCoord, pathPoint25.zCoord, pathPoint5, b16);
			PathPoint pathPoint10 = pathfinder20.getSafePoint(entity22, pathPoint25.xCoord, pathPoint25.yCoord, pathPoint25.zCoord - 1, pathPoint5, b16);
			if(pathPoint17 != null && !pathPoint17.isFirst && pathPoint17.distanceTo(pathPoint23) < f24) {
				++object15;
				pathfinder20.pathOptions[0] = pathPoint17;
			}

			if(pathPoint18 != null && !pathPoint18.isFirst && pathPoint18.distanceTo(pathPoint23) < f24) {
				pathfinder20.pathOptions[object15++] = pathPoint18;
			}

			if(pathPoint19 != null && !pathPoint19.isFirst && pathPoint19.distanceTo(pathPoint23) < f24) {
				pathfinder20.pathOptions[object15++] = pathPoint19;
			}

			if(pathPoint10 != null && !pathPoint10.isFirst && pathPoint10.distanceTo(pathPoint23) < f24) {
				pathfinder20.pathOptions[object15++] = pathPoint10;
			}

			for(int i26 = 0; i26 < object15; ++i26) {
				PathPoint pathPoint11 = pathfinder20.pathOptions[i26];
				float f12 = pathPoint25.totalPathDistance + pathPoint25.distanceTo(pathPoint11);
				if(!pathPoint11.isAssigned() || f12 < pathPoint11.totalPathDistance) {
					pathPoint11.previous = pathPoint25;
					pathPoint11.totalPathDistance = f12;
					pathPoint11.distanceToNext = pathPoint11.distanceTo(pathPoint23);
					if(pathPoint11.isAssigned()) {
						pathfinder20.path.changeDistance(pathPoint11, pathPoint11.totalPathDistance + pathPoint11.distanceToNext);
					} else {
						pathPoint11.distanceToTarget = pathPoint11.totalPathDistance + pathPoint11.distanceToNext;
						pathfinder20.path.addPoint(pathPoint11);
					}
				}
			}
		}

		return pathEntity10000;
	}

	private PathPoint getSafePoint(Entity entity, int x, int y, int z, PathPoint pathPoint, int offsetY) {
		PathPoint pathPoint8 = null;
		if(this.findPathOptions(x, y, z, pathPoint) > 0) {
			pathPoint8 = this.openPoint(x, y, z);
		}

		if(pathPoint8 == null && this.findPathOptions(x, y + offsetY, z, pathPoint) > 0) {
			pathPoint8 = this.openPoint(x, y + offsetY, z);
		}

		if(pathPoint8 != null) {
			offsetY = 0;

			while(true) {
				int i7;
				if(y <= 0 || (i7 = this.findPathOptions(x, y - 1, z, pathPoint)) <= 0) {
                    if(y > 0) {
                        pathPoint8 = this.openPoint(x, y, z);
                    }

					Material material9;
					if((material9 = this.worldMap.getBlockMaterial(x, y - 1, z)) == Material.water || material9 == Material.lava) {
						return null;
					}
					break;
				}

				if(i7 < 0) {
					return null;
				}

				++offsetY;
				if(offsetY >= 4) {
					return null;
				}

				--y;
			}
		}

		return pathPoint8;
	}

	private final PathPoint openPoint(int x, int y, int z) {
		int i4 = x | y << 10 | z << 20;
		PathPoint pathPoint5;
		if((pathPoint5 = (PathPoint)this.pointMap.get(i4)) == null) {
			pathPoint5 = new PathPoint(x, y, z);
			this.pointMap.put(i4, pathPoint5);
		}

		return pathPoint5;
	}

	private int findPathOptions(int x, int y, int z, PathPoint pathPoint) {
		for(int i5 = x; i5 < x + pathPoint.xCoord; ++i5) {
			for(int i6 = y; i6 < y + pathPoint.yCoord; ++i6) {
				for(int i7 = z; i7 < z + pathPoint.zCoord; ++i7) {
					Material material8;
					if((material8 = this.worldMap.getBlockMaterial(x, y, z)).getIsSolid()) {
						return 0;
					}

					if(material8 == Material.water || material8 == Material.lava) {
						return -1;
					}
				}
			}
		}

		return 1;
	}

	private static PathEntity createEntityPath(PathPoint pathPoint) {
		int i1 = 1;

		PathPoint pathPoint2;
		for(pathPoint2 = pathPoint; pathPoint2.previous != null; pathPoint2 = pathPoint2.previous) {
			++i1;
		}

		PathPoint[] pathPoint3 = new PathPoint[i1];
		pathPoint2 = pathPoint;
		--i1;

		for(pathPoint3[i1] = pathPoint; pathPoint2.previous != null; pathPoint3[i1] = pathPoint2) {
			pathPoint2 = pathPoint2.previous;
			--i1;
		}

		return new PathEntity(pathPoint3);
	}
}
