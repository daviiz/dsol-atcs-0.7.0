package antiTorpedoCombatSystem.OM;

import antiTorpedoCombatSystem.portType.detect;
import antiTorpedoCombatSystem.portType.ownPos;
import antiTorpedoCombatSystem.portType.simCtrl;
import antiTorpedoCombatSystem.portType.sonarInfo;
import asw.soa.util.SimUtil;
import devs.core.ObjectModelBase;
import lombok.Data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Data
public class OM_SENSOR extends ObjectModelBase {

    /**
     * AM_Transmitter's port types:
     */
    private ownPos in_ownPos;
    private sonarInfo out_sonarInfo;

    private double tTransmit = 3.0;

    /**
     * AM_Receiver's port types:
     */
    private sonarInfo in_sonarInfo;
    private simCtrl in_simCtrl;
    private detect out_detect;

    private sonarInfo selfInfo;
    private HashMap<String,sonarInfo> detectDataMap;

    private double tDetect = 5.0;

    public OM_SENSOR(){
        in_ownPos = new ownPos();
        out_sonarInfo = new sonarInfo();

        in_sonarInfo = new sonarInfo();
        in_simCtrl = new simCtrl();
        out_detect = new detect();
        selfInfo = new sonarInfo();
        detectDataMap = new HashMap<>();
    }
    public void updateTransmitterSonarInfo(){
        if(in_ownPos.isActive()) {
            out_sonarInfo.setCmp(in_ownPos.getCmp());
            out_sonarInfo.setLive(in_ownPos.isLive());
            out_sonarInfo.setName(in_ownPos.getName());
            out_sonarInfo.setPosition(in_ownPos.getPosition());
            out_sonarInfo.setDetectRange(in_ownPos.getDetectRange());
        }
        //out_sonarInfo.setSelf(true);
    }

    public void updateInfo(){
        if(in_sonarInfo == null){
            return;
        }
        if(!in_sonarInfo.getSenderId().equals("null.env")){
            selfInfo = in_sonarInfo;
            return;
        }
        if(in_sonarInfo.getName().equals(selfInfo.getName()))
            return;
        if(this.detectDataMap.size()>0){
            if(this.detectDataMap.containsKey(in_sonarInfo.getName())){
                this.detectDataMap.replace(in_sonarInfo.getName(),in_sonarInfo);
            }
        }else{
            this.detectDataMap.put(in_sonarInfo.getName(),in_sonarInfo);
        }
    }

    public void targetDetection(){
        if (this.detectDataMap.size()>0) {
            double minDistance = selfInfo.getDetectRange();
            Iterator iter = detectDataMap.entrySet().iterator();
            while(iter.hasNext()) {
                Map.Entry entry = (Map.Entry)iter.next();
                // 获取key
                String key = (String)entry.getKey();
                // 获取value
                sonarInfo value = (sonarInfo)entry.getValue();
                if(value != null && selfInfo.getName() !=null &&  !selfInfo.getName().equals(value.getName()) && selfInfo.getCmp()!= value.getCmp() ){
                    double tmpDistance = SimUtil.calcLength(selfInfo.getPosition().x,selfInfo.getPosition().y,
                            value.getPosition().x,value.getPosition().y);
                    if(tmpDistance < minDistance ){
                        minDistance = tmpDistance;
                        setOut_detect(value);
                    }

                }
            }
        }
    }
    private void setOut_detect(sonarInfo s){
        this.out_detect.setCmp(s.getCmp());
        this.out_detect.setLive(s.isLive());
        this.out_detect.setName(s.getName());
        this.out_detect.setPosition(s.getPosition());
    }
}
