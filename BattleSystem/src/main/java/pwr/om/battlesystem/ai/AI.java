
package pwr.om.battlesystem.ai;

import pwr.om.battlesystem.actor.actions.Action;
import pwr.om.battlesystem.actor.Actor;

/**
 *
 * @author KonradOliwer
 */
public interface AI {
    public Action selectAction(Actor self, Actor enemy);
}
