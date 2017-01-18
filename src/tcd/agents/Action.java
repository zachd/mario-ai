package tcd.agents;

import ch.idsia.benchmark.mario.engine.sprites.Mario;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 */
public class Action {

    private int[] NOTHING = {};
    private int[] RIGHT = {Mario.KEY_RIGHT};
    private int[] RIGHT_SPEED = {Mario.KEY_RIGHT, Mario.KEY_SPEED};
    private int[] RIGHT_JUMP = {Mario.KEY_RIGHT, Mario.KEY_JUMP};
    private int[] RIGHT_JUMP_SPEED = {Mario.KEY_RIGHT, Mario.KEY_SPEED, Mario.KEY_JUMP};

    private int[] LEFT = {Mario.KEY_LEFT};
    private int[] LEFT_SPEED = {Mario.KEY_LEFT, Mario.KEY_SPEED};
    private int[] LEFT_JUMP = {Mario.KEY_LEFT, Mario.KEY_JUMP};
    private int[] LEFT_JUMP_SPEED = {Mario.KEY_LEFT, Mario.KEY_SPEED, Mario.KEY_JUMP};

    private int[] JUMP = {Mario.KEY_JUMP};
    private int[] JUMP_SPEED = {Mario.KEY_JUMP, Mario.KEY_SPEED};

    private int[][] action_list = {NOTHING,
            RIGHT, RIGHT_SPEED, RIGHT_JUMP, RIGHT_JUMP_SPEED,
            LEFT, LEFT_SPEED, LEFT_JUMP, LEFT_JUMP_SPEED,
            JUMP, JUMP_SPEED};

    public float[] qScore; //holds the qscores for each action
    Random rand;

    public Action() {
        qScore = new float[action_list.length]; //initialise q_scores to 0
    }

    /**
     * Sets the qScore for an action
     * @param actionIndex the index of the action
     * @param newScore the new q score for the action
     */
    public void setQScore(int actionIndex, float newScore) {
        qScore[actionIndex] = newScore;
    }

    /**
     * Gets the qScore of an action
     * @param actionIndex the index of the action
     * @return qScore
     */
    public float getQScore(int actionIndex) {
        return qScore[actionIndex];
    }

    /**
     * Gets the best action for this objects q score list. If there is more than one action with the same score then it
     * chooses one of these at random
     * @return a boolean representation of the best action
     */
    public int getBestAction() {
        ArrayList<int[]> bestActionList = new ArrayList();//an array list that stores the indexes of the actions with the highest qscore
        float bestScore = 0;

        //get the best qScore in the list of qScores
        for(int i = 0; i< qScore.length; i++) {
            if (qScore[i] >= bestScore) {
                bestScore = qScore[i];
            }
        }
        //get all the actions with the best score (could be multiple)
        for(int i = 0; i< qScore.length; i++) {
            if (qScore[i] == bestScore) {
                bestActionList.add(action_list[i]); //add the corresponding action from action_list
            }
        }
        //choose one of the bestActionList at random
        rand = new Random();
        int randomIndex = rand.nextInt(bestActionList.size());
        return randomIndex;
    }

    /**
     * Cast our own action definitions above to an action boolean array that the mario engine can use
     * @param actionIndex the index of the action we want to cast to a Mario action
     * @return a boolean array representing the mario action
     */
    public boolean[] toMarioAction(int actionIndex){
        boolean[] marioAction = new boolean[6];
        for(int action : action_list[actionIndex]){
            marioAction[action] = true;
        }
        return marioAction;
    }
}
