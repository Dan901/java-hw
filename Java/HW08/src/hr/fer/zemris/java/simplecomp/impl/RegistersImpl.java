package hr.fer.zemris.java.simplecomp.impl;

import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Implementation of interface {@link Registers} that uses {@code Object array}
 * for storing registers' value.
 * 
 * @author Dan
 *
 */
public class RegistersImpl implements Registers {

	/**
	 * Array with values from all registers.
	 */
	private Object[] registers;
	
	/**
	 * Program counter.
	 */
	private int programCounter;
	
	/**
	 * Flag.
	 */
	private boolean flag;

	/**
	 * Creates a new {@code RegistersImpl} with given argument.
	 * @param regsLen number of registers
	 */
	public RegistersImpl(int regsLen) {
		registers = new Object[regsLen];
	}

	@Override
	public Object getRegisterValue(int index) {
		if (index < 0 || index >= registers.length) {
			throw new IndexOutOfBoundsException("Invalid register index: " + index);
		}

		return registers[index];
	}

	@Override
	public void setRegisterValue(int index, Object value) {
		if (index < 0 || index >= registers.length) {
			throw new IndexOutOfBoundsException("Invalid register index: " + index);
		}

		registers[index] = value;
	}

	@Override
	public int getProgramCounter() {
		return programCounter;
	}

	@Override
	public void setProgramCounter(int value) {
		programCounter = value;
	}

	@Override
	public void incrementProgramCounter() {
		programCounter++;
	}

	@Override
	public boolean getFlag() {
		return flag;
	}

	@Override
	public void setFlag(boolean value) {
		flag = value;
	}

}
