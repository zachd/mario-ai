package tcd.agents;

import ch.idsia.agents.Agent;
import ch.idsia.agents.LearningAgent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.benchmark.tasks.LearningTask;
import ch.idsia.tools.MarioAIOptions;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class QLearningAgent implements LearningAgent {

    private String name;
    private LearningTask learningTask;
    private WorldState state;
    private Rewards marioRewards;
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
        marioRewards = new Rewards();
        q_table = new QTable(marioRewards);
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
        this.marioRewards.directionalReward(environment.getMarioFloatPos());

        // TODO: Do something with the current environment observation

        WorldState state = new WorldState(environment);

        // Update the Q table with the current state
        q_table.update(state);
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
