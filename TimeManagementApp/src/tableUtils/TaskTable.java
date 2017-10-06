package tableUtils;

import dataBase.RetrieveDataFromDatabase;
import utils.Tasks;
import interfaces.TaskTableListener;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TaskTable extends JPanel {

    private JTable table;
    private TaskTableModel tableModel;
    private TaskTableListener tableListener;
    private JPopupMenu popup;

    public TaskTable() {
        tableModel = new TaskTableModel();
        table = new JTable(tableModel);
        popup = new JPopupMenu();
        JMenuItem removeTask = new JMenuItem(" Delete Task ");
        popup.add(removeTask);
        table.setRowHeight(25);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                table.getSelectionModel().setSelectionInterval(row, row);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    popup.show(table, e.getX(), e.getY());
                }
            }
        });
        removeTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (tableListener != null) {
                    tableListener.deleteTask(row);
                    tableModel.fireTableRowsDeleted(row, row);
                }
            }
        });

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        centerDataInTable();
    }

    public void refresh() {
        tableModel.fireTableDataChanged();
    }

    public void setTableListener(TaskTableListener listener) {
        this.tableListener = listener;
    }

    public void setData(List<Tasks> data) {
        tableModel.setData(data);
    }

    public void setTableData(String projectName) throws SQLException {
        ResultSet rs = null;
        LinkedList<Tasks> data1 = new LinkedList<>();
        rs = RetrieveDataFromDatabase.retrieveTasks(projectName);
        while (rs.next()) {
            data1.add(new Tasks(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getBoolean(5), projectName));
        }
        tableModel.setData(data1);
        refresh();
    }

    public void centerDataInTable() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(String.class, centerRenderer);
        table.setDefaultRenderer(Integer.class, centerRenderer);
    }

}
