package utils

import lombok.Getter
import lombok.Setter

@Getter
@Setter
class BaseUtils  {

    static int getEpoch() {
        return Calendar.getInstance().toInstant().getEpochSecond()
    }


}
