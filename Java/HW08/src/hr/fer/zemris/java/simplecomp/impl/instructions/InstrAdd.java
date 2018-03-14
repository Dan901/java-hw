package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Add instruction adds numbers from 2 registers and stores the result into a
 * third one.
 * 
 * <p>
 * Example:
 * 
 * <pre>
 * add rX, rY, rZ
 * </pre>
 * 
 * will result with:
 * 
 * <pre>
 * rX = rY + rZ
 * </pre>
 * 
 * @author Dan
 *
 */
public class InstrAdd implements Instruction {

	/**
	 * First register index.
	 */
	private int registerIndex1;

	/**
	 * Second register index.
	 */
	private int registerIndex2;

	/**
	 * Third register index.
	 */
	private int registerIndex3;

	/**
	 * Creates a new {@code InstrAdd} with given arguments.
	 * 
	 * @param arguments
	 *            3 register descriptors
	 */
	public InstrAdd(List<InstructionArgument> arguments) {
		if (arguments.size() != 3) {
			throw new IllegalArgumentException("Expected 3 arguments!");
		}

		for (int i = 0; i < 3; i++) {
			if (!arguments.get(i).isRegister() || RegisterUtil.isIndirect((Integer) arguments.get(i).getValue())) {
				throw new IllegalArgumentException("Type mismatch for register argument!");
			}
		}

		this.registerIndex1 = RegisterUtil.getRegisterIndex((Integer) arguments.get(0).getValue());
		this.registerIndex2 = RegisterUtil.getRegisterIndex((Integer) arguments.get(1).getValue());
		this.registerIndex3 = RegisterUtil.getRegisterIndex((Integer) arguments.get(2).getValue());
	}

	@Override
	public boolean execute(Computer computer) {
		Integer value1 = (Integer) computer.getRegisters().getRegisterValue(registerIndex2);
		Integer value2 = (Integer) computer.getRegisters().getRegisterValue(registerIndex3);

		computer.getRegisters().setRegisterValue(registerIndex1, value1 + value2);
		return false;
	}

}
