/*
 * Copyright 2015 KonradOliwer.
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
package pwr.om.geneticai.geneticalgorithm.fitensfunction;

import java.util.Random;
import pwr.om.battlesystem.Store;
import pwr.om.battlesystem.actor.Actor;
import pwr.om.battlesystem.actor.Hero;

/**
 *
 * @author KonradOliwer
 */
public class RandomActorsFactory {

    private static final Random random = new Random();

    public static Actor[] generateActors(int number) {
        Actor[] result = new Actor[number];
        for (int i = 0; i < number; i++) {
            result[i] = new Hero(-1, Store.getInsctance().getBloodLine(random.nextInt(4)),
                    Store.getInsctance().getProfession(random.nextInt(4)), random.nextInt(200));
        }
        return result;
    }
}
