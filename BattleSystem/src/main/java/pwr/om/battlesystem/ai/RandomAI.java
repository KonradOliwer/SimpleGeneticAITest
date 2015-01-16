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
package pwr.om.battlesystem.ai;

import java.util.Random;
import pwr.om.battlesystem.actor.Actor;
import pwr.om.battlesystem.actor.actions.Action;

/**
 *
 * @author KonradOliwer
 */
public class RandomAI implements AI {

    private final Random random = new Random();

    @Override
    public Action selectAction(Actor self, Actor enemy) {
        return self.getActions().get(random.nextInt(self.getActions().size()));
    }

}
