package edu.wm.cs.cs301.amazebychasepacker.gui;

import edu.wm.cs.cs301.amazebychasepacker.generation.CardinalDirection;
import edu.wm.cs.cs301.amazebychasepacker.generation.Maze;
import edu.wm.cs.cs301.amazebychasepacker.gui.Robot.Direction;



/**
 * 
 * @author Chase Packer
 * 
 * This class is responsible for measuring the distance between the robot and a specific obstacle.
 * 
 * Reliable Sensor can calculate the distance between it and wall (or the void) in a given direction.
 * 
 * This class collaborates with the robot, giving the robot the distance to obstacles, and collaborates with the maze in order
 * to calculate distances.
 *
 */
public class ReliableSensor implements DistanceSensor{
	
	
	private Robot.Direction MountedDirection;
	private Maze theMaze;
	private final float EnergyConsumption;

	
	/**
	 * Constructor
	 */
	public ReliableSensor(Robot.Direction Dir, Maze map)
	{
		this.EnergyConsumption = 1;
		//set mounted direction of sensor to this value.
		MountedDirection = Dir;
		
		theMaze = map;
		
		//set the energy consumption to the specified value.
		
	}
	//Complex
	/**
	 * Calculates the distance to the nearest obstacle in the given direction and returns it.
	 * If the sensor sees the void, it will return the maximum integer, which is used for
	 * robot method canSeeThroughTheExitIntoEternity().
	 * 
	 * To calculate, first the sensor figures out which direction it is searching in. Then,
	 * it iterates through the nodes in that direction until it encounters a wall.  
	 * If it iterates out of bounds, that means that the robot can see the void.
	 * 
	 * @param currentPosition: current position in the maze.
	 * @param currentDirection:  currentDirection the robot is facing.
	 * @param powersupply:  the robots current powersupply
	 * 
	 * @return: the distance to the wall in given direction, or max_value if exit is found.
	 */
	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			throws Exception {
		//assert that this sensor can check this direction.
		//with the current position, check the next block in the specified direction in a loop until we reach a wall, 
		//count the blocks checked
		boolean blocked = false;
		int distance = 0;//distance value to be checked.
		int[] pos_check = new int[2];//keeps track of position we are checking relative to the robot.
		
		pos_check[0] = currentPosition[0];//x value
		pos_check[1] = currentPosition[1];//y value
		
		powersupply[0] -= EnergyConsumption;//decrement energy for using sensor.

		//figure out which direction we are checking based on the sensor we are using
		//i.e. if the robot is facing north and we are checking the back sensor, then
		//the direction we are actually checking is South.
		
		CardinalDirection checkDirection = currentDirection;//direction we will check.

		if(MountedDirection == Robot.Direction.BACKWARD)
		{
			checkDirection = currentDirection.oppositeDirection();
		}
		else if(MountedDirection == Robot.Direction.LEFT)
		{
			checkDirection = findDirection(currentDirection, -1);
		}
		else if(MountedDirection == Robot.Direction.RIGHT)
		{
			checkDirection = findDirection(currentDirection, 1);
		}
		//Calculating Distance:
		//until we find a wall, check every position in the direction we are
		///checking
		
		//Logic the same for every direction, but with different numbers
		if(checkDirection == CardinalDirection.North)
		{
			return computeDistanceNW(pos_check, 1, CardinalDirection.North);
		}
		else if(checkDirection == CardinalDirection.South)
		{	
			return computeDistanceSE(pos_check, 1, CardinalDirection.South);
		}
		else if(checkDirection == CardinalDirection.East)
		{
			return computeDistanceSE(pos_check, 0, CardinalDirection.East);
		}
		else
		{
			return computeDistanceNW(pos_check, 0, CardinalDirection.West);
		}
	}
	
	
	/**
	 * This method is responsible for calculating the distance in given direction with position.
	 * It checks the current space, and then checks the next space in the next direction until
	 * it encounters a wall or goes out of bounds.
	 * 
	 * This method is specific for checking North and West.
	 * 
	 * @param pos_check the position being checked
	 * @param directioncheck either 0 for checking Horizontally, or 1 for checking vertically
	 * @param checkDirection, direction we are checking in
	 * @return distance
	 */
	private int computeDistanceNW(int[] pos_check, int directioncheck, CardinalDirection checkDirection)
	{
		
		int distance = 0;
		
		while(true)
		{
			
			//go until we find a wall, and then return the distance we iterated through
			if(theMaze.hasWall(pos_check[0], pos_check[1], checkDirection))
			{
				return distance;
			}
			else
			{
				//if no wall, add to distance, and go to next position
				distance += 1;
				pos_check[directioncheck] --;
				
				//if position is out of bounds, return max_value
				if(pos_check[directioncheck] < 0)
				{
					return Integer.MAX_VALUE;
				}
				
			}
		}
		
	}
	
