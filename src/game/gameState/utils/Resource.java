package game.gameState.utils;

public class Resource extends Card{
    private static short woodNum = 0;
    private static short metalNum = 0;
    private static short plasticNum = 0;

    public Resource(CardValue value, boolean initializing) throws  IncorrectInputException{
        if (initializing) {
            if (value.equals(CardValue.WOOD))
                woodNum++;
            if (value.equals(CardValue.METAL))
                metalNum++;
            if (value.equals(CardValue.PLASTIC))
                plasticNum++;
            if (woodNum > 16 || metalNum > 16 || plasticNum > 16)
                throw new IncorrectInputException("there are too many resources in the game");
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
