package combatSysModel.OM;

import devs.core.ObjectModelBase;
import combatSysModel.portType.scen_info;
import combatSysModel.portType.target_info;
import combatSysModel.portType.threat_info;

public class Weapon_Controller_updater_om  extends ObjectModelBase {

    private threat_info threat_info;
    private scen_info scen_info;
    private target_info target_info;


    public void identification(){
        this.target_info.setEnv_info(threat_info.getEnv_info());
    }


    public combatSysModel.portType.threat_info getThreat_info() {
        return threat_info;
    }

    public void setThreat_info(combatSysModel.portType.threat_info threat_info) {
        this.threat_info = threat_info;
    }

    public combatSysModel.portType.scen_info getScen_info() {
        return scen_info;
    }

    public void setScen_info(combatSysModel.portType.scen_info scen_info) {
        this.scen_info = scen_info;
    }

    public combatSysModel.portType.target_info getTarget_info() {
        return target_info;
    }

    public void setTarget_info(combatSysModel.portType.target_info target_info) {
        this.target_info = target_info;
    }
}
