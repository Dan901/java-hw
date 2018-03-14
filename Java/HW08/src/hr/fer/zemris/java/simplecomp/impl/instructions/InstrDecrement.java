package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Decrement instruction decrements the value stored in given register by 1.
 * <p>
 * Example:
 * 
 * <pre>
 * decrement rX
 * </pre>
 * 
 * @author Dan
 *
 */
public class InstrDecrement extends AbstractIncDec {

	/**
	 * Creates a new {@code InstrDecrement} with given arguments.
	 * 
	 * @param arguments
	 *            one register descriptor
	 */
	public InstrDecrement(List<InstructionArgument> arguments) {
		super(arguments);
		value = -1;
	}

}
