package asw_0_8_01.des.am;

import asw.soa.util.SimUtil;
import asw_0_8_01.om.PlatformSensorActorOm;
import asw_0_8_01.portMsgType.env_info;
import asw_0_8_01.portMsgType.move_result;
import asw_0_8_01.portMsgType.scen_info;
import asw_0_8_01.portMsgType.threat_info;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.language.d3.CartesianPoint;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PlatformSensorActor extends AtomicModelBase<PlatformSensorActorOm> {
    public InputPortBase<scen_info> in_scen_info;
    public InputPortBase<env_info> in_env_info;
    public InputPortBase<Boolean> in_engage_result;
    public InputPortBase<HashMap<String, move_result>> in_response;

    public OutputPortBase<String> out_request;
    public OutputPortBase<threat_info> out_threat_info;

    private Phase IDLE,PERIOD,REQUEST,DETECT;

    private HashMap<String,move_result> entitiesData;
    private scen_info in_scen_info_value;
    private boolean in_engage_result_value;
    private threat_info out_threat_info_value;

    private String entityName;


    public PlatformSensorActor(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public PlatformSensorActor(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_scen_info = new InputPortBase<scen_info>(this);
        in_env_info = new InputPortBase<env_info>(this);
        in_engage_result = new InputPortBase<Boolean>(this);
        in_response = new InputPortBase<HashMap<String,move_result>>(this);

        out_request = new OutputPortBase<String>(this);
        out_threat_info = new OutputPortBase<threat_info>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new PlatformSensorActorOm();
        entitiesData = new HashMap<String,move_result>();
        in_scen_info_value = new scen_info();
        in_engage_result_value = true;
        out_threat_info_value = new threat_info();
        entityName = "0";
    }

    @Override
    protected void constructPhase() {
        IDLE = new Phase("IDLE");   this.IDLE.setLifeTime(Double.POSITIVE_INFINITY);
        PERIOD = new Phase("PERIOD");   this.PERIOD.setLifeTime(10.0);
        REQUEST = new Phase("REQUEST");   this.REQUEST.setLifeTime(Double.POSITIVE_INFINITY);
        DETECT = new Phase("DETECT");   this.DETECT.setLifeTime(0.0);
        this.phase = IDLE;
    }

    @Override
    protected void deltaExternalFunc(Object value) {
        if(this.phase.getName().equals(IDLE.getName())){
            if(this.activePort == in_scen_info){
                in_scen_info_value = ((scen_info)value);
                entityName = in_scen_info_value.getResetInfo();
                this.phase = PERIOD;
            }
            return;
        }
        if(this.phase.getName().equals(PERIOD.getName())){
            if(this.activePort == in_engage_result){
                in_engage_result_value = ((Boolean)value);
                this.phase = IDLE;
                return;
            }
            if(this.activePort == in_env_info){
                //this.om.setIn_env_info((env_info)value);
                return;
            }
            return;
        }
        if(this.phase.getName().equals(DETECT.getName())){
            if(this.activePort == in_engage_result){
                in_engage_result_value = ((Boolean)value);
                this.phase = IDLE;
                return;
            }
            return;
        }
        if(this.phase.getName().equals(REQUEST.getName())){
            if(this.activePort == in_response){
                this.entitiesData = ((HashMap<String, move_result>)value);
                this.phase = DETECT;
                return;
            }
            if(this.activePort == in_env_info){
                //this.om.setIn_env_info((env_info)value);
            }
            return;
        }
    }

    @Override
    protected void deltaInternalFunc() {
        if(this.phase.getName().equals(PERIOD.getName())){
            this.phase = REQUEST;
        }
        if(this.phase.getName().equals(DETECT.getName())){
            this.detection_Algorithm();
        }
    }

    @Override
    protected void lambdaFunc() {
        if(this.phase.getName().equals(PERIOD.getName())){
            this.out_request.send("REQUEST");
        }
        if(this.phase.getName().equals(DETECT.getName()) && !out_threat_info_value.getName().equals("0")){
            this.out_threat_info.send(out_threat_info_value);
            out_threat_info_value.setName("0");
            this.phase = PERIOD;
        }
    }

    private void detection_Algorithm(){
        if(entitiesData.size()>0){
            move_result entity = entitiesData.get(entityName);
            if(entity == null){
                return;
            }
            int _detectedTargetsCount = 0;
            double detectRange = entity.getDetectRange();
            Iterator it = entitiesData.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry entry = (Map.Entry)it.next();
                String name = (String)entry.getKey();
                move_result value = (move_result)entry.getValue();
                if(value.getCmp() != entity.getCmp()){
                    double tmpDistance = SimUtil.calcLength(entity.getPosition().getX(),entity.getPosition().getY(),value.getPosition().getX(),value.getPosition().getY());
                    if(tmpDistance<detectRange){
                        _detectedTargetsCount++;
                        detectRange = tmpDistance;
                        out_threat_info_value = new threat_info(value);
                    }
                }
            }
            if(_detectedTargetsCount == 0){
                // entity.setPosition(new CartesianPoint(entity.getPosition().getX()+entity.getSpeed(),entity.getPosition().getY()+entity.getSpeed(),0.0));
                out_threat_info_value = new threat_info(entity);
            }
        }
    }
}
