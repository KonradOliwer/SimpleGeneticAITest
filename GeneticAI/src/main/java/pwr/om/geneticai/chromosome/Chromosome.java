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

import java.io.Serializable;
import pwr.om.battlesystem.actor.Actor;

/**
 *
 * @author KonradOliwer
 */
public abstract class Chromosome implements Cloneable, Serializable {

    protected int[] actionIds;
    protected int[] chromosome;

    protected Chromosome(Actor self, int indexPerAction) {
        actionIds = new int[self.getActions().size()];
        for (int i = 0; i < self.getActions().size(); i++) {
            actionIds[i] = self.getActions().get(i).getId();
        }
        chromosome = new int[actionIds.length * indexPerAction];
    }

     private Chromosome() {}

    public void copyArrayReprezentation(int[] arrayReprezentation) {
        System.arraycopy(arrayReprezentation, 0, chromosome, 0, arrayReprezentation.length);
    }

    public int[] getArrayReprezentation() {
        return chromosome;
    }

    public abstract int getActionPriority(Actor self, Actor enemy, int actionId);

    @Override
    public Chromosome clone() throws CloneNotSupportedException {
        Chromosome clone = (Chromosome) super.clone();
        clone.actionIds = actionIds.clone();
        clone.chromosome = chromosome.clone();
        return clone;
    }
}
