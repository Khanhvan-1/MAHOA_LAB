package week_03;
import javax.swing.*;
import java.awt.*;

final class LabUI {
    private LabUI() {}
    static JPanel column() {
        JPanel p=new JPanel(); p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(14,18,14,18)); return p;
    }
    static JTextArea area(int rows, boolean editable) {
        JTextArea a=new JTextArea(rows,58); a.setLineWrap(true); a.setWrapStyleWord(true);
        a.setEditable(editable); return a;
    }
    static void field(JPanel p,String title,JTextArea a) {
        p.add(new JLabel(title)); p.add(new JScrollPane(a)); p.add(Box.createVerticalStrut(8));
    }
    static JPanel row(Component... c) {
        JPanel p=new JPanel(new FlowLayout(FlowLayout.LEFT,7,4));
        for(Component x:c) p.add(x); return p;
    }
    static JButton button(String title,Runnable action) {
        JButton b=new JButton(title); b.addActionListener(e->action.run()); return b;
    }
    static void title(JPanel p,String title) {
        JLabel l=new JLabel(title); l.setFont(l.getFont().deriveFont(Font.BOLD,20f));
        p.add(l); p.add(Box.createVerticalStrut(12));
    }
    static void finish(JFrame f,JPanel p) {
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); f.setContentPane(p);
        f.pack(); f.setLocationRelativeTo(null);
    }
    static void error(Component parent,String message,Exception ex) {
        JOptionPane.showMessageDialog(parent,message+"\n"+ex.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE);
    }
}
