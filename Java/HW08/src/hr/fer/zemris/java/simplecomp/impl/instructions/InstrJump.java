package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Jump instruction performs an unconditional jump. Sets the next instruction to
 * instruction at given memory location (program counter is set to given
 * address).
 * 
 * <p>
 * Example:
 * 
 * <pre>
 * jump address
 * </pre>
 * 
 * @author Dan
 *
 */
public class InstrJump extends AbstractJump {

	/**
	 * Creates a new {@code InstrJump} with given arguments.
	 * 
	 * @param arguments
	 *            one memory location
	 */
	public InstrJump(List<InstructionArgument> arguments) {
		super(arguments);
	}

	@Override
	public boolean execute(Computer computer) {
		return super.execute(computer);
	}

}
