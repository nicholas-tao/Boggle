import java.util.*;
public class TimerTest {
  public static void main (String []args) {
    Scanner sc = new Scanner (System.in);
    long remaining = 15000000000L;
    while (remaining> 0) {
      long currentTime = System.nanoTime();
      long stopTime = remaining + currentTime; 
      if (currentTime == stopTime) {
        //textField.setVisble(false);
        System.out.println("times up");
      }
      String word = sc.nextLine();
      long elapsedTime =  System.nanoTime() - currentTime;
      remaining-=elapsedTime;
      
    }
  }
  
}
