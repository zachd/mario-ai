package tcd.agents;

import ch.idsia.benchmark.mario.environments.Environment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This object will hold the state of the mario world.
 */
public class WorldState {

    public boolean on_ground;
    public boolean able_to_jump;
    public boolean able_to_shoot;
    public int mode;

    public WorldState(Environment environment) {
        on_ground = environment.isMarioOnGround();
        able_to_jump = environment.isMarioAbleToJump();
        able_to_shoot = environment.isMarioAbleToShoot();
        mode = environment.getMarioMode();
    }

    @Override
    public boolean equals(Object comparison) {
        for(Field field : WorldState.class.getDeclaredFields()) {
            try {
                if (!field.get(this).equals(field.get(comparison)))
                    return false;
            } catch (IllegalAccessException e) {}
        }
        return true;
    }

    @Override
    public int hashCode(){
        ArrayList<Object> fields = new ArrayList<Object>();
        for(Field field : WorldState.class.getDeclaredFields()) {
            try {
                fields.add(field.get(this));
            } catch (IllegalAccessException e) {}
        }
        return Objects.hash(fields);
    }
}


