package com.mastering.lambdas.designpatterns.builder;

public class Game {
    private String homeTeam;
    private String awayTeam;
    private int currentYardLine;
    private String weather;
    private String stadium;

    static final class GameBuilder {
        private String homeTeam;
        private String awayTeam;
        private int currentYardLine;
        private String weather;
        private String stadium;

        public GameBuilder homeTeam(String homeTeam) {
            this.homeTeam = homeTeam;
            return this;
        }

        public GameBuilder awayTeam(String awayTeam) {
            this.awayTeam = awayTeam;
            return this;
        }

        public GameBuilder currentYardLine(int currentYardLine) {
            this.currentYardLine = currentYardLine;
            return this;
        }

        public GameBuilder weather(String weather) {
            this.weather = weather;
            return this;
        }

        public GameBuilder stadium(String stadium) {
            this.stadium = stadium;
            return this;
        }

        public Game build() {
            if (homeTeam == null)
                throw new IllegalStateException("No home team");
            if (awayTeam == null)
                throw new IllegalStateException("No away team");
            if (currentYardLine == 0)
                throw new IllegalStateException("Currrent yardline can't be 0");
            if (stadium == null)
                throw new IllegalStateException("No stadium");
            if (weather == null)
                throw new IllegalStateException("No weather");
            return new Game(this);
        }
    }

    private Game(GameBuilder builder) {
        awayTeam = builder.awayTeam;
        homeTeam = builder.homeTeam;
        currentYardLine = builder.currentYardLine;
        stadium = builder.stadium;
        weather = builder.weather;
    }

    @Override
    public String toString() {
        return "Game [homeTeam=" + homeTeam + ", awayTeam=" + awayTeam + ", currentYardLine=" + currentYardLine
                + ", weather=" + weather + ", stadium=" + stadium + "]";
    }

    public static void main(String[] args) throws Exception {
        Game hockeyGame = new GameBuilder()
                .awayTeam("Arizona")
                .homeTeam("Denver")
                .currentYardLine(35)
                .stadium("Giant Stadium")
                .weather("Snow")
                .build();
        Game footballGame = new GameBuilder()
                .homeTeam("Texas")
                .awayTeam("Boston")
                .currentYardLine(30)
                .weather("Snow")
                .stadium("Milde High")
                .build();
        System.out.println(hockeyGame);
        System.out.println(footballGame);
    }
}

