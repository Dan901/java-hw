package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.EmptyStackException;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class ObjectMultistackTest {

	private ObjectMultistack multiStack;
	private Object[] values;
	
	@Before
	public void setUp(){
		values = new Object[]{"1.2", Integer.valueOf(2), Double.valueOf(5), null, "-10"};
		
		ValueWrapper v0 = new ValueWrapper(values[0]);
		ValueWrapper v1 = new ValueWrapper(values[1]);
		ValueWrapper v2 = new ValueWrapper(values[2]);
		ValueWrapper v3 = new ValueWrapper(values[3]);
		ValueWrapper v4 = new ValueWrapper(values[4]);
		
		multiStack = new ObjectMultistack();
		multiStack.push("a", v0);
		multiStack.push("a", v1);
		multiStack.push("a", v2);
		multiStack.push("a", v3);
		multiStack.push("a", v4);
		
		multiStack.push("b", v1);
		multiStack.push("b", v2);
		multiStack.push("b", v2);
		multiStack.push("b", v1);
	}
	
	@Test
	public void testEmptyStack(){
		assertTrue(multiStack.isEmpty("c"));
		assertFalse(multiStack.isEmpty("a"));
	}
	
	@Test(expected=EmptyStackException.class)
	public void testPeekEmptyStack(){
		multiStack.peek("c");
	}
	
	@Test(expected=EmptyStackException.class)
	public void testPushEmptyStack(){
		multiStack.pop("c");
	}

	@Test
	public void testPopAndPeek(){
		assertEquals(multiStack.pop("a").numCompare(values[4]), 0);
		assertEquals(multiStack.peek("a").numCompare(values[3]), 0);
		assertEquals(multiStack.pop("a").numCompare(values[3]), 0);
		assertEquals(multiStack.pop("a").numCompare(values[2]), 0);
		assertEquals(multiStack.pop("a").numCompare(values[1]), 0);
		assertEquals(multiStack.pop("a").numCompare(values[0]), 0);
		
		assertEquals(multiStack.pop("b").numCompare(values[1]), 0);
		multiStack.push("b", new ValueWrapper(values[4]));
		assertEquals(multiStack.peek("b").numCompare(values[4]), 0);
		assertEquals(multiStack.pop("b").numCompare(values[4]), 0);
		assertEquals(multiStack.pop("b").numCompare(values[2]), 0);
		assertEquals(multiStack.pop("b").numCompare(values[2]), 0);
		assertEquals(multiStack.pop("b").numCompare(values[1]), 0);
		
		assertTrue(multiStack.isEmpty("a"));
		assertTrue(multiStack.isEmpty("b"));
	}
}
