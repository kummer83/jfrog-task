package autherization

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter

@AllArgsConstructor
@Setter
@Getter
class ChangePassword {

    String userName
    String oldPassword
    int newPassword1
    int newPassword2
}
