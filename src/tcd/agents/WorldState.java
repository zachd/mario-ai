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
    public boolean moving_forward;
    public boolean stuck;
    public boolean enemies_infront_near;
    public boolean enemies_infront_med;
    public boolean enemies_infront_far;
    public boolean enemies_behind_near;
    public boolean enemies_behind_med;
    public boolean enemies_behind_far;
    public int mode;

    // Private fields can be used for calculation
    private static final float NEAR = 10f;
    private static final float MED = 50f;
    private static final float FAR = 100f;

    public WorldState(Environment environment, Reward reward) {
        on_ground = environment.isMarioOnGround();
        able_to_jump = environment.isMarioAbleToJump();
        moving_forward = reward.getDirection();
        stuck = reward.isStuck();
        mode = environment.getMarioMode();
        updateEnemyPosition(environment);
    }

    /**
     * updates enemies_infromt and enemies_behind with the number of enemies on the screen
     * @param environment the current environment
     */
    public void updateEnemyPosition(Environment environment){
        enemies_infront_near = false;
        enemies_infront_med = false;
        enemies_infront_far = false;
        enemies_behind_near = false;
        enemies_behind_med = false;
        enemies_behind_far = false;

        float[] enemies = environment.getEnemiesFloatPos(); //{enemy1_type,enemy1_xpos,enemy1_ypos, enemy2_type,enemy2_xpos..}
        for(int i=0;i<enemies.length; i+=3){
            float enemy_xpos = enemies[i+1];
            float enemy_ypos = enemies[i+2];
            if(enemy_xpos > 0){
                if(enemy_xpos <= NEAR && (enemy_ypos <= NEAR || enemy_ypos >= -NEAR)){
                    enemies_infront_near = true;
                }
                else if(enemy_xpos <= MED && (enemy_ypos <= MED || enemy_ypos >=-MED)){
                    enemies_infront_med = true;
                }
                else if(enemy_ypos <= FAR || enemy_ypos >= -FAR){
                    enemies_infront_far = true;
                }

            }
            else if(enemies[i+1] < 0) {
                if(enemy_xpos <= -NEAR && (enemy_ypos <= NEAR || enemy_ypos >= -NEAR)){
                    enemies_behind_near = true;
                }
                else if(enemy_xpos <= -MED && (enemy_ypos <= MED || enemy_ypos >= -MED)){
                    enemies_behind_med = true;
                }
                else if(enemy_ypos <= FAR || enemy_ypos >= -FAR){
                    enemies_behind_far = true;
                }
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


