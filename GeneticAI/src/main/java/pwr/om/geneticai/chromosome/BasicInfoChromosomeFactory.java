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
import static pwr.om.battlesystem.actor.Actor.MAX_AP_VALUE;
import static pwr.om.battlesystem.actor.Actor.MAX_HP_VALUE;

/**
 *
 * @author KonradOliwer
 */
public class BasicInfoChromosomeFactory extends ChromosomeFactory {

    @Override
    public Chromosome createChromosome(Actor self) {
        return new BasicInfoChromosome(self);
    }

    @Override
    public boolean isValid(int[] chromosome, int gene) {
        int base = gene % BasicInfoChromosome.INDEXES_PER_ACION;
        return chromosome[gene] >= 0 && (base != 0 && base % 2 == 0 && chromosome[gene] >= chromosome[gene - 1]) && chromosome[gene] <= 100;
    }

    @Override
    public int[][] generatePopulation(Actor actor, int size) {
        Random random = new Random();
        int actionsNumber = actor.getActions().size();
        int[][] population = new int[size][];
        int ap = actor.getStatistics().getAp();
        int hp = actor.getStatistics().getHp();
        for (int i = 0; i < population.length; i++) {
            population[i] = new int[BasicInfoChromosome.INDEXES_PER_ACION * actionsNumber];
            for (int j = 0; j < actionsNumber; j++) {
                int base = BasicInfoChromosome.INDEXES_PER_ACION * j;
                population[i][base + BasicInfoChromosome.BASE_ACTION_PRIORITY_INDEX] = random.nextInt(100);

                population[i][base + BasicInfoChromosome.ENEMY_AP_LOWER_BORDER_INDEX] = random.nextInt(MAX_AP_VALUE);
                population[i][base + BasicInfoChromosome.ENEMY_AP_UPPER_BORDER_INDEX]
                        = randomUpperBoarder(population[i], random, base + BasicInfoChromosome.ENEMY_AP_LOWER_BORDER_INDEX, MAX_AP_VALUE);
                population[i][base + BasicInfoChromosome.ENEMY_HP_LOWER_BORDER_INDEX] = random.nextInt(MAX_HP_VALUE);
                population[i][base + BasicInfoChromosome.ENEMY_HP_UPPER_BORDER_INDEX]
                        = randomUpperBoarder(population[i], random, base + BasicInfoChromosome.ENEMY_HP_LOWER_BORDER_INDEX, MAX_HP_VALUE);

                population[i][base + BasicInfoChromosome.SELF_AP_LOWER_BORDER_INDEX] = random.nextInt(ap);
                population[i][base + BasicInfoChromosome.SELF_AP_UPPER_BORDER_INDEX]
                        = randomUpperBoarder(population[i], random, base + BasicInfoChromosome.SELF_AP_LOWER_BORDER_INDEX, ap);
                population[i][base + BasicInfoChromosome.SELF_HP_LOWER_BORDER_INDEX] = random.nextInt(hp);
                population[i][base + BasicInfoChromosome.SELF_HP_UPPER_BORDER_INDEX]
                        = randomUpperBoarder(population[i], random, base + BasicInfoChromosome.SELF_HP_LOWER_BORDER_INDEX, hp);
            }
            population[i][BasicInfoChromosome.INDEXES_PER_ACION * 9 + BasicInfoChromosome.BASE_ACTION_PRIORITY_INDEX] = 0;
        }
        return population;
    }

    private static int randomUpperBoarder(int[] chromosome, Random random, int lowe, int max) {
        return 1 + random.nextInt(max - chromosome[lowe]) + chromosome[lowe];
    }
}
