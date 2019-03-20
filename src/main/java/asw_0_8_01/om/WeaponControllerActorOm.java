package asw_0_8_01.om;

import combatSysModel.portType.*;
import devs.core.ObjectModelBase;
import lombok.Data;

@Data
public class WeaponControllerActorOm extends ObjectModelBase {

    private move_finished move_finished;
    private engage_result engage_result;
    private scen_info scen_info;
    private wp_guidance wp_guidance;
    private target_info target_info;

    private move_cmd move_cmd;

    public void tactical_search(){

    }

    public void apprch(){

    }

    public combatSysModel.portType.move_finished getMove_finished() {
        return move_finished;
    }

    public void setMove_finished(combatSysModel.portType.move_finished move_finished) {
        this.move_finished = move_finished;
    }

    public combatSysModel.portType.engage_result getEngage_result() {
        return engage_result;
    }

    public void setEngage_result(combatSysModel.portType.engage_result engage_result) {
        this.engage_result = engage_result;
    }

    public combatSysModel.portType.scen_info getScen_info() {
        return scen_info;
    }

    public void setScen_info(combatSysModel.portType.scen_info scen_info) {
        this.scen_info = scen_info;
    }

    public combatSysModel.portType.wp_guidance getWp_guidance() {
        return wp_guidance;
    }

    public void setWp_guidance(combatSysModel.portType.wp_guidance wp_guidance) {
        this.wp_guidance = wp_guidance;
    }

    public combatSysModel.portType.target_info getTarget_info() {
        return target_info;
    }

    public void setTarget_info(combatSysModel.portType.target_info target_info) {
        this.target_info = target_info;
    }

    public combatSysModel.portType.move_cmd getMove_cmd() {
        return move_cmd;
    }

    public void setMove_cmd(combatSysModel.portType.move_cmd move_cmd) {
        this.move_cmd = move_cmd;
    }
}
