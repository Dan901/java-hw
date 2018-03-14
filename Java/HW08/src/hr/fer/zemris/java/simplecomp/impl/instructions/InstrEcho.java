package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Echo instruction takes value from the register and prints it to standard
 * output.
 * <p>
 * Indirect addressing is supported.
 * <p>
 * Example:
 * 
 * <pre>
 * echo rX
 * echo [rX+offset]
 * </pre>
 * 
 * @author Dan
 *
 */
public class InstrEcho implements Instruction {

	/**
	 * Register descriptor.
	 */
	private int regDescriptor;

	/**
	 * Creates a new {@code InstrEcho} with given arguments.
	 * 
	 * @param arguments
	 *            register descriptor
	 */
	public InstrEcho(List<InstructionArgument> arguments) {
		if (arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 argument!");
		}

		if (!arguments.get(0).isRegister()) {
			throw new IllegalArgumentException("Type mismatch for register argument!");
		}
		regDescriptor = (Integer) arguments.get(0).getValue();
	}

	@Override
	public boolean execute(Computer computer) {
		Object value = computer.getRegisters().getRegisterValue(RegisterUtil.getRegisterIndex(regDescriptor));

		if (RegisterUtil.isIndirect(regDescriptor)) {
			if (value instanceof Integer) {
				int offset = RegisterUtil.getRegisterOffset(regDescriptor);
				value = computer.getMemory().getLocation((Integer) value + offset);
			} else {
				throw new IllegalArgumentException("Register has to contain a number for indirect addressing!");
			}
		}

		System.out.print(value.toString());

		return false;
	}

}
