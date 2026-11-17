package week_02;
import java.nio.file.*;
import java.util.Arrays;

/** Run with java -cp "target/test-classes;target/classes;target/dependency/*" week_02.Lab2SelfTest */
public class Lab2SelfTest {
    interface Action { void run() throws Exception; }
    static int checks;
    static void check(boolean result,String name) { if (!result) throw new AssertionError(name); checks++; }
    static void rejects(Action action,String name) throws Exception {
        try { action.run(); } catch (Exception expected) { checks++; return; }
        throw new AssertionError(name);
    }
    public static void main(String[] args) throws Exception {
        String text="Xin chào Lab 2 - Mã hóa hiện đại!";
        String longText=text.repeat(200);
        for (String plain : new String[]{text,longText,"","1234567890123456"}) {
            String des=DESCipher.encrypt(plain,"12345678");
            check(plain.equals(DESCipher.decrypt(des,"12345678")),"DES round-trip");
            check(!des.equals(DESCipher.encrypt(plain,"12345678")),"DES random IV");
            String triple=TripleDESCipher.encrypt(plain,"123456789012345678901234");
            check(plain.equals(TripleDESCipher.decrypt(triple,"123456789012345678901234")),"3DES round-trip");
            String aes=AESCipher.encrypt(plain,"MatKhauAES@2026");
            check(plain.equals(AESCipher.decrypt(aes,"MatKhauAES@2026")),"AES round-trip");
            byte[] altered=CipherUtil.fromB64(aes); altered[altered.length-1]^=1;
            rejects(() -> AESCipher.decrypt(CipherUtil.b64(altered),"MatKhauAES@2026"),"GCM tampering");
            rejects(() -> AESCipher.decrypt(aes,"wrong"),"GCM wrong password");
            check(plain.equals(RC4Cipher.decrypt(RC4Cipher.encrypt(plain,"Key"),"Key")),"RC4 round-trip");
            String iv=TwofishCipher.newIv(), two=TwofishCipher.encrypt(plain,"Twofish@Lab2",iv);
            check(plain.equals(TwofishCipher.decrypt(two,"Twofish@Lab2",iv)),"Twofish round-trip");
            String key=AESCTRCipher.newKey(), nonce=AESCTRCipher.newNonce(), ctr=AESCTRCipher.encrypt(plain,key,nonce);
            check(plain.equals(AESCTRCipher.decrypt(ctr,key,nonce)),"CTR round-trip");
            check(CipherUtil.fromB64(ctr).length==CipherUtil.utf8(plain).length,"CTR byte length");
            if (!plain.isEmpty()) {
                Path file=Files.createTempFile("lab2-roundtrip-",".txt");
                try {
                    Files.writeString(file,TwofishCipher.toFile(iv,two));
                    String[] parts=TwofishCipher.fromFile(Files.readString(file));
                    check(plain.equals(TwofishCipher.decrypt(parts[1],"Twofish@Lab2",parts[0])),"Twofish file");
                    Files.writeString(file,AESCTRCipher.toFile(nonce,ctr));
                    parts=AESCTRCipher.fromFile(Files.readString(file));
                    check(plain.equals(AESCTRCipher.decrypt(parts[1],key,parts[0])),"CTR file");
                } finally { Files.deleteIfExists(file); }
            }
        }
        check(RC4Cipher.encrypt("Plaintext","Key").equals("BBF316E8D940AF0AD3"),"RC4 published vector");
        rejects(() -> DESCipher.encrypt(text,"1234"),"DES key length");
        rejects(() -> TripleDESCipher.encrypt(text,"12345678901234567890123"),"3DES key length");
        rejects(() -> TwofishCipher.encrypt(text,"key","00"),"Twofish IV length");
        rejects(() -> AESCTRCipher.encrypt(text,"AA==","AA=="),"CTR key/nonce length");
        rejects(() -> CipherUtil.fromB64("!"),"invalid Base64");
        rejects(() -> CipherUtil.fromHex("XYZ"),"invalid HEX");
        rejects(() -> UserDao.register("x",new char[8]),"username validation");
        rejects(() -> UserDao.register("valid",new char[3]),"password validation");
        check(UserDao.newTicket().matches("[0-9A-F]{32}"),"ticket format");
        byte[] salt=CipherUtil.randomBytes(16);
        check(Arrays.equals(CipherUtil.pbkdf2("password".toCharArray(),salt,120000,256),CipherUtil.pbkdf2("password".toCharArray(),salt,120000,256)),"PBKDF2 stable salt");
        System.out.println("SELF-TEST OK: " + checks + " Java checks.");
    }
}
