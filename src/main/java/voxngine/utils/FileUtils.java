/*
 * Copyright LWJGL. All rights reserved.
 * License terms: http://lwjgl.org/license.php
 */
package voxngine.utils;

import static org.lwjgl.BufferUtils.createByteBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import org.lwjgl.BufferUtils;

public final class FileUtils {

	public FileUtils() {
	}

	private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
		ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
		buffer.flip();
		newBuffer.put(buffer);
		return newBuffer;
	}

	/**
	 * Reads the specified resource and returns the raw data as a ByteBuffer.
	 *
	 * @param resource   the resource to read
	 * @param bufferSize the initial buffer size
	 *
	 * @return the resource data
	 *
	 * @throws IOException if an IO error occurs
	 */
	@SuppressWarnings("resource")
	public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
		ByteBuffer buffer;

		File file = new File(resource);
		if ( file.isFile() ) {
			FileChannel fc = new FileInputStream(file).getChannel();
			buffer = BufferUtils.createByteBuffer((int)fc.size() + 1);

			while ( fc.read(buffer) != -1 ) ;

			fc.close();
		} else {
			buffer = createByteBuffer(bufferSize);

			InputStream source = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
			if ( source == null )
				throw new FileNotFoundException(resource);

			try {
				ReadableByteChannel rbc = Channels.newChannel(source);
				try {
					while ( true ) {
						int bytes = rbc.read(buffer);
						if ( bytes == -1 )
							break;
						if ( buffer.remaining() == 0 )
							buffer = resizeBuffer(buffer, buffer.capacity() * 2);
					}
				} finally {
					rbc.close();
				}
			} finally {
				source.close();
			}
		}

		buffer.flip();
		return buffer;
	}

}