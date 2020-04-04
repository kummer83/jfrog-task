package Setup

import groovyx.net.http.RESTClient
import spock.lang.Specification
import utils.UserUtils

class Init extends Specification {

    public static final String TEST_URL = "http://localhost:8083"
    public static RESTClient client = new RESTClient(TEST_URL)

    // Admin credentials (after updating default password) - used to authenticate.
    public static String USERNAME = "admin"
    public static String PASSWORD = "password"

    def setupSpec() {

        // Add authentication header to client
        UserUtils.addUserAuthentication(USERNAME, PASSWORD)
    }

}


