import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

public class DataTypes {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
/*

        Изменение типа изображения.
        Как по мне, преобразование несёт чисто информативный хараткер
        Так как, я не думаю, что я это-то использую, кому это нужно или можеть быть интересно
        можете посмотреть книжку закреплёную в README.md
        Ydachi!))

        Hahaha, пишу это спустя 20 минут после изучения преобразования типов
        Скажу так, я поспешил со своими выводами, они реально нужны и будут играть большую роль
        ))

 */
        // Преобразование типа CV_8U в CV_32F.

        Mat m = new Mat(1, 1, CvType.CV_8UC3, new Scalar(43, 0, 255));
        System.out.println(m.dump()); // [ 43, 0, 255]
        Mat m2 = new Mat();
        m.convertTo(m2, CvType.CV_32F, 1.0 / 255);
        System.out.println(m2.dump()); // [0.16862746, 0, 1]
        Mat m3 = new Mat();
        m2.convertTo(m3, CvType.CV_8U, 255);
        System.out.println(m3.dump()); // [ 43, 0, 255]


    }
}
