package asw_0_8_01.des.cm;

import asw_0_8_01.des.am.CommunicateBus;
import asw_0_8_01.des.am.DamageAssessment;
import asw_0_8_01.des.am.Environment;
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

    public OutputPortBase<String> out_wp_launch;
    public OutputPortBase<Boolean> out_engage_result;

    public Platform fleet,submarine;
    //public Weapon decoy1,decoy2,torpedo;
    public Environment environment;
    public CommunicateBus bus;
    public DamageAssessment damageAssessment;

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
        out_wp_launch = new OutputPortBase<String>(this);
        out_engage_result = new OutputPortBase<Boolean>(this);
    }

    @Override
    protected void couplingComponent() {
        fleet = new Platform("Fleet",this);
        submarine = new Platform("Submarine",this);
//        decoy1 = new Weapon("decoy1",this);
//        decoy2 = new Weapon("decoy2",this);
//        torpedo = new Weapon("torpedo",this);

        environment = new Environment("env",this);
        damageAssessment = new DamageAssessment("asses",this);

        bus = new CommunicateBus("bus",this);

        fleet.constructModel();
        submarine.constructModel();
//        decoy1.constructModel();
//        decoy2.constructModel();
//        torpedo.constructModel();

        environment.constructModel();
        damageAssessment.constructModel();

        bus.constructModel();

        /**
         * EIC
         */
        this.addExternalInputCoupling(this.in_scen_info,damageAssessment.in_scen_info);
        this.addExternalInputCoupling(this.in_scen_info,environment.in_scen_info);
        this.addExternalInputCoupling(this.in_scen_info,fleet.in_scen_info);
        this.addExternalInputCoupling(this.in_scen_info,submarine.in_scen_info);

//        this.addExternalInputCoupling(this.in_entity_info,decoy1.in_entity_info);
//        this.addExternalInputCoupling(this.in_entity_info,decoy2.in_entity_info);
//        this.addExternalInputCoupling(this.in_entity_info,torpedo.in_entity_info);

        /**
         * EOC
         */
        this.addExternalOutputCoupling(fleet.out_wp_launch,this.out_wp_launch);
        this.addExternalOutputCoupling(submarine.out_wp_launch,this.out_wp_launch);
        this.addExternalOutputCoupling(damageAssessment.out_engage_result,this.out_engage_result);

        /**
         * IC
         */
        this.addInternalCoupling(damageAssessment.out_engage_result,fleet.in_engage_result);
        this.addInternalCoupling(damageAssessment.out_engage_result,submarine.in_engage_result);
//        this.addInternalCoupling(damageAssessment.out_engage_result,torpedo.in_engage_result);
//        this.addInternalCoupling(damageAssessment.out_engage_result,decoy1.in_engage_result);
//        this.addInternalCoupling(damageAssessment.out_engage_result,decoy2.in_engage_result);


        //this.addInternalCoupling(environment.out_env_info,decoy1.in_env_info);
        //this.addInternalCoupling(environment.out_env_info,decoy2.in_env_info);
        this.addInternalCoupling(environment.out_env_info,fleet.in_env_info);
        //this.addInternalCoupling(environment.out_env_info,torpedo.in_env_info);
        this.addInternalCoupling(environment.out_env_info,submarine.in_env_info);


        this.addInternalCoupling(fleet.out_move_result,bus.in_move_result);
        //this.addInternalCoupling(decoy1.out_move_result,bus.in_move_result);
       // this.addInternalCoupling(decoy2.out_move_result,bus.in_move_result);
        this.addInternalCoupling(submarine.out_move_result,bus.in_move_result);
       // this.addInternalCoupling(torpedo.out_move_result,bus.in_move_result);

        this.addInternalCoupling(bus.out_move_result,fleet.in_move_result);
        //this.addInternalCoupling(bus.out_move_result,decoy1.in_move_result);
        //this.addInternalCoupling(bus.out_move_result,decoy2.in_move_result);
        this.addInternalCoupling(bus.out_move_result,submarine.in_move_result);
       // this.addInternalCoupling(bus.out_move_result,torpedo.in_move_result);









    }
}
