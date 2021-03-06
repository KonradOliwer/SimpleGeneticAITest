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
package pwr.om.geneticai.chromosome;

import java.util.Random;
import pwr.om.battlesystem.actor.Actor;

/**
 *
 * @author KonradOliwer
 */
public class ActionPriorityChromosomeFactory extends ChromosomeFactory {

    @Override
    public Chromosome createChromosome(Actor self) {
        return new ActionPriorityChromosome(self);
    }

    @Override
    public boolean isValid(int[] chromosome, int gene) {
        return chromosome[gene] >= 0;
    }

    @Override
    public boolean isValid(int[] chromosome) {
        return true;
    }

    @Override
    public int[][] generatePopulation(Actor actor, int size) {
        Random random = new Random();
        int[][] population = new int[size][];
        for (int i = 0; i < population.length; i++) {
            population[i] = new int[actor.getActions().size()];
            population[i][0] = 1; //wait action
            for (int j = 1; j < actor.getActions().size(); j++) {
                population[i][j] = random.nextInt(100);
            }
        }
        return population;
    }
}
