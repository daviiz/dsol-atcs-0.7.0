package asw.soa._DEM.atomicModel;

import asw.soa._DEM.portType.ENT_INFO;
import asw.soa._DEM.portType.MoveResult;
import asw.soa.util.SimUtil;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.*;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.exceptions.PortAlreadyDefinedException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;

public class Sensor extends AtomicModel<Double, Double, SimTimeDouble> {

    /**
     * X
     */
    public InputPort<Double, Double, SimTimeDouble, MoveResult> in_MOVE_RESULT;
    public InputPort<Double, Double, SimTimeDouble, MoveResult> in_THREAT_ENT_INFO;

    /**
     * Y
     */
    public OutputPort<Double, Double, SimTimeDouble, ENT_INFO> out_THREAT_INFO;

    /**
     * States
     */
    private Phase IDLE, DETECT;

    /**
     * model's private data
     */
    private MoveResult currentPos;
    private double detectRange;
    private ENT_INFO target;

    public Sensor(String modelName, CoupledModel<Double, Double, SimTimeDouble> parentModel, double detectRange) {
        super(modelName, parentModel);
        this.detectRange = detectRange;
        //this.conflictStrategy = false;
    }


    @Override
    public void initialize(Double e) {

        /**
         * 1. 模型成员变量实例化
         */
        in_MOVE_RESULT = new InputPort<Double, Double, SimTimeDouble, MoveResult>(this);
        in_THREAT_ENT_INFO = new InputPort<Double, Double, SimTimeDouble, MoveResult>(this);
        out_THREAT_INFO = new OutputPort<Double, Double, SimTimeDouble, ENT_INFO>(this);
        IDLE = new Phase("IDLE");
        DETECT = new Phase("DETECT");
        currentPos = new MoveResult();
        target = new ENT_INFO();
        IDLE.setLifeTime(Double.POSITIVE_INFINITY);
        DETECT.setLifeTime(8);

        /**
         * 2. 输入输出端口设置
         */
        try {
            this.addInputPort("MOVE_RESULT", in_MOVE_RESULT);
            this.addInputPort("THREAT_ENT_INFO", in_THREAT_ENT_INFO);
            this.addOutputPort("THREAT_INFO", out_THREAT_INFO);
        } catch (PortAlreadyDefinedException ex) {
            SimLogger.always().error(ex);
        }

        /**
         * 3. 模型状态初始化：
         */
        this.phase = DETECT;
        super.initialize(e);
    }

    @Override
    protected void deltaInternal() {
        this.elapsedTime = 0.0;
    }

    @Override
    protected void deltaExternal(Double e, Object value) {
        System.out.println("---" + this.modelName+" --Receive MSG, SimTime: " + this.simulator.getSimulatorTime());
        this.elapsedTime = this.elapsedTime +  e;
        if (this.phase.getName().equals("IDLE")) {
            this.phase = DETECT;
        }
        if (this.phase.getName().equals("DETECT")) {
            if (this.activePort == in_MOVE_RESULT) {

                //传感器接收自己的机动信息，决策依据：
                currentPos = (MoveResult) value;

            } else if (this.activePort == in_THREAT_ENT_INFO) {

                //传感器接收环境信息，决策依据：
                MoveResult ent = (MoveResult) value;
                if (ent.belong != currentPos.belong) {
                    double distance = SimUtil.calcLength(currentPos.x, currentPos.y, ent.x, ent.y);
                    if (distance < this.detectRange)
                        target = new ENT_INFO(ent);
                }

            }
        }
    }

    @Override
    protected void lambda() {

        System.out.println("---" + this.modelName+" --Send MSG, SimTime: " + this.simulator.getSimulatorTime());
        if (this.phase.getName().equals("DETECT")) {
            ENT_INFO result = new ENT_INFO(target);
            result.senderId = super.modelName;

            if (result.name.equals("0"))
            {

            }else {
                out_THREAT_INFO.send(result);
            }
        }
    }

    @Override
    protected Double timeAdvance() {
        return this.phase.getLifeTime();
    }
}
