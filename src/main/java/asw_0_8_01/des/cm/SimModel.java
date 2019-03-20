package asw_0_8_01.des.cm;

import asw_0_8_01.des.am.DamageAssessment;
import asw_0_8_01.des.am.Environment;
import asw_0_8_01.portMsgType.engage_result;
import asw_0_8_01.portMsgType.entity_info;
import asw_0_8_01.portMsgType.scen_info;
import asw_0_8_01.portMsgType.wp_launch;
import devs.core.CoupledModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class SimModel extends CoupledModelBase {

    public InputPortBase<scen_info> in_scen_info;
    public InputPortBase<entity_info> in_entity_info;

    public OutputPortBase<wp_launch> out_wp_launch;
    public OutputPortBase<engage_result> out_engage_result;

    private Platform fleet,submarine;

    private Weapon decoy1,decoy2,torpedo;

    private Environment environment;

    private DamageAssessment damageAssessment;

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
        in_scen_info = new InputPortBase<scen_info>(this);
        in_entity_info = new InputPortBase<entity_info>(this);
        out_wp_launch = new OutputPortBase<wp_launch>(this);
        out_engage_result = new OutputPortBase<engage_result>(this);
    }

    @Override
    protected void couplingComponent() {
        fleet = new Platform("fleet",this);
        submarine = new Platform("submarine",this);
        decoy1 = new Weapon("decoy1",this);
        decoy2 = new Weapon("decoy2",this);
        torpedo = new Weapon("torpedo",this);

        environment = new Environment("env",this);
        damageAssessment = new DamageAssessment("asses",this);

        fleet.constructModel();
        submarine.constructModel();
        decoy1.constructModel();
        decoy2.constructModel();
        torpedo.constructModel();

        environment.constructModel();
        damageAssessment.constructModel();



    }
}
