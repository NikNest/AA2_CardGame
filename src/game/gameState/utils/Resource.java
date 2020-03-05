package game.gameState.utils;

/**
 * class for resource representation, controls the amount of resources in the game
 * @author Nikita
 * @version 1
 */
public class Resource extends Card {
    private static short woodNum = 0;
    private static short metalNum = 0;
    private static short plasticNum = 0;

    /**
     * constructor of a resource card
     * @param value value of card(resource type)
     * @param initializing true for constructor in start command
     * @throws IncorrectInputException if there are too many resources of this type in the game
     */
    public Resource(CardValue value, boolean initializing) throws  IncorrectInputException {
        if (initializing) {
            if (value.equals(CardValue.WOOD) && woodNum < 16) {
                woodNum++;
                type = value;
                return;
            }
            if (value.equals(CardValue.METAL) && metalNum < 16 ) {
                metalNum++;
                type = value;
                return;
            }
            if (value.equals(CardValue.PLASTIC) && plasticNum < 16) {
                plasticNum++;
                type = value;
                return;
            }
            throw new IncorrectInputException("there are too many resources of this type in the game");
        }
    }

    /**
     * for correctly resources' compare
     * @return type
     */
    @Override
    public int hashCode() {
        return type.ordinal();
    }

    /**
     * for correctly resources' compare
     * @param obj other resourse
     * @return true if equals
     */
    @Override
    public boolean equals(Object obj) {
        return Resource.class.isInstance(obj) && this.type.equals(((Resource) obj).type);
    }

}
