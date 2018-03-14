package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Call instruction is used for calling a procedure. Program counter is stored
 * onto computer's stack and set to the address of a procedure.
 * <p>
 * Example:
 * 
 * <pre>
 * call address
 * </pre>
 * 
 * @author Dan
 *
 */
public class InstrCall implements Instruction {

	/**
	 * Address of a procedure.
	 */
	private int address;

	/**
	 * Creates a new {@code InstrCall} with given arguments.
	 * 
	 * @param arguments
	 *            1 memory address
	 */
	public InstrCall(List<InstructionArgument> arguments) {
		if (arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 argument!");
		}

		if (!arguments.get(0).isNumber()) {
			throw new IllegalArgumentException("Type mismatch for address argument!");
		}
		address = (Integer) arguments.get(0).getValue();
	}

	@Override
	public boolean execute(Computer computer) {
		AbstractStack.push(computer, computer.getRegisters().getProgramCounter());
		computer.getRegisters().setProgramCounter(address);
		return false;
	}

}
