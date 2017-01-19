package tcd.agents;

import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.tools.EvaluationInfo;

public class Reward {
    private final float HIT_BY_ENEMY = -50;
    private final float FORWARD = 10;
    private final float BACKWARD = -1;
    private final float FINISH = 100;
    private final float JUMP = 1;
    private final float STUCK = -10;
    private final float KILL = 25;

    private static final int STUCK_THRESHOLD = 5;
    private float prev_pos;
    private float current_reward;

    private int stuck_tick;
    private int last_distance_travelled;

    private int enemies_collided;
    private int enemies_killed;


    /**
     * Constructor for Reward class
     */
    public Reward() {
        current_reward = 0;
        prev_pos = -1;
        stuck_tick = 0;
        last_distance_travelled = 0;
        enemies_collided = 0;
        enemies_killed = 0;
    }

    /**
     * Reward function, given environment, will calculate the reward attainable for this state
     * @param environment
     */
    public void calculate(Environment environment) {
        current_reward = 0;

        // Add +1 reward if Mario is going right, -0.5 if going left
        directionalReward(environment.getMarioFloatPos());

        // Add -0.5 reward if Mario gets stuck
        stuckReward(environment);

        // Minus -100 reward if Mario collides with enemy
        enemyHitReward(environment);

        senseImmediateEnvironment(environment);

        //Add 30 for killing an enemy
        killReward(environment);
        // Add +100 reward if Mario has finished
        if(environment.isLevelFinished()){
            updateReward(FINISH);
        }
    }

    public void senseImmediateEnvironment(Environment environment) {
        byte[][] levelScene = environment.getLevelSceneObservationZ(0);
        for (int i = 0; i < levelScene.length; i++) {
            for (int j = 0; j < levelScene.length; j++) {
                if (levelScene[i][j] != 0) {
                    //System.out.println(levelScene[i][j]);
                }
            }
        }
        //System.out.println();
    }

    public void enemyHitReward(Environment environment) {
        EvaluationInfo evalInfo = environment.getEvaluationInfo();
        if (enemies_collided < evalInfo.collisionsWithCreatures) {
            updateReward(HIT_BY_ENEMY);
            enemies_collided = evalInfo.collisionsWithCreatures;
        }
    }

    /**
     * Checks introduced to check if Mario is stuck
     * @param environment
     */
    public void stuckReward(Environment environment){
        EvaluationInfo evalInfo = environment.getEvaluationInfo();
        int distance_travelled = evalInfo.distancePassedPhys;
        if ((last_distance_travelled < (distance_travelled + 2)) && (last_distance_travelled > (distance_travelled - 2))) {
            stuck_tick++;
        }
        else {
            stuck_tick = 0;
        }

        if(stuck_tick > STUCK_THRESHOLD){
            updateReward(STUCK);
            //System.out.println("STUCK");
        }
        last_distance_travelled = distance_travelled;
    }

    /**
     * Factoring in rewards for Mario heading towards the goal (heading right)
     * @param position
     */
    public void directionalReward(float[] position) {
        if(prev_pos > 0) {
            if (position[0] > prev_pos) {
                updateReward(FORWARD);
            } else {
                updateReward(BACKWARD);
            }
        }
        prev_pos = position[0];
    }

    /**
     * Reward for killing monsters
     * @param env the enviroment
     */
    public void killReward(Environment env){
        if(enemies_killed < env.getKillsTotal()){
            updateReward(KILL);
            enemies_killed = env.getKillsTotal();
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
     * Returns the rewards value
     * @return reward_value
     */
    public float getReward() {
        return current_reward;
    }
}
