package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Integer input instruction reads an {@code Integer} value from standard input
 * and stores it in a given memory location. Additionally, it sets the
 * computer's {@code flag} to {@code false} if some kind of error occurs during
 * converting to an {@code Integer} and to {@code true} if everything was
 * successful.
 * <p>
 * Example:
 * 
 * <pre>
 * iinput address
 * </pre>
 * 
 * @author Dan
 *
 */
public class InstrIinput implements Instruction {

	/**
	 * Memory address for storing the read value.
	 */
	private int address;
	
	/**
	 * {@code Reader} stream for reading the value.
	 */
	private BufferedReader reader;

	/**
	 * Creates a new {@code InstrIinput} with given arguments.
	 * 
	 * @param arguments
	 *            1 memory address
	 */
	public InstrIinput(List<InstructionArgument> arguments) {
		if (arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 argument!");
		}

		if (!arguments.get(0).isNumber()) {
			throw new IllegalArgumentException("Type mismatch for address argument!");
		}
		address = (Integer) arguments.get(0).getValue();
		reader = new BufferedReader(new InputStreamReader(System.in));
	}

	@Override
	public boolean execute(Computer computer) {
		Integer value;
		try {
			value = Integer.parseInt(reader.readLine().trim());
		} catch (Exception e){
			computer.getRegisters().setFlag(false);
			return false;
		}
		
		computer.getMemory().setLocation(address, value);
		computer.getRegisters().setFlag(true);
		
		return false;
	}

}
