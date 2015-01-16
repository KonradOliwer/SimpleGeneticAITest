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

import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author KonradOliwer
 */
public class BloodLine implements Serializable {

    public final int id;
    int apIncrease;
    int hpIncrease;
    int movmentSpeedIncrease;
    int agilitiIncrease;
    int inteligenceIncrease;
    int strengthIncrease;

    public BloodLine(int id,
            int apIncrease,
            int hpIncrease,
            int movmentSpeedIncrease,
            int agilitiIncrease,
            int inteligenceIncrease,
            int strengthIncrease) {

        this.id = id;
        this.apIncrease = apIncrease;
        this.hpIncrease = hpIncrease;
        this.movmentSpeedIncrease = movmentSpeedIncrease;
        this.agilitiIncrease = agilitiIncrease;
        this.inteligenceIncrease = inteligenceIncrease;
        this.strengthIncrease = strengthIncrease;
    }

    public void lvlUp(Actor actor) {
        Random random = new Random();
        actor.getStatistics().ap = actor.statisticIncrease(random, actor.getStatistics().ap, hpIncrease, Actor.MAX_AP_VALUE);
        actor.getStatistics().hp = actor.statisticIncrease(random, actor.getStatistics().hp, hpIncrease, Actor.MAX_HP_VALUE);
        actor.getStatistics().movmentSpeed = actor.statisticIncrease(random, actor.getStatistics().movmentSpeed, movmentSpeedIncrease,
                Actor.MAX_MOVMENT_SPEED_VALUE);
        actor.getStatistics().agility = actor.statisticIncrease(random, actor.getStatistics().agility, agilitiIncrease,
                Actor.MAX_ATTRIBUTE_VALUE);
        actor.getStatistics().inteligence = actor.statisticIncrease(random, actor.getStatistics().inteligence, inteligenceIncrease,
                Actor.MAX_ATTRIBUTE_VALUE);
        actor.getStatistics().strength = actor.statisticIncrease(random, actor.getStatistics().strength, strengthIncrease, 
                Actor.MAX_ATTRIBUTE_VALUE);
    }

    public int getId() {
        return id;
    }
}
