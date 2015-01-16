import pwr.om.battlesystem.Battleground;
import pwr.om.battlesystem.Store;
import pwr.om.battlesystem.actor.Actor;
import pwr.om.battlesystem.actor.Hero;
import pwr.om.battlesystem.ai.RandomAI;


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
        int id = 0;
        Actor actor2 = new Hero(id++, 50, 50, 50, 500, 300);
//        Actor actor1 = new Hero(id++, 50, 50, 50, 500, 1000);
        Actor actor1 = new Hero(id++, Store.getInsctance().getBloodLine(1), Store.getInsctance().getProfession(1), 20);
        actor1.setAI(new RandomAI());
        actor2.setAI(new RandomAI());
        Battleground battleground = new Battleground(1000, 20);
        Actor winner = battleground.determineWinner(actor1, actor2);
        if (winner != null) {
            System.out.println("winner: " + winner.getId());
        } else {
            System.out.println("We have a draw");
        }
    }
}
