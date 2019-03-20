package asw_0_8_01.des.cm;

import asw_0_8_01.des.exFrame.Generator;
import asw_0_8_01.des.exFrame.Transducer;
import devs.core.CMRootBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class CombatSystem extends CMRootBase {

    private Generator gen;
    private Transducer trans;
    private SimModel model;

    public CombatSystem(String modelName) {
        super(modelName);
    }

    public CombatSystem(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public CombatSystem(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructComponent() {
        gen = new Generator("gen",this);
        trans = new Transducer("trans",this);
        model = new SimModel("model",this);

        gen.constructModel();
        trans.constructModel();
        model.constructModel();
    }

    @Override
    protected void couplingComponent() {
        this.addInternalCoupling(gen.out_scen_info,model.in_scen_info);
        this.addInternalCoupling(gen.out_entity_info,model.in_entity_info);
        this.addInternalCoupling(model.out_engage_result,trans.in_engage_result);
        this.addInternalCoupling(model.out_wp_launch,gen.in_wp_launch);


    }
}
