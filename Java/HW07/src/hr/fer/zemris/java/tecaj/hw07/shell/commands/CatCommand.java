package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Cat command writes contents of the given file. If no charset is provided
 * system's default will be used. To check available charsets run
 * {@code CharsetsCommand}.
 * <p>
 * Expected input: {@code cat [path] [charset]}
 * 
 * @author Dan
 *
 */
public class CatCommand extends AbstractCommand {

	/**
	 * Command description.
	 */
	private static final String DESCRIPTION = "Cat command writes contents of the given file.\nIf no charset is provided system's default will be used.\nTo check available charsets run \"charset\" command.\nExpected input: \"cat [path] [charset]\"";

	/**
	 * Creates a new {@code CatCommand}.
	 */
	public CatCommand() {
		super("cat", DESCRIPTION);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		List<String> args = splitArgument(argument);

		try {
			if (args.size() < 1) {
				env.writeln("File name expected.");
				return ShellStatus.CONTINUE;
			}

			Charset cs = Charset.defaultCharset();
			if (args.size() == 2) {
				if (Charset.isSupported(args.get(1))) {
					cs = Charset.forName(args.get(1));
				} else {
					env.writeln("Unsupported charset. Proceeding with default one.");
				}
			}

			File file = new File(args.get(0));

			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(new BufferedInputStream(new FileInputStream(file)), cs))) {
				String line;
				while ((line = reader.readLine()) != null) {
					env.writeln(line);
				}

				reader.close();
			} catch (FileNotFoundException e) {
				env.writeln("File doesn't exist.");
				return ShellStatus.CONTINUE;
			}

		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage());
		}

		return ShellStatus.CONTINUE;
	}

}
