package antiTorpedoCombatSystem.OM;

import antiTorpedoCombatSystem.portType.sonarInfo;
import devs.core.ObjectModelBase;
import lombok.Data;

import java.util.HashMap;

@Data
public class OM_ENV extends ObjectModelBase {

    private HashMap<String,sonarInfo> out_sonarInfoMap;

    public OM_ENV(){
        this.out_sonarInfoMap = new HashMap<>();
    }

    public void updateEnviroment(sonarInfo _sonarInfo) {
        if(_sonarInfo.getSenderId() == null || _sonarInfo.getName().equals("default"))
            return;
        if(this.out_sonarInfoMap.size()>0){
            if(this.out_sonarInfoMap.containsKey(_sonarInfo.getName())){
                this.out_sonarInfoMap.replace(_sonarInfo.getName(),_sonarInfo);
            }else{
                this.out_sonarInfoMap.put(_sonarInfo.getName(),_sonarInfo);
            }
        }
        else{
            this.out_sonarInfoMap.put(_sonarInfo.getName(),_sonarInfo);
        }
    }

}
