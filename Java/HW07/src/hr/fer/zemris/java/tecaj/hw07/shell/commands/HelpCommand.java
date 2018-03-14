package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Help command lists either all available commands or gives description for a
 * requested command.
 * <p>
 * Expected input is: {@code help} for listing all commands or:
 * {@code help [command]} for a command description.
 * 
 * @author Dan
 *
 */
public class HelpCommand extends AbstractCommand {

	/**
	 * Command description.
	 */
	private static final String DESCRIPTION = "Help command lists either all available commands or gives description for a requested command.\nExpected input is: \"help\" for listing all commands or: \"help [command]\" for a command description.";

	/**
	 * Creates a new {@code HelpCommand}.
	 */
	public HelpCommand() {
		super("help", DESCRIPTION);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		List<String> args = splitArgument(argument);
		Iterable<ShellCommand> commands = env.commands();

		try {
			if (args.size() == 0) {
				env.writeln("Listing commands:");
				for (ShellCommand command : commands) {
					env.writeln(command.getCommandName());
				}

			} else if (args.size() == 1) {
				String name = args.get(0);
				for (ShellCommand command : commands) {
					if (name.equals(command.getCommandName())) {
						env.writeln("Description for: " + name);
						for (String line : command.getCommandDescription()) {
							env.writeln(line);
						}
						return ShellStatus.CONTINUE;
					}
				}

				env.writeln("Requested command is not supported.");

			} else {
				env.writeln("Invalid number of arguments.");
				return ShellStatus.CONTINUE;
			}

		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage());
		}

		return ShellStatus.CONTINUE;
	}

}
