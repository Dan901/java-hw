package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;
import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Abstract class for instructions that use stack. Static methods (push and pop)
 * are available.
 * 
 * @author Dan
 *
 */
public abstract class AbstractStack implements Instruction {

	/**
	 * Index of the register that is used for push and pop.
	 */
	protected int registerIndex;

	/**
	 * Creates a new {@code AbstractStack} with given arguments.
	 * 
	 * @param arguments
	 *            1 register descriptor
	 */
	public AbstractStack(List<InstructionArgument> arguments) {
		if (arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 argument!");
		}

		if (!arguments.get(0).isRegister() || RegisterUtil.isIndirect((Integer) arguments.get(0).getValue())) {
			throw new IllegalArgumentException("Type mismatch for register argument!");
		}
		registerIndex = RegisterUtil.getRegisterIndex((Integer) arguments.get(0).getValue());
	}

	/**
	 * Checks if given registers contain a stack pointer register.
	 * 
	 * @param registers
	 *            registers to check
	 * @throws IllegalStateException
	 *             if {@code registers} doesn't contain a stack pointer register
	 */
	private static void checkIfStackExists(Registers registers) {
		try {
			registers.getRegisterValue(Registers.STACK_REGISTER_INDEX);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalStateException("Stack doesn't exist in this computer!");
		}
	}

	/**
	 * Pushes the given value onto the stack of given computer.
	 * 
	 * @param computer
	 *            computer with a stack
	 * @param value
	 *            value to push
	 * @throws IllegalStateException
	 *             if {@code computer} doesn't contain a stack
	 */
	protected static void push(Computer computer, Object value) {
		checkIfStackExists(computer.getRegisters());
		int address = (Integer) computer.getRegisters().getRegisterValue(Registers.STACK_REGISTER_INDEX);
		computer.getMemory().setLocation(address, value);
		computer.getRegisters().setRegisterValue(Registers.STACK_REGISTER_INDEX, address - 1);
	}

	/**
	 * Pops from the stack of a given computer.
	 * 
	 * @param computer
	 *            computer with a stack
	 * @return an {@code Object} that was popped
	 * @throws IllegalStateException
	 *             if {@code computer} doesn't contain a stack
	 */
	protected static Object pop(Computer computer) {
		checkIfStackExists(computer.getRegisters());
		int address = (Integer) computer.getRegisters().getRegisterValue(Registers.STACK_REGISTER_INDEX) + 1;
		Object value = computer.getMemory().getLocation(address);
		computer.getRegisters().setRegisterValue(Registers.STACK_REGISTER_INDEX, address);
		return value;
	}

}
