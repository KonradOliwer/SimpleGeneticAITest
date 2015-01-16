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

import pwr.om.battlesystem.actor.Actor;
import pwr.om.battlesystem.actor.State;

/**
 *
 * @author KonradOliwer
 */
public class BasicInfoChromosome extends Chromosome {

    final static int INDEXES_PER_ACION = 9;
    final static int BASE_ACTION_PRIORITY_INDEX = 0;
    final static int SELF_HP_LOWER_BORDER_INDEX = 1;
    final static int SELF_HP_UPPER_BORDER_INDEX = 2;
    final static int SELF_AP_LOWER_BORDER_INDEX = 3;
    final static int SELF_AP_UPPER_BORDER_INDEX = 4;
    final static int ENEMY_HP_LOWER_BORDER_INDEX = 5;
    final static int ENEMY_HP_UPPER_BORDER_INDEX = 6;
    final static int ENEMY_AP_LOWER_BORDER_INDEX = 7;
    final static int ENEMY_AP_UPPER_BORDER_INDEX = 8;

    public BasicInfoChromosome(Actor self) {
        super(self, INDEXES_PER_ACION);
    }

    public BasicInfoChromosome(Actor self, int[] arrayReprezentation) {
        this(self);
        this.copyArrayReprezentation(arrayReprezentation);
    }

    @Override
    public int getActionPriority(Actor self, Actor enemy, int actionId) {
        int actionIndex = -1;
        for (int i = 0; i < actionIds.length; i++) {
            if (actionIds[i] == actionId) {
                actionIndex = i;
                break;
            }
        }
        State s = self.getState();
        State e = enemy.getState();
        int startIndexOfAction = actionIndex * INDEXES_PER_ACION;
        return (int) (chromosome[startIndexOfAction + BASE_ACTION_PRIORITY_INDEX]
                * between(SELF_HP_LOWER_BORDER_INDEX, SELF_HP_UPPER_BORDER_INDEX, startIndexOfAction, s.hp)
                * between(SELF_AP_LOWER_BORDER_INDEX, SELF_AP_UPPER_BORDER_INDEX, startIndexOfAction, s.ap)
                * between(ENEMY_HP_LOWER_BORDER_INDEX, ENEMY_HP_UPPER_BORDER_INDEX, startIndexOfAction, e.hp)
                * between(ENEMY_AP_LOWER_BORDER_INDEX, ENEMY_AP_UPPER_BORDER_INDEX, startIndexOfAction, e.ap)
                );
    }

    private double between(int lowerIndex, int upperIndex, int offset, int value) {
        return (chromosome[offset + lowerIndex] <= value && chromosome[offset + upperIndex] >= value) ? 1 : 0.8;
    }
}
