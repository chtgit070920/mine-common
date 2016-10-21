package com.mine.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RsaUtil {

	public static  RSAPublicKey  loadPublicKey(InputStream is) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		BufferedReader br= new BufferedReader(new InputStreamReader(is));
		String readLine= null;
		StringBuilder sb= new StringBuilder();
		while((readLine= br.readLine())!=null){
			if(readLine.charAt(0)=='-'){
				continue;
			}else{
				sb.append(readLine);
				sb.append('\r');
			}
		}
		return loadPublicKey(sb.toString());
	}
	public static RSAPublicKey loadPublicKey(String publicKeyStr) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		BASE64Decoder base64Decoder= new BASE64Decoder();
		byte[] buffer= base64Decoder.decodeBuffer(publicKeyStr);
		KeyFactory keyFactory= KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);
		return (RSAPublicKey) keyFactory.generatePublic(keySpec);
	}
	public static RSAPrivateKey loadPrivateKey(InputStream is) throws Exception{
		BufferedReader br= new BufferedReader(new InputStreamReader(is));
		String readLine= null;
		StringBuilder sb= new StringBuilder();
		while((readLine= br.readLine())!=null){
			if(readLine.charAt(0)=='-'){
				continue;
			}else{
				sb.append(readLine);
				sb.append('\r');
			}
		}
		return loadPrivateKey(sb.toString());
	}
	public static RSAPrivateKey loadPrivateKey(String privateKeyStr) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		BASE64Decoder base64Decoder= new BASE64Decoder();
		byte[] buffer= base64Decoder.decodeBuffer(privateKeyStr);
		PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);
		KeyFactory keyFactory= KeyFactory.getInstance("RSA");
		return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
	}
	
	public static void writeToFile(String path,byte[] content) throws IOException{
		File f=new File(path);
		File parent=f.getParentFile();
		if(!parent.exists()){
			parent.mkdirs();
		}
		
		BASE64Encoder encoder=new BASE64Encoder(); 
		String encoded=encoder.encode(content);  
		FileWriter fw=new FileWriter(path);  
	    fw.write(encoded);  
	    fw.close();   
	}
	
	public static byte[] getFromFile(String path) throws IOException{
		FileInputStream fis=new FileInputStream(path);
		return getFromFile(fis);
	}
	public static byte[] getFromFile(InputStream is) throws IOException {
		BufferedReader br= new BufferedReader(new InputStreamReader(is));
		String readLine= null;
		StringBuilder sb= new StringBuilder();
		while((readLine= br.readLine())!=null){
			if(readLine.charAt(0)=='-'){
				continue;
			}else{
				sb.append(readLine);
				sb.append('\r');
			}
		}
		BASE64Decoder base64Decoder= new BASE64Decoder();
		byte[] buffer= base64Decoder.decodeBuffer(sb.toString());
		return buffer;
	}
	
	/**
	 * 私钥 加密
	 */
	public static byte[] encrypt(RSAPrivateKey privatecKey, byte[] plainTextData) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
//		try {
			Cipher cipher= Cipher.getInstance("RSA/ECB/PKCS1Padding");//RSA/ECB/PKCS1Padding     "RSA", new BouncyCastleProvider()
			cipher.init(Cipher.ENCRYPT_MODE, privatecKey);
			byte[] output= cipher.doFinal(plainTextData);
			return output;
//		} catch (NoSuchAlgorithmException e) {
//			throw new Exception("无此加密算法");
//		} catch (NoSuchPaddingException e) {
//			e.printStackTrace();
//			return null;
//		}catch (InvalidKeyException e) {
//			throw new Exception("加密公钥非法,请检查");
//		} catch (IllegalBlockSizeException e) {
//			throw new Exception("明文长度非法");
//		} catch (BadPaddingException e) {
//			throw new Exception("明文数据已损坏");
//		}
	}

	/**
	 * 公钥解密
	 */
	public static byte[] decrypt(RSAPublicKey publicKey, byte[] cipherData) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
//		try {
			Cipher cipher= Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] output= cipher.doFinal(cipherData);
			return output;
//		} catch (NoSuchAlgorithmException e) {
//			throw new Exception("无此解密算法");
//		} catch (NoSuchPaddingException e) {
//			e.printStackTrace();
//			return null;
//		}catch (InvalidKeyException e) {
//			throw new Exception("解密私钥非法,请检查");
//		} catch (IllegalBlockSizeException e) {
//			throw new Exception("密文长度非法");
//		} catch (BadPaddingException e) {
//			throw new Exception("密文数据已损坏");
//		}		
	}
}
