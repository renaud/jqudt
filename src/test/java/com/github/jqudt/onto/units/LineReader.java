package com.github.jqudt.onto.units;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Represents the lines found in an {@link InputStream}. The lines are read one
 * at a time using {@link BufferedReader#readLine()} and may be streamed through
 * an iterator or returned all at once.
 * 
 * <p>
 * This class does not handle any concurrency issues.
 * 
 * <p>
 * The stream is closed automatically when the for loop is done :)
 * 
 * <pre>
 * {@code
 * for(String line : new LineReader(stream))
 *      // ...
 * }
 * </pre>
 * 
 * <p>
 * An {@link IllegalStateException} will be thrown if any {@link IOException}s
 * occur when reading or closing the stream.
 * 
 * @author Torleif Berger
 * @license http://creativecommons.org/licenses/by/3.0/
 * @see http://www.geekality.net/?p=1614
 */
public class LineReader implements Iterable<String>, Closeable {
	private BufferedReader reader;

	public LineReader(InputStream stream) {
		reader = new BufferedReader(new InputStreamReader(stream));
	}

	/**
	 * Closes the underlying stream.
	 */
	@Override
	public void close() throws IOException {
		reader.close();
	}

	/**
	 * Makes sure the underlying stream is closed.
	 */
	@Override
	protected void finalize() throws Throwable {
		close();
	}

	/**
	 * Returns an iterator over the lines remaining to be read.
	 * 
	 * <p>
	 * The underlying stream is closed automatically once
	 * {@link Iterator#hasNext()} returns false. This means that the stream
	 * should be closed after using a for loop.
	 * 
	 * @return This iterator.
	 */
	@Override
	public Iterator<String> iterator() {
		return new LineIterator();
	}

	/**
	 * Returns all lines remaining to be read and closes the stream.
	 * 
	 * @param skipComments
	 *            whether to skip lines starting with #
	 * @return The lines read from the stream.
	 */
	public List<String> readLines(boolean skipComments) {
		List<String> lines = new ArrayList<String>();
		for (String line : this) {
			if (!(skipComments && line.startsWith("#")))
				lines.add(line);
		}
		return lines;
	}

	/**
	 * One integer per line. Lines starting with # are skipped.
	 * 
	 * @return an array of ints from this file
	 */
	public static int[] readInts(InputStream is) {
		ArrayList<Integer> ints = new ArrayList<Integer>();
		for (String line : new LineReader(is)) {
			if (!line.startsWith("#"))
				ints.add(Integer.parseInt(line));
		}
		// copy it, because we can't cast from Integer to int...
		int[] ret = new int[ints.size()];
		for (int i = 0; i < ints.size(); i++) {
			ret[i] = ints.get(i);
		}
		return ret;
	}

	private class LineIterator implements Iterator<String> {
		private String nextLine;

		public String bufferNext() {
			try {
				return nextLine = reader.readLine();
			} catch (IOException e) {
				throw new IllegalStateException(
						"I/O error while reading stream.", e);
			}
		}

		public boolean hasNext() {
			boolean hasNext = nextLine != null || bufferNext() != null;

			if (!hasNext)
				try {
					if (reader.ready())
						reader.close();
				} catch (IOException e) {
					throw new IllegalStateException(
							"I/O error when closing stream.", e);
				}

			return hasNext;
		}

		public String next() {
			if (!hasNext())
				throw new NoSuchElementException();

			String result = nextLine;
			nextLine = null;
			return result;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}