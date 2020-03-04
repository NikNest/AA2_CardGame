package game;

import edu.kit.informatik.Terminal;
import game.gameState.Game;
import game.gameState.utils.IncorrectInputException;

import java.util.ArrayList;

public class Launcher {
    public static final String CARD_PATTERN = "^wood|metal|plastic|spider|snake|tiger|thunderstorm$";
    public static void main(String[] args) {
        ArrayList<String> str = new ArrayList<String>();
        str.add("c");
        str.add("a");
        str.add("b");
        str.add("a");
        str.add("c");
        str.add("a");
        str.add("b");
        str.remove("c");
        System.out.println(str);
//        boolean gameStarted = false;
//        while(!gameStarted) {
//            String startCommand = Terminal.readLine().trim();
//            try {
//                if (startCommand.equals(""))
//                    throw new IncorrectInputException();
//                else {
//                    String[] temp = startCommand.split(" ");
//                    if(temp.length != 2)
//                        throw new IncorrectInputException();
//                    else {
//                        if (temp[0].equals("start")) {
//                            String[] cards = temp[1].split(",");
//                            int num = 0;
//                            for (String card : cards) {
//                                if(!card.matches(CARD_PATTERN))
//                                    throw new IncorrectInputException("name: " + card + " isn't a valid cardname");
//                                num++;
//                            }
//                            if (num != 64)
//                                throw new IncorrectInputException("there are should be 64 card in the game. not: " + num);
//                            gameStarted = true;
//                            //throws exception in case of stack mistake
//                            Game game = new Game(cards);
//                            while(true) {
//                                if( !game.makeTurn() )
//                                break;
//                            }
//
//                        } else {
//                            throw new IncorrectInputException("game should start with \"start\" command");
//                        }
//                    }
//                }
//            } catch (IncorrectInputException e) {
//                gameStarted = false;
//            }
//        }
    }
}
