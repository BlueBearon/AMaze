package edu.wm.cs.cs301.amazebychasepacker.gui;

import edu.wm.cs.cs301.amazebychasepacker.generation.CardinalDirection;

/**
 * 
 * @author Chase Packer
 * 
 * This class extends Wizard by adding jump functionality.
 * 
 * In the same vein as Wizard, it works with the robot by giving it instructions, and works with the maze to figure out where it should go
 * and ideal jump locations.  This class can look at the maze and figure out if it is ideal for the wizard to jump in a particular scenario, 
 * and if not, send it over to the regular wizard.  This class is responsible for figuring out if a jump is ideal, and if so, telling the robot
 * where to jump. If jump is not ideal, it sends it over to regular wizard.
 *
 */
public class WizardJump extends Wizard implements RobotDriver
{
	@Override
	public boolean drive2Exit() throws Exception 
	{
		
		 boolean found = false;
         
         ReliableRobot checkRobot = (ReliableRobot) getRobot();
         
         
                while(!found)
                {
                        drive1Step2Exit();
                        
                        if(checkRobot.hasWon())
                        {
                                found = true;
                                return found;
                        }
                        
                }

         
         
         return false;
	}
	
	@Override
	public boolean drive1Step2Exit() throws Exception 
	{
		
		/*
		 * First attempt a jump (logic defined in seperate method
		 * 
		 * If jump is not the best action, perform normal navigation
		 */
		boolean jump = false;
		
		
		boolean move;
		
		
		//attempt a jump unless we are at an exit
		if(!getRobot().isAtExit())
		{
			jump = Jump();
		}
		
		if(!jump)//if decide to not jump, perform regular wizard navigation.
		{
			move = super.drive1Step2Exit();
			
			if(move)
			{
				return true;
			}
			else
			{
				return false;
			}
			
		}
		else
		{
			return true;
		}
		
		
		
	}
	
	
	/**
	 * This method performs a jump in the maze if the 
	 * @return whether jump was done or not
	 * @throws Exception
	 */
	private boolean Jump() throws Exception
	{
		
		int shortestDistancePosition = DirectionToJump();
	
		//if no space fulfills the requirement, return false, so we can move normally
		if(shortestDistancePosition == -1)
		{
			return false;
		}
		
		rotateRobotToJump(shortestDistancePosition);
		
		//Jump
		getRobot().jump();
		
		if(getRobot().hasStopped())
		{
			throw new Exception("The robot has stopped");
		}
		else
		{
			return true;//tell the wizard the jump was successful
		}
		
		
		
	}
	
	/**
	 * This method checks the distance to exit of all the spaces in each cardinal direction 
	 * in relation to the current Position, and then figures out which position, if any would be
	 * ideal to jump to.  If a jump is not ideal, we return -1.
	 * @return integer corresponding to which direction the robot should jump or -1.
	 * @throws Exception
	 */
	private int DirectionToJump() throws Exception
	{
		//Get Positions to Check
		int[] currentPosition = getRobot().getCurrentPosition();
		
		//Jump uses 40 energy.  Considering that the robot has to move and might rotate,
		//It's more efficient to jump if we can skip roughly 6 spaces.
		int distanceCheck = getMaze().getDistanceToExit(currentPosition[0], currentPosition[1]) - 6;
		
		int[] WestPosition = {currentPosition[0]-1, currentPosition[1]};
		
		int[] EastPosition = {currentPosition[0]+1, currentPosition[1]};
		
		int[] NorthPosition = {currentPosition[0], currentPosition[1]-1};
		
		int[] SouthPosition = {currentPosition[0], currentPosition[1]+1};
		
		
		int[][] Positions = {NorthPosition, SouthPosition, EastPosition, WestPosition};
		
		int[] PositionDistances = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
		
		//Get the distances of the corresponding spaces
		for(int r = 0; r < 4; r++)
		{
			
			if(Positions[r][0] >= 0 && Positions[r][1] >= 0)
			{
				if(Positions[r][0] < getMaze().getWidth() && Positions[r][1] < getMaze().getHeight())
				{
					PositionDistances[r] = getMaze().getDistanceToExit(Positions[r][0], Positions[r][1]);
				}
				
					
			}
		}
		
		//get the space with the shortest distance that is less expensive than moving one
		int shortestDistance = distanceCheck;
		int shortestDistancePosition = -1;
		
		for(int i = 0; i < 4; i++)
		{
			if(PositionDistances[i] < shortestDistance)
			{	
				shortestDistance = PositionDistances[i];
				shortestDistancePosition = i;
			}
		}
		
		
		return shortestDistancePosition;
	}
	
	/**
	 * This method is responsible for rotating the robot to the correct direction in order
	 * for the robot to jump to correct space.
	 * Based on shortestDistancePosition, it generates the direction of the neighbor, then uses
	 * turn direction to figure out how it needs to rotate the robot.
	 * @param position
	 * @throws Exception
	 */
	private void rotateRobotToJump(int position) throws Exception
	{
		//figure out which direction we need to rotate
				CardinalDirection currentDir = getRobot().getCurrentDirection();
				
				CardinalDirection neighborDir = CardinalDirection.North;
				
				//get the direction of the space we are jumping to
				switch(position)
				{
					case 0:
					{
						neighborDir = CardinalDirection.North;
						break;
					}
					
					case 1:
					{
						neighborDir = CardinalDirection.South;
						break;
					}
						
					case 2:
					{
						neighborDir = CardinalDirection.East;
						break;
					}
						
					case 3:
					{
						neighborDir = CardinalDirection.West;
						break;
					}
				}
				
				//rotate the robot to face direction we are jumping
				if(turnDirection(currentDir, neighborDir) == null)
				{
					//already facing correct direction, do nothing
				}
				else if(turnDirection(currentDir, neighborDir) == Robot.Turn.RIGHT)
				{
					getRobot().rotate(Robot.Turn.RIGHT);
				}
				else if(turnDirection(currentDir, neighborDir) == Robot.Turn.LEFT)
				{
					getRobot().rotate(Robot.Turn.LEFT);
				}
				else
				{
					getRobot().rotate(Robot.Turn.AROUND);
				}
	}
	
	
}
