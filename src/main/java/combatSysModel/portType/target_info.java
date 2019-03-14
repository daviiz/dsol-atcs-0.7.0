package combatSysModel.portType;

import devs.core.PortTypeBase;

public class target_info   extends PortTypeBase {
    private env_info info;

    public void setEnv_info(env_info _info){
        this.info = _info;
    }
}
