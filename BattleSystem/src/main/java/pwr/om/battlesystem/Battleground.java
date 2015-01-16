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
package pwr.om.battlesystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import pwr.om.battlesystem.actor.Actor;

/**
 *
 * @author KonradOliwer
 */
public class Battleground {

    private final Random random = new Random();
    private final List<Integer> actor1Actions = new ArrayList();
    private final List<Integer> actor2Actions = new ArrayList();
    private final int startDistance;
    private final int maxRounds;

    public Battleground(int startDistance, int maxRounds) {
        this.startDistance = startDistance;
        this.maxRounds = maxRounds;
    }

    public Actor determineWinner(Actor actor1, Actor actor2) {
        return determineWinner(actor1, actor2, false);
    }

    public Actor determineWinner(Actor actor1, Actor actor2, boolean saveActionSequence) {
        if (saveActionSequence) {
            actor1Actions.clear();
            actor2Actions.clear();
        }
        actor1.prepairForBattle();
        actor2.prepairForBattle();
        actor1.setStartDistance(startDistance);
        actor2.setStartDistance(startDistance);

        int round = 0;
        while (continueBattle(actor1, actor2) && round++ < maxRounds) {
            while (continueBattle(actor1, actor2) && roundFinished(actor1, actor2)) {
                nextMove(actor1, actor2, saveActionSequence);
            }
            actor1.finishRound();
            actor2.finishRound();
        }
        return round > maxRounds ? null : (actor1.isAlife() ? actor1 : actor2);
    }

    public int getStartDistance() {
        return startDistance;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    private boolean continueBattle(Actor actor1, Actor actor2) {
        return actor1.isAlife() && actor2.isAlife();
    }

    private boolean roundFinished(Actor actor1, Actor actor2) {
        return !actor1.isWaiting() && !actor2.isWaiting();
    }

    private void nextMove(Actor actor1, Actor actor2, boolean saveActionSequence) {
        int actionId;
        if (actor1.getCurrentAp() > actor2.getCurrentAp()) {
            actionId = actor1.takeAction(actor2);
            if (saveActionSequence) {
                actor1Actions.add(actionId);
            }
        } else if (actor1.getCurrentAp() < actor2.getCurrentAp()) {
            actionId = actor2.takeAction(actor1);
            if (saveActionSequence) {
                actor2Actions.add(actionId);
            }
        } else {
            if (random.nextBoolean()) {
                actionId = actor1.takeAction(actor2);
                if (saveActionSequence) {
                    actor1Actions.add(actionId);
                }
            } else {
                actionId = actor2.takeAction(actor1);
                if (saveActionSequence) {
                    actor2Actions.add(actionId);
                }
            }
        }
    }
}
