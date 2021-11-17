import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class CaptureCamera {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static boolean isRun = true;
    public static boolean isEnd = false;

    public static void main(String[] args) {

        JFrame window = new JFrame("Нажмите <Esc> для отключения от камеры");
        window.setSize(640, 480);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.setLocationRelativeTo(null);
        // Обработка нажатия кнопки Закрыть в заголовке окна
            window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                isRun = false;
                if (isEnd) {
                    window.dispose();
                    System.exit(0);
                } else {
                    System.out.println(
                            "Сначала нажмите <Esc>, потом Закрыть");
                }
            }
        });
        // Обработка нажатия клавиши <Esc>
        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 27) {
                    isRun = false;
                }
            }
        });
        JLabel label = new JLabel();
        window.setContentPane(label);
        window.setVisible(true);
        // Подключаемся к камере
        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            window.setTitle("Не удалось подключиться к камере");
            isRun = false;
            isEnd = true;
            return;
        }
        try {
            // Задаем размеры кадра
            camera.set(Videoio.CAP_PROP_FRAME_WIDTH, 640);
            camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, 480);
            // Считываем кадры
            Mat frame = new Mat();
            BufferedImage image = null;
            while (isRun) {
                if (camera.read(frame)) {
                    // Здесь можно вставить код обработки кадра
                    image = CvUtils.MatToBufferedImage(frame);
                    if (image != null) {
                        ImageIcon imageIcon = new ImageIcon(image);
                        label.setIcon(imageIcon);
                        label.repaint();
                        window.pack();
                    }
                    try {
                        Thread.sleep(100); // 10 кадров в секунду
                    } catch (InterruptedException e) {
                    }
                } else {
                    System.out.println("Не удалось захватить кадр");
                    break;
                }
            }
        } finally {
            camera.release();
            isRun = false;
            isEnd = true;
        }
        window.setTitle("Камера отключена");
    }
}
