package combatSysModel.DEM.atomicModel;

import devs.core.AtomicModelBase;
import combatSysModel.OM.Maneuver_updater_om;
import combatSysModel.portType.cmd_info;
import combatSysModel.portType.move_cmd;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.InputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.OutputPort;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class Updater_Maneuver_am extends AtomicModelBase<Maneuver_updater_om> {

    @Override
    protected void constructObjectModel() {
        this.om = new Maneuver_updater_om();
    }

    public InputPort<Double, Double, SimTimeDouble, move_cmd> in_move_cmd;

    public OutputPort<Double, Double, SimTimeDouble, cmd_info> out_cmd_info;

    private Phase WAIT,INTERPRETATION;
    @Override
    protected void constructPort() {
        in_move_cmd = new InputPort<Double, Double, SimTimeDouble, move_cmd>(this);
        out_cmd_info = new OutputPort<Double, Double, SimTimeDouble, cmd_info>(this);
    }

    @Override
    protected void constructPhase() {
        WAIT = new Phase("WAIT");   WAIT.setLifeTime(Double.POSITIVE_INFINITY);
        INTERPRETATION = new Phase("INTERPRETATION");    INTERPRETATION.setLifeTime(0.0);
        this.phase = WAIT;
    }


    @Override
    protected void deltaExternalFunc(Object value) {
        if(this.phase.getName().equals(WAIT.getName())){
            if(this.activePort == in_move_cmd){
                this.om.setMove_cmd((move_cmd)value);
                this.phase = INTERPRETATION;
                return;
            }
        }
    }

    @Override
    protected void deltaInternalFunc() {
        if(this.phase.getName().equals(INTERPRETATION.getName())){
            this.om.cmd_Interpreter();
            return;
        }
    }

    @Override
    protected void lambdaFunc() {
        if(this.phase.getName().equals(INTERPRETATION.getName())){
            this.om.getCmd_info().setSenderId(this.fullName);
            this.out_cmd_info.send(this.om.getCmd_info());
            this.phase = WAIT;
            return;
        }
    }

    public Updater_Maneuver_am(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Updater_Maneuver_am(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }
}
