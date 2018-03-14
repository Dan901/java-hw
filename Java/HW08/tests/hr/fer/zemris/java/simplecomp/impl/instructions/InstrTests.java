package hr.fer.zemris.java.simplecomp.impl.instructions;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;
import hr.fer.zemris.java.simplecomp.models.Memory;
import hr.fer.zemris.java.simplecomp.models.Registers;

@SuppressWarnings("javadoc")
@RunWith(MockitoJUnitRunner.class)
public class InstrTests {

	@Mock
	private Computer computer;

	@Mock
	private Registers registers;

	@Mock
	private Memory memory;

	@Mock
	private InstructionArgument regDescriptor;
	
	@Mock
	private InstructionArgument regDescriptor2;

	@Mock
	private InstructionArgument memoryAddress;
	
	@Mock
	private InstructionArgument number;

	@Before
	public void setUp() {
		when(computer.getRegisters()).thenReturn(registers);
		when(computer.getMemory()).thenReturn(memory);

		when(regDescriptor.isRegister()).thenReturn(true);
		when(regDescriptor2.isRegister()).thenReturn(true);
		when(memoryAddress.isNumber()).thenReturn(true);
		when(number.isNumber()).thenReturn(true);
	}

	@Test
	public void testLoad() {
		int value = 5;
		int regIndex = 2;
		int location = 10;
		
		when(regDescriptor.getValue()).thenReturn(regIndex);
		when(memoryAddress.getValue()).thenReturn(location);
		when(memory.getLocation(location)).thenReturn(value);

		InstrLoad load = new InstrLoad(Arrays.asList(regDescriptor, memoryAddress));
		load.execute(computer);
		
		verify(regDescriptor, times(1)).isRegister();
		verify(memoryAddress, times(1)).isNumber();
		verify(memory, times(1)).getLocation(location);
		verify(registers, times(1)).setRegisterValue(regIndex, value);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLoadWithIndirectAddressing(){
		int regValue = 0x1000102;
		when(regDescriptor.getValue()).thenReturn(regValue);
		
		InstrLoad load = new InstrLoad(Arrays.asList(regDescriptor, memoryAddress));
		load.execute(computer);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLoadWithMoreArgs(){
		@SuppressWarnings("unchecked")
		List<InstructionArgument> args = mock(List.class);
		when(args.size()).thenReturn(3);
		new InstrLoad(args);
	}
	
	@Test
	public void testMoveNormal(){
		int regIndex = 0;
		int regIndex2 = 3;
		Object value = new JButton();
		
		when(regDescriptor.getValue()).thenReturn(regIndex);
		when(regDescriptor2.getValue()).thenReturn(regIndex2);
		when(registers.getRegisterValue(regIndex2)).thenReturn(value);
		
		InstrMove move = new InstrMove(Arrays.asList(regDescriptor, regDescriptor2));
		move.execute(computer);
		
		verify(regDescriptor, times(1)).isRegister();
		verify(regDescriptor2, times(1)).isRegister();
		verify(registers, times(1)).setRegisterValue(regIndex, value);
	}
	
	@Test
	public void testMoveWithNumber(){
		int regIndex = 4;
		int value = 10;
		
		when(regDescriptor.getValue()).thenReturn(regIndex);
		when(number.getValue()).thenReturn(value);
		
		InstrMove move = new InstrMove(Arrays.asList(regDescriptor, number));
		move.execute(computer);
		
		verify(regDescriptor, times(1)).isRegister();
		verify(number, times(1)).isNumber();
		verify(registers, times(1)).setRegisterValue(regIndex, value);
	}
	
	@Test
	public void testMoveWithIndirectAddressing(){
		int regValue = 0x1FFFF02;
		int regValue2 = 0x1000103;
		int regIndex = RegisterUtil.getRegisterIndex(regValue);
		int regIndex2 = RegisterUtil.getRegisterIndex(regValue2);
		int offset1 = RegisterUtil.getRegisterOffset(regValue);
		int offset2 = RegisterUtil.getRegisterOffset(regValue2);
		int address1 = 5;
		int address2 = 10;
		Object value = new JButton();
		
		when(regDescriptor.getValue()).thenReturn(regValue);
		when(regDescriptor2.getValue()).thenReturn(regValue2);
		when(registers.getRegisterValue(regIndex)).thenReturn(address1);
		when(registers.getRegisterValue(regIndex2)).thenReturn(address2);
		when(memory.getLocation(address2 + offset2)).thenReturn(value);
		
		InstrMove move = new InstrMove(Arrays.asList(regDescriptor, regDescriptor2));
		move.execute(computer);
		
		verify(regDescriptor, times(1)).isRegister();
		verify(regDescriptor2, times(1)).isRegister();
		verify(memory, times(1)).setLocation(address1 + offset1, value);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMoveWithMoreArgs(){
		@SuppressWarnings("unchecked")
		List<InstructionArgument> args = mock(List.class);
		when(args.size()).thenReturn(3);
		new InstrMove(args);
	}
	
	@Test
	public void testPush(){
		int regIndex = 0;
		int spValue = 20;
		Object value = "abc";
		
		when(regDescriptor.getValue()).thenReturn(regIndex);
		when(registers.getRegisterValue(regIndex)).thenReturn(value);
		when(registers.getRegisterValue(Registers.STACK_REGISTER_INDEX)).thenReturn(spValue);
		
		InstrPush push = new InstrPush(Arrays.asList(regDescriptor));
		push.execute(computer);
		
		verify(regDescriptor, times(1)).isRegister();
		verify(memory, times(1)).setLocation(spValue, value);
		verify(registers, times(1)).setRegisterValue(Registers.STACK_REGISTER_INDEX, spValue - 1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPushWithIndirectAddressing(){
		int regValue = 0x1FFFF02;
		when(regDescriptor.getValue()).thenReturn(regValue);
		
		new InstrPush(Arrays.asList(regDescriptor));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPushWithMoreArgs(){
		@SuppressWarnings("unchecked")
		List<InstructionArgument> args = mock(List.class);
		when(args.size()).thenReturn(2);
		new InstrPush(args);
	}
	
	@Test
	public void testPop(){
		int regIndex = 0;
		int spValue = 20;
		Object value = "abc";
		
		when(regDescriptor.getValue()).thenReturn(regIndex);
		when(registers.getRegisterValue(Registers.STACK_REGISTER_INDEX)).thenReturn(spValue);
		when(memory.getLocation(spValue + 1)).thenReturn(value);
		
		InstrPop pop = new InstrPop(Arrays.asList(regDescriptor));
		pop.execute(computer);
		
		verify(regDescriptor, times(1)).isRegister();
		verify(registers, times(1)).setRegisterValue(regIndex, value);
		verify(registers, times(1)).setRegisterValue(Registers.STACK_REGISTER_INDEX, spValue + 1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPopWithIndirectAddressing(){
		int regValue = 0x1FFFF02;
		when(regDescriptor.getValue()).thenReturn(regValue);
		
		new InstrPop(Arrays.asList(regDescriptor));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPopWithMoreArgs(){
		@SuppressWarnings("unchecked")
		List<InstructionArgument> args = mock(List.class);
		when(args.size()).thenReturn(2);
		new InstrPop(args);
	}
	
	@Test
	public void testCall(){
		int address = 100;
		int spValue = 50;
		int pcValue = 5;
		
		when(memoryAddress.getValue()).thenReturn(address);
		when(registers.getProgramCounter()).thenReturn(pcValue);
		when(registers.getRegisterValue(Registers.STACK_REGISTER_INDEX)).thenReturn(spValue);
		
		InstrCall call = new InstrCall(Arrays.asList(memoryAddress));
		call.execute(computer);
		
		verify(memoryAddress, times(1)).isNumber();
		verify(memory, times(1)).setLocation(spValue, pcValue);
		verify(registers, times(1)).setRegisterValue(Registers.STACK_REGISTER_INDEX, spValue - 1);
		verify(registers, times(1)).setProgramCounter(address);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCallWithMoreArgs(){
		@SuppressWarnings("unchecked")
		List<InstructionArgument> args = mock(List.class);
		when(args.size()).thenReturn(2);
		new InstrCall(args);
	}
	
	@Test
	public void testRet(){
		int spValue = 20;
		int address = 0;
		
		when(registers.getRegisterValue(Registers.STACK_REGISTER_INDEX)).thenReturn(spValue);
		when(memory.getLocation(spValue + 1)).thenReturn(address);
		
		InstrRet ret = new InstrRet(Arrays.asList());
		ret.execute(computer);
		
		verify(registers, times(1)).setRegisterValue(Registers.STACK_REGISTER_INDEX, spValue + 1);
		verify(registers, times(1)).setProgramCounter(address);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRetWithMoreArgs(){
		@SuppressWarnings("unchecked")
		List<InstructionArgument> args = mock(List.class);
		when(args.size()).thenReturn(1);
		new InstrRet(args);
	}

}
