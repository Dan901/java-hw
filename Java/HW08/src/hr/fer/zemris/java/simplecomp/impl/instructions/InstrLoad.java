package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Load instruction loads from memory to one register.
 * <p>
 * Example:
 * 
 * <pre>
 * load rX, address
 * </pre>
 * 
 * @author Dan
 *
 */
public class InstrLoad implements Instruction {

	/**
	 * Index of the register to load to.
	 */
	private int registerIndex;

	/**
	 * Memory location containing value.
	 */
	private int memoryLocation;

	/**
	 * Creates a new {@code InstrLoad} with given arguments.
	 * 
	 * @param arguments
	 *            register descriptor and memory location
	 */
	public InstrLoad(List<InstructionArgument> arguments) {
		if (arguments.size() != 2) {
			throw new IllegalArgumentException("Expected 2 arguments!");
		}

		if (!arguments.get(0).isRegister() || RegisterUtil.isIndirect((Integer) arguments.get(0).getValue())) {
			throw new IllegalArgumentException("Type mismatch for register argument!");
		}
		registerIndex = RegisterUtil.getRegisterIndex((Integer) arguments.get(0).getValue());

		if (!arguments.get(1).isNumber()) {
			throw new IllegalArgumentException("Type mismatch for memory location argument!");
		}
		memoryLocation = (Integer) arguments.get(1).getValue();
	}

	@Override
	public boolean execute(Computer computer) {
		Object value = computer.getMemory().getLocation(memoryLocation);
		computer.getRegisters().setRegisterValue(registerIndex, value);
		return false;
	}

}
