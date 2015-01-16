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
package pwr.om.geneticai.geneticalgorithm.crossover;

import java.util.Random;

/**
 *
 * @author KonradOliwer
 */
public class RandomGenesCrossover extends BaseGenesCrossover {

    private final Random random = new Random();

    @Override
    protected int[] crossover(int[] chromosome1, int[] chromosome2) {
        int[] childChromosome = new int[chromosome1.length];
        for (int i = 0; i < childChromosome.length; i++) {
            childChromosome[i] = (random.nextBoolean() ? chromosome1[i] : chromosome2[i]);
        }
        return childChromosome;
    }

}
