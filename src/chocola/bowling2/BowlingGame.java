package chocola.bowling2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BowlingGame {

    private final List<List<Integer>> pinList;
    private final int playerNum;
    private int player = 1;
    private int round = 1;
    private int remainPins = 10;
    private Order order = Order.FIRST;

    public BowlingGame(int playerNum) {
        this.playerNum = ++playerNum;
        pinList = new ArrayList<>(playerNum);
        Collections.fill(pinList, new ArrayList<>(21));
    }

    public void addPin(int pin) {
        List<Integer> l = pinList.get(player);
        l.add(pin);

        if (order == Order.SECOND || pin == 10) {
            nextTurn();
            return;
        }

        order = Order.SECOND;
        remainPins -= pin;
    }

    public boolean validatePin(int pin) {
        return pin >= 0 && pin <= 10 && remainPins - pin >= 0;
    }

    public boolean isEnd() {
        return round > 10;
    }

    public void display() {

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

    private enum Order {

        FIRST, SECOND, THIRD
    }
}
