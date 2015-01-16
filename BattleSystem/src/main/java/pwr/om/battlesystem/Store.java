package pwr.om.battlesystem;

import pwr.om.battlesystem.actor.BloodLine;
import pwr.om.battlesystem.actor.Profession;

public class Store {

    private static final Store instance = new Store();
    private final BloodLine[] bloodLines;
    private final Profession[] professions;

    private Store() {
        bloodLines = new BloodLine[4];
        bloodLines[0] = new BloodLine(0, 1, 10, 1, 3, 3, 10);
        bloodLines[1] = new BloodLine(1, 5, 5, 5, 3, 10, 3);
        bloodLines[2] = new BloodLine(2, 5, 5, 5, 7, 7, 7);
        bloodLines[3] = new BloodLine(3, 7, 1, 7, 3, 10, 3);
        professions = new Profession[4];
        professions[0] = new Profession(0, 100, 5, 5, 30, 20, 50, 30, 100, 10, 20, 5, 50, 5);
        professions[1] = new Profession(1, 5, 50, 30, 20, 20, 5, 5, 100, 10, 10, 10, 100, 50);
        professions[2] = new Profession(2, 10, 20, 20, 10, 10, 5, 10, 20, 100, 30, 30, 75, 1);
        professions[3] = new Profession(3, 100, 30, 5, 5, 5, 5, 5, 100, 5, 5, 50, 10, 50);
    }

    public BloodLine getBloodLine(int id) {
        return bloodLines[id];
    }

    public Profession getProfession(int id) {
        return professions[id];
    }

    public BloodLine[] getBloodLines() {
        return bloodLines;
    }

    public Profession[] getProfessions() {
        return professions;
    }

    public static Store getInsctance() {
        return instance;
    }
}
