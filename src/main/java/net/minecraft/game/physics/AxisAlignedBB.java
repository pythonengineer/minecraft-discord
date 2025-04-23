package net.minecraft.game.physics;

public final class AxisAlignedBB {
	public float x0;
	public float y0;
	public float z0;
	public float x1;
	public float y1;
	public float z1;

	public AxisAlignedBB(float var1, float var2, float var3, float var4, float var5, float var6) {
		this.x0 = var1;
		this.y0 = var2;
		this.z0 = var3;
		this.x1 = var4;
		this.y1 = var5;
		this.z1 = var6;
	}

	public final AxisAlignedBB addCoord(float var1, float var2, float var3) {
		float var4 = this.x0;
		float var5 = this.y0;
		float var6 = this.z0;
		float var7 = this.x1;
		float var8 = this.y1;
		float var9 = this.z1;
		if(var1 < 0.0F) {
			var4 += var1;
		}

		if(var1 > 0.0F) {
			var7 += var1;
		}

		if(var2 < 0.0F) {
			var5 += var2;
		}

		if(var2 > 0.0F) {
			var8 += var2;
		}

		if(var3 < 0.0F) {
			var6 += var3;
		}

		if(var3 > 0.0F) {
			var9 += var3;
		}

		return new AxisAlignedBB(var4, var5, var6, var7, var8, var9);
	}

	public final AxisAlignedBB expand(float var1, float var2, float var3) {
		float var4 = this.x0 - var1;
		float var5 = this.y0 - var2;
		float var6 = this.z0 - var3;
		var1 += this.x1;
		var2 += this.y1;
		float var7 = this.z1 + var3;
		return new AxisAlignedBB(var4, var5, var6, var1, var2, var7);
	}

	public final float clipXCollide(AxisAlignedBB var1, float var2) {
		if(var1.y1 > this.y0 && var1.y0 < this.y1) {
			if(var1.z1 > this.z0 && var1.z0 < this.z1) {
				float var3;
				if(var2 > 0.0F && var1.x1 <= this.x0) {
					var3 = this.x0 - var1.x1;
					if(var3 < var2) {
						var2 = var3;
					}
				}

				if(var2 < 0.0F && var1.x0 >= this.x1) {
					var3 = this.x1 - var1.x0;
					if(var3 > var2) {
						var2 = var3;
					}
				}

				return var2;
			} else {
				return var2;
			}
		} else {
			return var2;
		}
	}

	public final float clipYCollide(AxisAlignedBB var1, float var2) {
		if(var1.x1 > this.x0 && var1.x0 < this.x1) {
			if(var1.z1 > this.z0 && var1.z0 < this.z1) {
				float var3;
				if(var2 > 0.0F && var1.y1 <= this.y0) {
					var3 = this.y0 - var1.y1;
					if(var3 < var2) {
						var2 = var3;
					}
				}

				if(var2 < 0.0F && var1.y0 >= this.y1) {
					var3 = this.y1 - var1.y0;
					if(var3 > var2) {
						var2 = var3;
					}
				}

				return var2;
			} else {
				return var2;
			}
		} else {
			return var2;
		}
	}

	public final float clipZCollide(AxisAlignedBB var1, float var2) {
		if(var1.x1 > this.x0 && var1.x0 < this.x1) {
			if(var1.y1 > this.y0 && var1.y0 < this.y1) {
				float var3;
				if(var2 > 0.0F && var1.z1 <= this.z0) {
					var3 = this.z0 - var1.z1;
					if(var3 < var2) {
						var2 = var3;
					}
				}

				if(var2 < 0.0F && var1.z0 >= this.z1) {
					var3 = this.z1 - var1.z0;
					if(var3 > var2) {
						var2 = var3;
					}
				}

				return var2;
			} else {
				return var2;
			}
		} else {
			return var2;
		}
	}

	public final boolean intersectsWith(AxisAlignedBB var1) {
		return var1.x1 >= this.x0 && var1.x0 <= this.x1 ? (var1.y1 >= this.y0 && var1.y0 <= this.y1 ? var1.z1 >= this.z0 && var1.z0 <= this.z1 : false) : false;
	}

	public final void offset(float var1, float var2, float var3) {
		this.x0 += var1;
		this.y0 += var2;
		this.z0 += var3;
		this.x1 += var1;
		this.y1 += var2;
		this.z1 += var3;
	}

	public final AxisAlignedBB copy() {
		return new AxisAlignedBB(this.x0, this.y0, this.z0, this.x1, this.y1, this.z1);
	}

	public final boolean isVecInYZ(Vec3D var1) {
		return var1 == null ? false : var1.yCoord >= this.y0 && var1.yCoord <= this.y1 && var1.zCoord >= this.z0 && var1.zCoord <= this.z1;
	}

	public final boolean isVecInXZ(Vec3D var1) {
		return var1 == null ? false : var1.xCoord >= this.x0 && var1.xCoord <= this.x1 && var1.zCoord >= this.z0 && var1.zCoord <= this.z1;
	}

	public final boolean isVecInXY(Vec3D var1) {
		return var1 == null ? false : var1.xCoord >= this.x0 && var1.xCoord <= this.x1 && var1.yCoord >= this.y0 && var1.yCoord <= this.y1;
	}
}
