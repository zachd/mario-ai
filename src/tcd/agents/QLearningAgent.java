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
    private Hashtable<WorldState, int[]> q_table;
    private ArrayList<Integer> rewards_table;

    private float[] MARIO_POSITION;
    private float[] ENEMY_POSITION;
    private boolean ENEMIES = false;

    public QLearningAgent() {
        setName("QLearning Agent");
    }

    public static void main(String[] args) {
        final MarioAIOptions marioAIOptions = new MarioAIOptions(args);
        Agent agent = new QLearningAgent();
        marioAIOptions.setAgent(agent);

        final BasicTask basicTask = new BasicTask(marioAIOptions);
        marioAIOptions.setVisualization(true);
        basicTask.doEpisodes(1, true, 1);
    }

    @Override/*
    || Q Learning Algorithm ||
    Initialise Q(s, a) arbitrarily
    for each episode
      initialise s
      repeat ( for each step of episode)
        choose a from s using pi derived from Q
        perform a, observe r, s'
        Q(s, a) ← Q(s, a) + α[r + γ maxa'Q'(s', a') - Q(s,a)]
        s ← s
      until s is terminal state
    */
    public void init() {
        // TODO: Tells our agent to initialise
        q_table = new Hashtable<WorldState, int[]>();
        rewards_table = new ArrayList<Integer>();
        //state = new WorldState();
        System.out.println("INIT STATE");
    }
    @Override
    public void learn() {
        // TODO: Tells our agent to start learning from 1000 trials
        System.out.println("LEARNING STATE");
    }

    @Override
    public void integrateObservation(Environment environment) {
        //state.update(environment);
        /*
        MARIO_POSITION = environment.getMarioFloatPos();
        ENEMY_POSITION = environment.getEnemiesFloatPos();

        if (MARIO_POSITION[0] + 10 > ENEMY_POSITION[0]) {
            ENEMIES = true;
        } else if (MARIO_POSITION[0] < ENEMY_POSITION[0]) {
            ENEMIES = false;
        }
        //System.out.println("X: " + MARIO_POSITION[0]);
        //System.out.println("Y: " + MARIO_POSITION[1]);
        //System.out.println();

        byte[][] enemies = environment.getEnemiesObservationZ(2);
        byte[][] obstacles = environment.getLevelSceneObservationZ(2);

        //System.out.println("State: " + state);
        // TODO: Do something with the current environment observation
        */

        WorldState state = new WorldState(environment);
        int[] q_scores = {0, 0, 0, 0, 0, 0};

        // Add to Q Table
        if(!q_table.containsKey(state)) {
            q_table.put(state, q_scores);
        }

        System.out.println("Q Table: " + Arrays.asList(q_table));
    }

    @Override
    public boolean[] getAction() {
        // TODO: Return action back to environment

        boolean[] action = new boolean[6];
        if (ENEMIES == true) {
            action[Mario.KEY_JUMP] = true;
            action[Mario.KEY_RIGHT] = true;
        } else {
            action[Mario.KEY_RIGHT] = true;
        }
        //System.out.println("Action: " + action);
        return action;
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
