package asw.soa._DEM.atomicModel;

import asw.soa._DEM.portType.ENT_INFO;
import asw.soa._DEM.portType.MoveCmd;
import asw.soa._DEM.portType.MoveResult;
import asw.soa._DEM.portType.ThreatInfo;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.*;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.exceptions.PortAlreadyDefinedException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;

public class Controller extends AtomicModel<Double, Double, SimTimeDouble> {

    /**
     * X
     */
    public InputPort<Double, Double, SimTimeDouble, MoveResult> in_MOVE_RESULT;
    public InputPort<Double, Double, SimTimeDouble, ENT_INFO> in_THREAT_INFO;
    /**
     * Y
     */
    public OutputPort<Double, Double, SimTimeDouble, MoveCmd> out_MOVE_CMD;

    /**
     * States
     */
    private Phase WAIT, IDENTIFICATION;

    /**
     * model's private data
     */
    private MoveResult currentPos;
    private ThreatInfo target;

    public Controller(String modelName, CoupledModel<Double, Double, SimTimeDouble> parentModel) {
        super(modelName, parentModel);
        //this.conflictStrategy = false;
    }

    @Override
    public void initialize(Double e) {
        /**
         * 1. 成员变量实例化
         */
        in_MOVE_RESULT = new InputPort<Double, Double, SimTimeDouble, MoveResult>(this);
        in_THREAT_INFO = new InputPort<Double, Double, SimTimeDouble, ENT_INFO>(this);
        out_MOVE_CMD = new OutputPort<Double, Double, SimTimeDouble, MoveCmd>(this);
        WAIT = new Phase("WAIT");
        WAIT.setLifeTime(Double.POSITIVE_INFINITY);
        IDENTIFICATION = new Phase("IDENTIFICATION");
        IDENTIFICATION.setLifeTime(12.0);

        currentPos = new MoveResult();
        target = new ThreatInfo();

        /**
         * 2. 输入输出端口设置
         */
        try {
            this.addInputPort("MOVE_RESULT", in_MOVE_RESULT);
            this.addInputPort("THREAT_INFO", in_THREAT_INFO);
            this.addOutputPort("MOVE_CMD", out_MOVE_CMD);
        } catch (PortAlreadyDefinedException ex) {
            SimLogger.always().error(ex);
        }

        /**
         * 3. 模型状态初始化
         */
        this.phase = IDENTIFICATION;
        super.initialize(e);
    }

    @Override
    protected void deltaInternal() {
        this.elapsedTime = 0.0;
        if (this.phase.getName().equals("WAIT")) {
            this.phase = IDENTIFICATION;
        }
        if (this.phase.getName().equals("IDENTIFICATION")) {

        }
    }

    @Override
    protected void deltaExternal(Double e, Object value) {
        System.out.println("---" + this.modelName+" --Receive MSG"+", SimTime: " + this.simulator.getSimulatorTime());
        this.elapsedTime =this.elapsedTime + e;
        if (this.phase.getName().equals("WAIT")) {
            this.phase = IDENTIFICATION;
        }
        if (this.phase.getName().equals("IDENTIFICATION")) {
            if (this.activePort == in_MOVE_RESULT) {
                //控制器接收自己的机动信息，决策依据
                currentPos = (MoveResult) value;
            } else if (this.activePort == in_THREAT_INFO) {
                target = new ThreatInfo((ENT_INFO) value);
            }
        }

    }

    @Override
    protected void lambda() {

        System.out.println("---" + this.modelName+" --Send MSG"+", SimTime: " + this.simulator.getSimulatorTime());
        if (this.phase.getName().equals("IDENTIFICATION")) {
            if (target.name.equals("0") || currentPos.name.equals("0")) {

            } else {
                MoveCmd msg = new MoveCmd(currentPos, target, "follow");
                msg.senderId = super.modelName;
                out_MOVE_CMD.send(msg);
            }
        }
    }

    @Override
    protected Double timeAdvance() {
        return this.phase.getLifeTime();
    }
}
