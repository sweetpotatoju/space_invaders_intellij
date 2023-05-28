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
    public TimerTask getTask(TimerTask task){
        if(taskArray.indexOf(task)==-1)return null;
        else return taskArray.get(taskArray.indexOf(task));
    }
    public void removeTask(TimerTask task){
        TimerTask tmp=taskArray.get(taskArray.indexOf(task));
        (taskArray.get(taskArray.indexOf(task))).cancel();
        System.out.println(task);
        System.out.println(tmp);
        taskArray.remove(task);
    }
}
