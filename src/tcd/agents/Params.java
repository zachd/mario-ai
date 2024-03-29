package tcd.agents;

public class Params {
    // Learning Params
    public static final float ALPHA = 0.7f;
    public static final float GAMMA = 0.6f;
    public static final int NUMBER_OF_LEARNS = 1000;
    public static final boolean PRINT_TO_FILE = true;
    public static final boolean SHOW_GAMEPLAY_DEBUG = false;

    // Level Seed Params
    public static final int EASY = 0;
    public static final int HARD = 48;
    public static final int HARDER = 99;
    public static final int LEVEL_SEED = EASY;


    // Size of near and medium boxes for detecting enemy location and obstacles
    public static final int VIEW_NEAR = 1;
    public static final int VIEW_MED = 3;
    public static final int VIEW_FAR = 6;


    // Reward Params
    public static final float HIT_BY_ENEMY = -100;
    public static final float FORWARD = 10;
    public static final float BACKWARD = -5;
    public static final float DEAD = -100;
    public static final float STUCK = -10;
    public static final float KILL = 20;
    public static final float COIN = 10;

    // Reward Calc Params
    public static final int STUCK_THRESHOLD = 10;
    public static final int STUCK_WINDOW = 3;
    public static final int ABOVE_MARIO_SIZE = 1; //used for calculating enemy positions
    public static final int BELOW_MARIO_SIZE = 1; //used for calculating enemy positions
}
