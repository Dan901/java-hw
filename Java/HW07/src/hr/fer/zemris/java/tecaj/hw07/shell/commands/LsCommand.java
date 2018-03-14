package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * List directory contents command will list everything contained in given
 * directory with it's basic attributes.
 * <p>
 * Expected input is: {@code ls [directory]}
 * 
 * @author Dan
 *
 */
public class LsCommand extends AbstractCommand {

	/**
	 * Command description.
	 */
	private static final String DESCRIPTION = "List directory contents command will list everything contained in given directory with it's basic attributes.\nExpected input is: \"ls [directory]\"";

	/**
	 * Creates a new {@code LsCommand}.
	 */
	public LsCommand() {
		super("ls", DESCRIPTION);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		List<String> args = splitArgument(argument);

		try {
			if (args.size() != 1) {
				env.writeln("One argument expected.");
				return ShellStatus.CONTINUE;
			}

			File directory = new File(args.get(0));
			File[] files = directory.listFiles();

			if (files == null) {
				env.writeln("Given path is not a directory.");
				return ShellStatus.CONTINUE;
			}
			
			for (File file : files) {
				env.writeln(fileToString(file));
			}

		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage());
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Converts given {@code File} to {@code String} in 4 column format with
	 * basic attributes.
	 * 
	 * @param file
	 *            {@code File} to convert
	 * @return {@code String} with basic file attributes
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	private String fileToString(File file) throws IOException {
		StringBuilder sb = new StringBuilder();

		if (file.isDirectory()) {
			sb.append("d");
		} else {
			sb.append("-");
		}

		if (file.canRead()) {
			sb.append("r");
		} else {
			sb.append("-");
		}

		if (file.canWrite()) {
			sb.append("w");
		} else {
			sb.append("-");
		}

		if (file.canExecute()) {
			sb.append("x ");
		} else {
			sb.append("- ");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BasicFileAttributeView faView = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class,
				LinkOption.NOFOLLOW_LINKS);
		BasicFileAttributes attrs = faView.readAttributes();

		sb.append(String.format("%10d ", attrs.size()));
		sb.append(sdf.format(new Date(attrs.creationTime().toMillis())));
		sb.append(" " + file.getName());

		return sb.toString();
	}

}
