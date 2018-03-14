package hr.fer.zemris.java.tecaj.hw07.shell;

import java.io.IOException;

/**
 * Environment for executing commands.
 * 
 * @author Dan
 *
 */
public interface Environment {

	/**
	 * Reads a line from the input stream.
	 * 
	 * @return line from input stream
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	String readLine() throws IOException;

	/**
	 * Writes a string to the output stream.
	 * 
	 * @param text
	 *            string that needs to be written
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	void write(String text) throws IOException;

	/**
	 * Writes a line to the output stream.
	 * 
	 * @param text
	 *            string that needs to be written
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	void writeln(String text) throws IOException;

	/**
	 * Returns an {@code Iterable} collection of all available commands.
	 * 
	 * @return available commands
	 */
	Iterable<ShellCommand> commands();

	/**
	 * @return Symbol that the shell prints when current command input spans
	 *         across multiple lines.
	 */
	Character getMultilineSymbol();

	/**
	 * Sets the symbol that the shell prints when current command input spans
	 * across multiple lines.
	 * 
	 * @param symbol
	 *            new symbol
	 */
	void setMultilineSymbol(Character symbol);

	/**
	 * @return Symbol that the shell prints when expecting user input.
	 */
	Character getPromptSymbol();

	/**
	 * Sets the symbol that the shell prints when expecting user input.
	 * 
	 * @param symbol
	 *            new symbol
	 */
	void setPromptSymbol(Character symbol);

	/**
	 * @return Symbol that the user writes at the end of the line to indicate
	 *         the command will span one more line.
	 */
	Character getMorelinesSymbol();

	/**
	 * Sets the symbol that the user writes at the end of the line to indicate
	 * the command will span one more line.
	 * 
	 * @param symbol
	 *            new symbol
	 */
	void setMorelinesSymbol(Character symbol);
}
