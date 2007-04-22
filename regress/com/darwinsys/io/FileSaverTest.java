package com.darwinsys.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import junit.framework.TestCase;

public class FileSaverTest extends TestCase {

	/** Test File name. */
	public static final String FILENAME = "fileiotest.dat";

	FileSaver saver;

	/** Test string */
	public static final String MESSAGE =
		"The quick brown fox jumps over the lazy dog.";

	/** Set up initial state data file state for testing */
	@Override
	public void setUp() {
		try {
			// Create file in "." with known name and contents
			FileIO.stringToFile(MESSAGE, FILENAME);
			final File file = new File(FILENAME);
			file.deleteOnExit();
			// Create FileSaver to save it.
			saver = new FileSaver(file);
		} catch (IOException ex) {
			throw new IllegalStateException("FileIOTest: can't create " + FILENAME);
		}
	}

	/** Test that the overwritten file contains something reasonable,
	 * and that nothing gets thrown in normal processing.
	 * @throws IOException
	 */
	public void testOne() throws IOException {
		final Writer writer = saver.getWriter();
		PrintWriter out = new PrintWriter(writer);
		out.print(MESSAGE);
		out.close();
		saver.finish();
		final String finalString = FileIO.readerToString(new FileReader(FILENAME));
		assertEquals("Reading string back", MESSAGE, finalString);
	}

	/**
	 * Test state forwarding
	 * @throws IOException
	 */
	public void testFailures() throws IOException {
		saver.getWriter();
		try {
			saver.getOutputStream();
			fail("Allowed getWriter AND getOutputStream");
		} catch (IllegalStateException e) {
			// Nothing to do
		}
		saver.finish();
		saver.getOutputStream();
	}
}