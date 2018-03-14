package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class ValueWrapperTest {

	@Test(expected=IllegalArgumentException.class)
	public void testIllegalType(){
		new ValueWrapper(Long.valueOf(1));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIllegalString(){
		ValueWrapper v = new ValueWrapper(Integer.valueOf(1));
		v.increment("12a");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIllegalString2(){
		ValueWrapper v = new ValueWrapper("1e2e");
		v.increment("1");
	}
	
	@Test
	public void testValueSetting() {
		ValueWrapper v = new ValueWrapper(null);
		assertNull(v.getValue());
		
		String value = "5";
		v.setValue(value);
		assertEquals(v.getValue(), value);
	}
	
	@Test
	public void testIncrement(){
		ValueWrapper v = new ValueWrapper(Integer.valueOf(5));
		
		v.increment(Integer.valueOf(4));
		assertEquals(Integer.valueOf(9), v.getValue());
		
		v.increment("-3");
		assertEquals(Integer.valueOf(6), v.getValue());
		
		v.increment(null);
		assertEquals(Integer.valueOf(6), v.getValue());
		
		v.increment(Double.valueOf(2));
		assertEquals(Double.valueOf(8), v.getValue());
		
		v.increment("1.1");
		assertEquals(Double.valueOf(9.1), v.getValue());
	}
	
	@Test
	public void testIncrementMaxInt() {
		ValueWrapper v = new ValueWrapper(Integer.MAX_VALUE);
		v.increment(1);
		assertEquals(Integer.MIN_VALUE, v.getValue());
		
		v.setValue(Integer.MAX_VALUE);
		v.increment(1.1);
		assertEquals(Integer.MAX_VALUE + 1.1, v.getValue());
	}
	
	@Test
	public void testDecrement(){
		ValueWrapper v = new ValueWrapper("10");
		
		v.decrement(Integer.valueOf(1));
		assertEquals(Integer.valueOf(9), v.getValue());
		
		v.decrement(null);
		assertEquals(Integer.valueOf(9), v.getValue());
		
		v.decrement("-2E0");
		assertEquals(Double.valueOf(11), v.getValue());
		
		v.decrement(Integer.valueOf(11));
		assertEquals(Double.valueOf(0), v.getValue());
	}
	
	@Test
	public void testDecrementMinInt() {
		ValueWrapper v = new ValueWrapper(new Integer(Integer.MIN_VALUE));
		v.decrement(1);
		assertEquals(Integer.MAX_VALUE, v.getValue());
		
		v.setValue(Integer.MIN_VALUE);
		v.decrement(1.1);
		assertEquals(Integer.MIN_VALUE - 1.1, v.getValue());
	}
	
	@Test
	public void testMultiply(){
		ValueWrapper v = new ValueWrapper("-10");

		v.multiply(Integer.valueOf(-1));
		assertEquals(Integer.valueOf(10), v.getValue());
		
		v.multiply("2.5");
		assertEquals(Double.valueOf(25), v.getValue());
		
		v.multiply(null);
		assertEquals(Double.valueOf(0), v.getValue());
	}
	
	@Test
	public void testDivideInteger(){
		ValueWrapper v = new ValueWrapper("100");
		
		v.divide("3");
		assertEquals(Integer.valueOf(33), v.getValue());
		
		v.divide("33");
		assertEquals(Integer.valueOf(1), v.getValue());
	}
	
	@Test(expected=ArithmeticException.class)
	public void testDivideIntegerWithZero(){
		ValueWrapper v = new ValueWrapper("1");
		v.divide(null);
	}
	
	@Test
	public void testDivideDouble(){
		ValueWrapper v = new ValueWrapper("50.0");
		
		v.divide("5");
		assertEquals(Double.valueOf(10), v.getValue());
		
		v.divide("10.0");
		assertEquals(Double.valueOf(1), v.getValue());
		
		v.divide(null);
		assertEquals(Double.POSITIVE_INFINITY, v.getValue());
	}	

	@Test
	public void testNumCompare() {
		ValueWrapper v = new ValueWrapper(Math.ulp(0.));
		assertFalse(v.numCompare(0.0) == 0);
		assertFalse(v.numCompare(0) == 0);
		assertTrue(v.numCompare(0) > 0);
		
		v.setValue(0);
		assertFalse(v.numCompare(Math.ulp(0.)) == 0);
		
		v.setValue(1.0);
		assertTrue(v.numCompare(0) > 0);
		
		v.setValue(Integer.MIN_VALUE);
		assertTrue(v.numCompare(50) < 0);
		
		v.setValue(0.0);
		assertTrue(v.numCompare(0) == 0);
	}
	
}
