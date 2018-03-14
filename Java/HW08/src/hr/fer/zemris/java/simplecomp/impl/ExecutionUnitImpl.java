package hr.fer.zemris.java.simplecomp.impl;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.ExecutionUnit;
import hr.fer.zemris.java.simplecomp.models.Instruction;

/**
 * Implementation of {@link ExecutionUnit}.
 * 
 * @author Dan
 *
 */
public class ExecutionUnitImpl implements ExecutionUnit {

	@Override
	public boolean go(Computer computer) {
		computer.getRegisters().setProgramCounter(0);
		
		try {
			while(true){
				Instruction instr = (Instruction) computer.getMemory().getLocation(computer.getRegisters().getProgramCounter());
				computer.getRegisters().incrementProgramCounter();
				if(instr.execute(computer)){
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

}
