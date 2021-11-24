package edu.wm.cs.cs301.amazebychasepacker.gui;

import java.util.Arrays;

import edu.wm.cs.cs301.amazebychasepacker.generation.CardinalDirection;
import edu.wm.cs.cs301.amazebychasepacker.generation.Maze;
import edu.wm.cs.cs301.amazebychasepacker.gui.Constants.UserInput;



/**
 * 
 * @author Chase Packer
 * 
 * CRC: This class, Reliable Robot, is responsible for receiving instructions from the Wizard and using the controller
 * to move throughout the maze.
 * 
 * It can move the robot, jump over walls, rotate, and use its sensors to check distances to objects.
 * 
 * It collaborates with the wizard to know where to go, collaborates with the controller to actually move, and colloborates with
 * the ReliableSensor in order to get the distance to various obstacles.
 *
 */
public class ReliableRobot implements Robot{
	
	//the controller robot will use
	private PlayAnimationActivity thisController;
	
	//Array that contains the robot's sensors
	private DistanceSensor[] sensors = new DistanceSensor[4];
	
	//Maze used to figure out if robot is in a room or at the exit.
	private Maze theMaze;
	
	//Robot's current Position
	//private int[] currentPosition;
	
	//Robot's current Direction
	//private CardinalDirection currentDirection;
	
	//Robot's battery level
	private float[] batterylevel = new float[1]; 
	
	//Energy consumption values for Robot's actions
	private final float FullRotation;
	private final float forwardEnergy;
	private final float JumpEnergy;
	
	//The amount of spaces the robot has traveled
	private int odometer;
	
	//Whether or not the robot is active or not
	private boolean alive;
	
	
	/**
	 * Constructor for the Reliable Robot
	 * 
	 * Set initial constants, create and orient the robot's sensors, and activates robot.
	 * 
	 * @param theController the robot will use.
	 */
	public ReliableRobot(PlayAnimationActivity theController)
	{
		FullRotation = 6;
		forwardEnergy = 6;
		JumpEnergy = 40;
		
		
		
		setController(theController);
		
		theMaze = thisController.getMazeConfiguration();
		
		batterylevel[0] = 3500;
		
		ReliableSensor front = new ReliableSensor(Robot.Direction.FORWARD, theMaze);
		ReliableSensor back = new ReliableSensor(Robot.Direction.BACKWARD, theMaze);
		ReliableSensor left = new ReliableSensor(Robot.Direction.LEFT, theMaze);
		ReliableSensor right = new ReliableSensor(Robot.Direction.RIGHT, theMaze);
		
		addDistanceSensor(front, Robot.Direction.FORWARD);
		addDistanceSensor(back, Robot.Direction.BACKWARD);
		addDistanceSensor(left, Robot.Direction.LEFT);
		addDistanceSensor(right, Robot.Direction.RIGHT);
	
		
		//currentDirection = thisController.getCurrentDirection();
		
		
		
		alive = true;
		//set the constant attributes
		//get current position on board
		
	}
	
	//Trivial
	/**
	 * Sets the controller that the robot will use.
	 * @param controller the robot will use.
	 */
	@Override
	public void setController(PlayAnimationActivity controller) {
		// TODO Auto-generated method stub
		//set theConroller to the parameter
		thisController = controller;
	}

	public PlayAnimationActivity getController()
	{
		return thisController;
	}

	
	public Maze getMaze()
	{
		return theMaze;
	}
	
	public DistanceSensor[] getsensors()
	{
		return sensors;
	}
	
	//Trivial
	/**
	 * Adds a distance sensor the the robot.
	 * Depending on the sensor's mounted direction, it will go into one of four spaces in the
	 * array of sensors.
	 */
	@Override
	public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
		// TODO Auto-generated method stub
		//set sensors direction to mounted direction
		sensor.setSensorDirection(mountedDirection);
		
		//0 = Front Sensor
		//1 = Back Sensor
		//2 = Left Sensor
		//3 = Right Sensor
		
