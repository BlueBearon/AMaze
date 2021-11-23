package edu.wm.cs.cs301.amazebychasepacker.generation;




import java.util.*;

/**
 * This class is a subclass of the Regular MazeBuilder class, but instead
 * uses Boruvka's algorithm for generating a spanning tree through the maze.
 * 
 * Date: October 3-2021
 * @author Chase Packer
 * @author Paul Falstad
 * @author Peter Kemper
 * @version 2.0
 * 
 */
public class MazeBuilderBoruvka extends MazeBuilder implements Runnable{
	
	/**
	 * These arrays will contain all the edge weights of the walls.
	 * 
	 * In a perfect maze of width x and height y,
	 * the initial number of breakable walls is equal to
	 * x*(y-1) + (x-1)*y.
	 * 
	 * One array will by width * (height - 1) cells.
	 * 
	 * Other will be (width -1) * height cells.
	 */
	
	private int[][] horizontal_weights;//might have to change
	private int[][] vertical_weights;
	
	/**
	 * This Vertex Array will contain all the vertex objects which represent all
	 * the tiles in the maze
	 */
	private Vertex[][] vertex_board;
	
	
	
	/**
	 *the components array keeps track of all components so that they can be easily referenced
	 */
	private ArrayList<component> components;
	
	
	private int getNumComponents()
	{
		return components.size();
	}
	
	
	/**
	 * Vertex Class represents a vertex in the graph
	 * 
	 * it stores the component it is apart of, and has methods to access elements in it's component
	 * 
	 * The primary purpose of the Vertex class is to facilitate interaction with a component object, while still
	 * differentiating between components and vertexes
	 */
	private class Vertex
	{
		private component memberof;
		
		
		private Vertex()
		{
			memberof = new component(this);
			components.add(memberof);

		}
		
		/**
		 * get the edge weight of the component's cheapest edge weight
		 */
		private int getCheapEdgeWeight()
		{
			return memberof.cheapest_edgeweight;
		}
		
	}
	
	/**
	 * Component class represents a component in the graph, which can contain multiple vertexes.
	 * 
	 * It stores an array list of it's vertexes; a reference to the wall with the cheapest edge for easy access;
	 *  and the edge weight of it's cheapest edge for comparison.
	 * 
	 * It has the method mergeComponents for merging two components together when a wall in the maze is removed.
	 * 
	 * A component is created every time a vertex is created.
	 */
	private class component
	{
		/**
		 * Contains all the vertexes which belong to this component
		 */
		ArrayList<Vertex> vertexes;
		
		/**
		 * The integer value of this component's cheapest edge
		 */
		private int cheapest_edgeweight;
		
		/**
		 * Reference to this component's cheapest edge
		 */
		private Wallboard cheapest_edge;
		
		
		/**
		 * Initializes the vertex list, other variables, and adds g to it's own vertex list.
		 * 
		 * @param g:  The vertex from which this component was created
		 * 
		 * 
		 */
		private component(Vertex g)
		{
			//set cheapest edge to null
			//set edgeweight to maxvalue
			//add component to component arraylist
			
			vertexes = new ArrayList<Vertex>();
			
			cheapest_edge = null;
			cheapest_edgeweight = Integer.MAX_VALUE;
			vertexes.add(g);
			
			
		}
		/**
		 * Merges the elements of two components into one.
		 * 
		 * First, it creates a copy of b's vertexes to allow for changing within iteration
		 * Then for every vertex in b, copy it to a, and delete it from b.
		 * 
		 * Finally, remove b from the list of components and set b to null
		 * 
		 * @param b:  the component that will be merged with this component
		 */
		private void mergeComponents(component b)
		{
			// for every element in b
			
				//set element to be member of this component
				//add element to this component's arraylist
				//remove element from b
			
			
			ArrayList<Vertex> vertexescopy = new ArrayList<Vertex>(b.vertexes);
			for(Vertex a: vertexescopy)
			{
				a.memberof = this;
				this.vertexes.add(a);
				b.vertexes.remove(a);
			}
			
			
			//remove b from list of components
			//set b to null for garbage collector
			components.remove(b);
			b = null;
			
			
		
		}
		
	
	}
	


