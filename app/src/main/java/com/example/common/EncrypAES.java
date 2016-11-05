package com.example.common;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

/**
 * AES�㷨������
 * @author Administrator
 *
 */
public class EncrypAES {
	private static final String TAG="EncryAES";

	/**
	 * Cipher������ɼ��ܻ���ܹ���
	 */
	private Cipher c;
	
	/**
	 * ���ֽ����鸺�𱣴���ܵĽ��
	 */
	private byte[] cipherByte;
	
	/**
	 * ����������Կ
	 */
	private static final String SECRETKET="AESDemo";
	private SecretKeySpec deskey;
	
	public EncrypAES(){
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		
		try {
			deskey = new SecretKeySpec(getRawKey(SECRETKET.getBytes()),"AES");
			
			//����Cipher����,ָ����֧�ֵ�DES�㷨
			c = Cipher.getInstance("AES");
		} catch (Exception e) {
			Log.e(TAG, "EnrypAES construct failed.",e);
		}
	}
	
	/**
	 * ���ַ�������
	 * 
	 * @param str
	 * @return
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	private byte[] Encrytor(String str) throws InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		
		// ������Կ����Cipher������г�ʼ����ENCRYPT_MODE��ʾ����ģʽ
		c.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] src = str.getBytes();
		
		cipherByte = c.doFinal(src);		// ���ܣ���������cipherByte
		return cipherByte;
	}

	/**
	 * ���ַ�������
	 * 
	 * @param buff
	 * @return
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	private byte[] Decryptor(byte[] buff) throws InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		
		// ������Կ����Cipher������г�ʼ����DECRYPT_MODE��ʾ����ģʽ
		c.init(Cipher.DECRYPT_MODE, deskey);
		cipherByte = c.doFinal(buff);
		
		return cipherByte;
	}
	
	/**
	 * ���ַ������м���
	 * @param string Ҫ���ܵ��ַ���
	 * @return ���ܺ���ַ���
	 */
	public String EncryptorString(String string){
		String result =null;
		byte[] encontent;
		try {
			encontent = Encrytor(string);
			result=toHex(encontent);
		} catch (InvalidKeyException e) {
			Log.e(TAG, "EncryptorString",e);
		} catch (IllegalBlockSizeException e) {
			Log.e(TAG, "EncryptorString",e);
		} catch (BadPaddingException e) {
			Log.e(TAG, "EncryptorString",e);
		}
		
		return result;
	}
	
	/**
	 * ���ַ������н���
	 * @param string Ҫ���ܵ��ַ���
	 * @return ���ܺ���ַ���
	 */
	public String DecryptorString(String string){
		byte[] cryptcontent=toByte(string);
		byte[] decontent;
		String result=null;
		
		try {
			decontent=Decryptor(cryptcontent);
			result=new String(decontent);
			
		} catch (InvalidKeyException e) {
			Log.e(TAG,"DecryptorString failed.",e);
		} catch (IllegalBlockSizeException e) {
			Log.e(TAG,"DecryptorString failed.",e);
		} catch (BadPaddingException e) {
			Log.e(TAG,"DecryptorString failed.",e);
		}
		
		return result;
	}

	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		
		/**
		 * ��һ��ܹؼ���
		 * <br>�����еĴ�������һ��SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		 * <br>��getInstance����������һ��String������
		 * ��������ǲ����ٵģ���Ϊ���������������Ļ������ɵ���Կ������ģ������ܺͽ��ܱ�������ͬ������Կ��
		 * ���Ի���ֲ��ܽ��ܵ����⣨�����׳�BadPaddingException�쳣�����������ַ����У��Ա�����һ�������ļ��ܽ����
		 * �ᷢ��ÿһ�μ��ܵĽ�����ǲ�һ���ġ�
		 * <br>������������õ�����Կ���ܣ�ͬ�����ַ����κ�ʱ��õ��ļ��ܽ������һ���ġ�
		 */
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
		sr.setSeed(seed);
		kgen.init(128, sr); // 192 and 256 bits may not be available
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}
	
	public static String toHex(String txt) {  
        return toHex(txt.getBytes());  
    }
	
    public static String fromHex(String hex) {  
        return new String(toByte(hex));  
    }  
      
    public static byte[] toByte(String hexString) {  
        int len = hexString.length()/2;  
        byte[] result = new byte[len];  
        for (int i = 0; i < len; i++)  
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();  
        return result;  
    }  
 
    public static String toHex(byte[] buf) {  
        if (buf == null)  
            return "";  
        StringBuffer result = new StringBuffer(2*buf.length);  
        for (int i = 0; i < buf.length; i++) {  
            appendHex(result, buf[i]);  
        }  
        return result.toString();  
    }
    
    private final static String HEX = "0123456789ABCDEF";
    private static void appendHex(StringBuffer sb, byte b) {  
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));  
    }
}
