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
package pwr.om.geneticai.geneticalgorithm.crossover;

import pwr.om.geneticalgorithm.integer.CrossoverMethod;

/**
 *
 * @author KonradOliwer
 */
public abstract class BaseGenesCrossover implements CrossoverMethod{
    private int[][] populationCoppy;
    

    @Override
    public void performe(int[][] population, int[] matesIndexes) {
        if (populationCoppy == null){
            populationCoppy = new int[population.length][];
        }
        System.arraycopy(population, 0, populationCoppy, 0, populationCoppy.length);
        int j = 0;
        for (int i = 0; i < population.length; i++) {
            population[i] = crossover(populationCoppy[matesIndexes[j++]], populationCoppy[matesIndexes[j++]]);
        }
    }

    protected abstract int[] crossover(int[] chromosome1, int[] chromosome2);
}
