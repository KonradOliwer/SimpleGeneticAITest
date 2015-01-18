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
package pwr.om.geneticai.geneticalgorithm.fitensfunction;

import pwr.om.battlesystem.actor.Actor;
import pwr.om.geneticai.chromosome.ChromosomeFactory;

/**
 *
 * @author KonradOliwer
 */
public interface EnemyFactory {
    public Actor[][] getEnemies(int[][] population, int[] distribution, Actor testedActor);
    public ChromosomeFactory getChromosomeFactory();
}
