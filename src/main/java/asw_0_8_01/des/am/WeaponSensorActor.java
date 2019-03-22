package asw_0_8_01.des.am;

import asw_0_8_01.om.WeaponSensorActorOm;
import asw_0_8_01.portMsgType.entity_info;
import asw_0_8_01.portMsgType.env_info;
import asw_0_8_01.portMsgType.threat_info;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class WeaponSensorActor extends AtomicModelBase<WeaponSensorActorOm> {

    public InputPortBase<entity_info> in_entity_info;
    public InputPortBase<env_info> in_env_info;
    public InputPortBase<Boolean> in_engage_result;
    public InputPortBase<String> in_response;

    public OutputPortBase<String> out_request;
    public OutputPortBase<threat_info> out_threat_info;

    public WeaponSensorActor(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public WeaponSensorActor(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_entity_info = new InputPortBase<entity_info>(this);
        in_env_info = new InputPortBase<env_info>(this);
        in_engage_result = new InputPortBase<Boolean>(this);
        in_response = new InputPortBase<String>(this);

        out_request = new OutputPortBase<String>(this);
        out_threat_info = new OutputPortBase<threat_info>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new WeaponSensorActorOm();
    }

    @Override
    protected void constructPhase() {

    }

    @Override
    protected void deltaExternalFunc(Object value) {

    }

    @Override
    protected void deltaInternalFunc() {

    }

    @Override
    protected void lambdaFunc() {

    }
}
