package edu.wm.cs.cs301.amazebychasepacker.gui;

import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import edu.wm.cs.cs301.amazebychasepacker.generation.CardinalDirection;
import edu.wm.cs.cs301.amazebychasepacker.generation.Maze;
import edu.wm.cs.cs301.amazebychasepacker.gui.Robot.Direction;
import edu.wm.cs.cs301.amazebychasepacker.gui.Robot.Turn;



/**
 * 
 * @author Chase Packer
 * 
 * * CRC:  This class is responsible for figuring out the path to get through the maze and telling the robot where to go.
 * 
 * It can choose the next closest space to the exit and tell the robot to move that space until the exit.
 * 
 * It collaborates with the Robot in controlling the robot's movement, and collaborates with the maze in order to figure out
 * the optimal path through the maze.
 *
 */
public class Wizard implements RobotDriver{
	
	//the maze the robot will use for navigation.
	private Maze theMaze;
	
	//the robot the wizard will control.
	private Robot theRobot;
	
	//starting energy of the robot.
	private float startEnergy;
	
	//current distance to exit.
	private int disttoExit = 1000;//arbitrary starting value that will be overwritten
	
	
	public Wizard()
	{
		
	}
	//Trivial
	/**
	 * This method sets the robot the Wizard will control.
	 * 
	 * @param r: the robot the Wizard will control.
	 */
	@Override
	public void setRobot(Robot r) {
		// TODO Auto-generated method stub
		
		//set private variable theRobot to the parameter robot
		theRobot = r;
		
		startEnergy = r.getBatteryLevel();
		
	}
	
	
	public Robot getRobot()
	{
		return theRobot;
	}
	//Trivial
	/**
	 * This method sets the maze the Wizard will have to navigate.
	 * 
	 * @param maze:  The maze the robot will have to navigate.
	 */
	@Override
	public void setMaze(Maze maze) {
		// TODO Auto-generated method stub
		
		//set private variable theMaze to the parameter maze
		theMaze = maze;
		
	}
	
	public Maze getMaze()
	{
		return theMaze;
	}
	/**
	 * This method finds the optimal path through the maze and tells the robot where to go to finish the maze.
	 * It does so by repeatedly asking the Maze for a neighbor closer to exit and then moves the robot into 
	 * that position, rotating as needed.
	 * 
	 * @return: returns true if successful in navigating through the maze.
	 */
	//Complex
	@Override
	public boolean drive2Exit() throws Exception {
		
		   boolean found = false;
	        
	        //Allows us to access the Robot's controller
	        ReliableRobot checkRobot = (ReliableRobot) theRobot;
	        
	        		//go until the maze is navigated
	               while(!found)
	               {
	            	   
	            	   		//just call drive1Step2Exit repeatedly
	                       drive1Step2Exit();
	                       
	                       //We know that we are finished navigating if the currentclass is StateWinning
	                       if(checkRobot.hasWon())
	                       {
	                               found = true;
	                               
	                               if(theRobot.getClass() == UnreliableRobot.class)
	                               {
	                            	   deactivatethreads();//stop all the threads
	                               }
	                               return true;
	                       }
	                       
	               }

	        
	        
	        return false;
	}
	
	
	/**
     * This method is responsible for turning off all threads once the wizard has navigated the maze.
     */
    private void deactivatethreads() 
    {
		
    	theRobot.stopFailureAndRepairProcess(Robot.Direction.FORWARD);
    	theRobot.stopFailureAndRepairProcess(Robot.Direction.BACKWARD);
    	theRobot.stopFailureAndRepairProcess(Robot.Direction.LEFT);
    	theRobot.stopFailureAndRepairProcess(Robot.Direction.RIGHT);
		
	}
	
