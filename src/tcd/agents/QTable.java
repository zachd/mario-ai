package tcd.agents;


import java.util.HashMap;

public class QTable {

    private HashMap<WorldState, Action> table;

    private int prevActionIndex = -1;
    private WorldState prevState;
    private float prevReward;

    private int actionIndex;
    private Action action;

    public QTable() {
        table = new HashMap<WorldState, Action>();
    }

    /**
     *  Updates the QTable with the Q score for the previous action.
     * @param state The current WorldState
     * @param reward The current Reward
     */
    public void update(WorldState state, Reward reward){

        // Add new state to QTable if it doesn't exist
        if(!table.containsKey(state)){
            table.put(state, new Action());
        }

        // Get the best action for the new state
        action = table.get(state);
        actionIndex = action.getBestAction();

        // Set Q Score for the old action
        if(prevActionIndex != -1) {
            Action oldAction = table.get(prevState);
            float oldActionScore = oldAction.getQScore(prevActionIndex) + QLearningAgent.ALPHA *
                    (prevReward + (QLearningAgent.GAMMA * action.getQScore(actionIndex)) - oldAction.getQScore(prevActionIndex));
            oldAction.setQScore(prevActionIndex, oldActionScore);
        }

        // Save state of current state and action for next iteration
        prevState = state;
        prevActionIndex = actionIndex;
        prevReward = reward.getReward();
    }

    /**
     * Returns the current best action to take
     * @return action
     */
    public Action getNewAction(){
        return action;
    }

    /**
     * Returns the index of the current best action to take
     * @return index of best action
     */
    public int getNewActionIndex(){
        return actionIndex;
    }
}
