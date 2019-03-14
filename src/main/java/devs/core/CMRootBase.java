package devs.core;

import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 *模型开发：命名规范：
 * 原子模型：按照模型耦合层次由内而外命名，如：Actor_Controller_Platform_am
 * 耦合模型：按照模型耦合层次由内而外命名，如：Controller_Platform_cm
 */
public abstract class CMRootBase extends CoupledModel.TimeDouble implements IDEVSModel{

    public CMRootBase(String modelName) { super(modelName); }

    public CMRootBase(String modelName, TimeDouble parentModel) { super(modelName, parentModel); }

    public CMRootBase(String modelName, DEVSSimulatorInterface.TimeDouble simulator) { super(modelName, simulator); }

    /**
     * construct input and output port
     */
    protected abstract void constructComponent();
    /**
     *  coupling relation construction
     *  根耦合模型此方法为空
     */
    protected abstract void couplingComponent();

    /**
     * unify the coupling structure's construct code
     */
    @Override
    public final void constructModel(){
        constructComponent();
        couplingComponent();
    }
}
