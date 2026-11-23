package week_03;
import javax.swing.*;
import java.awt.*;

public class frm_Menu extends JFrame {
    public frm_Menu(){
        super("LAB 3 - Mã hóa khóa công khai");JPanel p=LabUI.column();
        LabUI.title(p,"LAB 3 - MÃ HÓA KHÓA CÔNG KHAI");
        p.add(new JLabel("Bài 3.1: chạy RSADemo trong Terminal. Chọn bài bên dưới để mở giao diện."));
        p.add(Box.createVerticalStrut(15));
        JPanel buttons=new JPanel(new GridLayout(3,2,12,12));
        buttons.add(LabUI.button("3.2 RSA (Swing)",()->new frm_RSA().setVisible(true)));
        buttons.add(LabUI.button("3.3 ECC",()->new frm_ECC().setVisible(true)));
        buttons.add(LabUI.button("3.4 RSA + AES",()->new frm_RSA_AES().setVisible(true)));
        buttons.add(LabUI.button("3.5 Chữ ký ECDSA",()->new frm_ECC_Signature().setVisible(true)));
        buttons.add(LabUI.button("Kết nối SQL Server",()->new frm_KetNoiSQL().setVisible(true)));
        buttons.add(LabUI.button("Xem lịch sử",()->new frm_LichSu().setVisible(true)));
        buttons.setPreferredSize(new Dimension(680,220));p.add(buttons);LabUI.finish(this,p);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String[] args){SwingUtilities.invokeLater(()->new frm_Menu().setVisible(true));}
}
