package tableUtils;

import utils.Tasks;
import java.util.List;

public class Controller {

    Data data = new Data();

    public List<Tasks> getTask() {
        return data.getData();
    }

    public void addTask(FormEvent ev) {
        Tasks task = new Tasks(ev.getTaskTitle(), ev.getDesc(), ev.getDate(), ev.getPriority(), ev.getStatus(), ev.getProjectTitle());
        data.addTask(task);
    }

    public void removeTask(int index) {
        data.removeTask(index);
    }
}
