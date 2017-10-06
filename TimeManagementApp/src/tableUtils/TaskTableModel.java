package tableUtils;

import dataBase.EditTask;
import utils.Tasks;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TaskTableModel extends AbstractTableModel {

    private String[] colName = {"Project_Title", "Task_Title", "Description", "Date", "Priority", "Achieving-status"};
    private List<Tasks> data;

    @Override
    public String getColumnName(int column) {
        return colName[column];
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Tasks task = data.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return task.getProjectTitle();
            case 1:
                return task.getTaskTitle();
            case 2:
                return task.getDesc();
            case 3:
                return task.getDate();
            case 4:
                return task.getPriority();
            case 5:
                return task.getStatus();
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return false;
            case 1:
                return true;
            case 2:
                return true;
            case 3:
                return true;
            case 4:
                return true;
            case 5:
                return true;
        }
        return false;
    }
    
    @Override
    public void setValueAt(Object val, int rowIndex, int columnIndex) {
        if (data == null) {
            return;
        }
        Tasks task = data.get(rowIndex);
        switch (columnIndex) {
            case 1:
                String preTitle = task.getTaskTitle();
                task.setTaskTitle((String) val);
                EditTask.editTaskTitle(task.getTaskTitle(), preTitle);
                break;
            case 2:
                task.setDesc((String) val);
                EditTask.editDesc(task.getDesc(), task.getTaskTitle());
                break;
            case 3:
                task.setDate((String) val);
                EditTask.editDate(task.getDate(), task.getTaskTitle());
                break;
            case 4:
                task.setPriority((Integer) val);
                EditTask.editPriority(task.getPriority(), task.getTaskTitle());
                break;
            case 5:
                task.setStatus((Boolean) val);
                EditTask.editStatus(task.getStatus(), task.getTaskTitle());
                break;
        }
    }


    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;

            case 1:
                return String.class;

            case 2:
                return String.class;

            case 3:
                return String.class;

            case 4:
                return Integer.class;

            case 5:
                return Boolean.class;  //here was String.class and i changed to Boolean.class 
        }
        return null;
    }

    public void setData(List<Tasks> data) {
        this.data = data;
    }
}
