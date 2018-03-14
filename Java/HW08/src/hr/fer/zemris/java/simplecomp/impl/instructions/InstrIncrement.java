package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Increment instruction increments the value stored in given register by 1.
 * <p>
 * Example:
 * 
 * <pre>
 * increment rX
 * </pre>
 * 
 * @author Dan
 *
 */
public class InstrIncrement extends AbstractIncDec {

	/**
	 * Creates a new {@code InstrIncrement} with given arguments.
	 * 
	 * @param arguments
	 *            one register descriptor
	 */
	public InstrIncrement(List<InstructionArgument> arguments) {
		super(arguments);
		value = 1;
	}

}
