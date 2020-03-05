package game.gameState.utils;

/**
 * class for representing catastrophe
 * @author Nikita
 * @version 1
 */
public class Catastrophe extends Card {
    private static boolean inGame = false;

    /**
     * catastrophe constructor, controls amount if cotostrophes in the game
     * @throws IncorrectInputException if there is already a catastrophe in the game
     */
    public Catastrophe() throws IncorrectInputException {
        if (inGame)
            throw new IncorrectInputException("there are too many catastrophes in the game");
        else
            inGame = true;
    }
}
