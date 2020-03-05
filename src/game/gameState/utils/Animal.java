package game.gameState.utils;

public class Animal extends Card{
    private static short spiderNum = 0;
    private static short snakeNum = 0;
    private static short tigerNum = 0;
    public Animal(CardValue value) throws IncorrectInputException {
        if(value.equals(CardValue.SPIDER) && spiderNum < 5) {
            spiderNum++;
            type = value;
            return;
        }
        if(value.equals(CardValue.SNAKE) && snakeNum < 5) {
            snakeNum++;
            type = value;
            return;
        }
        if(value.equals(CardValue.TIGER) && tigerNum < 5) {
            tigerNum++;
            type = value;
            return;
        }
        throw new IncorrectInputException("there are already too many animals of this type in the game");
    }
    public int getNumDice() {
        if(type == CardValue.SPIDER) {
            return 4;
        } else if(type == CardValue.SNAKE) {
            return 6;
        } else {
            return 8;
        }
    }
}
