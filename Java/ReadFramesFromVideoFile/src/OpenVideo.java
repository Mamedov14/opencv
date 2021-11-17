import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class OpenVideo {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        // "F:\\png\\video.mp4"

        JFrame window = new JFrame("Просмотр видео");
        window.setSize(1000, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        JLabel label = new JLabel();
        window.setContentPane(label);
        window.setVisible(true);
        VideoCapture capture = new VideoCapture("F:\\png\\people.mp4");
        if (!capture.isOpened()) {
            System.out.println("Не удалось открыть видео");
            return;
        }
        Mat frame = new Mat();
        BufferedImage image = null;
        while (capture.read(frame)) {
            Imgproc.resize(frame, frame, new Size(960, 540));
            // Здесь можно вставить код обработки кадра
            image = CvUtils.MatToBufferedImage(frame);
            if (image != null) {
                ImageIcon imageIcon = new ImageIcon(image);
                label.setIcon(imageIcon);
                label.repaint();
                window.pack();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
        System.out.println("Выход");
        capture.release();
    }
}
