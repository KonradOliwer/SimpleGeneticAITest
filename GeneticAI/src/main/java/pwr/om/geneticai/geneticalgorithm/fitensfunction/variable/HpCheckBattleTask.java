
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
package pwr.om.geneticai.geneticalgorithm.fitensfunction.variable;

import java.util.concurrent.Callable;
import pwr.om.battlesystem.Battleground;
import pwr.om.battlesystem.actor.Actor;
import pwr.om.geneticai.chromosome.ChromosomeFactory;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.BattleTask;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.BattleTasksFactory;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.EnemyFactory;

/**
 *
 * @author KonradOliwer
 */
public class HpCheckBattleTask extends BattleTask {

    HpCheckBattleTask(Battleground battleground, int populationStartIndex, int repeats, Actor[] actorsTested, Actor[] enemis, int size) {
        super(battleground, populationStartIndex, repeats, actorsTested, enemis, size);
    }

    @Override
    public int calculateResult(int selfIndex, int round) {
        battleground.determineWinner(actorsTested[selfIndex], selectEnemy(selfIndex, round));
        Actor self = actorsTested[selfIndex];
        return (int) (100 * ((double) (self.getState().hp < 0 ? 0 : self.getState().hp)) / self.getStatistics().getHp());
    }

    public static class Factory extends BattleTasksFactory {

        public Factory(Battleground battleground, int repeats, EnemyFactory enemyFactory, ChromosomeFactory chromosomeFactory) {
            super(battleground, repeats, enemyFactory, chromosomeFactory);
        }

        @Override
        protected Callable getBattleTask(Actor[] actorsTested, Actor[] enemis, int populationStartIndex, int size) {
            return new HpCheckBattleTask(this.battleground, populationStartIndex, this.repeats, actorsTested, enemis, size);
        }

    }
}
