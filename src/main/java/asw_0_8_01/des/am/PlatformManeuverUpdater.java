package asw_0_8_01.des.am;

import asw_0_8_01.om.PlatformManeuverUpdaterOm;
import devs.core.AtomicModelBase;
import devs.core.InputPortBase;
import devs.core.OutputPortBase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

public class PlatformManeuverUpdater extends AtomicModelBase<PlatformManeuverUpdaterOm> {

    public InputPortBase<String> in_move_cmd;

    public OutputPortBase<String> out_cmd_info;

    private String in_move_cmd_value;
    private String out_cmd_info_value;

    private Phase WAIT,INTERPRETATION;

    public PlatformManeuverUpdater(String modelName, CoupledModel.TimeDouble parentModel) {
        super(modelName, parentModel);
    }

    public PlatformManeuverUpdater(String modelName, DEVSSimulator.TimeDouble simulator) {
        super(modelName, simulator);
    }

    @Override
    protected void constructPort() {
        in_move_cmd = new InputPortBase<String>(this);

        out_cmd_info = new OutputPortBase<String>(this);
    }

    @Override
    protected void constructObjectModel() {
        this.om = new PlatformManeuverUpdaterOm();
        in_move_cmd_value = "0";
        out_cmd_info_value = "0";
    }

    @Override
    protected void constructPhase() {
        WAIT = new Phase("WAIT");   WAIT.setLifeTime(Double.POSITIVE_INFINITY);
        INTERPRETATION = new Phase("INTERPRETATION");    INTERPRETATION.setLifeTime(0.0);
        this.phase = WAIT;
    }

    @Override
    protected void deltaExternalFunc(Object value) {
        if(WAIT.getName().equals(this.phase.getName())){
            in_move_cmd_value = (String)value;
            this.phase = INTERPRETATION;
        }
    }

    @Override
    protected void deltaInternalFunc() {
        if(INTERPRETATION.getName().equals(this.phase.getName())){
            out_cmd_info_value = in_move_cmd_value;
            this.phase = WAIT;
        }
    }

    @Override
    protected void lambdaFunc() {
        if(INTERPRETATION.getName().equals(this.phase.getName())){
            if(out_cmd_info_value.equals("0"))
            {

            }else{
                /**
                 * out_cmd_info_value == in_move_cmd_value  :  EVASION,APPROACH,要么靠近，要么逃逸
                 */
                this.out_cmd_info.send(out_cmd_info_value);
            }

        }
    }
}
