package Security

import lombok.AllArgsConstructor
import lombok.Getter

import lombok.Setter


@AllArgsConstructor
@Setter
@Getter
class User {

    String name
    String email
    String password
    String retypePassword
    UserGroups userGroup
    boolean admin

}
