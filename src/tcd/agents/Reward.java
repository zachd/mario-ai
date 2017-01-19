package tcd.agents;

import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.tools.EvaluationInfo;

public class Reward {
    private final float HIT_BY_ENEMY = -50;
    private final float FORWARD = 10;
    private final float BACKWARD = -1;
    private final float FINISH = 50;
    private final float STUCK = -10;
    private final float KILL = 20;

    private static final int STUCK_THRESHOLD = 20;
    private static final int STUCK_WINDOW = 3;
    private float prev_xpos;
    private float current_reward;

    private int stuck_tick;
    private boolean stuck;
    private boolean moving_forward;
    private int last_distance_travelled;
    private int last_enemies_collided;
    private int last_enemies_killed;

    private byte[][] levelScene;

    /**
     * Constructor for Reward class
     */
    public Reward() {
        current_reward = 0;
        prev_xpos = -1;
        stuck_tick = 0;
        last_distance_travelled = 0;
        last_enemies_collided = 0;
        last_enemies_killed = 0;
    }

    /**
     * Reward function, given environment, will calculate the reward attainable for this state
     * @param environment
     */
    public void calculate(Environment environment) {
        current_reward = 0;

        // Add positive reward if Mario is going right, negative if going left
        directionalReward(environment.getMarioFloatPos()[0]);

        // Add negative reward if Mario gets stuck
        stuckReward(environment);

        // Add negative reward if Mario collides with enemy
        enemyHitReward(environment);

        //senseImmediateEnvironment(environment);

        // Add positive reward for killing an enemy
        killReward(environment);

        // Add large positive reward if Mario has finished
        if(environment.isLevelFinished()){
            updateReward(FINISH);
        }
    }

    public void senseImmediateEnvironment(Environment environment) {
        levelScene = environment.getLevelSceneObservationZ(2);
        for (int i=0; i<levelScene[0].length; i++) {
            for (int j=0; j<levelScene[1].length; j++) {
                if ((levelScene[i][j] == 2)) {
                    //System.out.println("COIN! @ " + i + " and " + j);
                }
            }
        }
//        System.out.println(environment.getMarioFloatPos()[0]);
//        System.out.println(environment.getMarioFloatPos()[1]);
//        if (getReceptiveFieldCellValue((int)environment.getMarioFloatPos()[0], (int)environment.getMarioFloatPos()[1]) == 2) {
//            System.out.println("COIN");
//        }
    }

    public int getReceptiveFieldCellValue(int x, int y)
    {
        if (x < 0 || x >= levelScene.length || y < 0 || y >= levelScene[0].length)
            return 0;
        return levelScene[x][y];
    }

    /**
     * Negative reward if Mario is hit by an emeny
     * @param env the environment
     */
    public void enemyHitReward(Environment env) {
        EvaluationInfo evalInfo = env.getEvaluationInfo();
        if (evalInfo.collisionsWithCreatures > last_enemies_collided) {
            updateReward(HIT_BY_ENEMY);
            last_enemies_collided = evalInfo.collisionsWithCreatures;
        }
    }

    /**
     * Checks introduced to check if Mario is stuck
     * @param environment
     */
    public void stuckReward(Environment environment){
        int distance_travelled = environment.getEvaluationInfo().distancePassedPhys;
        if ((last_distance_travelled < (distance_travelled + STUCK_WINDOW))
                && (last_distance_travelled > (distance_travelled - STUCK_WINDOW)))
            stuck_tick++;
        else
            stuck_tick = 0;

        if(stuck_tick > STUCK_THRESHOLD)
            updateReward(STUCK);

        stuck = stuck_tick > STUCK_THRESHOLD;
        last_distance_travelled = distance_travelled;
    }

    /**
     * Returns positive reward for moving right, negative for moving left
     * @param current_xpos
     */
    public void directionalReward(float current_xpos) {
        if(prev_xpos > 0) {
            if (current_xpos > prev_xpos) {
                updateReward(FORWARD);
                moving_forward = true;
            } else {
                updateReward(BACKWARD);
                moving_forward = false;
            }
        }
        prev_xpos = current_xpos;
    }

    /**
     * Positive reward for killing enemies
     * @param env the enviroment
     */
    public void killReward(Environment env){
        if(env.getKillsTotal() > last_enemies_killed){
            updateReward(KILL);
            last_enemies_killed = env.getKillsTotal();
        }
    }
    /**
     * Updates the reward variable at any one episode
     * @param reward
     */
    public void updateReward(float reward) {
        current_reward += reward;
    }

    /**
     * Returns the current reward value
     * @return float reward_value
     */
    public float getReward() {
        return current_reward;
    }

    /**
     * Returns if Mario is stuck or not
     * @return true if stuck, false if not
     */
    public boolean isStuck(){
        return stuck;
    }

    /**
     * Returns Mario's direction
     * @return true if forward, false if backwards
     */
    public boolean getDirection(){
        return moving_forward;
    }
}
