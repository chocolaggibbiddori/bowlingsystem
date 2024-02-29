package chocola.bowling.score;

public class ScoreTable {

    private final Score[][] scores;

    public ScoreTable(int playerNum) {
        scores = new Score[playerNum + 1][11];
        for (int i = 1; i <= playerNum; i++) {
            for (int j = 0; j < 10; j++) {
                scores[i][j] = new Score();
            }
            scores[i][10] = new Score10();
        }
    }

    public void setPin1(int player, int round, int pin1) {
        Score score = scores[player][round];
        score.setPin1(pin1);

        Score preScore = scores[player][round - 1];
        if (preScore.isStart()) preScore.setBonus1(pin1);
        setPreTotal(preScore, score);

        if (preScore.isStrike()) {
            Score prePreScore = scores[player][round - 2];
            if (prePreScore.isStrike()) {
                prePreScore.setBonus2(pin1);
                setPreTotal(prePreScore, preScore);
            }
        }
    }

    public void setPin2(int player, int round, int pin2) {
        Score score = scores[player][round];
        score.setPin2(pin2);

        Score preScore = scores[player][round - 1];
        if (preScore.isStart()) preScore.setBonus2(pin2);
        setPreTotal(preScore, score);
    }

    public void setBonus1(int player, int round, int bonus) {
        Score score = scores[player][round];
        score.setBonus1(bonus);

        if (round == 10) {
            Score preScore = scores[player][round - 1];
            if (preScore.isStart()) preScore.setBonus2(bonus);
            setPreTotal(preScore, score);
        }
    }

    public void setBonus2(int player, int round, int bonus) {
        Score score = scores[player][round];
        score.setBonus2(bonus);
    }

    private void setPreTotal(Score preScore, Score score) {
        if (preScore.isEnd()) score.setPreTotal(preScore.getTotal());
    }

    public void display() {
        String header = String.format("   " + "| %d ".repeat(9) + "| 10" + "| TOTAL |", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        String line = "-".repeat(52);
        System.out.println(header);
        System.out.println(line);

        for (int player = 1; player < scores.length; player++) {
            StringBuilder pinSb = new StringBuilder();
            StringBuilder scoreSb = new StringBuilder();

            pinSb.append("   |");
            scoreSb.append(" ").append((char) (64 + player)).append(" |");

            for (int round = 1; round <= 10; round++) {
                Score score = scores[player][round];

                pinSb.append(score.toPinString()).append("|");
                scoreSb.append(score.toScoreString()).append("|");
            }

            pinSb.append("       |");
            scoreSb.append(getTotal(player)).append(" |");

            System.out.println(pinSb);
            System.out.println(scoreSb);
            System.out.println(line);
        }
    }

    private String getTotal(int player) {
        int total = 0;
        for (int i = 10; i > 0; i--) {
            Score score = scores[player][i];
            if (score.isEnd()) {
                total = score.getTotal();
                break;
            }
        }

        return formatNumber(total);
    }

    private String formatNumber(int score) {
        if (score < 10) return "     " + score;
        else if (score < 100) return "    " + score;
        else if (score <= 300) return "   " + score;
        else throw new IllegalArgumentException("Score can't exceed 300 points");
    }
}
