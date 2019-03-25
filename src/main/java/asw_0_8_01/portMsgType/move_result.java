package asw_0_8_01.portMsgType;

import devs.core.PortTypeBase;
import lombok.Data;
import nl.tudelft.simulation.language.d3.CartesianPoint;

@Data
public class move_result extends PortTypeBase {
    private CartesianPoint position;
    private String name;
    private double remainingTime;
    private int cmp = 0;
    private double detectRange = Double.POSITIVE_INFINITY;
    private double speed = 0;

    public move_result(){
        this.name = "0";
        remainingTime = Double.POSITIVE_INFINITY;
    }
}
