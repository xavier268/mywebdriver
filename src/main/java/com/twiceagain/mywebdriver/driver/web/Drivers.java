/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.driver.web;

import com.google.common.io.Files;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Driver factory class. // TODO - provide window resize to max
 * vertical/horizontal size and avoid scrolling ?
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
                // LoadGeckodriver if needed
                Config.installGeckoDriver();
                // Set geckodriver full absolute path
                System.setProperty("webdriver.gecko.driver", Config.geckodriver_absolute_path);
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
     * Takes a (full view) Base64 screenshoot from the provided webdriver.
     *
     * @param wd - the driver to shoot.
     * @return Base64 string. You can use it directly to display inline images.
     */
    public static String screenshot2B64(WebDriver wd) {
        return ((TakesScreenshot) wd).getScreenshotAs(OutputType.BASE64);
    }

    /**
     * Screen shot of selected element only.
     *
     * @param wd
     * @param ele
     * @return
     * @throws IOException
     */
    public static File screenshot2TempFile(WebDriver wd, WebElement ele)
            throws IOException {
        Drivers.scrollElementIntoView(wd, ele);
        File fi = ((TakesScreenshot) wd).getScreenshotAs(OutputType.FILE);
        BufferedImage img = ImageIO.read(fi);
        // Get the location of element on the page
        Point point = ele.getLocation();

        // Get width and height of the element
        int eleWidth = ele.getSize().getWidth();
        int eleHeight = ele.getSize().getHeight();

        // Crop the entire page screenshot to get only element screenshot
        BufferedImage eleScreenshot = img.getSubimage(point.getX(), point.getY(),
                eleWidth, eleHeight);
        ImageIO.write(eleScreenshot, "png", fi);
        return fi;
    }

    /**
     * ScreenShot into a temporary file. The temp file will disappear when the
     * jvm stops.
     *
     * @param wd
     * @return
     */
    public static File screenshot2TempFile(WebDriver wd) {
        return ((TakesScreenshot) wd).getScreenshotAs(OutputType.FILE);
    }

    /**
     * ScreenShot to the provided file as PNG.
     *
     * @param wd
     * @param toFileName - file name to copy to ...
     * @return - fileName used.
     */
    public static String screenshot2File(WebDriver wd, File toFileName) {
        try {
            File fi = screenshot2TempFile(wd);
            Files.move(fi, toFileName);

            return toFileName.getAbsolutePath();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * ScreenShot to the provided file as PNG.
     *
     * @param wd
     * @param ele
     * @param toFileName - file name to copy to ...
     * @return - fileName used.
     */
    public static String screenshot2File(WebDriver wd, WebElement ele, File toFileName) {
        try {
            File fi = screenshot2TempFile(wd, ele);
            Files.move(fi, toFileName);

            return toFileName.getAbsolutePath();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * ScreenShot to the provided file as PNG.
     *
     * @param wd
     * @param fileName
     * @return - fileName used.
     */
    public static String screenshot2File(WebDriver wd, String fileName) {
        return screenshot2File(wd, new File(fileName));
    }

    /**
     * ScreenShot to the provided file as PNG.
     *
     * @param wd
     * @param ele
     * @param fileName
     * @return - fileName used.
     */
    public static String screenshot2File(WebDriver wd, WebElement ele, String fileName) {
        return screenshot2File(wd, ele, new File(fileName));
    }

    /**
     * Driver configuration object.
     */
    public static class Config {

        @Override
        public String toString() {
            return "Config{" + "useGrid=" + useGrid + ", gridUrl=" + gridUrl
                    + ", noImage=" + noImage + ", noCSS=" + noCSS
                    + ", noCookie=" + noCookie + ", noFlash=" + noFlash
                    + ", noJava=" + noJava + ", noJs=" + noJs
                    + ", noSilver=" + noSilver + ", width=" + width
                    + ", height=" + height + '}';
        }

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
        public static final String QUICKJAVA_URL
                = "https://addons.mozilla.org/firefox/downloads/latest/quickjava/addon-1237-latest.xpi";
        /**
         * Path to loaded quickJava file. Do not set manually !
         */
        protected static String quickJavaPath = null;
        /**
         * Path to a geckodriver, compatible with local firefox version AND
         * selenium version.
         */
        public static String geckodriver_absolute_path = null;

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
         * Defines browser window sizes
         */
        public int width = 1280, height = 800;

        /**
         * Construct default configuration.
         */
        public Config() {

            loadFirefoxQuickJavaExtension();

        }

        /**
         * Load (and cache) the quickJava Firefox extension.
         *
         * @return
         */
        protected static String loadFirefoxQuickJavaExtension() {

            try {
                final String fileName = "quickjava.xpi";

                // If already loaded, do nothing.
                if (Config.quickJavaPath != null || new File(fileName).exists()) {
                    Config.quickJavaPath = fileName;
                    LOG.log(Level.INFO, "Already loaded xpi file : {0}", Config.quickJavaPath);
                    return Config.quickJavaPath;
                }

                URL url = new URL(Config.QUICKJAVA_URL);
                ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                try (FileOutputStream fos = new FileOutputStream(fileName)) {
                    fos.getChannel().transferFrom(rbc, 0, 1_000_000L);
                    Config.quickJavaPath = fileName;
                }
                LOG.log(Level.INFO, "Loaded xpi file : {0}", fileName);
                return Config.quickJavaPath;
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
                return null;
            }
        }

        /**
         * Install the geckoDriver executable for LOCAL firefox instances. The
         * executable file is provided as a resource. 
         *
         */
        protected static void installGeckoDriver() {
            
            final File  gf = new File("geckodriver");

            // If already loaded, do nothing, excpet updating the path.
            if ( gf.exists()) {
                LOG.log(Level.INFO, "Already loaded geckodriver file : {0}", geckodriver_absolute_path);
                if(geckodriver_absolute_path == null ) {                    
                    geckodriver_absolute_path = gf.getAbsolutePath();
                    LOG.log(Level.INFO, "Updated geckodriver installation path : {0}", geckodriver_absolute_path);
                }
                return;
            } 
            
            LOG.info("No Geckodriver available yet - installing from resources ...");

            byte[] buffer = new byte[128_000]; // 128K buffer
            int len;

            try (
                    InputStream in = Drivers.class.getClassLoader().getResourceAsStream("geckodriver-0.18.0");
                    OutputStream out = new FileOutputStream(gf)) {

                len = in.read(buffer);
                while (len > 0) {
                    out.write(buffer, 0, len);
                    len = in.read(buffer);
                }
            } catch (IOException ex) {
                Logger.getLogger(Drivers.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Make file executable.
            gf.setExecutable(true, true);
            geckodriver_absolute_path = gf.getAbsolutePath();
            LOG.log(Level.INFO, "Geckodriver installed in {0}", geckodriver_absolute_path);

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
            fp.setPreference("extensions.thatoneguydotnet.QuickJava.curVersion", "2.1.0");  // Prevents loading the 'thank you for installing screen'
            if (noImage) {
                //  Turns images off
                fp.setPreference("extensions.thatoneguydotnet.QuickJava.startupStatus.Images", 2);
                // Turns animated images off
                fp.setPreference("extensions.thatoneguydotnet.QuickJava.startupStatus.AnimatedImage", 2);
            }

            if (noCSS) {
                fp.setPreference("extensions.thatoneguydotnet.QuickJava.startupStatus.CSS", 2);
            }

            if (noCookie) {
                fp.setPreference("extensions.thatoneguydotnet.QuickJava.startupStatus.Cookies", 2);
            }
            if (noFlash) {
                fp.setPreference("extensions.thatoneguydotnet.QuickJava.startupStatus.Flash", 2);
            }
            if (noJava) {
                fp.setPreference("extensions.thatoneguydotnet.QuickJava.startupStatus.Java", 2);
            }
            if (noJs) {
                fp.setPreference("extensions.thatoneguydotnet.QuickJava.startupStatus.JavaScript", 2);
            }
            if (noSilver) {
                fp.setPreference("extensions.thatoneguydotnet.QuickJava.startupStatus.Silverlight", 2);
            }

            desCap.setCapability(FirefoxDriver.PROFILE, fp);
            return desCap;

        }

    }

    /**
     * Visualy mark a list of elements on the webpage.
     *
     * @param wd
     * @param lwe
     * @param message
     * @param backrgba
     * @param frontrgba
     */
    public static void highlightElements(WebDriver wd,
            List<WebElement> lwe,
            String message,
            String backrgba,
            String frontrgba) {
        int i = 0;
        for (WebElement we : lwe) {
            i++;
            highlightElement(wd, we, String.format("#%d %s", i, message, frontrgba, backrgba));
        }
    }

    /**
     * Highlight selected elements.
     *
     * @param wd
     * @param lwe
     * @param message
     */
    public static void highlightElements(WebDriver wd,
            List<WebElement> lwe,
            String message) {
        int i = 0;
        for (WebElement we : lwe) {
            i++;
            highlightElement(wd, we, String.format("#%d %s", i, message));
        }

    }

    /**
     * Visualy mark an element on the webPage.
     *
     * @param wd
     * @param we
     * @param innerHtml - content of message div
     */
    public static void highlightElement(
            WebDriver wd,
            WebElement we,
            String innerHtml) {

        highlightElement(wd, we, innerHtml, "rgba(64,64,64,0.2)", "rgba(255,0,0,1.0)");
    }

    /**
     * Visualy mark an element on the webPage.
     *
     * @param wd
     * @param we
     * @param innerHtml - content of message dv
     * @param backrgba - background color string : try 'red' or
     * 'rgba(64,0,0,0.3)'
     * @param frontrgba - front color for border and text
     */
    public static void highlightElement(
            WebDriver wd,
            WebElement we,
            String innerHtml,
            String backrgba,
            String frontrgba) {

        if (wd == null || we == null) {
            return;
        }

        // scrollElementIntoView(wd, we);
        // Get element size and location
        int left = we.getLocation().x;
        int top = we.getLocation().y;
        int width = we.getSize().width;
        int height = we.getSize().height;

        final String css = String.format("color:%s;"
                + "background-color:%s;"
                + "border-style:dashed;border-width:2px;border-color:%s; "
                + "position:absolute;"
                + "top:%dpx;left:%dpx;"
                + "width:%dpx;height:%dpx;"
                + "z-index:1000;",
                frontrgba, backrgba, frontrgba,
                top, left, width, height);

        final String script = String.format("elem = document.createElement('div');"
                + "elem.innerHTML='%s';"
                + "elem.setAttribute('style','%s');"
                + "document.body.appendChild(elem);",
                innerHtml, css);

        ((JavascriptExecutor) wd).executeScript(script);
    }

    /**
     * Scroll to make element visible.
     *
     * @param wd
     * @param we
     */
    public static void scrollElementIntoView(WebDriver wd, WebElement we) {
        if (wd == null || we == null) {
            return;
        }
        String script = "arguments[0].scrollIntoView();";
        ((JavascriptExecutor) wd).executeScript(script, we);
    }

    /**
     * Get an estimate of the document page height (beyond the viewport).
     * Caution : this seems to crash the browser when run in Grid mode ...
     *
     * @param wd
     * @return
     */
    public static int getPageHeigth(WebDriver wd) {
        String script = "var body = document.bodyvar;"
                + "html = document.documentElement;"
                + "return Math.max( document.body.scrollHeight, document.body.offsetHeight,"
                + "document.documentElement.clientHeight, document.documentElement.scrollHeight,"
                + "document.documentElement.offsetHeight );";

        Long res = (Long) ((JavascriptExecutor) wd).executeScript(script);
        return res.intValue();
    }

    /**
     * Adjust page height to ensure there are no vertical scrollbars. Caution :
     * this seems to crash the browser when run in Grid mode ...
     *
     * @param wd
     */
    public static void adjustPageHeight(WebDriver wd) {
        int ww = wd.manage().window().getSize().width;
        int hh = wd.manage().window().getSize().height;
        hh = Math.max(hh, Drivers.getPageHeigth(wd) + 200);
        wd.manage().window().setSize(new Dimension(ww, hh));
    }
}
