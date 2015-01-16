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

import java.util.Random;
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
public class Profession {

    public final int id;
    public int fireballUnlock;
    public int slashUnlock;
    public int hitUnlock;
    public int healUnlock;
    public int reflectUnlock;
    public int strengtheningArmorUnlock;
    public int regenerationUnlock;
    public int moveUnlock;
    public int shootUnlock;
    public int fireUnlock;
    public int apIncrease;
    public int hpIncrease;
    public int movmentSpeedIncrease;

    public Profession(int id,
            int fireballUnlock,
            int slashUnlock,
            int hitUnlock,
            int healUnlock,
            int reflectUnlock,
            int strengtheningArmorUnlock,
            int regenerationUnlock,
            int moveUnlock,
            int shootUnlock,
            int fireUnlock,
            int apIncrease,
            int hpIncrease,
            int movmentSpeedIncrease) {
        this.id = id;
        this.fireballUnlock = fireballUnlock;
        this.slashUnlock = slashUnlock;
        this.hitUnlock = hitUnlock;
        this.healUnlock = healUnlock;
        this.reflectUnlock = reflectUnlock;
        this.strengtheningArmorUnlock = strengtheningArmorUnlock;
        this.regenerationUnlock = regenerationUnlock;
        this.moveUnlock = moveUnlock;
        this.shootUnlock = shootUnlock;
        this.fireUnlock = fireUnlock;
        this.apIncrease = apIncrease;
        this.hpIncrease = hpIncrease;
        this.movmentSpeedIncrease = movmentSpeedIncrease;
    }

    public void lvlUp(Actor actor) {
        Random random = new Random();
        actor.getStatistics().ap = actor.statisticIncrease(random, actor.getStatistics().ap, hpIncrease, Actor.MAX_AP_VALUE);
        actor.getStatistics().hp = actor.statisticIncrease(random, actor.getStatistics().hp, hpIncrease, Actor.MAX_HP_VALUE);
        actor.getStatistics().movmentSpeed = actor.statisticIncrease(random, actor.getStatistics().movmentSpeed, movmentSpeedIncrease,
                Actor.MAX_MOVMENT_SPEED_VALUE);

        boolean[] ownedAbilities = actor.ownedAbilities();

        if (!ownedAbilities[FireAction.ACTION_CD] && unlock(random, fireUnlock)) {
            actor.actions.add(new FireAction());
        }
        if (!ownedAbilities[FireballAction.ACTION_CD] && unlock(random, fireballUnlock)) {
            actor.actions.add(new FireballAction());
        }
        if (!ownedAbilities[HealAction.ACTION_CD] && unlock(random, healUnlock)) {
            actor.actions.add(new HealAction());
        }
        if (!ownedAbilities[HitAction.ACTION_CD] && unlock(random, hitUnlock)) {
            actor.actions.add(new HitAction());
        }
        if (!ownedAbilities[MoveAction.ACTION_CD] && unlock(random, moveUnlock)) {
            actor.actions.add(new MoveAction());
        }
        if (!ownedAbilities[ReflectAction.ACTION_CD] && unlock(random, reflectUnlock)) {
            actor.actions.add(new ReflectAction());
        }
        if (!ownedAbilities[RegenerationAction.ACTION_CD] && unlock(random, regenerationUnlock)) {
            actor.actions.add(new RegenerationAction());
        }
        if (!ownedAbilities[ShootAction.ACTION_CD] && unlock(random, shootUnlock)) {
            actor.actions.add(new ShootAction());
        }
        if (!ownedAbilities[SlashAction.ACTION_CD] && unlock(random, slashUnlock)) {
            actor.actions.add(new SlashAction());
        }
        if (!ownedAbilities[StrengtheningArmorAction.ACTION_CD] && unlock(random, strengtheningArmorUnlock)) {
            actor.actions.add(new StrengtheningArmorAction());
        }
    }

    private boolean unlock(Random random, int chance) {
        return random.nextInt(101) <= chance;
    }
}
