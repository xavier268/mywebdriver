# Project mywebdriver : my customized webdriver and utilities, and scrapping demos using them

These helper classes are designed to run either with a locally configured firefox/chrome/... 
selenium webdriver or with a "standalone server" hub/grid.

The selenium grid is best run within a set of docker containers - see proposed scripts.

Options are available to skip loading images, which very significantly improves scrapping performance.

See the tests for demos on how to use

# General architecture principles

As often as possible, default behaviour and settings  are alreday implemented. You just need to customize the specificities for the website you wish to scrap. If you do not specify an option, sensible default behaviour should happen.

The Drivers class provides static metdos to obtain a selenium webdriver instance, that will be provided to the scrapper (generator).

Most scrappers (called 'generators', see AmazonFR example) will extend the WebpageBasic generator, by :

* defining the specific document parser (that translates a WebElement into a MongoDocument object)
* overriding the various xPath strings that caractérize the target scrapping (see javadoc for WebpageBasic)

Once you have your generator class for the site you want to scrap, scrapping involves 6 steps :

1. get a Webdriver by calling Drivers.getDriver with the relevant configuration (or reuse existing instance)
2. construct an instance a WebpageBasic or of the scrapping subclass you designed
3. set the desired specificities of your site :
* xpath string, if you did not subclass. These strings are used to specify what to look for as a document, what to expect (detect page load), is there a next page and how to get it, how to ensure previous page has disappeared before strating to parse again, etc ... (see javadoc and demos) 
* a custom documentParser that transforms the provided WebElement into a parsed MongoDocument oject.
* optionnally, a Limiter object, that will set limits in terms of elaŝed time, nbr of pages, nbr of documents, etc. (see LimiterBasic object, or build your own)
4. call the init() method to initiate the page (caution : all setting must be done BEFORE calling init()).
5. call the processDocuments method, to trigger the processing, specifying what you want to do with each parsed document, providing a document processor (or a lambda function). Default DocumentProcessor implementations are available to just print documents to standard output, or save them in a mongo collection.
6. optionnally, close the Webpage implementation (this will close the WebDriver), or close the Webdriver directly.


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
* The first build  will trigger installation of key binary extension components in your local directory (firefox xpi extensions and geckodriver).
* You may have to re-run the tests for them to pass, the first time the extensions are loaded and cached.
* Two artifacts will be generated, a "normal" executable jar without the dependencies, and a "shaded uberjar" executable. with all dependencies included. Multiple instances may be run in parallele in an headless context such as AWS/EC2 or AWS/Lamda for maximum throughput.

Testing the generated artifact can be done on the command line.
* launching the artifact with no parameter will print the list of available parameters. Currently testing the grid and the non-grid(local) implementations are available, that will take a screenshot of the google home page, and oextract the google logo from it.

# Developping a scrapper

Look at the various source code demo examples in the startup package.
