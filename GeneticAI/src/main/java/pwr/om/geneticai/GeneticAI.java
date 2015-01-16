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
package pwr.om.geneticai;

import java.io.Serializable;
import pwr.om.battlesystem.actor.Actor;
import pwr.om.battlesystem.actor.actions.Action;
import pwr.om.battlesystem.ai.AI;
import pwr.om.geneticai.chromosome.Chromosome;

/**
 *
 * @author KonradOliwer
 */
public class GeneticAI implements AI, Serializable {

    private Chromosome chromosome;

    public GeneticAI() {
    }

    public GeneticAI(Chromosome chromosome) {
        super();
        this.chromosome = chromosome;
    }

    public void setChromosome(Chromosome chromosome) {
        this.chromosome = chromosome;
    }

    public Chromosome getChromosome() {
        return chromosome;
    }

    @Override
    public Action selectAction(Actor self, Actor enemy) {
        if (self.getActions().size() > 0) {
            Action best = self.getActions().get(0);
            Action current;
            int bestPriority = -1;
            int currentPriority;
            for (int i = 0; i < self.getActions().size(); i++) {
                current = self.getActions().get(i);
                if (current.canPerforme(self, enemy)) {
                    currentPriority = chromosome.getActionPriority(self, enemy, current.getId());
                    if (currentPriority > bestPriority) {
                        bestPriority = currentPriority;
                        best = current;
                    }
                }
            }
            return best;
        }
        throw new NullPointerException("Actor can't take wait action");
    }
}
