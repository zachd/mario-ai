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
    private boolean initialRun = true;
    private QTable q_table;

    public QLearningAgent() {
        setName("QLearning Agent");
    }

    public static final float ALPHA = 0.1f;
    public static final float GAMMA = 0.5f;

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
        Q(s, a) ← Q(s, a) + α[r(s, a) + γ max a Q(s', a) - Q(s,a)]
        s ← s'
      until s is terminal state
    */
    public void init() {
        // TODO: Tells our agent to initialise
        marioRewards = new Rewards();
        q_table = new QTable();
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
        if (initialRun) {
            marioRewards.setPos(environment.getMarioFloatPos());
            initialRun = false;
        }

        marioRewards.checkDirection(environment.getMarioFloatPos());

        // TODO: Do something with the current environment observation

        WorldState state = new WorldState(environment);

        System.out.println(marioRewards.getReward());
        // Update the Q table with the current state
        q_table.update(state);
    }

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
