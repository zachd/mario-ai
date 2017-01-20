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
    public boolean enemies_infront_far;
    public boolean enemies_behind_near;
    public boolean enemies_behind_med;
    public boolean enemies_behind_far;
    public int mode;
    public boolean obstacle_infront; //is there an impassible object directly infront of mario
    public int coin_right;
    public int coin_left;

    // Private fields can be used for calculation or storage
    private byte[][] levelScene;
    private int[] marioEgoPos;
    private int mario_in_levelScene = 9; //the index of the levelScene array that mario is at (19*19 grid so he is at 10,10)
    private int obstacle_search_iStart = mario_in_levelScene - 1;
    private int obstacle_search_jStart = mario_in_levelScene - 0;
    private int obstacle_search_iEnd = mario_in_levelScene + 0;
    private int obstacle_search_jEnd = mario_in_levelScene + 1;


    public WorldState(Environment environment, Reward reward) {
        on_ground = environment.isMarioOnGround();
        able_to_jump = environment.isMarioAbleToJump();
        moving_forward = reward.getDirection();
        stuck = reward.isStuck();
        mode = environment.getMarioMode();
        updateEnemyPosition(environment);
        coinReward(environment);
        //updateObstaclePosition(environment);
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
                if(enemy_xpos <= Params.NEAR && (enemy_ypos <= Params.NEAR || enemy_ypos >= -Params.NEAR)){
                    enemies_infront_near = true;
                }
                else if(enemy_xpos <= Params.MED && (enemy_ypos <= Params.MED || enemy_ypos >=-Params.MED)){
                    enemies_infront_med = true;
                }
                else if(enemy_ypos <= Params.FAR || enemy_ypos >= -Params.FAR){
                    enemies_infront_far = true;
                }

            }
            else if(enemies[i+1] < 0) {
                if(enemy_xpos <= -Params.NEAR && (enemy_ypos <= Params.NEAR || enemy_ypos >= -Params.NEAR)){
                    enemies_behind_near = true;
                }
                else if(enemy_xpos <= -Params.MED && (enemy_ypos <= Params.MED || enemy_ypos >= -Params.MED)){
                    enemies_behind_med = true;
                }
                else if(enemy_ypos <= Params.FAR || enemy_ypos >= -Params.FAR){
                    enemies_behind_far = true;
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


