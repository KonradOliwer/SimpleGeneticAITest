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
package pwr.om.battlesystem.actor;

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
public final class Hero extends Actor {

    private Profession profession;
    private BloodLine bloodLine;

    public Hero(int id, int strength, int inteligence, int agility, int maxAp, int maxHp) {
        super(id);
        statistics.agility = agility;
        statistics.strength = strength;
        statistics.inteligence = inteligence;
        statistics.ap = maxAp;
        statistics.hp = maxHp;
        actions.add(new FireAction());
        actions.add(new FireballAction());
        actions.add(new HealAction());
        actions.add(new HitAction());
        actions.add(new MoveAction());
        actions.add(new ReflectAction());
        actions.add(new RegenerationAction());
        actions.add(new ShootAction());
        actions.add(new SlashAction());
        actions.add(new StrengtheningArmorAction());
    }

    public Hero(int id, BloodLine bloodLine, Profession profession, int lvl) {
        super(id);
        this.profession = profession;
        this.bloodLine = bloodLine;
        statistics.agility = 1;
        statistics.strength = 1;
        statistics.inteligence = 1;
        statistics.ap = 100;
        statistics.hp = 10;
        statistics.movmentSpeed = 80;
        for (int i = 0; i < lvl; i++) {
            profession.lvlUp(this);
            bloodLine.lvlUp(this);
        }
    }
}
