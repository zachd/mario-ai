package tcd.agents;

import ch.idsia.agents.Agent;
import ch.idsia.agents.LearningAgent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.benchmark.tasks.LearningTask;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.MarioAIOptions;

public class QLearningAgent implements LearningAgent {

    private String name;
    private static LearningTask learningTask;
    private WorldState state;
    private Reward reward;
    private QTable q_table;
    private static MarioAIOptions marioAIOptions;
    public QLearningAgent() {
        setName("QLearning Agent");
    }


    public static boolean game_started = false;
    public static boolean show_debug = false;

    /**
     * Main task to test the Q-Learning Agent
     * @param args
     */
    public static void main(String[] args) {
        // Set up options
        marioAIOptions = new MarioAIOptions(args);
        marioAIOptions.setArgs("-ls " + Params.LEVEL_SEED);
        LearningAgent agent = new QLearningAgent();
        marioAIOptions.setAgent(agent);
        //marioAIOptions.setLevelType(0);

        // Learning task
        learningTask = new LearningTask(marioAIOptions);
        marioAIOptions.setVisualization(false);
        System.out.println("INIT STATE");
        agent.init();
        System.out.println("LEARN STATE");
        agent.learn();

        // Gameplay task
        System.out.println("GAMEPLAY STATE");
        show_debug = Params.SHOW_GAMEPLAY_DEBUG;
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
        int kills = 0, wins = 0, time = 0, coins = 0, score = 0, timeouts = 0;
        int progress_counter = 0;
        for (int j = 0; j < Params.NUMBER_OF_MODES; j++) {
            //marioAIOptions.setMarioMode(j);
            for (int i = 0; i < Params.NUMBER_OF_LEARNS / Params.NUMBER_OF_MODES; i++) {
                learningTask.runSingleEpisode(1);
                // Add eval data
                EvaluationInfo eval = learningTask.getEnvironment().getEvaluationInfo();
                kills += eval.killsTotal;
                if (eval.marioStatus == Mario.STATUS_WIN) {
                    System.out.println("#" + progress_counter + " Mario Won! Wins: " + (++wins));
                } else if (eval.marioStatus == Mario.STATUS_DEAD && eval.timeLeft == 0) {
                    System.out.println("#" + progress_counter + " Mario Stuck! Timeouts: " + (++timeouts));
                }
                time += eval.timeSpent;
                coins += eval.coinsGained;
                score += eval.computeWeightedFitness();
                if (progress_counter % (Params.NUMBER_OF_LEARNS / 10) == 0)
                    System.out.println("Learning: " + (int) ((float) progress_counter / Params.NUMBER_OF_LEARNS * 100) + "%");
                progress_counter++;
            }
        }
        System.out.println("\nLEARNING RESULTS");
        System.out.println("# Level Wins: " + wins + " | # Timeouts: " + timeouts);
        System.out.println("Avg Kills: " + (float) kills / Params.NUMBER_OF_LEARNS + " | Avg Time: " +
                (float) time / Params.NUMBER_OF_LEARNS + "\nAvg Coins: " + (float) coins / Params.NUMBER_OF_LEARNS +
                " | Avg Score: " + (float) score / Params.NUMBER_OF_LEARNS + "\n");
        if(Params.PRINT_TO_FILE)
            q_table.printToFile();
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
