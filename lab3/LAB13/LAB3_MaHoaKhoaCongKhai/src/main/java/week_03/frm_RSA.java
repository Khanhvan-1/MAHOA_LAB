package week_03;
import javax.swing.*;
import java.awt.*;

public class frm_RSA extends JFrame {
    private final RSACipher rsa=new RSACipher();
    private final JComboBox<String> cbo_bits=new JComboBox<>(new String[]{"2048","3072"});
    private final JTextArea txt_plaintext=LabUI.area(3,true),txt_ciphertext=LabUI.area(5,true);
    private final JLabel lbl_keyinfo=new JLabel("Chưa có khóa.");
    private final JLabel lbl_status=new JLabel("Bấm Generate Keys để tạo cặp khóa.");
    public frm_RSA(){
        super("RSA Cipher"); JPanel p=LabUI.column(); LabUI.title(p,"MẬT MÃ RSA (OAEP - SHA-256)");
        p.add(LabUI.row(new JLabel("Key size:"),cbo_bits,LabUI.button("Generate Keys",this::generate)));
        p.add(lbl_keyinfo); LabUI.field(p,"Plaintext:",txt_plaintext); LabUI.field(p,"Ciphertext:",txt_ciphertext);
        p.add(LabUI.row(LabUI.button("Encrypt",()->crypt(true)),LabUI.button("Decrypt",()->crypt(false))));
        lbl_status.setPreferredSize(new Dimension(700,55)); p.add(lbl_status); LabUI.finish(this,p);
    }
    private void generate(){try{
        long start=System.currentTimeMillis(); rsa.generateKeys(Integer.parseInt((String)cbo_bits.getSelectedItem()));
        lbl_keyinfo.setText("RSA-"+rsa.getBits()+": tạo khóa "+(System.currentTimeMillis()-start)+" ms, tối đa "+rsa.maxPlaintextBytes()+" byte");
        lbl_status.setText("Đã tạo cặp khóa mới.");
    }catch(Exception ex){LabUI.error(this,"Không tạo được khóa",ex);}}
    private void crypt(boolean encrypt){
        String input=(encrypt?txt_plaintext:txt_ciphertext).getText();
        if(input.isEmpty()){JOptionPane.showMessageDialog(this,"Hãy nhập "+(encrypt?"Plaintext":"Ciphertext")+" trước.");return;}
        String operation=encrypt?"ENCRYPT":"DECRYPT";
        try{
            String output=encrypt?rsa.encrypt(input):rsa.decrypt(input);
            JTextArea target=encrypt?txt_ciphertext:txt_plaintext; target.setText(output);target.setCaretPosition(0);
            lbl_status.setText("<html>"+(encrypt?"Đã mã hóa RSA ("+output.length()+" ký tự Base64). ":"Đã giải mã RSA. ")
                +HistoryDao.tryInsert("RSA",operation,input.length(),true)+"</html>");
        }catch(Exception ex){
            lbl_status.setText("<html>THẤT BẠI. "+HistoryDao.tryInsert("RSA",operation,input.length(),false)+"</html>");
            LabUI.error(this,encrypt?"Mã hóa thất bại":"Giải mã thất bại: sai khóa hoặc bản mã bị sửa",ex);
        }
    }
    public static void main(String[] args){SwingUtilities.invokeLater(()->new frm_RSA().setVisible(true));}
}
