import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.*;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class DetectColor {

    public static void main(String[] args) throws FrameGrabber.Exception {

        IplImage img;
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();

//      VideoCapture capture = new VideoCapture(1);

        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(1);
        grabber.start();

        Frame frame = grabber.grab();
        CanvasFrame canvasFrame = new CanvasFrame("Detect");
        canvasFrame.setCanvasSize(frame.imageWidth, frame.imageHeight);

        while (canvasFrame.isVisible() && (frame = grabber.grab()) != null) {
            img = converter.convert(frame);
            IplImage image = img.clone();
//          IplImage imgThreshold = hsvThreshold(image, cvScalar(10, 110, 90, 0), cvScalar(25, 255, 255, 0));
            IplImage imgHSV = cvCreateImage(cvGetSize(image), 8, 3);
            cvCvtColor(image, imgHSV, CV_BGR2HSV);
            IplImage imgThreshold = cvCreateImage(cvGetSize(image), 8, 1);
            cvInRangeS(imgHSV, cvScalar(10, 110, 90, 0), cvScalar(25, 255, 255, 0), imgThreshold);
            CvSeq contour = new CvSeq(null);
            CvMemStorage storage = CvMemStorage.create();
            cvFindContours(imgThreshold, storage, contour, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
            while (contour != null && !contour.isNull()) {
                if (contour.elem_size() > 0) {
                    CvBox2D box = cvMinAreaRect2(contour, storage);
                    if (box != null && box.size().width() * box.size().height() > 2200) {
                        circle(cvarrToMat(img), new Point(Math.round(box.center().x()), Math.round(box.center().y())),
                                15, RGB(255, 1, 0), FILLED, LINE_4, 0);
                    }
                }
                contour = contour.h_next();
            }
            canvasFrame.showImage(converter.convert(img));
//          canvasFrame.showImage(converter.convert(imgHSV));
//          canvasFrame.showImage(converter.convert(imgThreshold));
            try {
                canvasFrame.waitKey(30);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
//              Logger.getLogger(DetectColor.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        canvasFrame.dispose();
    }

    public static IplImage hsvThreshold(IplImage orgImg, CvScalar min, CvScalar max) {
        IplImage imgHSV = cvCreateImage(cvGetSize(orgImg), 8, 3);
        cvCvtColor(orgImg, imgHSV, CV_BGR2HSV);
        IplImage imgThreshold = cvCreateImage(cvGetSize(orgImg), 8, 1);
        cvInRangeS(imgHSV, min, max, imgThreshold);
        cvReleaseImage(imgHSV);
        return imgThreshold;
    }
}
