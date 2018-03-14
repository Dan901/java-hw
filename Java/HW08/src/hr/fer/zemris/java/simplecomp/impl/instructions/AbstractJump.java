package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Abstract class for all variants of jump instructions. Holds location of the
 * next instruction and can execute the jump, so different implementations can
 * choose should the jump occur.
 * 
 * @author Dan
 *
 */
public abstract class AbstractJump implements Instruction {

	/**
	 * Location of the next instruction.
	 */
	private int memoryLocation;

	/**
	 * Creates a new {@code AbstractJump} with given arguments.
	 * 
	 * @param arguments
	 *            1 memory location
	 */
	public AbstractJump(List<InstructionArgument> arguments) {
		if (arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 argument!");
		}

		if (!arguments.get(0).isNumber()) {
			throw new IllegalArgumentException("Type mismatch for memory location argument!");
		}
		memoryLocation = (Integer) arguments.get(0).getValue();

	}

	@Override
	public boolean execute(Computer computer) {
		computer.getRegisters().setProgramCounter(memoryLocation);
		return false;
	}

}
