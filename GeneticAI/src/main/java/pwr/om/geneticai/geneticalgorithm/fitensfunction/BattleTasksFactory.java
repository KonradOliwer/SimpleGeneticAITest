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
package pwr.om.geneticai.geneticalgorithm.fitensfunction;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import pwr.om.battlesystem.Battleground;
import pwr.om.battlesystem.actor.Actor;
import pwr.om.geneticai.GeneticAI;
import pwr.om.geneticai.chromosome.ChromosomeFactory;

/**
 *
 * @author KonradOliwer
 */
public abstract class BattleTasksFactory {

    private Actor testedActor;
    private final ChromosomeFactory chromosomeFactory;
    protected final Battleground battleground;
    protected final int repeats;
    private final EnemyFactory enemiesFactory;
    private Actor[][] testedActors;

    public BattleTasksFactory(Battleground battleground, int repeats, EnemyFactory enemiesFactory, ChromosomeFactory chromosomeFactory) {
        this.chromosomeFactory = chromosomeFactory;
        this.battleground = battleground;
        this.repeats = repeats;
        this.enemiesFactory = enemiesFactory;
    }

    protected abstract Callable getBattleTask(Actor[] actorsTested, Actor[] enemis, int populationStartIndex, int size);

    public FutureTask[] produceTasks(int[][] population, int[] distribution) {
        lasyInitTestedActors(distribution);
        Actor[][] enemies = enemiesFactory.getEnemies(population, distribution, testedActor);
        FutureTask<int[]>[] result = new FutureTask[distribution.length];
        int startIndex = 0;
        for (int i = 0; i < distribution.length; i++) {
            for (int j = 0; j < testedActors[i].length; j++) {
                ((GeneticAI) testedActors[i][j].getAi()).getChromosome().copyArrayReprezentation(population[startIndex + j]);
            }
            result[i] = new FutureTask(getBattleTask(testedActors[i], enemies[i], startIndex, distribution[i]));
            startIndex += distribution[i];
        }
        return result;
    }

    private void lasyInitTestedActors(int[] distribution) {
        if (testedActors == null) {
            testedActors = new Actor[distribution.length][];
            for (int i = 0; i < distribution.length; i++) {
                testedActors[i] = new Actor[distribution[i]];
                for (int j = 0; j < distribution[i]; j++) {
                    try {
                        testedActors[i][j] = testedActor.clone();
                        testedActors[i][j].setAI(new GeneticAI(chromosomeFactory.createChromosome(testedActors[i][j])));
                    } catch (CloneNotSupportedException ex) {
                        Logger.getLogger(BattleTasksFactory.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public void setTestedActor(Actor testedActor) {
        this.testedActor = testedActor;
    }

    public Actor getTestedActor() {
        return testedActor;
    }

    public ChromosomeFactory getChromosomeFactory() {
        return chromosomeFactory;
    }

    public Battleground getBattleground() {
        return battleground;
    }

    public int getRepeats() {
        return repeats;
    }

    public EnemyFactory getEnemiesFactory() {
        return enemiesFactory;
    }
}
