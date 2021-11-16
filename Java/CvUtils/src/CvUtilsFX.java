/*

    Большинство методов реализованы для сохранения информации и общего развития.

 */


import javafx.embed.swing.SwingFXUtils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;


public class CvUtilsFX {

    // Преобразование Mat в WritableImage.

    public static WritableImage MatToWritableImage(Mat m) {

        BufferedImage bim = CvUtils.MatToBufferedImage(m);
        if (bim == null) {
            return null;
        } else {
            return SwingFXUtils.toFXImage(bim, null);
        }
    }

    // Преобразование Mat в WritableImage (ускоренный вариант).

    public static WritableImage MatToImageFX(Mat mat) {

        if (mat == null || mat.empty()) {
            return null;
        }
        if (mat.depth() == CvType.CV_8U) {
        } else if (mat.depth() == CvType.CV_16U) {
            Mat m_16 = new Mat();
            mat.convertTo(m_16, CvType.CV_8U, 255.0 / 65535);
            mat = m_16;
        } else if (mat.depth() == CvType.CV_32F) {
            Mat m_32 = new Mat();
            mat.convertTo(m_32, CvType.CV_8U, 255);
            mat = m_32;
        } else {
            return null;
        }
        if (mat.channels() == 1) {
            Mat m_bgra = new Mat();
            Imgproc.cvtColor(mat, m_bgra, Imgproc.COLOR_GRAY2BGRA);
            mat = m_bgra;
        } else if (mat.channels() == 3) {
            Mat m_bgra = new Mat();
            Imgproc.cvtColor(mat, m_bgra, Imgproc.COLOR_BGR2BGRA);
            mat = m_bgra;
        } else if (mat.channels() == 4) {
        } else {
            return null;
        }
        byte[] buf = new byte[mat.channels() * mat.cols() * mat.rows()];
        mat.get(0, 0, buf);
        WritableImage wim = new WritableImage(mat.cols(), mat.rows());
        PixelWriter pw = wim.getPixelWriter();
        pw.setPixels(0, 0, mat.cols(), mat.rows(),
                WritablePixelFormat.getByteBgraInstance(),
                buf, 0, mat.cols() * 4);
        return wim;
    }

    // Преобразование Image (WritableImage) в Mat.

    public static Mat ImageFXToMat(Image image) {

        if (image == null) {
            return new Mat();
        }
        PixelReader pixelReader = image.getPixelReader();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        byte[] buf = new byte[4 * width * height];
        pixelReader.getPixels(0, 0, width, height, WritablePixelFormat.getByteBgraInstance(),
                buf, 0, width * 4);
        Mat mat = new Mat(height, width, CvType.CV_8UC4);
        mat.put(0, 0, buf);
        return mat;
    }


    // Класс CvUtilsFX.

    public static void showImage(Mat img, String title) {

        Image im = MatToImageFX(img);
        Stage window = new Stage();
        ScrollPane sp = new ScrollPane();
        ImageView iv = new ImageView();
        if (im != null) {
            iv.setImage(im);
            if (im.getWidth() < 1000) {
                sp.setPrefWidth(im.getWidth() + 5);
            } else sp.setPrefWidth(1000.0);
            if (im.getHeight() < 700) {
                sp.setPrefHeight(im.getHeight() + 5);
            } else sp.setPrefHeight(700.0);
        }
        sp.setContent(iv);
        sp.setPannable(true);
        BorderPane box = new BorderPane();
        box.setCenter(sp);
        Scene scene = new Scene(box);
        window.setScene(scene);
        window.setTitle(title);
        window.show();
    }
}



