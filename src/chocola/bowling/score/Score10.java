package chocola.bowling.score;

class Score10 extends Score {

    private boolean bonus1IsPresent;
    private boolean bonus2IsPresent;

    @Override
    void setBonus1(int bonus1) {
        super.setBonus1(bonus1);
        bonus1IsPresent = true;
    }

    @Override
    void setBonus2(int bonus2) {
        super.setBonus2(bonus2);
        bonus2IsPresent = true;
    }

    @Override
    String toPinString() {
        if (isPre()) return "   ";

        int pin1 = getPin1();
        int pin2 = getPin2();
        int bonus1 = getBonus1();
        int bonus2 = getBonus2();
        String pin1ToString = getPinToString(pin1);
        String pin2ToString = getPinToString(pin2);
        String bonus1ToString = getPinToString(bonus1);
        String bonus2ToString = getPinToString(bonus2);

        if (isStrike()) {
            if (bonus2IsPresent) return "X" + bonus1ToString + bonus2ToString;
            if (bonus1IsPresent) return "X" + bonus1ToString + " ";
            return "X  ";
        }

        if (isSpare()) {
            if (bonus1IsPresent) return pin1ToString + "/" + bonus1ToString;
            return pin1ToString + "/ ";
        }

        if (isStart()) return pin1ToString + "  ";

        return pin1ToString + " " + pin2ToString;
    }
}
