import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;

import java.io.IOException;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;


public class Vision {

    public static final String XML_FILE = "C:\\Java Book\\opencv_java\\haarcascade_frontalface_default.xml";

    CvHaarClassifierCascade classifierFace;
    int count = 0;

    public Vision() throws IOException {

        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        IplImage img;
        classifierFace = new CvHaarClassifierCascade(cvLoad(XML_FILE));

        // захват камеры
        // OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(1);

        // подключение к камере через rtsp
        // FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("rtsp://");

        // подключить видосик
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("C:\\Java Book\\opencv_java\\temp.mp4");

        grabber.setAudioStream(0);
        grabber.start();

        Frame frame = grabber.grab();
        CanvasFrame canvasFrame = new CanvasFrame("Title");
        canvasFrame.setCanvasSize(frame.imageWidth, frame.imageHeight);

        // сохранять видосы
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("C:\\Java Book\\opencv_java\\opencv_test.avi",
                frame.imageWidth, frame.imageHeight);
        recorder.setFrameRate(25);
        recorder.setVideoCodec(13);
        recorder.setFormat("avi");
        double quality = 1;
        recorder.setVideoBitrate((int) (quality * 1024 * 1024));
        recorder.start();

        while (canvasFrame.isVisible() && (frame = grabber.grab()) != null) {
//          img = converter.convert(frame);
//          IplImage resizeImage = getSubImageFromIpl(img, 100, 100, 200, 200);

            img = toGray(converter.convert(frame));

            // поиск лица
            findObject(img);

            canvasFrame.showImage(converter.convert(img));
            recorder.record(converter.convert(img));
        }

        recorder.stop();
        recorder.close();
        canvasFrame.dispose();
    }

    // поиск всех лиц
    public IplImage getSubImageFromIpl(IplImage img, int x, int y, int w, int h) {
        IplImage resizeImage = IplImage.create(w, h, img.depth(), img.nChannels());
        cvSetImageROI(img, cvRect(x, y, w, h));
        cvCopy(img, resizeImage);
        cvResetImageROI(img);
        return resizeImage;
    }

    // сохранение вырезаного изображения в определённом размере
    public IplImage resizeIplImage(IplImage img, int w, int h) {
        IplImage resizeImage = IplImage.create(w, h, img.depth(), img.nChannels());
        cvResize(img, resizeImage);
        return resizeImage;
    }


    // поиск границы лица
    public void findObject(opencv_core.IplImage currentFrame) throws IOException {
        CvMemStorage storage = CvMemStorage.create();
        CvSeq faces = cvHaarDetectObjects(currentFrame, classifierFace, storage, 1.5, 3, CV_HAAR_MAGIC_VAL);
        int total = faces.total();
        int face_w = 70;
        int face_h = 80;
        if (total > 0) {
            System.out.println(total + " faces");
            for (int i = 0; i < total; i++) {
                CvRect rect = new CvRect(cvGetSeqElem(faces, 1));
                int x = rect.x();
                int y = rect.y();
                int w = rect.width();
                int h = rect.height();
//                IplImage face = getSubImageFromIpl(currentFrame, x, y, w, h);
//                face = resizeIplImage(face, face_w, face_h);
//                 cvSaveImage("C:\\Users\\vagif\\OneDrive\\Документы\\GitHub\\opencv\\JavaCV\\src\\main\\resources\\face\\" + count + "-new.jpg", face);
//                 count++;
                rectangle(cvarrToMat(currentFrame), new Rect(x, y, w, h), new Scalar(0, 255, 0, 0), 2, 0, 0);
            }
        }
    }

    public IplImage toGray(IplImage img) {
        IplImage currentFrame = IplImage.create(img.width(), img.height(), IPL_DEPTH_8U, 1);
        cvCvtColor(img, currentFrame, CV_RGBA2GRAY);
        return currentFrame;
    }

}