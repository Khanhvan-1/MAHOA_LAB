package week_02;
import java.awt.*;
import javax.swing.*;
public class frm_Menu extends JFrame {
    public frm_Menu() {
        setTitle("LAB 2 - Mã hóa đối xứng hiện đại"); setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new BorderLayout(12,12));
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        JLabel title = new JLabel("LAB 2 - MÃ HÓA ĐỐI XỨNG HIỆN ĐẠI");
        title.setFont(new Font("Segoe UI",Font.BOLD,20)); panel.add(title,BorderLayout.NORTH);
        JPanel buttons = new JPanel(new GridLayout(4,2,12,12));
        addButton(buttons, "2.1 DES", () -> new frm_DES().setVisible(true));
        addButton(buttons, "2.2 TripleDES", () -> new frm_TripleDES().setVisible(true));
        addButton(buttons, "2.3 AES + Login", () -> new frm_AES().setVisible(true));
        addButton(buttons, "2.4 RC4", () -> new frm_RC4().setVisible(true));
        addButton(buttons, "2.5 Twofish", () -> new frm_Twofish().setVisible(true));
        addButton(buttons, "2.6 Nâng cao - AES-CTR", () -> new frm_AES_CTR().setVisible(true));
        addButton(buttons, "Kết nối SQL Server", () -> new frm_KetNoiSQL().setVisible(true));
        addButton(buttons, "Xem lịch sử", () -> new frm_LichSu().setVisible(true));
        panel.add(buttons,BorderLayout.CENTER);
        panel.add(new JLabel("Bài nâng cao 02: chạy LAB2_Python/aes_cbc_gui.py."),BorderLayout.SOUTH);
        setContentPane(panel); pack(); setMinimumSize(getSize()); setLocationRelativeTo(null);
    }
    private void addButton(JPanel panel,String text,Runnable action) {
        JButton button=new JButton(text); button.setPreferredSize(new Dimension(250,42));
        button.addActionListener(e -> action.run()); panel.add(button);
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new frm_Menu().setVisible(true)); }
}
