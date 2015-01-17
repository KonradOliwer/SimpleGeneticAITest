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

import java.util.List;
import pwr.om.battlesystem.actor.Actor;
import pwr.om.battlesystem.actor.State;
import pwr.om.battlesystem.actor.Statistics;
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
public class EnemyCdChromosome extends Chromosome {

    private static final int NEVER = Integer.MAX_VALUE / 2;
    final static int INDEXES_PER_ACION = 25;
    final static int BASE_ACTION_PRIORITY_INDEX = 0;
    final static int SELF_HP_LOWER_BORDER_INDEX = 1;
    final static int SELF_HP_UPPER_BORDER_INDEX = 2;
    final static int ENEMY_HP_LOWER_BORDER_INDEX = 3;
    final static int ENEMY_HP_UPPER_BORDER_INDEX = 4;
    final static int ENEMY_FIRE_LOWER_BORDER_INDEX = 5;
    final static int ENEMY_FIRE_UPPER_BORDER_INDEX = 6;
    final static int ENEMY_FIREBALL_LOWER_BORDER_INDEX = 7;
    final static int ENEMY_FIREBALL_UPPER_BORDER_INDEX = 8;
    final static int ENEMY_HEAL_LOWER_BORDER_INDEX = 9;
    final static int ENEMY_HEAL_UPPER_BORDER_INDEX = 10;
    final static int ENEMY_HIT_LOWER_BORDER_INDEX = 11;
    final static int ENEMY_HIT_UPPER_BORDER_INDEX = 12;
    final static int ENEMY_MOVE_LOWER_BORDER_INDEX = 13;
    final static int ENEMY_MOVE_UPPER_BORDER_INDEX = 14;
    final static int ENEMY_REFLECT_LOWER_BORDER_INDEX = 15;
    final static int ENEMY_REFLECT_UPPER_BORDER_INDEX = 16;
    final static int ENEMY_REGENERATION_LOWER_BORDER_INDEX = 17;
    final static int ENEMY_REGENERATION_UPPER_BORDER_INDEX = 18;
    final static int ENEMY_SHOOT_LOWER_BORDER_INDEX = 19;
    final static int ENEMY_SHOOT_UPPER_BORDER_INDEX = 20;
    final static int ENEMY_SLASH_LOWER_BORDER_INDEX = 21;
    final static int ENEMY_SLASH_UPPER_BORDER_INDEX = 22;
    final static int ENEMY_STRENGTHENING_ARMOR_LOWER_BORDER_INDEX = 23;
    final static int ENEMY_STRENGTHENING_ARMOR_UPPER_BORDER_INDEX = 24;

    public EnemyCdChromosome(Actor self) {
        super(self, INDEXES_PER_ACION);
    }

    public EnemyCdChromosome(Actor self, int[] arrayReprezentation) {
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
                + 3 * between(SELF_HP_LOWER_BORDER_INDEX, SELF_HP_UPPER_BORDER_INDEX, startIndexOfAction, s.hp)
                + 3 * between(ENEMY_HP_LOWER_BORDER_INDEX, ENEMY_HP_UPPER_BORDER_INDEX, startIndexOfAction, e.hp)
                + between(ENEMY_FIREBALL_LOWER_BORDER_INDEX, ENEMY_FIREBALL_UPPER_BORDER_INDEX, startIndexOfAction,
                        getWhenActionReady(self.getActions(), FireballAction.ACTION_ID))
                + between(ENEMY_FIRE_LOWER_BORDER_INDEX, ENEMY_FIRE_UPPER_BORDER_INDEX, startIndexOfAction,
                        getWhenActionReady(self.getActions(), FireAction.ACTION_ID))
                + between(ENEMY_HEAL_LOWER_BORDER_INDEX, ENEMY_HEAL_UPPER_BORDER_INDEX, startIndexOfAction,
                        getWhenActionReady(self.getActions(), HealAction.ACTION_ID))
                + between(ENEMY_HIT_LOWER_BORDER_INDEX, ENEMY_HIT_UPPER_BORDER_INDEX, startIndexOfAction,
                        getWhenActionReady(self.getActions(), HitAction.ACTION_ID))
                + between(ENEMY_MOVE_LOWER_BORDER_INDEX, ENEMY_MOVE_UPPER_BORDER_INDEX, startIndexOfAction,
                        getWhenActionReady(self.getActions(), MoveAction.ACTION_ID))
                + between(ENEMY_REFLECT_LOWER_BORDER_INDEX, ENEMY_REFLECT_UPPER_BORDER_INDEX, startIndexOfAction,
                        getWhenActionReady(self.getActions(), ReflectAction.ACTION_ID))
                + between(ENEMY_REGENERATION_LOWER_BORDER_INDEX, ENEMY_REGENERATION_UPPER_BORDER_INDEX, startIndexOfAction,
                        getWhenActionReady(self.getActions(), RegenerationAction.ACTION_ID))
                + between(ENEMY_SHOOT_LOWER_BORDER_INDEX, ENEMY_SHOOT_UPPER_BORDER_INDEX, startIndexOfAction,
                        getWhenActionReady(self.getActions(), ShootAction.ACTION_ID))
                + between(ENEMY_SLASH_LOWER_BORDER_INDEX, ENEMY_SLASH_UPPER_BORDER_INDEX, startIndexOfAction,
                        getWhenActionReady(self.getActions(), SlashAction.ACTION_ID))
                + between(ENEMY_STRENGTHENING_ARMOR_LOWER_BORDER_INDEX, ENEMY_STRENGTHENING_ARMOR_UPPER_BORDER_INDEX, startIndexOfAction,
                        getWhenActionReady(self.getActions(), StrengtheningArmorAction.ACTION_ID)));
    }

    private int getWhenActionReady(List<Action> actions, int actionId) {
        for (Action action : actions) {
            if (action.getId() == actionId) {
                return action.getWhenReady();
            }
        }
        return NEVER;
    }

    private double between(int lowerIndex, int upperIndex, int offset, int value) {
        return (chromosome[offset + lowerIndex] <= value && chromosome[offset + upperIndex] >= value) ? 1 : 0;
    }
}
