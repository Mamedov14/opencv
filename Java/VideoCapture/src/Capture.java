import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.highgui.HighGui.waitKey;

public class Capture {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        VideoCapture capture = new VideoCapture(0);
        Mat image = new Mat();
        int index = 0;
        if (capture.isOpened()) {
            while (true) {
                capture.read(image);
                imshow("temp", image);
                index = waitKey(1);
                if (index == 27) {
                    break;
                }
            }
        }
    }


}
