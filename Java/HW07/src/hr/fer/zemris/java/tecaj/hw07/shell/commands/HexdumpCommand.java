package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.StringJoiner;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Hexdump command produces hexadecimal representation of a file.
 * <p>
 * Expected input is: {@code hexdump [path]}
 * 
 * @author Dan
 *
 */
public class HexdumpCommand extends AbstractCommand {

	/**
	 * Command description.
	 */
	private static final String DESCRIPTION = "Hexdump command produces hexadecimal representation of a file.\nExpected input is: \"hexdump [path]\"";

	/**
	 * Number of bytes to print per line.
	 */
	private static final int LINE_LENGTH = 16;

	/**
	 * Creates a new {@code HexdumpCommand}.
	 */
	public HexdumpCommand() {
		super("hexdump", DESCRIPTION);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		List<String> args = splitArgument(argument);

		try {
			if (args.size() != 1) {
				env.writeln("File name expected.");
				return ShellStatus.CONTINUE;
			}

			File file = new File(args.get(0));

			try (InputStream stream = new BufferedInputStream(new FileInputStream(file))) {
				byte[] bytes = new byte[LINE_LENGTH];
				int length, i = 0;

				while ((length = stream.read(bytes)) > 0) {
					env.writeln(String.format("%08X: %s", i * LINE_LENGTH, bytesToString(bytes, length)));
					i++;
				}

			} catch (FileNotFoundException e) {
				env.writeln("File doesn't exist.");
				return ShellStatus.CONTINUE;
			}

		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage());
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Converts given {@code bytes} to a {@code String} of a fixed length:
	 * {@value #LINE_LENGTH}.
	 * 
	 * @param bytes
	 *            {@code bytes} to convert
	 * @param length
	 *            number of {@code bytes} in given {@code Array}; rest of the is
	 *            filled with blanks
	 * @return {@code String} with given {@code bytes} and character
	 *         representation of them
	 */
	private String bytesToString(byte[] bytes, int length) {
		StringBuilder chars = new StringBuilder();
		StringJoiner firstHalf = new StringJoiner(" ");

		for (int i = 0; i < Integer.min(length, LINE_LENGTH / 2); i++) {
			firstHalf.add(String.format("%02X", Byte.toUnsignedInt(bytes[i])));
			chars.append(byteToChar(bytes[i]));
		}
		for (int i = length; i < LINE_LENGTH / 2; i++) {
			firstHalf.add("  ");
		}

		StringJoiner secondHalf = new StringJoiner(" ");

		for (int i = LINE_LENGTH / 2; i < length; i++) {
			secondHalf.add(String.format("%02X", Byte.toUnsignedInt(bytes[i])));
			chars.append(byteToChar(bytes[i]));
		}
		for (int i = Integer.max(length, LINE_LENGTH / 2); i < LINE_LENGTH; i++) {
			secondHalf.add("  ");
		}

		StringJoiner output = new StringJoiner(" | ");
		output.add(firstHalf.toString());
		output.add(secondHalf.toString());
		output.add(chars.toString());

		return output.toString();
	}

	/**
	 * Converts {@code byte} to a {@code char}. If {@code byte} is not in the
	 * standard subset (<tt>b &lt; 32 || b &gt; 127</tt>), dot '.' is returned.
	 * 
	 * @param b
	 *            {@code byte}
	 * @return {@code char} with given value
	 */
	private char byteToChar(byte b) {
		if (b < 32 || b > 127) {
			return '.';
		} else {
			return (char) b;
		}
	}

}
