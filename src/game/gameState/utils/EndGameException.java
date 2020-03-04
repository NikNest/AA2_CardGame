package game.gameState.utils;

import edu.kit.informatik.Terminal;

/**
 * class of the custom exception for end game situation
 * @author  Nikita
 * @version 1
 */
public class EndGameException extends Exception{
    /**
     * constructor with error msg
     */
    public EndGameException() {
        Terminal.printError("GAME OVER: there no more card in the deck");
    }
}


