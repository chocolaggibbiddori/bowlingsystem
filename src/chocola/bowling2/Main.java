package chocola.bowling2;

import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("인원 수를 입력하십시오: ");
        int playerNum = sc.nextInt();

        BowlingGame game = new BowlingGame(playerNum);
        game.display();

        while (!game.isEnd()) {
            int pin = play(game);
            game.addPin(pin);
            game.display();
        }

        System.out.println("\n" + "-".repeat(23) + "게임 종료" + "-".repeat(22));
    }

    private static int play(BowlingGame scoreTable) {
        int pin;
        do {
            System.out.print("쓰러진 핀 개수: ");
            pin = sc.nextInt();
        } while (!scoreTable.validatePin(pin));
        return pin;
    }
}
