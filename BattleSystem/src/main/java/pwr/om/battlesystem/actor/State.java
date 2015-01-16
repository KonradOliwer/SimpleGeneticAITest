
package pwr.om.battlesystem.actor;

import java.io.Serializable;

/**
 *
 * @author KonradOliwer
 */
public class State implements Serializable {
    public static final int EFFECT_NOT_APPLYED = 0;
    public int hp;
    public int ap;
    public int regenerationEndsIn;
    public int reflectEndsIn;
    public int strengtheningArmorEndsIn;
    public int distance;
    public boolean waiting;
}
