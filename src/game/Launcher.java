package game;

import edu.kit.informatik.Terminal;
import game.gameState.Game;
import game.gameState.utils.IncorrectInputException;

/**
 * main class who controls the game process
 * @author Nikita
 * @version 1.0
 */
public class Launcher {
    /**
     * patter for card matching
     */
    public static final String CARD_PATTERN = "^wood|metal|plastic|spider|snake|tiger|thunderstorm$";

    /**
     * method that launches the new game and runs it
     * @return true if quit
     */
    private static boolean playGame() {
        while (true) {
            String startCommand = Terminal.readLine().trim();
            if (startCommand.equals("quit"))
                return false;
            try {
                if (startCommand.equals(""))
                    throw new IncorrectInputException();
                else {
                    String[] temp = startCommand.split(" ");
                    if (temp.length != 2)
                        throw new IncorrectInputException();
                    else {
                        if (temp[0].equals("start")) {
                            String[] cards = temp[1].split(",");
                            int num = 0;
                            for (String card : cards) {
                                if (!card.matches(CARD_PATTERN))
                                    throw new IncorrectInputException("name: " + card + " isn't a valid cardname");
                                num++;
                            }
                            if (num != 64)
                                throw new IncorrectInputException("there are should be 64 "
                                        + "card in the game. not: " + num);
                            //throws exception in case of stack mistake
                            Game game = new Game(cards);
                            //game loop
                            while (true) {
                                String userInput = Terminal.readLine().trim();
                                if (userInput.equals("quit"))
                                    return false;
                                if (userInput.equals("reset")) {
                                    Terminal.printLine("OK");
                                    return true;
                                }
                                if (game.makeTurn(userInput))
                                    return true;
                            }

                        } else {
                            throw new IncorrectInputException("game should start with \"start\" command");
                        }
                    }
                }
            } catch (IncorrectInputException e) { }
        }
    }

    /**
     * main method
     * @param args command line arguments
     */
    public static void main(String[] args) {
        while (true) {
            if (!playGame())
                break;
        }
    }
}
