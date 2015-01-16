package pwr.om.battlesystem.actor;

import java.io.Serializable;
import java.util.ArrayList;
import pwr.om.battlesystem.ai.AI;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import pwr.om.battlesystem.actor.actions.Action;
import pwr.om.battlesystem.actor.actions.WaitAction;

/**
 *
 * @author KonradOliwer
 */
public class Actor implements Cloneable, Serializable {

    public static final int MAX_ATTRIBUTE_VALUE = 100;
    public static final int MAX_MOVMENT_SPEED_VALUE = 500;
    public static final int MAX_HP_VALUE = 1000;
    public static final int MAX_AP_VALUE = 500;
    protected final int id;
    protected AI ai;
    protected final Statistics statistics = new Statistics();
    protected final State state = new State();
    List<Action> actions = new ArrayList<>();

    public Actor(int id) {
        this.id = id;
        actions.add(new WaitAction());
    }

    @Override
    public Actor clone() throws CloneNotSupportedException {
        super.clone();
        Actor clone = new Actor(id);
        clone.statistics.agility = statistics.agility;
        clone.statistics.inteligence = statistics.inteligence;
        clone.statistics.strength = statistics.strength;
        clone.statistics.movmentSpeed = statistics.movmentSpeed;
        clone.statistics.hp = statistics.hp;
        clone.statistics.ap = statistics.ap;
        clone.actions.clear();
        try {
            for (Action action : actions) {
                clone.actions.add(action.getClass().newInstance());
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Actor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clone;
    }

    public void prepairForBattle() {
        state.ap = statistics.ap;
        state.hp = statistics.hp;
        state.reflectEndsIn = 0;
        state.regenerationEndsIn = 0;
        state.strengtheningArmorEndsIn = 0;
    }

    public int getId() {
        return id;
    }

    public void setAI(AI ai) {
        this.ai = ai;
    }

    public int takeAction(Actor enemy) {
        if (!isWaiting()) {
            try{
                Action action = selectAction(enemy);
            return action.performe(this, enemy);
            }catch(Exception ex){
                Action action = selectAction(enemy);
                ex.printStackTrace();
                return WaitAction.ACTION_ID;
            }
        }else{
            return WaitAction.ACTION_ID;
        }
    }

    protected Action selectAction(Actor enemy) {
        Action wait = null;
        for (Action action : actions) {
            if (action.getId() != WaitAction.ACTION_ID) {
                if (action.canPerforme(this, enemy)) {
                    return ai.selectAction(this, enemy);
                }
            } else {
                wait = action;
            }
        }
        return wait;
    }

    public boolean isAlife() {
        return state.hp > 0;
    }

    public boolean isWaiting() {
        return state.waiting;
    }

    public void setStartDistance(int startDistance) {
        state.distance = startDistance;
    }

    public int getCurrentAp() {
        return state.ap;
    }

    public void finishRound() {
        if (isAlife() && state.regenerationEndsIn > 0) {
            if (state.hp + statistics.inteligence > statistics.hp) {
                state.hp = statistics.hp;
            } else {
                state.hp += statistics.inteligence;
            }
        }

        state.reflectEndsIn -= state.reflectEndsIn > 0 ? 1 : 0;
        state.regenerationEndsIn -= state.regenerationEndsIn > 0 ? 1 : 0;
        state.strengtheningArmorEndsIn -= state.strengtheningArmorEndsIn > 0 ? 1 : 0;
        state.ap += statistics.ap;
        state.waiting = false;
        for (Action action : actions) {
            action.finishRound();
        }
    }

    public State getState() {
        return state;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public List<Action> getActions() {
        return actions;
    }

    int statisticIncrease(Random random, int current, int increaseRange, int max) {
        current += random.nextInt(increaseRange);
        if (current > max) {
            current = max;
        }
        return current;
    }

    boolean[] ownedAbilities() {
        boolean[] pa = new boolean[Action.getMaxId() + 1];
        for (Action a : actions) {
            pa[a.getId()] = true;
        }
        return pa;
    }

    public AI getAi() {
        return ai;
    }

    public void setAi(AI ai) {
        this.ai = ai;
    }
}
