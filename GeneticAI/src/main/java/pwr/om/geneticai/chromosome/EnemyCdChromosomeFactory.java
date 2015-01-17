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
import static pwr.om.battlesystem.actor.Actor.MAX_HP_VALUE;
import pwr.om.battlesystem.actor.actions.Action;
import pwr.om.battlesystem.actor.actions.FireAction;
import pwr.om.battlesystem.actor.actions.FireballAction;
import pwr.om.battlesystem.actor.actions.HealAction;
import pwr.om.battlesystem.actor.actions.HitAction;
import pwr.om.battlesystem.actor.actions.MoveAction;
import pwr.om.battlesystem.actor.actions.ReflectAction;
import pwr.om.battlesystem.actor.actions.RegenerationAction;
import pwr.om.battlesystem.actor.actions.ShootAction;
import pwr.om.battlesystem.actor.actions.SlashAction;
import pwr.om.battlesystem.actor.actions.StrengtheningArmorAction;

/**
 *
 * @author KonradOliwer
 */
public class EnemyCdChromosomeFactory extends ChromosomeFactory {

    private static final int UNKNOW = 5;

    @Override
    public Chromosome createChromosome(Actor self) {
        return new EnemyCdChromosome(self);
    }

    @Override
    public boolean isValid(int[] chromosome, int gene) {
        return chromosome[gene] >= 0;
    }

    @Override
    public int[][] generatePopulation(Actor actor, int size) {
        Random random = new Random();
        int actionsNumber = actor.getActions().size();
        int[][] population = new int[size][];
        int ap = actor.getStatistics().getAp();
        int hp = actor.getStatistics().getHp();
        int li;
        int max;
        for (int i = 0; i < population.length; i++) {
            population[i] = new int[EnemyCdChromosome.INDEXES_PER_ACION * actionsNumber];
            for (int j = 0; j < actionsNumber; j++) {
                int base = EnemyCdChromosome.INDEXES_PER_ACION * j;
                population[i][base + EnemyCdChromosome.BASE_ACTION_PRIORITY_INDEX] = random.nextInt(5);

                max = MAX_HP_VALUE;
                li = EnemyCdChromosome.ENEMY_HP_LOWER_BORDER_INDEX;
                population[i][base + li] = random.nextInt(max);
                population[i][base + EnemyCdChromosome.ENEMY_HP_UPPER_BORDER_INDEX]
                        = randomUpperBoarder(population[i], random, base + li, max);
                
                max = getActionCd(actor, FireballAction.ACTION_CD);
                li = EnemyCdChromosome.ENEMY_FIREBALL_LOWER_BORDER_INDEX;
                population[i][base + li] = random.nextInt(max);
                population[i][base + EnemyCdChromosome.ENEMY_FIREBALL_UPPER_BORDER_INDEX] 
                        = randomUpperBoarder(population[i], random, base + li, max);
                max = getActionCd(actor, FireAction.ACTION_CD);
                
                li = EnemyCdChromosome.ENEMY_FIRE_LOWER_BORDER_INDEX;
                population[i][base + li] = random.nextInt(max);
                population[i][base + EnemyCdChromosome.ENEMY_FIRE_UPPER_BORDER_INDEX] 
                        = randomUpperBoarder(population[i], random, base + li, max);
                
                max = getActionCd(actor, HealAction.ACTION_CD);
                li = EnemyCdChromosome.ENEMY_HEAL_LOWER_BORDER_INDEX;
                population[i][base + li] = random.nextInt(max);
                population[i][base + EnemyCdChromosome.ENEMY_HEAL_UPPER_BORDER_INDEX] 
                        = randomUpperBoarder(population[i], random, base + li, max);
                
                max = getActionCd(actor, HitAction.ACTION_CD);
                li = EnemyCdChromosome.ENEMY_HIT_LOWER_BORDER_INDEX;
                population[i][base + li] = random.nextInt(max);
                population[i][base + EnemyCdChromosome.ENEMY_HIT_UPPER_BORDER_INDEX] 
                        = randomUpperBoarder(population[i], random, base + li, max);
                
                max = getActionCd(actor, MoveAction.ACTION_CD);
                li = EnemyCdChromosome.ENEMY_MOVE_LOWER_BORDER_INDEX;
                population[i][base + li] = random.nextInt(max);
                population[i][base + EnemyCdChromosome.ENEMY_MOVE_UPPER_BORDER_INDEX] 
                        = randomUpperBoarder(population[i], random, base + li, max);
                
                max = getActionCd(actor, ReflectAction.ACTION_CD);
                li = EnemyCdChromosome.ENEMY_REFLECT_LOWER_BORDER_INDEX;
                population[i][base + li] = random.nextInt(max);
                population[i][base + EnemyCdChromosome.ENEMY_REFLECT_UPPER_BORDER_INDEX] 
                        = randomUpperBoarder(population[i], random, base + li, max);
                
                max = getActionCd(actor, RegenerationAction.ACTION_CD);
                li = EnemyCdChromosome.ENEMY_REGENERATION_LOWER_BORDER_INDEX;
                population[i][base + li] = random.nextInt(max);
                population[i][base + EnemyCdChromosome.ENEMY_REGENERATION_UPPER_BORDER_INDEX] 
                        = randomUpperBoarder(population[i], random, base + li, max);
                
                max = getActionCd(actor, ShootAction.ACTION_CD);
                li = EnemyCdChromosome.ENEMY_SHOOT_LOWER_BORDER_INDEX;
                population[i][base + li] = random.nextInt(max);
                population[i][base + EnemyCdChromosome.ENEMY_SHOOT_UPPER_BORDER_INDEX] 
                        = randomUpperBoarder(population[i], random, base + li, max);
                
                max = getActionCd(actor, SlashAction.ACTION_CD);
                li = EnemyCdChromosome.SELF_HP_LOWER_BORDER_INDEX;
                population[i][base + li] = random.nextInt(max);
                population[i][base + EnemyCdChromosome.ENEMY_SLASH_UPPER_BORDER_INDEX] 
                        = randomUpperBoarder(population[i], random, base + li, max);
                
                max = getActionCd(actor, StrengtheningArmorAction.ACTION_CD);
                li = EnemyCdChromosome.ENEMY_STRENGTHENING_ARMOR_LOWER_BORDER_INDEX;
                population[i][base + li] = random.nextInt(max);
                population[i][base + EnemyCdChromosome.ENEMY_STRENGTHENING_ARMOR_UPPER_BORDER_INDEX] 
                        = randomUpperBoarder(population[i], random, base + li, max);
            }
            population[i][EnemyCdChromosome.INDEXES_PER_ACION * 9 + EnemyCdChromosome.BASE_ACTION_PRIORITY_INDEX] = 0;
        }
        return population;
    }

    private int getActionCd(Actor actor, int actionId) {
        for (Action action : actor.getActions()) {
            if (action.getId() == actionId) {
                return action.getCooldown();
            }
        }
        return UNKNOW;
    }

    private static int randomUpperBoarder(int[] chromosome, Random random, int lower, int max) {
        return 1 + random.nextInt(max - chromosome[lower]) + chromosome[lower];
    }
}
