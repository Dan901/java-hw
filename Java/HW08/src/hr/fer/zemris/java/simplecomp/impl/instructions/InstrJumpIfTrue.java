package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * JumpIfTrue instruction performs a conditional jump. Sets the next instruction
 * to instruction at given memory location (program counter is set to given
 * address) if the computer's {@code flag} is {@code true}.
 * 
 * <p>
 * Example:
 * 
 * <pre>
 * jumpIfTrue address
 * </pre>
 * 
 * @author Dan
 *
 */
public class InstrJumpIfTrue extends AbstractJump {

	/**
	 * Creates a new {@code InstrJumpIfTrue} with given arguments.
	 * 
	 * @param arguments
	 *            one memory location
	 */
	public InstrJumpIfTrue(List<InstructionArgument> arguments) {
		super(arguments);
	}

	@Override
	public boolean execute(Computer computer) {
		if (computer.getRegisters().getFlag()) {
			return super.execute(computer);
		} else {
			return false;
		}
	}

}
