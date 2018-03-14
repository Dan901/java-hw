package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Symbol command allows user to view and change symbols for: prompt, multiline
 * and morelines.
 * <p>
 * Expected input for viewing is: {@code symbol [SYMBOLNAME]} and for changing:
 * {@code symbol [SYMBOLNAME] [symbol]}
 * 
 * @author Dan
 *
 */
public class SymbolCommand extends AbstractCommand {

	/**
	 * Command description.
	 */
	private static final String DESCRIPTION = "Symbol command allows user to view and change symbols for: prompt, multiline and morelines.\nExpected input for viewing is: \"symbol [SYMBOLNAME]\" and for changing: \"symbol [SYMBOLNAME] [symbol]\"";

	/**
	 * Creates a new {@code SymbolCommand}.
	 */
	public SymbolCommand() {
		super("symbol", DESCRIPTION);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		List<String> args = splitArgument(argument);
		String symbolName;
		Character symbol;

		try {
			if (args.size() == 1) {
				symbolName = args.get(0);

				env.write("Symbol for " + symbolName + " is '");

				switch (symbolName) {
				case "PROMPT":
					symbol = env.getPromptSymbol();
					break;
				case "MORELINES":
					symbol = env.getMorelinesSymbol();
					break;
				case "MULTILINE":
					symbol = env.getMultilineSymbol();
					break;
				default:
					env.writeln("No such symbol.");
					return ShellStatus.CONTINUE;
				}

				env.writeln(symbol.toString() + "'");

			} else if (args.size() == 2) {
				symbolName = args.get(0);
				if (args.get(1).length() != 1) {
					env.writeln("Symbol can only be 1 character.");
					return ShellStatus.CONTINUE;
				}
				Character newSymbol = args.get(1).charAt(0);

				env.write("Symbol for " + symbolName + " changed from '");

				switch (symbolName) {
				case "PROMPT":
					symbol = env.getPromptSymbol();
					env.setPromptSymbol(newSymbol);
					break;
				case "MORELINES":
					symbol = env.getMorelinesSymbol();
					env.setMorelinesSymbol(newSymbol);
					break;
				case "MULTILINE":
					symbol = env.getMultilineSymbol();
					env.setMultilineSymbol(newSymbol);
					break;
				default:
					env.writeln("No such symbol.");
					return ShellStatus.CONTINUE;
				}

				env.writeln(symbol.toString() + "' to '" + newSymbol + "'");
			}
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage());
		}

		return ShellStatus.CONTINUE;
	}

}
