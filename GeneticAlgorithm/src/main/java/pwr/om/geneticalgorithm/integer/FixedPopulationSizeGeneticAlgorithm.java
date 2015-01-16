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
package pwr.om.geneticalgorithm.integer;

import pwr.om.geneticalgorithm.StopFunction;

/**
 *
 * @author KonradOliwer
 */
public class FixedPopulationSizeGeneticAlgorithm {

    private final ChromosomeValidatior chromosomeValidatior;
    private final CrossoverMethod crossoverMethod;
    private final Mutation mutation;
    private final SelectionFunction selectionFunction;
    private final StopFunction stopFunction;
    private final int[][] population;
    private final int[] populationFitnesFunctionValuse;
    private final int[] matesIndexes;
    private final FitnesFuntion fitnesFuntion;

    private FixedPopulationSizeGeneticAlgorithm(Builder builder) {
        chromosomeValidatior = builder.chromosomeValidatior;
        crossoverMethod = builder.crossoverMethod;
        mutation = builder.mutation;
        selectionFunction = builder.selectionFunction;
        population = builder.population;
        stopFunction = builder.stopFunction;
        fitnesFuntion = builder.fitnesFuntion;
        populationFitnesFunctionValuse = new int[population.length];
        matesIndexes = new int[population.length * 2];
    }

    public int[][] run() {
        int interation = 1;
        while (stopFunction.isDone(interation++, populationFitnesFunctionValuse)) {
            fitnesFuntion.evaluate(population, populationFitnesFunctionValuse);
            selectionFunction.evaluate(population, populationFitnesFunctionValuse, matesIndexes);
            crossoverMethod.performe(population, matesIndexes);
            mutation.perform(chromosomeValidatior, population);
        }
        return population;
    }

    public int[] getBest() {
        int bestIndex = -1;
        int bestValue = -1;
        for (int i = 0; i < populationFitnesFunctionValuse.length; i++) {
            if (bestValue < populationFitnesFunctionValuse[i]) {
                bestValue = populationFitnesFunctionValuse[i];
                bestIndex = i;
            }
        }
        return population[bestIndex];
    }

    public static class Builder {

        private ChromosomeValidatior chromosomeValidatior;
        private CrossoverMethod crossoverMethod;
        private Mutation mutation;
        private FitnesFuntion fitnesFuntion;
        private SelectionFunction selectionFunction;
        private int[][] population;
        private StopFunction stopFunction;

        public static Builder init() {
            return new Builder();
        }

        public Builder fitnesFuntion(FitnesFuntion fitnesFuntion) {
            this.fitnesFuntion = fitnesFuntion;
            return this;
        }

        public Builder stopFunction(StopFunction stopFunction) {
            this.stopFunction = stopFunction;
            return this;
        }

        public Builder chromosomeValidatior(ChromosomeValidatior chromosomeValidatior) {
            this.chromosomeValidatior = chromosomeValidatior;
            return this;
        }

        public Builder crossoverMethod(CrossoverMethod crossoverMethod) {
            this.crossoverMethod = crossoverMethod;
            return this;
        }

        public Builder mutation(Mutation mutation) {
            this.mutation = mutation;
            return this;
        }

        public Builder selectionFunction(SelectionFunction selectionFunction) {
            this.selectionFunction = selectionFunction;
            return this;
        }

        public Builder population(int[][] population) {
            this.population = population;
            return this;
        }

        public FixedPopulationSizeGeneticAlgorithm build() {
            if (chromosomeValidatior == null) {
                chromosomeValidatior = new DummyValidationChromosome();
            }
            if (fitnesFuntion == null) {
                throw new NullPointerException("Fitnes funtion not provided");
            }
            if (crossoverMethod == null) {
                throw new NullPointerException("Crossover method not provided");
            }
            if (mutation == null) {
                throw new NullPointerException("Mutation class not provided");
            }
            if (selectionFunction == null) {
                throw new NullPointerException("Selection function chromosomeValidatior");
            }
            if (population == null || population.length == 0) {
                throw new NullPointerException("Population not provided");
            }
            if (stopFunction == null) {
                throw new NullPointerException("Algorithm stop function not provided");
            }
            return new FixedPopulationSizeGeneticAlgorithm(this);
        }
    }
}
