package week_02;

import java.awt.*;
import javax.swing.*;

/** Giao diện Swing cho bài TripleDES, chuyển từ hướng dẫn NetBeans sang VS Code. */
public class frm_TripleDES extends JFrame {
    private final JTextArea txt_plaintext = new JTextArea(4, 54);
    private final JTextArea txt_ciphertext = new JTextArea(4, 54);
    private final JTextField txt_key = new JTextField("123456789012345678901234", 32);
    private final JButton btn_encrypt = new JButton("Encrypt");
    private final JButton btn_decrypt = new JButton("Decrypt");
    private final JButton btn_save = new JButton("Save File");
    private final JButton btn_open = new JButton("Open File");
    private final JLabel lbl_status = new JLabel("Sẵn sàng.");
    

    public frm_TripleDES() {
        setTitle("TripleDES Cipher");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel content = new JPanel(new BorderLayout(10, 12));
        content.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        JLabel title = new JLabel("MẬT MÃ TRIPLEDES");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        content.add(title, BorderLayout.NORTH);
        JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
        
        txt_plaintext.setLineWrap(true); txt_plaintext.setWrapStyleWord(true);
        txt_ciphertext.setLineWrap(true);
        fields.add(area("Plaintext:", txt_plaintext));
        fields.add(row("Key:", txt_key ));
        fields.add(row("Đúng 24 ký tự không dấu (24 byte)"));
        
        fields.add(area("Ciphertext:", txt_ciphertext));
        fields.add(row(btn_encrypt, btn_decrypt, btn_save, btn_open));
        content.add(fields, BorderLayout.CENTER);
        lbl_status.setPreferredSize(new Dimension(720, 52));
        content.add(lbl_status, BorderLayout.SOUTH);
        btn_encrypt.addActionListener(e -> transform(true));
        btn_decrypt.addActionListener(e -> transform(false));
        btn_save.addActionListener(e -> saveFile());
        btn_open.addActionListener(e -> openFile());
        
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
            String output = encrypt ? TripleDESCipher.encrypt(input, txt_key.getText() ) : TripleDESCipher.decrypt(input, txt_key.getText() );
            (encrypt ? txt_ciphertext : txt_plaintext).setText(output);
            String saved = HistoryDao.tryInsert("TripleDES", encrypt ? "ENCRYPT" : "DECRYPT", null, input.length(), output.length());
            lbl_status.setText("<html>" + (encrypt ? "Đã mã hóa " : "Đã giải mã ") + "TripleDES. " + saved  + "</html>");
        } catch (IllegalArgumentException ex) { error(ex); }
        catch (Exception ex) { error(new Exception((encrypt ? "Mã hóa thất bại: " : "Giải mã thất bại: sai Key/IV hoặc bản mã bị sửa. ") + ex.getMessage())); }
    }
    private void saveFile() {
        if (txt_ciphertext.getText().isBlank()) { JOptionPane.showMessageDialog(this,"Chưa có bản mã để lưu."); return; }
        String path = FileTextIO.saveText(this, txt_ciphertext, "tripledes_ciphertext.txt"); if (path != null) lbl_status.setText("Đã lưu: " + path);
    }
    private void openFile() {
        String path = FileTextIO.openText(this, txt_ciphertext); if (path != null) lbl_status.setText("Đã mở: " + path);
    }
    
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new frm_TripleDES().setVisible(true)); }
}
