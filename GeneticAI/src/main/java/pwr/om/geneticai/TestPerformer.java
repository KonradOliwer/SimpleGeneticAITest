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

import java.awt.Toolkit;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import pwr.om.battlesystem.Battleground;
import pwr.om.battlesystem.actor.Actor;
import pwr.om.battlesystem.ai.EnchantedRandomAI;
import pwr.om.geneticai.chromosome.BasicInfoChromosomeFactory;
import pwr.om.geneticai.chromosome.ChromosomeFactory;
import pwr.om.geneticai.chromosome.EnemyCdChromosomeFactory;
import pwr.om.geneticai.geneticalgorithm.crossover.BaseGenesCrossover;
import pwr.om.geneticai.geneticalgorithm.crossover.PartialGenesCrossover;
import pwr.om.geneticai.geneticalgorithm.crossover.RandomGenesCrossover;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.BattleTasksFactory;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.EnemyFactory;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.variable.HpCheckBattleTask;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.variable.RandomAIEnemyFactory;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.variable.RandomEnemyBattleTask;

/**
 *
 * @author KonradOliwer
 */
public class TestPerformer {

    private static final Logger LOG = Logger.getLogger(TestPerformer.class.getName());
    private static final String SAVE_DIR = "test_resylts.txt";
    private final static int REPEAT = 4;
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

    public static void main(String[] args) throws Exception {
        changeLogLvl(Level.INFO);
//        performeSinge();
        performeGeneralTest(true);
//        loadForTests();
//        randomActorTests(50, true);
        sound();
    }

    private static void sound() {
        Runnable runnable;
        for (int i = 0; i < 4; i++) {
            try {
                runnable = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.asterisk");
                if (runnable != null) {
                    runnable.run();
                }
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
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

    private static void performeGeneralTest(boolean decresteInt) {
        List<BattleTasksFactory> battleTasksFactories;
        List<BaseGenesCrossover> crossovers;
        List<Double> mutationChances;
        List<Integer> heroIndexes;
        computedActors = new ArrayList[REPEAT];
        for (int i = 0; i < computedActors.length; i++) {
            computedActors[i] = new ArrayList();
        }
        for (currentRound = 0; currentRound < REPEAT; currentRound++) {
            LOG.log(Level.INFO, String.format("Round %d/%d", currentRound+1, REPEAT));
            computedActorIndex = 0;
            battleTasksFactories = getBattleTasksFactories();
            crossovers = new ArrayList();
            crossovers.add(new PartialGenesCrossover(9));
//            crossovers.add(new RandomGenesCrossover());
            mutationChances = new ArrayList();
            mutationChances.add(0.005);
            heroIndexes = new ArrayList();
            heroIndexes.add(50);
//            heroIndexes.add(750);
            if (decresteInt) {
                for (Integer heroIndexe : heroIndexes) {
                    Data.getSavedActor()[heroIndexe].getStatistics().setInteligence(1);
                }
            }
            LOG.log(Level.INFO, "init finished");
            int maxSize = heroIndexes.size() * battleTasksFactories.size() * crossovers.size() * mutationChances.size();
            int step = 1;

            for (Integer heroIndexe : heroIndexes) {
                for (BattleTasksFactory battleTasksFactory : battleTasksFactories) {
                    for (BaseGenesCrossover crossover : crossovers) {
                        for (Double mutationChance : mutationChances) {
                            createActor(battleTasksFactory, battleTasksFactory.getChromosomeFactory(), battleTasksFactory.getEnemiesFactory(),
                                    crossover, mutationChance, heroIndexe);
                            LOG.log(Level.INFO, String.format("Round %d/%d: %d/%d", currentRound+1, REPEAT, step++, maxSize));
                        }
                    }
                }
            }
        }

        LOG.log(Level.INFO, "Start tests");
        testActors();
        LOG.log(Level.INFO, "Tests compleat");
        saveResults();
        LOG.log(Level.INFO, "Tests results saved");
        System.out.println(acc.toString());
        int index = 0;
        for (int i = 0; i < computedActors.length; i++) {
            for (Actor actor : computedActors[i]) {
                saveActor(actor, Integer.toString(index++));
            }
        }
        LOG.log(Level.INFO, "Actors saved");
    }

    public static void loadForTests() {
        computedActors = new ArrayList[1];
        computedActors[0] = Data.loadAllComputedActors();
        testActors();
        System.out.println(acc.toString());
    }

    public static void randomActorTests(int index, boolean decresteInt) {
        computedActors = new ArrayList[REPEAT];
        if (decresteInt) {
            Data.getSavedActor()[index].getStatistics().setInteligence(1);
        }
        for (int round = 0; round < REPEAT; round++) {
            computedActors[round] = new ArrayList();
            computedActors[round].add(Data.getSavedActor()[index]);
            computedActors[round].get(0).setAI(new EnchantedRandomAI());
        }
        testActors();
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
//            enemyFactories.add(new FFAEnemyFactory(chromosmeFactory));
        }
        return enemyFactories;
    }

    private static List<ChromosomeFactory> getChromosomeFactories() {
        List<ChromosomeFactory> chromosmeFactories = new ArrayList();
//        chromosmeFactories.add(new ActionPriorityChromosomeFactory());
//        chromosmeFactories.add(new BasicInfoAddChromosomeFactory());
        chromosmeFactories.add(new BasicInfoChromosomeFactory());
//        chromosmeFactories.add(new EnemyCdChromosomeFactory());
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
            acc.append(i).append("\t");
            acc.append(String.format("%.3f", selfRandomAITest[i])).append("\t");
            acc.append(String.format("%.3f", randomAIFullTest[i]));
            for (int j = 0; j < computedActors[0].size(); j++) {
                acc.append("\t").append(String.format("%.2f", geneticAIFight[i][j]));
            }
            acc.append("\n");
        }
    }

    public static void saveActor(Actor actor, String name) {
        Data.saveActor(actor, String.format("%s.actor", name));
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
            LOG.log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }
}
