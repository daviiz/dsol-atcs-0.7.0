package asw_0_8_01.des.cm;

import asw_0_8_01.des.am.PlatformSensorActor;
import asw_0_8_01.des.am.PlatformSensorUpdater;
import asw_0_8_01.portMsgType.*;
import devs.core.CoupledModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class PlatformSensor extends CoupledModelBase {

    public InputPortBase<move_result> in_move_result;
    public InputPortBase<scen_info> in_scen_info;
    public InputPortBase<env_info> in_env_info;
    public InputPortBase<engage_result> in_engage_result;

    public OutputPortBase<threat_info> out_threat_info;

    private PlatformSensorActor actor;
    private PlatformSensorUpdater updater;

    public PlatformSensor(String modelName) {
        super(modelName);
    }

    public PlatformSensor(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public PlatformSensor(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_move_result = new InputPortBase<move_result>(this);
        in_scen_info = new InputPortBase<scen_info>(this);
        in_env_info = new InputPortBase<env_info>(this);
        in_engage_result = new InputPortBase<engage_result>(this);

        out_threat_info = new OutputPortBase<threat_info>(this);
    }

    @Override
    protected void couplingComponent() {
        actor = new PlatformSensorActor("actor",this);
        updater = new PlatformSensorUpdater("updater",this);
        actor.constructModel();
        updater.constructModel();

    }
}