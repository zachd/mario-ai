package tcd.agents;

import ch.idsia.agents.Agent;
import ch.idsia.agents.LearningAgent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.benchmark.tasks.LearningTask;
import ch.idsia.tools.MarioAIOptions;

import java.util.Hashtable;

public class QLearningAgent implements LearningAgent {

    private String name;
    private LearningTask learningTask;
    private WorldState state;
    private Hashtable<WorldState, int[]> q_table;

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
        state = new WorldState();
        System.out.println("INIT STATE");
    }
    @Override
    public void learn() {
        System.out.println("LEARNING STATE");
        // TODO: Tells our agent to start learning from 1000 trials
    }

    @Override
    public void integrateObservation(Environment environment) {
        state.update(environment);
        System.out.println("State: " + state);
        // TODO: Do something with the current environment observation
    }

    @Override
    public boolean[] getAction() {
        // TODO: Return action back to environment

        boolean[] action = new boolean[6];
        action[Mario.KEY_RIGHT] = true;

        System.out.println("Action: " + action);
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
