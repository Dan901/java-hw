package hr.fer.zemris.java.simplecomp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import hr.fer.zemris.java.simplecomp.impl.ComputerImpl;
import hr.fer.zemris.java.simplecomp.impl.ExecutionUnitImpl;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.ExecutionUnit;
import hr.fer.zemris.java.simplecomp.models.InstructionCreator;
import hr.fer.zemris.java.simplecomp.parser.InstructionCreatorImpl;
import hr.fer.zemris.java.simplecomp.parser.ProgramParser;

/**
 * Executes assembler code form file whose path is given either as a command
 * line argument or entered via standard input.
 * 
 * @author Dan
 *
 */
public class Simulator {

	/**
	 * Program entry point. Initializes {@link Computer}, parses commands and
	 * executes them.
	 * 
	 * @param args
	 *            file path
	 */
	public static void main(String[] args) {
		Computer comp = new ComputerImpl(256, 16);
		InstructionCreator creator = new InstructionCreatorImpl("hr.fer.zemris.java.simplecomp.impl.instructions");

		String path;
		if (args.length == 1) {
			path = args[0];
		} else {
			System.out.println("Enter path to the file with assembler code: ");
			try {
				path = (new BufferedReader(new InputStreamReader(System.in))).readLine();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}

		File file = new File(path);
		if(!file.exists() || !file.isFile()){
			System.out.println("Given file doesn't exist: " + path);
			return;
		}
		
		try {
			ProgramParser.parse(path, comp, creator);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		ExecutionUnit exec = new ExecutionUnitImpl();
		if (!exec.go(comp)) {
			System.out.println("An exception occured during execution of: " + path);
		}
	}
}
