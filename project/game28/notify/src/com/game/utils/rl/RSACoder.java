package com.game.utils.rl;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RSACoder {
    private static final String Algorithm = "RSA";
    public static final String Signature_Algorithm_MD5 = "MD5withRSA";
    public static final String Signature_Algorithm_SHA1 = "SHA1WithRSA";

    
    public static String signMS(String data, String privateKeyFile) throws Exception { 
    	PrivateKey privateKey = getPrivateKeyPem(privateKeyFile);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());    
        KeyFactory keyFactory = KeyFactory.getInstance(Algorithm);   
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);  
  
        Signature signature = Signature.getInstance(Signature_Algorithm_SHA1);  
        signature.initSign(priKey);  
        signature.update(data.getBytes("UTF-8"));   
        return Base64.encode(signature.sign());  
    }
	
	private static PrivateKey getPrivateKeyPem(String fileName) throws Exception {
    	String key = readFile(fileName, "UTF-8");
    	byte[] keyBytes = buildPKCS8Key(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(Algorithm);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;       
    }
    
    private static byte[] buildPKCS8Key(String privateKey) throws IOException {
        if (privateKey.contains("-----BEGIN PRIVATE KEY-----")) {
            return Base64.decode(privateKey.replaceAll("-----\\w+ PRIVATE KEY-----", ""));
        } else if (privateKey.contains("-----BEGIN RSA PRIVATE KEY-----")) {
            final byte[] innerKey = Base64.decode(privateKey.replaceAll("-----\\w+ RSA PRIVATE KEY-----", ""));
            final byte[] result = new byte[innerKey.length + 26];
            System.arraycopy(Base64.decode("MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKY="), 0, result, 0, 26);
            System.arraycopy(BigInteger.valueOf(result.length - 4).toByteArray(), 0, result, 2, 2);
            System.arraycopy(BigInteger.valueOf(innerKey.length).toByteArray(), 0, result, 24, 2);
            System.arraycopy(innerKey, 0, result, 26, innerKey.length);
            return result;
        } else {
            return Base64.decode(privateKey);
        }
    }
    
    private static String readFile(String filePath, String charSet) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        try {
            FileChannel fileChannel = fileInputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) fileChannel.size());
            fileChannel.read(byteBuffer);
            byteBuffer.flip();
            return new String(byteBuffer.array(), charSet);
        } finally {
            fileInputStream.close();
        }

    }
    
    public static boolean verifyMS(String data, String signData, String publicKeyFile)  
            throws Exception {
    	PublicKey publicKey = getPubliceKeyPem(publicKeyFile);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey.getEncoded());    
		KeyFactory keyFactory = KeyFactory.getInstance(Algorithm);
        PublicKey pubKey = keyFactory.generatePublic(keySpec);    
        Signature signature = Signature.getInstance(Signature_Algorithm_SHA1);  
        signature.initVerify(pubKey);  
        signature.update(data.getBytes("UTF-8"));
        return signature.verify(Base64.decode(signData));  
    }
    
    private static PublicKey getPubliceKeyPem(String fileName) throws Exception {
    	String key = readFile(fileName, "UTF-8");
        key = key.replaceAll("\\-{5}[\\w\\s]+\\-{5}[\\r\\n|\\n]", "");
        KeyFactory keyFactory = KeyFactory.getInstance(Algorithm);
        byte[] encodedKey = Base64.decode(key);
        PublicKey pubKey = keyFactory
                .generatePublic(new X509EncodedKeySpec(encodedKey));
        return pubKey;
    }
}
