package pwr.om.battlesystem.ai;

import java.io.Serializable;
import pwr.om.battlesystem.actor.actions.Action;
import pwr.om.battlesystem.actor.Actor;

/**
 *
 * @author KonradOliwer
 */
public interface AI extends Serializable {

    public Action selectAction(Actor self, Actor enemy);
}