	/**
	 * This array list keeps track of all the breakable wallboards on the list
	 */
	private ArrayList<Wallboard> walls = new ArrayList<Wallboard>();
	
	
	
	
	/**
	 * Constructor for this MazeBuilder just calls the constructor for the super class
	 * and initializes the components list.
	 */
	public MazeBuilderBoruvka()
	{
		super();
		System.out.println("MazeBuilderBoruvka uses Boruvka's algorithm to generate maze.");
		
		components = new ArrayList<component>();
		
		
		
	}
	
	
	
	
	/**
	 * returns the value of the edge weight of a wallboard, must be the same each time that wallboard is called.
	 * 
	 * We will two arrays that store all the weights we generate ahead of time.
	 *
	 *choose array depending on direction of wallboard.
	 * 
	 * This method will be used to get the value from that array.
	 * 
	 * Aside from modifying the values of West and North walls,
	 * the method will just get the value stored according to the walls x and y coordinates.
	 * 
	 * @param w:  The wall board that we want the weight of
	 * @return The weight of this edge
	 */
	public int getEdgeWeight(Wallboard w)
	{
		//get the x coordinate of wallboard
		
		int edgeweight = 0;
		
		
		if(!floorplan.canTearDown(w))
		{
			throw new ArrayIndexOutOfBoundsException("This wall doesn't have a weight because it cannot be torn down");
		}
		
		int xcoord = w.getX();
		
		//get y coordinate of wallboard
		int ycoord = w.getY();
		
		//get direction of wallboard
		CardinalDirection direction = w.getDirection();
		
		if(direction == CardinalDirection.West)
		{
			xcoord -=1;
			direction = CardinalDirection.East;
		}
		else if(direction == CardinalDirection.North)
		{
			ycoord -=1;
			direction = CardinalDirection.South;
		}
		
		
		//if wallboard is north or south, go to horizontal walls array
		if(direction == CardinalDirection.South)
		{
			edgeweight = horizontal_weights[xcoord][ycoord];
		}
		//if wallboard is east or west, go to vertical walls array
		if(direction == CardinalDirection.East)
		{
			edgeweight = vertical_weights[xcoord][ycoord];
		}
		//use x coord, y coord, and direction to go to the specific spot in weight array
		
		//return the value in that part of the array
				
		
		return edgeweight;
	}
	
	/**
	 * Overall method for generating the pathways through the maze.
	 * 
	 * Start by getting all the walls and initializing the weights of the walls.
	 * 
	 * Setup the vertexes array and component Arraylist.
	 * 
	 * The above steps are completed in a method called setup which just prepares the builder to 
	 * generate the paths.
	 * 
	 * while loop till finished.
	 * 
	 * Sets all components cheapest edge weight to max_value.
	 * 
	 * Goes through all the walls in the wall list.
	 * 
	 * Checks if the wall connects two different components.
	 * If no, remove wall from eligible list.
	 * Otherwise, 
	 *	
	 * compare the edge weight of the wall with the first components cheapest edge.
	 * If wall's edgeweight is lower, set component's cheapest edge to the wall.
	 * 
	 * Do the same for the other component.
	 * 
	 * From the components, get all the cheapest edges.
	 * 
	 * Remove cheapest edges and merge the components.
	 * 
	 * Stop when there is only one component left in the list of components.
	 *  
	 */
	@Override
	protected void generatePathways()
	{	
	//perform setup operations
		setup();	
		boolean finished = false;
		while(!finished)
		{
			//set all the components cheapest edge null to start
			for(component i: components)
			{
				i.cheapest_edge = null; //This is the "null" wallboard
				i.cheapest_edgeweight = Integer.MAX_VALUE;
			}	
			//check every wall in the list of walls
			//create a copy of the list of walls to allow for iteration and removal
			ArrayList<Wallboard> wallscopy = new ArrayList<Wallboard>(getWalls());
			for(Wallboard w: wallscopy)
			{
				boolean Same_Component = connectsToSame(w);
				
				//if they connect the same component, remove the wall
				if(Same_Component)
				{
					getWalls().remove(w);
				}
				else
				{
					Vertex a = vertex_board[w.getX()][w.getY()];
					Vertex b = vertex_board[w.getNeighborX()][w.getNeighborY()];
					
					//use wall to find a and b
					
					//compare the cheapest edge of the component of this wall
					if(getEdgeWeight(w) < a.getCheapEdgeWeight())
					{
						//if this wall is cheaper, replace the previous edge
						a.memberof.cheapest_edge = w;
						a.memberof.cheapest_edgeweight = getEdgeWeight(w);
					}
					
					//do the same for the other component the wall is between
					if(getEdgeWeight(w) < b.getCheapEdgeWeight())
					{
						b.memberof.cheapest_edge = w;
						b.memberof.cheapest_edgeweight = getEdgeWeight(w);
					}
				}
			}
			
			//gather all the walls that were the cheapest edge of a component
			ArrayList<Wallboard> cheap_edges = toberemoved();
			//remove each edge and merge components
			for(Wallboard w: cheap_edges)
			{
				removeEdge(w);
			}
			//we are done once there is one element remaining.
			if(components.size() == 1)
			{
				finished = true;
			}		
		}	
	}
	
	
	
	
	/**
	 * This method performs all the necessary setting up of data structures necessary for starting the algorithm.
	 * 
	 * Initializes the horizontal and vertical edge weight arrays.
	 * 
	 * Puts vertexes into every element of vertex_board.
	 * 
	 * Finds all the walls and puts them into arraylist.
	 * 
	 * Finds all the rooms and sets them as one component.
	 * 
	 * Generates and stores all the weights of the walls into the 2D arrays.
	 * 
	 * 
	 */
	private void setup()
	{
		
		//initialize weight arrays for generation
		horizontal_weights = new int[width][height-1];
		vertical_weights = new int[width-1][height];
		
		//initialize the vertex_board for all the tiles on the board
		createVertexes();
		

		
		findallwalls();
		
		findrooms();
		
		generateweights();
		
		
		
	}
	
