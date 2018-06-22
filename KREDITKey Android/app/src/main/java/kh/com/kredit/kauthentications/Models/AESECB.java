package kh.com.kredit.kauthentications.Models;

public class AESECB {
    private static final String KEY = "AD48WC42SDCE3212";
	public AESECB(){
    }
    public String stringEncrytion(String data, String key){
        AESUtil aes = new AESUtil(key, key);
        String cipher = aes.encrypt(data,key);
        return cipher;
    }
    public String stringEncrytion(String data){
        AESUtil aes = new AESUtil(KEY, KEY);
        String cipher = aes.encrypt(data,KEY);
        return cipher;
    }
    public String stringDecrytion(String cipher, String key){
        AESUtil aes = new AESUtil(key, key);
        String plaintext = aes.decrypt(cipher,key);
        return plaintext;
    }
    public String stringDecrytion(String cipher){
        AESUtil aes = new AESUtil(KEY, KEY);
        String plaintext = aes.decrypt(cipher,KEY);
        return plaintext;
    }

}