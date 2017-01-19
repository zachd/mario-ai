package tcd.agents;

import ch.idsia.agents.Agent;
import ch.idsia.agents.LearningAgent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.benchmark.tasks.LearningTask;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.MarioAIOptions;

import java.util.Arrays;
import java.util.logging.Logger;

public class QLearningAgent implements LearningAgent {

    private String name;
    private static LearningTask learningTask;
    private WorldState state;
    private Reward reward;
    private QTable q_table;
    public QLearningAgent() {
        setName("QLearning Agent");
    }

    public static final float ALPHA = 1.0f;
    public static final float GAMMA = 0.5f;
    public static final int NUMBER_OF_LEARNS = 1000;

    public static boolean game_started = false;
    public static boolean learning_complete = false;

    /**
     * Main task to test the Q-Learning Agent
     * @param args
     */
    public static void main(String[] args) {
        // Set up options
        MarioAIOptions marioAIOptions = new MarioAIOptions(args);
        //marioAIOptions.setArgs("-ls 48");
        LearningAgent agent = new QLearningAgent();
        marioAIOptions.setAgent(agent);

        // Learning task
        learningTask = new LearningTask(marioAIOptions);
        marioAIOptions.setVisualization(false);
        System.out.println("INIT STATE");
        agent.init();
        System.out.println("LEARN STATE");
        agent.learn();

        // Gameplay task
        //learning_complete = true;
        System.out.println("GAMEPLAY STATE");
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
        reward = new Reward();
        q_table = new QTable();
    }

    /**
     * Run by the Learning Track so the agent can learn from 10000 trials
     */
    @Override
    public void learn() {
        int kills = 0, wins = 0, time = 0, coins = 0, score = 0;
        for(int i=0; i<NUMBER_OF_LEARNS;i++) {
            learningTask.runSingleEpisode(1);
            // Add eval data
            EvaluationInfo eval = learningTask.getEnvironment().getEvaluationInfo();
            kills += eval.killsTotal;
            wins += (eval.marioStatus == Mario.STATUS_WIN ? 1 : 0);
            time += eval.timeSpent;
            coins += eval.coinsGained;
            score += eval.computeWeightedFitness();
            if (i % (NUMBER_OF_LEARNS / 10) == 0)
                System.out.println("Learning: " + (int) ((float) i / NUMBER_OF_LEARNS * 100) + "%");
        }
        System.out.println("\nLEARNING RESULTS");
        System.out.println("# of Wins: " + wins);
        System.out.println("Avg Kills: " + (float)kills/NUMBER_OF_LEARNS + " | Avg Time: " +
                (float)time/NUMBER_OF_LEARNS + "\nAvg Coins: " + (float)coins/NUMBER_OF_LEARNS +
                " | Avg Score: " + (float)score/NUMBER_OF_LEARNS + "\n");
    }

    /**
     * Called every tick while a game is running
     * @param environment Current game environment
     */
    @Override
    public void integrateObservation(Environment environment) {
        if (game_started) {
            // Update the state reward with the environment
            reward.calculate(environment);
            // Get the worldstate representation of the environment
            WorldState state = new WorldState(environment, reward);
            // Update the Q table with the current state
            q_table.update(state, reward);
        } else if (environment.isMarioOnGround()) {
            game_started = true;
        }
    }

    /**
     * The action Mario performs in the game after each observation
     * @return A boolean array of keys to press
     */
    @Override
    public boolean[] getAction() {
        Action new_action;
        int action_index = 0;
        try {
            new_action = q_table.getNewAction();
            action_index = q_table.getNewActionIndex();
            return new_action.toMarioAction(action_index);
        } catch (Exception e) {
            return (new Action()).toMarioAction(action_index);
        }
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