	/**
	 * This method instantiates the vertex_board, then goes through every position in vertex board and creates a new vertex in that postion.
	 */
	private void createVertexes()
	{
		
		vertex_board = new Vertex[width][height];
		
		for(int w = 0; w < width; w++)
			for(int h = 0; h < height; h++)
			{
				vertex_board[w][h] = new Vertex();
			}
	}
	
	
	/**
	 * This method populates the two arrays for weights with random numbers.
	 * 
	 * Creates random number generator.
	 * 
	 * Go through every element in each array, and store a random number in the spot in the array.
	 */
	private void generateweights()
	{
		for(int w = 0; w < width; w++)
			for(int h = 0; h < (height-1); h++)
			{
				horizontal_weights[w][h] = random.nextInt();
			}
		
		for(int w = 0; w < (width-1); w++)
			for(int h = 0; h < height; h++)
			{
				vertical_weights[w][h] = random.nextInt();
			}
	}
	
	/**
	 * Before generation, find all the breakable walls in the maze.
	 * 
	 * Iterate through every cell in the maze, and get breakable south and east walls.
	 * Don't need to get north and west since these walls will either be border or already be found in previous step.
	 * 
	 * Put all the found walls into arraylist of walls.
	 */
	private void findallwalls()
	{
		//double loop to go through every cell in maze
		
			//check if south wall exists, and if so, is it breakable
				//if so, add it to mazelist
			//check if east wall exists, and if so, it it breakable
				//if so, add it to mazelist

		
		for(int w = 0; w < width; w++)
			for(int h = 0; h < height; h++)
			{
				if(floorplan.hasWall(w, h, CardinalDirection.South))
				{
					Wallboard testWall1 = new Wallboard(w, h, CardinalDirection.South);
					
					if(floorplan.canTearDown(testWall1))
					{
						getWalls().add(testWall1);
				
					}
				}
				
				if(floorplan.hasWall(w, h, CardinalDirection.East))
				{
					Wallboard testWall2 = new Wallboard(w, h, CardinalDirection.East);
					
					if(floorplan.canTearDown(testWall2))
					{
						getWalls().add(testWall2);
					
					}
				}
			}
		

		
	}
	/**
	 * For mazes with rooms, we want to mark all the cells in each room as a part of the same component,
	 *  and treat it like one large cell.
	 * 
	 * Iterate through every cell in maze.
	 * 
	 * If a cell is marked as in a room, check if the cell next to is in a room. If so, merge components.
	 * 
	 * Loop through again in order direction to merge all the row components of the room.
	 * 
	 */
	private void findrooms()
	{
		//loop through iterating with height to combine each level of the room
		for(int w = 0; w < width; w++)
			for(int h = 0; h < height; h++)
			{	
				//Is this tile and the tile next to it in a room
				if(floorplan.isInRoom(w,h) && floorplan.isInRoom(w, h+1))
				{
					vertex_board[w][h].memberof.mergeComponents(vertex_board[w][h+1].memberof);
				}
			}
		

		//combine all the levels into one room
		for(int h = 0; h < height; h++)
			for(int w = 0; w < width; w++)
			{
				if(floorplan.isInRoom(w,h) && floorplan.isInRoom(w+1, h))
				{
					//just need to find the strip once
					if(!vertex_board[w][h].memberof.vertexes.contains(vertex_board[w+1][h]))
					{
					vertex_board[w][h].memberof.mergeComponents(vertex_board[w+1][h].memberof);
					}
					
				}
			}
		
		
	}
	
	
	
	
	/**
	 * Once we have a path between two components, there is no need to create another path between them.
	 * 
	 * For the wall, find the two cells it connects, check to see if they are in the same component in the components array.
	 * 
	 * If no, return false.
	 * 
	 * if yes, remove true, in the generate pathways method, the wall be removed from the wall list to speed up algorithm.
	 * 
	 * @param wall:  The wall that we are testing
	 * @return:  boolean value of whether the two components are connected
	 */
	private boolean connectsToSame(Wallboard wall)
	{
		
		//get cell x and cell y between the wall
		
		//get coordinates of x and y
		
		//check these coordinates in the components array
		//if they are apart of same component in array, return true, otherwise return false
		//get position of cell x
		int x1 = wall.getX();
		int y1 = wall.getY();
		//use position of cell x and direction of wall to find position of cell y
		int x2 = wall.getNeighborX();
		int y2 = wall.getNeighborY();
		
		Vertex a = vertex_board[x1][y1];
		Vertex b = vertex_board[x2][y2];
		
		//access the component of the vertex and see if the other vertex is in the same component
		boolean samecomponent = a.memberof.vertexes.contains(b);
		
		return(samecomponent);
		
	}
	
