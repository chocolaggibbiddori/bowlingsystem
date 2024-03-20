package chocola.bowling2;

import java.util.ArrayList;
import java.util.List;

public class BowlingGame {

    private final List<List<Integer>> pinList;
    private final ScoreBoard scoreBoard;
    private final int playerNum;
    private int player = 1;
    private int round = 1;
    private int remainPins = 10;
    private Order order = Order.FIRST;

    public BowlingGame(int playerNum) {
        this.playerNum = playerNum;
        scoreBoard = new ScoreBoard();
        pinList = initPinList();
    }

    private List<List<Integer>> initPinList() {
        List<List<Integer>> pinList = new ArrayList<>(playerNum + 1);
        for (int i = 0; i <= playerNum; i++)
            pinList.add(new ArrayList<>(21));
        return pinList;
    }

    public void addPin(int pin) {
        List<Integer> pinList = this.pinList.get(player);
        pinList.add(pin);
        remainPins -= pin;
        scoreBoard.addPinInBoard(pinList, pin);
        scoreBoard.addScoreInBoard(pinList);
        checkTurn(pinList);

        if (remainPins == 0) pinInit();
    }

    private void checkTurn(List<Integer> pinList) {
        if (round < 10) {
            if (order == Order.SECOND || remainPins == 0) nextTurn();
            else order = Order.SECOND;
        } else {
            switch (order) {
                case FIRST -> order = Order.SECOND;
                case SECOND -> {
                    int prePin = pinList.get(pinList.size() - 2);
                    if (prePin == 10 || remainPins == 0) order = Order.THIRD;
                    else nextTurn();
                }
                case THIRD -> nextTurn();
            }
        }
    }

    private void nextTurn() {
        nextPlayer();
        pinInit();
        orderInit();
    }

    private void nextPlayer() {
        ++player;
        if (player > playerNum) {
            player = 1;
            ++round;
        }
    }

    private void pinInit() {
        remainPins = 10;
    }

    private void orderInit() {
        order = Order.FIRST;
    }

    public boolean isNotValidPin(int pin) {
        boolean notValid = pin < 0 || pin > 10 || remainPins - pin < 0;
        if (notValid) System.out.printf("남아 있는 핀의 개수는 %d개 입니다.\n", remainPins);
        return notValid;
    }

    public void display() {
        scoreBoard.markPlayer();
        System.out.println(scoreBoard);
    }

    public boolean isPlaying() {
        return round <= 10;
    }

    private enum Order {

        FIRST, SECOND, THIRD
    }

    private class ScoreBoard {

        static final int LINE_IDX_NUM = 53;

        final StringBuilder scoreBuilder;
        final int[] pinIdxArr;

        ScoreBoard() {
            scoreBuilder = initScoreBuilder();
            pinIdxArr = initPinIdxArr();
        }

        private StringBuilder initScoreBuilder() {
            int capacity = LINE_IDX_NUM * 3 * playerNum;
            StringBuilder scoreBuilder = new StringBuilder(capacity);
            String header = "   | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10| TOTAL |\n";
            String line = "----------------------------------------------------\n";

            scoreBuilder
                    .append(header)
                    .append(line);
            for (int player = 1; player <= playerNum; player++) {
                char playerChar = playerToChar(player);
                scoreBuilder
                        .append("   |   |   |   |   |   |   |   |   |   |   |       |\n ")
                        .append(playerChar)
                        .append(" |   |   |   |   |   |   |   |   |   |   |       |\n")
                        .append(line);
            }

            return scoreBuilder;
        }

        private char playerToChar(int player) {
            return (char) (player + 64);
        }

        private int[] initPinIdxArr() {
            return new int[playerNum + 1];
        }

        @Override
        public String toString() {
            return scoreBuilder.toString();
        }

