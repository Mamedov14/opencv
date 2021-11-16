import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Load {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {

        Mat image = Imgcodecs.imread("F:\\png\\yellow.jpg");

        if (image.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }

        System.out.println(image.width());
        System.out.println(image.height());
        System.out.println(CvType.typeToString(image.type()));
        System.out.println(image.channels());

        /*

        Первое значение представляет ширину изображения,
        второе — высоту,
        третье — тип матрицы,
        а четвертое — количество каналов.

        Параметр flags позволяет дополнительно указать различные флаги. Если параметр
        имеет значение –1, то изображение загружается как есть (включая альфа-канал),
        если равен 0 — то будет выполнено преобразование в оттенки серого, если больше
        0 — изображение будет иметь три канала (без альфа-канала). В качестве значения
        можно указать следующие статические константы (или их комбинацию) из класса

         */

        /*

        IMREAD_UNCHANGED — изображение загружается как есть (включая альфа-канал).
        IMREAD_COLOR — цветное изображение из трех каналов (если другие флаги не установлены,
        то по 8 битов на канал — тип CV_8UC3).
        IMREAD_ANYDEPTH — если флаг установлен, то вернет 16-битное или 32-битное
        значение (при условии, что изображение имеет такую глубину цвета, в противном случае — 8-битное).
        IMREAD_ANYCOLOR — если флаг установлен, то вернет изображение в любом доступном цветовом формате.

         */

        // несколько примеров

        System.out.println(Imgcodecs.IMREAD_UNCHANGED); // -1
        System.out.println(Imgcodecs.IMREAD_GRAYSCALE); // 0
        System.out.println(Imgcodecs.IMREAD_COLOR); // 1
        System.out.println(Imgcodecs.IMREAD_ANYDEPTH); // 2
        System.out.println(Imgcodecs.IMREAD_ANYCOLOR); // 4
        System.out.println(Imgcodecs.IMREAD_LOAD_GDAL); // 8
        System.out.println(Imgcodecs.IMREAD_REDUCED_GRAYSCALE_2); // 16
        System.out.println(Imgcodecs.IMREAD_REDUCED_GRAYSCALE_4); // 32
        System.out.println(Imgcodecs.IMREAD_REDUCED_GRAYSCALE_8); // 64
        System.out.println(Imgcodecs.IMREAD_REDUCED_COLOR_2); // 17
        System.out.println(Imgcodecs.IMREAD_REDUCED_COLOR_4); // 33
        System.out.println(Imgcodecs.IMREAD_REDUCED_COLOR_8); // 65
        System.out.println(Imgcodecs.IMREAD_IGNORE_ORIENTATION); // 128

    }
}
