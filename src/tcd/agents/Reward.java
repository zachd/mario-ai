package tcd.agents;

import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.tools.EvaluationInfo;

public class Reward {
    private float prev_xpos;
    private float current_reward;

    private int stuck_tick;
    private boolean stuck;
    private boolean moving_forward;
    private int last_distance_travelled;
    private int last_enemies_collided;
    private int last_enemies_killed;
    private int prev_coins_collected;

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
        prev_coins_collected = 0;
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

        // Add positive reward for collecting coin
        coinReward(environment);

        // Add positive reward for killing an enemy
        killReward(environment);

        // Add large negative reward if Mario dies
        if(environment.getMarioStatus() == Mario.STATUS_DEAD){
            updateReward(Params.DEAD);
        }

        // Add large positive reward if Mario has finished
        if(environment.getMarioStatus() == Mario.STATUS_WIN){
            updateReward(Params.FINISH);
        }
    }

    /**
     * Negative reward if Mario is hit by an emeny
     * @param env the environment
     */
    public void enemyHitReward(Environment env) {
        EvaluationInfo evalInfo = env.getEvaluationInfo();
        if (evalInfo.collisionsWithCreatures > last_enemies_collided) {
            updateReward(Params.HIT_BY_ENEMY);
            last_enemies_collided = evalInfo.collisionsWithCreatures;
        }
    }

    /**
     * Checks introduced to check if Mario is stuck
     * @param environment
     */
    public void stuckReward(Environment environment){
        int distance_travelled = environment.getEvaluationInfo().distancePassedPhys;
        if ((last_distance_travelled < (distance_travelled + Params.STUCK_WINDOW))
                && (last_distance_travelled > (distance_travelled - Params.STUCK_WINDOW)))
            stuck_tick++;
        else
            stuck_tick = 0;

        if(stuck_tick > Params.STUCK_THRESHOLD)
            updateReward(Params.STUCK);

        stuck = stuck_tick > Params.STUCK_THRESHOLD;
        last_distance_travelled = distance_travelled;
    }

    /**
     * Returns positive reward for moving right, negative for moving left
     * @param current_xpos
     */
    public void directionalReward(float current_xpos) {
        if(prev_xpos > 0) {
            if (current_xpos > prev_xpos) {
                updateReward(Params.FORWARD);
                moving_forward = true;
            } else {
                updateReward(Params.BACKWARD);
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
        if(env.getKillsByStomp() > last_enemies_killed){
            updateReward(Params.KILL);
            last_enemies_killed = env.getKillsByStomp();
        }
    }

    /**
     * Positive reward for collecting coins
     * @param environment
     */
    public void coinReward(Environment environment) {
        EvaluationInfo coinCollected = environment.getEvaluationInfo();
        int coins = coinCollected.coinsGained;
        if (coins > prev_coins_collected) {
            updateReward(Params.COIN);
            prev_coins_collected = coins;
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
