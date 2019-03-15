package antiTorpedoCombatSystem.DES;

import devs.core.CMRootBase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class CM_Root extends CMRootBase {

    public CM_UNDERWATER_PLATFORM fleet,torpedo,submarine,decoy;
    public AM_ENV env;

    public CM_Root(String modelName) {
        super(modelName);
    }

    public CM_Root(String modelName, TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public CM_Root(String modelName, DEVSSimulatorInterface.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructComponent() {
        fleet = new CM_UNDERWATER_PLATFORM("fleet",this);
        //decoy = new CM_UNDERWATER_PLATFORM("decoy",this);
        //torpedo = new CM_UNDERWATER_PLATFORM("torpedo",this);
        submarine = new CM_UNDERWATER_PLATFORM("submarine",this);

        env = new AM_ENV("env",this);

        fleet.constructModel();
        //decoy.constructModel();
        //torpedo.constructModel();
        submarine.constructModel();
        env.constructModel();
    }

    @Override
    protected void couplingComponent() {
        this.addInternalCoupling(fleet.out_sonarInfo,env.in_sonarInfo);
        //this.addInternalCoupling(decoy.out_sonarInfo,env.in_sonarInfo);
        //this.addInternalCoupling(torpedo.out_sonarInfo,env.in_sonarInfo);
        this.addInternalCoupling(submarine.out_sonarInfo,env.in_sonarInfo);

        this.addInternalCoupling(env.out_sonarInfo,fleet.in_sonarInfo);
        //this.addInternalCoupling(env.out_sonarInfo,decoy.in_sonarInfo);
        //this.addInternalCoupling(env.out_sonarInfo,torpedo.in_sonarInfo);
        this.addInternalCoupling(env.out_sonarInfo,submarine.in_sonarInfo);
    }
}
