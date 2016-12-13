package ch.idsia.scenarios;

import ch.idsia.ai.agents.Agent;
import ch.idsia.ai.agents.AgentsPool;
import ch.idsia.ai.agents.SimpleAgent;
import ch.idsia.ai.agents.ai.SRNAgent;
import ch.idsia.ai.agents.ai.ScaredAgent;
import ch.idsia.ai.agents.human.HumanKeyboardAgent;
import competition.icegic.robin.*;
import competition.icegic.sergiolopez.*;
import competition.cig.alexandrupaler.*;
import ch.idsia.ai.tasks.ProgressTask;
import ch.idsia.ai.tasks.Task;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.tools.EvaluationOptions;
import ucsc_mario_astar.*;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: May 5, 2009
 * Time: 12:46:43 PM
 */

/**
 * The <code>Play</code> class shows how simple is to run an iMario benchmark.
 * It shows how to set up some parameters, create a task,
 * use the CmdLineParameters class to set up options from command line if any.
 * Defaults are used otherwise.
 *
 * @author  Julian Togelius, Sergey Karakovskiy
 * @version 1.0, May 5, 2009
 * @since   JDK1.0
 */

public class Play {
    /**
     * <p>An entry point of the class.
     *
     * @param args input parameters for customization of the benchmark.
     *
     * @see ch.idsia.scenarios.MainRun
     * @see ch.idsia.tools.CmdLineOptions
     * @see ch.idsia.tools.EvaluationOptions
     *
     * @since   iMario1.0
     */

    public static void main(String[] args) {
        EvaluationOptions options = new CmdLineOptions(args);
        Agent select_agent = new UCSC_AStarAgent();
        options.setAgent(select_agent);
        
        
        Task task = new ProgressTask(options);
//        options.setMaxFPS(false);
//        options.setVisualization(true);
//        options.setNumberOfTrials(5);
        options.setLevelRandSeed((int) (Math.random () * Integer.MAX_VALUE));
        options.setTimeLimit(200);
        options.setLevelLength(1000);
        options.setLevelDifficulty(50);
        task.setOptions(options);

        System.out.println ("Score: " + task.evaluate (options.getAgent())[0]);
        System.out.println("Simulation/Play finished");       
    }
}
