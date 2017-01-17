package tcd.agents;

public class Rewards {
    public final float HIT_BY_ENEMY = -100;
    public final float FORWARD = 1;
    public final float BACKWARD = 0;
    public final float FINISH = 500;

    public float[] MARIO_PREV_POS;
    public float MARIO_REWARD;

    public Rewards() {
        this.MARIO_REWARD = 0;
    }

    public void complete() {
        updateReward(FINISH);
    }

    public void checkDirection(float[] position) {
        if (position[1] > this.MARIO_PREV_POS[1]) {
            updateReward(FORWARD);
        } else {
            updateReward(BACKWARD);
        }
    }

    public void setPos(float[] pos) {
        this.MARIO_PREV_POS = pos;
    }

    public void updateReward(float reward) {
        this.MARIO_REWARD = this.MARIO_REWARD + reward;
    }

    public float getReward() {
        return this.MARIO_REWARD;
    }
}
