package week_02;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.lang.reflect.Field;

/** Paint actual Swing components without opening windows; smoke-test action listeners. */
public class Lab2GuiTest {
    static Object field(Object form,String name) throws Exception { Field f=form.getClass().getDeclaredField(name); f.setAccessible(true); return f.get(form); }
    static void layout(Container c) { c.doLayout(); for(Component child:c.getComponents()) if(child instanceof Container) layout((Container)child); }
    public static void main(String[] args) throws Exception {
        Path output=Path.of(args[0]); Files.createDirectories(output);
        SwingUtilities.invokeAndWait(() -> {
            try {
                for(JFrame f:new JFrame[]{new frm_Menu(),new frm_DES(),new frm_TripleDES(),new frm_AES(),new frm_RC4(),new frm_Twofish(),new frm_AES_CTR(),new frm_KetNoiSQL(),new frm_LichSu()}) {
                    try {
                        String name=f.getClass().getSimpleName();
                        if (name.equals("frm_DES")||name.equals("frm_TripleDES")||name.equals("frm_RC4")||name.equals("frm_Twofish")||name.equals("frm_AES_CTR")) {
                            JTextArea plain=(JTextArea)field(f,"txt_plaintext"), cipher=(JTextArea)field(f,"txt_ciphertext");
                            String text=name.equals("frm_RC4")?"Plaintext":"Xin chào Lab 2 - Mã hóa hiện đại!";
                            if(name.equals("frm_Twofish")) ((JButton)field(f,"btn_iv")).doClick();
                            if(name.equals("frm_AES_CTR")) { ((JButton)field(f,"btn_genkey")).doClick(); ((JButton)field(f,"btn_gennonce")).doClick(); }
                            plain.setText(text); ((JButton)field(f,"btn_encrypt")).doClick();
                            if(cipher.getText().isEmpty()) throw new AssertionError("No ciphertext");
                            plain.setText(""); ((JButton)field(f,"btn_decrypt")).doClick();
                            if(!plain.getText().equals(text)) throw new AssertionError("UI round-trip: "+name);
                        }
                        layout(f);
                        Container content=f.getContentPane();
                        BufferedImage image=new BufferedImage(content.getWidth(),content.getHeight(),BufferedImage.TYPE_INT_RGB);
                        Graphics2D g=image.createGraphics(); content.printAll(g); g.dispose();
                        ImageIO.write(image,"png",output.resolve(name+".png").toFile());
                    } finally { f.dispose(); }
                }
                System.out.println("GUI TEST OK: 9 forms, 5 UI encryption/decryption round-trips.");
            } catch(Exception ex) { throw new RuntimeException(ex); }
        });
    }
}
