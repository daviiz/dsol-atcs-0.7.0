package combatSysModel.DEM.coupledModel;

import devs.core.CoupledModelBase;
import combatSysModel.DEM.atomicModel.Environment_am;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class ASW_CM extends CoupledModelBase {


     public Platform_cm fleet;
     public Platform_cm submarine;
     public Environment_am env;

    //private Actor_Maneuver_am f;


    public ASW_CM(String modelName) {
        super(modelName);

    }

    public ASW_CM(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);

    }

    public ASW_CM(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        this.addInternalCoupling(fleet.out_move_result,env.in_move_result);
        this.addInternalCoupling(submarine.out_move_result,env.in_move_result);
        this.addInternalCoupling(env.out_env_info,fleet.in_env_info);
        this.addInternalCoupling(env.out_env_info,submarine.in_env_info);

    }

    @Override
    protected void couplingComponent() {

        fleet = new Platform_cm("Fleet",this);
        fleet.constructModel();

        submarine = new Platform_cm("Submarine",this);
        submarine.constructModel();

        env = new Environment_am("env",this);
        env.constructModel();
    }
}
