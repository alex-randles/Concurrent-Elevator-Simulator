import java.util.*;
import  java.io.*; 
import java.util.concurrent.atomic.AtomicInteger;
// one access to thread at a time 
//  not thread that is synchronized, its the method acting on it
// monitor controls object of a thread, even though its not one 
// deadlock - 2 or more threads become interlocked 
import java.util.*; 
public class Person extends Thread{
	static final AtomicInteger idGenerator = new AtomicInteger(0);   
    int arrivalTime;
	int id; 
    int arrivalFloor;
    int destinationFloor;
    int baggageWeight;
    int personWeight;
    ElevatorController elevatorController;
    Random random = new Random();
    



    public Person(ElevatorController elevatorController){
        this.id =  idGenerator.getAndIncrement();;
         this.arrivalTime = time();
        this.arrivalFloor =  arrivalFloor(); 
        this.destinationFloor = destinationFloor();
        /* testing
        this.arrivalTime = 3;
        this.arrivalFloor =  6; 
        this.destinationFloor = 9;*/
        this.baggageWeight = luggage();
        this.personWeight = passengerWeight();
        this.random = new Random();
        this.elevatorController = elevatorController;
        id++;
    }

    public int getCurrentFloor(){
         return arrivalFloor; 

    }



    public int passengerWeight(){
        return random.nextInt((100-40)+1)+40;
        // 40 - 100 kg - 1000kg max
    }

    public int arrivalFloor(){
        return  random.nextInt((10-0)+1)+0;
    }
    
    public int destinationFloor(){
		int randomFloor = random.nextInt((10-0)+1)+0;
		while(this.arrivalFloor == randomFloor){
			randomFloor = random.nextInt((10-0)+1)+0;
		}
		
		return randomFloor; 
	}
	
	public int time(){
		 return random.nextInt((10-0)+1)+0;

	}

    public int luggage(){
        return random.nextInt((30-0)+1)+0;
        // 0 - 30kg 
    }
    
    public int getTotalWeight(){
		return  this.baggageWeight + this.personWeight;
	}

    public String toString(){
		String personalDetails = String.format("id: %s, with weight %d arrivalFloor: %s, destinationFloor: %s and arrivalTime: %s",id, getTotalWeight(), arrivalFloor, destinationFloor, arrivalTime);
		return personalDetails; 
		
		
	}
	
   public void writeOutput() throws IOException{
		String outputStr = String.format("Person (Thread ID) %s makes request at time %s starting at floor %s with the destination floor %s.\n", this.id, this.arrivalTime, this.arrivalFloor, this.destinationFloor); 
		/*BufferedWriter writer = new BufferedWriter(new FileWriter("output.dat"));
		PrintWriter pw = new PrintWriter(writer);
		pw.println(outputStr);
		writer.close();*/
		

    	  
    	  try{
			  
			 File file =new File("output.dat");
    	     if(!file.exists()){
    	 	     file.createNewFile();
    	    }
			  FileWriter fileWritter = new FileWriter(file.getName(),true);
			  BufferedWriter bw = new BufferedWriter(fileWritter);
              bw.write(outputStr);
              bw.close();
    	  
    	  }
    	 catch(IOException e){
    	   System.out.println("Exception occurred:");
    	   e.printStackTrace();
      }
		
	}
    

    public void run(){
        try{
			writeOutput(); 
            elevatorController.makeRequest(this);
        }
        catch(Exception e){


        }

    }

}



