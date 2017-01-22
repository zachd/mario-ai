package tcd.agents;

import ch.idsia.benchmark.mario.environments.Environment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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
    public int mode;
    public int coin_right;
    public int coin_left;

    public boolean enemy_location_near_above;
    public boolean enemy_location_near_level;
    public boolean enemy_location_near_below;
    public boolean enemy_location_med_above;
    public boolean enemy_location_med_level;
    public boolean enemy_location_med_below;

    public boolean obstacle_location_near_above;
    public boolean obstacle_location_near_level;
    public boolean obstacle_location_near_below;
    public boolean obstacle_location_med_above;
    public boolean obstacle_location_med_level;
    public boolean obstacle_location_med_below;

    // Private fields can be used for calculation or storage
    private byte[][] levelScene;
    private byte[][] enemy_level_scene;
    private int[] marioEgoPos;
    private int mario_in_levelScene = 9; //the index of the levelScene array that mario is at (19*19 grid so he is at 10,10)
    private int search_space_start = mario_in_levelScene - Params.ENEMY_MED;
    private int search_space_end = mario_in_levelScene + Params.ENEMY_MED;

    public WorldState(Environment environment, Reward reward) {
        on_ground = environment.isMarioOnGround();
        able_to_jump = on_ground && environment.isMarioAbleToJump();
        moving_forward = reward.getDirection();
        stuck = reward.isStuck();
        mode = environment.getMarioMode();
        updateCoinsNearby(environment);
        updateObstaclePosition(environment);
        updateEnemyObservation(environment);
        //old_updateEnemyObservation(environment);
        if(QLearningAgent.show_debug) {
            System.out.println("en near:" + enemy_location_near_above + ", "+enemy_location_near_level + ", "+enemy_location_near_below);
            System.out.println("en med:" + enemy_location_med_above + ", "+enemy_location_med_level + ", "+enemy_location_med_below);
            //System.out.println("ob near:" + Arrays.toString(obstacle_location_near));
            //System.out.println("en med:" + Arrays.toString(obstacle_location_med));
        }
    }

    public void updateEnemyObservation(Environment environment) {
        enemy_location_near_above = false;
        enemy_location_near_level = false;
        enemy_location_near_below = false;

        enemy_location_med_above = false;
        enemy_location_med_level = false;
        enemy_location_med_below = false;

        enemy_level_scene = environment.getEnemiesObservationZ(2); //level 2 just gives a 1 if a creature is there, 0 if not
        for (int i = search_space_start; i <= search_space_end; i++) {
            for (int j = mario_in_levelScene + 1; j <= search_space_end; j++) {
                if (enemy_level_scene[i][j] != 0) { //if there is an enemy at i,j
                    if (j > mario_in_levelScene) { //if the enemy is in front of mario
                        if (i == mario_in_levelScene - Params.ABOVE_MARIO_SIZE) { //if the enemy is above mario
                            if ((j - mario_in_levelScene) <= Params.ENEMY_NEAR) {//if the enemy is near
                                enemy_location_near_above = true;
                            } else { //enemy must be medium distance away if it is not near
                                enemy_location_med_above = true;
                            }
                        } else if (i == mario_in_levelScene) { //if the enemy is level with mario
                            if ((j - mario_in_levelScene) <= Params.ENEMY_NEAR) {//if the enemy is near
                                enemy_location_near_level = true;
                            } else {
                                enemy_location_med_level = true;
                            }
                        } else if (i == mario_in_levelScene + Params.BELOW_MARIO_SIZE) { //if the enemy is below
                            if ((j - mario_in_levelScene) <= Params.ENEMY_NEAR) {//if the enemy is near
                                enemy_location_near_below = true;
                            } else {
                                enemy_location_med_below = true;
                            }
                        }
                    }
                }
            }
        }
    }

    public void updateObstaclePosition(Environment environment) {
        obstacle_location_near_above = false;
        obstacle_location_near_level = false;
        obstacle_location_near_below = false;

        obstacle_location_med_above = false;
        obstacle_location_med_level = false;
        obstacle_location_med_below = false;

        levelScene = environment.getLevelSceneObservationZ(2);
        for (int i = search_space_start; i<= search_space_end; i++) {
            for (int j = mario_in_levelScene + 1; j<= search_space_end; j++) {
                if((levelScene[i][j] != 0 && levelScene[i][j] != 2 )) { //if the block is not a coin or nothing
                    if(j > mario_in_levelScene){//the obstacle is infront of mario
                        if(i == mario_in_levelScene - Params.ABOVE_MARIO_SIZE){ //obstale above mario
                            if(j - mario_in_levelScene <= Params.ENEMY_NEAR){
                                obstacle_location_near_above = true;
                            }
                            else{
                                obstacle_location_med_above = true;
                            }
                        }
                        else if(i == mario_in_levelScene){
                            if(j - mario_in_levelScene <= Params.ENEMY_NEAR){
                                obstacle_location_near_level = true;
                            }
                            else{
                                obstacle_location_med_level = true;
                            }
                        }
                        else if(i == mario_in_levelScene + Params.BELOW_MARIO_SIZE){
                            if(j - mario_in_levelScene <= Params.ENEMY_NEAR){
                                obstacle_location_near_below = true;
                            }
                            else{
                                obstacle_location_med_below = true;
                            }
                        }
                    }
                }

            }
        }
    }

    public void updateCoinsNearby(Environment environment) {
        coin_right = 0;
        coin_left = 0;
        levelScene = environment.getLevelSceneObservationZ(2);
        marioEgoPos =  environment.getMarioEgoPos();
        if ((getCellInformation(marioEgoPos[1] + 1, marioEgoPos[0]) == 2) ||
                ((getCellInformation(marioEgoPos[1] + 1, marioEgoPos[0] - 1) == 2)) ||
                ((getCellInformation(marioEgoPos[1] + 1, marioEgoPos[0] + 1) == 2))) {
            coin_right++;
        } else if ((getCellInformation(marioEgoPos[1] - 1, marioEgoPos[0]) == 2) ||
                ((getCellInformation(marioEgoPos[1] - 1, marioEgoPos[0] - 1) == 2)) ||
                ((getCellInformation(marioEgoPos[1] - 1, marioEgoPos[0] + 1) == 2 ))) {
            coin_left++;
        }
    }

    public int getCellInformation(int x, int y) {
        return levelScene[x][y];
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


