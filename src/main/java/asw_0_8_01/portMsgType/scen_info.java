package asw_0_8_01.portMsgType;

import devs.core.PortTypeBase;
import lombok.Data;

@Data
public class scen_info extends PortTypeBase {
   private String resetInfo;  // "Fleet"  "Submarine"
}
