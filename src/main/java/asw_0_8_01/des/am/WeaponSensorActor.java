package asw_0_8_01.des.am;

import asw.soa.util.SimUtil;
import asw_0_8_01.om.WeaponSensorActorOm;
import asw_0_8_01.portMsgType.entity_info;
import asw_0_8_01.portMsgType.env_info;
import asw_0_8_01.portMsgType.move_result;
import asw_0_8_01.portMsgType.threat_info;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WeaponSensorActor extends AtomicModelBase<WeaponSensorActorOm> {

    public InputPortBase<entity_info[]> in_entity_info;
    public InputPortBase<env_info> in_env_info;
    public InputPortBase<Boolean> in_engage_result;
    public InputPortBase<move_result[]> in_response;

    public OutputPortBase<String> out_request;
    public OutputPortBase<threat_info> out_threat_info;

    private Phase IDLE,PERIOD,REQUEST,DETECT;

    private String entityName;
    private entity_info[] in_entity_info_value;
    private boolean in_engage_result_value;
    private HashMap<String,move_result> entitiesData;
    private boolean detectnterFuncExecFlag = false;
    private boolean fireRequest = false;
    private threat_info out_threat_info_value;

    public WeaponSensorActor(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public WeaponSensorActor(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_entity_info = new InputPortBase<entity_info[]>(this);
        in_env_info = new InputPortBase<env_info>(this);
        in_engage_result = new InputPortBase<Boolean>(this);
        in_response = new InputPortBase<move_result[]>(this);

        out_request = new OutputPortBase<String>(this);
        out_threat_info = new OutputPortBase<threat_info>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new WeaponSensorActorOm();
        entityName = this.fullName.split("\\.")[2];
        in_entity_info_value = new entity_info[2];
        entitiesData = new HashMap<String,move_result>();
        out_threat_info_value = new threat_info();
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
            if(this.activePort == in_entity_info){
                in_entity_info_value = ((entity_info[])value);
                for (int i = 0; i < 2; i++) {
                    if(entityName.equals(in_entity_info_value[i].getResetInfo()))
                    {
                        this.phase = PERIOD;
                    }
                }

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
                move_result[] results = new move_result[10];
                results = (move_result[])value;

                for(int i=0; i<10 && results[i]!= null;i++){
                    this.entitiesData.put(results[i].getName(),results[i]);
                }

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
            fireRequest = true;

        }
        if(this.phase.getName().equals(DETECT.getName())){
            this.detection_Algorithm();
            this.phase = PERIOD;
        }
    }

    private void detection_Algorithm(){
        if(entitiesData.size()>0){
            move_result entity = entitiesData.get(entityName);
            if(entity == null){
                return;
            }
            out_threat_info_value = new threat_info(entity);
            detectnterFuncExecFlag = true;
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
                        detectnterFuncExecFlag = true;
                    }
                }
            }
            //if(_detectedTargetsCount == 0){
                // entity.setPosition(new CartesianPoint(entity.getPosition().getX()+entity.getSpeed(),entity.getPosition().getY()+entity.getSpeed(),0.0));
                //out_threat_info_value = new threat_info(entity);
            //}
        }

    }

    @Override
    protected void lambdaFunc() {
        if(fireRequest){
            this.out_request.send("REQUEST");
            this.phase = REQUEST;
            fireRequest = false;
        }
        if(detectnterFuncExecFlag && !out_threat_info_value.getName().equals("0")){
            this.out_threat_info.send(out_threat_info_value);
            //this.phase = PERIOD;
            detectnterFuncExecFlag = false;
        }
    }
}
