package io.github.lemgrb.testtemplates.e2eweb.utilities;

import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

/**
 * Utility class to take screenshots.
 */
@Slf4j
public class VideoRecorder {

  private static ScreenRecorder screenRecorder;
  private ProjectProperties properties;
  private String recordingsFolder;
  private String featureName;
  private String scenarioName;
  private static VideoRecorder single_instance;

  public VideoRecorder(ProjectProperties properties, String featureName, String scenarioName)
          throws IOException, AWTException {
    this.properties = properties;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String date = format.format(new Date());
    log.info("▒▒▒ RECORDING_FOLDER: " + properties.getProperties()
            .getProperty("RECORDING_FOLDER"));

    GraphicsConfiguration gconfig = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration();

    screenRecorder = new ScreenRecorder(gconfig,
            gconfig.getBounds(),
            new Format(MediaTypeKey, FormatKeys.MediaType.FILE, MimeTypeKey, MIME_AVI),
            new Format(MediaTypeKey, FormatKeys.MediaType.VIDEO, EncodingKey,
                    ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                    CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                    DepthKey, (int) 24, FrameRateKey, Rational.valueOf(15),
                    QualityKey, 1.0f,
                    KeyFrameIntervalKey, (int) (15 * 60)),
            new Format(MediaTypeKey, MediaType.VIDEO,
                    EncodingKey, "black", FrameRateKey, Rational.valueOf(30)), null,
            new File(properties.getProperties()
                    .getProperty("RECORDING_FOLDER").toUpperCase()
                    + "_" + date
              + "/" + featureName
              + "/" + scenarioName));
  }

  public void start() throws IOException {
    screenRecorder.start();
  }

  public void stop() throws IOException {
    screenRecorder.stop();
  }
}
