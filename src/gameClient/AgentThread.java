package gameClient;
//finish
import api.*;

import java.util.HashSet;
//finishhhh
/** the class is Runnable object for CL_Agent class.
 *  calls it's specific agent to move according to it's
 *  position and sleep until the agent is calculated to
 *  reach is goal. runs until the game ends
 */
public class AgentThread implements Runnable {

    private CL_Agent _agent;
    private static game_service _game;
    private static Player _mover;

    public AgentThread(CL_Agent agent, game_service game, Player mover){
        _agent = agent;
        _game = game;
        _mover = mover;
    }

    @Override
    public void run() {
        long dt = 0;
        while(_game.isRunning()){
            dt = _mover.init(_agent);
            try {
                Thread.sleep(dt);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}