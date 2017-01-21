package tcd.agents;

public class Params {
    // Learning Params
    public static final float ALPHA = 0.6f;
    public static final float GAMMA = 0.6f;
    public static final int NUMBER_OF_LEARNS = 1000;
    public static final int NUMBER_OF_MODES = 3;
    public static final boolean PRINT_TO_FILE = true;
    public static final boolean SHOW_GAMEPLAY_DEBUG = true;

    // Level Seed Params
    public static final int EASY = 0;
    public static final int HARD = 48;
    public static final int LEVEL_SEED = HARD;

    // Agent State Params
    public static final float NEAR = 10f;
    public static final float MED = 50f;
    public static final float FAR = 100f;

    //Size of near and medium boxes for detecting enemy location
    public static final int ENEMY_NEAR = 1;
    public static final int ENEMY_MED = 3;


    // Reward Params
    public static final float HIT_BY_ENEMY = -100;
    public static final float FORWARD = 10;
    public static final float BACKWARD = -5;
    public static final float FINISH = 100;
    public static final float DEAD = -100;
    public static final float STUCK = -20;
    public static final float KILL = 0;
    public static final float COIN = 10;

    // Reward Calc Params
    public static final int STUCK_THRESHOLD = 10;
    public static final int STUCK_WINDOW = 3;
}
