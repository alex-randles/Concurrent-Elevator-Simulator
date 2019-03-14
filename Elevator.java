// synchronization will not take place here 
// it will in the elevator controller, controlling the class 
import java.util.concurrent.*;
import java.util.*; 

public class Elevator extends Thread{
   private int maxFloor;
   private int minFloor;
   public static int currentWeight;
   private int maxWeight; 
   private ElevatorController elevatorController; 
   public Elevator(ElevatorController elevatorController){
      // this will control the actions of the elevator thread
      // in a synchronized manner 
      this.elevatorController = elevatorController; 
      this.maxFloor = 10;
      this.minFloor = 0; 
      this.currentWeight = 0; 
      this.maxWeight = 4; 
   }
   
   
   
   public void run(){
   try{
	   
	   while(true){
		  {
		    
		  // checkWeight(); 
		   // time is updated 1 on each floor 
		  // System.out.println("Time: " + elevatorController.currentTime); 
		 // sleepWhileEmpty(); 
		   elevatorController.acceptRequest(); 
		  // System.out.println(elevatorController.request); 
		  // System.out.println(elevatorController.waiting); 
		   elevatorController.enterElevator(); 	
		   elevatorController.exitElevator(); 	
		   elevatorController.currentTime++; 
		   elevatorController.changeFloor(minFloor,maxFloor);
		   System.out.printf("Current Time: %s\n", elevatorController.currentTime); 
          
          
		  // System.out.printf("There is %s people in the elevator\n", elevatorController.numPeople); 
		  // System.out.printf("Elevator is on floor %s\n",elevatorController.currentFloor); 	   
		   // sleep for 100 milliseconds between floors   
		  // System.out.println(elevatorController.inElevator); 
		   Thread.sleep(1000); 
		   
		   
		   
 
		    
	   
		   
	   
	   
	   
	   
					}
			}
     
     }
	  
	 catch(Exception e){
		 System.out.println("lift not working"); 
		 e.printStackTrace(); 
		 
	 }
	 

   }

   
   public void checkWeight() throws InterruptedException{
	   if (this.currentWeight > this.maxWeight)
	   { 
		   System.out.printf("Warning the elevator is too heavy with weight: %s and max weight %s!", currentWeight, maxWeight); 
		   Thread.sleep(1000);
		}
   }
   
   public synchronized void sleepWhileEmpty() throws InterruptedException{
            
            synchronized(this){
				
				while(elevatorController.waiting.isEmpty()){
					wait(); 
				}
				
				notifyAll(); 
			}

   }

}
