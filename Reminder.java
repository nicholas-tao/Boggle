import java.util.Timer;
import java.util.TimerTask;

public class Reminder {
    Timer timer;
    int time;
    int remaining = 15 - time;
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
    
    public void pause() {
      this.timer.cancel();
    }
    
    
    public void resume() {
      this.timer = new Timer();
      this.timer.schedule(new RemindTask(), 0, 1000 );
    }
    
    public int remain(){
      return remaining;
    }

}



  



  
