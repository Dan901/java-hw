package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Move instruction stores some value into a register or a memory. Arguments can
 * be a register or a memory address (indirect addressing) while source can also
 * be a plain number.
 * <p>
 * Examples:
 * 
 * <pre>
 * move rX, rY
 * move rX, number
 * move [rX+offset1], [rY+offset2]
 * </pre>
 * 
 * @author Dan
 *
 */
public class InstrMove implements Instruction {

	/**
	 * Register descriptor for a destination.
	 */
	private int destRegDescriptor;

	/**
	 * Source of the value; register descriptor or a plain number.
	 */
	private InstructionArgument source;

	/**
	 * Creates a new {@code InstrMove} with given arguments.
	 * 
	 * @param arguments
	 *            2 register descriptors or a plain number as second
	 */
	public InstrMove(List<InstructionArgument> arguments) {
		if (arguments.size() != 2) {
			throw new IllegalArgumentException("Expected 2 arguments!");
		}

		if (!arguments.get(0).isRegister()) {
			throw new IllegalArgumentException("Type mismatch for register argument!");
		}
		destRegDescriptor = (Integer) arguments.get(0).getValue();

		source = arguments.get(1);
	}

	@Override
	public boolean execute(Computer computer) {
		Object value;

		if (source.isRegister()) {
			int srcRegDescriptor = (Integer) source.getValue();
			Object srcRegValue = computer.getRegisters()
					.getRegisterValue(RegisterUtil.getRegisterIndex(srcRegDescriptor));

			if (RegisterUtil.isIndirect(srcRegDescriptor)) {
				value = computer.getMemory()
						.getLocation((Integer) srcRegValue + RegisterUtil.getRegisterOffset(srcRegDescriptor));
			} else {
				value = srcRegValue;
			}
		} else if (source.isNumber()) {
			value = source.getValue();
		} else {
			throw new IllegalArgumentException("Type mismatch for source argument!");
		}

		int destRegIndex = RegisterUtil.getRegisterIndex(destRegDescriptor);
		if (RegisterUtil.isIndirect(destRegDescriptor)) {
			int location = (Integer) computer.getRegisters().getRegisterValue(destRegIndex)
					+ RegisterUtil.getRegisterOffset(destRegDescriptor);
			computer.getMemory().setLocation(location, value);
		} else {
			computer.getRegisters().setRegisterValue(destRegIndex, value);
		}

		return false;
	}

}
