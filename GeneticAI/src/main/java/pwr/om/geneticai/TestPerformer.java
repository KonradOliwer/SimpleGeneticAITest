/*
 * Copyright 2015 KonradOliwer.
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
package pwr.om.geneticai;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import pwr.om.battlesystem.Battleground;
import pwr.om.battlesystem.actor.Actor;
import pwr.om.geneticai.chromosome.ActionPriorityChromosomeFactory;
import pwr.om.geneticai.chromosome.BasicInfoAddChromosomeFactory;
import pwr.om.geneticai.chromosome.BasicInfoChromosomeFactory;
import pwr.om.geneticai.chromosome.ChromosomeFactory;
import pwr.om.geneticai.chromosome.EnemyCdChromosomeFactory;
import pwr.om.geneticai.geneticalgorithm.crossover.BaseGenesCrossover;
import pwr.om.geneticai.geneticalgorithm.crossover.PartialGenesCrossover;
import pwr.om.geneticai.geneticalgorithm.crossover.RandomGenesCrossover;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.BattleTasksFactory;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.EnemyFactory;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.variable.FFAEnemyFactory;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.variable.HpCheckBattleTask;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.variable.RandomAIEnemyFactory;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.variable.RandomEnemyBattleTask;

/**
 *
 * @author KonradOliwer
 */
public class TestPerformer {

    private static final String SAVE_DIR = "test_resylts.txt";
    private final static int REPEAT = 2;
    private final static int ITERATIONS = 1000;
    private final static int POPULATION_SIZE = 500;
    private final static int MAX_ROUNDS = 50;
    private final static int START_DISTANCE = 1000;
    private final static StringBuilder acc = new StringBuilder();
    private final static BasicTests test = new BasicTests(ITERATIONS, POPULATION_SIZE);
    private final static Battleground battleground = new Battleground(MAX_ROUNDS, START_DISTANCE);
    private static List<Actor>[] computedActors;
    private static double[] selfRandomAITest;
    private static double[] randomAIFullTest;
    private static double[][] geneticAIFight;
    private static int computedActorIndex;
    private static int currentRound = 0;

    public static void main(String[] args) {
        changeLogLvl(Level.WARNING);
//        performeSinge();
        performeGeneralTest();
    }

    private static void performeSinge() {
        BattleTasksFactory battleTaskFactory;
        ChromosomeFactory chromosmeFactory;
        EnemyFactory enemyFactory;
        BaseGenesCrossover crossover;
        double mutatuonChance;

        mutatuonChance = 0.05;
        crossover = new RandomGenesCrossover();
        chromosmeFactory = new EnemyCdChromosomeFactory();
        enemyFactory = new RandomAIEnemyFactory(chromosmeFactory);
        battleTaskFactory = new RandomEnemyBattleTask.Factory(battleground, 5, enemyFactory, chromosmeFactory);
        createActor(battleTaskFactory, chromosmeFactory, enemyFactory, crossover, mutatuonChance);

        testActors();

        saveResults();
        System.out.println(acc.toString());
    }

    private static void performeGeneralTest() {
        List<BattleTasksFactory> battleTasksFactories;
        List<BaseGenesCrossover> crossovers;
        List<Double> mutationChances;
        List<Integer> heroIndexes;
        computedActors = new ArrayList[REPEAT];
        for (int i = 0; i < computedActors.length; i++) {
            computedActors[i] = new ArrayList();
        }
        for (currentRound = 0; currentRound < REPEAT; currentRound++) {
            computedActorIndex = 0;
            battleTasksFactories = getBattleTasksFactories();
            crossovers = new ArrayList();
            crossovers.add(new PartialGenesCrossover(9));
            crossovers.add(new RandomGenesCrossover());
            mutationChances = new ArrayList();
            mutationChances.add(0.005);
            heroIndexes = new ArrayList();
            heroIndexes.add(50);
            heroIndexes.add(700);

            for (BattleTasksFactory battleTasksFactory : battleTasksFactories) {
                for (BaseGenesCrossover crossover : crossovers) {
                    for (Double mutationChance : mutationChances) {
                        createActor(battleTasksFactory, battleTasksFactory.getChromosomeFactory(), battleTasksFactory.getEnemiesFactory(),
                                crossover, mutationChance);
                    }
                }
            }
        }

        testActors();
        saveResults();
        System.out.println(acc.toString());
    }

    private static void createActor(BattleTasksFactory battleTaskFactory, ChromosomeFactory chromosomeFactory, EnemyFactory enemyFactory,
            BaseGenesCrossover crossover, double mutationChance) {
        createActor(battleTaskFactory, chromosomeFactory, enemyFactory, crossover, mutationChance, 50);
    }

