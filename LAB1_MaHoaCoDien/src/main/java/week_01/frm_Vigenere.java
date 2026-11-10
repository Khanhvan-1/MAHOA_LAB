package week_01;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.*;

public class frm_Vigenere extends JFrame {

    private final JTextArea txt_plaintext = new JTextArea(3, 45);
    private final JTextField txt_key = new JTextField("KEY", 12);
    private final JTextArea txt_ciphertext = new JTextArea(3, 45);

    private final JButton btn_encrypt = new JButton("Encrypt");
    private final JButton btn_decrypt = new JButton("Decrypt");
    private final JButton btn_open = new JButton("Open Ciphertext File");
    private final JButton btn_save = new JButton("Save Ciphertext File");

    private final JLabel lbl_status = new JLabel("Sẵn sàng.");

    public frm_Vigenere() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Vigenere Cipher");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(10, 12));
        content.setBorder(
                BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("MẬT MÃ VIGENÈRE");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        content.add(title, BorderLayout.NORTH);

        txt_plaintext.setLineWrap(true);
        txt_plaintext.setWrapStyleWord(true);
        txt_ciphertext.setLineWrap(true);
        txt_ciphertext.setWrapStyleWord(true);

        JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));

        JPanel plainPanel = new JPanel(new BorderLayout(0, 5));
        plainPanel.add(new JLabel("Plaintext:"), BorderLayout.NORTH);
        plainPanel.add(new JScrollPane(txt_plaintext), BorderLayout.CENTER);

        JPanel keyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        keyPanel.add(new JLabel("Key:"));
        keyPanel.add(txt_key);
        keyPanel.add(new JLabel("Chỉ gồm chữ A-Z, ví dụ KEY"));

        JPanel cipherPanel = new JPanel(new BorderLayout(0, 5));
        cipherPanel.add(new JLabel("Ciphertext:"), BorderLayout.NORTH);
        cipherPanel.add(new JScrollPane(txt_ciphertext), BorderLayout.CENTER);

        fields.add(plainPanel);
        fields.add(Box.createVerticalStrut(8));
        fields.add(keyPanel);
        fields.add(Box.createVerticalStrut(8));
        fields.add(cipherPanel);

        content.add(fields, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(btn_encrypt);
        buttons.add(btn_decrypt);
        buttons.add(btn_open);
        buttons.add(btn_save);

        JPanel bottom = new JPanel(new BorderLayout(0, 8));
        bottom.add(buttons, BorderLayout.NORTH);
        bottom.add(lbl_status, BorderLayout.SOUTH);
        content.add(bottom, BorderLayout.SOUTH);

        btn_encrypt.addActionListener(e -> encrypt());
        btn_decrypt.addActionListener(e -> decrypt());

        btn_open.addActionListener(e -> {
            String path = FileTextIO.openText(this, txt_ciphertext);
            if (path != null) {
                lbl_status.setText("Đã mở tệp: " + path);
            }
        });

        btn_save.addActionListener(e -> {
            String path = FileTextIO.saveText(
                    this, txt_ciphertext, "vigenere_ciphertext.txt");
            if (path != null) {
                lbl_status.setText("Đã lưu tệp: " + path);
            }
        });

        setContentPane(content);
        pack();
        setMinimumSize(getSize());
    }

    private void encrypt() {
        String plaintext = txt_plaintext.getText();

        if (plaintext.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Hãy nhập Plaintext trước khi mã hóa.");
            return;
        }

        try {
            String key = txt_key.getText().trim();
            txt_ciphertext.setText(VigenereCipher.encrypt(plaintext, key));
            lbl_status.setText("Đã mã hóa Vigenère với khóa " + key + ".");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Dữ liệu không hợp lệ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
        private void decrypt() {
        String ciphertext = txt_ciphertext.getText();

        if (ciphertext.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Hãy nhập Ciphertext trước khi giải mã.");
            return;
        }

        try {
            String key = txt_key.getText().trim();
            txt_plaintext.setText(VigenereCipher.decrypt(ciphertext, key));
            lbl_status.setText("Đã giải mã Vigenère với khóa " + key + ".");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Dữ liệu không hợp lệ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unused")
    private void showKeyError() {
        JOptionPane.showMessageDialog(
                this,
                "Khóa Vigenere phải là Chỉ gồm chữ A-Z, ví dụ KEY.",
                "Khóa không hợp lệ",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new frm_Vigenere().setVisible(true);
        });
    }
}