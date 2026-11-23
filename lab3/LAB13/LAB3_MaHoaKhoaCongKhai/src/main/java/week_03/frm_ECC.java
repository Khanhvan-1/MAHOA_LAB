package week_03;
import javax.swing.*;
import java.awt.*;

public class frm_ECC extends JFrame {
    private final ECCCipher ecc=new ECCCipher();
    private final JTextArea txt_public=LabUI.area(2,false),txt_private=LabUI.area(2,false);
    private final JTextArea txt_plaintext=LabUI.area(4,true),txt_ciphertext=LabUI.area(4,true);
    private final JLabel lbl_status=new JLabel("Bấm Generate Keys hoặc Load Keys.");
    private final JLabel lbl_result=new JLabel(" ");
    public frm_ECC(){
        super("MẬT MÃ ĐƯỜNG CONG ELLIPTIC (ECC - secp256r1)"); JPanel p=LabUI.column(); LabUI.title(p,"MẬT MÃ ĐƯỜNG CONG ELLIPTIC (ECC - secp256r1)");
        LabUI.field(p,"Public key:",txt_public); LabUI.field(p,"Private key (khóa thử nghiệm):",txt_private);
        p.add(LabUI.row(LabUI.button("Generate Keys",this::generate),LabUI.button("Save Keys",this::saveKeys),LabUI.button("Load Keys",this::loadKeys)));
        LabUI.field(p,"Plaintext:",txt_plaintext); LabUI.field(p,"Ciphertext:",txt_ciphertext);
        JPanel buttons=LabUI.row(LabUI.button("Encrypt",()->crypt(true)),LabUI.button("Decrypt",()->crypt(false)));

        p.add(buttons); lbl_result.setFont(lbl_result.getFont().deriveFont(Font.BOLD,22f));p.add(lbl_result);
        lbl_status.setPreferredSize(new Dimension(740,65)); p.add(lbl_status); LabUI.finish(this,p);
    }
    private void generate(){try{
        ecc.generateKeys(); txt_public.setText(ecc.getPublicKeyBase64());txt_private.setText(ecc.getPrivateKeyBase64());
        txt_public.setCaretPosition(0);txt_private.setCaretPosition(0);lbl_result.setText(" ");
        lbl_status.setText("Đã tạo cặp khóa mới. Bản mã/chữ ký cũ không dùng được với khóa mới.");
    }catch(Exception ex){LabUI.error(this,"Không tạo được khóa",ex);}}
    private void saveKeys(){
        if(txt_public.getText().isBlank()){JOptionPane.showMessageDialog(this,"Chưa có khóa để lưu.");return;}
        String path=FileTextIO.saveString(this,CryptoSupport.toKeyFile(txt_public.getText(),txt_private.getText()),"ecc_keys.txt");
        if(path!=null)lbl_status.setText("<html>Đã lưu khóa thử nghiệm: "+path+". Giữ bí mật dòng PRIVATE.</html>");
    }
    private void loadKeys(){
        String content=FileTextIO.openString(this);if(content==null)return;
        try{String[] keys=CryptoSupport.fromKeyFile(content);ecc.loadKeys(keys[0],keys[1]);
            txt_public.setText(keys[0]);txt_private.setText(keys[1]);
            txt_public.setCaretPosition(0);txt_private.setCaretPosition(0);lbl_result.setText(" ");
            lbl_status.setText("Đã nạp khóa từ tệp.");
        }catch(Exception ex){LabUI.error(this,"Tệp khóa không hợp lệ",ex);}
    }
    private void crypt(boolean encrypt){
        String input=(encrypt?txt_plaintext:txt_ciphertext).getText();
        if(input.isEmpty()){JOptionPane.showMessageDialog(this,"Hãy nhập "+(encrypt?"Plaintext":"Ciphertext")+" trước.");return;}
        String operation=encrypt?"ENCRYPT":"DECRYPT";
        try{
            String output=encrypt?ecc.encrypt(input):ecc.decrypt(input);
            JTextArea target=encrypt?txt_ciphertext:txt_plaintext;target.setText(output);target.setCaretPosition(0);
            lbl_status.setText("<html>"+(encrypt?"Đã mã hóa":"Đã giải mã")+" ECC. Bản rõ: "+txt_plaintext.getText().length()+" ký tự. "
                +HistoryDao.tryInsert("ECC",operation,input.length(),true)+"</html>");
        }catch(Exception ex){
            lbl_status.setText("<html>THẤT BẠI (sai khóa hoặc bản mã bị sửa). "+HistoryDao.tryInsert("ECC",operation,input.length(),false)+"</html>");
            LabUI.error(this,encrypt?"Mã hóa thất bại":"Giải mã thất bại: sai khóa riêng hoặc bản mã bị sửa",ex);
        }
    }

    public static void main(String[] args){SwingUtilities.invokeLater(()->new frm_ECC().setVisible(true));}
}
