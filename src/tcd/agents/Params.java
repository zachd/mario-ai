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
    public static final int HARDER = 99;
    public static final int LEVEL_SEED = HARDER;

    // Agent State Params
    public static final float NEAR = 10f;
    public static final float MED = 50f;
    public static final float FAR = 100f;

    //Size of near and medium boxes for detecting enemy location
    public static final int ENEMY_NEAR = 2;
    public static final int ENEMY_MED = 3;


    // Reward Params
    public static final float HIT_BY_ENEMY = -500;
    public static final float FORWARD = 10;
    public static final float BACKWARD = -2;
    public static final float FINISH = 0;
    public static final float DEAD = -500;
    public static final float STUCK = -10;
    public static final float KILL = 20;
    public static final float COIN = 5;

    // Reward Calc Params
    public static final int STUCK_THRESHOLD = 10;
    public static final int STUCK_WINDOW = 3;
}
