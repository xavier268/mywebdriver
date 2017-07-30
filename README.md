# mywebdriver : customized webdriver and utilities

These helper classes are designed to run either with a configured firefox/chrome/... 
webdriver or with a "standalone server" hub/grid.

See the tests for demos on how to use

# mymongodriver : utilities to use mongo driver

This relies on the **synchroneous** version of the driver.
Use java8 facilities (such as CompletableFuture.async if needed for async access).

# Known bugs and limitations

* Trying to resize window to maximum size will crash the browser when running on grid. 
It seems to work fine on a local browser ...

# Configuration checklist before testing

* Launch grid container in docker (use provided script)
* launch mongo conatianer (use provided script)
* ensure latest geckodriver can be found in the Drivers.Confic.getcckoDriver 
location. It should be executable, and the path should be in the $PATH variable.

