package sk.janusek.fwcsb;

import java.util.List;

import sk.janusek.fwcsb.Board.Match;
import sk.janusek.fwcsb.Board.MatchStatus;

public class Test {

    public static void main(String[] args) {

        Board board = Board.getInstance();

        Match m1 = board.startMatch("Home1", "Away1");

        // testing correct initialization
        assert (m1.getHomeScore() == 0);
        assert (m1.getAwayScore() == 0);
        assert (m1.getHomeTeam().equals("Home1"));
        assert (m1.getAwayTeam().equals("Away1"));
        assert (m1.getStatus() == MatchStatus.STARTED);

        // testing update function
        m1.updateScore(3, 5);
        assert (m1.getHomeScore() == 3);
        assert (m1.getAwayScore() == 5);

        try {
            m1.updateScore(-1, 9);
            assert (false);
        } catch (IllegalArgumentException e) {
            assert (true);
        }

        try {
            m1.updateScore(2, 5);
            assert (false);
        } catch (IllegalArgumentException e) {
            assert (true);
        }

        board.finishMatch(m1);
        assert (m1.getStatus() == MatchStatus.FINISHED);

        try {
            m1.updateScore(6, 6);
            assert (false);
        } catch (IllegalStateException e) {
            assert (true);
        }
       

        // testing compareTo
        Match m2 = board.startMatch("Home2", "Away2");
        Match m3 = board.startMatch("Home3", "Away3");
        Match m4 = board.startMatch("Home4", "Away4");
        Match m5 = board.startMatch("Home5", "Away5");
        m2.updateScore(2, 1);
        m3.updateScore(1, 2);
        m4.updateScore(1, 1);
        m5.updateScore(3, 3);

        List<Match> matches = board.getCurrentMatches();
        assert (matches.get(0).equals(m5));
        assert (matches.get(1).equals(m3));
        assert (matches.get(2).equals(m2));
        assert (matches.get(3).equals(m4));     
        
        // testing board
        assert (matches.size() == 3);  
        
        board.finishMatch(m2);
        matches = board.getCurrentMatches();
        assert (matches.size() == 2);  

        board.finishMatch(m2);
        matches = board.getCurrentMatches();
        assert (matches.size() == 2);  

        board.finishMatch(m3);
        board.finishMatch(m4);
        matches = board.getCurrentMatches();
        assert (matches.size() == 0);  

        System.out.println("Success");

    }
}
