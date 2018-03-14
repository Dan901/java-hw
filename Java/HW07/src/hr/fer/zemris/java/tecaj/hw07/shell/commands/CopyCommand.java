package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Copy command will copy given source file to given destination (file or
 * directory). If destination already exists user will be asked for overwriting
 * permission.
 * <p>
 * Expected input is: {@code copy [source] [destination]}
 * 
 * @author Dan
 *
 */
public class CopyCommand extends AbstractCommand {

	/**
	 * Command description.
	 */
	private static final String DESCRIPTION = "Copy command will copy given source file to given destination (file or directory).\nIf destination already exists user will be asked for overwriting permission.\nExpected input is: \"copy [source] [destination]\"";

	/**
	 * Creates a new {@code CopyCommand}.
	 */
	public CopyCommand() {
		super("copy", DESCRIPTION);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		List<String> args = splitArgument(argument);

		try {
			if (args.size() != 2) {
				env.writeln("Two arguments expected.");
				return ShellStatus.CONTINUE;
			}

			File source = new File(args.get(0));
			File dest = new File(args.get(1));

			if (!source.exists() || !source.isFile()) {
				env.writeln("Source doesn't exist or is not a file.");
				return ShellStatus.CONTINUE;
			}

			if (dest.isDirectory()) {
				dest = new File(dest, source.getName());
			} else if (dest.exists()) {
				env.writeln("Destination already exists. Do you want to overwrite it? (Y/N)");
				if (!env.readLine().trim().equals("Y")) {
					env.writeln("Aborting copying.");
					return ShellStatus.CONTINUE;
				}
			}

			if (dest.isDirectory()) {
				dest = new File(dest, source.getName());
			}

			copyFile(source.toPath(), dest.toPath());

		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Copies the source file to the destination file.
	 * 
	 * @param source
	 *            file
	 * @param dest
	 *            file
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	private void copyFile(Path source, Path dest) throws IOException {
		InputStream input = new BufferedInputStream(Files.newInputStream(source));
		OutputStream output = new BufferedOutputStream(Files.newOutputStream(dest));

		byte[] b = new byte[2048];
		int n;

		while ((n = input.read(b)) > 0) {
			output.write(b, 0, n);
		}
		output.flush();

		input.close();
		output.close();
	}

}
