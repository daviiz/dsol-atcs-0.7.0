package asw_0_8_01.portMsgType;

import devs.core.PortTypeBase;
import lombok.Data;


@Data
public class entity_info extends PortTypeBase {
    private String resetInfo;//  "Decoy1" "Decoy2" "Torpedo"
}
