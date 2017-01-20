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
    public boolean enemies_infront_near;
    public boolean enemies_infront_med;
    //public boolean enemies_behind_near;
    //public boolean enemies_behind_med;
    public int mode;
    public boolean obstacle_infront; //is there an impassible object directly infront of mario
    public int coin_right;
    public int coin_left;

    // Private fields can be used for calculation or storage
    private byte[][] levelScene;
    private byte[][] enemy_level_scene;
    private int[] marioEgoPos;
    private int mario_in_levelScene = 9; //the index of the levelScene array that mario is at (19*19 grid so he is at 10,10)
    private int obstacle_search_iStart = mario_in_levelScene - 1;
    private int obstacle_search_jStart = mario_in_levelScene - 0;
    private int obstacle_search_iEnd = mario_in_levelScene + 0;
    private int obstacle_search_jEnd = mario_in_levelScene + 1;
    private int enemy_search_space_start = mario_in_levelScene - Params.ENEMY_MED;
    private int enemy_search_space_end = mario_in_levelScene + Params.ENEMY_MED;


    public WorldState(Environment environment, Reward reward) {
        on_ground = environment.isMarioOnGround();
        able_to_jump = environment.isMarioAbleToJump();
        //moving_forward = reward.getDirection();
        stuck = reward.isStuck();
        mode = environment.getMarioMode();
        //coinReward(environment);
        //updateObstaclePosition(environment);
        updateEnemyObservation(environment);
    }

    public void updateObstaclePosition(Environment environment) {
        obstacle_infront = false;
        levelScene = environment.getLevelSceneObservationZ(2);
        for (int i = mario_in_levelScene-1; i<= mario_in_levelScene; i++) {
            for (int j = mario_in_levelScene+1; j<= mario_in_levelScene+1; j++) {
                if((levelScene[i][j] != 0 && levelScene[i][j] != 2 )) {
                    obstacle_infront = true;
                }

            }
        }
    }

    public void updateEnemyObservation(Environment environment){
        enemies_infront_near = false;
        enemies_infront_med = false;
        //enemies_behind_near = false;
        //enemies_behind_med = false;

        enemy_level_scene = environment.getEnemiesObservationZ(2); //level 2 just gives a 1 if a creature is there, 0 if not
        for(int i=enemy_search_space_start; i<=enemy_search_space_end;i++){
            for(int j=enemy_search_space_start; j<=enemy_search_space_end; j++){
                if(enemy_level_scene[i][j] != 0){ //if there is an enemy at i,j
                    if(j > mario_in_levelScene){ //if the enemy is in front of mario
                        if((j-mario_in_levelScene) <= Params.ENEMY_NEAR){//if the enemy is near
                            enemies_infront_near = true;
                        }
                        else{
                            enemies_infront_med = true; //if the enemy is not near it must be in the medium box
                        }
                    }
                    /*else if(j < mario_in_levelScene){ //if the enemy is behind mario
                        if((mario_in_levelScene-j <= Params.NEAR)){ //if the enemy is in the near box
                            enemies_behind_near = true;
                        }
                        else{
                            enemies_behind_med = true;
                        }
                    }*/
                }
            }
        }
    }

    public void coinReward(Environment environment) {
        coin_right = 0;
        coin_left = 0;
        levelScene = environment.getLevelSceneObservationZ(2);
        marioEgoPos =  environment.getMarioEgoPos();
        if ((getCellInformation(marioEgoPos[1] + 1, marioEgoPos[0]) == 2) ||
                ((getCellInformation(marioEgoPos[1] + 1, marioEgoPos[0] - 1) == 2)) ||
                ((getCellInformation(marioEgoPos[1] + 1, marioEgoPos[0] + 1) == 2))) {
            coin_right++;
        } else if ((getCellInformation(marioEgoPos[1] -1, marioEgoPos[0]) == 2) ||
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


