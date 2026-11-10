package week_01;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class frm_TongHop extends JFrame {

    private final JComboBox<String> cbo_algorithm =
            new JComboBox<>(CipherTongHop.ALGORITHMS);

    private final JLabel lbl_hint = new JLabel("Khóa mẫu: 3");
    private final JTextArea txt_plaintext = new JTextArea(3, 50);
    private final JTextField txt_key = new JTextField("3", 16);
    private final JTextArea txt_ciphertext = new JTextArea(3, 50);
    private final JPanel pnl_matrix = new JPanel(new BorderLayout(10, 5));
    private final JTextArea txt_matrix = new JTextArea(5, 9);
    private final JButton btn_matrix = new JButton("Generate Key Matrix");

    private final JButton btn_encrypt = new JButton("Encrypt");
    private final JButton btn_decrypt = new JButton("Decrypt");
    private final JButton btn_open = new JButton("Open File");
    private final JButton btn_save = new JButton("Save File");
    private final JButton btn_sql = new JButton("Kết nối SQL Server");
    private final JButton btn_history = new JButton("Xem lịch sử");

    private final JLabel lbl_status = new JLabel("Sẵn sàng.");

    public frm_TongHop() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Chương trình tổng hợp - 6 thuật toán");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(10, 12));
        content.setBorder(
                BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel(
                "CHƯƠNG TRÌNH TỔNG HỢP 6 THUẬT TOÁN CỔ ĐIỂN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        content.add(title, BorderLayout.NORTH);

        txt_plaintext.setLineWrap(true);
        txt_plaintext.setWrapStyleWord(true);
        txt_ciphertext.setLineWrap(true);
        txt_ciphertext.setWrapStyleWord(true);

        JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));

        JPanel algorithmPanel =
                new JPanel(new FlowLayout(FlowLayout.LEFT));
        algorithmPanel.add(new JLabel("Thuật toán:"));
        algorithmPanel.add(cbo_algorithm);
        algorithmPanel.add(lbl_hint);

        JPanel plainPanel = new JPanel(new BorderLayout(0, 5));
        plainPanel.add(new JLabel("Plaintext:"), BorderLayout.NORTH);
        plainPanel.add(
                new JScrollPane(txt_plaintext), BorderLayout.CENTER);

        JPanel keyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        keyPanel.add(new JLabel("Key:"));
        keyPanel.add(txt_key);

        txt_matrix.setEditable(false);
        txt_matrix.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        pnl_matrix.add(new JLabel("Key Matrix (I/J dùng chung ô):"), BorderLayout.NORTH);
        pnl_matrix.add(new JScrollPane(txt_matrix), BorderLayout.CENTER);
        JPanel matrixButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        matrixButtons.add(btn_matrix);
        pnl_matrix.add(matrixButtons, BorderLayout.SOUTH);
        pnl_matrix.setVisible(false);

        JPanel cipherPanel = new JPanel(new BorderLayout(0, 5));
        cipherPanel.add(new JLabel("Ciphertext:"), BorderLayout.NORTH);
        cipherPanel.add(
                new JScrollPane(txt_ciphertext), BorderLayout.CENTER);

        fields.add(algorithmPanel);
        fields.add(Box.createVerticalStrut(8));
        fields.add(plainPanel);
        fields.add(Box.createVerticalStrut(8));
        fields.add(keyPanel);
        fields.add(pnl_matrix);
        fields.add(Box.createVerticalStrut(8));
        fields.add(cipherPanel);

        content.add(fields, BorderLayout.CENTER);

        JPanel cipherButtons =
                new JPanel(new FlowLayout(FlowLayout.LEFT));
        cipherButtons.add(btn_encrypt);
        cipherButtons.add(btn_decrypt);
        cipherButtons.add(btn_open);
        cipherButtons.add(btn_save);

        JPanel sqlButtons =
                new JPanel(new FlowLayout(FlowLayout.LEFT));
        sqlButtons.add(btn_sql);
        sqlButtons.add(btn_history);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));

        cipherButtons.setAlignmentX(LEFT_ALIGNMENT);
        sqlButtons.setAlignmentX(LEFT_ALIGNMENT);
        lbl_status.setAlignmentX(LEFT_ALIGNMENT);

        bottom.add(cipherButtons);
        bottom.add(sqlButtons);
        bottom.add(Box.createVerticalStrut(8));
        bottom.add(lbl_status);

        content.add(bottom, BorderLayout.SOUTH);

        cbo_algorithm.addActionListener(e -> {
            String algorithm =
                    (String) cbo_algorithm.getSelectedItem();
            String sample = CipherTongHop.sampleKey(algorithm);
            txt_key.setText(sample);
            lbl_hint.setText("Khóa mẫu: " + sample);
            pnl_matrix.setVisible("Playfair".equals(algorithm));
            refreshMatrix(false);
            pack();
        });

        btn_matrix.addActionListener(e -> refreshMatrix(true));
        txt_key.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshMatrix(false); }
            public void removeUpdate(DocumentEvent e) { refreshMatrix(false); }
            public void changedUpdate(DocumentEvent e) { refreshMatrix(false); }
        });

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
                    this, txt_ciphertext, "ciphertext.txt");
            if (path != null) {
                lbl_status.setText("Đã lưu tệp: " + path);
            }
        });

        btn_sql.addActionListener(e ->
                new frm_KetNoiSQL().setVisible(true));

        btn_history.addActionListener(e ->
                new frm_LichSu().setVisible(true));

        setContentPane(content);
        pack();
        setMinimumSize(getSize());
    }

    private void refreshMatrix(boolean showMessage) {
        if (!"Playfair".equals(cbo_algorithm.getSelectedItem())) {
            txt_matrix.setText("");
            return;
        }
        try {
            txt_matrix.setText(PlayfairCipher.getKeyMatrix(txt_key.getText()));
            txt_matrix.setCaretPosition(0);
            if (showMessage) {
                lbl_status.setText("Đã tạo ma trận khóa 5x5 (I và J dùng chung ô).");
            }
        } catch (IllegalArgumentException ex) {
            txt_matrix.setText("");
            if (showMessage) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Khóa không hợp lệ", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void encrypt() {
        String algorithm = (String) cbo_algorithm.getSelectedItem();
        String input = txt_plaintext.getText();

        try {
            String result = CipherTongHop.encrypt(
                    algorithm, input, txt_key.getText());

            txt_ciphertext.setText(result);

            String saved = HistoryDao.tryInsert(
                    algorithm, "ENCRYPT", input, result);

            lbl_status.setText("Đã mã hóa " + algorithm + ". " + saved);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Dữ liệu không hợp lệ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void decrypt() {
        String algorithm = (String) cbo_algorithm.getSelectedItem();
        String input = txt_ciphertext.getText();

        try {
            String result = CipherTongHop.decrypt(
                    algorithm, input, txt_key.getText());

            txt_plaintext.setText(result);

            String saved = HistoryDao.tryInsert(
                    algorithm, "DECRYPT", input, result);

            lbl_status.setText("Đã giải mã " + algorithm + ". " + saved);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Dữ liệu không hợp lệ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new frm_TongHop().setVisible(true);
        });
    }
}
