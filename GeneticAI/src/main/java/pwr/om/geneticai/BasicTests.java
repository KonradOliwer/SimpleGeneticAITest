package pwr.om.geneticai;

import java.util.logging.Level;
import java.util.logging.Logger;
import pwr.om.battlesystem.Battleground;
import pwr.om.battlesystem.actor.Actor;
import pwr.om.battlesystem.ai.AI;
import pwr.om.battlesystem.ai.EnchantedRandomAI;
import pwr.om.geneticai.chromosome.Chromosome;
import pwr.om.geneticai.chromosome.ChromosomeFactory;
import pwr.om.geneticai.geneticalgorithm.IterationBasedStopFunction;
import pwr.om.geneticai.geneticalgorithm.RouletteWheelSelectionFunction;
import pwr.om.geneticai.geneticalgorithm.crossover.RandomGenesCrossover;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.BattleFitnesFuntion;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.BattleTasksFactory;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.EnemyFactory;
import pwr.om.geneticai.geneticalgorithm.mutation.DoubleVMutation;
import pwr.om.geneticalgorithm.integer.FitnesFuntion;
import pwr.om.geneticalgorithm.integer.FixedPopulationSizeGeneticAlgorithm;
import pwr.om.geneticalgorithm.integer.Mutation;

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
/**
 *
 * @author KonradOliwer
 */
public class BasicTests {

    private final int iterations;
    private final int populationSize;

    public BasicTests(int iterations, int populationSize) {
        this.iterations = iterations;
        this.populationSize = populationSize;
    }

    public Actor generateActorWithAI(BattleTasksFactory battleTaskFactory, ChromosomeFactory chromosomeFactory, EnemyFactory enemyFactory) {
        return generateActorWithAI(battleTaskFactory, chromosomeFactory, enemyFactory, 50);
    }

    public Actor generateActorWithAI(BattleTasksFactory battleTaskFactory, ChromosomeFactory chromosomeFactory, EnemyFactory enemyFactory,
            int testActorsIndex) {
        BattleFitnesFuntion fitnesFunction;
        Mutation mutation;
        double mutationChancePerGene;
        Actor testActor;
        Actor[] testActors = Data.getSavedActor();

        mutationChancePerGene = 0.01;
        mutation = new DoubleVMutation(mutationChancePerGene);
        testActor = testActors[testActorsIndex];

        battleTaskFactory.setTestedActor(testActor);
        fitnesFunction = new BattleFitnesFuntion(battleTaskFactory);
        FixedPopulationSizeGeneticAlgorithm algorith = FixedPopulationSizeGeneticAlgorithm.Builder.init()
                .crossoverMethod(new RandomGenesCrossover())
                .fitnesFuntion(fitnesFunction)
                .mutation(mutation)
                .selectionFunction(new RouletteWheelSelectionFunction())
                .stopFunction(new IterationBasedStopFunction(iterations))
                .population(chromosomeFactory.generatePopulation(testActor, populationSize))
                .build();
        algorith.run();
        fitnesFunction.clean();

        try {
            Actor result = testActor.clone();
            Chromosome chromosome = chromosomeFactory.createChromosome(testActor);
            chromosome.copyArrayReprezentation(algorith.getBest());
            result.setAI(new GeneticAI(chromosome));

            return result;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(BasicTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public double selfRandomAITest(Battleground battleground, Actor self) {
        return selfRandomAITest(battleground, self, 100);
    }

    public double selfRandomAITest(Battleground battleground, Actor self, int repeat) {
        try {
            double wins = 0;
            Actor enemy = self.clone();
            enemy.setAI(new EnchantedRandomAI());
            for (int i = 0; i < repeat; i++) {
                wins += self == battleground.determineWinner(self, enemy, true) ? 1 : 0;
            }
            return wins / repeat;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(BasicTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Double.NaN;
    }

    public double randomAIFullTest(Battleground battleground, Actor self) {
        return randomAIFullTest(battleground, self, 10);
    }

    public double randomAIFullTest(Battleground battleground, Actor self, int repeat) {
        double wins = 0;
        Actor[] actors = Data.getSavedActor();
        AI ai = new EnchantedRandomAI();
        for (Actor enemy : actors) {
            enemy.setAI(ai);
            for (int i = 0; i < repeat; i++) {
                wins += self == battleground.determineWinner(self, enemy) ? 1 : 0;
            }
        }
        return wins / (repeat * actors.length);
    }
}
