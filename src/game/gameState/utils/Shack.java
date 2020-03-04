package game.gameState.utils;

import java.util.ArrayList;

public class Shack {
    ArrayList<Resource> savedResourses = new ArrayList<>();
    boolean inGame = false;
    public boolean save(Resource res) {
        if(savedResourses.size() < 5) {
            savedResourses.add(res);
            return true;
        }
        return false;
    }

    public ArrayList<Resource> getSavedResourses() {
        return savedResourses;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }
}
