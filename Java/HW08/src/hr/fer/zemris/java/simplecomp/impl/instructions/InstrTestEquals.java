package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * TestEquals instruction tests if two registers contain the same values and
 * sets the computer's {@code flag} accordingly.
 * <p>
 * Example:
 * 
 * <pre>
 * testEquals rX, rY
 * </pre>
 * 
 * @author Dan
 *
 */
public class InstrTestEquals implements Instruction {

	/**
	 * Index of the first register.
	 */
	private int registerIndex1;

	/**
	 * Index of the second register.
	 */
	private int registerIndex2;

	/**
	 * Creates a new {@code InstrTestEquals} with given arguments.
	 * 
	 * @param arguments
	 *            2 register descriptors
	 */
	public InstrTestEquals(List<InstructionArgument> arguments) {
		if (arguments.size() != 2) {
			throw new IllegalArgumentException("Expected 2 arguments!");
		}

		for (int i = 0; i < 2; i++) {
			if (!arguments.get(i).isRegister() || RegisterUtil.isIndirect((Integer) arguments.get(i).getValue())) {
				throw new IllegalArgumentException("Type mismatch for register argument!");
			}
		}

		registerIndex1 = RegisterUtil.getRegisterIndex((Integer) arguments.get(0).getValue());
		registerIndex2 = RegisterUtil.getRegisterIndex((Integer) arguments.get(1).getValue());
	}

	@Override
	public boolean execute(Computer computer) {
		Object value1 = computer.getRegisters().getRegisterValue(registerIndex1);
		Object value2 = computer.getRegisters().getRegisterValue(registerIndex2);

		computer.getRegisters().setFlag(value1.equals(value2));
		return false;
	}

}
