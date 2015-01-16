/*
 * Copyright 2014 KonradOliwer.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pwr.om.geneticai.geneticalgorithm.fitensfunction.old;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import pwr.om.battlesystem.Battleground;
import pwr.om.battlesystem.actor.Actor;
import pwr.om.battlesystem.actor.Hero;
import pwr.om.battlesystem.ai.RandomAI;
import pwr.om.geneticai.chromosome.Chromosome;
import pwr.om.geneticai.chromosome.ChromosomeFactory;
import pwr.om.geneticalgorithm.integer.FitnesFuntion;

/**
 *
 * @author KonradOliwer
 */
public class OldBattleFitnesFuntion implements FitnesFuntion {

    private static final int ENEMIES_NUMBER_PER_DEMON = 1000;
    private static final int REPEATS = 10;
    private static final int ENEMIES_LVL_RANGE = 30;
    private static final int WORKERS_NUMBER = Runtime.getRuntime().availableProcessors() - 1;
    private final Battleground battleground;
    private final Actor testedActor;
    private final int winScore;
    private final int drawScore;
    private final int loseScore;
    private final int populationSize;
    private final Chromosome[] localPopulation;
    private int[] managerStartPos;
    private Actor[] enemies;
    private OldBattleManager[] managers;
    private FutureTask<int[]>[] tasks;
    private int[] populationFitnesFunctionValuse;
    private final ExecutorService executorService;

    public OldBattleFitnesFuntion(Battleground battleground, Actor testedActor, int populationSize, int winScore, int drawScor,
            int loseScore, ChromosomeFactory chromosomeFactory) {
        executorService = Executors.newFixedThreadPool(WORKERS_NUMBER);
        this.battleground = battleground;
        this.testedActor = testedActor;
        this.winScore = winScore;
        this.drawScore = drawScor;
        this.loseScore = loseScore;
        this.populationSize = populationSize;
        Random random = new Random();
        int enemyId = 1;
        enemies = new Actor[ENEMIES_NUMBER_PER_DEMON];
        for (int i = 0; i < enemies.length; i++) {
            enemies[i] = new Hero(1, 50, 50, 50, 500, 300);
//            enemies[i] = new Hero(enemyId++, Store.getInsctance().getBloodLine(random.nextInt(4)),
//                    Store.getInsctance().getProfession(random.nextInt(4)), random.nextInt(ENEMIES_LVL_RANGE));
            enemies[i].setAI(new RandomAI());
        }
        initManagers();
        localPopulation = new Chromosome[populationSize];
        for (int i = 0; i < localPopulation.length; i++) {
            localPopulation[i] = chromosomeFactory.newInstance(testedActor);
        }
    }

    @Override
    public void evaluate(int[][] population, int[] populationFitnesFunctionValuse) {
        this.populationFitnesFunctionValuse = populationFitnesFunctionValuse;
        for (int i = 0; i < population.length; i++) {
            localPopulation[i].copyArrayReprezentation(population[i]);
        }
        for (int i = 0; i < managers.length; i++) {
            managers[i].setProcessingData(localPopulation);
            tasks[i] = managers[i].getBattleTask();
            executorService.execute(tasks[i]);

        }
        int[] fitnessFunstionValue;
        FutureTask task;
        try {
            for (int i = 0; i < managers.length; i++) {
                task = tasks[i];
                fitnessFunstionValue = tasks[i].get();
                System.arraycopy(fitnessFunstionValue, 0, populationFitnesFunctionValuse, managerStartPos[i], managers[i].getSize());
            }
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(OldBattleFitnesFuntion.class.getName()).log(Level.SEVERE, null, ex);
        }
        changeToPositiveFunctionValues();
    }

    public void changeToPositiveFunctionValues() {
        if (loseScore < 0) {
            for (int i = 0; i < populationFitnesFunctionValuse.length; i++) {
                populationFitnesFunctionValuse[i] += 1 + REPEATS * loseScore * -1;
            }
        }
        if (drawScore < 0) {
            for (int i = 0; i < populationFitnesFunctionValuse.length; i++) {
                populationFitnesFunctionValuse[i] += 1 + REPEATS * drawScore * -1;
            }
        }
    }

    public void clean() {
        executorService.shutdownNow();
    }

    private void initManagers() {
        int avSize = populationSize / WORKERS_NUMBER;
        int extra = populationSize % WORKERS_NUMBER;
        int[] sizes = new int[WORKERS_NUMBER];
        managerStartPos = new int[WORKERS_NUMBER];
        for (int i = 0; i < WORKERS_NUMBER; i++) {
            sizes[i] = avSize;
            if (i < extra) {
                sizes[i]++;
            }
            if (i > 0) {
                managerStartPos[i] = managerStartPos[i - 1] + sizes[i - 1];
            } else {
                managerStartPos[i] = 0;
            }
        }
        managers = new OldBattleManager[WORKERS_NUMBER];
        tasks = new FutureTask[WORKERS_NUMBER];
        try {
            for (int i = 0; i < managers.length; i++) {
                managers[i] = new OldBattleManager(this, testedActor.clone(), deepCopy(enemies), managerStartPos[i], sizes[i]);
            }
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(OldBattleFitnesFuntion.class.getName()).log(Level.SEVERE, null, ex);
        }
        enemies = null;
    }

    private Actor[] deepCopy(Actor[] actors) {
        Actor[] copy = null;
        try {
            copy = new Actor[actors.length];
            for (int i = 0; i < copy.length; i++) {
                copy[i] = actors[i].clone();
            }
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(OldBattleFitnesFuntion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return copy;
    }

    int getBattleRepeats() {
        return REPEATS;
    }

    public Battleground getBattleground() {
        return battleground;
    }

    public int getWinScore() {
        return winScore;
    }

    public int getDrawScor() {
        return drawScore;
    }

    public int getLoseScore() {
        return loseScore;
    }
}
