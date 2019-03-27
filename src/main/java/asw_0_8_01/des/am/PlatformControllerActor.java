package asw_0_8_01.des.am;

import asw_0_8_01.om.PlatformControllerActorOm;
import asw_0_8_01.portMsgType.guidance_info;
import asw_0_8_01.portMsgType.scen_info;
import asw_0_8_01.portMsgType.threat_info;
import asw_0_8_01.portMsgType.wp_guidance;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class PlatformControllerActor extends AtomicModelBase<PlatformControllerActorOm> {

    public InputPortBase<Boolean> in_move_finished;
    public OutputPortBase<wp_guidance> out_wp_guidance;
    public InputPortBase<guidance_info> in_guidance_info;

    ///////////
    public InputPortBase<Boolean> in_engage_result; //用于结束控制器模型运行的消息
    public InputPortBase<scen_info> in_scen_info;   //用户初始化控制器模型的仿真
    public InputPortBase<threat_info> in_target_info;

    private boolean in_engage_result_value;
    private scen_info in_scen_info_value;
    private threat_info in_target_info_value;

    public OutputPortBase<String> out_wp_launch;
    public OutputPortBase<String> out_move_cmd;

    private Phase IDLE,RECONNAIASSANCE,APPROACH,COMBAT,EVASION,END;
    private String out_wp_launch_value;
    private String out_move_cmd_value;
    private String entityName;

    private boolean apprchExecFlag = false;
    private boolean combatExecFlag = false;
    private boolean evasionExecFlag = false;
    private boolean reconExecFlag = false;
    private threat_info thisModelInfo;
    private boolean wpLaunched = false;


    public PlatformControllerActor(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public PlatformControllerActor(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_engage_result = new InputPortBase<Boolean>(this);
        in_scen_info = new InputPortBase<scen_info>(this);
        in_target_info = new InputPortBase<threat_info>(this);
        in_move_finished = new InputPortBase<Boolean>(this);
        in_guidance_info = new InputPortBase<guidance_info>(this);

        out_wp_launch = new OutputPortBase<String>(this);
        out_move_cmd = new OutputPortBase<String>(this);
        out_wp_guidance = new OutputPortBase<wp_guidance>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new PlatformControllerActorOm();
        out_wp_launch_value = "0";
        out_move_cmd_value = "0";
        this.entityName = this.fullName.split("\\.")[2];
        in_engage_result_value = true;
        in_scen_info_value = new scen_info();
        in_target_info_value = new threat_info();
        thisModelInfo = new threat_info();
    }

    @Override
    protected void constructPhase() {
        IDLE = new Phase("IDLE");
        IDLE.setLifeTime(Double.POSITIVE_INFINITY);

        RECONNAIASSANCE = new Phase("RECONNAIASSANCE");
        RECONNAIASSANCE.setLifeTime(5.0);

        APPROACH = new Phase("APPROACH");
        APPROACH.setLifeTime(5.0);

        COMBAT = new Phase("COMBAT");
        COMBAT.setLifeTime(5.0);

        EVASION = new Phase("EVASION");
        EVASION.setLifeTime(5.0);

        END = new Phase("END");
        END.setLifeTime(Double.POSITIVE_INFINITY);

        this.phase = IDLE;
    }

    @Override
    protected void deltaExternalFunc(Object value) {
        switch (this.phase.getName()){
            case "IDLE" :{
                if(this.activePort == in_scen_info){
                    in_scen_info_value = (scen_info)value;
                    this.entityName = this.fullName.split("\\.")[2];
                    this.phase = RECONNAIASSANCE;
                }
                if(this.activePort == in_target_info){
                    in_target_info_value = (threat_info) value;

                    if(this.entityName.equals(in_target_info_value.getName())){
                        thisModelInfo = (threat_info) value;
                    }

                    this.phase = APPROACH;
                }
                if(this.activePort == in_engage_result){
                    in_engage_result_value = (Boolean)value;
                    this.phase = END;
                }
                break;
            }
            case "COMBAT" :{
                if(this.activePort == in_target_info){
                    in_target_info_value = (threat_info) value;

                    if(this.entityName.equals(in_target_info_value.getName())){
                        thisModelInfo = (threat_info) value;
                    }
                }
                break;
            }
            case "EVASION" :{
                if(this.activePort == in_target_info){
                    in_target_info_value = (threat_info) value;

                    if(this.entityName.equals(in_target_info_value.getName())){
                        thisModelInfo = (threat_info) value;
                    }
                }
                break;
            }

        }
    }

    @Override
    protected void deltaInternalFunc() {
        switch (this.phase.getName()){
            case "APPROACH":{
                this.apprch();
                this.phase = COMBAT;
                break;
            }
            case "COMBAT":{
                this.combat();
                this.phase = EVASION;
                break;
            }
            case "EVASION":{
                this.evasion();
                this.phase = IDLE;
                break;
            }
            case "RECONNAIASSANCE" :{
                this.recon();
                this.phase = IDLE;
                break;
            }
        }
    }

    @Override
    protected void lambdaFunc() {

        if(apprchExecFlag){
            this.out_move_cmd.send(out_move_cmd_value);
            apprchExecFlag = false;
        }

        if(combatExecFlag && !wpLaunched){
            this.out_wp_launch.send(out_wp_launch_value);
            combatExecFlag = false;
            wpLaunched = true;
        }

        if(evasionExecFlag){
            this.out_move_cmd.send(out_move_cmd_value);
            evasionExecFlag = false;
        }
        if(reconExecFlag){
            this.out_move_cmd.send(out_move_cmd_value);
            reconExecFlag = false;
        }

    }

    private void apprch(){
        if(this.entityName.contains("Submarine") && !in_target_info_value.getName().equals("Submarine")) {
            this.out_move_cmd_value = "APPROACH$"+this.in_target_info_value.getPosition().getX()+"$"+this.in_target_info_value.getPosition().getY()+"";
        }
        apprchExecFlag = true;
    }

    private void combat(){
        //"Torpedo$srcX$srcY$desX$desY"  "Decoy$srcX$srcY$desX$desY"
        if(this.entityName.equals("Fleet") && !in_target_info_value.getName().equals("Fleet")){
            out_wp_launch_value = "Decoy$"+thisModelInfo.getPosition().getX()+"$"+thisModelInfo.getPosition().getY()+"$"
                                +in_target_info_value.getPosition().getX()+"$"+in_target_info_value.getPosition().getY()+"$";
            combatExecFlag = true;
        }
        if(this.entityName.equals("Submarine") && !in_target_info_value.getName().equals("Submarine")) {
            out_wp_launch_value = "Torpedo$" + thisModelInfo.getPosition().getX() + "$" + thisModelInfo.getPosition().getY() + "$"
                    + in_target_info_value.getPosition().getX() + "$" + in_target_info_value.getPosition().getY() + "$";
            combatExecFlag = true;
        }
    }

    private void evasion(){
        if(this.entityName.contains("Fleet") && !in_target_info_value.getName().equals("Fleet")) {
            this.out_move_cmd_value = "EVASION$"+this.in_target_info_value.getPosition().getX()+"$"+this.in_target_info_value.getPosition().getY()+"";
        }
        evasionExecFlag = true;
    }

    private void recon(){
        if(in_target_info_value.getName().equals("0"))
        {
            reconExecFlag = false;
        }else{
            if(this.entityName.contains("Submarine")) {
                this.out_move_cmd_value = "APPROACH$"+this.in_target_info_value.getPosition().getX()+"$"+this.in_target_info_value.getPosition().getY()+"";
            }
            if(this.entityName.contains("Fleet")) {
                this.out_move_cmd_value = "EVASION$"+this.in_target_info_value.getPosition().getX()+"$"+this.in_target_info_value.getPosition().getY()+"";
            }
            reconExecFlag = true;
        }
    }
}
