package hr.fer.zemris.java.simplecomp;

import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * This class offers utility methods for register descriptors that are described
 * in {@link InstructionArgument} documentation.
 * 
 * @author Dan
 *
 */
public class RegisterUtil {

	/**
	 * Returns the register index contained in given descriptor.
	 * 
	 * @param registerDescriptor
	 *            register descriptor
	 * @return number represented by the lowest 8 bits of given {@code Integer}
	 *         as unsigned 8-bit number (0 - 255)
	 */
	public static int getRegisterIndex(int registerDescriptor) {
		int mask = 0xFF;
		return registerDescriptor & mask;
	}

	/**
	 * Checks if given descriptor contains indirect addressing.
	 * 
	 * @param registerDescriptor
	 *            register descriptor
	 * @return {@code true} if 24th bit of given {@code Integer} is 1; otherwise
	 *         {@code false}
	 */
	public static boolean isIndirect(int registerDescriptor) {
		int mask = 1 << 24;
		return (registerDescriptor & mask) != 0;
	}

	/**
	 * Returns the address offset contained in given descriptor.
	 * 
	 * @param registerDescriptor
	 *            register descriptor
	 * @return number represented by bits on positions 8 - 23 of given
	 *         {@code Integer} as signed 16-bit number
	 */
	public static int getRegisterOffset(int registerDescriptor) {
		int mask = 0xFFFF;
		short result = (short) ((registerDescriptor >> 8) & mask);
		return result;
	}
}
