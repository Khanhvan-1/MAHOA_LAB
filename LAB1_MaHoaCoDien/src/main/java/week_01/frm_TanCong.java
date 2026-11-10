
package week_01;

import javax.swing.*;
import java.awt.*;

public class frm_TanCong extends JFrame {

    private JTextArea txt_cipher;
    private JTextArea txt_result;
    private JTextField txt_known_plain;
    private JTextField txt_known_cipher;
    private JLabel lbl_status;

    public frm_TanCong() {
        setTitle("Phan tich (Tan cong) mat ma co dien");
        setSize(750, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(
                15, 20, 15, 20));

        JLabel title = new JLabel(
                "PHÂN TÍCH (TẤN CÔNG) MẬT MÃ CỔ ĐIỂN",
                SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        main.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        // Nhap ban ma
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                "Ciphertext"));

        txt_cipher = new JTextArea(3, 30);
        txt_cipher.setLineWrap(true);
        txt_cipher.setWrapStyleWord(true);
        txt_cipher.setText("KHOOR");

        inputPanel.add(new JScrollPane(txt_cipher),
                BorderLayout.CENTER);
        center.add(inputPanel);

        // Hai nut phan tich Caesar
        JPanel buttons = new JPanel(new FlowLayout());

        JButton btn_bruteforce = new JButton(
                "Brute Force Caesar");
        JButton btn_frequency = new JButton(
                "Frequency Analysis");

        buttons.add(btn_bruteforce);
        buttons.add(btn_frequency);
        center.add(buttons);

        // Known plaintext
        JPanel knownPanel = new JPanel(new FlowLayout());
        knownPanel.setBorder(BorderFactory.createTitledBorder(
                "Known Plaintext - Vigenere"));

        txt_known_plain = new JTextField("HELLO", 12);
        txt_known_cipher = new JTextField("RIJVS", 12);

        JButton btn_known = new JButton(
                "Known Plaintext (Vigenere)");

        knownPanel.add(new JLabel("Bản rõ đã biết:"));
        knownPanel.add(txt_known_plain);
        knownPanel.add(new JLabel("Bản mã:"));
        knownPanel.add(txt_known_cipher);
        knownPanel.add(btn_known);

        center.add(knownPanel);

        // Ket qua
        JPanel resultPanel = new JPanel(new BorderLayout(5, 5));
        resultPanel.setBorder(BorderFactory.createTitledBorder(
                "Kết quả"));

        txt_result = new JTextArea(15, 40);
        txt_result.setEditable(false);
        txt_result.setFont(new Font(
                "Consolas", Font.PLAIN, 14));
        txt_result.setLineWrap(false);

        resultPanel.add(new JScrollPane(txt_result),
                BorderLayout.CENTER);
        center.add(resultPanel);

        main.add(center, BorderLayout.CENTER);

        // Trang thai
        lbl_status = new JLabel("Sẵn sàng.");
        main.add(lbl_status, BorderLayout.SOUTH);

        add(main);

        // Su kien Brute Force
        btn_bruteforce.addActionListener(e -> {
            String cipher = txt_cipher.getText().trim();

            if (cipher.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, "Hãy nhập Ciphertext.");
                return;
            }

            txt_result.setText(
                    AttackDemos.caesarBruteForce(cipher));
            txt_result.setCaretPosition(0);
            lbl_status.setText(
                    "Đã thử đủ 26 khóa Caesar.");
        });

        // Su kien Frequency Analysis
        btn_frequency.addActionListener(e -> {
            String cipher = txt_cipher.getText().trim();

            if (cipher.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, "Hãy nhập Ciphertext.");
                return;
            }

            txt_result.setText(
                    AttackDemos.frequency(cipher));
            txt_result.setCaretPosition(0);
            lbl_status.setText(
                    "Đã thống kê tần suất chữ cái.");
        });

        // Su kien Known Plaintext
        btn_known.addActionListener(e -> {
            try {
                String key = AttackDemos.knownPlaintext(
                        txt_known_plain.getText(),
                        txt_known_cipher.getText());

                txt_result.setText(
                        "Khóa Vigenère suy ra từng vị trí: "
                        + key
                        + "\nNếu khóa ngắn hơn bản rõ, "
                        + "khóa sẽ lặp lại (KEY KEY ...).");

                lbl_status.setText("Khóa suy ra: " + key);

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Dữ liệu không hợp lệ",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new frm_TanCong().setVisible(true);
        });
    }
}
