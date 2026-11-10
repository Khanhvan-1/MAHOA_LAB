package week_01;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.SQLException;
import javax.swing.*;

public class frm_LichSu extends JFrame {

    private final JTable tbl_history = new JTable();
    private final JButton btn_refresh = new JButton("Tải lại");
    private final JLabel lbl_status = new JLabel("Đang tải...");

    public frm_LichSu() {
        initComponents();
        setLocationRelativeTo(null);
        tbl_history.setModel(HistoryDao.emptyModel());
        loadData();
    }

    private void initComponents() {
        setTitle("Lịch sử thao tác (SQL Server)");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(10, 12));
        content.setBorder(
                BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("LỊCH SỬ THAO TÁC - BẢNG DBO.HISTORY");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        content.add(title, BorderLayout.NORTH);

        tbl_history.setRowHeight(26);
        tbl_history.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(tbl_history);
        scrollPane.setPreferredSize(new Dimension(900, 300));
        content.add(scrollPane, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(10, 0));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(btn_refresh);

        bottom.add(buttons, BorderLayout.WEST);
        bottom.add(lbl_status, BorderLayout.CENTER);
        content.add(bottom, BorderLayout.SOUTH);

        btn_refresh.addActionListener(e -> loadData());

        setContentPane(content);
        pack();
        setMinimumSize(getSize());
    }

    private void loadData() {
        try {
            tbl_history.setModel(HistoryDao.loadLatest(50));

            lbl_status.setText(
                    "Đã tải " + tbl_history.getRowCount()
                    + " dòng mới nhất từ dbo.history.");
        } catch (SQLException ex) {
            lbl_status.setText(
                    "Không đọc được lịch sử: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new frm_LichSu().setVisible(true));
    }
}