package edu.wm.cs.cs301.amazebychasepacker.gui;


import edu.wm.cs.cs301.amazebychasepacker.generation.CardinalDirection;
import edu.wm.cs.cs301.amazebychasepacker.generation.Maze;
import edu.wm.cs.cs301.amazebychasepacker.gui.Robot.Direction;

/**
 * 
 * @author Chase Packer
 * 
 * This unreliable sensor class extends reliable sensor class by forcing the sensor to occasionally fail.  This class works with the maze
 * in figuring out distance and is called by a robot.
 * 
 * This class is responsible for return the distance requested or informing the robot of the failure.
 *
 */
public class UnreliableSensor extends ReliableSensor implements DistanceSensor
{

	private boolean operational = true;
	
	private Thread t;
	
	public UnreliableSensor(Direction Dir, Maze map) 
	{
		super(Dir, map);
		
	}
	
	
	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			throws Exception
	{
		//if sensor is working, call superclass method
		int dist;
		
		//if sensor is not working, throw error
		
		if(operational == true)
		{
			dist = super.distanceToObstacle(currentPosition, currentDirection, powersupply);
		}
		else
		{
			//return -4;
			throw new Exception();
		}
		
		return dist;
		
	}
	

	
	
	
	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException 
	{

		//create class for determining if sensor is unoperational
		amIWorking failureProcess = new amIWorking(meanTimeBetweenFailures, meanTimeToRepair);
		
		
		t = new Thread(failureProcess);
		
		//start the thread
		t.start();
		
		
	}

	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException 
	{

		t.interrupt();
		
	}

	@Override
	public int getStatus() {
		if(operational)
			return 1;
		else
			return 0;
	}

	/**
	 * This class implements the thread for Sensor failure, when it runs, and until it is interrupted,
	 * the thread will repeatedly switch on and off the operational variable for whether or not
	 * the robot is working
	 * @author Chase Packer
	 *
	 */
	private class amIWorking implements Runnable
	{
		
		//meantime betweenFailures
		int operationaltime;
		int repairtime;
		
		//meantimetorepair
		
		public amIWorking(int meanTimeBetweenFailures, int meanTimeToRepair)
		{
			//initialize variables.
			operationaltime = meanTimeBetweenFailures;
			repairtime = meanTimeToRepair;
		}

		@Override
		public void run() 
		{
			
			try
			{
				//go untill interrupted
				while(true)
				{
					
					
					//wait to make sensor fail
					Thread.sleep(operationaltime);
					
					//make sensor fail
					operational = false;
					
					//wait to repair sensor
					Thread.sleep(repairtime);
	
					//repair sensor
					operational = true;
					
					
				}
			}
			catch(InterruptedException e)
			{
				//when interrupted, set sensor to operational
				operational = true;
			}
			
			
		}
		
	}

}
