package hr.fer.zemris.java.tecaj.hw07.crypto;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class CryptoTests {

	@Test
	public void testHexToByte1(){
		String hex = "1234";
		byte[] expected = new byte[]{(byte) 0x12, (byte) 0x34};
		
		assertTrue(Arrays.equals(expected, Crypto.hexToByte(hex)));
	}
	
	@Test
	public void testHexToByte2(){
		String hex = "1A2B3f42";
		byte[] expected = new byte[]{(byte) 0x1A, (byte) 0x2B, (byte) 0x3F, (byte) 0x42};
		
		assertTrue(Arrays.equals(expected, Crypto.hexToByte(hex)));
	}
	
	@Test
	public void testHexToByteEmpty(){
		String hex = "";
		byte[] expected = new byte[0];
		
		assertTrue(Arrays.equals(expected, Crypto.hexToByte(hex)));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testHexToByteInvalid(){
		String hex = "0";
		Crypto.hexToByte(hex);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testHexToByteInvalid2(){
		String hex = "A23B5";
		Crypto.hexToByte(hex);
	}
	
}
