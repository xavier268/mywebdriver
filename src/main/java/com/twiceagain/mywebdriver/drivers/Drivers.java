/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.drivers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Driver factory class.
 *
 * @author xavier
 */
public class Drivers {

    private static final Logger LOG = Logger.getLogger(Drivers.class.getName());

    /**
     * Get a driver with the specified config.
     *
     * @param config
     * @return
     */
    public static WebDriver getDriver(final Config config) {
        WebDriver wd;
        try {
            // Local firefox instance
            if (!config.useGrid) {
                // Set geckodriver full absolute path
                System.setProperty("webdriver.gecko.driver", Config.geckoDriverPath);
                wd = new FirefoxDriver(config.getDesiredCapabilities());
                // Grid instance
            } else {

                wd = new RemoteWebDriver(new URL(config.gridUrl), config.getDesiredCapabilities());

            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }

        // Adjust window size
        wd.manage().window().setSize(new Dimension(config.width, config.height));
        return wd;
    }

    /**
     * Get the driver with the default config.
     *
     * @return
     */
    public static WebDriver getDriver() {
        return getDriver(new Config());
    }

    /**
     * Driver configuration object.
     */
    public static class Config {

        /**
         * Should we use grid or local instance ?
         */
        public boolean useGrid = false;
        /**
         * Which grid url to use : default to "http://localhost:4444/wd/hub"
         */
        public String gridUrl = "http://localhost:4444/wd/hub";
        /**
         * Load QuickJava plugin. Version 2.1.0 as of may 8th, 2017
         *
         */
        public static String quickJavaUrl = "https://addons.mozilla.org/firefox/downloads/latest/quickjava/addon-1237-latest.xpi";
        /**
         * Path to loaded quickJava file. Do not set manually !
         */
        protected static String quickJavaPath = null;
        /**
         * Path to a geckodriver, compatible with lovcal firefox version AND
         * selenium version.
         */
        public static String geckoDriverPath = "/home/xavier/bin/geckodriver";
        /**
         * Do not display images ?
         */
        public boolean noImage = false;
        /**
         * Do not display css ?
         */
        public boolean noCSS = false;
        /**
         * Do not accept cookies ?
         */
        public boolean noCookie = false;
        /**
         * Do not accept flash ?
         */
        public boolean noFlash = true;
        /**
         * Do not accept java ?
         */
        public boolean noJava = true;
        /**
         * Do not accept javascript ?
         */
        public boolean noJs = false;
        /**
         * Do not accept silverLight ?
         */
        public boolean noSilver = true;
        /**
         * Defines browser woindow sizes
         */
        public int width = 1280, height = 800;

        /**
         * Construct default configuration.
         */
        public Config() {
            try {
                loadFirefoxQuickJavaExtension();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
                System.exit(0);
            }
        }

        /**
         * Load (and cache) the quickJava Firefox extension.
         *
         * @return
         * @throws MalformedURLException
         * @throws IOException
         */
        protected static String loadFirefoxQuickJavaExtension() throws MalformedURLException, IOException {

            final String fileName = "quickjava.xpi";

            // If already loaded, do nothing.
            if (Config.quickJavaPath != null || new File(fileName).exists()) {
                Config.quickJavaPath = fileName;
                LOG.log(Level.INFO, "Already loaded xpi file : {0}", Config.quickJavaPath);
                return Config.quickJavaPath;
            }

            URL url = new URL(Config.quickJavaUrl);
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.getChannel().transferFrom(rbc, 0, 1_000_000L);
            Config.quickJavaPath = fileName;
            LOG.log(Level.INFO, "Loaded xpi file : {0}", fileName);
            return Config.quickJavaPath;
        }

        /**
         * Predefined config.
         *
         * @return
         */
        public static Config defaultLocalFirefox() {
            Config conf = new Config();
            conf.useGrid = false;
            return conf;
        }

        /**
         * Predefined config.
         *
         * @return
         */
        public static Config defaultGridFirefox() {
            Config conf = new Config();
            conf.useGrid = true;
            return conf;
        }

        /**
         * Get the desired capabilities from the config object.
         *
         * @return
         * @throws IOException
         */
        protected DesiredCapabilities getDesiredCapabilities() throws IOException {

            DesiredCapabilities desCap = DesiredCapabilities.firefox();

            // Setting firefox profile to optimize load - see about:config in a firefox window
            FirefoxProfile fp = (new FirefoxProfile());
            //fp.setPreference("webdriver.load.strategy", "fast");
            /**
             * IMPORTANT - Firefox has now frozen the
             * 'permissions.default.image' parameter to the 1 value. It CANNOT
             * be changed anymore. We need to use an extension or Chrome See :
             * http://stackoverflow.com/questions/31571726/cant-turn-off-images-in-selenium-firefox
             *
             */
            //fp.setPreference("permissions.default.image.xavier", "PreventAutomaticImageLoading"); //marquer le passage ...
            //fp.setPreference("permissions.default.image", 2); // default is 1:load image automatically 2: block all images

            fp.addExtension(new File(quickJavaPath));
            fp.setPreference("thatoneguydotnet.QuickJava.curVersion", "2.0.6.1");  // Prevents loading the 'thank you for installing screen'
            if (noImage) {
                //  Turns images off
                fp.setPreference("thatoneguydotnet.QuickJava.startupStatus.Images", 2);
                // Turns animated images off
                fp.setPreference("thatoneguydotnet.QuickJava.startupStatus.AnimatedImage", 2);
            }

            if (noCSS) {
                fp.setPreference("thatoneguydotnet.QuickJava.startupStatus.CSS", 2);
            }

            if (noCookie) {
                fp.setPreference("thatoneguydotnet.QuickJava.startupStatus.Cookies", 2);
            }
            if (noFlash) {
                fp.setPreference("thatoneguydotnet.QuickJava.startupStatus.Flash", 2);
            }
            if (noJava) {
                fp.setPreference("thatoneguydotnet.QuickJava.startupStatus.Java", 2);
            }
            if (noJs) {
                fp.setPreference("thatoneguydotnet.QuickJava.startupStatus.JavaScript", 2);
            }
            if (noSilver) {
                fp.setPreference("thatoneguydotnet.QuickJava.startupStatus.Silverlight", 2);
            }

            desCap.setCapability(FirefoxDriver.PROFILE, fp);
            return desCap;

        }

    }

}
