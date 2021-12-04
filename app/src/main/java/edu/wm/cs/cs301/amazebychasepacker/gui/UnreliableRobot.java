package edu.wm.cs.cs301.amazebychasepacker.gui;

import edu.wm.cs.cs301.amazebychasepacker.gui.Robot.Direction;

import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * 
 * @author Chase Packer
 * 
 * This class is designed to extend UnreliableRobot but accounts for having unreliable sensors.
 * 
 * This class works with the controller to control movement in the maze; the maze in order to measure whether or not the robot is at the exit
 * or in a room; and is directed by the driver.
 * 
 * The robot is responsible for accepting instructions from the driver, and giving the proper inputs to the controller.
 *
 */
public class UnreliableRobot extends ReliableRobot implements Robot
{
	
	
	final Robot.Direction[] directions = {Robot.Direction.FORWARD,Robot.Direction.BACKWARD,Robot.Direction.LEFT,Robot.Direction.RIGHT};

	public UnreliableRobot(PlayAnimationActivity theController, String sensorBinary)
	{
		super(theController);
		
		//uses sensorBinary to figure out which sensors are reliable and unreliable and
		//adds/replaces them on the robot.
		
		int Failtime = 4000;
		
		int Repairtime = 2000;
		
		
		
		assert(sensorBinary.length() == 4): "Invalid Sensor Binary";
		
		for(int i = 0; i < sensorBinary.length() ; i++)
		{
			if(sensorBinary.charAt(i) == '1')
			{
				addDistanceSensor(new ReliableSensor(directions[i], getMaze()), directions[i]);
				
			}
			else if(sensorBinary.charAt(i) == '0')
			{
				addDistanceSensor(new UnreliableSensor(directions[i], getMaze()), directions[i]);
				
				//startFailureAndRepairProcess(directions[i], Failtime, Repairtime);
				
			}
		}
		
		//START FAILURE PROCESS FOR SENSORS////////////////////////////////////////
		StartSensors s = new StartSensors(Failtime, Repairtime);
		Thread t = new Thread(s);
		
		t.start();//start the thread on each sensor.
		
		
		
	}
	
	
	@Override
	public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException 
	{
		try
		{
			boolean seevoid = super.canSeeThroughTheExitIntoEternity(direction);
			
			return seevoid;
		
		}
		catch(Exception e)
		{
			throw new UnsupportedOperationException();
		}
		
		
	}
	
	
	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException 
	{
		// TODO Auto-generated method stub
		
		//calls startFailureAndRepairProcess on the 4 sensors in the sensors array at the proper interval
		switch(direction)
		{
		
		case FORWARD:
		{
			if(getsensors()[0].getClass() == UnreliableSensor.class)
				getsensors()[0].startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
			break;
			
		}
		
		case BACKWARD:
		{
			if(getsensors()[1].getClass() == UnreliableSensor.class)
				getsensors()[1].startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
			break;
		}
			
		case LEFT:
		{
			if(getsensors()[2].getClass() == UnreliableSensor.class)
				getsensors()[2].startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
			break;
		}
			
		case RIGHT:
		{
			if(getsensors()[3].getClass() == UnreliableSensor.class)
				getsensors()[3].startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
			break;
		}
		
		}
		//throw new UnsupportedOperationException();
		//calls stopFailureAndRepairProcess on the 4 sensors in the sensors array
		
		
		
		//throw new UnsupportedOperationException();
		
	}
	
	
	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException 
	{
		// TODO Auto-generated method stub
		
		
		switch(direction)
		{
		
		case FORWARD:
		{
			if(getsensors()[0].getClass() == UnreliableSensor.class)
			getsensors()[0].stopFailureAndRepairProcess();
			break;
			
		}
		
		case BACKWARD:
		{
			if(getsensors()[1].getClass() == UnreliableSensor.class)
			getsensors()[1].stopFailureAndRepairProcess();
			break;
		}
			
		case LEFT:
		{
			if(getsensors()[2].getClass() == UnreliableSensor.class)
			getsensors()[2].stopFailureAndRepairProcess();
			break;
		}
			
		case RIGHT:
		{
			if(getsensors()[3].getClass() == UnreliableSensor.class)
			getsensors()[3].stopFailureAndRepairProcess();
			break;
		}
		
		}
		
		//throw new UnsupportedOperationException();
		//calls stopFailureAndRepairProcess on the 4 sensors in the sensors array
		
	}

	@Override
	public int[] getSensorStatus() {

		int[] status = new int[4];

		for(int i = 0; i < 4; i++)
		{
			status[i] = getsensors()[i].getStatus();
		}

		return status;
	}

	/**
	 * For testing purposes, this method forcefully makes all UnreliableSensors not operational.
	 */
	public void killSensors()
	{
		for(int i = 0; i< 4; i++)
		{
			if(getsensors()[i].getClass() == UnreliableSensor.class)
			{
				getsensors()[i].stopFailureAndRepairProcess();
				
				getsensors()[i] = null;
			}
		}
	}
	
	/**
	 * For testing purposes, this method "kills" a specified sensor on the robot, making it
	 * inoperable until it replaced
	 * @param i
	 */
	public void KillOneSensor(Robot.Direction i)
	{
		switch(i)
		{
		
		case FORWARD:
		{
			if(getsensors()[0].getClass() == UnreliableSensor.class)//only kill if the sensor is unreliable
			{
				getsensors()[0].stopFailureAndRepairProcess();//stop the thread
				
				getsensors()[0] = null;//kill the robot
			}
			break;
			
		}
		
		case BACKWARD:
		{
			if(getsensors()[1].getClass() == UnreliableSensor.class)
			{
				getsensors()[1].stopFailureAndRepairProcess();
				
				getsensors()[1] = null;
			}
			break;
		}
			
		case LEFT:
		{
			if(getsensors()[2].getClass() == UnreliableSensor.class)
			{
				getsensors()[2].stopFailureAndRepairProcess();
				
				getsensors()[2] = null;
			}
			break;
		}
			
		case RIGHT:
		{
			if(getsensors()[3].getClass() == UnreliableSensor.class)
			{
				getsensors()[3].stopFailureAndRepairProcess();
				
				getsensors()[3] = null;
			}
			break;
		}
		
		
		
		
		
		
		
		}
	}
	
	public boolean hasWon()
	{
		return getController().hasWon();
	}
	
	
	/**
	 * This class is in charge of operating the thread that starts the the other sensor threads
	 * and handling the 1.3 second delay in between.
	 * @author Chase Packer
	 *
	 */
	private class StartSensors implements Runnable
	{
		
		//meantime betweenFailures
		int operationaltime;
		int repairtime;
		
		//meantimetorepair
		
		public StartSensors(int meanTimeBetweenFailures, int meanTimeToRepair)
		{
			//initialize variables.
			operationaltime = meanTimeBetweenFailures;
			repairtime = meanTimeToRepair;
		}
		/**
		 * For every sensor that is unreliable, start the Failure and Repairprocess, then wait
		 * 1.3 seconds to start the next.
		 */
		@Override
		public void run() 
		{
			
			for(int i = 0; i < 4; i++)
			{
				if(getsensors()[i].getClass() == UnreliableSensor.class)//only unreliable
				{
					//start thread
					startFailureAndRepairProcess(directions[i],operationaltime, repairtime);
					
					//wait 1.3 seconds
					try 
					{
						Thread.sleep(1300);
					}
					catch (InterruptedException e) 
					{
						
					}
					
				}
			}
			
				
		}
		
	}

}
