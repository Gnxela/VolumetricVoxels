package me.alexng.untitled.render;

import me.alexng.untitled.render.exceptions.ShaderException;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram implements Cleanable {

	private final int handle;

	public ShaderProgram() {
		this.handle = glCreateProgram();
	}

	public void attachShader(Shader shader) throws IOException, ShaderException {
		if (!shader.isLoaded()) {
			shader.load();
		}
		glAttachShader(handle, shader.getHandle());
		shader.cleanup();
	}

	public void linkProgram() throws ShaderException {
		glLinkProgram(handle);
		if (glGetProgrami(handle, GL_LINK_STATUS) == GL_FALSE) {
			String log = glGetProgramInfoLog(handle);
			throw new ShaderException("Unable to link shaders: " + log);
		}
	}

	public void use() {
		glUseProgram(handle);
	}

	public void setBool(String name, boolean value) {
		glUniform1i(glGetUniformLocation(handle, name), value ? 1 : 0);
	}

	public void setInt(String name, int value) {
		glUniform1i(glGetUniformLocation(handle, name), value);
	}

	public void setMatrix4f(String name, Matrix4f transform) {
		try (MemoryStack memoryStack = MemoryStack.stackPush()) {
			setMatrix4f(name, transform.get(memoryStack.mallocFloat(16)));
		}
	}

	public void setMatrix4f(String name, FloatBuffer buffer) {
		glUniformMatrix4fv(glGetUniformLocation(handle, name), false, buffer);
	}

	public int getHandle() {
		return handle;
	}

	@Override
	public void cleanup() {
		glDeleteProgram(handle);
	}
}
