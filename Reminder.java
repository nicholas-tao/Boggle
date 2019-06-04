import java.util.Timer;
import java.util.TimerTask;

public class Reminder {
    Timer timer;
    
    //Reminder constructor that takes in seconds
    public Reminder(int seconds) {
        timer = new Timer();
        timer.schedule(new RemindTask(), seconds*1000); //schedule the reminder
    }

    //subclass RemindTask
    class RemindTask extends TimerTask {
        public void run() {
            System.out.println("Time's up!");
            timer.cancel(); //Terminate the timer thread
        }
    }

}



  