package game.gameState.utils;

public class Card {
    CardValue type;
    public String getType() {
        return type.name().toLowerCase();
    }
}
