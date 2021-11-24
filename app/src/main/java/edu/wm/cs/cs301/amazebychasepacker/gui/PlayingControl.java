package edu.wm.cs.cs301.amazebychasepacker.gui;


import android.provider.Settings;

import edu.wm.cs.cs301.amazebychasepacker.generation.CardinalDirection;
import edu.wm.cs.cs301.amazebychasepacker.generation.Floorplan;
import edu.wm.cs.cs301.amazebychasepacker.generation.Maze;

/**
 * This class is responsible for replacing StatePlaying from Eclipse Project.
 *
 * PlayManualActivity and PlayAnimationActivity will share a lot of code in
 * navigating the maze, so both methods will make calls to methods in this class
 * for operations
 */
public class PlayingControl
{
    //FPV
    //Map
    private MazePanel panel;
    private Maze mazeConfig;
    private PlayAnimationActivity activity;

    int px, py ; // current position on maze grid (x,y)
    int dx, dy;  // current direction

    int angle; // current viewing angle, east == 0 degrees
    int walkStep; // counter for intermediate steps within a single step forward or backward

    boolean started;

    public PlayingControl()
    {
        started = false;
    }


    public void setMazeConfiguration(Maze config) {
        mazeConfig = config;
    }

    public void start(PlayAnimationActivity activity, MazePanel panel) {
        started = true;
        // keep the reference to the controller to be able to call method to switch the state
        this.activity = activity;
        // keep the reference to the panel for drawing
        this.panel = panel;
        //
        // init data structure for visible walls
        //seenCells = new Floorplan(mazeConfig.getWidth()+1,mazeConfig.getHeight()+1) ;
        // set the current position and direction consistently with the viewing direction
        setPositionDirectionViewingDirection();
        walkStep = 0; // counts incremental steps during move/rotate operation
        /*
        // configure compass rose
        cr = new CompassRose();
        cr.setPositionAndSize(Constants.VIEW_WIDTH/2,
                (int)(0.1*Constants.VIEW_HEIGHT),35);

        if (panel != null) {
            startDrawer();
        }
        else {
            // else: dry-run without graphics, most likely for testing purposes
            printWarning();
        }

         */
    }

    protected int[] getCurrentPosition() {
        int[] result = new int[2];
        result[0] = px;
        result[1] = py;
        return result;
    }

    public Maze getMazeConfiguration() {
        return mazeConfig ;
    }

    /**
     * Method incorporates all reactions to keyboard input in original code,
     * The simple key listener calls this method to communicate input.
     * @param key provides the feature the user selected
     * @param value is not used, exists only for consistency across State classes
     * @return false if not started yet otherwise true
     */
    public boolean keyDown(Constants.UserInput key, int value) {
        if (!started)
            return false;

        // react to input for directions and interrupt signal (ESCAPE key)
        // react to input for displaying a map of the current path or of the overall maze (on/off toggle switch)
        // react to input to display solution (on/off toggle switch)
        // react to input to increase/reduce map scale
        switch (key) {
            case START: // misplaced, do nothing
                break;
            case UP: // move forward
                walk(1);
                // check termination, did we leave the maze?
                if (isOutside(px,py)) {
                    activity.switchToWinning();
                }
                break;
            case LEFT: // turn left
                rotate(1);
                break;
            case RIGHT: // turn right
                rotate(-1);
                break;
            case DOWN: // move backward
                walk(-1);
                // check termination, did we leave the maze?
                if (isOutside(px,py)) {
                   activity.switchToWinning();
                }
                break;
            case RETURNTOTITLE: // escape to title screen
                activity.switchToTitle();
                break;
            case JUMP: // make a step forward even through a wall
                // go to position if within maze
                if (mazeConfig.isValidPosition(px + dx, py + dy)) {
                    setCurrentPosition(px + dx, py + dy) ;
                    draw() ;
                }
                break;
            case TOGGLELOCALMAP: // show local information: current position and visible walls
                // precondition for showMaze and showSolution to be effective
                // acts as a toggle switch
                //mapMode = !mapMode;
                //draw() ;
                break;
            case TOGGLEFULLMAP: // show the whole maze
                // acts as a toggle switch
                //showMaze = !showMaze;
                //draw() ;
                break;
            case TOGGLESOLUTION: // show the solution as a yellow line towards the exit
                // acts as a toggle switch
                //showSolution = !showSolution;
                //draw() ;
                break;
            case ZOOMIN: // zoom into map
                //mapView.incrementMapScale();
                //draw() ;
                break ;
            case ZOOMOUT: // zoom out of map
                //mapView.decrementMapScale();
                //draw() ;
                break ;
        } // end of internal switch statement for playing state
        return true;
    }

    protected void startDrawer()
    {
        /*
        firstPersonView = new FirstPersonView(Constants.VIEW_WIDTH,
                Constants.VIEW_HEIGHT, Constants.MAP_UNIT,
                Constants.STEP_SIZE, seenCells, mazeConfig.getRootnode()) ;
        mapView = new Map(seenCells, 15, mazeConfig) ;
        // draw the initial screen for this state
        draw();

         */
    }

    private void setPositionDirectionViewingDirection() {
        // obtain starting position
        int[] start = mazeConfig.getStartingPosition() ;
        setCurrentPosition(start[0],start[1]) ;
        // set current view direction and angle
        angle = 0; // angle matches with east direction,
        // hidden consistency constraint!
        setDirectionToMatchCurrentAngle();
        // initial direction is east, check this for sanity:
        assert(dx == 1);
        assert(dy == 0);
    }

