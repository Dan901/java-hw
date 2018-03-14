package hr.fer.zemris.java.cstr;

import java.util.Arrays;

/**
 * Different implementation of {@link String} class. This class offers all the
 * basic functionality that the mentioned {@code String} class offers, only
 * difference being the complexity of {@link #substring(int, int)} and similar
 * methods, which is in this case <b>O(1)</b>.
 * <p>
 * {@code CString} objects are immutable, and every method that modifies some
 * part of the {@code CString} returns a new object.
 * <p>
 * Unless otherwise noted, passing a <tt>null</tt> argument to a constructor or
 * method in this class will cause a {@link IllegalArgumentException} to be
 * thrown.
 * 
 * @author Dan
 *
 */
public class CString {

	/**
	 * Array of characters, of which not all need to be part of this CString.
	 */
	private char[] data;
	/**
	 * Index of the first character, in data array, that is part of this
	 * CString.
	 */
	private int offset;
	/**
	 * Length of this CString.
	 */
	private int length;

	/**
	 * Allocates a new {@code CString} that contains characters from a subarray
	 * of the character array argument. The contents of the subarray are copied;
	 * subsequent modification of the character array does not affect the newly
	 * created {@code CString}.
	 * 
	 * @param data
	 *            array that is the source of characters
	 * @param offset
	 *            index of the first character, in data array, that is part of
	 *            this {@code CString}
	 * @param length
	 *            length of this {@code CString}
	 * @throws IndexOutOfBoundsException
	 *             if the {@code offset} and {@code length} arguments index
	 *             characters outside the bounds of the {@code data} array
	 */
	public CString(char[] data, int offset, int length) {
		if (data == null) {
			throw new IllegalArgumentException("CString char array cannot be null.");
		}

		if (offset < 0 || (offset != 0 && offset >= data.length)) {
			throw new IndexOutOfBoundsException("CString index out of range: " + offset);
		}

		if (length < 0 || length > data.length - offset) {
			throw new IndexOutOfBoundsException("CString index out of range: " + length);
		}

		this.data = Arrays.copyOfRange(data, offset, offset + length);
		this.offset = 0;
		this.length = length;
	}

	/**
	 * Allocates a new {@code CString} that contains characters from the
	 * character array argument. The contents of the array are copied;
	 * subsequent modification of the character array does not affect the newly
	 * created {@code CString}.
	 * 
	 * @param data
	 *            array that is the source of characters
	 */
	public CString(char[] data) {
		this(data, 0, data.length);
	}

	/**
	 * Initializes a newly created {@code CString} object so that it represents
	 * the same sequence of characters as the argument. The newly created
	 * {@code CString} is a copy of the argument string.
	 * 
	 * @param original
	 *            {@code CString} to copy
	 */
	public CString(CString original) {
		if (original == null) {
			throw new IllegalArgumentException("CString cannot be null.");
		}

		this.offset = 0;
		this.length = original.length;

		if (original.data.length > length) {
			this.data = Arrays.copyOfRange(original.data, original.offset, original.offset + length);
		} else {
			this.data = original.data;
		}
	}

	/**
	 * Private constructor used by {@link #substring(int, int)}. Newly created
	 * {@code CString} shares the data array with calling {@code CString}.
	 * 
	 * @param offset
	 *            offset
	 * @param length
	 *            length
	 * @param data
	 *            data array
	 */
	private CString(int offset, int length, char[] data) {
		this.data = data;
		this.offset = offset;
		this.length = length;
	}

	/**
	 * Returns a {@code CString} object so that it represents the same sequence
	 * of characters as the argument.
	 * 
	 * @param s
	 *            {@code String} to copy
	 * @return {@code CString} that is a copy of the argument string
	 */
	public static CString fromString(String s) {
		if (s == null) {
			throw new IllegalArgumentException("String cannot be null.");
		}

		return new CString(s.toCharArray());
	}

	/**
	 * @return the length of this {@code CString}
	 */
	public int length() {
		return length;
	}

	/**
	 * Returns the character at given index in this {@code CString}.
	 * 
	 * @param index
	 *            index of the character
	 * @return the character at given index
	 * @throws IndexOutOfBoundsException
	 *             if the {@code index} argument is outside the bounds of this
	 *             {@code CString}
	 */
	public char charAt(int index) {
		if (index < 0 || index >= length) {
			throw new IndexOutOfBoundsException("CString index out of range: " + index);
		}

		return data[offset + index];
	}

