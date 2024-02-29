package chocola.bowling;

import chocola.bowling.score.ScoreTable;

import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("인원 수를 입력하십시오: ");
        int n = sc.nextInt();

        ScoreTable scoreTable = new ScoreTable(n);
        scoreTable.display();

        for (int round = 1; round < 10; round++) {
            for (int player = 1; player <= n; player++) {
                int pin1 = firstPlay();
                scoreTable.setPin1(player, round, pin1);
                scoreTable.display();

                if (pin1 == 10) continue;

                int pin2 = secondPlay(pin1);
                scoreTable.setPin2(player, round, pin2);
                scoreTable.display();
            }
        }

        for (int player = 1; player <= n; player++) {
            int round = 10;

            int pin1 = firstPlay();
            scoreTable.setPin1(player, round, pin1);
            scoreTable.display();

            if (pin1 == 10) {
                int bonus1 = firstPlay();
                scoreTable.setBonus1(player, round, bonus1);
                scoreTable.display();

                int bonus2 = firstPlay();
                scoreTable.setBonus2(player, round, bonus2);
                scoreTable.display();
                continue;
            }

            int pin2 = secondPlay(pin1);
            scoreTable.setPin2(player, round, pin2);
            scoreTable.display();

            if (pin1 + pin2 == 10) {
                int bonus1 = firstPlay();
                scoreTable.setBonus1(player, round, bonus1);
                scoreTable.display();
            }
        }

        System.out.println("\n" + "-".repeat(23) + "게임 종료" + "-".repeat(22));
    }

    private static int firstPlay() {
        System.out.print("쓰러진 핀 개수: ");
        int pin;
        while ((pin = sc.nextInt()) < 0 || pin > 10) {
            System.out.println("올바른 핀 개수를 입력하세요.");
            System.out.print("쓰러진 핀 개수: ");
        }
        return pin;
    }

    private static int secondPlay(int pin1) {
        System.out.print("쓰러진 핀 개수: ");
        int pin2;
        while ((pin2 = sc.nextInt()) < 0 || pin1 + pin2 > 10) {
            System.out.println("올바른 핀 개수를 입력하세요.");
            System.out.print("쓰러진 핀 개수: ");
        }
        return pin2;
    }
}
