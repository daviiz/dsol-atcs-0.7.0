package asw_0_8_01.om;

import combatSysModel.portType.env_info;
import combatSysModel.portType.target_info;
import combatSysModel.portType.threat_info;
import devs.core.ObjectModelBase;
import lombok.Data;

@Data
public class PlatformControllerUpdaterOm extends ObjectModelBase {

    private threat_info threat_info;
    private env_info env_info;
    private target_info target_info;

    public PlatformControllerUpdaterOm(){
        threat_info = new threat_info();
        env_info = new env_info();
        target_info = new target_info();

    }


    public void identification(){
        this.target_info.setEnv_info(threat_info.getEnv_info());
    }

    public combatSysModel.portType.threat_info getThreat_info() {
        return threat_info;
    }

    public void setThreat_info(combatSysModel.portType.threat_info threat_info) {
        this.threat_info = threat_info;
    }

    public combatSysModel.portType.env_info getEnv_info() {
        return env_info;
    }

    public void setEnv_info(combatSysModel.portType.env_info env_info) {
        this.env_info = env_info;
    }

    public combatSysModel.portType.target_info getTarget_info() {
        return target_info;
    }

    public void setTarget_info(combatSysModel.portType.target_info target_info) {
        this.target_info = target_info;
    }
}
