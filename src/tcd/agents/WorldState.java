package tcd.agents;

import ch.idsia.benchmark.mario.environments.Environment;

/**
 * This object will hold the state of the mario world.
 */
public class WorldState {


    public boolean on_ground;
    public boolean able_to_jump;
    public boolean able_to_shoot;

    public WorldState(){

    }

    public void update(Environment environment) {
        on_ground = environment.isMarioOnGround();
        able_to_jump = environment.isMarioAbleToJump();
        able_to_shoot = environment.isMarioAbleToShoot();
    }
}


