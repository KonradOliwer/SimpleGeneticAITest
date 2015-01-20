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
package pwr.om.battlesystem.actor;

import java.io.Serializable;

/**
 *
 * @author KonradOliwer
 */
public class Statistics implements Cloneable, Serializable {

    protected int agility;
    protected int inteligence;
    protected int strength;
    protected int movmentSpeed;
    protected int hp;
    protected int ap;

    public int getAgility() {
        return agility;
    }

    public int getInteligence() {
        return inteligence;
    }

    public int getStrength() {
        return strength;
    }

    public int getMovmentSpeed() {
        return movmentSpeed;
    }

    public int getHp() {
        return hp;
    }

    public int getAp() {
        return ap;
    }

    //For test purpose---------------------------------------------------------
    public void setAgility(int agility) {
        this.agility = agility;
    }
    public void setInteligence(int inteligence) {
        this.inteligence = inteligence;
    }
    public void setStrength(int strength) {
        this.strength = strength;
    }
    public void setMovmentSpeed(int movmentSpeed) {
        this.movmentSpeed = movmentSpeed;
    }
    public void setHp(int hp) {
        this.hp = hp;
    }
    public void setAp(int ap) {
        this.ap = ap;
    }
    //---------------------------------------------------------For test purpose
}
