// synchronization will not take place here 
// it will in the elevator controller, controlling the class 
import java.util.concurrent.*;
import java.util.*; 

public class Elevator extends Thread{
   private int maxFloor;
   private int minFloor;
   private ElevatorController elevatorController; 
   public Elevator(ElevatorController elevatorController){
      // this will control the actions of the elevator thread
      // in a synchronized manner 
      this.elevatorController = elevatorController; 
      this.maxFloor = 10;
      this.minFloor = 0; 
   }
   
   
   
   public void run(){
   try{
	   
	   while(true){
		  {
		   elevatorController.enterElevator(); 	
		   elevatorController.exitElevator(); 	
		   elevatorController.changeFloor(minFloor,maxFloor); 
		   System.out.printf("Elevator is on floor %s\n",elevatorController.currentFloor); 	   
		   // sleep for 100 milliseconds between floors   
		  // System.out.println(elevatorController.inElevator); 
		   Thread.sleep(100); 
		   
 
		    
	   
		   
	   
	   
	   
	   
	  }
     }
     
 }
	  
	 catch(Exception e){
		 System.out.println("lift not working"); 
		 e.printStackTrace(); 
		 
	 }
	 

}






}
