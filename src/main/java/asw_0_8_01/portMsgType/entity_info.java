package asw_0_8_01.portMsgType;

import devs.core.PortTypeBase;
import lombok.Data;
import nl.tudelft.simulation.language.d3.CartesianPoint;


@Data
public class entity_info extends PortTypeBase {
    private String resetInfo;//  "Decoy1" "Decoy2" "Torpedo"
    private CartesianPoint src;
    private CartesianPoint des;
}
