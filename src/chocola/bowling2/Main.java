package chocola.bowling2;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int playerNum;
        do {
            System.out.print("인원 수를 입력하십시오: ");
            playerNum = scanInt();
        } while (playerNum <= 0);

        BowlingGame game = new BowlingGame(playerNum);
        game.display();

        while (!game.isEnd()) {
            play(game);
            game.display();
        }

        System.out.println("\n-----------------------게임 종료----------------------");
    }

    private static void play(BowlingGame scoreTable) {
        int pin;
        do {
            System.out.print("쓰러진 핀 개수: ");
            pin = scanInt();
        } while (!scoreTable.addPin(pin));
    }

    private static int scanInt() {
        int i;

        try {
            i = sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("올바른 값을 입력해 주십시오.");
            i = -1;
            sc.next();
        }

        return i;
    }
}
