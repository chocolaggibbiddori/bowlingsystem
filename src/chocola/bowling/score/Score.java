package chocola.bowling.score;

import java.util.function.IntSupplier;

class Score {

    private int pin1;
    private int pin2;
    private int bonus1;
    private int bonus2;
    private int preTotal;
    private Type scoreType = Type.NORMAL;
    private State state = State.PRE;

    void setPin1(int pin1) {
        this.pin1 = pin1;
        if (pin1 == 10) scoreType = Type.STRIKE;
        state = State.START;
    }

    int getPin1() {
        return pin1;
    }

    void setPin2(int pin2) {
        this.pin2 = pin2;
        if (pin1 + pin2 == 10) scoreType = Type.SPARE;
        else state = State.END;
    }

    int getPin2() {
        return pin2;
    }

    void setBonus1(int bonus1) {
        this.bonus1 = bonus1;
        if (isSpare()) state = State.END;
    }

    int getBonus1() {
        return bonus1;
    }

    void setBonus2(int bonus2) {
        this.bonus2 = bonus2;
        state = State.END;
    }

    int getBonus2() {
        return bonus2;
    }

    void setPreTotal(int preTotal) {
        this.preTotal = preTotal;
    }

    int getTotal() {
        IntSupplier ts = () -> preTotal + pin1 + pin2 + bonus1 + bonus2;
        return isEnd() ? ts.getAsInt() : 0;
    }

    boolean isSpare() {
        return scoreType == Type.SPARE;
    }

    boolean isStrike() {
        return scoreType == Type.STRIKE;
    }

    boolean isPre() {
        return state == State.PRE;
    }

    boolean isStart() {
        return state == State.START;
    }

    boolean isEnd() {
        return state == State.END;
    }

    String toPinString() {
        if (isPre()) return "   ";
        if (isStrike()) return " X ";
        if (isSpare()) return getPinToString(pin1) + " /";
        if (isStart()) return getPinToString(pin1) + "  ";
        return getPinToString(pin1) + " " + getPinToString(pin2);
    }

    String getPinToString(int pin) {
        return pin == 0 ? "-" : (pin == 10 ? "X" : pin + "");
    }

    String toScoreString() {
        if (isEnd()) {
            int total = getTotal();
            return formatNumber(total);
        }
        return "   ";
    }

    String formatNumber(int score) {
        if (score < 10) return "  " + score;
        else if (score < 100) return " " + score;
        else if (score <= 300) return "" + score;
        else throw new IllegalArgumentException("Score can't exceed 300 points");
    }

    private enum Type {

        NORMAL, SPARE, STRIKE
    }

    private enum State {

        PRE, START, END
    }
}