		if(mountedDirection == Robot.Direction.FORWARD)
		{
			sensors[0] = sensor;
		}
		else if(mountedDirection == Robot.Direction.BACKWARD)
		{
			sensors[1] = sensor;
		}
		else if(mountedDirection == Robot.Direction.LEFT)
		{
			sensors[2] = sensor;
		}
		else
		{
			sensors[3] = sensor;
		}
		

		
		
		
		
		//put sensor into sensors array
	

		
	}
	
	//Trivial
	/**
	 * Returns the current position in the maze.
	 * @returns the current position of the robot.
	 */
	@Override
	public int[] getCurrentPosition() throws Exception {
		// TODO Auto-generated method stub
		//return currentpostion attribute
		return thisController.getCurrentPosition();
	}
	
	//Trivial
	/**
	 * Returns the robot's current direction.
	 * @returns the robot's current direction.
	 */
	@Override
	public CardinalDirection getCurrentDirection() {
		// TODO Auto-generated method stub
		//return currentdirection attribute
		return thisController.getCurrentDirection();
	}
	
	//Trivial
	/**
	 * Returns the current battery level of the robot.
	 * @returns the current battery level of the robot
	 */
	@Override
	public float getBatteryLevel() {
		// TODO Auto-generated method stub
		//return batterylevel attribute
		return batterylevel[0];
	}
	
	//Trivial
	/**
	 * Sets the new battery level of the robot
	 * @param level is the robot's new battery level.
	 */
	@Override
	public void setBatteryLevel(float level) {
		// TODO Auto-generated method stub
		//set batterylevel attribute to parameter
		batterylevel[0] = level;
		
	}
	
	//Trivial
	/**
	 * Returns the value for the energy usage of a full rotation.
	 * 
	 * @returns energy used in full rotation of robot.
	 */
	@Override
	public float getEnergyForFullRotation() {
		// TODO Auto-generated method stub
		//return value for energy usaage
		return FullRotation;
	}
	
	//Trivial
	/**
	 * Returns the value of energy usage of a step forward.
	 * 
	 * @returns the value of energy usage for a step forward.
	 */
	@Override
	public float getEnergyForStepForward() {
		// TODO Auto-generated method stub
		//return value of Energy usage
		return forwardEnergy;
	}
	//Trivial
	/**
	 * Returns the current distance traveled.
	 * 
	 * @returns the current distance traveled.
	 */
	@Override
	public int getOdometerReading() {
		// TODO Auto-generated method stub
		
		//return odometer value
		return odometer;
	}
	//Trivial
	/**
	 * Takes the odometer reading and sets it to zero.
	 * 
	 */
	@Override
	public void resetOdometer() {
		// TODO Auto-generated method stub
		
		//take odometer attribute and set it to 0
		odometer = 0;
		
	}
	//Complex
	/**
	 * Takes the robot and changes it's direction depending on the parameter turn value. 
	 *  Uses controller to input command.  Will not rotate if the robot is dead.
	 * 
	 * @param:  direction which the robot will turn.
	 */
	@Override
	public void rotate(Turn turn) {
		
		//assert the robot isn't stopped
		assert(!hasStopped()):"Robot is dead";
		
		//assert the turn is the proper enum
		
		
		/*
		 * The Maze coordinates and coordinates in the State don't match.
		 * 
		 * In order to account for this, whenever we want to rotate left,
		 * we press right instead and vice versa.
		 * 
		 */
		
		
		//if turn is left, use controller input function with right press
		if(turn == Robot.Turn.RIGHT)
		{
			thisController.keyDown(UserInput.LEFT, 1);
			batterylevel[0] -= FullRotation * 0.5;//decrement energy
		}
		else if(turn == Robot.Turn.LEFT)
		{
			//if turn is right, use controller input function with right press
			thisController.keyDown(UserInput.RIGHT, 1);
			batterylevel[0] -= FullRotation * 0.5;
		}
		else
		{
			//if turn is around, tell controller to turn right twice.
			thisController.keyDown(UserInput.RIGHT, 1);
			thisController.keyDown(UserInput.RIGHT, 1);
			
			batterylevel[0] -= FullRotation;
		}
		

		//update direction
		
		//currentDirection = thisController.getCurrentDirection();
		
		
		
		//check if robot has stopped.
		if(batterylevel[0] <= 0)
		{
			alive = false;
			System.out.println("Robot out of energy");
		}
		
		
	}
	//Complex
	/**
	 * Takes the robot and uses the controller to call a movement to move forward.
	 * This method will not work if the robot is dead.  It will also decrement energy.
	 * If the robot does not move, this method recognizes this means the robot has stopped.
	 * 
	 * @param distance the amount of times the robot will be moved.
	 */
	@Override
	public void move(int distance) {
		
		//assert distance is positive value
		assert(distance >= 0): "Value must be positive";
		
		//for the distance specified
		for(int i = 0; i < distance; i++)
		{
			//assert robot isn't stopped
			assert(!hasStopped()): "Robot has to be alive";
			
			
			int[] check = thisController.getCurrentPosition();
			
			//for distance times, tell controller to move forward
			thisController.keyDown(UserInput.UP, 0);
			
			
			//check if robot is stopped by making sure position has changed from before.
			if(Arrays.equals(check, thisController.getCurrentPosition()))
			{
				alive = false;
			}
			
			
			odometer += 1;
		
			
			batterylevel[0] -= forwardEnergy;
			
			if(batterylevel[0] <= 0)
			{
				alive = false;
			}
			
			if(batterylevel[0] <= 0)
			{
				alive = false;
				System.out.println("Robot out of energy");
			}
			
		}
		
	}
	//Complex
	/**
	 * This method moves the robot one space forward in the direction it is facing,
	 * regardless of whether or not there is a wall in the way.  If their is no wall,
	 * it will merely call move.  But if there is a wall, it will directly manipulate 
	 * the current position in the state.
	 * 
	 * 
	 */
	@Override
	public void jump() {

		assert(!hasStopped()):"robot must be alive";
		//call the move method
		//if there is no wall, we can just use the move method
		move(1);	
		//if there is a wall in the way, then the move method should cause the robot to stop
		//check if robot has stopped
		//if so, we will directly change the robot's position through the state object itself
		if(hasStopped() && getBatteryLevel() > 0)
		{
			
			batterylevel[0] += 6;
			
			int[] curPos = thisController.getCurrentPosition();
			int[] newPos = new int[2];
			
			//copy the new position into new array to avoid memory sharing issues
			newPos[0] = curPos[0];
			newPos[1] = curPos[1];
			
			CardinalDirection directionToMove = thisController.getCurrentDirection();
			
			//attempt the jump
			if(directionToMove == CardinalDirection.North)
			{
				newPos[1] -= 1; //Space above
				attemptJump(newPos);
			}
			else if(directionToMove == CardinalDirection.South)
			{
				
				newPos[1] += 1; //space below
				attemptJump(newPos);
					
			}
			else if(directionToMove == CardinalDirection.East)
			{
				newPos[0] += 1; //space to the right
				attemptJump(newPos);
					
			}
			else if(directionToMove == CardinalDirection.West)
			{
				newPos[0] -= 1; //space to the left
				attemptJump(newPos);
					
			}		
		}
		else
		{
			odometer += 1;
			batterylevel[0] -= 34;//check
		}
		if(batterylevel[0] <= 0)
		{
			alive = false;
			System.out.println("Robot out of energy");
		}
		
	}

	/**
	 * This method is responsible for attempting a jump given coordinates to attempt to jump to.
	 * If jump is successful, it will reset robot to be alive and decrement energy.
	 * If jump is unsuccessful, it will keep the robot dead.
	 * @param newPos
	 */
	private void attemptJump(int[] newPos)
	{
		try
		{
			thisController.setNewPosition(newPos[0], newPos[1]);
			alive = true;
			batterylevel[0] -= JumpEnergy;
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			alive = false;
			System.out.println("Attempted to jump outside the map");
		}
	}
	
	
	
	
	//Trivial
	/**
	 * Checks to see if the robot is at the exit of the maze.
	 * @return boolean value for whether robot is or is not at the exit spot.
	 */
	@Override
	public boolean isAtExit() {
		
		//in maze, check if current position is at the exit of the maze
		int[] exit = theMaze.getExitPosition();
		
		if(Arrays.equals(exit, thisController.getCurrentPosition()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	//Trivial
	/**
	 * Checks to see if the robot is currently in a room.
	 * @return boolean value for whether to robot is or is not in a room.
	 */
	@Override
	public boolean isInsideRoom() {
		// TODO Auto-generated method stub
		//in maze, check to see if current postion is marked as being in a room
		
		return theMaze.isInRoom(thisController.getCurrentPosition()[0], thisController.getCurrentPosition()[1]);
	}
	//Trivial?
	/**
	 * Checks to see if the robot has been stopped.
	 */
	@Override
	public boolean hasStopped() {
		
		return !alive;
	}
	
	
	public boolean hasWon()
	{
		return thisController.hasWon();
	}
	//Trivial with object refrence
	/**
	 * Gets the distance to a wall in a given direction.  Makes sure that the robot has a sensor
	 * in that direction, then calls the distance method with the direction specific sensor.
	 * 
	 * @param direction:  the direction that will be checked
	 * @return the distance value to the wall in given direction.
	 * @throws Error if there is no sensor in the given direction.
	 */
	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		
		int Distance = Integer.MAX_VALUE;
		
		String i = "Sensor wrong";
		
		DistanceSensor sensorToUse = new ReliableSensor(Robot.Direction.FORWARD, theMaze);//just setup, this sensor isn't used
		
		if(direction == Robot.Direction.FORWARD)
		{
			if(sensors[0] != null)
			{
				sensorToUse = sensors[0];
			}
			else
			{
				throw new UnsupportedOperationException("Don't have a sensor in this direction");
			}
		}
		else if(direction == Robot.Direction.BACKWARD)
		{
			if(sensors[1] != null)
			{
				sensorToUse = sensors[1];
			}
			else
			{
				throw new UnsupportedOperationException("Don't have a sensor in this direction");
			}
		}
		else if(direction == Robot.Direction.LEFT)
		{
			if(sensors[2] != null)
			{
				sensorToUse = sensors[2];
			}
			else
			{
				throw new UnsupportedOperationException("Don't have a sensor in this direction");
			}
		}
		else if(direction == Robot.Direction.RIGHT)
		{
			if(sensors[3] != null)
			{
				sensorToUse = sensors[3];
			}
			else
			{
				throw new UnsupportedOperationException("Don't have a sensor in this direction");
			}
		}
		//call the same method in sensor
		try 
		{
			Distance = sensorToUse.distanceToObstacle(thisController.getCurrentPosition(), thisController.getCurrentDirection(), batterylevel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new UnsupportedOperationException();
		}
		
		
		//return the retrieved value
		return Distance;
	}
	/**
	 * This method figures out whether or not the robot is currently looking at the exit.
	 * It checks distance in specified, and returns true if the distance value is the 
	 * maximum integer.
	 * 
	 * @param direction:  direction to be checked.
	 * @return boolean value for whether or not the robot is looking at the exit.
	 */
	@Override
	public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		
		//assert there is a sensor in that direction
		
		int Distance = distanceToObstacle(direction);
		
		//call distance to obstacle
		
		//if distance is equal to max value
		
		if(Distance == Integer.MAX_VALUE)
		{
			return true;
		}
		else
		{
			return false;
		}
		//return true, otherwise false
	}
	//Complex?
	/**
	 * To be implemented in P4
	 */
	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		
	}
	//Complex?
	/**
	 * To be implemented in P4
	 */
	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		
	}

}
