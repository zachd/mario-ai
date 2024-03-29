package tcd.agents;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;

public class QTable {

    public HashMap<WorldState, Action> table;

    public int prevActionIndex = -1;
    public WorldState prevState;
    public float prevReward;
    public Action prevAction;

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
            prevAction = table.get(prevState);
            float oldActionScore = prevAction.getQScore(prevActionIndex) + Params.ALPHA *
                    (reward.getReward() + (Params.GAMMA * action.getQScore(actionIndex)) - prevAction.getQScore(prevActionIndex));
            prevAction.setQScore(prevActionIndex, oldActionScore);

            // Debugging parameters
            if(QLearningAgent.show_debug) {
                System.out.println("\u23BE Current State: " + state);
                System.out.println("\u23B9 Prev action: Mario." + prevAction.action_terms[prevActionIndex]
                        + " (Q: " + prevAction.qScore[prevActionIndex] + ")");
                System.out.println("\u23B9 Reward for prev: " + reward.getReward());
                System.out.println("\u23B9 Next Max Action: " + action.action_terms[actionIndex]
                        + " (Q: " + action.qScore[actionIndex] + ")");
                System.out.println("\u23BF Q Scores: " + Arrays.toString(action.qScore));
            }
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

    /**
     * Print to QTable to file
     */
    public void printToFile() {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new File("qtable.txt"));
        } catch (FileNotFoundException e) {}
        out.println("Actions: " + Arrays.toString(Action.action_terms));
        out.println("\nQ Table:");
        for (HashMap.Entry<WorldState,Action> entry : table.entrySet()) {
            out.println(entry.getKey() + "\t=>\t" + entry.getValue());
        }
        out.close();
    }
}
