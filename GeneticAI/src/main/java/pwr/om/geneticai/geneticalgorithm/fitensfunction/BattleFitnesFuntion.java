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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import pwr.om.geneticalgorithm.integer.FitnesFuntion;

/**
 *
 * @author KonradOliwer
 */
public class BattleFitnesFuntion implements FitnesFuntion {

    protected final BattleTasksFactory taskFactory;
    protected final ExecutorService executorService;
    protected final int tasksNumber;
    protected int[] distribution;

    public BattleFitnesFuntion(BattleTasksFactory taskFactory) {
        this.taskFactory = taskFactory;
        tasksNumber = Runtime.getRuntime().availableProcessors() - 1;
        executorService = Executors.newFixedThreadPool(tasksNumber);
    }

    @Override
    public void evaluate(int[][] population, int[] populationFitnesFunctionValuse) {
        calculateDistribution(population);
        FutureTask<int[]>[] tasks = taskFactory.produceTasks(population, distribution);
        for (int i = 0; i < tasks.length; i++) {
            executorService.execute(tasks[i]);
        }
        int pos = 0;
        for (int i = 0; i < tasks.length; i++) {
            try {
                int[] result = tasks[i].get();
                System.arraycopy(result, 0, populationFitnesFunctionValuse, pos, distribution[i]);
                pos += distribution[i];
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(BattleFitnesFuntion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void calculateDistribution(int[][] population) {
        if (distribution == null) {
            distribution = new int[population.length];
            int minSize = population.length / tasksNumber;
            int increased = population.length % tasksNumber;
            for (int i = 0; i < tasksNumber; i++) {
                distribution[i] = i < increased ? minSize + 1 : minSize;
            }
        }
    }

    public void clean() {
        executorService.shutdownNow();
    }
}
