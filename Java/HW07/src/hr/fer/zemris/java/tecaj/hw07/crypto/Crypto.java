package hr.fer.zemris.java.tecaj.hw07.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * This program offers the possibility to encrypt/decrypt given file using the
 * AES crypto-algorithm and the 128-bit encryption key or calculate and check
 * the SHA-256 file digest.
 * <p>
 * Possible program arguments are as follows:
 * <ul>
 * <li>checksha filePath
 * <li>encrypt inputFilePath outputFilePath
 * <li>decrypt inputFilePath outputFilePath
 * </ul>
 * 
 * @author Dan
 *
 */
public class Crypto {

	/**
	 * Program entry point.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Arguments expected.");
			return;
		}

		if (args[0].equals("checksha")) {
			if (args.length != 2) {
				System.err.println("Expected file name.");
				return;
			}
			calculateSHA256(args[1]);
		} else {
			try {
				encryptDecryptAES(args);
			} catch (IOException e) {
				System.err.println("An I/O exception occured.");
				return;
			}
		}

	}

	/**
	 * Calculates SHA-256 digest of file given by its relative or absolute path
	 * and reads from standard input expected digest for comparison.
	 * 
	 * @param path
	 *            path of the file
	 */
	private static void calculateSHA256(String path) {
		String fileName = Paths.get(path).getFileName().toString();

		try (InputStream stream = new BufferedInputStream(new FileInputStream(path))) {
			MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

			byte[] bytes = new byte[4096];
			int n;

			while ((n = stream.read(bytes)) > 0) {
				sha256.update(bytes, 0, n);
			}

			String digest = DatatypeConverter.printHexBinary(sha256.digest()).toLowerCase();

			System.out.printf("Please provide expected sha-256 digest for %s%n> ", fileName);
			String expectedDigest = (new BufferedReader(new InputStreamReader(System.in))).readLine();

			if (digest.equals(expectedDigest)) {
				System.out.printf("Digesting completed. Digest of %s matches expected digest.%n", fileName);
			} else {
				System.out.printf(
						"Digesting completed. Digest of %s does not match the expected digest. Digest was: %s",
						fileName, digest);
			}
		} catch (IOException e) {
			System.err.println("An I/O exception occured. Check if file exists and that you have access to it.");
			System.exit(-1);
		} catch (NoSuchAlgorithmException e) {
			System.err.println("SHA-256 algorithm is not supported.");
			System.exit(-1);
		}
	}

	/**
	 * Performs encryption/decryption based on given arguments.
	 * 
	 * @param args
	 *            {@code Array} where first argument defines
	 *            encryption/decryption, second input file path and third output
	 *            file path.
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	private static void encryptDecryptAES(String[] args) throws IOException {
		if (args.length != 3) {
			System.err.println("Invalid number of arguments.");
			System.exit(-1);
		}

		int mode = 0;
		String result = "";

		switch (args[0]) {
		case "encrypt":
			mode = Cipher.ENCRYPT_MODE;
			result = "Encryption";
			break;
		case "decrypt":
			mode = Cipher.DECRYPT_MODE;
			result = "Decryption";
			break;
		default:
			System.err.println("Unsupported operation.");
			System.exit(-1);
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.printf("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):%n> ");
		String keyText = reader.readLine();
		checkHexText(keyText);

		System.out.printf("Please provide initialization vector as hex-encoded text (32 hex-digits):%n> ");
		String ivText = reader.readLine();
		checkHexText(ivText);

		SecretKeySpec keySpec = new SecretKeySpec(hexToByte(keyText), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(hexToByte(ivText));

		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(mode, keySpec, paramSpec);
		} catch (GeneralSecurityException e) {
			System.err.println("Error during cipher initialization.");
			System.exit(-1);
		}

		executeCipher(cipher, args[1], args[2]);

		System.out.printf("%s completed. Generated file %s based on file %s.%n", result, args[1], args[2]);
	}

	/**
	 * Executes already initialized {@code Cipher} on given files. Only small
	 * portion of file is read into memory at any given time.
	 * 
	 * @param cipher
	 *            initialized {@code Cipher}
	 * @param inputFile
	 *            input file path
	 * @param outputFile
	 *            output file path
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	private static void executeCipher(Cipher cipher, String inputFile, String outputFile) throws IOException {
		InputStream input = new BufferedInputStream(new FileInputStream(inputFile));
		OutputStream output = new BufferedOutputStream(new FileOutputStream(outputFile));

		byte[] bytes = new byte[4096];
		int n;

		while ((n = input.read(bytes)) > 0) {
			byte[] processed = cipher.update(bytes, 0, n);
			output.write(processed);
		}

		try {
			output.write(cipher.doFinal());
		} catch (GeneralSecurityException e) {
			System.err.println("Error during finalizing.");
			System.exit(-1);
		}

		input.close();
		output.flush();
		output.close();
	}

	/**
	 * Checks if given hexadecimal text is valid; contains only hexadecimal
	 * characters and is of length 32 digits. If given text is invalid program
	 * is aborted.
	 * 
	 * @param hex
	 *            {@code String} to check
	 *
	 */
	private static void checkHexText(String hex) {
		if (!hex.matches("[0-9a-fA-F]*") || hex.length() != 32) {
			System.err.println("Invalid hexadecimal text.");
			System.exit(-1);
		}
	}

	/**
	 * Converts given hexadecimal text to {@code byte array}.
	 * 
	 * @param hex
	 *            hexadecimal {@code String}
	 * @return {@code byte array}
	 * @throws IndexOutOfBoundsException
	 *             if odd number of hexadecimal characters is given
	 */
	protected static byte[] hexToByte(String hex) {
		int len = hex.length();
		byte[] data = new byte[len / 2];

		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
		}

		return data;
	}

}
