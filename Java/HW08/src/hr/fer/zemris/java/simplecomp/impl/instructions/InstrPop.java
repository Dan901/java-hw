package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Pop instruction pops the last value that was pushed onto a computer's stack
 * and stores it into a register.
 * <p>
 * Example:
 * 
 * <pre>
 * pop rX
 * </pre>
 * 
 * @author Dan
 *
 */
public class InstrPop extends AbstractStack {

	/**
	 * Creates a new {@code InstrPop} with given arguments.
	 * 
	 * @param arguments
	 *            1 register descriptor
	 */
	public InstrPop(List<InstructionArgument> arguments) {
		super(arguments);
	}

	@Override
	public boolean execute(Computer computer) {
		Object value = pop(computer);
		computer.getRegisters().setRegisterValue(registerIndex, value);
		return false;
	}

}
