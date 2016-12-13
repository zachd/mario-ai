package ucsc_mario_astar;


import java.util.LinkedList;

import ch.idsia.mario.engine.sprites.Mario;
import ch.idsia.mario.environments.Environment;
import ch.idsia.ai.agents.Agent;
import ch.idsia.ai.agents.ai.BasicAIAgent;
import ch.idsia.mario.engine.LevelScene;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy
 * Date: Apr 25, 2009
 * Time: 12:30:41 AM
 * Package: ch.idsia.ai.agents.ai;
 */
public class UCSC_AStarAgent extends BasicAIAgent implements Agent
{
    protected boolean action[] = new boolean[Environment.numberOfButtons];
    protected String name = "UCSC_AStarAgent";
    public LevelScene world; 
	Mario mario = new Mario(this.world); 
	int status = mario.getStatus();
	int[][] costByte;
	
	int trueJumpCounter = 0;
    int trueSpeedCounter = 0;

    public UCSC_AStarAgent()
    {
        super("UCSC_AStarAgent");
    }
    
private class SearchNode{
    	
    	// We wish to not use time as the element
    	// but have to consider Mario's velocity in addition to enemies falling and travel velocities 
    	private int timeElapsed = 0;
		public float remainingTimeEstimated = 0;
		private float remainingTime = 0;
		
		
		private float max_cost = 10000; // max cost limit

		public SearchNode parentPos = null;
		public SearchNode chosenChild = null;
		public LevelScene sceneSnapshot = null;
		public int distanceFromOrigin = 0;
		public boolean hasBeenHurt = false;
		public boolean isInVisitedList = false;
		
		boolean[] action;
		int repetitions = 1;
		
		// Use the top-right corner as goal and detect the top most platform for Mario to reach
		/*
		 * Start Node: Mario's position
		 * End Node: The highest platform at the right side of the screen
		 * 
		 * Obstacles: Enemies (spiked), must be measured to ensure safety of mario's pathway
		 *            Gaps from below
		 * Variables: Mario's running speed
		 *            Mario's maximum jump height
		 *            Enemy Location (whether to jump over, on top, or run underneath the enemies)
		 *            Mario's holding of Koopa shell
		 *            Mario's powerups (fireball)
		 *            
		 * Priority level (how close object are to Mario to do action):
		 *            Shoot fireball at enemy (within 2 blocks) (1)
		 *            Jump on top of enemies (within max running distance and not spiky) (2)
		 *            Jump over enemies (within max running distance) (3)
		 *            Run under enemies (4)
		 *            Jump over gaps (5)
		 *            Jump onto higher platforms (6)
		 * 
		 */
		
    }

    public void reset()
    {
        action = new boolean[Environment.numberOfButtons];// Empty action
        action[Mario.KEY_RIGHT] = true;
        action[Mario.KEY_SPEED] = true;
        trueJumpCounter = 0;
        trueSpeedCounter = 0;
    }
    
    // Checks to see if there is a gap nearby 
    private boolean DangerOfGap(byte[][] levelScene)
    {
        for (int x = 9; x < 13; ++x)
        {
            boolean flag = true;
            for(int y = 12; y < 22; ++y) //checks if there is floor in between the given numbers.
            {
                if  (levelScene[y][x] != 0)
                    flag = false; //if there is then flag turns off.
            }
            if (flag && levelScene[12][11] != 0) //[12][11] is where Mario plans to land.
                return true;
        }
        return false;
    }