        void addPinInBoard(List<Integer> pinList, int pin) {
            int idx = getRoundIdx(player, round);
            String pinStr = pinToString(pin);
            int size = pinList.size();

            switch (order) {
                case FIRST -> {
                    if (round < 10) {
                        if (pin == 10) scoreBuilder.replace(idx, idx + 3, " X ");
                        else scoreBuilder.replace(idx, idx + 1, pinStr);
                    } else {
                        if (pin == 10) scoreBuilder.replace(idx, idx + 1, "X");
                        else scoreBuilder.replace(idx, idx + 1, pinStr);
                    }
                }
                case SECOND -> {
                    int prePin = pinList.get(size - 2);

                    if (round < 10) {
                        if (remainPins == 0) scoreBuilder.replace(idx + 2, idx + 3, "/");
                        else scoreBuilder.replace(idx + 2, idx + 3, pinStr);
                    } else {
                        if (prePin == 10) {
                            if (pin == 10) scoreBuilder.replace(idx + 1, idx + 2, "X");
                            else scoreBuilder.replace(idx + 1, idx + 2, pinStr);
                        } else {
                            if (remainPins == 0) scoreBuilder.replace(idx + 1, idx + 2, "/");
                            else scoreBuilder.replace(idx + 2, idx + 3, pinStr);
                        }
                    }
                }
                case THIRD -> {
                    int prePin = pinList.get(size - 2);
                    int prePrePin = pinList.get(size - 3);

                    if (remainPins == 0) {
                        if (prePrePin == 10) {
                            if (prePin == 10) scoreBuilder.replace(idx + 2, idx + 3, "X");
                            else scoreBuilder.replace(idx + 2, idx + 3, "/");
                        } else scoreBuilder.replace(idx + 2, idx + 3, "X");
                    } else scoreBuilder.replace(idx + 2, idx + 3, pinStr);
                }
            }
        }

        void addScoreInBoard(List<Integer> pinList) {
            int pinIdx = pinIdxArr[player];
            addScoreInBoard(pinList, pinIdx);
        }

        private void addScoreInBoard(List<Integer> pinList, int pinIdx) {
            int size = pinList.size();
            if (pinIdx >= size) return;

            int scoreIdx = getScoreIdx();
            if (isOverFinalScoreIdx(scoreIdx)) return;

            int pin = pinList.get(pinIdx);
            int preScoreIdxStart = scoreIdx - 4;
            int preScoreIdxEnd = scoreIdx - 1;
            int sum = getScore(preScoreIdxStart, preScoreIdxEnd);
            int nextPinIdx = pinIdx + 1;
            int nextNextPinIdx = pinIdx + 2;
            boolean doNotHaveNextPin = nextPinIdx >= size;
            boolean doNotHaveNextNextPin = nextNextPinIdx >= size;

            if (pin == 10) {
                if (doNotHaveNextPin || doNotHaveNextNextPin) return;
                int nextPin = pinList.get(nextPinIdx);
                int nextNextPin = pinList.get(nextNextPinIdx);

                sum += (pin + nextPin + nextNextPin);
                pinIdxArr[player] = nextPinIdx;
            } else {
                if (doNotHaveNextPin) return;
                int nextPin = pinList.get(nextPinIdx);

                if (pin + nextPin == 10) {
                    if (doNotHaveNextNextPin) return;
                    int nextNextPin = pinList.get(nextNextPinIdx);

                    sum += (10 + nextNextPin);
                } else {
                    sum += (pin + nextPin);
                }
                pinIdxArr[player] = nextNextPinIdx;
            }

            String sumStr = String.format("%3d", sum);
            int totalIdx = getRoundIdx(player, 10) + 6 + LINE_IDX_NUM;

            scoreBuilder.replace(scoreIdx, scoreIdx + 3, sumStr);
            scoreBuilder.replace(totalIdx, totalIdx + 3, sumStr);
            addScoreInBoard(pinList);
        }

        private String pinToString(int pin) {
            if (pin == 0) return "-";
            else return pin + "";
        }

        private int getScore(int start, int end) {
            try {
                String s = scoreBuilder.substring(start, end).trim();
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        void markPlayer() {
            int nowPlayer = player;
            int prePlayer = getPrePlayer();
            int nowMarkIdx = getPlayerIdx(nowPlayer);
            int preMarkIdx = getPlayerIdx(prePlayer);

            if (isPlaying()) scoreBuilder.replace(nowMarkIdx, nowMarkIdx + 3, "==>");
            scoreBuilder.replace(preMarkIdx, preMarkIdx + 3, "   ");
        }

        private int getPrePlayer() {
            int prePlayer = player - 1;
            return prePlayer < 1 ? playerNum : prePlayer;
        }

        private int getPlayerIdx(int player) {
            --player;
            int playerRow = 2 + player * 3;
            return playerRow * LINE_IDX_NUM;
        }

        private int getRoundIdx(int player, int round) {
            int playerIdx = getPlayerIdx(player);
            return playerIdx + round * 4;
        }

        private int getScoreIdx() {
            int roundIdx = getRoundIdx(player, 1);
            int scoreIdx = roundIdx + LINE_IDX_NUM;

            while (!scoreBuilder.substring(scoreIdx, scoreIdx + 3).isBlank()) {
                if (isOverFinalScoreIdx(scoreIdx)) break;
                scoreIdx += 4;
            }

            return scoreIdx;
        }

        private boolean isOverFinalScoreIdx(int scoreIdx) {
            int baseScoreIdx = 40;
            return scoreIdx > baseScoreIdx + player * LINE_IDX_NUM * 3;
        }
    }
}
