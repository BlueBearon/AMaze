package edu.wm.cs.cs301.amazebychasepacker.gui;

import java.util.concurrent.TimeUnit;

import edu.wm.cs.cs301.amazebychasepacker.generation.Maze;
import edu.wm.cs.cs301.amazebychasepacker.gui.Robot.Direction;
import edu.wm.cs.cs301.amazebychasepacker.gui.Robot.Turn;


/**
 * 
 * @author Chase Packer
 * 
 * This class is responsible for driving the robot through the maze by following walls.  It works with the robot and it's sensors to determine
 * where it can go in the maze, and directs the robot as to what it should do.  
 * 
 * This class of capable of figuring out where it can go in the maze, and capable of following walls in order to reach the exit.
 *
 */
public class Wallfollower implements RobotDriver{
	
	private Maze theMaze;
	
	private Robot theRobot;
	
	private float startEnergy;
	
	private int roomCounter = 0;
	
	public Wallfollower()
	{
		
	}

	@Override
	public void setRobot(Robot r) 
	{
		 theRobot = r;
         
         startEnergy = r.getBatteryLevel();
		
		
	}

	@Override
	public void setMaze(Maze maze) 
	{
		theMaze = maze;
	}
	
	public Maze getMaze()
	{
		return theMaze;
	}

	@Override
	public boolean drive2Exit() throws Exception {
		

        boolean found = false;
        
        //Allows us to access the Robot's controller
        ReliableRobot checkRobot = (ReliableRobot) theRobot;
        
        		//go until the maze is navigated
               while(!found)
               {
            	   
            	   		//just call drive1Step2Exit repeatedly
            	   
            	   	if(!theRobot.hasStopped())
            	   	{
                       drive1Step2Exit();
            	   	}
            	   	else {
            	   		throw new Exception();
            	   	}
                       
                       //We know that we are finished navigating if the currentclass is StateWinning
                       if(checkRobot.hasWon())
                       {
                    	   
                    	   System.out.println("Robot has won");
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
     * This method checks to see if the robot has stopped, and if so, throws an exception
     * @throws Exception
     */
	private void checkStop() throws Exception
    {
            if(theRobot.hasStopped())
            {
                   throw new Exception("The robot has stopped");
            }
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

	@Override
	public boolean drive1Step2Exit() throws Exception 
	{
		//if the robot is at the exit, rotate until robot can see void, then move
    	
        if(theRobot.isAtExit())
        {
        	rotateToExit();      	
        }
        else if(checkRight() > 0)//check right
        {
        	
        	//if there is no right wall, rotate right, and continue to follow the wall
        		/*
        		 * Extra:  Check For Loop
        		 * 
        		 * If the robot Somehow gets into a situation where it is in the inner part of the room
        		 * it will get stuck in a loop because there is never a right wall to stop it.
        		 * 
        		 * To fix this, I've added a counter that counts the amount of times the robot goes right
        		 * in a room.  If this number exceeds 3, we know we are in a loop.  When we enter a room,
        		 * we should really only press right if we are exiting the room.
        		 * 
        		 * To get out of the loop, we tell the robot the move as forward as possible, getting it to a wall,
        		 * then telling it to rotate left.  This should get the robot out of any loops and back to expected 
        		 * behavior.
        		 */
        		if(theRobot.isInsideRoom())//if we are inside room, increment the amount of times we press right
        		{
        			roomCounter++;	
        		}
        		if(roomCounter > 3)//if we press right more than three times, we are in a loop
    			{
    				if(checkUp()>0)//go up until we reach a wall
    				{
    					theRobot.move(checkUp());
    				}
    				theRobot.rotate(Turn.LEFT);//rotate left so that we can get back to following the wall
    			}
        		else
        		{
        			 checkStop();
                     theRobot.rotate(Turn.RIGHT);//rotate right
                     checkStop();
                     theRobot.move(1);
        		}
        }
        else if(checkUp() > 0)//check Forward
        {
        		//if there is no wall to the front, move forward, continuing to follow the wall
        	roomCounter = 0;//we are out of loop
               checkStop();
               theRobot.move(1);
        }
        else
        {
        		//if we have no other choice, rotate left
               checkStop();
               theRobot.rotate(Turn.LEFT);
        }
        
        checkStop();
        return true;
	}

	@Override
	public float getEnergyConsumption() 
	{
		 //get energy level of the robot
        float currentEnergy = theRobot.getBatteryLevel();
                       
        //subtract that from the starting energy and return it
        return (startEnergy - currentEnergy);
		
	}

	@Override
	public int getPathLength() 
	{
		return theRobot.getOdometerReading();

	}
	
	/**
     * This method is responsible for determining returning the distance to the right of the robot.
     * However this method accounts for failures by moving the robot in different directions in 
     * order to find a functional sensor, then rotates back to original direction.  If robot
     * cannot find a working sensor, then it waits until the original sensor comes back online.
     * 
     * retVal will be negative while distance has not been calculated.
     * 
     * @return distance value
     */
    private int checkRight()
    {
    	
    	int retVal = -4;//arbitrary negative value, just needs to be negative just to activate loop
    	retVal = tryDistance(Direction.RIGHT);
    	
    	//if retVal is still negative, i.e., right sensor is not working, rotate right and check Forward Sensor
    	if(retVal < 0)
    	{
    		int rotates = 1;
        	theRobot.rotate(Turn.RIGHT);
        	
        	retVal = tryDistance(Direction.FORWARD);
        	
        	//if Forward doesn't work, rotate again, and try Left sensor
        	if(retVal < 0)
        	{
        		rotates++;
        		theRobot.rotate(Turn.RIGHT);
        		retVal = tryDistance(Direction.LEFT);
        		
        	}
        	
        	//if left doesn't work, rotate again, and try back sensor.
        	if(retVal < 0)
        	{
        		rotates++;
        		theRobot.rotate(Turn.RIGHT);
        	
        		retVal = tryDistance(Direction.BACKWARD);
       
        	}
        	
        	//rotate back to orginal position
        	for(int i = 0; i < rotates; i++)
        		theRobot.rotate(Turn.LEFT);
        	
        	//if we still can't get the distance value, just wait until sensor comes back online
        	if(retVal < 0)
        	{
        		while(retVal < 0)
                {
                	try 
                	{
						TimeUnit.MILLISECONDS.sleep(200);
						retVal = tryDistance(Direction.RIGHT);
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
			retVal = -4;
		}
    	
    	return retVal;
    }
    
    /**
     * This method is responsible for rotating to find the void at the exit spot.
     * @throws Exception 
     */
    private void rotateToExit() throws Exception
    {
    	if(checkUp() == Integer.MAX_VALUE)//if void is forward
    	{
    		theRobot.move(1);
    	}
    	else if(checkRight() == Integer.MAX_VALUE)//if void is to right
    	{
    		theRobot.rotate(Turn.RIGHT);
    		theRobot.move(1);
    	}
    	else//if not forward or right, it has to be left.
    	{
    		theRobot.rotate(Turn.LEFT);
    		theRobot.move(1);
    	}
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

}
