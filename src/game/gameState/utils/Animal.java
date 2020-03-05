package game.gameState.utils;

/**
 * class for animal representation
 * @author Nikita
 * @version 1
 */
public class Animal extends Card {
    private static short spiderNum = 0;
    private static short snakeNum = 0;
    private static short tigerNum = 0;

    /**
     * constructor of animal
     * @param value animal type
     * @throws IncorrectInputException when there are to many animals of this type in the game
     */
    public Animal(CardValue value) throws IncorrectInputException {
        if (value.equals(CardValue.SPIDER) && spiderNum < 5) {
            spiderNum++;
            type = value;
            return;
        }
        if (value.equals(CardValue.SNAKE) && snakeNum < 5) {
            snakeNum++;
            type = value;
            return;
        }
        if (value.equals(CardValue.TIGER) && tigerNum < 5) {
            tigerNum++;
            type = value;
            return;
        }
        throw new IncorrectInputException("there are already too many animals of this type in the game");
    }

    /**
     * getter for dice type(4, 6, 8)
     * @return 4 6 or 8
     */
    public int getNumDice() {
        if (type == CardValue.SPIDER) {
            return 4;
        } else if (type == CardValue.SNAKE) {
            return 6;
        } else {
            return 8;
        }
    }
}
