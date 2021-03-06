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
package pwr.om.geneticai.chromosome;

import pwr.om.battlesystem.actor.Actor;
import pwr.om.geneticalgorithm.integer.ChromosomeValidatior;

/**
 *
 * @author KonradOliwer
 */
public abstract class ChromosomeFactory implements ChromosomeValidatior {

    public abstract Chromosome createChromosome(Actor self);

    public abstract int[][] generatePopulation(Actor actor, int size);

    @Override
    public boolean isValid(int[] chromosome) {
        for (int i = 0; i < chromosome.length; i++) {
            if (!isValid(chromosome, i)) {
                return false;
            }
        }
        return true;
    }
}
