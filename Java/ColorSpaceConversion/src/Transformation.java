import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.highgui.HighGui.waitKey;

public class Transformation {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {


        //  Преобразование BGR в оттенки серого.
        // Данный код работает, далее будет приведён более интересный вариант, листай.
/*
        Mat image = Imgcodecs.imread("F:\\png\\yellow.png");
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGRA2GRAY);
        int index;
        if (!image.empty()) {
            while (true) {
                imshow("temp", image);
                imshow("gray", gray);
                index = waitKey(1);
                if (index == 27) {
                    break;
                }
            }
        }
*/

        /*

         * Прежде чем смотеть примеры, советую просто включить голову, всё работает абсолютно интуитивно!

         */

        // Преобразование BGR в RGB.

        Mat matrix = new Mat(1, 1, CvType.CV_8UC3, new Scalar(0, 128, 255));
        System.out.println(matrix.dump()); // [ 0, 128, 255]
        Mat matrix2 = new Mat();
        Imgproc.cvtColor(matrix, matrix2, Imgproc.COLOR_BGR2RGB);
        System.out.println(matrix2.dump()); // [255, 128, 0]

        //  Добавление или удаление альфа-канала.

        Mat m = new Mat(1, 1, CvType.CV_8UC3, new Scalar(0, 128, 255));
        System.out.println(m.dump()); // [ 0, 128, 255]
        Mat m2 = new Mat();
        Imgproc.cvtColor(m, m2, Imgproc.COLOR_BGR2BGRA);
        System.out.println(m2.dump()); // [ 0, 128, 255, 255]
        Mat m3 = new Mat();
        Imgproc.cvtColor(m2, m3, Imgproc.COLOR_BGRA2BGR);
        System.out.println(m3.dump()); // [ 0, 128, 255]

        // Преобразование BGR в HSV.

        /*

         Компоненты цвета в модели HSV (HSB):
         H (Hue) — цветовой тон. Обычно значение в диапазоне 0...360,
         но для изображений типа CV_8U это значение делится пополам, чтобы уместиться в диапазон
         0...255. В итоге получается диапазон 0...179;

         S (Saturation) — насыщенность. Обычно значение в диапазоне 0.0...1.0,
         но для изображений типа CV_8U диапазон 0...255;

         V (Value) — значение цвета или яркость.
         Обычно значение в диапазоне 0.0...1.0, но для изображений типа CV_8U диапазон 0...255.

         */


        // Преобразование BGR в HLS.

        /*

        Компоненты цвета в модели HLS:

        H (Hue) — цветовой тон. Обычно значение в диапазоне 0...360,
        но для изображений типа CV_8U это значение делится пополам,
        чтобы уместиться в диапазон 0...255. В итоге получается диапазон 0...179;

        L (Lightness) — светлота.
        Обычно значение в диапазоне 0.0...1.0,
        но для изображений типа CV_8U диапазон 0...255;

        S (Saturation) — насыщенность.
        Обычно значение в диапазоне 0.0...1.0,
        но для изображений типа CV_8U диапазон 0...255.

         */

        Mat mat = new Mat(1, 1, CvType.CV_8UC3, new Scalar(43, 0, 255));
        System.out.println(mat.dump()); // [ 43, 0, 255]
        Mat mat1 = new Mat();
        Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_BGR2HLS);
        System.out.println(mat1.dump()); // [175, 128, 255]
        Mat mat2 = new Mat();
        Imgproc.cvtColor(mat1, mat2, Imgproc.COLOR_HLS2BGR);
        System.out.println(mat2.dump()); // [ 43, 1, 255]


        /*

         * Наглядый пример как рабоатет преобразование цветов в компьюторе.

         */
        
        Mat image = Imgcodecs.imread("F:\\png\\yellow.png");
        Mat gray = new Mat();
        Mat hsv = new Mat();
        Mat hls = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGRA2GRAY);
        Imgproc.cvtColor(image, hsv, Imgproc.COLOR_BGR2HSV);
        Imgproc.cvtColor(image, hls, Imgproc.COLOR_BGR2HLS);
        int index;
        if (!image.empty()) {
            while (true) {
                imshow("temp", image);
                imshow("gray", gray);
                imshow("hsv", hsv);
                imshow("hls", hls);
                index = waitKey(1);
                if (index == 27) {
                    break;
                }
            }
        }


    }
}
