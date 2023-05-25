package spaceinvaders;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

public class GameTimer extends Timer {
    private List<TimerTask> taskArray = new ArrayList<>();
    public void addTask(TimerTask task,long delay, long period){
        taskArray.add(task);
        if (period == 0) this.schedule(task,delay);
        else this.schedule(task,delay,period);
    }
    public boolean isTaskExist(TimerTask task){
        return taskArray.contains(task);
    }
    public void removeTask(TimerTask task){
        (taskArray.get(taskArray.indexOf(task))).cancel();
        taskArray.remove(task);
    }
}
