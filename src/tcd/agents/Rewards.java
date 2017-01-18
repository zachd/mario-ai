package tcd.agents;

public class Rewards {
    private final float HIT_BY_ENEMY = -100;
    private final float FORWARD = 1;
    private final float BACKWARD = 0;
    private final float FINISH = 500;
    private final float JUMP = 1;

    private float MARIO_PREV_POS;
    private float MARIO_REWARD;

    public Rewards() {
        this.MARIO_REWARD = 0;
        this.MARIO_PREV_POS = 0;
    }

    public void complete() {
        updateReward(FINISH);
    }

    public void directionalReward(float[] position) {
        if (position[1] > this.MARIO_PREV_POS) {
            updateReward(FORWARD);
        } else {
            updateReward(BACKWARD);
        }
        setPos(position[1]);
    }

    public void jumpReward(float[] position) {

    }

    public void setPos(float position) {
        this.MARIO_PREV_POS = position;
    }

    public void updateReward(float reward) {
        this.MARIO_REWARD = this.MARIO_REWARD + reward;
    }

    public float getReward() {
        return this.MARIO_REWARD;
    }
}
