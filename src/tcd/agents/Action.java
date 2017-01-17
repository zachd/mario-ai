package tcd.agents;

import ch.idsia.benchmark.mario.engine.sprites.Mario;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 */
public class Action {

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

    private int[][] action_list = {RIGHT, RIGHT_SPEED, RIGHT_JUMP, RIGHT_JUMP_SPEED,
                                    LEFT, LEFT_SPEED, LEFT_JUMP, LEFT_JUMP_SPEED,
                                        JUMP, JUMP_SPEED};

    private float[] qScore; //holds the qscores for each action
    Random rand;

    public Action() {
        qScore = new float[action_list.length]; //initialise q_scores to 0
    }

    /**
     * updates the qScore for an action
     * @param newScore the new q score for the action
     * @param actionIndex the index of the action
     */
    public void updateQScore(float newScore, int actionIndex){
        qScore[actionIndex] = newScore;
    }


    /**
     * gets the best action for this objects q score list. If there is more than one action with the same score then it
     * chooses one of these at random
     * @return a boolean representation of the best action
     */
    public boolean[] getBestAction() {
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
        int[] chosenAction = bestActionList.get(randomIndex);
        boolean[] bestAction = qActionToMarioAction(chosenAction);

        return bestAction;
    }

    /**
     * Cast our own action definitions above to an action boolean array that the mario engine can use
     * @param actions an int array that is the action we want to cast to a mario action
     * @return a boolean array representing the mario action
     */
    public boolean[] qActionToMarioAction(int[] actions){
        boolean[] marioAction = new boolean[6];
        for(int action : actions){
            marioAction[action] = true;
        }
        return marioAction;
    }
}
