package asw_0_8_01.portMsgType;

import devs.core.PortTypeBase;
import lombok.Data;
import nl.tudelft.simulation.language.d3.CartesianPoint;

@Data
public class threat_info extends PortTypeBase {
    private CartesianPoint position;
    private String name;
    private int cmp = 0;
    public threat_info(){
        this.name = "0";
    }
}
