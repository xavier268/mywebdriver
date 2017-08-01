# mywebdriver : customized webdriver and utilities

These helper classes are designed to run either with a configured firefox/chrome/... 
selenium webdriver or with a "standalone server" hub/grid.

The grid is best run within a set of docker containers - see proposed scripts.

See the tests for demos on how to use

# mymongodriver : utilities to use mongo driver

This relies on the **synchroneous** version of the driver.
Use java8 facilities (such as CompletableFuture.async if needed for async access).

# Known bugs and limitations

* Trying to resize window to maximum size running on grid within a virtual frame buffer X11 server will crash the browser. 

# Configuration checklist before testing

* Launch grid container, preferably in a docker  sontainer (see provided script)
* lLunch mongo container (see provided script)
* Ensure geckodriver version in resources (currently 0.18.0) is compatible with local firefox version

# Developping a scrapper

Fisrts, look at the source code for demo examples - see AmazonFr and others ...

Then,

* Extend the WebpageBasic webpage implementation, by specifying the xpath strings.
* Specify the Document processing BiFunction, that will parse each selected webelement 
* Process the page
