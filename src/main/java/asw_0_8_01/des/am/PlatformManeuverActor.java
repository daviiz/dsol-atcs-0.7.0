package asw_0_8_01.des.am;

import asw.soa.data.ViewData;
import asw_0_8_01.om.PlatformManeuverActorOm;
import asw_0_8_01.portMsgType.entity_info;
import asw_0_8_01.portMsgType.env_info;
import asw_0_8_01.portMsgType.move_result;
import asw_0_8_01.portMsgType.scen_info;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class PlatformManeuverActor extends AtomicModelBase<PlatformManeuverActorOm> {

    public InputPortBase<Boolean> in_engage_result;
    public InputPortBase<env_info> in_env_info;
    public InputPortBase<scen_info> in_scen_info;

    public InputPortBase<String> in_cmd_info;

    public OutputPortBase<Boolean> out_move_finished;
    public OutputPortBase<Boolean> out_fuel_exhasuted;
    public OutputPortBase<move_result> out_move_result;

    private ViewData viewData;

    public ViewData getViewData() {
        return viewData;
    }

    public void setViewData(ViewData viewData) {
        this.viewData = viewData;
    }

    public PlatformManeuverActor(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public PlatformManeuverActor(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_engage_result = new InputPortBase<Boolean>(this);
        in_env_info = new InputPortBase<env_info>(this);
        in_scen_info = new InputPortBase<scen_info>(this);

        in_cmd_info = new InputPortBase<String>(this);

        out_move_finished = new OutputPortBase<Boolean>(this);
        out_fuel_exhasuted = new OutputPortBase<Boolean>(this);
        out_move_result = new OutputPortBase<move_result>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new PlatformManeuverActorOm();
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
