package asw_0_8_01.des.am;

import asw_0_8_01.om.PlatformControllerUpdaterOm;
import asw_0_8_01.portMsgType.scen_info;
import asw_0_8_01.portMsgType.threat_info;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class PlatformControllerUpdater extends AtomicModelBase<PlatformControllerUpdaterOm> {

    public InputPortBase<threat_info> in_threat_info;
    public InputPortBase<scen_info> in_scen_info;

    public OutputPortBase<threat_info> out_target_info;

    private String entityName;
    private threat_info in_threat_info_value;

    private Phase WAIT,IDENTIFICATION;

    public PlatformControllerUpdater(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public PlatformControllerUpdater(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_threat_info = new InputPortBase<threat_info>(this);
        in_scen_info = new InputPortBase<scen_info>(this);
        out_target_info = new OutputPortBase<threat_info>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new PlatformControllerUpdaterOm();
        entityName = "0";
        in_threat_info_value = new threat_info();
    }

    @Override
    protected void constructPhase() {
        WAIT = new Phase("WAIT");
        WAIT.setLifeTime(Double.POSITIVE_INFINITY);
        IDENTIFICATION = new Phase("IDENTIFICATION");
        IDENTIFICATION.setLifeTime(5.0);
        this.phase = WAIT;
    }

    @Override
    protected void deltaExternalFunc(Object value) {
        if (this.phase.getName().equals(WAIT.getName())) {
            if (this.activePort == in_scen_info) {
                scen_info tmp = ((scen_info)value);
                this.entityName = tmp.getResetInfo();
                return;
            }
            if (this.activePort == in_threat_info) {
                in_threat_info_value = ((threat_info)value);
                this.phase = IDENTIFICATION;
                return;
            }
            return;
        }
        if (this.phase.getName().equals(IDENTIFICATION.getName())) {
            if (this.activePort == in_threat_info) {
                in_threat_info_value = ((threat_info)value);
                return;
            }
            return;
        }
    }

    @Override
    protected void deltaInternalFunc() {
        if (this.phase.getName().equals(IDENTIFICATION.getName())) {
           this.phase = WAIT;
        }
    }

    @Override
    protected void lambdaFunc() {
        if (this.phase.getName().equals(IDENTIFICATION.getName())) {
            this.out_target_info.send(in_threat_info_value);
        }
    }
}
