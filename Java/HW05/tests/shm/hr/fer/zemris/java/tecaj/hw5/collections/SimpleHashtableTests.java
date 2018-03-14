package hr.fer.zemris.java.tecaj.hw5.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class SimpleHashtableTests {

	private SimpleHashtable<String, Integer> examMarks;

	@Before
	public void setUp() {
		examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Marko", 4);
		examMarks.put("Ivan", 3);
		examMarks.put("Ana", 5);

	}

	@Test
	public void testToString() {
		assertEquals("[Marko=4, Ana=5, Ivana=2, Kristina=5, Ivan=3, Ante=2, Jasna=2]", examMarks.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor() {
		new SimpleHashtable<>(0);
	}

	@Test(expected = NullPointerException.class)
	public void testPutNull() {
		examMarks.put(null, 1);
	}

	@Test
	public void testPutAndGet() {
		assertEquals(2, (int) examMarks.get("Ivana"));
		examMarks.put("Ivana", 5);
		assertEquals(5, (int) examMarks.get("Ivana"));

		examMarks.put("Marija", null);
		assertNull(examMarks.get("Marija"));

		assertEquals(5, (int) examMarks.get("Ana"));
		assertNull(examMarks.get(null));
	}

	@Test
	public void testSize() {
		assertEquals(7, examMarks.size());
	}

	@Test
	public void testIsEmpty() {
		assertTrue((new SimpleHashtable<>()).isEmpty());
		assertFalse(examMarks.isEmpty());
	}

	@Test
	public void testContainsKey() {
		assertTrue(examMarks.containsKey("Ana"));
		assertTrue(examMarks.containsKey("Jasna"));
		assertFalse(examMarks.containsKey("ana"));
		assertFalse(examMarks.containsKey(2));
		assertFalse(examMarks.containsKey(null));
	}

	@Test
	public void testContainsValue() {
		assertTrue(examMarks.containsValue(5));
		assertFalse(examMarks.containsValue(6));
		assertFalse(examMarks.containsValue(null));

		examMarks.put("Marija", null);
		assertTrue(examMarks.containsValue(null));

	}

	@Test
	public void testRemove() {
		// no change
		examMarks.remove("marko");
		examMarks.remove(null);
		assertEquals(7, examMarks.size());

		examMarks.remove("Marko");
		assertEquals(6, examMarks.size());
		assertFalse(examMarks.containsKey("Marko"));
	}

	@Test
	public void testClearAndRemove() {
		examMarks.clear();
		assertEquals(0, examMarks.size());
		assertTrue(examMarks.isEmpty());

		examMarks.remove("Marko");
		assertEquals(0, examMarks.size());
		assertTrue(examMarks.isEmpty());
	}

	@Test
	public void testIteratorCartesianProduct() {
		// prints to stdout
		for (SimpleHashtable.TableEntry<String, Integer> pair1 : examMarks) {
			for (SimpleHashtable.TableEntry<String, Integer> pair2 : examMarks) {
				System.out.printf("(%s => %d) - (%s => %d)%n", pair1.getKey(), pair1.getValue(), pair2.getKey(),
						pair2.getValue());
			}
		}
	}

	@Test(expected = IllegalStateException.class)
	public void testIteratorRemoveTwice() {
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		while (iter.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
			if (pair.getKey().equals("Ivana")) {
				iter.remove();
				iter.remove();
			}
		}
	}

	@Test(expected = ConcurrentModificationException.class)
	public void testIteratorIllegalRemove() {
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		while (iter.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
			if (pair.getKey().equals("Ivana")) {
				examMarks.remove("Ivana");
			}
		}
	}

	@Test
	public void testIteratorRemove() {
		assertTrue(examMarks.containsKey("Ivana"));
		
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		while (iter.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
			if (pair.getKey().equals("Ivana")) {
				iter.remove();
			}
		}
		
		assertFalse(examMarks.containsKey("Ivana"));
		assertEquals(6, examMarks.size());
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testIteratorEmpty(){
		(new SimpleHashtable<>()).iterator().next();
	}
	
	@SuppressWarnings("unused")
	@Test(expected=ConcurrentModificationException.class)
	public void testIterator() {
		//valid remove from inner iterator causes outer to throw
		for (SimpleHashtable.TableEntry<String, Integer> pair1 : examMarks) {
			Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
			while (iter.hasNext()) {
				SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
				if (pair.getKey().equals("Ivana")) {
					iter.remove();
				}
			}
		}
	}
}
