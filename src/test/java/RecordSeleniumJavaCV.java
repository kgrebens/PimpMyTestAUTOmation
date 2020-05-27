import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.OpenCVFrameConverter;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.imageio.ImageIO;


/**
 *
 * https://github.com/bytedeco/javacv
 */
public class RecordSeleniumJavaCV {

    // The WebDriver is a tool for writing automated tests of websites.
    FirefoxDriver driver;

    public static boolean videoComplete=false;
    public static String inputImageDir= "videos" + File.separator + "inputImgFolder"+File.separator;
    public static String inputImgExt="png";
    public static String outputVideoDir= "videos" + File.separator;
    public static String outputVideo;
    public static int counter=0;
    public static int imgProcessed=0;
    public static FFmpegFrameRecorder recorder=null;
    public static int videoWidth=1920;
    public static int videoHeight=1080;
    public static int videoFrameRate=3;
    public static int videoQuality=0; // 0 is the max quality
    public static int videoBitRate=9000;
    public static String videoFormat="mp4";
    public static int videoCodec=avcodec.AV_CODEC_ID_MPEG4;
    public static Thread t1=null;
    public static Thread t2=null;
    public static boolean isRegionSelected=false;
    public static int c1=0;
    public static int c2=0;
    public static int c3=0;
    public static int c4=0;

    /**
     * Explanation:
     * 1) videoComplete variables tells if user has stopped the recording or not.
     * 2) inputImageDir defines the input directory where screenshots will be stored which would be utilized by the video thread
     * 3) inputImgExt denotes the extension of the image taken for screenshot.
     * 4) outputVideo is the name of the recorded video file
     * 5) counter is used for numbering the screenshots when stored in input directory.
     * 6) recorder is used for starting and stopping the video recording
     * 7) videoWidth, videoFrameRate etc define output video param
     * 8) If user wants to record only a selected region then c1,c2,c3,c4 denotes the coordinate
     *
     * @return
     * @throws Exception
     */
    public static FFmpegFrameRecorder getRecorder() throws Exception
    {
        if(recorder!=null)
        {
            return recorder;
        }
        recorder = new FFmpegFrameRecorder(outputVideo,videoWidth,videoHeight);
        try
        {
            recorder.setFrameRate(videoFrameRate);
            recorder.setVideoCodec(videoCodec);
            recorder.setVideoBitrate(videoBitRate);
            recorder.setFormat(videoFormat);
            recorder.setVideoQuality(videoQuality); // maximum quality
            recorder.start();
        }
        catch(Exception e)
        {
            System.out.println("Exception while starting the recorder object "+e.getMessage());
            throw new Exception("Unable to start recorder");
        }
        return recorder;
    }

    /**
     * Explanation:
     * 1) This method is used to get the Recorder object.
     * 2) We create an object of FFmpegFrameRecorder named "Recorder" and then set all its video parameters.
     * 3) Lastly we start the recorder and then return the object.
     *
     * @return
     * @throws Exception
     */
    public static Robot getRobot() throws Exception
    {
        Robot r=null;
        try {
            r = new Robot();
            return r;
        } catch (AWTException e) {
            System.out.println("Issue while initiating Robot object "+e.getMessage());
            throw new Exception("Issue while initiating Robot object");
        }
    }

    /**
     * Explanation:
     * 1) Two threads are started in this module when user starts the recording
     * 2) First thread calls the takeScreenshot module which keeps on taking screenshot of user screen and saves them on local disk.
     * 3) Second thread calls the prepareVideo which monitors the screenshot created in step 2 and add them continuously on the video.
     *
     * @param r
     */
    public static void takeScreenshot(Robot r)
    {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle rec=new Rectangle(size);
        if(isRegionSelected)
        {
            rec=new Rectangle(c1, c2, c3-c1, c4-c2);
        }
        while(!videoComplete)
        {
            counter++;
            BufferedImage img = r.createScreenCapture(rec);
            try {
                ImageIO.write(img, inputImgExt, new File(inputImageDir+counter+"."+inputImgExt));
            } catch (IOException e) {
                System.out.println("Got an issue while writing the screenshot to disk "+e.getMessage());
                counter--;
            }
        }
    }

    /**
     * Explanation:
     * 1) If user has selected a region for recording then we set the rectangle with the coordinate value of c1,c2,c3,c4. Otherwise we set the rectangle to be full screen
     * 2) Now we run a loop until videoComplete is false (remains false until user press stop recording.
     * 3) Now we capture the region and write the same to the input image directory.
     * 4) So when user starts the recording this method keeps on taking screenshot and saves them into disk.
     *
     */
    public static void prepareVideo()
    {
        File scanFolder=new File(inputImageDir);
        while(!videoComplete)
        {
            File[] inputFiles=scanFolder.listFiles();
            try {
                getRobot().delay(500);
            } catch (Exception e) {
            }
            //for(int i=0;i<scanFolder.list().length;i++)
            for(int i=0;i<inputFiles.length;i++)
            {
                //imgProcessed++;
                addImageToVideo(inputFiles[i].getAbsolutePath());
                //String imgToAdd=scanFolder.getAbsolutePath()+File.separator+imgProcessed+"."+inputImgExt;
                //addImageToVideo(imgToAdd);
                //new File(imgToAdd).delete();
                inputFiles[i].delete();
            }
        }
        File[] inputFiles=scanFolder.listFiles();
        for(int i=0;i<inputFiles.length;i++)
        {
            addImageToVideo(inputFiles[i].getAbsolutePath());
            inputFiles[i].delete();
        }
    }

