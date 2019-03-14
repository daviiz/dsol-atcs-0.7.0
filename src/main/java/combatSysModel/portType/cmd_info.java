package combatSysModel.portType;

import devs.core.PortTypeBase;

public class cmd_info extends PortTypeBase {
    public COMMAND cmd;
    public cmd_info(){
        cmd = COMMAND.DEFAULT;
    }
}
