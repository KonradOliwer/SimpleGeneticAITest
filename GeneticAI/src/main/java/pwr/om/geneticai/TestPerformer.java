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
import pwr.om.geneticai.chromosome.BasicInfoChromosomeFactory;
import pwr.om.geneticai.chromosome.ChromosomeFactory;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.BattleTasksFactory;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.EnemyFactory;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.variable.FFAEnemyFactory;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.variable.RandomEnemyBattleTask;

/**
 *
 * @author KonradOliwer
 */
public class TestPerformer {

    private static final String SAVE_DIR = "test_resylts.txt";
    private final static int ITERATIONS = 100;
    private final static int POPULATION_SIZE = 100;
    private final static int MAX_ROUNDS = 50;
    private final static int START_DISTANCE = 1000;
    private final static List<Actor> computedActors = new ArrayList();
    private final static List<String> computedActorsNames = new ArrayList();
    private final static StringBuilder resultCollector = new StringBuilder();
    private final static BasicTests test = new BasicTests(ITERATIONS, POPULATION_SIZE);

    public static void main(String[] args) {
        changeLogLvl(Level.WARNING);
        Battleground battleground = new Battleground(MAX_ROUNDS, START_DISTANCE);
        BattleTasksFactory battleTaskFactory = null;
        ChromosomeFactory chromosmeFactory = null;
        EnemyFactory enemyFactory = null;

        chromosmeFactory = new BasicInfoChromosomeFactory();
        enemyFactory = new FFAEnemyFactory(chromosmeFactory);
        battleTaskFactory = new RandomEnemyBattleTask.Factory(battleground, 100, enemyFactory, chromosmeFactory);

        createActor(battleTaskFactory, chromosmeFactory, enemyFactory);

        saveActors();
        saveResults();
    }

    public static void changeLogLvl(Level logLvl) {
        Logger log = LogManager.getLogManager().getLogger("");
        for (Handler h : log.getHandlers()) {
            h.setLevel(logLvl);
        }
    }

    public static void createActor(BattleTasksFactory battleTaskFactory, ChromosomeFactory chromosomeFactory, EnemyFactory enemyFactory) {
        Actor result = test.generateActorWithAI(battleTaskFactory, chromosomeFactory, enemyFactory);
        computedActors.add(result);
        computedActorsNames.add(String.format("bt-%d-%d_%s_%s_%s.act", ITERATIONS, POPULATION_SIZE,
                battleTaskFactory.getClass().getSimpleName(),
                chromosomeFactory.getClass().getSimpleName(),
                enemyFactory.getClass().getSimpleName())
        );
    }

    public static void saveActors() {
        for (int i = 0; i < computedActors.size() && i < computedActorsNames.size(); i++) {
            Data.saveActor(computedActors.get(i), SAVE_DIR);
        }
    }

    public static void saveResults() {
        FileWriter fw = null;
        try {
            fw = new FileWriter(SAVE_DIR);
            fw.append(resultCollector.toString());
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
