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

/**
 *
 * @author KonradOliwer
 */
public interface ChromosomeValidatior {

    /**
     *
     * @param chromosome
     * @param gene - index in chromosome
     * @return - true if given gean is valid
     */
    public boolean isValid(int[] chromosome, int gene);

    public boolean isValid(int[] chromosome);
}