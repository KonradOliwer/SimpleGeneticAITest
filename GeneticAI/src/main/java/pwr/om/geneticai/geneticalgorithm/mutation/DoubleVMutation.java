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
package pwr.om.geneticai.geneticalgorithm.mutation;

import java.util.Random;
import pwr.om.geneticalgorithm.integer.ChromosomeValidatior;
import pwr.om.geneticalgorithm.integer.Mutation;

/**
 *
 * @author KonradOliwer
 */
public class DoubleVMutation implements Mutation {

    private static final int MAX_RETRY_TO_PERFORME_VALID_MUTATION = 20;
    private final Random random = new Random();
    private final int chancePerGene;
    private int randomUpperBorder;

    public DoubleVMutation(double chancePerGene) {
        if (chancePerGene > 1) {
            throw new RuntimeException("chancePerGene greater then 1");
        }
        randomUpperBorder = (int) Math.pow(10, findDecimalPlaces(chancePerGene));
        this.chancePerGene = (int) (chancePerGene * randomUpperBorder);
    }

    @SuppressWarnings("empty-statement")
    private int findDecimalPlaces(double number) {
        double fractionalPart = number - Math.round(number);
        int n = 0;
        while (fractionalPart * Math.pow(10, n++) % 10 != 0);
        return n - 2;
    }

    @Override
    public void perform(ChromosomeValidatior chromosomeValidatior, int[][] populations) {
        for (int[] chromosome : populations) {
            for (int i = 0; i < chromosome.length; i++) {
                if (chancePerGene > random.nextInt(randomUpperBorder)) {
                    int attempts = 0;
                    boolean perforemdValidMutation = false;
                    while (!perforemdValidMutation && attempts++ < MAX_RETRY_TO_PERFORME_VALID_MUTATION) {
                        perforemdValidMutation = mutate(chromosomeValidatior, chromosome, i);
                    }
                }
            }
        }
    }

    private boolean mutate(ChromosomeValidatior chromosomeValidatior, int[] chromosome, int gene) {
        chromosome[gene] += (random.nextBoolean() ? 1 : -1) * random.nextInt(1 + chromosome[gene]);
        return chromosomeValidatior.isValid(chromosome, gene);
    }
}
