package hr.fer.zemris.java.cstr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class CStringTests {

	@Test
	public void testConstructors() {
		char[] test = "abcdef".toCharArray();

		CString cs = new CString(test, 2, 2);
		assertEquals("Invalid string", "cd", cs.toString());
		assertEquals("Invalid length", 2, cs.length());
		assertEquals("Invalid character", 'c', cs.charAt(0));

		CString cs2 = new CString(test);
		assertEquals("Expected: \"abcdef\"", "abcdef", cs2.toString());

		CString cs3 = new CString(cs2);
		assertEquals("Expected: \"abcdef\"", "abcdef", cs3.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullArgument1() {
		new CString(null, 0, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullArgument2() {
		CString.fromString(null);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInvalidIndex1() {
		new CString("abc".toCharArray(), 2, 2);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInvalidIndex2() {
		new CString("abc".toCharArray(), 4, 1);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInvalidIndex3() {
		CString cs = CString.fromString("abcdef");
		cs.charAt(7);
	}

	@Test
	public void testCharAt() {
		CString cs = new CString("abcdef".toCharArray(), 3, 3);

		assertEquals("Expected 'd'", 'd', cs.charAt(0));
		assertEquals("Expected 'e'", 'e', cs.charAt(1));
		assertEquals("Expected 'f'", 'f', cs.charAt(2));
	}

	@Test
	public void testIndexOf() {
		CString cs = CString.fromString("abbcbd");

		assertEquals("Expected 0", 0, cs.indexOf('a'));
		assertEquals("Expected 1", 1, cs.indexOf('b'));
		assertEquals("Expected 5", 5, cs.indexOf('d'));
		assertEquals("Expected -1", -1, cs.indexOf('e'));
	}

	@Test
	public void testStartsWith() {
		CString cs = CString.fromString("abcdef");
		CString cs2 = CString.fromString("");
		CString cs3 = CString.fromString("ab");
		CString cs4 = CString.fromString("abcdefg");
		CString cs5 = CString.fromString("bc");

		assertEquals("Expected true", true, cs.startsWith(cs));
		assertEquals("Expected true", true, cs.startsWith(cs2));
		assertEquals("Expected true", true, cs.startsWith(cs3));
		assertEquals("Expected false", false, cs.startsWith(cs4));
		assertEquals("Expected false", false, cs.startsWith(cs5));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullArgument3() {
		CString.fromString("a").startsWith(null);
	}

	@Test
	public void testEndsWith() {
		CString cs = CString.fromString("abcdef");
		CString cs2 = CString.fromString("");
		CString cs3 = CString.fromString("ef");
		CString cs4 = CString.fromString("abcdefg");
		CString cs5 = CString.fromString("de");

		assertEquals("Expected true", true, cs.endsWith(cs));
		assertEquals("Expected true", true, cs.endsWith(cs2));
		assertEquals("Expected true", true, cs.endsWith(cs3));
		assertEquals("Expected false", false, cs.endsWith(cs4));
		assertEquals("Expected false", false, cs.endsWith(cs5));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullArgument4() {
		CString.fromString("a").endsWith(null);
	}

	@Test
	public void testContains() {
		CString cs = CString.fromString("abcdef");
		CString cs2 = CString.fromString("");
		CString cs3 = CString.fromString("abc");
		CString cs4 = CString.fromString("de");
		CString cs5 = CString.fromString("abcdefg");
		CString cs6 = CString.fromString("gh");

		assertEquals("Expected true", true, cs.contains(cs));
		assertEquals("Expected true", true, cs.contains(cs2));
		assertEquals("Expected true", true, cs.contains(cs3));
		assertEquals("Expected true", true, cs.contains(cs4));
		assertEquals("Expected false", false, cs.contains(cs5));
		assertEquals("Expected false", false, cs.contains(cs6));
	}

	@Test
	public void testSubstring() {
		CString cs = CString.fromString("abcdef");

		assertEquals("Invalid length", 0, cs.substring(2, 2).length());
		assertEquals("Invalid substring", "abcdef", cs.substring(0, 6).toString());
		assertEquals("Invalid substring", "b", cs.substring(1, 2).toString());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInvalidIndex4() {
		CString cs = CString.fromString("abcdef");
		cs.substring(2, 1);
	}

	@Test
	public void testLeftRight() {
		CString cs = CString.fromString("abcdefgh").substring(1, 7);

		assertEquals("Invalid left", "b", cs.left(1).toString());
		assertEquals("Invalid left", "bcdefg", cs.left(6).toString());
		assertEquals("Invalid right", "g", cs.right(1).toString());
		assertEquals("Invalid right", "bcdefg", cs.right(6).toString());
	}

	@Test
	public void testAdd() {
		CString cs = CString.fromString("abcdefghi").substring(0, 4);
		CString cs2 = CString.fromString("abcdefghi").substring(4, 9);

		assertEquals("Invalid add", "abcdefghi", cs.add(cs2).toString());
		assertEquals("Expected no change", cs.toString(), cs.add(CString.fromString("")).toString());
		assertEquals("Expected no change", cs.toString(), CString.fromString("").add(cs).toString());
	}

	@Test
	public void testReplaceAll() {
		CString cs = CString.fromString("abccbaabc");
		CString cs2 = cs.substring(0, 5);

		assertEquals("Invalid replaceAll", "adccdaadc", cs.replaceAll('b', 'd').toString());
		assertEquals("Expected no change", cs.toString(), cs.replaceAll('1', '2').toString());
		assertEquals("Invalid replaceAll", "abaab", cs2.replaceAll('c', 'a').toString());
		assertEquals("Expected empty", "", CString.fromString("").replaceAll('a', 'b').toString());
	}

	@Test
	public void testReplaceAll2() {
		CString cs = CString.fromString("ababab");

		assertEquals("Invalid replaceAll", "abababababab",
				cs.replaceAll(CString.fromString("ab"), CString.fromString("abab")).toString());
		assertEquals("Invalid replaceAll", "bbb",
				cs.replaceAll(CString.fromString("a"), CString.fromString("")).toString());
		assertEquals("Invalid replaceAll", "acccacccaccc",
				cs.replaceAll(CString.fromString("b"), CString.fromString("ccc")).toString());

		CString cs2 = CString.fromString("1abcddcbaabcd1").substring(1, 13); // without ones

		assertEquals("Expected no change", "abcddcbaabcd",
				cs2.replaceAll(CString.fromString("1"), CString.fromString("2")).toString());
		assertEquals("Expected empty", "", cs2.replaceAll(cs2, CString.fromString("")).toString());
		assertEquals("Expected no change", "aeddcbaaed",
				cs2.replaceAll(CString.fromString("bc"), CString.fromString("e")).toString());

		CString cs3 = CString.fromString("aaaa");

		assertEquals("Invalid replaceAll", "bbbb",
				cs3.replaceAll(CString.fromString("a"), CString.fromString("b")).toString());
		assertEquals("Invalid replaceAll", "1a1a1a1a1",
				cs3.replaceAll(CString.fromString(""), CString.fromString("1")).toString());
		assertEquals("Invalid replaceAll", "",
				cs3.replaceAll(CString.fromString("aaaa"), CString.fromString("")).toString());
		assertEquals("Invalid replaceAll", "a",
				cs3.replaceAll(CString.fromString("aaaa"), CString.fromString("a")).toString());

	}

}
