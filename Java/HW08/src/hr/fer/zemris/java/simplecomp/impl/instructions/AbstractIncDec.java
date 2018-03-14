package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Abstract class for instructions that add a fixed value to a current value
 * stored in a register.
 * 
 * @author Dan
 *
 */
public abstract class AbstractIncDec implements Instruction {

	/**
	 * Index of the register.
	 */
	private int registerIndex;

	/**
	 * Value to be added to the register's current value.
	 */
	protected int value;

	/**
	 * Creates a new {@code AbstractIncDec} with given arguments.
	 * 
	 * @param arguments
	 *            1 register descriptor
	 */
	public AbstractIncDec(List<InstructionArgument> arguments) {
		if (arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 argument!");
		}

		if (!arguments.get(0).isRegister() || RegisterUtil.isIndirect((Integer) arguments.get(0).getValue())) {
			throw new IllegalArgumentException("Type mismatch for register argument!");
		}
		registerIndex = RegisterUtil.getRegisterIndex((Integer) arguments.get(0).getValue());
	}

	@Override
	public boolean execute(Computer computer) {
		Integer regValue = (Integer) computer.getRegisters().getRegisterValue(registerIndex);
		computer.getRegisters().setRegisterValue(registerIndex, regValue + value);

		return false;
	}

}
