package combatSysModel.DEM.atomicModel;

import devs.core.AtomicModelBase;
import combatSysModel.OM.Weapon_Controller_actor_om;
import combatSysModel.portType.*;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.InputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.OutputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

/**
 *
 */
public class Actor_Controller_Weapon_am extends AtomicModelBase<Weapon_Controller_actor_om> {

    public InputPort<Double,Double, SimTimeDouble, move_finished> in_move_finished;
    public InputPort<Double,Double, SimTimeDouble, engage_result> in_engage_result;
    public InputPort<Double,Double, SimTimeDouble, scen_info> in_scen_info;
    public InputPort<Double,Double, SimTimeDouble, wp_guidance> in_wp_guidance;
    public InputPort<Double,Double, SimTimeDouble, target_info> in_target_info;

    public OutputPort<Double,Double, SimTimeDouble, move_cmd> out_move_cmd;


    @Override
    protected void constructPort() {
        in_move_finished =new InputPort<Double, Double, SimTimeDouble, move_finished>(this);
        in_engage_result = new InputPort<Double, Double, SimTimeDouble, engage_result>(this);
        in_scen_info = new InputPort<Double, Double, SimTimeDouble, scen_info>(this);
        in_wp_guidance = new InputPort<Double, Double, SimTimeDouble, wp_guidance>(this);
        in_target_info = new InputPort<Double, Double, SimTimeDouble, target_info>(this);

        out_move_cmd = new OutputPort<Double, Double, SimTimeDouble, move_cmd>(this);
    }

    private Phase IDLE,SEARCH,APPCH_WAIT,APPROACH,END;

    @Override
    protected void constructPhase() {
        IDLE = new Phase("IDLE");   IDLE.setLifeTime(Double.POSITIVE_INFINITY);
        SEARCH = new Phase("SEARCH");   SEARCH.setLifeTime(0.0);
        APPCH_WAIT = new Phase("APPCH_WAIT");   APPCH_WAIT.setLifeTime(10.0);
        APPROACH = new Phase("APPROACH");   APPROACH.setLifeTime(0.0);

        END = new Phase("END");   END.setLifeTime(Double.POSITIVE_INFINITY);
        this.phase = IDLE;
    }


    @Override
    protected void deltaExternalFunc(Object value) {
        if (this.phase.getName().equals(IDLE.getName())) {
            if (this.activePort == in_scen_info) {
                this.om.setScen_info((scen_info) value);
                this.phase = SEARCH;
                return;
            }
            if (this.activePort == in_move_finished) {
                this.om.setMove_finished((move_finished) value);
                this.phase = SEARCH;
                return;
            }
            if(this.activePort == in_target_info) {
                this.om.setTarget_info((target_info)value);
                this.phase = APPROACH;
                return;
            }
            if(this.activePort == in_engage_result) {
                this.om.setEngage_result((engage_result)value);
                this.phase = END;
                return;
            }
            return;
        }
        if(this.phase.getName().equals(APPCH_WAIT.getName())){
            if(this.activePort == in_target_info){
                this.om.setTarget_info((target_info)value);
                this.phase = APPROACH;
                return;
            }
            if(this.activePort == in_engage_result) {
                this.om.setEngage_result((engage_result)value);
                this.phase = END;
                return;
            }
            return;
        }

    }

    @Override
    protected void deltaInternalFunc() {
        if(this.phase.getName().equals(SEARCH.getName())){
            this.om.tactical_search();
            return;
        }
        if(this.phase.getName().equals(APPROACH.getName())){
            this.om.apprch();
            return;
        }
    }

    @Override
    protected void constructObjectModel() {
        this.om = new Weapon_Controller_actor_om();
    }

    @Override
    protected void lambdaFunc() {
        if(this.phase.getName().equals(SEARCH.getName())){
            this.om.getMove_cmd().setSenderId(this.fullName);
            out_move_cmd.send(this.om.getMove_cmd());
            this.phase = IDLE;
            return;
        }

        if(this.phase.getName().equals(APPROACH.getName())){
            this.om.getMove_cmd().setSenderId(this.fullName);
            out_move_cmd.send(this.om.getMove_cmd());
            this.phase = APPCH_WAIT;
            return;
        }

    }

    public Actor_Controller_Weapon_am(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Actor_Controller_Weapon_am(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }


}
