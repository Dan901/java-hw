package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Make directory command will create the appropriate directory structure
 * depending on given path (relative or absolute).
 * <p>
 * Expected input is: {@code mkdir [path]}
 * 
 * @author Dan
 *
 */
public class MkDirCommand extends AbstractCommand {

	/**
	 * Command description.
	 */
	private static final String DESCRIPTION = "Make directory command will create the appropriate directory structure depending on given path (relative or absolute).\nExpected input is: \"mkdir [path]\"";

	/**
	 * Creates a new {@code MkDirCommand}.
	 */
	public MkDirCommand() {
		super("mkdir", DESCRIPTION);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		List<String> args = splitArgument(argument);

		try {
			if (args.size() != 1) {
				env.writeln("One argument expected.");
				return ShellStatus.CONTINUE;
			}

			Path path;
			try {
				path = Paths.get(args.get(0));
			} catch (InvalidPathException e) {
				env.writeln("Invalid path.");
				return ShellStatus.CONTINUE;
			}
			
			try {
				Files.createDirectories(path);
			} catch (FileAlreadyExistsException e) {
				env.writeln("Cannot create the directory.");
				return ShellStatus.CONTINUE;
			}
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage());
		}

		return ShellStatus.CONTINUE;
	}

}
