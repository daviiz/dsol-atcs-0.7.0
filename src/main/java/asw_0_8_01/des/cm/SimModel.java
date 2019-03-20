package asw_0_8_01.des.cm;

import asw_0_8_01.portMsgType.engage_result;
import asw_0_8_01.portMsgType.entity_gen;
import asw_0_8_01.portMsgType.scen_gen;
import asw_0_8_01.portMsgType.wp_launch;
import devs.core.CoupledModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class SimModel extends CoupledModelBase {

    public InputPortBase<scen_gen> in_scen_info;
    public InputPortBase<entity_gen> in_entity_info;

    public OutputPortBase<wp_launch> out_wp_launch;
    public OutputPortBase<engage_result> out_engage_result;

    public SimModel(String modelName) {
        super(modelName);
    }

    public SimModel(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public SimModel(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_scen_info = new InputPortBase<scen_gen>(this);
        in_entity_info = new InputPortBase<entity_gen>(this);
        out_wp_launch = new OutputPortBase<wp_launch>(this);
        out_engage_result = new OutputPortBase<engage_result>(this);
    }

    @Override
    protected void couplingComponent() {

    }
}
