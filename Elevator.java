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
	   
	   while(true && elevatorController.currentFloor < 11){
		  {
			System.out.printf("Elevator is on floor %s\n",elevatorController.currentFloor); 	   
		   elevatorController.enterElevator(); 	
		   elevatorController.exitElevator(); 	   
		   Thread.sleep(100); 
		   //System.out.println(elevatorController.goingUp);
		   
		   
		   if (elevatorController.goingUp == true){
			   
			   if(elevatorController.currentFloor == maxFloor){
				    elevatorController.currentFloor--;	   
				    elevatorController.goingUp = false; 
				    elevatorController.goingDown = true; 			    
			   }
		       else{
				   elevatorController.currentFloor++;
				}
	       }
			
		   else{
			   
			   if(elevatorController.currentFloor == minFloor){
					elevatorController.goingUp = true; 
				    elevatorController.goingDown = false; 	
				    elevatorController.currentFloor++;
		   
				 }
				else{
			      elevatorController.currentFloor--;
                 }
			   
			 }   
		    
	   
		   
	   
	   
	   
	   
	  }
     }
     
 }
	  
	 catch(Exception e){
		 System.out.println("lift not working"); 
		 e.printStackTrace(); 
		 
	 }
	 

}






}
