package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Halt instruction expects no arguments and stops the program execution.
 * <p>
 * Example:
 * 
 * <pre>
 * halt
 * </pre>
 * 
 * @author Dan
 *
 */
public class InstrHalt implements Instruction {

	/**
	 * Creates a new {@code InstrHalt}.
	 * 
	 * @param arguments
	 *            empty list is expected
	 */
	public InstrHalt(List<InstructionArgument> arguments) {
		if (arguments.size() != 0) {
			throw new IllegalArgumentException("Arguments were not expected!");
		}
	}

	@Override
	public boolean execute(Computer computer) {
		return true;
	}

}
