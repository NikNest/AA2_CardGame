package game.gameState.utils;

public class Catastrophe extends Card{
    private static boolean inGame = false;
    public Catastrophe(CardValue value) throws IncorrectInputException{
        if(inGame)
            throw new IncorrectInputException("there are too many catastrophes in the game");
    }
}
