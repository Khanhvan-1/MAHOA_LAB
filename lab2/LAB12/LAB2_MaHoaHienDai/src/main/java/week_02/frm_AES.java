package week_02;

import java.awt.*;
import javax.swing.*;

/** Giao diện Swing cho bài AES, chuyển từ hướng dẫn NetBeans sang VS Code. */
public class frm_AES extends JFrame {
    private final JTextArea txt_plaintext = new JTextArea(4, 54);
    private final JTextArea txt_ciphertext = new JTextArea(4, 54);
    private final JTextField txt_key = new JTextField("", 32);
    private final JButton btn_encrypt = new JButton("Encrypt");
    private final JButton btn_decrypt = new JButton("Decrypt");
    private final JButton btn_save = new JButton("Save File");
    private final JButton btn_open = new JButton("Open File");
    private final JLabel lbl_status = new JLabel("Sẵn sàng.");
    private String currentUser;
    private final JTextField txt_username = new JTextField("sinhvien01", 24);
    private final JPasswordField txt_password = new JPasswordField(24);
    private final JTextField txt_ticket = new JTextField(36);
    private final JButton btn_sql = new JButton("Kết nối SQL Server");
    private final JButton btn_register = new JButton("Register");
    private final JButton btn_login = new JButton("Login");

    public frm_AES() {
        setTitle("AES Cipher - Register / Login");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel content = new JPanel(new BorderLayout(10, 12));
        content.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        JLabel title = new JLabel("AES-GCM - ĐĂNG KÝ / ĐĂNG NHẬP (SQL SERVER)");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        content.add(title, BorderLayout.NORTH);
        JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
        fields.add(row("Username:", txt_username));
        fields.add(row("Password:", txt_password, "(ít nhất 8 ký tự)"));
        fields.add(row(btn_sql, btn_register, btn_login));
        txt_ticket.setEditable(false); fields.add(row("Ticket:", txt_ticket));
        txt_plaintext.setLineWrap(true); txt_plaintext.setWrapStyleWord(true);
        txt_ciphertext.setLineWrap(true);
        fields.add(area("Plaintext:", txt_plaintext));
        fields.add(row("Key AES:", txt_key ));
        fields.add(row("Mật khẩu để tạo khóa AES-256"));
        
        fields.add(area("Ciphertext:", txt_ciphertext));
        fields.add(row(btn_encrypt, btn_decrypt, btn_save, btn_open));
        content.add(fields, BorderLayout.CENTER);
        lbl_status.setPreferredSize(new Dimension(720, 52));
        content.add(lbl_status, BorderLayout.SOUTH);
        btn_encrypt.addActionListener(e -> transform(true));
        btn_decrypt.addActionListener(e -> transform(false));
        btn_save.addActionListener(e -> saveFile());
        btn_open.addActionListener(e -> openFile());
        btn_sql.addActionListener(e -> new frm_KetNoiSQL().setVisible(true));
        btn_register.addActionListener(e -> authenticate(true));
        btn_login.addActionListener(e -> authenticate(false));
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
        if (currentUser == null || txt_ticket.getText().isEmpty()) { JOptionPane.showMessageDialog(this,"Hãy Login trước khi dùng AES."); return; }
        String input = (encrypt ? txt_plaintext : txt_ciphertext).getText();
        if (input.isEmpty()) { JOptionPane.showMessageDialog(this, encrypt ? "Hãy nhập Plaintext trước khi mã hóa." : "Hãy nhập Ciphertext trước khi giải mã."); return; }
        try {
            String output = encrypt ? AESCipher.encrypt(input, txt_key.getText() ) : AESCipher.decrypt(input, txt_key.getText() );
            (encrypt ? txt_ciphertext : txt_plaintext).setText(output);
            String saved = HistoryDao.tryInsert("AES", encrypt ? "ENCRYPT" : "DECRYPT", currentUser, input.length(), output.length());
            lbl_status.setText("<html>" + (encrypt ? "Đã mã hóa " : "Đã giải mã ") + "AES. " + saved  + "</html>");
        } catch (IllegalArgumentException ex) { error(ex); }
        catch (Exception ex) { error(new Exception((encrypt ? "Mã hóa thất bại: " : "Giải mã thất bại: sai Key/IV hoặc bản mã bị sửa. ") + ex.getMessage())); }
    }
    private void saveFile() {
        if (txt_ciphertext.getText().isBlank()) { JOptionPane.showMessageDialog(this,"Chưa có bản mã để lưu."); return; }
        String path = FileTextIO.saveText(this, txt_ciphertext, "aes_ciphertext.txt"); if (path != null) lbl_status.setText("Đã lưu: " + path);
    }
    private void openFile() {
        String path = FileTextIO.openText(this, txt_ciphertext); if (path != null) lbl_status.setText("Đã mở: " + path);
    }
    private void authenticate(boolean register) {
        char[] password = txt_password.getPassword();
        String user = txt_username.getText().trim();
        if (!register) { currentUser = null; txt_ticket.setText(""); }
        try {
            if (register) {
                UserDao.register(user, password);
                String saved = HistoryDao.tryInsert("AES", "REGISTER", user, 0, 0);
                lbl_status.setText("<html>Register thành công: " + user + ". Bấm Login. " + saved + "</html>");
            } else if (UserDao.login(user, password)) {
                currentUser = user; txt_ticket.setText(UserDao.newTicket());
                String saved = HistoryDao.tryInsert("AES", "LOGIN", user, 0, 0);
                lbl_status.setText("<html>Login thành công: " + user + ". Có thể dùng Encrypt/Decrypt. " + saved + "</html>");
            } else { lbl_status.setText("Login thất bại."); error(new Exception("Sai username hoặc mật khẩu.")); }
        } catch (Exception ex) { error(ex); }
        finally { java.util.Arrays.fill(password, '\0'); }
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new frm_AES().setVisible(true)); }
}
