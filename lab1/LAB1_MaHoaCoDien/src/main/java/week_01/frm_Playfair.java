package week_01;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.*;

public class frm_Playfair extends JFrame {

    private final JTextArea txt_plaintext = new JTextArea(3, 45);
    private final JTextField txt_key = new JTextField("KEYWORD", 12);
    private final JTextArea txt_ciphertext = new JTextArea(3, 45);

    private final JButton btn_encrypt = new JButton("Encrypt");
    private final JButton btn_decrypt = new JButton("Decrypt");
    private final JButton btn_open = new JButton("Open Ciphertext File");
    private final JButton btn_save = new JButton("Save Ciphertext File");

    private final JLabel lbl_status = new JLabel("Sẵn sàng.");
    private final JTextArea txt_matrix = new JTextArea(5, 9);
    private final JButton btn_matrix = new JButton("Generate Key Matrix");

    public frm_Playfair() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Playfair Cipher");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(10, 12));
        content.setBorder(
                BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("MẬT MÃ PLAYFAIR");
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
        keyPanel.add(new JLabel("Chỉ gồm chữ A-Z, ví dụ KEYWORD"));

        JPanel cipherPanel = new JPanel(new BorderLayout(0, 5));
        cipherPanel.add(new JLabel("Ciphertext:"), BorderLayout.NORTH);
        cipherPanel.add(new JScrollPane(txt_ciphertext), BorderLayout.CENTER);

        fields.add(plainPanel);
        fields.add(Box.createVerticalStrut(8));
                fields.add(keyPanel);
        fields.add(Box.createVerticalStrut(8));

        txt_matrix.setEditable(false);
        txt_matrix.setLineWrap(false);
        txt_matrix.setFont(new Font("Monospaced", Font.BOLD, 14));

        JPanel matrixPanel = new JPanel(new BorderLayout(10, 5));
        matrixPanel.add(new JLabel("Key Matrix:"), BorderLayout.NORTH);
        matrixPanel.add(new JScrollPane(txt_matrix), BorderLayout.CENTER);

        JPanel matrixButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        matrixButtons.add(btn_matrix);
        matrixPanel.add(matrixButtons, BorderLayout.SOUTH);

        fields.add(matrixPanel);
        fields.add(Box.createVerticalStrut(8));
        fields.add(cipherPanel);

        btn_matrix.addActionListener(e -> {
            try {
                txt_matrix.setText(
                        PlayfairCipher.getKeyMatrix(txt_key.getText()));
                txt_matrix.setCaretPosition(0);
                lbl_status.setText(
                        "Đã tạo ma trận khóa 5x5 (I và J dùng chung ô).");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Khóa không hợp lệ",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

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
                    this, txt_ciphertext, "playfair_ciphertext.txt");
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
            txt_ciphertext.setText(PlayfairCipher.encrypt(plaintext, key));
            lbl_status.setText("Đã mã hóa Playfair (bỏ dấu cách, J -> I, chèn X).");
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
            txt_plaintext.setText(PlayfairCipher.decrypt(ciphertext, key));
            lbl_status.setText("Đã giải mã Playfair (bỏ dấu cách, J -> I, chèn X).");
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
                "Khóa Vigenere phải là Chỉ gồm chữ A-Z, ví dụ KEYWORD.",
                "Khóa không hợp lệ",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new frm_Playfair().setVisible(true);
        });
    }
}