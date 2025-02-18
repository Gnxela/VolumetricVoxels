package me.alexng.volumetricVoxels.shape;


import me.alexng.volumetricVoxels.raster.Rasterable;
import org.joml.Matrix4f;

public abstract class Shape {

	public int color;

	public Shape(int color) {
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public abstract Rasterable toRasterInput(Matrix4f model);
}