	/**
	 * Converts this {@code CString} to a new character array.
	 *
	 * @return a newly allocated character array whose length is the length of
	 *         this {@code CString} and whose contents are initialized to
	 *         contain the character sequence represented by this
	 *         {@code CString}
	 */
	public char[] toCharArray() {
		return Arrays.copyOfRange(data, offset, offset + length);
	}

	/**
	 * Converts this {@code CString} to a new {@code String}.
	 * 
	 * @return {@code String} containing same characters as this {@code CString}
	 */
	@Override
	public String toString() {
		return new String(data, offset, length);
	}

	/**
	 * Returns the index within this {@code CString} of the first occurrence of
	 * the specified character. If no such character occurs in this
	 * {@code CString}, then -1 is returned.
	 * 
	 * @param c
	 *            a character
	 * @return the index of the first occurrence of the character in the
	 *         character sequence represented by this object, or -1 if the
	 *         character does not occur
	 */
	public int indexOf(char c) {
		for (int i = offset; i < offset + length; i++) {
			if (data[i] == c) {
				return i - offset;
			}
		}

		return -1;
	}

	/**
	 * Tests if this {@code CString} starts with the specified prefix.
	 * 
	 * @param s
	 *            the prefix
	 * @return {@code true} if the character sequence represented by the
	 *         argument is a prefix of the character sequence represented by
	 *         this {@code CString}; {@code false} otherwise. Note also that
	 *         {@code true} will be returned if the argument is empty.
	 */
	public boolean startsWith(CString s) {
		if (s == null) {
			throw new IllegalArgumentException("CString cannot be null.");
		}

		if (s.length > length) {
			return false;
		}

		for (int i = 0; i < s.length; i++) {
			if (data[offset + i] != s.data[s.offset + i]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Tests if this {@code CString} ends with the specified suffix.
	 * 
	 * @param s
	 *            the suffix
	 * @return {@code true} if the character sequence represented by the
	 *         argument is a suffix of the character sequence represented by
	 *         this {@code CString}; {@code false} otherwise. Note also that
	 *         {@code true} will be returned if the argument is empty.
	 */
	public boolean endsWith(CString s) {
		if (s == null) {
			throw new IllegalArgumentException("CString cannot be null.");
		}

		if (s.length > length) {
			return false;
		}

		for (int i = 0; i < s.length; i++) {
			if (data[offset + length - s.length + i] != s.data[s.offset + i]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns the index within this {@code CString} of the first occurrence of
	 * the specified {@code CString} starting from given index. If no such
	 * {@code CString} occurs in this {@code CString}, then -1 is returned.
	 * 
	 * @param s
	 *            the sequence to search for
	 * @param startIndex
	 *            the index from where the search starts
	 * @return the index within this {@code CString} of the first occurrence of
	 *         the specified {@code CString} starting from given index. If no
	 *         such {@code CString} occurs in this {@code CString}, then -1 is
	 *         returned.
	 */
	private int indexOf(CString s, int startIndex) {
		if (s.length > length) {
			return -1;
		}

		if (s.length == 0) {
			return startIndex;
		}

		for (int i = startIndex; i < length; i++) {
			for (int j = 0; j < s.length; j++) {
				if (data[offset + i + j] != s.data[s.offset + j]) {
					break;
				}

				if (j == s.length - 1) {
					return i;
				}
			}
		}

		return -1;
	}

	/**
	 * Tests if this {@code CString} contains the specified {@code CString}.
	 * 
	 * @param s
	 *            the sequence to search for
	 * @return {@code true} if this {@code CString} contains given argument,
	 *         {@code false} otherwise. Note also that {@code true} will be
	 *         returned if the argument is empty.
	 */
	public boolean contains(CString s) {
		if (s == null) {
			throw new IllegalArgumentException("CString cannot be null.");
		}

		if (indexOf(s, 0) != -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns a new {@code CString} that is a substring of this {@code CString}
	 * . The substring begins at the specified {@code startIndex} and extends to
	 * the character at index {@code endIndex - 1}.
	 * 
	 * @param startIndex
	 *            the beginning index, inclusive.
	 * @param endIndex
	 *            the ending index, exclusive.
	 * @return the specified substring
	 * @throws IndexOutOfBoundsException
	 *             if indexes are out of bounds of this {@code CString}
	 */
	public CString substring(int startIndex, int endIndex) {
		if (startIndex < 0 || startIndex > endIndex || startIndex >= length) {
			throw new IndexOutOfBoundsException("CString index out of range: " + startIndex);
		}

		if (endIndex < 0 || endIndex > length) {
			throw new IndexOutOfBoundsException("CString index out of range: " + endIndex);
		}

		return new CString(offset + startIndex, endIndex - startIndex, data);
	}

	/**
	 * Returns a new {@code CString} which represents starting part of this
	 * {@code CString} and is of given length.
	 * 
	 * @param n
	 *            length
	 * @return the specified substring
	 * @throws IndexOutOfBoundsException
	 *             if length is out of bounds of this {@code CString}
	 */
	public CString left(int n) {
		if (n < 0) {
			throw new IndexOutOfBoundsException("CString index out of range: " + n);
		}

		return substring(0, n);
	}

	/**
	 * Returns a new {@code CString} which represents ending part of this
	 * {@code CString} and is of given length.
	 * 
	 * @param n
	 *            length
	 * @return the specified substring
	 * @throws IndexOutOfBoundsException
	 *             if length is out of bounds of this {@code CString}
	 */
	public CString right(int n) {
		return substring(length - n, length);
	}

	/**
	 * Concatenates the specified {@code CString} to the end of this
	 * {@code CString}. If the length of the argument {@code CString} is 0, then
	 * this {@code CString} object is returned.
	 * 
	 * @param s
	 *            the {@code CString} that is concatenated to the end of this
	 *            {@code CString}
	 * @return a {@code CString} that represents the concatenation of this
	 *         object's characters followed by the {@code CString} argument's
	 *         characters
	 */
	public CString add(CString s) {
		if (s == null) {
			throw new IllegalArgumentException("CString cannot be null.");
		}

		if (s.length == 0) {
			return this;
		}

		char buffer[] = new char[length + s.length];
		System.arraycopy(data, offset, buffer, 0, length);
		System.arraycopy(s.data, s.offset, buffer, length, s.length);

		return new CString(buffer);
	}

	/**
	 * Returns a new {@code CString} resulting from replacing all occurrences of
	 * {@code oldChar} in this {@code CString} with {@code newChar}. If the
	 * character {@code oldChar} does not occur in the character sequence
	 * represented by this {@code CString} object, then a reference to this
	 * {@code CString} object is returned.
	 * 
	 * @param oldChar
	 *            the old character
	 * @param newChar
	 *            the new character
	 * @return a {@code CString} derived from this {@code CString} by replacing
	 *         every occurrence of {@code oldChar} with {@code newChar}
	 */
	public CString replaceAll(char oldChar, char newChar) {
		if (oldChar == newChar || indexOf(oldChar) == -1) {
			return this;
		}

		CString newCString = new CString(data, offset, length);

		for (int i = 0; i < length; i++) {
			if (newCString.data[i] == oldChar) {
				newCString.data[i] = newChar;
			}
		}

		return newCString;
	}

	/**
	 * Replaces each substring of this {@code CString} that matches the literal
	 * {@code oldStr} sequence with the specified literal {@code newStr}
	 * sequence. The replacement proceeds from the beginning of the
	 * {@code CString} to the end.
	 * 
	 * @param oldStr
	 *            the substring to be replaced
	 * @param newStr
	 *            the replacement substring
	 * @return the resulting {@code CString}
	 */
	public CString replaceAll(CString oldStr, CString newStr) {
		if (oldStr == null || newStr == null) {
			throw new IllegalArgumentException("CString cannot be null.");
		}

		int index = indexOf(oldStr, 0);

		if (index == -1) {
			return this;
		}

		CString newCString = new CString(data, offset, index);

		while (index < length) {
			newCString = newCString.add(newStr);
			int startIndex = index + oldStr.length;

			if (startIndex >= length) {
				break;
			}

			if (oldStr.length == 0) {
				index = startIndex + 1;
			} else {
				index = indexOf(oldStr, startIndex);
			}

			if (index == -1) {
				index = length;
			}

			newCString = newCString.add(substring(startIndex, index));
		}

		if (oldStr.length == 0) {
			newCString = newCString.add(newStr);
		}

		return newCString;
	}
}
