package tcd.agents;

import ch.idsia.benchmark.mario.environments.Environment;

public class Reward {
    private final float HIT_BY_ENEMY = -100;
    private final float FORWARD = 1;
    private final float BACKWARD = 0;
    private final float FINISH = 500;
    private final float JUMP = 1;

    private float prev_pos;
    private float reward_value;

    public Reward() {
        reward_value = 0.0f;
        prev_pos = 0.0f;
    }

    public void update(Environment environment) {
        resetReward();

        // Add reward if Mario is going right
        directionalReward(environment.getMarioFloatPos());

        // Add reward if Mario has finished
        if(environment.isLevelFinished()){
            updateReward(FINISH);
        }

    }


    public void directionalReward(float[] position) {
        if (position[1] > prev_pos) {
            updateReward(FORWARD);
        } else {
            updateReward(BACKWARD);
        }
        setPos(position[1]);
    }

    public void jumpReward(float[] position) {

    }

    public void setPos(float position) {
        prev_pos = position;
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
