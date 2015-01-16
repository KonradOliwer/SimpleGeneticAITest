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
import java.util.concurrent.Callable;
import pwr.om.battlesystem.Battleground;
import pwr.om.battlesystem.actor.Actor;

/**
 *
 * @author KonradOliwer
 */
public abstract class BattleTask implements Callable<int[]> {

    protected final Random random = new Random();
    protected final int size;
    protected final Battleground battleground;
    protected final int populationStartIndex;
    protected final int repeats;
    protected final Actor[] actorsTested;
    protected final Actor[] enemis;

    protected BattleTask(Battleground battleground, int populationStartIndex, int repeats, Actor[] actorsTested, Actor[] enemis, int size) {
        this.actorsTested = actorsTested;
        this.battleground = battleground;
        this.enemis = enemis;
        this.populationStartIndex = populationStartIndex;
        this.repeats = repeats;
        this.size = size;
    }

    public Actor selectEnemy(int selfIndex, int round) {
        return enemis[random.nextInt(enemis.length)];
    }

    @Override
    public int[] call() throws Exception {
        int[] result = new int[size];
        for (int i = 0; i < repeats; i++) {
            for (int j = 0; j < result.length; j++) {
                result[j] += calculateResult(j, i);
            }
        }
        return result;
    }

    public abstract int calculateResult(int selfIndex, int round);

    public int getSize() {
        return size;
    }
}