	/**
	 * This method contains all of the logic for which direction the robot needs to turn in order
	 * to be able to move into the neighbor tile.  For instance, if the robot is currently facing
	 * north, and the neighbor is to the west of the robot, the robot will turn left.
	 * 
	 * 
	 * Protected So that WizardJump can access it
	 * 
	 * @param currentDir current direction of the robot
	 * @param neighborDir Direction to the neighbor space.
	 * @return how the robot needs to rotate to go to neighbor.
	 * @throws Exception
	 */
	protected Robot.Turn turnDirection(CardinalDirection currentDir, CardinalDirection neighborDir) throws Exception
	{
		
		
		
		if(currentDir == neighborDir)//don't need to move
		{
			return null;
		}
		else if(currentDir == neighborDir.oppositeDirection())//if in opposite direction, rotate around
		{
			return Robot.Turn.AROUND;
		}
		else if(currentDir == CardinalDirection.North && neighborDir == CardinalDirection.West)
		{
			return Robot.Turn.LEFT;
		}
		else if(currentDir == CardinalDirection.West && neighborDir == CardinalDirection.South)
		{
			return Robot.Turn.LEFT;
		}
		else if(currentDir == CardinalDirection.South && neighborDir == CardinalDirection.East)
		{
			return Robot.Turn.LEFT;
		}
		else if(currentDir == CardinalDirection.East && neighborDir == CardinalDirection.North)
		{
			return Robot.Turn.LEFT;
		}
		else if(currentDir == CardinalDirection.North && neighborDir == CardinalDirection.East)
		{
			return Robot.Turn.RIGHT;
		}
		else if(currentDir == CardinalDirection.East && neighborDir == CardinalDirection.South)
		{
			return Robot.Turn.RIGHT;
		}
		else if(currentDir == CardinalDirection.South && neighborDir == CardinalDirection.West)
		{
			return Robot.Turn.RIGHT;
		}
		else if(currentDir == CardinalDirection.West && neighborDir == CardinalDirection.North)
		{
			return Robot.Turn.RIGHT;
		}
		else
		{
			throw new Exception("No turns are correct");
		}
		
		
	}
	
	
	/**
	 * This method finds the next optimal move through the maze and controls the robot to complete that movement.
	 * It does so by asking the Maze for a neighbor closer to the exit.
	 *
	 *@return : returns true if the robot was successfully moved to the adjacent cell.
	 */
	@Override
	public boolean drive1Step2Exit() throws Exception {
		
		//if robot is at exit tile, find direction of exit, then get out of maze
		if(theRobot.isAtExit())
		{
			 
			rotateToExit();
			theRobot.move(1);
			return true;
		}
		else
		{

		//call maze method get current Postion and neighbor
		int[] currentPos = theRobot.getCurrentPosition();
		int[] neighbor = theMaze.getNeighborCloserToExit(currentPos[0], currentPos[1]);

		int dist = theMaze.getDistanceToExit(currentPos[0], currentPos[1]);

		String msg = "Distance " + dist;

		Log.v("Wizard", msg);

		//figure out which direction the neighbor is in.
		CardinalDirection neighborDir = findNeighborsDirection(currentPos, neighbor);


		//tell robot to rotate to proper direction
		CardinalDirection currentDir = theRobot.getCurrentDirection();
		
		if(turnDirection(currentDir, neighborDir) == null)
		{
			//already facing correct direction, do nothing
		}
		else if(turnDirection(currentDir, neighborDir) == Robot.Turn.AROUND)
		{
			theRobot.rotate(Robot.Turn.AROUND);
		}
		else if(turnDirection(currentDir, neighborDir) == Robot.Turn.LEFT)
		{
			theRobot.rotate(Robot.Turn.LEFT);
		}
		else if(turnDirection(currentDir, neighborDir) == Robot.Turn.RIGHT)
		{
			theRobot.rotate(Robot.Turn.RIGHT);
		}
		//tell robot to move forward	
		theRobot.move(1);
		
		if(theRobot.hasStopped())
		{
			throw new Exception("The robot has stopped");
		}
		//make sure position has changed to neighbor position.
		if(Arrays.equals(theRobot.getCurrentPosition(), neighbor))
		{
			return true;
		}
		else
		{
			return false;
		}
		}
	}
	
	
	/**
     * This method is responsible for rotating to find the void at the exit spot.
     * @throws Exception 
     */
    private void rotateToExit() throws Exception
    {
        while(checkUp() != Integer.MAX_VALUE)
       {
               theRobot.rotate(Robot.Turn.RIGHT);
       }
    }
	
	
	/**
	 * Figures out the direction to the tile the robot is going to move to by looking at the coordinates
	 * of the neighbor tile and comparing it to its own.
	 * @param currentPosition
	 * @param neighbor
	 * @return Cardinal Direction to neighbor
	 * @throws Exception
	 */
	private CardinalDirection findNeighborsDirection(int[] currentPosition, int[] neighbor) throws Exception
	{
		int x = currentPosition[0];
		int y = currentPosition[1];
		
		int neighx = neighbor[0];
		int neighy = neighbor[1];
		
		if(x > neighx)
		{
			return CardinalDirection.West;
		}
		else if(x < neighx)
		{
			return CardinalDirection.East;
		}
		else if(y > neighy)
		{
			return CardinalDirection.North;
		}
		else if(y < neighy)
		{
			return CardinalDirection.South;
		}
		else
		{
			throw new Exception("Neigbor is the same as current Postion");
		}
	}

	
	/**
	 * Returns the energy consumed going through the maze by taking current energy from robot and subtracting it
	 * from starting energy.
	 * 
	 * @return the energy consumed in the maze.
	 */
	@Override
	public float getEnergyConsumption() {

		
		//get energy level of the robot
		float currentEnergy = theRobot.getBatteryLevel();
		
		//subtract that from the starting energy and return it
		return (startEnergy - currentEnergy);
	}
	/**
	 * This method returns the distance traveled through out the maze.
	 * 
	 * @return:  distance traveled
	 */
	//Trivial
	@Override
	public int getPathLength() {
		
		//return robot's odometer reading
		
		return theRobot.getOdometerReading();
	}
	
	
	 /**
     * This method is responsible for determining the distance in front of the robot.
     * This method behaves the same as checkRight in that will rotate if the sensor is
     * currently not working.  Implementation is the same as CheckRight, but with directions
     * changed.
     * @return distance value
     */
    private int checkUp()
    {
    	
    	int retVal = -4;
    	
    	retVal = tryDistance(Direction.FORWARD);
    	
    	//forward didn't work, try left
    	if(retVal < 0)
    	{
    		int rotates = 1;
        	theRobot.rotate(Turn.RIGHT);
        	
        	retVal = tryDistance(Direction.LEFT);
        	
        	//left didn't work, try back
        	if(retVal < 0)
        	{
        		rotates++;
        		theRobot.rotate(Turn.RIGHT);
        		retVal = tryDistance(Direction.BACKWARD);
        		
        	}
        	
        	//back didn't work, try right
        	if(retVal < 0)
        	{
        		rotates++;
        		theRobot.rotate(Turn.RIGHT);
        		retVal = tryDistance(Direction.RIGHT);
        	}
        	
        	for(int i = 0; i < rotates; i++)
        		theRobot.rotate(Turn.LEFT);
        	
        	
        	if(retVal < 0)
        	{
        		while(retVal < 0)
                {
                	try 
                	{
						TimeUnit.MILLISECONDS.sleep(200);
						retVal = tryDistance(Direction.FORWARD);
					}
                	catch (InterruptedException e)
                	{
						
					}
                	
                	
                }
        	}
    	}
    

    	
    	return retVal;
    }
    
    /**
     * Calls distance to obstacle. if it fails, returns -4.
     * @param x Direction to check
     * @return
     */
    private int tryDistance(Direction x)
    {
    	int retVal = -4;
    	
    	try
		{
			retVal = theRobot.distanceToObstacle(x);
		}
		catch(UnsupportedOperationException e)
		{
			
		}
    	
    	return retVal;
    }

}
