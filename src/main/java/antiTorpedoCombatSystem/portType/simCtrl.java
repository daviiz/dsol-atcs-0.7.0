package antiTorpedoCombatSystem.portType;

import devs.core.PortTypeBase;
import lombok.Data;
@Data
public class simCtrl extends PortTypeBase {

    private String START = "START",PAUSE = "PAUSE",STOP = "STOP";
}