	/**
	 * Removes an edge from the maze.
	 * 
	 * Removes wall from maze using floorplan.deleteWallboard().
	 * 
	 * Call for a merge between the two components.
	 * 
	 * Remove wall from the list of walls.
	 * 
	 * @param wall:  The wall that we are removing from the maze.
	 */
	private void removeEdge(Wallboard wall)
	{
		//get position of cell x
		int x1 = wall.getX();
		int y1 = wall.getY();
		//use position of cell x and direction of wall to find position of cell y
		int x2 = wall.getNeighborX();
		int y2 = wall.getNeighborY();
		
		
		//delete wall from the floorplan
		if(floorplan.canTearDown(wall))
		floorplan.deleteWallboard(wall);
		else//if wallboard is somehow not breakable.
			throw new RuntimeException("Can't remove this wall");
		
		
		//remove the wall from the list of walls
		getWalls().remove(wall);
		
		//call mergecomponents with the two cell locations
		Vertex v1 = vertex_board[x1][y1];
		Vertex v2 = vertex_board[x2][y2];
		
		v1.memberof.mergeComponents(v2.memberof);
		
		
	}
	
	
	/**
	 * This method will return an arraylist of all the walls that will be removed in each round of the algorithm.
	 * 
	 * It accesses the components arraylist and gets the cheapest edge from each component.
	 * 
	 * Each cheapest edge (excluding duplicates), will be put into the new arraylist, which is returned.
	 * 
	 * @return:  arraylist containing all the cheapest edges that will be removed.
	 */
	private ArrayList<Wallboard> toberemoved()
	{
		//Arraylist to be returned
		//for each component in components arraylist
		
			//check if component's cheapest edge is already in new list
			//if not, add it to the newlist
		
		//return the new ArrayList
		
		ArrayList<Wallboard> ret = new ArrayList<Wallboard>();
		
		for(component c: components)
		{
			if(!ret.contains(c.cheapest_edge))
			{
				ret.add(c.cheapest_edge);
			}
		}
		
		return ret;
		
	}


	protected void setWalls(ArrayList<Wallboard> walls) {
		this.walls = walls;
	}




	public ArrayList<Wallboard> getWalls() {
		return walls;
	}
	


}
