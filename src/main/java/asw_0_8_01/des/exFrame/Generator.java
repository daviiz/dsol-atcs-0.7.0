package asw_0_8_01.des.exFrame;

import asw_0_8_01.om.GeneratorOm;
import asw_0_8_01.portMsgType.entity_info;
import asw_0_8_01.portMsgType.scen_info;
import asw_0_8_01.portMsgType.wp_launch;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class Generator extends AtomicModelBase<GeneratorOm> {


    public InputPortBase<wp_launch> in_wp_launch;
    public InputPortBase<Boolean> in_engage_result;
    public OutputPortBase<scen_info> out_scen_info;
    public OutputPortBase<entity_info> out_entity_info;

    private wp_launch in_wp_launch_value;
    private boolean in_engage_result_value;
    private scen_info out_scen_info_value;
    private entity_info out_entity_info_value;

    private boolean isFireWeapon = false;
    private boolean isReplicaiton = false;

    private Phase WAIT,GEN;

    private int replicationCount = 0;

    @Override
    protected void constructPort() {
        in_wp_launch = new InputPortBase<wp_launch>(this);
        in_engage_result = new InputPortBase<Boolean>(this);
        out_entity_info = new OutputPortBase<entity_info>(this);
        out_scen_info = new OutputPortBase<scen_info>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new GeneratorOm();
    }

    @Override
    protected void constructPhase() {
        WAIT = new Phase("WAIT"); WAIT.setLifeTime(Double.POSITIVE_INFINITY);
        GEN = new Phase("GEN"); WAIT.setLifeTime(0.0);
        this.phase = GEN;
    }

    @Override
    protected void deltaExternalFunc(Object value){
        if(this.phase.getName().equals(WAIT.getName())){
            if(this.activePort == in_wp_launch){
                /**
                 *to-do:发射武器：鱼雷/鱼雷诱饵，从wp_launch中获取武器实体的初始位置
                 */
                in_wp_launch_value = (wp_launch)value;

                isFireWeapon = true;
            }
            if(this.activePort == in_engage_result && replicationCount<100){
                /**
                 * to-do:执行下一个Replication：
                 */
                in_engage_result_value = (Boolean)value;
                System.out.println("Rep_"+replicationCount+",已经执行完成了，是否成功拦截了鱼雷？ "+(in_engage_result_value ? "是": "否"));
                isReplicaiton = true;
            }
            this.phase  = GEN;
        }
    }

    @Override
    protected void deltaInternalFunc() {
        if(this.phase.getName().equals(GEN.getName())){
            //执行第一个Replication：

            replicationCount += 1;
            this.phase  = WAIT;
        }
    }

    @Override
    protected void lambdaFunc() {
        if(this.phase.getName().equals(GEN.getName())){
            if(isFireWeapon){
                out_entity_info_value.setSenderId("Rep_"+replicationCount);
                out_entity_info.send(out_entity_info_value);
                isFireWeapon = false;
            }
            if(isReplicaiton){
                out_scen_info_value.setSenderId("Rep_"+replicationCount);
                out_scen_info.send(out_scen_info_value);
                isFireWeapon = false;
            }
        }
    }

    public Generator(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public Generator(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }
}
