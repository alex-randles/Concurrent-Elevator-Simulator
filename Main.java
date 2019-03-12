

public class Main{
	
	public  static void main(String[] args){
		ElevatorController elevatorController = new ElevatorController(); 
		Elevator elevator = new Elevator(elevatorController);
		Person  p1 = new Person(1,3,1); // (id, arrivalFloor, destinationFloor) 9
		Person  p2 = new Person(2,3,8);
		//Person  p3 = new Person(3,3,8);
		//p1.start();
		//p2.start();  
		elevatorController.makeRequest(p1); 
		elevatorController.makeRequest(p2); 
		elevator.start(); 

	}








}
