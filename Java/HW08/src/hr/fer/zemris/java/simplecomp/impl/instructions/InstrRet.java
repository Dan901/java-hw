package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Return instruction is used for returning from a procedure. Returning address
 * is popped from computer's stack.
 * <p>
 * Example:
 * 
 * <pre>
 * ret
 * </pre>
 * 
 * @author Dan
 *
 */
public class InstrRet implements Instruction {

	/**
	 * Creates a new {@code InstrRet}.
	 * 
	 * @param arguments
	 *            empty list is expected
	 */
	public InstrRet(List<InstructionArgument> arguments) {
		if (arguments.size() != 0) {
			throw new IllegalArgumentException("Arguments were not expected!");
		}
	}

	@Override
	public boolean execute(Computer computer) {
		int address = (Integer) AbstractStack.pop(computer);
		computer.getRegisters().setProgramCounter(address);
		return false;
	}

}
