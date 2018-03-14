package hr.fer.zemris.java.simplecomp.impl;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Memory;
import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Implementation of interface {@link Compiler} that consists of {@link Memory}
 * and {@link Registers}.
 * 
 * @author Dan
 *
 */
public class ComputerImpl implements Computer {

	/**
	 * Computer's memory.
	 */
	private Memory memory;

	/**
	 * Computer's registers.
	 */
	private Registers registers;

	/**
	 * Creates a new {@code ComputerImpl} with given arguments.
	 * 
	 * @param memorySize
	 *            size of the memory
	 * @param regsLen
	 *            number of registers
	 * @throws IllegalArgumentException
	 *             if {@code memorySize} or {@code regsLen} are negative
	 */
	public ComputerImpl(int memorySize, int regsLen) {
		if (memorySize < 0) {
			throw new IllegalArgumentException("Minimal memory size is 0.");
		}
		memory = new MemoryImpl(memorySize);

		if (regsLen < 0) {
			throw new IllegalArgumentException("Minimal number of registers is 0.");
		}
		registers = new RegistersImpl(regsLen);
	}

	@Override
	public Registers getRegisters() {
		return registers;
	}

	@Override
	public Memory getMemory() {
		return memory;
	}

}
