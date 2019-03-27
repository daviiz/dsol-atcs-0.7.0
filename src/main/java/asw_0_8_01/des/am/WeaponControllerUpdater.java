package asw_0_8_01.des.am;

import asw_0_8_01.om.WeaponControllerUpdaterOm;
import asw_0_8_01.portMsgType.threat_info;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class WeaponControllerUpdater extends AtomicModelBase<WeaponControllerUpdaterOm> {

    public InputPortBase<threat_info> in_threat_info;
    public OutputPortBase<threat_info> out_target_info;

    private Phase WAIT,IDENTIFICATION;

    private String entityName;
    private threat_info in_threat_info_value;

    public WeaponControllerUpdater(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public WeaponControllerUpdater(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_threat_info = new InputPortBase<threat_info>(this);
        out_target_info = new OutputPortBase<threat_info>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new WeaponControllerUpdaterOm();
        this.entityName = this.fullName.split("\\.")[2];
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
