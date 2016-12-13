package ucsc_mario_astar;

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
	
	int trueJumpCounter = 0;
    int trueSpeedCounter = 0;

    public UCSC_AStarAgent()
    {
        super("UCSC_AStarAgent");
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
            for(int y = 12; y < 22; ++y)
            {
                if  (levelScene[y][x] != 0)
                    flag = false;
            }
            if (flag && levelScene[12][11] != 0)
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
        
    	status = mario.getStatus();
        action[Mario.KEY_RIGHT] = true; 
        action[Mario.KEY_JUMP] = true;
        if (levelScene[11][13] == enemiesPos[12][14]) {
        	System.out.println("avoided enemy");
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
}
