package chocola.bowling2;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int playerNum;
        do {
            System.out.print("인원 수를 입력하십시오: ");
            playerNum = scanPlayerNum();
        } while (playerNum < 1);

        BowlingGame game = new BowlingGame(playerNum);
        game.display();

        while (game.isPlaying()) {
            play(game);
            game.display();
        }

        System.out.println("\n-----------------------게임 종료----------------------");
    }

    private static void play(BowlingGame scoreTable) {
        int pin;
        do {
            System.out.print("쓰러진 핀 개수: ");
            pin = scanPin();
        } while (pin == -1 || scoreTable.isNotValidPin(pin));

        scoreTable.addPin(pin);
    }

    private static int scanPlayerNum() {
        int i;

        try {
            i = Integer.parseInt(sc.nextLine().trim());
            if (i < 1 || i > 10) throw new InputMismatchException();
        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("1 이상 10 이하의 숫자로 입력해 주십시오.");
            i = -1;
        }

        return i;
    }

    private static int scanPin() {
        int i;

        try {
            i = Integer.parseInt(sc.nextLine().trim());
            if (i < 0 || i > 10) throw new InputMismatchException();
        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("0 이상 10 이하의 숫자로 입력해 주십시오.");
            i = -1;
        }

        return i;
    }
}
