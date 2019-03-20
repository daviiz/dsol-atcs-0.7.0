package asw_0_8_01.om;

import combatSysModel.portType.cmd_info;
import combatSysModel.portType.move_cmd;
import devs.core.ObjectModelBase;
import lombok.Data;

@Data
public class PlatformManeuverUpdaterOm extends ObjectModelBase {

    private move_cmd move_cmd;
    private combatSysModel.portType.cmd_info cmd_info;

    public PlatformManeuverUpdaterOm(){
        move_cmd = new move_cmd();
        cmd_info = new cmd_info();
    }

    public void cmd_Interpreter(){
        cmd_info.cmd = move_cmd.cmd;
    }

    public combatSysModel.portType.move_cmd getMove_cmd() { return move_cmd; }

    public void setMove_cmd(combatSysModel.portType.move_cmd move_cmd) {
        this.move_cmd = move_cmd;
    }

    public combatSysModel.portType.cmd_info getCmd_info() {
        return cmd_info;
    }

    public void setCmd_info(combatSysModel.portType.cmd_info cmd_info) {
        this.cmd_info = cmd_info;
    }
}
