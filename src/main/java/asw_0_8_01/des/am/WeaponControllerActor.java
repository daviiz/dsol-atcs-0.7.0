package asw_0_8_01.des.am;

import asw_0_8_01.om.WeaponControllerActorOm;
import asw_0_8_01.portMsgType.entity_info;
import asw_0_8_01.portMsgType.threat_info;
import combatSysModel.portType.target_info;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class WeaponControllerActor extends AtomicModelBase<WeaponControllerActorOm> {

    public InputPortBase<Boolean> in_move_finished;
    ////

    public InputPortBase<Boolean> in_engage_result;
    public InputPortBase<entity_info[]> in_entity_info;
    public InputPortBase<threat_info> in_target_info;

    public OutputPortBase<String> out_move_cmd;// "APPROACH$X$Y"

    private Phase IDLE,SEARCH,END,APPCH_WAIT,APPROACH;

    private String entityName;
    private entity_info initialInfo;
    private threat_info target_info_value;
    private boolean fireApprch = false;
    private boolean fireTacticl_move = false;
    private String out_move_cmd_value;

    public WeaponControllerActor(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public WeaponControllerActor(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_move_finished = new InputPortBase<Boolean>(this);
        in_engage_result = new InputPortBase<Boolean>(this);
        in_entity_info = new InputPortBase<entity_info[]>(this);
        in_target_info = new InputPortBase<threat_info>(this);

        out_move_cmd = new OutputPortBase<String>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new WeaponControllerActorOm();
        this.entityName = this.fullName.split("\\.")[2];
        initialInfo = new entity_info();
        target_info_value = new threat_info();
        out_move_cmd_value = "0";
    }

    @Override
    protected void constructPhase() {
        IDLE = new Phase("IDLE"); IDLE.setLifeTime(Double.POSITIVE_INFINITY);
        END = new Phase("END"); END.setLifeTime(Double.POSITIVE_INFINITY);
        SEARCH = new Phase("SEARCH"); SEARCH.setLifeTime(0.0);
        APPROACH = new Phase("APPROACH"); APPROACH.setLifeTime(0.0);
        APPCH_WAIT = new Phase("APPCH_WAIT"); APPCH_WAIT.setLifeTime(10.0);
        this.phase = IDLE;
    }

    @Override
    protected void deltaExternalFunc(Object value) {
        switch (this.phase.getName()){
            case "IDLE" :{
                if(this.activePort == in_entity_info){
                    entity_info[] tmp = (entity_info[]) value;
                    for (int i = 0; i < 2; i++) {
                        if(this.entityName.equals(tmp[i].getResetInfo())){
                            initialInfo = tmp[i];
                            this.phase = SEARCH;
                        }
                    }
                }
                if(this.activePort == in_target_info){
                    target_info_value = (threat_info)value;
                    this.phase = APPROACH;
                }
                if(this.activePort == in_engage_result){
                    this.phase = END;
                }
                break;
            }
            case "APPCH_WAIT": {
                if(this.activePort == in_target_info){
                    target_info_value = (threat_info)value;
                    this.phase = APPROACH;
                }
                if(this.activePort == in_engage_result){
                    this.phase = END;
                }
                break;
            }
        }

    }

    @Override
    protected void deltaInternalFunc() {
        switch (this.phase.getName()){
            case "SEARCH" :{
                this.tacticl_move();
                this.phase = IDLE;
                break;
            }
            case "APPROACH" :{
                this.apprch();
                this.phase = APPCH_WAIT;
                break;
            }
            case "APPCH_WAIT" :{
                this.phase = SEARCH;
                break;
            }
        }
    }

    private void tacticl_move(){
        xxx();
        fireTacticl_move = true;
    }

    private void apprch(){
        xxx();
        fireApprch = true;
    }

    private void xxx(){
        if("0".equals(target_info_value.getName())){
            return;
        }
        if(!entityName.equals(target_info_value.getName())){
            out_move_cmd_value = "APPROACH$"+target_info_value.getPosition().getX()+"$"+target_info_value.getPosition().getY();
        }
    }

    @Override
    protected void lambdaFunc() {
        if(fireTacticl_move || fireApprch){
            out_move_cmd.send(out_move_cmd_value);
        }

    }
}
