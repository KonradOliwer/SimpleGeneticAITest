import java.util.Random;
import pwr.om.battlesystem.Battleground;
import pwr.om.battlesystem.actor.Actor;
import pwr.om.battlesystem.actor.Hero;
import pwr.om.battlesystem.ai.AI;
import pwr.om.battlesystem.ai.EnchantedRandomAI;
import pwr.om.geneticai.GeneticAI;
import pwr.om.geneticai.chromosome.BasicInfoChromosome;
import pwr.om.geneticai.chromosome.BasicInfoChromosomeFactory;
import pwr.om.geneticai.chromosome.BasicInformationPopulationGenerator;
import pwr.om.geneticai.geneticalgorithm.IterationBasedStopFunction;
import pwr.om.geneticai.geneticalgorithm.crossover.RandomGenesCrossover;
import pwr.om.geneticai.geneticalgorithm.RouletteWheelSelectionFunction;
import pwr.om.geneticai.geneticalgorithm.mutation.DoubleVMutation;
import pwr.om.geneticai.geneticalgorithm.fitensfunction.old.OldBattleFitnesFuntion;
import pwr.om.geneticalgorithm.integer.FixedPopulationSizeGeneticAlgorithm;

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
/**
 *
 * @author KonradOliwer
 */
public class Test {

    public static void main(String[] args) {
        int loseScore = 0;
        int drawScor = 1;
        int winScore = 100;
        int populationSize = 1000;
        double mutationChancePerGene = 0.05;
        int iterations = 1000;
        Actor testedActor = new Hero(0, 50, 50, 50, 500, 300);
        Battleground battleground = new Battleground(1000, 50);
        OldBattleFitnesFuntion battleFitnessFunction = new OldBattleFitnesFuntion(battleground, testedActor, populationSize, winScore, drawScor,
                loseScore, 
//                new ActionPriorityChromosomeFactory()
                new BasicInfoChromosomeFactory()
        );
        try {
            FixedPopulationSizeGeneticAlgorithm algorith = FixedPopulationSizeGeneticAlgorithm.Builder.init()
                    .crossoverMethod(new RandomGenesCrossover())
                    .fitnesFuntion(battleFitnessFunction)
                    .mutation(new DoubleVMutation(mutationChancePerGene))
                    .selectionFunction(new RouletteWheelSelectionFunction())
                    .stopFunction(new IterationBasedStopFunction(iterations))
                    .population(BasicInformationPopulationGenerator.generatePopulation(testedActor, populationSize))
//                    .population(ActionPriorityPopulationGenerator.generatePopulation(testedActor, populationSize))
                    .build();
            algorith.run();
            testedActor.setAI(new GeneticAI(
//                    new ActionPriorityChromosome(testedActor, algorith.getBest())
                    new BasicInfoChromosome(testedActor, algorith.getBest())
            ));
            int win = 0;
            int draw = 0;
            int lose = 0;
            AI enemyAI = new EnchantedRandomAI();
            Random random = new Random();
            for (int i = 0; i < 1000; i++) {
                Actor randomActor = new Hero(1, 50, 50, 50, 500, 300);
//            Actor randomActor = new Hero(1, Store.getInsctance().getBloodLine(random.nextInt(4)),
//                    Store.getInsctance().getProfession(random.nextInt(4)), random.nextInt(30));
                randomActor.setAI(enemyAI);
                Actor winner = battleground.determineWinner(testedActor, randomActor, true);
                if (winner == testedActor) {
                    win++;
                } else if (winner == null) {
                    draw++;
                } else {
                    lose++;
                }
            }
            System.out.println(String.format("Win: %d; Lose: %d; Draw: %d", win, lose, draw));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            battleFitnessFunction.clean();
        }
    }
}
