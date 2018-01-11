package com.promelle.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.promelle.exception.InitializationException;
import com.promelle.exception.SecurityException;

public abstract class RSAUtils {
	private static KeyFactory keyFactory;
	private static Cipher cipher;
	private static final String RSA = "RSA";

	static {
		Security.addProvider(Security.getProvider("SunRsaSign"));
		try {
			keyFactory = KeyFactory.getInstance(RSA);
			cipher = Cipher.getInstance(RSA);
		} catch (NoSuchAlgorithmException e) {
			throw new InitializationException(e);
		} catch (NoSuchPaddingException e) {
			throw new InitializationException(e);
		}
	}

	/**
	 * This function is responsibly for generating a key pair(public & private)
	 * at the specified directory path
	 * 
	 * @param dirPath
	 * @throws NoSuchAlgorithmEncryptionException
	 * @throws IOEncryptionException
	 * @throws SecurityException
	 */
	public static void generateKeyPair(String dirPath) throws SecurityException {
		OutputStream publicKeyStream = null;
		OutputStream privateKeyStream = null;
		try {
			publicKeyStream = new FileOutputStream(dirPath + "/public.key");
			privateKeyStream = new FileOutputStream(dirPath + "/private.key");
			new File(dirPath).mkdirs();
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
			kpg.initialize(512);
			KeyPair keyPair = kpg.generateKeyPair();
			// Store Public Key.
			publicKeyStream.write(new X509EncodedKeySpec(keyPair.getPublic()
					.getEncoded()).getEncoded());
			// Store Private Key.
			privateKeyStream.write(new PKCS8EncodedKeySpec(keyPair.getPrivate()
					.getEncoded()).getEncoded());
		} catch (NoSuchAlgorithmException e) {
			throw new SecurityException(e);
		} catch (IOException e) {
			throw new SecurityException(e);
		} finally {
			if (privateKeyStream != null) {
				try {
					privateKeyStream.close();
				} catch (IOException e) {
					throw new SecurityException(e);
				}
			}
			if (publicKeyStream != null) {
				try {
					publicKeyStream.close();
				} catch (IOException e) {
					throw new SecurityException(e);
				}
			}
		}
	}

	/**
	 * This function is responsible for retrieving a public key from the
	 * specified public key file path
	 * 
	 * @param keyFilePath
	 * @return
	 * @throws IOEncryptionException
	 * @throws InvalidKeySpecEncryptionException
	 * @throws SecurityException
	 */
	public static PublicKey getPublicKey(String keyFilePath)
			throws SecurityException {
		InputStream fis = null;
		try {
			fis = new FileInputStream(keyFilePath);
			byte[] encodedKey = new byte[fis.available()];
			fis.read(encodedKey);
			return keyFactory
					.generatePublic(new X509EncodedKeySpec(encodedKey));
		} catch (IOException e) {
			throw new SecurityException(e);
		} catch (InvalidKeySpecException e) {
			throw new SecurityException(e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					throw new SecurityException(e);
				}
			}
		}
	}

	/**
	 * This function is responsible for retrieving a private key from the
	 * specified private key file path
	 * 
	 * @param keyFilePath
	 * @return
	 * @throws SecurityException
	 */
	public static PrivateKey getPrivateKey(String keyFilePath)
			throws SecurityException {
		InputStream fis = null;
		try {
			fis = new FileInputStream(keyFilePath);
			byte[] encodedKey = new byte[fis.available()];
			fis.read(encodedKey);
			return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(
					encodedKey));
		} catch (IOException e) {
			throw new SecurityException(e);
		} catch (InvalidKeySpecException e) {
			throw new SecurityException(e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					throw new SecurityException(e);
				}
			}
		}
	}

	/**
	 * This function is responsible for retrieving a key pair from the specified
	 * public key & private key file path
	 * 
	 * @param keyFilePath
	 * @return
	 * @throws SecurityException
	 */
	public static KeyPair getKeyPair(String privateFile, String publicFile)
			throws SecurityException {
		return new KeyPair(getPublicKey(publicFile), getPrivateKey(privateFile));
	}

	/**
	 * This function is responsible for encrypting the input
	 * 
	 * @param input
	 * @param pubKey
	 * @return
	 * @throws SecurityException
	 */
	public static byte[] encrypt(byte[] input, Key pubKey)
			throws SecurityException {
		try {
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			return cipher.doFinal(input);
		} catch (InvalidKeyException e) {
			throw new SecurityException(e);
		} catch (IllegalBlockSizeException e) {
			throw new SecurityException(e);
		} catch (BadPaddingException e) {
			throw new SecurityException(e);
		}
	}

	/**
	 * This function is responsible for decrypting the input
	 * 
	 * @param input
	 * @param privKey
	 * @return
	 * @throws SecurityException
	 */
	public static byte[] decrypt(byte[] input, Key privKey)
			throws SecurityException {
		try {
			cipher.init(Cipher.DECRYPT_MODE, privKey);
			return cipher.doFinal(input);
		} catch (InvalidKeyException e) {
			throw new SecurityException(e);
		} catch (IllegalBlockSizeException e) {
			throw new SecurityException(e);
		} catch (BadPaddingException e) {
			throw new SecurityException(e);
		}
	}

	/**
	 * This function is responsible for encrypting the input
	 * 
	 * @param input
	 * @param publicKeyFilePath
	 * @return
	 * @throws SecurityException
	 */
	public static byte[] encrypt(byte[] input, String publicFile)
			throws SecurityException {
		return encrypt(input, getPublicKey(publicFile));
	}

	/**
	 * This function is responsible for decrypting the input
	 * 
	 * @param input
	 * @param privateKeyFilePath
	 * @return
	 * @throws SecurityException
	 */
	public static byte[] decrypt(byte[] input, String privateFile)
			throws SecurityException {
		return decrypt(input, getPrivateKey(privateFile));
	}

	/**
	 * This function is responsible for encrypting the input
	 * 
	 * @param input
	 * @param keyPair
	 * @return
	 * @throws SecurityException
	 */
	public static byte[] encrypt(byte[] input, KeyPair keyPair)
			throws SecurityException {
		return encrypt(input, keyPair.getPublic());
	}

	/**
	 * This function is responsible for decrypting the input
	 * 
	 * @param input
	 * @param keyPair
	 * @return
	 * @throws SecurityException
	 */
	public static byte[] decrypt(byte[] input, KeyPair keyPair)
			throws SecurityException {
		return decrypt(input, keyPair.getPrivate());
	}
}
