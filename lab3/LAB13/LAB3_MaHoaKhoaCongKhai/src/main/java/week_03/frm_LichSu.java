package week_03;
import javax.swing.*;
import java.awt.*;

public class frm_LichSu extends JFrame {
    private final JTable tbl_history=new JTable(HistoryDao.emptyModel());
    private final JLabel lbl_status=new JLabel("Đang tải...");
    public frm_LichSu() {
        super("Lịch sử thao tác (SQL Server)"); JPanel p=LabUI.column();
        LabUI.title(p,"LỊCH SỬ THAO TÁC - BẢNG DBO.HISTORY");
        tbl_history.setRowHeight(25); JScrollPane sp=new JScrollPane(tbl_history);
        sp.setPreferredSize(new Dimension(840,330)); p.add(sp);
        p.add(LabUI.row(LabUI.button("Tải lại",this::loadData),lbl_status));
        LabUI.finish(this,p); loadData();
    }
    private void loadData(){
        try{tbl_history.setModel(HistoryDao.loadLatest(50));
            lbl_status.setText("Đã tải "+tbl_history.getRowCount()+" dòng mới nhất từ dbo.History.");
        }catch(Exception ex){lbl_status.setText("Không đọc được lịch sử: "+ex.getMessage());}
    }
    public static void main(String[] args){SwingUtilities.invokeLater(()->new frm_LichSu().setVisible(true));}
}
