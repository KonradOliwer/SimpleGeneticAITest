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
package pwr.om.geneticai.geneticalgorithm;

import java.util.Arrays;
import java.util.Random;
import pwr.om.geneticalgorithm.integer.SelectionFunction;

/**
 *
 * @author KonradOliwer
 */
public class RouletteWheelSelectionFunction implements SelectionFunction {

    private final Random random = new Random();
    private IndexedValue[] partsArray;

    @Override
    public void evaluate(int[][] population, int[] populationFitnesFunctionValuse, int[] resultIndexes) {
        generatePartsArray(population, populationFitnesFunctionValuse);
        fillResultIndexes(resultIndexes);
    }

    private void generatePartsArray(int[][] population, int[] populationFitnesFunctionValuse) {
        if (partsArray == null) {
            partsArray = new IndexedValue[population.length];
            for (int i = 0; i < partsArray.length; i++) {
                partsArray[i] = new IndexedValue();
            }
        }
        for (int i = 0; i < populationFitnesFunctionValuse.length; i++) {
            partsArray[i].index = i;
            partsArray[i].value = populationFitnesFunctionValuse[i];
        }
        Arrays.sort(partsArray);

        for (int i = 1; i < partsArray.length; i++) {
            partsArray[i].value = partsArray[i].value + partsArray[i - 1].value;
        }
    }

    private void fillResultIndexes(int[] resultIndexes) {
        try {
            int bound = partsArray[partsArray.length - 1].value + 1;
            for (int i = 0; i < resultIndexes.length; i++) {
                int value = random.nextInt(bound);
                for (IndexedValue entity : partsArray) {
                    // takes adventage that partsArray is sorted by values
                    if (value < entity.value) {
                        resultIndexes[i] = entity.index;
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
