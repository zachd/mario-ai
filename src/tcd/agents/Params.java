package tcd.agents;

public class Params {
    // Learning Params
    public static final float ALPHA = 0.8f;
    public static final float GAMMA = 0.6f;
    public static final int NUMBER_OF_LEARNS = 10000;
    public static final int NUMBER_OF_MODES = 3;

    // Level Seed Params
    public static final int EASY = 0;
    public static final int HARD = 48;
    public static final int LEVEL_SEED = EASY;

    // Agent State Params
    public static final float NEAR = 10f;
    public static final float MED = 50f;
    public static final float FAR = 100f;

    //Size of near and medium boxes for detecting enemy location
    public static final int ENEMY_NEAR = 2;
    public static final int ENEMY_MED = 5;


    // Reward Params
    public static final float HIT_BY_ENEMY = -50;
    public static final float FORWARD = 10;
    public static final float BACKWARD = -5;
    public static final float FINISH = 100;
    public static final float STUCK = -20;
    public static final float KILL = 10;
    public static final float COIN = 10;

    // Reward Calc Params
    public static final int STUCK_THRESHOLD = 10;
    public static final int STUCK_WINDOW = 3;
}
