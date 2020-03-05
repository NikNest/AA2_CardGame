package game.gameState.utils;

/**
 * abstract class for card representation
 * @author Nikita
 * @version 1
 */
public abstract class Card {
    /**
     * card type
     */
    CardValue type;

    /**
     * getter of card type
     * @return name of the card type
     */
    public String getType() {
        return type.name().toLowerCase();
    }
}
