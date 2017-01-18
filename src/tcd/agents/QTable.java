package tcd.agents;


import java.util.HashMap;

public class QTable {

    private HashMap<WorldState, Action> table;

    private int oldActionIndex = -1;
    private WorldState oldState;
    private float oldReward;

    private int newActionIndex;
    private Action newAction;

    public QTable() {
        table = new HashMap<WorldState, Action>();
    }

    /**
     *  Updates the QTable with the Q score for the previous action.
     * @param newState The current WorldState
     */
    public void update(WorldState newState, Reward newReward){

        // Add new state to QTable if it doesn't exist
        if(!table.containsKey(newState)){
            table.put(newState, new Action());
        }

        // Get the best action for the new state
        newAction = table.get(newState);
        newActionIndex = newAction.getBestAction();

        // Set Q Score for the old action
        if(oldActionIndex != -1) {
            Action oldAction = table.get(oldState);
            float oldActionScore = oldAction.getQScore(oldActionIndex) + QLearningAgent.ALPHA *
                    (oldReward + (QLearningAgent.GAMMA * newAction.getQScore(newActionIndex)) - oldAction.getQScore(oldActionIndex));
            oldAction.setQScore(oldActionIndex, oldActionScore);
        }

        // Save state of current state and action for next iteration
        oldState = newState;
        oldActionIndex = newActionIndex;
        oldReward = newReward.getRewardValue();
    }

    /**
     * Returns the current best action to take
     * @return action
     */
    public Action getNewAction(){
        return newAction;
    }

    /**
     * Returns the index of the current best action to take
     * @return index of best action
     */
    public int getNewActionIndex(){
        return newActionIndex;
    }
}
