import Setup.Init
import autherization.ChangePassword
import groovyx.net.http.HttpResponseException
import org.junit.Test
import utils.BaseUtils
import utils.UserUtils

class BasicUserMngTest extends Init {

    @Test
    def 'Create user'() {

        UserUtils.addUserAuthentication(USERNAME, PASSWORD)

        def user = UserUtils.generateUser(true)

        when: 'Creating user with valid parameters'
        def response = UserUtils.createUser(user)

        then: "server return status 201"
        assert response.status == 201

        cleanup: 'Delete user'
        //UserUtils.deleteUser(user.getName())
    }

    @Test
    def 'Delete user'() {
        def user = UserUtils.generateUser(true)
        when: 'Creating user with valid parameters'
        UserUtils.createUser(user)

        and: 'Delete user'
        UserUtils.deleteUser(user.getName())

        and: 'get user details'
        UserUtils.getUserDetails(user)

        then: "User Details not return (server return 404 code (not found)"
        HttpResponseException e = thrown(HttpResponseException)
        assert e.response.status == 404: 'response code should be 401 when you use wrong credentials'
    }

    @Test
    def 'Update user'() {
        def updatedEmail = "updatedemail@mail.com"
        def user = UserUtils.generateUser(true)

        when: 'Creating user with valid parameters'
        UserUtils.createUser(user)

        and: 'Updating user email'
        user.setEmail(updatedEmail)
        UserUtils.updateUser(user)

        and: 'get user details'
        def response = UserUtils.getUserDetails(user)
        def email = response.getData().email

        then: "User details should be updated"
        assert response.status == 200
        assert email == updatedEmail

        cleanup: 'Delete user'
        UserUtils.deleteUser(user.getName())
    }

    @Test
    def 'Retrieve users list'() {

        when: 'Creating user with valid parameters'
        def user = UserUtils.generateUser(true)
        UserUtils.createUser(user)

        and: 'Get user list'
        def listResponse = UserUtils.retrieveUserList()


        then: "User list return (including newly created user)"
        assert listResponse.getData().toString().contains(user.getName())

        cleanup: 'Delete user'
        UserUtils.deleteUser(user.getName())
    }


    @Test
    def 'Use invalid authentication credentials'() {
        def INVALID_PASSWORD = "1234"

        when: 'Updating user authentication with invalid password'
        UserUtils.addUserAuthentication(USERNAME,INVALID_PASSWORD)

        and: 'Trying to create user with invalid credentials'
        UserUtils.createUser(UserUtils.generateUser(true))

        then: 'server returns 401 code (unauthorized)'
        HttpResponseException e = thrown(HttpResponseException)
        assert e.response.status == 401: 'response code should be 401 wrong authentication credentials are used'

        cleanup:
        // Revert to original admin credentials
        UserUtils.addUserAuthentication(USERNAME,PASSWORD)
    }


    // Less important tests
    // ******************************************

    @Test
    def 'Update user password by admin'() {

        when: 'Creating user'
        def user = UserUtils.generateUser(true)
        UserUtils.createUser(user)

        sleep(1000)

        and: 'Updating user password'
        def newPassword =  BaseUtils.getEpoch()

        ChangePassword changePassword = new ChangePassword()
        changePassword.setUserName(user.getName())
        changePassword.setOldPassword(user.getPassword())
        changePassword.setNewPassword1(newPassword)
        changePassword.setNewPassword2(newPassword)

        def updatedResponse = UserUtils.updateUserPassword(changePassword)

        then: 'server returns 200 code (ok)'
        assert updatedResponse.status == 200: 'response code should be 200 when tried to update user password'

        cleanup: 'Delete user'
        UserUtils.deleteUser(user.getName())
    }

    @Test
    def 'Execute method by regular user which requires admin user'() {

        when: 'Creating user'
        def user = UserUtils.generateUser(false)
        UserUtils.createUser(user)

        and: 'Updating client authorization to new user credentials'
        UserUtils.addUserAuthentication(user.getName(), user.getPassword())

        and: 'Trying to create a user (method requires admin user'
        UserUtils.createUser(user)

        then: 'server returns 403 code (forbidden)'
        HttpResponseException e = thrown(HttpResponseException)
        assert e.response.status == 403: 'response code should be 403 when user not authorize to execute a given method'

        cleanup:
        // Revert user authentication credentials
        UserUtils.addUserAuthentication(USERNAME,PASSWORD)

        UserUtils.deleteUser(user.getName())
    }

}



