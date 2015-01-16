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
package pwr.om.geneticai.geneticalgorithm.fitensfunction.old;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import pwr.om.battlesystem.Battleground;
import pwr.om.battlesystem.actor.Actor;
import pwr.om.battlesystem.ai.AI;
import pwr.om.battlesystem.ai.EnchantedRandomAI;
import pwr.om.geneticai.GeneticAI;
import pwr.om.geneticai.chromosome.Chromosome;

/**
 *
 * @author KonradOliwer
 */
public class OldBattleManager {
    private final int size;
    private final Actor testedUnit;
    private final Actor[] enemies;
    private final int copyStart;
    private final Battleground battleground;
    private final int battleRepeats;
    private final Chromosome[] processingData;
    private final int[] results;
    private final GeneticAI selfAI;
    private final OldBattleFitnesFuntion parent;
    private int enemyIndex = 0;

    OldBattleManager(OldBattleFitnesFuntion parent, Actor testedUnit, Actor[] enemies, int copyStart, int size) {
        this.size = size;
        this.parent = parent;
        this.testedUnit = testedUnit;
        this.enemies = enemies;
        this.copyStart = copyStart;
        battleground = parent.getBattleground();
        battleRepeats = parent.getBattleRepeats();
        processingData = new Chromosome[size];
        results = new int[size];

        AI enemyAI = new EnchantedRandomAI();
        for (int i = 0; i < enemies.length; i++) {
            this.enemies[i].setAI(enemyAI);
        }

        selfAI = new GeneticAI();
        testedUnit.setAI(selfAI);
    }

    private void nextEnemyIndex() {
        if (enemyIndex++ == enemies.length - 1) {
            enemyIndex = 0;
        }
    }

    public FutureTask<int[]> getBattleTask() {
        return new FutureTask(new BattleTask());
    }

    void setProcessingData(Chromosome[] global) {
        System.arraycopy(global, copyStart, processingData, 0, size);
    }

    public class BattleTask implements Callable<int[]> {

        @Override
        public int[] call() throws Exception {
            Arrays.fill(results, 1);
            for (int i = 0; i < battleRepeats; i++) {
                for (int j = 0; j < processingData.length; j++) {
                    selfAI.setChromosome(processingData[j]);
                    Actor result = battleground.determineWinner(testedUnit, enemies[enemyIndex], true);
                    results[j] += result == null ? parent.getDrawScor() : (result.getId() == testedUnit.getId() ? parent.getWinScore()
                            : parent.getLoseScore());
                    nextEnemyIndex();
                }
            }
            return results;
        }
    }

    public int getSize() {
        return size;
    }
}
