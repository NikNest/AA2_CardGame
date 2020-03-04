package game.gameState.utils;

public class Animal extends Card{
    private static short spiderNum = 0;
    private static short snakeNum = 0;
    private static short tigerNum = 0;
    public Animal(CardValue value) throws  IncorrectInputException{
        if(value.equals(CardValue.SPIDER))
            spiderNum++;
        if(value.equals(CardValue.SNAKE))
            snakeNum++;
        if(value.equals(CardValue.TIGER))
            tigerNum++;
        if(spiderNum > 5 || snakeNum > 5 || tigerNum > 5)
            throw new IncorrectInputException("there are too much animal in the game");
    }
}
