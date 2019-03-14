package combatSysModel.DEM.atomicModel;

import devs.core.AtomicModelBase;
import combatSysModel.OM.Platform_Controller_actor_om;
import combatSysModel.portType.*;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.InputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.OutputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class Actor_Controller_Platform_am extends AtomicModelBase<Platform_Controller_actor_om> {

    public InputPort<Double, Double, SimTimeDouble, move_finished> in_move_finished;
    public InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.engage_result> in_engage_result;
    public InputPort<Double, Double, SimTimeDouble, combatSysModel.portType.env_info> in_env_info;
    public InputPort<Double, Double, SimTimeDouble, guidance_info> in_guidance_info;
    public InputPort<Double, Double, SimTimeDouble, target_info> in_target_info;
    public InputPort<Double, Double, SimTimeDouble, scen_info> in_scen_info;

    public InputPort<Double, Double, SimTimeDouble, move_result> in_move_result;

    public OutputPort<Double, Double, SimTimeDouble, move_cmd> out_move_cmd;
    public OutputPort<Double, Double, SimTimeDouble, wp_launch> out_wp_launch;
    public OutputPort<Double, Double, SimTimeDouble, wp_guidance> out_wp_guidance;

    private Phase IDLE,RECONNAIASSANCE,APPROACH,COMBAT,EVASION,CONTROL,END;

    @Override
    protected void constructPort() {
        in_move_finished = new InputPort<Double, Double, SimTimeDouble, move_finished>(this);
        in_engage_result = new InputPort<Double, Double, SimTimeDouble, engage_result>(this);
        in_env_info = new InputPort<Double, Double, SimTimeDouble, env_info>(this);
        in_guidance_info = new InputPort<Double, Double, SimTimeDouble, guidance_info>(this);
        in_target_info = new InputPort<Double, Double, SimTimeDouble, target_info>(this);
        in_scen_info = new InputPort<Double, Double, SimTimeDouble, scen_info>(this);

        in_move_result = new InputPort<Double, Double, SimTimeDouble, move_result>(this);

        out_move_cmd = new OutputPort<Double, Double, SimTimeDouble, move_cmd>(this);
        out_wp_launch = new OutputPort<Double, Double, SimTimeDouble, wp_launch>(this);
        out_wp_guidance = new OutputPort<Double, Double, SimTimeDouble, wp_guidance>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new Platform_Controller_actor_om();
    }

    @Override
    protected void constructPhase() {
        IDLE = new Phase("IDLE");
        IDLE.setLifeTime(Double.POSITIVE_INFINITY);

        RECONNAIASSANCE = new Phase("RECONNAIASSANCE");
        RECONNAIASSANCE.setLifeTime(10.0);

        APPROACH = new Phase("APPROACH");
        APPROACH.setLifeTime(10.0);

        COMBAT = new Phase("COMBAT");
        COMBAT.setLifeTime(10.0);

        EVASION = new Phase("EVASION");
        EVASION.setLifeTime(10.0);

        CONTROL = new Phase("CONTROL");
        CONTROL.setLifeTime(10.0);

        END = new Phase("END");
        END.setLifeTime(Double.POSITIVE_INFINITY);

        this.phase = APPROACH;
        this.lastPhase = IDLE;
    }

    @Override
    protected void deltaExternalFunc(Object value) {

        if(this.activePort == in_move_result){
            this.om.setIn_move_result((move_result)value);
            this.phase = APPROACH;
            return;
        }

        if(this.phase.getName().equals(IDLE.getName())){

            if(this.lastPhase.getName().equals(RECONNAIASSANCE.getName())){
                if(this.activePort == in_move_finished){
                    this.om.setIn_move_finished((move_finished)value);
                }
                this.phase = RECONNAIASSANCE;
            }
            if(this.lastPhase.getName().equals(EVASION.getName())){
                if(this.activePort == in_move_finished){
                    this.om.setIn_move_finished((move_finished)value);
                }
                this.phase = EVASION;
            }
            if(this.lastPhase.getName().equals(APPROACH.getName())){
                if(this.activePort == in_move_finished){
                    this.om.setIn_move_finished((move_finished)value);
                }
                this.phase = APPROACH;
            }

            if(this.activePort == in_target_info){
                this.om.setIn_target_info((target_info)value);
                return;
            }
            if(this.activePort == in_scen_info){
                this.om.setIn_scen_info((scen_info)value);
                this.phase = RECONNAIASSANCE;
                return;
            }
            if(this.activePort == in_engage_result){
                this.om.setIn_engage_result((engage_result)value);
                this.phase = END;
                return;
            }
            if(this.activePort == in_guidance_info){
                this.om.setIn_guidance_info((guidance_info)value);
                this.phase = CONTROL;
                return;
            }

            this.lastPhase = IDLE;
            return;
        }
        if(this.phase.getName().equals(CONTROL.getName())){
            if(this.activePort == in_engage_result){
                this.om.setIn_engage_result((engage_result)value);
                this.phase = END;
                return;
            }
            if(this.activePort == in_target_info){
                this.om.setIn_target_info((target_info)value);
                return;
            }
        }
        if(this.phase.getName().equals(COMBAT.getName())){
            if(this.activePort == in_target_info){
                this.om.setIn_target_info((target_info)value);
                return;
            }
        }
        if(this.phase.getName().equals(EVASION.getName())){
            if(this.activePort == in_target_info){
                this.om.setIn_target_info((target_info)value);
                return;
            }
        }
    }

    @Override
    protected void deltaInternalFunc() {
        if(this.phase.getName().equals(RECONNAIASSANCE.getName())){
            this.om.Reconn();
            return;
        }
        if(this.phase.getName().equals(APPROACH.getName())){
            this.om.Apprch();
            return;
        }
        if(this.phase.getName().equals(COMBAT.getName())){
            this.om.Combat();
            return;
        }
        if(this.phase.getName().equals(EVASION.getName())){
            this.om.Evasion();
            return;
        }
        if(this.phase.getName().equals(CONTROL.getName())){
            this.om.Ctrl();
            return;
        }
    }

    @Override
    protected void lambdaFunc() {
        if(this.phase.getName().equals(RECONNAIASSANCE.getName())){
            this.om.getOut_move_cmd().setSenderId(this.fullName);
            this.out_move_cmd.send(this.om.getOut_move_cmd());
            this.phase = IDLE;
            this.lastPhase = RECONNAIASSANCE;
            return;
        }
        if(this.phase.getName().equals(APPROACH.getName())){
            this.om.getOut_move_cmd().setSenderId(this.fullName);
            this.out_move_cmd.send(this.om.getOut_move_cmd());
            if(IDLE.getName().equals(this.om.getApprchNextPhase())){
                this.phase = IDLE;
            }else{
                this.phase = COMBAT;
            }
            this.lastPhase = APPROACH;
            return;
        }
        if(this.phase.getName().equals(COMBAT.getName())){
            this.om.getOut_wp_launch().setSenderId(this.fullName);
            this.out_wp_launch.send(this.om.getOut_wp_launch());
            this.phase = IDLE;
            this.lastPhase = COMBAT;
            return;
        }
        if(this.phase.getName().equals(EVASION.getName())){
            this.om.getOut_move_cmd().setSenderId(this.fullName);
            this.out_move_cmd.send(this.om.getOut_move_cmd());
            this.phase = IDLE;
            this.lastPhase = EVASION;
            return;
        }
        if(this.phase.getName().equals(CONTROL.getName())){
            this.om.getOut_wp_guidance().setSenderId(this.fullName);
            this.out_wp_guidance.send(this.om.getOut_wp_guidance());
            this.phase = IDLE;
            this.lastPhase = CONTROL;
            return;
        }
    }

    public Actor_Controller_Platform_am(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Actor_Controller_Platform_am(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }


}
