// synchronization will not take place here 
// it will in the elevator controller, controlling the class 
import java.util.concurrent.*;
import java.util.*; 
import  java.util.concurrent.atomic.AtomicBoolean; 
import java.io.*; 



public class Elevator extends Thread{
   private int maxFloor;
   private int minFloor;
   private ElevatorController elevatorController; 
   private Elevator backUpElevator; 
   private volatile AtomicBoolean faulty;
   public Elevator(ElevatorController elevatorController){
      // this will control the actions of the elevator thread
      // in a synchronized manner 
      this.elevatorController = elevatorController; 
      this.maxFloor = 10;
      this.minFloor = 0; 
      this.faulty =  new AtomicBoolean(false);
      //this weightLimit = 2; 
   }
   
   
   
   public void run(){
   try{
	   
	   writeOutput(); 
	   while(!(faulty.get())){
		  {
		    
		    

			 
			 
			 

		    elevatorController.acceptRequest(); 
		   //System.out.println(elevatorController.waiting); 
		   elevatorController.enterElevator(); 	
		   elevatorController.exitElevator(); 	
		   elevatorController.currentTime++; 
		   elevatorController.changeFloor(minFloor,maxFloor);
		   System.out.printf("Current weight is %s **********\n", elevatorController.currentWeight); 
		   System.out.printf("Current Time: %s\n", elevatorController.currentTime); 
		   // sleep for 100 between floors
		   Thread.sleep(1000); 
		  // randomFault(); 
		   
		}
	   }
     
     }
	  
	 catch(Exception e){
		 System.out.println("lift not working"); 
		 e.printStackTrace(); 
		 
	 }
	 

   }




   public synchronized void randomFault() {
	   
	   
	   if(elevatorController.getTime() == 4){
				System.out.println("**********FAULT PEOPLE BEING TRANSFERRED TO ANOTHER ELEVATOR***********"); 
				System.out.println("**********Starting backup elevator from floor 1************************"); 
				ElevatorController backUpElevatorController = new ElevatorController(); 
				elevatorController.transferPeople(elevatorController, backUpElevatorController); 
				backUpElevator = new Elevator(backUpElevatorController);
				backUpElevator.start(); 
				stopCurrentThread(); 
				
		}


	  
	   
   }
   
   public synchronized void stopCurrentThread(){
	    this.faulty.set(true); 
   }

   public synchronized void sleepWhileEmpty() throws InterruptedException{
            
            synchronized(this){
				
				while(elevatorController.request.isEmpty()){
					wait(); 
				}
				
				notifyAll(); 
			}

   }
   
   public void writeOutput() throws IOException{
		// this will output when a new elevator has started 
		String headerStars = "*********************************************************************************************";					
		String headText = " 						The elevator has started					"; 		
		String heading = String.format("%s\n%s\n%s\n", headerStars, headText,headerStars);

    	  
    	  try{
			  
			 File file =new File("output.dat");
    	     if(!file.exists()){
    	 	     file.createNewFile();
    	    }
			  FileWriter fileWritter = new FileWriter(file.getName(),true);
			  BufferedWriter bw = new BufferedWriter(fileWritter);
              bw.write(heading);
              bw.close();
    	  
    	  }
    	 catch(IOException e){
    	   System.out.println("Exception occurred:");
    	   e.printStackTrace();
      }
		
	}
   

}
