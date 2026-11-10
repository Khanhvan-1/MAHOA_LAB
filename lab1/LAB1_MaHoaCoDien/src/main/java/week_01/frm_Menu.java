
package week_01;

import javax.swing.*;
import java.awt.*;

public class frm_Menu extends JFrame {

    public frm_Menu() {
        setTitle("LAB 1 - Mã hóa đối xứng cổ điển");
        setSize(730, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {

        JPanel main = new JPanel(new BorderLayout(12, 12));
        main.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        // Tieu de
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(
            "LAB 1 - MÃ HÓA ĐỐI XỨNG CỔ ĐIỂN",
            SwingConstants.CENTER
        );
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(
            "Chọn một bài để mở form tương ứng:",
            SwingConstants.CENTER
        );
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        top.add(title);
        top.add(Box.createVerticalStrut(12));
        top.add(subtitle);

        main.add(top, BorderLayout.NORTH);

        // 9 nut chuc nang
        JPanel buttons = new JPanel(new GridLayout(3, 3, 12, 12));

        JButton btn_caesar = new JButton("1.1 Caesar");
        JButton btn_vigenere = new JButton("1.2 Vigenère");
        JButton btn_railfence = new JButton("1.3 Rail Fence");

        JButton btn_playfair = new JButton("1.4 Playfair");
        JButton btn_transposition = new JButton(
            "1.5 Transposition"
        );
        JButton btn_autokey = new JButton("1.6 Autokey");

        JButton btn_tonghop = new JButton(
            "Nâng cao 01 - Tổng hợp"
        );
        JButton btn_tancong = new JButton(
            "Nâng cao 02 - Tấn công"
        );
        JButton btn_sql = new JButton(
            "Kết nối SQL Server"
        );

        buttons.add(btn_caesar);
        buttons.add(btn_vigenere);
        buttons.add(btn_railfence);

        buttons.add(btn_playfair);
        buttons.add(btn_transposition);
        buttons.add(btn_autokey);

        buttons.add(btn_tonghop);
        buttons.add(btn_tancong);
        buttons.add(btn_sql);

        main.add(buttons, BorderLayout.CENTER);

        // Ghi chu
        JLabel footer = new JLabel(
            "Mỗi form mở ra là một cửa sổ riêng; "
            + "đóng form con không làm tắt chương trình.",
            SwingConstants.CENTER
        );

        main.add(footer, BorderLayout.SOUTH);
        add(main);

        // Xu ly su kien cac nut
        btn_caesar.addActionListener(e ->
            new frm_Caesar().setVisible(true)
        );

        btn_vigenere.addActionListener(e ->
            new frm_Vigenere().setVisible(true)
        );

        btn_railfence.addActionListener(e ->
            new frm_RailFence().setVisible(true)
        );

        btn_playfair.addActionListener(e ->
            new frm_Playfair().setVisible(true)
        );

        btn_transposition.addActionListener(e ->
            new frm_Transposition().setVisible(true)
        );

        btn_autokey.addActionListener(e ->
            new frm_Autokey().setVisible(true)
        );

        btn_tonghop.addActionListener(e ->
            new frm_TongHop().setVisible(true)
        );

        btn_tancong.addActionListener(e ->
            new frm_TanCong().setVisible(true)
        );

        btn_sql.addActionListener(e ->
            new frm_KetNoiSQL().setVisible(true)
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new frm_Menu().setVisible(true);
        });
    }
}