	/**
	 * This method is responsible for calculating the distance in given direction with position.
	 * It checks the current space, and then checks the next space in the next direction until
	 * it encounters a wall or goes out of bounds.
	 * 
	 * This method is specific for checking South and East.
	 * 
	 * 
	 * @param pos_check the position being checked
	 * @param directioncheck either 0 for checking Horizontally, or 1 for checking vertically
	 * @param checkDirection direction we are checking in
	 * @return distance
	 */
	private int computeDistanceSE(int[] pos_check, int directioncheck, CardinalDirection checkDirection)
	{
		
		int distance = 0;
		
		while(true)
		{
			//go until we find a wall, and then return the distance we iterated through
			if(theMaze.hasWall(pos_check[0], pos_check[1], checkDirection))
			{
				return distance;
			}
			else
			{
				//if no wall, add to distance, and go to next position
				distance += 1;
				pos_check[directioncheck] ++;
				
				int VoidValue = 0;
				
				//get the max width or height for checking out of bounds
				if(directioncheck == 0)
				{
					VoidValue = theMaze.getWidth();
				}
				else
				{
					VoidValue = theMaze.getHeight();
				}
				
				
				//if out of bounds, return max
				if(pos_check[directioncheck] >= VoidValue)
				{
					return Integer.MAX_VALUE;
				}
				
			}
		}
		
	}
	
	/**
	 * This method helps the sensor figure out which direction it is allowed to check, as
	 * a sensor can only check one direction.
	 * 
	 * @param currentDirection:  the current Direction of the robot.
	 * @param i value, either -1 or 1, for which direction to return.
	 * @return direction that will be checked.
	 * 
	 */
	private CardinalDirection findDirection(CardinalDirection currentDirection, int i) {
		// TODO Auto-generated method stub
		
	
		
		switch(currentDirection)
		{
		case North:
		{
			if(i == 1)
			{
				return CardinalDirection.East;
			}
			else
			{
				return CardinalDirection.West;
			}
		}
		case South:
		{
			if(i == 1)
			{
				return CardinalDirection.West;
			}
			else
			{
				return CardinalDirection.East;
			}
		}
		case East:
		{
			if(i == 1)
			{
				return CardinalDirection.South;
			}
			else
			{
				return CardinalDirection.North;
			}
		}
		case West:
		{
			if(i == 1)
			{
				return CardinalDirection.North;
			}
			else
			{
				return CardinalDirection.South;
			}
		}
		default:
			break;
		}
		
		return null;
	}
	//Trivial
	/**
	 * Get's the maze and sets the maze to theMaze attribute for later use
	 * 
	 * @param maze, the maze the sensor will use.
	 */
	@Override
	public void setMaze(Maze maze) {
		// TODO Auto-generated method stub
		
		//set parameter maze to theMaze attribute
		theMaze = maze;
		
	}
	//Trivial
	/**
	 * Sets the sensors direction to the parameter.
	 * 
	 * @param mountedDirection the direction of the sensor.
	 */
	@Override
	public void setSensorDirection(Direction mountedDirection) {
		// TODO Auto-generated method stub
		
		//set the sensor's direction to the parameter direction.
		this.MountedDirection = mountedDirection;
		
	}
	//Trivial
	/**
	 * Returns the value of the amount that using the sensor depletes the battery
	 * 
	 * @return  the amount energy used for using sensors.
	 */
	@Override
	public float getEnergyConsumptionForSensing() {
		// TODO Auto-generated method stub
		
		//return battery consumption value.
		return EnergyConsumption;
	}
	
	
	//Complex?
	/**
	 * To be implemented in P4
	 */
	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		
		throw new UnsupportedOperationException();
		
	}
	//Complex?
	/**
	 * To be implemented in P4
	 */
	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		
	}

}