    /**
     * Draws the current content on panel to show it on screen.
     */
    protected void draw() {
        /*
        if (panel == null) {
            printWarning();
            return;
        }
        // draw the first person view and the map view if wanted
        firstPersonView.draw(panel, px, py, walkStep, angle,
                getPercentageForDistanceToExit()) ;
        if (isInMapMode()) {
            mapView.draw(panel, px, py, angle, walkStep,
                    isInShowMazeMode(),isInShowSolutionMode()) ;
        }
        // update the screen with the buffer graphics
        panel.update() ;

         */
    }

    /**
     * Calculates a distance to exit as a percentage.
     * 1.0 is for the starting position as this is the maximal
     * distance possible.
     * @return a value between 0.0 and 1.0, the smaller the closer
     */
    float getPercentageForDistanceToExit() {
        return mazeConfig.getDistanceToExit(px, py) /
                ((float) mazeConfig.getMazedists().getMaxDistance());
    }


    /**
     * Prints the warning about a missing panel only once
     */
    boolean printedWarning = false;
    protected void printWarning() {
        if (printedWarning)
            return;
        System.out.println("StatePlaying.start: warning: no panel, dry-run game without graphics!");
        printedWarning = true;
    }


    protected void setCurrentPosition(int x, int y) {
        px = x ;
        py = y ;
    }
    private void setCurrentDirection(int x, int y) {
        dx = x ;
        dy = y ;
    }

    protected CardinalDirection getCurrentDirection() {
        return CardinalDirection.getDirection(dx, dy);
    }

    private void setDirectionToMatchCurrentAngle() {
        setCurrentDirection((int) Math.cos(radify(angle)), (int) Math.sin(radify(angle))) ;
    }

    final double radify(int x) {
        return x*Math.PI/180;
    }

    /**
     * Helper method for walk()
     * @param dir is the direction of interest
     * @return true if there is no wall in this direction
     */
    protected boolean checkMove(int dir) {
        CardinalDirection cd = null;
        switch (dir) {
            case 1: // forward
                cd = getCurrentDirection();
                break;
            case -1: // backward
                cd = getCurrentDirection().oppositeDirection();
                break;
            default:
                throw new RuntimeException("Unexpected direction value: " + dir);
        }
        return !mazeConfig.hasWall(px, py, cd);
    }


    /**
     * Performs a rotation with 4 intermediate views,
     * updates the screen and the internal direction
     * @param dir for current direction, values are either 1 or -1
     */
    private synchronized void rotate(int dir) {
        final int originalAngle = angle;
        final int steps = 4;

        for (int i = 0; i != steps; i++) {
            // add 1/4 of 90 degrees per step
            // if dir is -1 then subtract instead of addition
            angle = originalAngle + dir*(90*(i+1))/steps;
            angle = (angle+1800) % 360;
            // draw method is called and uses angle field for direction
            // information.
            //slowedDownRedraw();
        }
        // update maze direction only after intermediate steps are done
        // because choice of direction values are more limited.
        setDirectionToMatchCurrentAngle();
        //logPosition(); // debugging
        //drawHintIfNecessary();
    }

    /**
     * Moves in the given direction with 4 intermediate steps,
     * updates the screen and the internal position
     * @param dir, only possible values are 1 (forward) and -1 (backward)
     */
    private synchronized void walk(int dir) {
        // check if there is a wall in the way
        if (!checkMove(dir))
            return;
        // walkStep is a parameter of FirstPersonDrawer.draw()
        // it is used there for scaling steps
        // so walkStep is implicitly used in slowedDownRedraw
        // which triggers the draw operation in
        // FirstPersonDrawer and MapDrawer
        for (int step = 0; step != 4; step++) {
            walkStep += dir;
            //slowedDownRedraw();
        }
        setCurrentPosition(px + dir*dx, py + dir*dy) ;
        walkStep = 0; // reset counter for next time
        //logPosition(); // debugging
        //drawHintIfNecessary();
    }

    /**
     * Checks if the given position is outside the maze
     * @param x coordinate of position
     * @param y coordinate of position
     * @return true if position is outside, false otherwise
     */
    private boolean isOutside(int x, int y) {
        return !mazeConfig.isValidPosition(x, y) ;
    }

    /**
     * Draw a visual cue to help the user unless the
     * map is on display anyway.
     * This is the map if current position faces a dead end
     * otherwise it is a compass rose.
     */
    private void drawHintIfNecessary() {

        /*
        if (isInMapMode())
            return; // no need for help
        // in testing environments, there is sometimes no panel to draw on
        // or the panel is unable to deliver a graphics object
        // check this and quietly move on if drawing is impossible
        if ((panel == null || panel.getBufferGraphics() == null)) {
            printWarning();
            return;
        }
        // if current position faces a dead end, show map with solution
        // for guidance
        if (isFacingDeadEnd()) {
            //System.out.println("Facing deadend, help by showing solution");
            mapView.draw(panel, px, py, angle, walkStep, true, true) ;
        }
        else {
            // draw compass rose
            cr.setCurrentDirection(getCurrentDirection());
            cr.paintComponent(panel);
        }
        panel.update();
        */
    }

    /**
     * Checks if the current position and direction
     * faces a dead end
     * @return true if at the current position there is
     * a wall to the left, right and front, false otherwise
     */
    private boolean isFacingDeadEnd() {
        return (!isOutside(px,py) &&
                mazeConfig.hasWall(px, py, getCurrentDirection()) &&
                mazeConfig.hasWall(px, py, getCurrentDirection().oppositeDirection().rotateClockwise()) &&
                mazeConfig.hasWall(px, py, getCurrentDirection().rotateClockwise()));
    }
}
