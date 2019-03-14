package combatSysModel.DEM.atomicModel;

import devs.core.AtomicModelBase;
import combatSysModel.OM.Maneuver_actor_om;
import combatSysModel.portType.*;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.InputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.OutputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class Actor_Maneuver_am extends AtomicModelBase<Maneuver_actor_om> {
    public InputPort<Double, Double, SimTimeDouble, engage_result> in_engage_result;
    public InputPort<Double, Double, SimTimeDouble, scen_info> in_scen_info;
    public InputPort<Double, Double, SimTimeDouble, env_info> in_env_info;
    public InputPort<Double, Double, SimTimeDouble, cmd_info> in_cmd_info;

    public OutputPort<Double, Double, SimTimeDouble, move_finished> out_move_finished;
    public OutputPort<Double, Double, SimTimeDouble, move_result> out_move_result;
    public OutputPort<Double, Double, SimTimeDouble, fuel_exhausted> out_fuel_exhausted;


    private Phase IDLE,MOVE,FUEL;
    @Override
    protected void constructPort() {
        in_engage_result = new InputPort<Double, Double, SimTimeDouble, engage_result>(this);
        in_scen_info = new InputPort<Double, Double, SimTimeDouble, scen_info>(this);
        in_env_info = new InputPort<Double, Double, SimTimeDouble, env_info>(this);
        in_cmd_info = new InputPort<Double, Double, SimTimeDouble, cmd_info>(this);

        out_move_finished = new OutputPort<Double, Double, SimTimeDouble, move_finished>(this);
        out_move_result = new OutputPort<Double, Double, SimTimeDouble, move_result>(this);
        out_fuel_exhausted = new OutputPort<Double, Double, SimTimeDouble, fuel_exhausted>(this);
    }

    @Override
    protected void constructPhase() {
        IDLE = new Phase("IDLE");   IDLE.setLifeTime(Double.POSITIVE_INFINITY);
        MOVE = new Phase("MOVE");   MOVE.setLifeTime(7.0);
        FUEL = new Phase("FUEL");   FUEL.setLifeTime(0.0);
        this.phase = MOVE;
    }

    @Override
    protected void constructObjectModel() {
        this.om = new Maneuver_actor_om();
    }

    @Override
    protected void deltaExternalFunc(Object value) {
        if(this.phase.getName().equals(IDLE.getName())){
            if(this.activePort == in_scen_info){
                this.om.setScen_info((scen_info) value);
                return;
            }
            if(this.activePort == in_cmd_info){
                this.om.setCmd_info((cmd_info) value);
                this.phase = MOVE;
                return;
            }

            return;
        }
        if(this.phase.getName().equals(MOVE.getName())){
            if(this.activePort == in_engage_result){
                this.om.setEngage_result((engage_result) value);
                this.phase = IDLE;
                return;
            }
            if(this.activePort == in_env_info){
                env_info e = (env_info) value;
                if(e.camp != this.om.getViewData().belong){
                    this.om.setEnv_info((env_info) value);
                }
                return;
            }
            if(this.activePort == in_cmd_info){
                this.om.setCmd_info((cmd_info) value);
                return;
            }
            return;
        }
    }

    @Override
    protected void deltaInternalFunc() {
        //System.out.println("---" + this.modelName+" -- deltaInternalFunc "+", SimTime: " + this.simulator.getSimulatorTime());
        if(this.phase.getName().equals(MOVE.getName())){
            this.om.cmd_Check();
            if(!this.om.isCmdCheckResult()){
                this.om.motion_Equation();
                this.om.getViewData().startTime = this.simulator.getSimulatorTime();
                this.om.getViewData().stopTime = this.om.getViewData().startTime + this.phase.getLifeTime();
            }
            return;
        }
        if(this.phase.getName().equals(FUEL.getName())){
            this.om.fuel_Check();
            return;
        }
    }

    @Override
    protected void lambdaFunc() {
        //System.out.println("---" + this.modelName+" --Send MSG"+", SimTime: " + this.simulator.getSimulatorTime());
        if(this.phase.getName().equals(MOVE.getName())){
           if(this.om.isCmdCheckResult()){
               this.om.getMove_finished().setSenderId(this.fullName);
                this.out_move_finished.send(this.om.getMove_finished());
                this.phase = IDLE;
                return;
           }
            if(!this.om.isCmdCheckResult()){
                this.om.getMove_result().setSenderId(this.fullName);
                this.out_move_result.send(this.om.getMove_result());
                this.phase = FUEL;
                return;
            }
            return;
        }
        if(this.phase.getName().equals(FUEL.getName())){
            if(this.om.isFuelCheckResult()){
                this.om.getFuel_exhausted().setSenderId(this.fullName);
                this.out_fuel_exhausted.send(this.om.getFuel_exhausted());
                this.phase = IDLE;
                return;
            }
            if(!this.om.isFuelCheckResult()){
                //System.out.println("IDLE");
                this.phase = MOVE;
                return;
            }
        }

    }

    public Actor_Maneuver_am(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Actor_Maneuver_am(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }
}
