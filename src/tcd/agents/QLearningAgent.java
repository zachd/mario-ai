package tcd.agents;

import ch.idsia.agents.Agent;
import ch.idsia.agents.LearningAgent;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.benchmark.tasks.LearningTask;
import ch.idsia.tools.MarioAIOptions;

public class QLearningAgent implements LearningAgent {

    private String name;
    private LearningTask learningTask;
    private WorldState state;
    private Reward reward;
    private QTable q_table;

    public QLearningAgent() {
        setName("QLearning Agent");
    }

    public static final float ALPHA = 0.1f;
    public static final float GAMMA = 0.5f;

    /**
     * Main task to test the Q-Learning Agent
     * @param args
     */
    public static void main(String[] args) {
        // Set up options
        MarioAIOptions marioAIOptions = new MarioAIOptions(args);
        LearningAgent agent = new QLearningAgent();
        marioAIOptions.setAgent(agent);

        // Learning task
        LearningTask learningTask = new LearningTask(marioAIOptions);
        agent.init();
        agent.learn();

        // Gameplay task
        marioAIOptions.setVisualization(true);
        BasicTask basicTask = new BasicTask(marioAIOptions);
        basicTask.setOptionsAndReset(marioAIOptions);
        basicTask.runSingleEpisode(1);
    }

    /**
     * Run by the Learning Track to initialise our agent
     */
    @Override
    public void init() {
        System.out.println("INIT STATE");
        reward = new Reward();
        q_table = new QTable();
    }

    /**
     * Run by the Learning Track so the agent can learn from 10000 trials
     */
    @Override
    public void learn() {
        System.out.println("LEARNING STATE");
    }

    /**
     * Called every tick while a game is running
     * @param environment Current game environment
     */
    @Override
    public void integrateObservation(Environment environment) {

        // Get the worldstate representation of the environment
        WorldState state = new WorldState(environment);

        // Update the state reward with the environment
        reward.update(environment);

        // Update the Q table with the current state
        q_table.update(state, reward);
    }

    /**
     * The action Mario performs in the game after each observation
     * @return A boolean array of keys to press
     */
    @Override
    public boolean[] getAction() {
        // TODO: Return action back to environment
        Action new_action = q_table.getNewAction();
        boolean[] mario_action = new_action.toMarioAction(q_table.getNewActionIndex());
        return mario_action;
    }


    @Override
    public Agent getBestAgent() {
        return this;
    }

    @Override
    public void setLearningTask(LearningTask learningTask) {
        this.learningTask = learningTask;
    }

    @Override
    public void setObservationDetails(int rfWidth, int rfHeight, int egoRow, int egoCol) {

    }

    @Override
    public void giveReward(float reward) {

    }

    @Override
    public void setEvaluationQuota(long num) {

    }

    @Override
    public void reset() {

    }

    @Override
    public void giveIntermediateReward(float intermediateReward) {

    }

    @Override
    public void newEpisode() {

    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
