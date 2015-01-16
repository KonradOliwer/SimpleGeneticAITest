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

import pwr.om.battlesystem.actor.Actor;

/**
 *
 * @author KonradOliwer
 */
public class HealAction extends Action {

    public static final int ACTION_ID = 2;
    public static final int ACTION_COST = 214;
    public static final int ACTION_CD = 2;

    static {
        Action.maxId = Math.max(Action.maxId, ACTION_ID);
    }

    public HealAction() {
        super(ACTION_ID, ACTION_COST, ACTION_CD);
    }

    @Override
    protected boolean stateRequiramentsMet(Actor user, Actor enemy) {
        return true;
    }

    @Override
    protected void effect(Actor user, Actor enemy) {
        int newValue = user.getState().hp + 3 * user.getStatistics().getInteligence();
        if (newValue > user.getStatistics().getHp()) {
            user.getState().hp = user.getStatistics().getHp();
        } else {
            user.getState().hp = newValue;
        }
    }
}
