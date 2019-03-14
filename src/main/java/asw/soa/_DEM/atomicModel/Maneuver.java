package asw.soa._DEM.atomicModel;

import asw.soa._DEM.portType.ENT_INFO;
import asw.soa._DEM.portType.MoveCmd;
import asw.soa._DEM.portType.MoveResult;
import asw.soa._OM.Fleet_OM;
import asw.soa._OM.Submarine_OM;
import asw.soa.data.ViewData;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.*;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.exceptions.PortAlreadyDefinedException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.language.d3.CartesianPoint;

public class Maneuver extends AtomicModel<Double, Double, SimTimeDouble> {

    /**
     * 模型输入端口 - X
     */
    public InputPort<Double, Double, SimTimeDouble, MoveCmd> in_MOVE_CMD;
    /**
     * 模型输出端口 - Y
     */
    public OutputPort<Double, Double, SimTimeDouble, MoveResult> out_MOVE_RESULT;

    /**
     * 模型状态集合 - States
     */
    private Phase IDLE, MOVE, FUEL;

    /**
     * 模型私有数据
     */
    private ViewData data;
    private ENT_INFO target;
    private MoveCmd moveCmd;

    public Maneuver(String modelName, CoupledModel<Double, Double, SimTimeDouble> parentModel, ViewData data) {
        super(modelName, parentModel);
        this.data = data;
        //this.conflictStrategy = false;
    }

    @Override
    public void initialize(Double e) {
        /**
         * 1. 成员变量实例化
         */
        this.target = new ENT_INFO();
        this.moveCmd = new MoveCmd();
        in_MOVE_CMD = new InputPort<Double, Double, SimTimeDouble, MoveCmd>(this);
        out_MOVE_RESULT = new OutputPort<Double, Double, SimTimeDouble, MoveResult>(this);
        IDLE = new Phase("IDLE");
        IDLE.setLifeTime(Double.POSITIVE_INFINITY);
        MOVE = new Phase("MOVE");
        MOVE.setLifeTime(10);
        FUEL = new Phase("FUEL");
        FUEL.setLifeTime(0);


        /**
         * 2. 输入输出端口设置
         */
        try {
            this.addInputPort("MOVE_CMD", in_MOVE_CMD);
            this.addOutputPort("MOVE_RESULT", out_MOVE_RESULT);
        } catch (PortAlreadyDefinedException ex) {
            SimLogger.always().error(ex);
        }
        /**
         * 3. 模型状态初始化：
         */
        this.phase = MOVE;
        //this.sigma = this.phase.getLifeTime();
        super.initialize(e);
    }

    /**
     * the delta internal function that should be implemented by the extending class.
     */
    @Override
    protected void deltaInternal() {
        this.elapsedTime = 0.0;
        if (super.phase.getName().equals("IDLE")) {
            this.phase = MOVE;
        }
        if (super.phase.getName().equals("MOVE")) {
            //this.sigma = this.phase.getLifeTime();
            this.data.origin = this.data.destination;

            if (!this.data.status) {
                this.data.destination = new CartesianPoint(data.destination.x, data.destination.y, 0);
            } else if (this.target.name.equals("0")) {
                data.destination = new CartesianPoint(data.destination.x + data.speed, data.destination.y + data.speed,
                        0);
            } else {

                /**
                 * 调用OM解析moveCmd，并返回结果：
                 */
                if(this.modelName.equals("Fleet$maneuver")){
                    data.destination = Fleet_OM.AlgorithmsOfManeuver(data,moveCmd);
                }else if(this.modelName.equals("Submarine_maneuver")){
                    data.destination = Submarine_OM.AlgorithmsOfManeuver(data,moveCmd);
                }
            }
            data.startTime = this.simulator.getSimulatorTime();
            data.stopTime = data.startTime + this.phase.getLifeTime();
        }
    }

    /**
     * The user defined deltaExternal method that is defined in an extension of this class.
     *
     * @param e     R; the elapsed time since the last state transition
     * @param value Object; the value that has been passed through the port
     */
    @Override
    protected synchronized void deltaExternal(Double e, Object value) {
        System.out.println("---" + this.modelName+" --Receive MSG"+", SimTime: " + this.simulator.getSimulatorTime());
        this.elapsedTime = this.elapsedTime +  e;
        if (this.phase.getName().equals("MOVE")) {
            this.moveCmd = (MoveCmd) value;
            this.target = new ENT_INFO(this.moveCmd.threat);
        }
    }

    /**
     * the lambda function that should be implemented by the extending class.
     */
    @Override
    protected void lambda() {

        System.out.println("---" + this.modelName+" --Send MSG"+", SimTime: " + this.simulator.getSimulatorTime());
        if (this.phase.getName().equals("MOVE")) {
            MoveResult result = new MoveResult(data);
            result.senderId = super.modelName;
            if (result.name.equals("0")) return;
            out_MOVE_RESULT.send(result);
        }
    }

    /**
     * the time advance function that should be implemented by the extending class.
     *
     * @return the ta, which is the time advance from one state to the next.
     */
    @Override
    protected Double timeAdvance()
    {
        return this.phase.getLifeTime();
    }
}
