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
package pwr.om.battlesystem.actor.actions;

import java.io.Serializable;
import pwr.om.battlesystem.actor.Actor;

/**
 *
 * @author KonradOliwer
 */
public abstract class Action implements Serializable {

    protected static int maxId = -1;
    protected final int id;
    protected final int cost;
    protected final int cooldown;
    protected int whenReady;

    protected Action(int id, int cost, int cooldown) {
        this.id = id;
        this.cost = cost;
        this.cooldown = cooldown;
    }

    protected abstract void effect(Actor user, Actor enemy);

    protected abstract boolean stateRequiramentsMet(Actor user, Actor enemy);

    public boolean canPerforme(Actor user, Actor enemy) {
        return user.getState().ap >= cost && whenReady == 0 && stateRequiramentsMet(user, enemy);
    }

    public static int getMaxId() {
        return maxId;
    }

    public int getWhenReady() {
        return whenReady;
    }

    public int performe(Actor user, Actor enemy) {
        effect(user, enemy);
        whenReady = cooldown;
        return id;
    }

    public void finishRound() {
        if (whenReady > 0) {
            whenReady--;
        }
    }

    public int getCost() {
        return cost;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getId() {
        return id;
    }
}
