// this clas will allow sychronized access to the elevator thread 
// will control the actions of the elevator 
import java.util.concurrent.*;
import java.util.*; 


public class ElevatorController{
    public boolean goingUp; 
    public boolean goingDown; 
    public int currentFloor; 
    private ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> request; 
     private ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> inElevator;   
    // the request hashmap maps the request to the floor they are at 
    // the person making the request will join the linked queue for that floor 
    // (hashmap) {floor 0 -> linkedQueue{0 : [person1 - > person 2 -> 3}
    
    public ElevatorController(){
		this.goingUp = true; 
		this.goingDown = false; 
		this.request = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>>(); 
		this.inElevator = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>>(); 
		this.currentFloor = 0; 
		
	}
	
	public synchronized void addPerson(Person person, int floor, ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> hashmap){
		ConcurrentLinkedQueue<Person> tmp = new ConcurrentLinkedQueue<Person>(); 
		if (hashmap.containsKey(floor)){
			tmp = hashmap.get(floor);
			tmp.add(person);
			hashmap.put(floor, tmp); 
		}
		else{
			tmp.add(person); 
			hashmap.put(floor, tmp); 
			
		}
		
	}
	
	public synchronized Person removeAPerson(int floor, ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> hashmap) {
		ConcurrentLinkedQueue<Person> tmp = new ConcurrentLinkedQueue<Person>(); 
		if (hashmap.containsKey(floor)){
			tmp = hashmap.get(floor);
			Person personRemoved = tmp.poll();
			hashmap.put(floor, tmp); 
			if (hashmap.get(floor).isEmpty()){
				hashmap.remove(floor); 
			}
			return personRemoved; 
		}
		
		return null; 
		
	}
	public synchronized void makeRequest(Person person){
		addPerson(person,person.arrivalFloor, request); 
		System.out.println(this.request); 
		
	}
	
	public synchronized void openDoors() throws InterruptedException{

		// while there is still people in/waiting on elevator 
	    while(request.get(currentFloor)==null){
			
			wait() ;
		}
		
		// allow people  in the elevator out at there floors 
		ConcurrentLinkedQueue<Person> tmp = request.get(currentFloor); 
		while(!(tmp.isEmpty())){
			Person personLeaving = tmp.poll(); 
			System.out.println(personLeaving); 
		}
		//System.out.println("hi"); 
		notifyAll(); 
	
	}
	
	public synchronized void allowEnter() throws InterruptedException{
		
		// while there is people waiting on the elevator 
		while (request.isEmpty() || removeAPerson(currentFloor, request)==null){
			wait();
			
		}
		
	    Person personEntering = removeAPerson(currentFloor, request); 
	    System.out.println(personEntering); 
		// who's allowed enter the elevator 
		while(personEntering != null ){ 
		//System.out.println("dhdhd"); 
		System.out.println("Leaving:" + personEntering); 
		// find out where they'd like to get off 
		//addPerson(personEntering,personEntering.destinationFloor,inElevator); 
	    ConcurrentLinkedQueue<Person> tmp = new  ConcurrentLinkedQueue<Person>(); 
	    tmp.add(personEntering); 
		inElevator.put(personEntering.destinationFloor, tmp);
		System.out.println("In elevator:"  + inElevator );  
		personEntering = removeAPerson(3, request); 
		System.out.println(personEntering); 
		System.out.print(currentFloor); 
		}
		notifyAll(); 
		
	
	

    }
    
    public synchronized void enterElevator(){
		
		
       Person personEntering = removeAPerson(currentFloor, request); 
       // if there is a person on the floor requesting to get in, let them in 
       
       if (personEntering!=null){
		 
		 // check if the elevator needs to go up or down
		 if (personEntering.destinationFloor < currentFloor){
			 goingDown = true;
			 goingUp = false;
		 }
		 else{
			 goingDown = true;
			 goingUp = false;			 
	    }  
		   
       System.out.printf("Allowing people in on floor %s...\n",currentFloor); 
       // while there are people at that floor let them enter the elevator
       // then add them to the inElevator hashmap with there destination floor 
       while(personEntering!=null){
		addPerson(personEntering, personEntering.destinationFloor,inElevator);
       System.out.println(personEntering); 
	    personEntering = removeAPerson(currentFloor, request); 
	}
		
	}
   }
   
   public synchronized void exitElevator() throws InterruptedException{
	   while(request.isEmpty() && inElevator.isEmpty()){
		   
		   wait(); 
	   }

       //while(inElevator.isEmpty()){
       //   wait(); 
		//}
       //System.out.println("-------");
	   Person personLeaving = removeAPerson(currentFloor, inElevator); 
       // if there is a person on the floor requesting to get in, let them in 
       if (personLeaving!=null){
       System.out.printf("Letting people out on floor %s...\n",currentFloor); 
       // while there are people at that floor let them enter the elevator
       // then add them to the inElevator hashmap with there destination floor 
       while(personLeaving!=null){
       System.out.println(personLeaving); 
	    personLeaving = removeAPerson(currentFloor, inElevator); 
	}
		
	} 
	   
	   
	   
	   
	   
	   
	}

}
