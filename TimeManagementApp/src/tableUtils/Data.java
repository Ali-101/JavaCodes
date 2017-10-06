package tableUtils;

import utils.Tasks;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Data {

    private List<Tasks> data;

    public Data() {
        this.data = new LinkedList<>();
    }

    public void addTask(Tasks task) {
        data.add(task);
    }

    public List<Tasks> getData() {
        return Collections.unmodifiableList(data);
    }

    public void removeTask(int index) {
        data.remove(index);
    }
    
}
