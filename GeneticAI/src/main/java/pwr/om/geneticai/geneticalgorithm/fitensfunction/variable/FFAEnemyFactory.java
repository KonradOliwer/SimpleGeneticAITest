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
package pwr.om.geneticai.geneticalgorithm.fitensfunction.variable;

import java.util.logging.Level;
import java.util.logging.Logger;
import pwr.om.battlesystem.actor.Actor;
import pwr.om.geneticai.GeneticAI;
import pwr.om.geneticai.chromosome.ChromosomeFactory;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.EnemyFactory;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.RandomActorsFactory;

/**
 *
 * @author KonradOliwer
 */
public class FFAEnemyFactory implements EnemyFactory {

    private final ChromosomeFactory chromosomeFactory;
    private Actor[][] enemies;

    public FFAEnemyFactory(ChromosomeFactory chromosomeFactory) {
        this.chromosomeFactory = chromosomeFactory;
    }

    @Override
    public Actor[][] getEnemies(int[][] population, int[] distribution, Actor testedActor) {
        lasyInitEnemies(population, distribution);
        int populationIndex = 0;
        for (int i = 0; i < enemies.length; i++) {
            for (int j = 0; j < enemies[i].length; j++) {
                ((GeneticAI) enemies[i][j].getAi()).getChromosome().copyArrayReprezentation(population[populationIndex++]);
            }
        }
        return enemies;
    }

    private void lasyInitEnemies(int[][] population, int[] distribution) {
        if (enemies == null) {
            enemies = new Actor[distribution.length][];
            enemies[0] = RandomActorsFactory.generateActors(distribution[0]);
            for (int i = 1; i < enemies.length; i++) {
                enemies[i] = new Actor[distribution[i]];
                for (int j = 0; j < distribution[i]; j++) {
                    try {
                        enemies[i][j] = enemies[0][j].clone();
                    } catch (CloneNotSupportedException ex) {
                        Logger.getLogger(FFAEnemyFactory.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            for (int i = 0; i < enemies.length; i++) {
                for (int j = 0; j < enemies[i].length; j++) {
                    enemies[i][j].setAI(new GeneticAI(chromosomeFactory.createChromosome(enemies[i][j])));
                }
            }
        }
    }

    @Override
    public ChromosomeFactory getChromosomeFactory() {
        return chromosomeFactory;
    }
}
