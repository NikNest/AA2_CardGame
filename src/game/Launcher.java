package game;

import edu.kit.informatik.Terminal;
import game.gameState.Game;
import game.gameState.utils.IncorrectInputException;


import java.util.*;

public class Launcher {
    public static final String CARD_PATTERN = "^wood|metal|plastic|spider|snake|tiger|thunderstorm$";
    //true if continue
    private static boolean playGame(){
        while(true) {
            String startCommand = Terminal.readLine().trim();
            if(startCommand.equals("quit"))
                return false;
            try {
                if (startCommand.equals(""))
                    throw new IncorrectInputException();
                else {
                    String[] temp = startCommand.split(" ");
                    if(temp.length != 2)
                        throw new IncorrectInputException();
                    else {
                        if (temp[0].equals("start")) {
                            String[] cards = temp[1].split(",");
                            int num = 0;
                            for (String card : cards) {
                                if(!card.matches(CARD_PATTERN))
                                    throw new IncorrectInputException("name: " + card + " isn't a valid cardname");
                                num++;
                            }
                            if (num != 64)
                                throw new IncorrectInputException("there are should be 64 card in the game. not: " + num);
                            //throws exception in case of stack mistake
                            Game game = new Game(cards);
                            while(true) {
                                try {
                                    String userInput = Terminal.readLine().trim();
                                    if (userInput.equals("quit"))
                                        return false;
                                    if (userInput.equals("reset")) {
                                        Terminal.printLine("OK");
                                        return true;
                                    }
                                    if (game.makeTurn(userInput))
                                        return true;
                                } catch (EndGameException e) {
                                    return true;
                                }
                            }

                        } else {
                            throw new IncorrectInputException("game should start with \"start\" command");
                        }
                    }
                }
            } catch (IncorrectInputException e) {}
        }
    }
    public static void main(String[] args) {
        ArrayList<String> str = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        str.add("1");
        str.add("2");
        str.add("3");
        str.add("4");
        str.add("5");
        str.add("6");
        str.add("7");
        int num = 3;
        ListIterator<String> iter = str.listIterator(str.size());
        while(num > 0) {
            temp.add(iter.previous());
            num--;
        }
        Collections.reverse(temp);
        System.out.println(temp);
//            while(true) {
//                if(!playGame())
//                    break;
//            }
    }
}
