# jfrog-assignment

# General info

* As requested, the application assumes newly installed Artifactory instance is running locally. 
   it's also assumed the admin password update after the initial artifactory setup was completed (as requested by the Artifactory UI).
* I've stored all the top configuration (endpoint URL, credentials, etc.) in a setup class. 
* I've used Spock test framework to define and execute the tests.
* I've created utils class, including methods that wrap the Artifactory APIs and other helper methods.
* Verified all tests passed using mvn test command.
