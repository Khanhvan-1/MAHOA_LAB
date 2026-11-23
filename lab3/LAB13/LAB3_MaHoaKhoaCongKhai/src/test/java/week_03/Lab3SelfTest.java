package week_03;

public class Lab3SelfTest {
    private static int checks;
    interface Action { void run() throws Exception; }
    static void check(boolean ok,String message){if(!ok)throw new AssertionError(message);checks++;}
    static void fails(Action action,String message)throws Exception{
        try{action.run();}catch(Exception ex){checks++;return;}throw new AssertionError(message);
    }
    public static void main(String[] args)throws Exception{
        for(int bits:new int[]{2048,3072}){
            RSACipher r=new RSACipher();r.generateKeys(bits);
            String c=r.encrypt("HELLO RSA");
            check(r.decrypt(c).equals("HELLO RSA"),"RSA roundtrip");
            check(c.length()==(bits==2048?344:512),"RSA ciphertext length");
            check(!c.equals(r.encrypt("HELLO RSA")),"OAEP randomness");
            String limit="A".repeat(r.maxPlaintextBytes());
            check(r.decrypt(r.encrypt(limit)).equals(limit),"RSA maximum");
            fails(()->r.encrypt(limit+"A"),"RSA overflow");
            r.generateKeys(bits);fails(()->r.decrypt(c),"RSA wrong key");
        }
        ECCCipher ecc=new ECCCipher();ecc.generateKeys();String ec=ecc.encrypt("HELLO ECC");
        check(ecc.decrypt(ec).equals("HELLO ECC"),"ECC roundtrip");
        check(ecc.getPublicKeyBase64().length()==124,"ECC public length");
        check(!ec.equals(ecc.encrypt("HELLO ECC")),"ECC randomness");
        String[] keys=CryptoSupport.fromKeyFile(CryptoSupport.toKeyFile(ecc.getPublicKeyBase64(),ecc.getPrivateKeyBase64()));
        ECCCipher restored=new ECCCipher();restored.loadKeys(keys[0],keys[1]);
        check(restored.decrypt(ec).equals("HELLO ECC"),"ECC saved keys");
        ECCCipher publicOnly=new ECCCipher();publicOnly.loadKeys(keys[0],"");
        check(ecc.decrypt(publicOnly.encrypt("public only")).equals("public only"),"ECC public only encrypt");
        fails(()->publicOnly.decrypt(ec),"ECC private missing");
        ecc.generateKeys();fails(()->ecc.decrypt(ec),"ECC wrong key");
        byte[] ebytes=CryptoSupport.unb64(ec);ebytes[ebytes.length-1]^=1;
        fails(()->restored.decrypt(CryptoSupport.b64(ebytes)),"ECC tamper");
        RSA_AES_Cipher hybrid=new RSA_AES_Cipher();hybrid.generateKeys();
        String longText="Tiếng Việt - LAB3 RSA + AES.\n".repeat(100);
        String hc=hybrid.encrypt(longText);
        check(hybrid.decrypt(hc).equals(longText),"Hybrid long Unicode roundtrip");
        check(!hc.equals(hybrid.encrypt(longText)),"Hybrid randomness");
        RSA_AES_Cipher hr=new RSA_AES_Cipher();hr.loadKeys(hybrid.getPublicKeyBase64(),hybrid.getPrivateKeyBase64());
        check(hr.decrypt(hc).equals(longText),"Hybrid saved keys");
        byte[] bytes=CryptoSupport.unb64(hc);bytes[bytes.length-1]^=1;
        fails(()->hybrid.decrypt(CryptoSupport.b64(bytes)),"Hybrid tamper");
        hybrid.generateKeys();fails(()->hybrid.decrypt(hc),"Hybrid wrong key");
        ECCSignature sig=new ECCSignature();sig.generateKeys();String signature=sig.sign("THONG DIEP A");
        check(sig.verify("THONG DIEP A",signature),"Signature valid");
        check(!sig.verify("THONG DIEP B",signature),"Signature modified message");
        ECCSignature verifier=new ECCSignature();verifier.loadKeys(sig.getPublicKeyBase64(),"");
        check(verifier.verify("THONG DIEP A",signature),"Public only verify");
        fails(()->verifier.sign("THONG DIEP A"),"Public only signing rejected");
        sig.generateKeys();check(!sig.verify("THONG DIEP A",signature),"Signature wrong key");
        System.out.println("PASS: "+checks+" checks (RSA, ECC, RSA-AES, ECDSA).");
    }
}
