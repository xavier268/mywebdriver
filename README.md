# Project mywebdriver : my customized webdriver and utilities, and scrapping demos using them

These helper classes are designed to run either with a locally configured firefox/chrome/... 
selenium webdriver or with a "standalone server" hub/grid.

The selenium grid is best run within a set of docker containers - see proposed scripts.

Options are available to skip loading images, which very significantly improves scrapping

See the tests for demos on how to use

# General architecture principles





# Known bugs and limitations

* Trying to resize window to maximum size running on grid within a virtual frame buffer X11 server will crash the browser. 
* Chrome is untested. Prefer Firefox.
* Windows and MacOs should normally run, but are untested - prefer linux.
* The mymongodriver relies on the **synchroneous** version of the driver. Use java8 facilities (such as CompletableFuture.async if needed for async loading).

# Requirements and configuration 

* Best use in a linux box (or virtualbox) and docker isntalled
* Launch grid container, preferably in a docker  sontainer (see provided script)
* Launch mongo container (see provided script)
* Ensure geckodriver version in resources (currently 0.18.0) is compatible with your local firefox version

Then, build with maven.
* The first run will trigger installation of key components in your local directory (firefox extensions, geckodriver).
* You may have to re-run the tests for them to pass.
* Two artifacts will be generated, a "nomral" executable jar, and a "shaded uberjar" executable. Multiple instances may be run in parallele in an headless context such as AWS/EC2 for maximum throughput.

# Developping a scrapper

Look at the source code for demo examples - see AmazonFr and others ...

Then,

* Extend the WebpageBasic webpage implementation, by specifying the xpath strings of the site you want to scrap.
* Specify the specific Dparsing class to transform each selected webelemnt into a MongoDocument (see examples)
* Process the page