    public boolean[] getAction(Environment observation)
    {
    	assert(observation != null);
    	byte[][] levelScene = observation.getCompleteObservation();
    	float[] marioPos = observation.getMarioFloatPos();
    	byte[][] enemiesPos = observation.getEnemiesObservation();

    	/*
    	for(int i = 0; i<22; i++){ //for printing out levelscene.
    		System.out.println();
    		for(int j = 0; j<22; j++){ 
    			System.out.print(levelScene[i][j] + ",");
    		}
    	}
    	System.out.println();
    	*/
    	
		//Jasmine's code//
		costByte = new int[22][22];
		//System.out.println("NODES COST MAP");
		for(int i = 0; i<22; i++){ //prints the Node cases
			//System.out.println("TESTING");
			for(int j = 0; j<22; j++){
				//System.out.println("TESTING2");
				if (levelScene[i][j]== (byte)(-10)){ //if the object is the platform, give it a high cost
					costByte[i][j] = 6;
				}
				if (levelScene[i][j]== (byte)(20)){ //if the object is a pipe
					costByte[i][j] = 5;
				}
				if (enemiesPos[i][j]== (byte)(2)){ //if the object is the platform, give it a high cost
					costByte[i][j] = 3;
				}
				else {
					costByte[i][j] = 0;
				}

			}}
    	
    	
    	status = mario.getStatus();
    	action[Mario.KEY_RIGHT] = true; 
    	action[Mario.KEY_JUMP] = true;
    	if (levelScene[11][13] == enemiesPos[12][14]) {
    		//System.out.println("avoided enemy");
    		action[Mario.KEY_JUMP] = true;
    	}
    	if (levelScene[11][13] != 0 || levelScene[11][12] != 0 ||  DangerOfGap(levelScene))
    	{
    		if (observation.mayMarioJump() || ( !observation.isMarioOnGround() && action[Mario.KEY_JUMP])) {
    			action[Mario.KEY_JUMP] = true;
    		}
    		++trueJumpCounter;
    	} else {
    		action[Mario.KEY_JUMP] = false;
    		trueJumpCounter = 0;
    	}

        if (trueJumpCounter > 16) {
            trueJumpCounter = 0;
            action[Mario.KEY_JUMP] = false;
        }
        Heuristic h = new Heuristic(levelScene[11][11]);
        pathfindAStar(levelScene,levelScene[11][13], levelScene[11][18], h);

        action[Mario.KEY_SPEED] = DangerOfGap(levelScene);
        return action;
    }

    public AGENT_TYPE getType() {
        return Agent.AGENT_TYPE.AI;
    }

    public String getName() {        
    	return name;    
    }

    public void setName(String Name) { 
    	this.name = Name;    
    }
    // side note: 0 is air, -10 in not passable
    
    
    //This is closely following the pathfinding A* pseudo-code in our book (pg. 220-223). 
    public void pathfindAStar(byte[][] graph, byte start, byte end, Heuristic heuristic){ // we need a graph param here. not sure what that is yet.
    	
    // This structure is used to keep track of the information we need for each node.
    	class NodeRecord{
    		byte n;
    		byte[] connection; //list of bytes?
    		int costSoFar;
    		int estTotalCost;
    	}
    	//Initialize the record for the start node.
    	NodeRecord startRecord = new NodeRecord();
    	startRecord.n = start;
    	//startRecord.connection;
    	startRecord.costSoFar =0;
    	startRecord.estTotalCost= heuristic.estimate(start);
    	System.out.println(startRecord.estTotalCost);
    	//Initialize the open and closed lists.
    	LinkedList <NodeRecord> open = new LinkedList<NodeRecord>();
    	if (startRecord!=null)
    		open.add(startRecord);
    	LinkedList <NodeRecord> closed = new LinkedList<NodeRecord>();
    	
    	int startX = 13;
    	int startY = 11;
    	int goalY = 21;
    	int goalX = 11;
    	
    	//Iterate through processing each node.
    	
    	/*
    	while (open.size()>0){
    		//Find the smallest element in the open list.
    		// using the estimatedTotalCost.
    		int current= open.getFirst().estTotalCost;
    		NodeRecord currentNode = open.getFirst();
    		for (int i=0; i < open.size(); i++){
    			if (current> open.get(i).estTotalCost){ 
    				current = open.get(i).estTotalCost;
    				currentNode = open.get(i);}
    		}
    		// end != goal?
    		// If it is the goal node, then terminate.
    		if (currentNode.n == end) break;
    		
    		//Otherwise, get its outgoing connections.
    		// connections = graph.getConnections(current) --> not sure what to do here.
    		
    	}//end of while loop
    	*/
    }
    
    public byte[] getConnections(byte[][] graph, int startX, int startY){
    	byte[] gConnect;
    	if ((startX==13) && (startY==11)){
       	}
		return null;
		}
    
}
