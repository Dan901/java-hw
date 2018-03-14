package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Push instruction stores a value from a register onto a computer's stack.
 * <p>
 * Example:
 * 
 * <pre>
 * push rX
 * </pre>
 * 
 * @author Dan
 *
 */
public class InstrPush extends AbstractStack {

	/**
	 * Creates a new {@code InstrPush} with given arguments.
	 * 
	 * @param arguments
	 *            1 register descriptor
	 */
	public InstrPush(List<InstructionArgument> arguments) {
		super(arguments);
	}

	@Override
	public boolean execute(Computer computer) {
		push(computer, computer.getRegisters().getRegisterValue(registerIndex));
		return false;
	}

}
