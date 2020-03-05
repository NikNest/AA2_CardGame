package game.gameState.utils;

public class Resource extends Card{
    private static short woodNum = 0;
    private static short metalNum = 0;
    private static short plasticNum = 0;

    public Resource(CardValue value, boolean initializing) throws  IncorrectInputException{
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

    @Override
    public int hashCode() {
        return type.ordinal();
    }

    @Override
    public boolean equals(Object obj) {
        return Resource.class.isInstance(obj) && this.type.equals(((Resource)obj).type);
    }

}
