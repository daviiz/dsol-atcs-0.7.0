package combatSysModel.portType;

import devs.core.PortTypeBase;

public class move_cmd  extends PortTypeBase {
    public COMMAND cmd;
    public move_cmd(){
        this.cmd = COMMAND.DEFAULT;
    }
}
