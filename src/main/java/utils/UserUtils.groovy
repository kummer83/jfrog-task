package utils

import Security.User
import autherization.ChangePassword
import static Setup.Init.*

class UserUtils extends BaseUtils {

    public static final String JSON_CONTENT_TYPE = "application/json"
    public static final String API_PREFIX = "/artifactory/api/security/users"

    static User generateUser(boolean isAdmin) {
        def epoch = getEpoch()
        def userName = "user_" + epoch

        User user = new User()
        user.setName("user_" + epoch)
        user.setPassword(epoch as String)
        user.setRetypePassword(epoch as String)
        user.setAdmin(isAdmin)
        user.setEmail(userName + "@gmail.com")

        return user
    }

    static def createUser(User user) {
        return client.put(path: API_PREFIX + "/" + user.getName(), body: user, contentType:JSON_CONTENT_TYPE)
    }

    static def updateUser(User user) {
        return client.post(path: API_PREFIX + "/" + user.getName(), body: user, contentType: JSON_CONTENT_TYPE)
    }

    static def deleteUser(String userName) {
        return client.delete(path: API_PREFIX + "/" + userName, contentType: "text/plain")
    }

    static def getUserDetails(User user) {
        return client.get(path: API_PREFIX + "/" + user.getName(), contentType: JSON_CONTENT_TYPE)
    }

    static def retrieveUserList() {
        return client.get(path: API_PREFIX, contentType: JSON_CONTENT_TYPE)
    }

    static def updateUserPassword(ChangePassword changePassword) {
        return client.post(path: API_PREFIX + '/authorization/changePassword', body: changePassword, requestContentType: "application/json")
    }

    static void addUserAuthentication(String userName, String password) {
        client.headers['Authorization'] = "Basic ${"$userName:$password".bytes.encodeBase64()}";

    }


}
