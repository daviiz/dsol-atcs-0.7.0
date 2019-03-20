package devs.core;

import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.AtomicModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

/**
 *模型开发：命名规范：
 * 原子模型：按照模型耦合层次由内而外命名，如：Actor_Controller_Platform_am
 * 耦合模型：按照模型耦合层次由内而外命名，如：Controller_Platform_cm
 *
 * 原子模型二次开发注意：
 * 1.原子模型DEM与OM是配套成对存在的，在DEM中InputPort & OutputPort 中定义的 PortType 具体类型的参数，必须在对应OM中定义为成员变量，
 *   这么做的目的是：实现单一职责原则，DEM只负责原子模型中业务流程的控制，不涉及任何业务计算和数据，OM仅负责模型的具体业务计算；
 * 2.原子模型实例化后必须立即setOM(om)方法，添加OM模型；
 */
public abstract class AtomicModelBase<OMType extends ObjectModelBase> extends AtomicModel<Double, Double, SimTimeDouble> implements IDEVSModel {

    protected OMType om;

    /** the last phase (if needed). */
    protected Phase lastPhase = new Phase("");

    protected String nextPhaseName = "";

    @Override
    protected final void deltaInternal() {
        this.elapsedTime = 0.0;
        deltaInternalFunc();
        //this.om.setStatusValid();
    }

    @Override
    protected final void deltaExternal(Double e, Object value) {
        if(this.phase.getLifeTime() > 99999999.0){
            this.elapsedTime = 0.0;
        }else{
            this.elapsedTime += e;
        }
        deltaExternalFunc(value);
    }

    @Override
    protected final Double timeAdvance() {
        double ta = this.phase.getLifeTime();
        if(this.phase.getLifeTime() < 0.00000001){
            this.elapsedTime = 0.0;
        }
        if(this.phase.getLifeTime() == Double.POSITIVE_INFINITY){
            this.elapsedTime = 0.0;
            ta = 10000000000.0;
        }
        return ta;
    }

    @Override
    protected final void lambda() {
        //ensure updated message output each simulation frame:
        if(this.om == null){
            return;
        }
        //if(this.om.status){
            lambdaFunc();
        //}
        //this.om.setStatusInvalid();
    }

    /**
     * construct input and output port
     */
    protected abstract void constructPort();

    /**
     * construct om
     */
    protected abstract void constructObjectModel();

    /**
     * define atomic model's states
     */
    protected abstract void constructPhase();

    /**
     * atomic model's private data initialization
     */
    //protected abstract void constructModelData();

    /**
     * 原子模型外部函数，
     * 接收外部输入，无需考虑仿真事件时间推进，专注于业务逻辑
     * 外部转换函数一般用于：
     * 1.接收输入
     * 2.外部状态转换
     * 3.调用OM实现业务模型逻辑
     * @param value
     */
    protected abstract void deltaExternalFunc(Object value);

    /**
     * 原子模型内部函数
     * 一般用于：
     * 1.调用OM实现业务模型逻辑
     * 2.内部状态转换
     */
    protected abstract void deltaInternalFunc();

    /**
     * 原子模型-输出函数：
     * 一般用于实现
     * 1.模型输出；
     */
    protected abstract void lambdaFunc();



    public AtomicModelBase(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public AtomicModelBase(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    /**
     * 构造模型的顺序不能变！
     */
    @Override
    public final void constructModel() {
        constructObjectModel();
        constructPhase();
        constructPort();

        super.initialize(0.0);
    }
    public final OMType getOm() {
        return om;
    }

    public final void setOm(OMType om) {
        this.om = om;
    }
}
