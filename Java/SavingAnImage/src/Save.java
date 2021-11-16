import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Save {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {

        // Загрузим изображение в формате JPEG, а затем сохраним его в формате PNG:

        Mat image = Imgcodecs.imread("F:\\png\\yellow.jpg");
        if (image.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        boolean st = Imgcodecs.imwrite("F:\\png\\yellow.png", image);
        if (!st) {
            System.out.println("Не удалось сохранить изображение");
        }

        /*

        Изображения, имеющие тип CV_8U, можно сохранить в любом доступном формате.
        Чтобы сохранить изображения, имеющие тип CV_16U, нужно использовать форматы
        PNG, JPEG 2000 или TIFF. Если нужно сохранить изображение с альфа-каналом, то
        следует использовать формат PNG.

         */

    }
}
