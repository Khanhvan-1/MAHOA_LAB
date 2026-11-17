package week_02;

import java.awt.*;
import javax.swing.*;

/** Giao diện Swing cho bài AES-CTR, chuyển từ hướng dẫn NetBeans sang VS Code. */
public class frm_AES_CTR extends JFrame {
    private final JTextArea txt_plaintext = new JTextArea(4, 54);
    private final JTextArea txt_ciphertext = new JTextArea(4, 54);
    private final JTextField txt_key = new JTextField("", 32);
    private final JButton btn_encrypt = new JButton("Encrypt");
    private final JButton btn_decrypt = new JButton("Decrypt");
    private final JButton btn_save = new JButton("Save File");
    private final JButton btn_open = new JButton("Open File");
    private final JLabel lbl_status = new JLabel("Sẵn sàng.");
    private final JTextField txt_nonce = new JTextField(32);
    private final JButton btn_gennonce = new JButton("Generate Nonce");
    private final JButton btn_genkey = new JButton("Generate Key");

    public frm_AES_CTR() {
        setTitle("AES-CTR Cipher");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel content = new JPanel(new BorderLayout(10, 12));
        content.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        JLabel title = new JLabel("MẬT MÃ AES-CTR");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        content.add(title, BorderLayout.NORTH);
        JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
        
        txt_plaintext.setLineWrap(true); txt_plaintext.setWrapStyleWord(true);
        txt_ciphertext.setLineWrap(true);
        fields.add(area("Plaintext:", txt_plaintext));
        fields.add(row("Key (Base64):", txt_key , btn_genkey));
        fields.add(row("Mỗi thông điệp dùng nonce mới; giữ đúng Key và Nonce để giải mã."));
        fields.add(row("Nonce (Base64):", txt_nonce, btn_gennonce));
        fields.add(area("Ciphertext:", txt_ciphertext));
        fields.add(row(btn_encrypt, btn_decrypt, btn_save, btn_open));
        content.add(fields, BorderLayout.CENTER);
        lbl_status.setPreferredSize(new Dimension(720, 52));
        content.add(lbl_status, BorderLayout.SOUTH);
        btn_encrypt.addActionListener(e -> transform(true));
        btn_decrypt.addActionListener(e -> transform(false));
        btn_save.addActionListener(e -> saveFile());
        btn_open.addActionListener(e -> openFile());
        btn_gennonce.addActionListener(e -> { txt_nonce.setText(AESCTRCipher.newNonce()); lbl_status.setText("Đã tạo Nonce mới. Giữ nguyên để giải mã."); });
        btn_genkey.addActionListener(e -> { txt_key.setText(AESCTRCipher.newKey()); lbl_status.setText("Đã tạo khóa ngẫu nhiên 256 bit."); });
        setContentPane(content); pack(); setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    private static JPanel row(Object... items) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (Object item : items) panel.add(item instanceof String ? new JLabel((String)item) : (Component)item);
        return panel;
    }
    private static JPanel area(String label, JTextArea text) {
        JPanel panel = new JPanel(new BorderLayout(0,5));
        panel.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
        panel.add(new JLabel(label),BorderLayout.NORTH);
        panel.add(new JScrollPane(text),BorderLayout.CENTER); return panel;
    }
    private void error(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    private void transform(boolean encrypt) {
        
        String input = (encrypt ? txt_plaintext : txt_ciphertext).getText();
        if (input.isEmpty()) { JOptionPane.showMessageDialog(this, encrypt ? "Hãy nhập Plaintext trước khi mã hóa." : "Hãy nhập Ciphertext trước khi giải mã."); return; }
        try {
            String output = encrypt ? AESCTRCipher.encrypt(input, txt_key.getText() , txt_nonce.getText()) : AESCTRCipher.decrypt(input, txt_key.getText() , txt_nonce.getText());
            (encrypt ? txt_ciphertext : txt_plaintext).setText(output);
            String saved = HistoryDao.tryInsert("AES-CTR", encrypt ? "ENCRYPT" : "DECRYPT", null, input.length(), output.length());
            lbl_status.setText("<html>" + (encrypt ? "Đã mã hóa " : "Đã giải mã ") + "AES-CTR. " + saved  + "<br>Plaintext: " + CipherUtil.utf8(encrypt ? input : output).length + " byte; ciphertext (giải Base64): " + CipherUtil.fromB64(encrypt ? output : input).length + " byte." + "</html>");
        } catch (IllegalArgumentException ex) { error(ex); }
        catch (Exception ex) { error(new Exception((encrypt ? "Mã hóa thất bại: " : "Giải mã thất bại: sai Key/IV hoặc bản mã bị sửa. ") + ex.getMessage())); }
    }
    private void saveFile() {
        if (txt_ciphertext.getText().isBlank()) { JOptionPane.showMessageDialog(this,"Chưa có bản mã để lưu."); return; }
        String path = FileTextIO.saveString(this, AESCTRCipher.toFile(txt_nonce.getText(), txt_ciphertext.getText()), "aes_ctr_ciphertext.txt"); if (path != null) lbl_status.setText("Đã lưu IV/Nonce và bản mã: " + path);
    }
    private void openFile() {
        String content = FileTextIO.openString(this);
        if (content == null) return;
        try { String[] parts = AESCTRCipher.fromFile(content); txt_nonce.setText(parts[0]); txt_ciphertext.setText(parts[1]); lbl_status.setText("Đã khôi phục IV/Nonce và bản mã. Nhập đúng Key để giải mã."); } catch (IllegalArgumentException ex) { error(ex); }
    }
    
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new frm_AES_CTR().setVisible(true)); }
}
