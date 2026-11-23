package week_03;
import javax.swing.*;
import java.awt.*;

public class frm_KetNoiSQL extends JFrame {
    private final JTextField txt_server=new JTextField("127.0.0.1",28);
    private final JTextField txt_port=new JTextField("14333",28);
    private final JTextField txt_database=new JTextField("LAB3_MaHoaKhoaCongKhai",28);
    private final JTextField txt_user=new JTextField("lab3_user",28);
    private final JPasswordField txt_password=new JPasswordField(28);
    private final JLabel lbl_status=new JLabel("Chưa kết nối.");
    public frm_KetNoiSQL() {
        super("Kết nối Microsoft SQL Server"); JPanel p=LabUI.column();
        LabUI.title(p,"KẾT NỐI MICROSOFT SQL SERVER");
        JPanel fields=new JPanel(new GridLayout(5,2,8,8));
        String[] labels={"Server:","Port:","Database:","User:","Password:"};
        JTextField[] inputs={txt_server,txt_port,txt_database,txt_user,txt_password};
        for(int i=0;i<labels.length;i++){fields.add(new JLabel(labels[i]));fields.add(inputs[i]);}
        p.add(fields);
        p.add(LabUI.row(LabUI.button("Test Connection",this::test),
            LabUI.button("Xem lịch sử",()->new frm_LichSu().setVisible(true))));
        lbl_status.setPreferredSize(new Dimension(660,65)); p.add(lbl_status); LabUI.finish(this,p);
    }
    private void test() {
        char[] password=txt_password.getPassword();
        try {
            DbConfig.set(txt_server.getText().trim(),Integer.parseInt(txt_port.getText().trim()),
                txt_database.getText().trim(),txt_user.getText().trim(),password);
            lbl_status.setText("<html>"+DbConfig.test()+"</html>");
        } catch(Exception ex) {
            DbConfig.clear(); lbl_status.setText("Kết nối thất bại.");
            LabUI.error(this,"Không kết nối được SQL Server",ex);
        } finally {java.util.Arrays.fill(password,'\0');}
    }
    public static void main(String[] args){SwingUtilities.invokeLater(()->new frm_KetNoiSQL().setVisible(true));}
}
