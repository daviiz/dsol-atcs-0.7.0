package asw_0_8_01.om;

import asw.soa.util.SimUtil;
import combatSysModel.portType.*;
import devs.core.ObjectModelBase;
import lombok.Data;
import nl.tudelft.simulation.language.d3.CartesianPoint;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Data
public class Weapon_sensor_actor_om extends ObjectModelBase {
    private engage_result in_engage_result;
    private env_info in_env_info;
    private scen_info in_scen_info;
    private response in_response;

    private threat_info out_threat_info;
    private request out_request;

    private Map<String,env_info> env_infoMap = new HashMap<String,env_info>();


    public Weapon_sensor_actor_om(){
        in_engage_result = new engage_result();
        in_env_info = new env_info();
        in_scen_info = new scen_info();
        in_response = new response();

        out_threat_info = new threat_info();
        out_request = new request();
    }

    /**
     * update out threat_info
     */
    public void detection_algrithm(){
        out_threat_info.setEnv_info(getNearestEnemy(in_response.location));
    }

    private env_info getNearestEnemy(CartesianPoint location){
        env_info target = new env_info();
        if (env_infoMap.size()>0) {
            double distance = Double.POSITIVE_INFINITY;

            Iterator iter = env_infoMap.entrySet().iterator();
            while(iter.hasNext()) {
                Map.Entry entry = (Map.Entry)iter.next();
                // 获取key
                String key = (String)entry.getKey();
                // 获取value
                env_info value = (env_info)entry.getValue();

                if(in_scen_info.camp != value.camp){
                    double tmp = SimUtil.calcLength(location.x,location.y,value.location.x,value.location.y);
                    if(tmp < distance){
                        distance = tmp;
                        target = value;
                    }
                }
            }
        }
        return target;
    }


    public engage_result getIn_engage_result() {
        return in_engage_result;
    }

    public void setIn_engage_result(engage_result in_engage_result) {
        this.in_engage_result = in_engage_result;
    }

    public env_info getIn_env_info() {
        return in_env_info;
    }

    public void setIn_env_info(env_info in_env_info) {
        this.in_env_info = in_env_info;

        if(env_infoMap.containsKey(in_env_info.getSenderId())){
            env_infoMap.remove(in_env_info.getSenderId());
            env_infoMap.put(in_env_info.getSenderId(),in_env_info);
        }else{
            env_infoMap.put(in_env_info.getSenderId(),in_env_info);
        }
    }

    public scen_info getIn_scen_info() {
        return in_scen_info;
    }

    public void setIn_scen_info(scen_info in_scen_info) {
        this.in_scen_info = in_scen_info;
    }

    public response getIn_response() {
        return in_response;
    }

    public void setIn_response(response in_response) {
        this.in_response = in_response;
    }

    public threat_info getOut_threat_info() {
        return out_threat_info;
    }

    public void setOut_threat_info(threat_info out_threat_info) {
        this.out_threat_info = out_threat_info;
    }

    public request getOut_request() {
        return out_request;
    }

    public void setOut_request(request out_request) {
        this.out_request = out_request;
    }
}
