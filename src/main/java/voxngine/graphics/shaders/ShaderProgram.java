/*
 * The MIT License (MIT)
 *
 * Copyright � 2014, Heiko Brumme
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package voxngine.graphics.shaders;


import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

/**
 * This class represents a shader program.
 *
 * @author Heiko Brumme
 */
public class ShaderProgram {

    /**
     * Stores the handle of the program.
     */
    private final int id;

    /**
     * Creates a shader program.
     */
    public ShaderProgram() { 
        id = glCreateProgram();
    }

    /**
     * Attach a shader to this program.
     *
     * @param shader Shader to get attached
     */
    public void attachShader(Shader shader) {
        glAttachShader(id, shader.getID());
    }

    /**
     * Binds the fragment out color variable.
     *
     * @param number Color number you want to bind
     * @param name Variable name
     */
    public void bindFragmentDataLocation(int number, CharSequence name) {
        glBindFragDataLocation(id, number, name);
    }

    /**
     * Link this program.
     */
    public void link() {
        glLinkProgram(id);
        checkStatus();
    }

    /**
     * Gets the location of an attribute variable with specified name.
     *
     * @param name Attribute name
     * @return Location of the attribute
     */
    public int getAttributeLocation(CharSequence name) {
        return glGetAttribLocation(id, name);
    }

    /**
     * Enables a vertex attribute.
     *
     * @param location Location of the vertex attribute
     */
    public void enableVertexAttribute(int location) {
        glEnableVertexAttribArray(location);
    }

    /**
     * Disables a vertex attribute.
     *
     * @param location Location of the vertex attribute
     */
    public void disableVertexAttribute(int location) {
        glDisableVertexAttribArray(location);
    }

    /**
     * Sets the vertex attribute pointer.
     *
     * @param location Location of the vertex attribute
     * @param size Number of values per vertex
     * @param stride Offset between consecutive generic vertex attributes in
     * bytes
     * @param offset Offset of the first component of the first generic vertex
     * attribute in bytes
     */
    public void pointVertexAttribute(int location, int size, int stride, int offset) {
    	glVertexAttribPointer(location, size, GL_FLOAT, false, stride, offset);
    }

    /**
     * Gets the location of an uniform variable with specified name.
     *
     * @param name Uniform name
     * @return Location of the uniform
     */
    public int getUniformLocation(CharSequence name) {
        return glGetUniformLocation(id, name);
    }

    /**
     * Sets the uniform variable for specified location.
     *
     * @param location Uniform location
     * @param value Value to set
     */
    public void setUniform(int location, int value) {
        glUniform1i(location, value);
    }

    /**
     * Sets the uniform variable for specified location.
     *
     * @param location Uniform location
     * @param value Value to set
     */
    public void setUniform(int location, Vector2f value) {
    	FloatBuffer fb = BufferUtils.createFloatBuffer(2);
        glUniform2fv(location, value.get(fb));
    }

    /**
     * Sets the uniform variable for specified location.
     *
     * @param location Uniform location
     * @param value Value to set
     */
    public void setUniform(int location, Vector3f value) {
    	 FloatBuffer fb = BufferUtils.createFloatBuffer(3);
         glUniform3fv(location, value.get(fb));
    }

    /**
     * Sets the uniform variable for specified location.
     *
     * @param location Uniform location
     * @param value Value to set
     */
    public void setUniform(int location, Vector4f value) {
       FloatBuffer fb = BufferUtils.createFloatBuffer(4);	
       glUniform4fv(location, value.get(fb));
    }

//    /**
//     * Sets the uniform variable for specified location.
//     *
//     * @param location Uniform location
//     * @param value Value to set
//     */
//    public void setUniform(int location, Matrix2f value) {
//        glUniformMatrix2fv(location, false, value.getBuffer());
//    }

    /**
     * Sets the uniform variable for specified location.
     *
     * @param location Uniform location
     * @param value Value to set
     */
    public void setUniform(int location, Matrix3f value) {
    	FloatBuffer fb = BufferUtils.createFloatBuffer(9);
        glUniformMatrix3fv(location, false, value.get(fb));
    }

    /**
     * Sets the uniform variable for specified location.
     *
     * @param location Uniform location
     * @param value Value to set
     */
    public void setUniform(int location, Matrix4f value) {
    	FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        glUniformMatrix4fv(location, false, value.get(fb));
    }

    /**
     * Use this shader program.
     */
    public void bind() {
        glUseProgram(id);
    }
    
    /**
     * Use this shader program.
     */
    public void unbind() {
        glUseProgram(0);
    }

    /**
     * Checks if the program was linked successfully.
     */
    public void checkStatus() {
        int status = glGetProgrami(id, GL_LINK_STATUS);
        if (status != GL_TRUE) {
            throw new RuntimeException(glGetProgramInfoLog(id));
        }
    }

    /**
     * Deletes the shader program.
     */
    public void delete() {
        glDeleteProgram(id);
    }
    
    /**
     * Load shader from file.
     *
     * @param type Type of the shader
     * @param path File path of the shader
     * @return Shader from specified file
     */
    public Shader loadShader(int type, String path) {
        StringBuilder builder = new StringBuilder();

        try (InputStream in = new FileInputStream(path);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load a shader file!"
                    + System.lineSeparator() + ex.getMessage());
        }

        CharSequence source = builder.toString();
        return new Shader(type, source);
    }

}