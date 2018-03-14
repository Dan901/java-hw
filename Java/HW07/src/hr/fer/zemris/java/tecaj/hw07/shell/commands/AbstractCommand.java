package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.ShellCommand;

/**
 * Abstract class for shell commands that share common properties (like name and
 * description) and the same argument splitting rules.
 * 
 * @author Dan
 *
 */
public abstract class AbstractCommand implements ShellCommand {

	/**
	 * Name of the command.
	 */
	private final String commandName;

	/**
	 * Description of a command divided into lines.
	 */
	private final List<String> commandDescription;

	/**
	 * Creates a new command with given arguments.
	 * 
	 * @param name
	 *            command name
	 * @param description
	 *            command description
	 */
	public AbstractCommand(String name, String description) {
		commandName = name;
		commandDescription = Collections.unmodifiableList(Arrays.asList(description.split("\\n")));
	}

	@Override
	public String getCommandName() {
		return commandName;
	}

	@Override
	public List<String> getCommandDescription() {
		return commandDescription;
	}

	/**
	 * Splits arguments around whitespaces, unless the argument is inside
	 * quotation marks, in which case it is considered as one argument and the
	 * quotation marks are discarded.
	 * <p>
	 * Escaping inside quotation marks is supported for characters '\' and '"'
	 * using '\' as escape character.
	 *
	 * @param arg
	 *            {@code String} containing arguments
	 * @return {@code List} of arguments or an empty {@code List} in case the string is null or
	 *         doesn't contain any arguments
	 */
	public static List<String> splitArgument(String arg) {
		List<String> arguments = new ArrayList<>();

		if (arg == null) {
			return arguments;
		}

		StringBuilder builder = null;
		SplitStates state = SplitStates.START;

		for (int i = 0; i < arg.length(); i++) {
			char currentChar = arg.charAt(i);

			switch (state) {
			case START:
				if (!Character.isWhitespace(currentChar)) {
					builder = new StringBuilder();
					
					if (currentChar == '"') {
						state = SplitStates.QUOTE;
					} else {
						state = SplitStates.WORD;
						builder.append(currentChar);
					}
				}
				continue;
				
			case WORD:
				if (Character.isWhitespace(currentChar)) {
					arguments.add(builder.toString());
					state = SplitStates.START;
				} else {
					builder.append(currentChar);
				}
				continue;
				
			case QUOTE:
				if (currentChar == '"') {
					arguments.add(builder.toString());
					state = SplitStates.START;
				} else if (currentChar == '\\') {
					state = SplitStates.ESCAPED;
				} else {
					builder.append(currentChar);
				}
				continue;
				
			case ESCAPED:
				if (currentChar != '"' && currentChar != '\\') {
					builder.append(arg.charAt(i - 1));
				}
				
				builder.append(currentChar);
				state = SplitStates.QUOTE;
				continue;
			}
		}

		if (state != SplitStates.START) {
			arguments.add(builder.toString());
		}

		return arguments;
	}

	/**
	 * States used for parsing {@code String} arguments.
	 *
	 * @author Dan
	 *
	 */
	private enum SplitStates {
		/**
		 * Start state.
		 */
		START,

		/**
		 * Currently reading argument (not in quotes).
		 */
		WORD,

		/**
		 * Currently reading argument in quotes.
		 */
		QUOTE,

		/**
		 * Currently reading argument in quotes and previous character was an
		 * escape character ("\").
		 */
		ESCAPED
	}
}