    private static void createActor(BattleTasksFactory battleTaskFactory, ChromosomeFactory chromosomeFactory, EnemyFactory enemyFactory,
            BaseGenesCrossover crossover, double mutationChance, int actorIndex) {
        Actor result = test.generateActorWithAI(battleTaskFactory, chromosomeFactory, enemyFactory, crossover, actorIndex, mutationChance);
        computedActors[currentRound].add(result);

        acc.append(computedActorIndex++).append(": ").append(
                String.format("%d\t%d\t%d\t%.3f\t%s\t%s\t%s\t%s", ITERATIONS, POPULATION_SIZE, actorIndex, mutationChance,
                        crossover.getClass().getSimpleName(),
                        battleTaskFactory.getClass().getSimpleName(),
                        chromosomeFactory.getClass().getSimpleName(),
                        enemyFactory.getClass().getSimpleName())
        ).append("\n");
    }

    private static List<BattleTasksFactory> getBattleTasksFactories() {
        List<BattleTasksFactory> result = new ArrayList();
        List<EnemyFactory> enemyFactories = getEnemyFactories(getChromosomeFactories());

        for (EnemyFactory enemyFactory : enemyFactories) {
            result.add(new HpCheckBattleTask.Factory(battleground, 5, enemyFactory, enemyFactory.getChromosomeFactory()));
            result.add(new RandomEnemyBattleTask.Factory(battleground, 5, enemyFactory, enemyFactory.getChromosomeFactory()));
        }
        return result;
    }

    private static List<EnemyFactory> getEnemyFactories(List<ChromosomeFactory> chromosmeFactories) {
        List<EnemyFactory> enemyFactories = new ArrayList();
        for (ChromosomeFactory chromosmeFactory : chromosmeFactories) {
            enemyFactories.add(new RandomAIEnemyFactory(chromosmeFactory));
            enemyFactories.add(new FFAEnemyFactory(chromosmeFactory));
        }
        return enemyFactories;
    }

    private static List<ChromosomeFactory> getChromosomeFactories() {
        List<ChromosomeFactory> chromosmeFactories = new ArrayList();
        chromosmeFactories.add(new ActionPriorityChromosomeFactory());
        chromosmeFactories.add(new BasicInfoAddChromosomeFactory());
        chromosmeFactories.add(new BasicInfoChromosomeFactory());
        chromosmeFactories.add(new EnemyCdChromosomeFactory());
        return chromosmeFactories;
    }

    private static void testActors() {
        selfRandomAITest = new double[computedActors[0].size()];
        randomAIFullTest = new double[computedActors[0].size()];
        geneticAIFight = new double[computedActors[0].size()][computedActors[0].size()];
        for (int round = 0; round < REPEAT; round++) {
            for (int i = 0; i < computedActors[round].size(); i++) {
                selfRandomAITest[i] += test.selfRandomAITest(battleground, computedActors[round].get(i), 100);
                randomAIFullTest[i] += test.randomAIFullTest(battleground, computedActors[round].get(i), 10);
                for (int j = 0; j < computedActors[round].size(); j++) {
                    if (i != j) {
                        geneticAIFight[i][j] += test.geneticAIFight(battleground, computedActors[round].get(i),
                                computedActors[round].get(j));
                    } else {
                        geneticAIFight[i][j] += 0.5;
                    }
                }
            }
        }
        for (int i = 0; i < computedActors[0].size(); i++) {
            selfRandomAITest[i] /= REPEAT;
            randomAIFullTest[i] /= REPEAT;
            for (int j = 0; j < computedActors[0].size(); j++) {
                geneticAIFight[i][j] /= REPEAT;
            }
        }
        acumulateTestResults();
    }

    private static void acumulateTestResults() {
        acc.append("\nactor index\tselfFight\trandomAIFullTest");
        for (int i = 0; i < computedActorIndex; i++) {
            acc.append("\t").append(i);
        }
        acc.append("\n");
        for (int i = 0; i < computedActors[0].size(); i++) {
            acc.append(String.format("%.3f", selfRandomAITest[i])).append("\t");
            acc.append(String.format("%.3f", randomAIFullTest[i]));
            for (int j = 0; j < computedActors[0].size(); j++) {
                acc.append("\t").append(String.format("%.2f", geneticAIFight[i][j]));
            }
            acc.append("\n");
        }
    }

    private static void changeLogLvl(Level logLvl) {
        Logger log = LogManager.getLogManager().getLogger("");
        for (Handler h : log.getHandlers()) {
            h.setLevel(logLvl);
        }
    }

    private static void saveResults() {
        FileWriter fw = null;
        try {
            fw = new FileWriter(SAVE_DIR);
            fw.append(acc.toString());
        } catch (IOException ex) {
            Logger.getLogger(TestPerformer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(TestPerformer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
