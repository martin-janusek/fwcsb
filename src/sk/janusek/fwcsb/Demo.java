package sk.janusek.fwcsb;

import java.util.List;

import sk.janusek.fwcsb.Board.Match;

public class Demo {
    
    public static void main(String[] args) {

        Board board = Board.getInstance();

        board.startMatch("Mexico", "Canada").updateScore(0, 5);
        board.startMatch("Spain", "Brazil").updateScore(10, 2);
        board.startMatch("Germany", "France").updateScore(2, 2);
        board.startMatch("Uruguay", "Italy").updateScore(6, 6);
        board.startMatch("Argentina", "Australia").updateScore(3, 1);


        List<Match> results = board.getCurrentMatches();
        for (Match match : results) {
            System.out.println(match.toString());
        }
    }
}
