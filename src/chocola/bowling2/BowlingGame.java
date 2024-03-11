package chocola.bowling2;

import java.util.ArrayList;
import java.util.List;

public class BowlingGame {

    private final List<List<Integer>> pinList;
    private final StringBuilder scoreBoard;
    private final int playerNum;
    private int player = 1;
    private int round = 1;
    private int remainPins = 10;
    private Order order = Order.FIRST;

    public BowlingGame(int playerNum) {
        this.playerNum = playerNum;
        this.scoreBoard = initScoreBoard();
        this.pinList = initPinList();
    }

    public boolean addPin(int pin) {
        if (!validatePin(pin)) return false;

        List<Integer> pinList = this.pinList.get(player);
        pinList.add(pin);
        remainPins -= pin;
        addPinInBoard(pinList, pin);
        addScoreInBoard(pinList);
        checkTurn(pinList);

        if (remainPins == 0) pinInit();
        return true;
    }

    public void display() {
        checkPlayer();
        System.out.println(scoreBoard);
    }

    public boolean isEnd() {
        return round > 10;
    }

    private StringBuilder initScoreBoard() {
        int capacity = 53 * 3 * playerNum;
        StringBuilder scoreBoard = new StringBuilder(capacity);
        String header = "   | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10| TOTAL |\n";
        String line = "----------------------------------------------------\n";

        scoreBoard
                .append(header)
                .append(line);
        for (int player = 1; player <= playerNum; player++) {
            char playerChar = playerToChar(player);
            scoreBoard
                    .append("   |   |   |   |   |   |   |   |   |   |   |       |\n ")
                    .append(playerChar)
                    .append(" |   |   |   |   |   |   |   |   |   |   |       |\n")
                    .append(line);
        }

        return scoreBoard;
    }

    private char playerToChar(int player) {
        return (char) (player + 64);
    }

    private List<List<Integer>> initPinList() {
        List<List<Integer>> pinList = new ArrayList<>(playerNum + 1);
        for (int i = 0; i <= playerNum; i++)
            pinList.add(new ArrayList<>(21));
        return pinList;
    }

    private boolean validatePin(int pin) {
        boolean valid = pin >= 0 && pin <= 10 && remainPins - pin >= 0;
        if (!valid) System.out.println("올바른 핀 개수를 입력해 주십시오.");
        return valid;
    }

    private void addPinInBoard(List<Integer> pinList, int pin) {
        int idx = getBoardIdx(player, round);
        String pinStr = pinToString(pin);

        switch (order) {
            case FIRST -> {
                if (round < 10) {
                    if (pin == 10) scoreBoard.replace(idx, idx + 3, " X ");
                    else scoreBoard.replace(idx, idx + 1, pinStr);
                } else {
                    if (pin == 10) scoreBoard.replace(idx, idx + 1, "X");
                    else scoreBoard.replace(idx, idx + 1, pinStr);
                }
            }
            case SECOND -> {
                int prePin = pinList.get(pinList.size() - 2);

                if (round < 10) {
                    if (remainPins == 0) scoreBoard.replace(idx + 2, idx + 3, "/");
                    else scoreBoard.replace(idx + 2, idx + 3, pinStr);
                } else {
                    if (prePin == 10) {
                        if (pin == 10) scoreBoard.replace(idx + 1, idx + 2, "X");
                        else scoreBoard.replace(idx + 1, idx + 2, pinStr);
                    } else {
                        if (remainPins == 0) scoreBoard.replace(idx + 1, idx + 2, "/");
                        else scoreBoard.replace(idx + 2, idx + 3, pinStr);
                    }
                }
            }
            case THIRD -> {
                int prePin = pinList.get(pinList.size() - 2);

                if (prePin == 10) {
                    if (pin == 10) scoreBoard.replace(idx + 2, idx + 3, "X");
                    else scoreBoard.replace(idx + 2, idx + 3, pinStr);
                } else {
                    if (remainPins == 0) scoreBoard.replace(idx + 2, idx + 3, "/");
                    else scoreBoard.replace(idx + 2, idx + 3, pinStr);
                }
            }
        }
    }

    private String pinToString(int pin) {
        if (pin == 0) return "-";
        else return pin + "";
    }

    private void addScoreInBoard(List<Integer> pinList) {
        // TODO [2024-03-12]: 점수 표기 기능
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
        player++;
        if (player > playerNum) {
            player = 1;
            round++;
        }
    }

    private void pinInit() {
        remainPins = 10;
    }

    private void orderInit() {
        order = Order.FIRST;
    }

    private void checkPlayer() {
        int nowPlayer = player;
        int prePlayer = prePlayer();
        int nowMarkIdx = getBoardIdx(nowPlayer);
        int preMarkIdx = getBoardIdx(prePlayer);

        scoreBoard
                .replace(nowMarkIdx, nowMarkIdx + 3, "==>")
                .replace(preMarkIdx, preMarkIdx + 3, "   ");
    }

    private int prePlayer() {
        int prePlayer = player - 1;
        return prePlayer < 1 ? playerNum : prePlayer;
    }

    private int getBoardIdx(int player) {
        player--;
        int lineIdxNum = 53;
        int firstPlayerIdx = lineIdxNum * 2;
        return firstPlayerIdx + player * lineIdxNum * 3;
    }

    private int getBoardIdx(int player, int round) {
        int playerIdx = getBoardIdx(player);
        return playerIdx + round * 4;
    }

    private enum Order {

        FIRST, SECOND, THIRD
    }
}
