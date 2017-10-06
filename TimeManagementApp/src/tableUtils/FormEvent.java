package tableUtils;

import java.util.EventObject;

public class FormEvent extends EventObject {

    private String taskTitle;
    private String projectTitle;
    private String desc;
    private String date;
    private int priority;
    private boolean status;

    public FormEvent(Object source) {
        super(source);
    }

    public FormEvent(Object source, String taskTitle, String projectTitle, String desc, String date, int priority, boolean status) {
        super(source);
        this.taskTitle = taskTitle;
        this.projectTitle = projectTitle;
        this.desc = desc;
        this.priority = priority;
        this.date = date;
        this.status = status;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public String getDesc() {
        return desc;
    }

    public String getDate() {
        return date;
    }

    public int getPriority() {
        return priority;
    }

    public boolean getStatus() {
        return status;
    }

}