    /**
     * Explanation:
     * 1) cvLoadImage is used to load the image passed as argument
     * 2) We call the convert method to convert the image to frame which could be used by the recorder
     * 3) We pass the frame obtained in step 2 and add the same in the recorder by calling the record method.
     *
     * @return
     */
    public static OpenCVFrameConverter.ToIplImage getFrameConverter()
    {
        OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
        return grabberConverter;
    }

    /**
     * Explanation:
     * 1) We start a loop which will run until video complete is set true (done only when user press stop recording)
     * 2) We keep on monitoring the input  Image directory
     * 3) We traverse each file found in the input image directory and add those images to video using the addImageToVideo method. After the image has been added we delete the image
     * 4) Using the loop in step1 we keep on repeating step 2 and 3 so that each image gets added to video. We added a delay of 500ms so that this module does not picks a half created image from the takeScreenshot module
     * 5) When user press stop recording the loop gets broken. Now we finally traverse the input image directory and add the remaining images to video.
     *
     * @param imgPath
     */
    public static void addImageToVideo(String imgPath)
    {
        try {
            getRecorder().record(getFrameConverter().convert(cvLoadImage(imgPath)));
        } catch (Exception e) {
            System.out.println("Exception while adding image to video "+e.getMessage());
        }
    }

    /**
     * Explanation:
     * 1) We make a JFrame with the button for staring and stopping the recording. One more button is added for allowing user to record only a selected portion of screen
     * 2) If user clicks to select only certain region then we call a class CropRegion method getImage which helps in retrieving the coordinate of the region selected by user and update the same in variable c1,c2,c3,c4
     * 3) If user clicks on start recording then startRecording method is called
     * 4) If user clicks on stoprecording then stopRecording method is called
     */
    @BeforeEach
    public void beforeTest() {

        System.out.println("this.screenRecorder.start()");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp  = dateFormat.format(new Date());

        outputVideo= outputVideoDir + "recording_" + timestamp + ".mp4";

        try {
            t1=new Thread()
            {
                public void run() {
                    try {
                        takeScreenshot(getRobot());
                    } catch (Exception e) {
                        System.out.println("Cannot make robot object, Exiting program "+e.getMessage());
                        System.exit(0);
                    }
                }
            };
            t2=new Thread()
            {
                public void run() {
                    prepareVideo();
                }
            };
            t1.start();
            t2.start();
            System.out.println("Started recording at "+new Date());


        } catch (Exception e) {
            System.out.println("screenRecorder.start " + e.getMessage());
        }
    }

    @AfterEach
    public void afterTest()  {

        System.out.println("this.screenRecorder.stop()");

        try {
            videoComplete=true;
            System.out.println("Stopping recording at "+new Date());
            t1.join();
            System.out.println("Screenshot thread complete");
            t2.join();
            System.out.println("Video maker thread complete");
            getRecorder().stop();
            System.out.println("Recording has been saved successfully at "+new File(outputVideo).getAbsolutePath());

        } catch (Exception e) {
            System.out.println("screenRecorder.stop " + e.getMessage());
        }
    }

    @Test
    public void test_Oversized() {

        // GeckoDriver is a connecting link to the Firefox browser for your scripts in Selenium.
        System.setProperty("webdriver.gecko.driver", ".\\libs\\geckodriver.exe");

        // create driver
        driver = new FirefoxDriver();

        // Maximize the window
        driver.manage().window().maximize();

        // Aufruf der Seite
        driver.get("https://60tools.com/de/tool/bmi-calculator");

        // Eingabe der Daten in die Textfelder
        driver.findElement(By.name("weight")).sendKeys("180");
        driver.findElement(By.name("size")).sendKeys("200");
        driver.findElement(By.name("age")).sendKeys("20");

        // Auswahl des Geschlechts
        WebElement gender = driver.findElement(By.name("sex"));
        gender.sendKeys("Männlich");
        gender.sendKeys(Keys.RETURN);

        // Starten der Berechnung per Klick auf den Button
        WebElement button = driver.findElement(By.xpath("//*[@id=\"toolForm\"]/table/tbody/tr[5]/td[2]/input[2]"));
        button.click();

        // Vergleich des Ergebnisses
        String str2 = driver.findElement(By.xpath("//*[@id=\"content\"]/div[2]")).getText().substring(0,2);
        System.out.println("str2: " + str2.substring(0,2));
        assertTrue(str2.contains("45"));

        // Webdriver schliesst die Seite
        driver.close();

    }








}
