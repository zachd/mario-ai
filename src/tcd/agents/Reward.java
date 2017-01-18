package tcd.agents;

import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.tools.EvaluationInfo;

public class Reward {
    private final float HIT_BY_ENEMY = -100;
    private final float FORWARD = 1;
    private final float BACKWARD = 0;
    private final float FINISH = 500;
    private final float JUMP = 1;

    private static final int STUCK_THRESHOLD = 5;
    private float prev_pos;
    private float reward_value;

    private int stuck_tick;
    private int last_distance_travelled;

    public Reward() {
        reward_value = 0.0f;
        prev_pos = -1;
        stuck_tick = 0;
        last_distance_travelled = 0;
    }

    public void calculate(Environment environment) {
        resetReward();

        // Add reward if Mario is going right
        directionalReward(environment.getMarioFloatPos());
        stuckReward(environment);
        // Add reward if Mario has finished
        if(environment.isLevelFinished()){
            updateReward(FINISH);
        }
        System.out.println("updated reward = "+ reward_value);

    }

    /**
     *
     * @param environment
     */
    public void stuckReward(Environment environment){
        EvaluationInfo evalInfo = environment.getEvaluationInfo();
        int distance_travelled = evalInfo.distancePassedPhys;
        if(distance_travelled == last_distance_travelled){
            stuck_tick ++;
        }
        else{
            stuck_tick = 0;
        }

        if(stuck_tick > STUCK_THRESHOLD){
            updateReward(-10);
        }
        last_distance_travelled = distance_travelled;
    }

    public void directionalReward(float[] position) {
        if(prev_pos > 0) {
            if (position[1] > prev_pos) {
                updateReward(FORWARD);
            } else {
                updateReward(BACKWARD);
            }
        }
        prev_pos = position[1];
    }

    public void jumpReward(float[] position) {

    }


    public void resetReward() {
        reward_value = 0.0f;
    }

    public void updateReward(float reward) {
        reward_value += reward;
    }

    public float getRewardValue() {
        return reward_value;
    }
}
