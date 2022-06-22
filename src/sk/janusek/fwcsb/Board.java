package sk.janusek.fwcsb;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Board {

    private static Board _instance;

    private List<Match> matchList = new ArrayList<Match>();

    private Board() {

    }

    public static Board getInstance() {
        if (_instance == null) {
            _instance = new Board();
        }

        return _instance;
    }

    public Match startMatch(String homeTeam, String awayTeam) {
        Match result = new Match(homeTeam, awayTeam);
        this.matchList.add(result);
        return result;
    }

    public synchronized void finishMatch(Match match) {
        this.matchList.remove(match);
        match.finish();
    }

    public List<Match> getCurrentMatches() {
        List<Match> aList = new ArrayList<>(this.matchList);
        aList.sort(new Comparator<Match>() {

            @Override
            public int compare(Match o1, Match o2) {

                int result = (o2.homeScore + o2.awayScore) - (o1.homeScore + o1.awayScore);
                if (result == 0) {
                    return o2.createdAt.compareTo(o1.createdAt);
                } else {
                    return result;
                }
            }

        });
       
        return aList;
    }

    public class Match {

        private final String homeTeam;
        private final String awayTeam;

        private int homeScore = 0;
        private int awayScore = 0;

        private MatchStatus status;

        private final Long createdAt = System.nanoTime();

        private Match(String aHomeTeam, String aAwayTeam) {
            this.homeTeam = aHomeTeam;
            this.awayTeam = aAwayTeam;
            this.status = MatchStatus.STARTED;
        }

        /*
         * Transfer this match to status FINISHED
         */
        private void finish() {
            this.status = MatchStatus.FINISHED;
        }

        /*
         * Updates current match score to new pair of if and only if the Match is in
         * state STARTED.
         * Synchronized to ensure the score change is atomic. Score for each team can be
         * only increased.
         * 
         * @param newHomeScore new home score
         * 
         * @param newAwayScore new away score
         * 
         * @throws IllegalArgumentException if new score is smaller than current score
         * 
         * @throws IllegalStateException if current match status is different from
         * status STARTED
         */
        public synchronized void updateScore(int newHomeScore, int newAwayScore) {
            if (this.status == MatchStatus.STARTED) {
                if (newHomeScore >= homeScore) {
                    this.homeScore = newHomeScore;
                } else {
                    throw new IllegalArgumentException("Cannot change home score " + homeScore + " to " + newHomeScore);
                }

                if (newAwayScore >= awayScore) {
                    this.awayScore = newAwayScore;
                } else {
                    throw new IllegalArgumentException("Cannot change away score " + awayScore + " to " + newAwayScore);
                }

            } else {
                throw new IllegalStateException("Cannot update score in state " + this.status);
            }

        }

        public String getHomeTeam() {
            return homeTeam;
        }

        public String getAwayTeam() {
            return awayTeam;
        }

        public int getHomeScore() {
            return homeScore;
        }

        public int getAwayScore() {
            return awayScore;
        }

        public MatchStatus getStatus() {
            return status;
        }

        @Override
        public String toString() {
            return String.format("%s %d - %s %d", homeTeam, homeScore, awayTeam, awayScore);
        }

    }

    public static enum MatchStatus {
        STARTED, FINISHED;
    }
}
