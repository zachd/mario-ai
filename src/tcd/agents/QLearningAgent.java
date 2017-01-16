package tcd.agents;

import ch.idsia.agents.Agent;
import ch.idsia.agents.LearningAgent;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.tasks.LearningTask;

public class QLearningAgent implements LearningAgent {

    private String name;

    public QLearningAgent() {
        setName("QLearning Agent");
    }


    @Override
    public void init() {

    }

    @Override
    public void learn() {

    }

    @Override
    public boolean[] getAction() {
        return null;
    }


    @Override
    public void giveIntermediateReward(float intermediateReward) {

    }

    @Override
    public void newEpisode() {

    }

    @Override
    public void setObservationDetails(int rfWidth, int rfHeight, int egoRow, int egoCol) {

    }

    @Override
    public void setLearningTask(LearningTask learningTask) {

    }

    @Override
    public void integrateObservation(Environment environment) {

    }

    @Override
    public Agent getBestAgent() {
        return null;
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
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

}
