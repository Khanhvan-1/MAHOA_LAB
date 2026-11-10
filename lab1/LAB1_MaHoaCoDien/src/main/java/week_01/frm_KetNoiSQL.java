package week_01;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Arrays;
import javax.swing.*;

public class frm_KetNoiSQL extends JFrame {

    private final JTextField txt_server = new JTextField("127.0.0.1");
    private final JTextField txt_port = new JTextField("14333");
    private final JTextField txt_database =
            new JTextField("LAB1_MaHoaCoDien");
    private final JTextField txt_user = new JTextField("lab1_user");
    private final JPasswordField txt_password = new JPasswordField();

    private final JButton btn_test = new JButton("Test Connection");
    private final JButton btn_history = new JButton("Xem lịch sử");
    private final JLabel lbl_status = new JLabel("Chưa kết nối.");

    public frm_KetNoiSQL() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Kết nối Microsoft SQL Server");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(10, 12));
        content.setBorder(
                BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("KẾT NỐI MICROSOFT SQL SERVER");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        content.add(title, BorderLayout.NORTH);

        JPanel labels = new JPanel(new GridLayout(5, 1, 8, 10));
        JPanel inputs = new JPanel(new GridLayout(5, 1, 8, 10));

        labels.add(new JLabel("Server:"));
        labels.add(new JLabel("Port:"));
        labels.add(new JLabel("Database:"));
        labels.add(new JLabel("User:"));
        labels.add(new JLabel("Password:"));

        inputs.add(txt_server);
        inputs.add(txt_port);
        inputs.add(txt_database);
        inputs.add(txt_user);
        inputs.add(txt_password);

        JPanel fields = new JPanel(new BorderLayout(12, 0));
        fields.add(labels, BorderLayout.WEST);
        fields.add(inputs, BorderLayout.CENTER);
        content.add(fields, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(btn_test);
        buttons.add(btn_history);

        lbl_status.setPreferredSize(new Dimension(520, 80));

        JPanel bottom = new JPanel(new BorderLayout(0, 8));
        bottom.add(buttons, BorderLayout.NORTH);
        bottom.add(lbl_status, BorderLayout.CENTER);
        content.add(bottom, BorderLayout.SOUTH);

        btn_test.addActionListener(e -> testConnection());
        btn_history.addActionListener(e ->
                new frm_LichSu().setVisible(true));

        setContentPane(content);
        pack();
        setMinimumSize(getSize());
    }

    private void testConnection() {
        char[] password = txt_password.getPassword();

        try {
            int port = Integer.parseInt(txt_port.getText().trim());

            DbConfig.set(
                    txt_server.getText().trim(),
                    port,
                    txt_database.getText().trim(),
                    txt_user.getText().trim(),
                    password);

            lbl_status.setText("<html>" + DbConfig.test() + "</html>");
        } catch (Exception ex) {
            DbConfig.clear();
            lbl_status.setText(
                    "<html>Kết nối thất bại: " + ex.getMessage() + "</html>");

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Không kết nối được SQL Server",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            Arrays.fill(password, '\0');
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new frm_KetNoiSQL().setVisible(true));
    }
}