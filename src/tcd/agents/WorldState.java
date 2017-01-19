package tcd.agents;

import ch.idsia.benchmark.mario.environments.Environment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This object will hold the state of the mario world.
 */
public class WorldState {

    // Public fields are included in WorldState equals/hashCode
    public boolean on_ground;
    public boolean able_to_jump;
    public boolean able_to_shoot;
    public boolean direction;
    public int enemies_infront;
    public int enemies_behind;
    public int mode;

    // Private fields can be used for calculation or storage
    private float mario_x = 0;
    private float mario_y = 0;

    public WorldState(Environment environment) {
        on_ground = environment.isMarioOnGround();
        able_to_jump = environment.isMarioAbleToJump();
        able_to_shoot = environment.isMarioAbleToShoot();
        direction = environment.getMarioFloatPos()[0] > mario_x;
        mode = environment.getMarioMode();
        mario_x = environment.getMarioFloatPos()[0];
        mario_y = environment.getMarioFloatPos()[1];
        updateEnemyPosition(environment);
    }

    /**
     * updates enemies_infromt and enemies_behind with the number of enemies on the screen
     * @param environment the current environment
     */
    public void updateEnemyPosition(Environment environment){
        enemies_infront = 0;
        enemies_behind = 0;
        float[] enemies = environment.getEnemiesFloatPos(); //{enemy1_type,enemy1_xpos,enemy1_ypos, enemy2_type,enemy2_xpos..}
        for(int i=0;i<enemies.length; i+=3){
            if(enemies[i+1] > 0){
                enemies_infront++;
            }
            else if(enemies[i+1] < 0) {
                enemies_behind++;
            }
        }
    }
    /**
     * Checks whether an input WorldState is equal to the current WorldState object,
     * @param input WorldState to check comparison
     * @return boolean whether all attributes are equal
     */
    @Override
    public boolean equals(Object input) {
        for(Field field : WorldState.class.getFields()) {
            try {
                if (!field.get(this).equals(field.get(input)))
                    return false;
            } catch (IllegalAccessException e) {}
        }
        return true;
    }

    /**
     * Gets the hashCode for all fields in the WorldState object
     * @return hash
     */
    @Override
    public int hashCode(){
        ArrayList<Object> fields = new ArrayList<Object>();
        for(Field field : WorldState.class.getFields()) {
            try {
                fields.add(field.get(this));
            } catch (IllegalAccessException e) {}
        }
        return Objects.hash(fields);
    }


    /**
     * Returns a String representation of the WorldState
     * @return String
     */
    @Override
    public String toString() {
        String response = "{";
        for(Field field : WorldState.class.getFields()) {
            try {
                response += field.getName() + ": " + field.get(this) + ", ";
            } catch (IllegalAccessException e) {}
        }
        return response.substring(0, response.length()-2) + "}";
    }
}


