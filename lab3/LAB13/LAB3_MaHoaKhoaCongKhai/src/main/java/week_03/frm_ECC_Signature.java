package week_03;
import javax.swing.*;
import java.awt.*;

public class frm_ECC_Signature extends JFrame {
    private final ECCSignature ecdsa=new ECCSignature();
    private final JTextArea txt_public=LabUI.area(2,false),txt_private=LabUI.area(2,false);
    private final JTextArea txt_plaintext=LabUI.area(4,true),txt_ciphertext=LabUI.area(4,true);
    private final JLabel lbl_status=new JLabel("Bấm Generate Keys hoặc Load Keys.");
    private final JLabel lbl_result=new JLabel(" ");
    public frm_ECC_Signature(){
        super("CHỮ KÝ SỐ ECDSA (SHA256withECDSA)"); JPanel p=LabUI.column(); LabUI.title(p,"CHỮ KÝ SỐ ECDSA (SHA256withECDSA)");
        LabUI.field(p,"Public key:",txt_public); LabUI.field(p,"Private key (khóa thử nghiệm):",txt_private);
        p.add(LabUI.row(LabUI.button("Generate Keys",this::generate),LabUI.button("Save Keys",this::saveKeys),LabUI.button("Load Keys",this::loadKeys)));
        LabUI.field(p,"Message:",txt_plaintext); LabUI.field(p,"Signature:",txt_ciphertext);
        JPanel buttons=LabUI.row(LabUI.button("Sign",()->crypt(true)),LabUI.button("Verify",()->crypt(false)));

        p.add(buttons); lbl_result.setFont(lbl_result.getFont().deriveFont(Font.BOLD,22f));p.add(lbl_result);
        lbl_status.setPreferredSize(new Dimension(740,65)); p.add(lbl_status); LabUI.finish(this,p);
    }
    private void generate(){try{
        ecdsa.generateKeys(); txt_public.setText(ecdsa.getPublicKeyBase64());txt_private.setText(ecdsa.getPrivateKeyBase64());
        txt_public.setCaretPosition(0);txt_private.setCaretPosition(0);lbl_result.setText(" ");
        lbl_status.setText("Đã tạo cặp khóa mới. Bản mã/chữ ký cũ không dùng được với khóa mới.");
    }catch(Exception ex){LabUI.error(this,"Không tạo được khóa",ex);}}
    private void saveKeys(){
        if(txt_public.getText().isBlank()){JOptionPane.showMessageDialog(this,"Chưa có khóa để lưu.");return;}
        String path=FileTextIO.saveString(this,CryptoSupport.toKeyFile(txt_public.getText(),txt_private.getText()),"ecdsa_keys.txt");
        if(path!=null)lbl_status.setText("<html>Đã lưu khóa thử nghiệm: "+path+". Giữ bí mật dòng PRIVATE.</html>");
    }
    private void loadKeys(){
        String content=FileTextIO.openString(this);if(content==null)return;
        try{String[] keys=CryptoSupport.fromKeyFile(content);ecdsa.loadKeys(keys[0],keys[1]);
            txt_public.setText(keys[0]);txt_private.setText(keys[1]);
            txt_public.setCaretPosition(0);txt_private.setCaretPosition(0);lbl_result.setText(" ");
            lbl_status.setText("Đã nạp khóa từ tệp.");
        }catch(Exception ex){LabUI.error(this,"Tệp khóa không hợp lệ",ex);}
    }
    private void crypt(boolean sign){
        String message=txt_plaintext.getText();String operation=sign?"SIGN":"VERIFY";
        try{
            boolean ok=true;
            if(sign){txt_ciphertext.setText(ecdsa.sign(message));lbl_result.setText(" ");}
            else{ok=ecdsa.verify(message,txt_ciphertext.getText());lbl_result.setText(ok?"VALID":"INVALID");
                lbl_result.setForeground(ok?new Color(0,128,0):Color.RED);}
            lbl_status.setText("<html>"+(sign?"Đã ký bằng KHÓA RIÊNG. ":"Đã xác minh bằng KHÓA CÔNG KHAI. ")
                +HistoryDao.tryInsert("ECDSA",operation,message.length(),ok)+"</html>");
        }catch(Exception ex){lbl_result.setText(" ");
            lbl_status.setText("<html>Thao tác thất bại. "+HistoryDao.tryInsert("ECDSA",operation,message.length(),false)+"</html>");
            LabUI.error(this,sign?"Ký thất bại":"Xác minh thất bại",ex);}
    }

    public static void main(String[] args){SwingUtilities.invokeLater(()->new frm_ECC_Signature().setVisible(true));}
}
