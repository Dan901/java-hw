package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Charsets command lists names of supported charsets for your Java platform and
 * takes no arguments.
 * 
 * @author Dan
 *
 */
public class CharsetsCommand extends AbstractCommand {

	/**
	 * Command description.
	 */
	private static final String DESCRIPTION = "Charsets command lists names of supported charsets for your Java platform and takes no arguments.";
	
	/**
	 * Creates a new {@code CharsetsCommand}.
	 */
	public CharsetsCommand() {
		super("charsets", DESCRIPTION);
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		Collection<String> charsets = Charset.availableCharsets().keySet();
		
		try{
			env.writeln("Available charsets on this platform are: ");
			for (String charset : charsets) {
				env.writeln(charset);
			}
			
		} catch (IOException e){
			throw new IllegalStateException(e.getMessage());
		}
		
		return ShellStatus.CONTINUE;
	}

}
