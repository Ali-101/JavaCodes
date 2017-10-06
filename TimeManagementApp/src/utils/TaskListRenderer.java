package utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class TaskListRenderer implements ListCellRenderer {

    private JLabel label;
    private JPanel panel;
    private Color selected;
    private Color normal;

    public TaskListRenderer() {
        label = new JLabel();
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(label);
        label.setFont(new Font("Serif", Font.BOLD, 18));
        label.setIcon(Utils.createImage("/images/25.png"));
        selected = new Color(210, 210, 255);
        normal = Color.WHITE;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Tasks task = (Tasks) value;
        label.setText("  " + task.getTaskTitle());
        panel.setBackground(cellHasFocus ? selected : normal);
        return panel;
    }

}
