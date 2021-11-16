/*

    Большинство методов реализованы для сохранения информации и общего развития.

 */

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.Arrays;

public class CvUtils {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

/*
    // всё работает!
    // Пример загрузки и просмотра изображения в приложении Swing:
    public static void main(String[] args) {
        Mat img = Imgcodecs.imread("F:\\png\\yellow.jpg");
        if (img.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        CvUtils.showImage(img, "Текст в заголовке окна");
    }*/

    // Преобразование Mat в BufferedImage.

    public static BufferedImage MatToBufferedImage(Mat mat) {

        if (mat == null || mat.empty()) {
            return null;
        }
        if (mat.depth() == CvType.CV_8U) {
        } else if (mat.depth() == CvType.CV_16U) { // CV_16U => CV_8U
            Mat m_16 = new Mat();
            mat.convertTo(m_16, CvType.CV_8U, 255.0 / 65535);
            mat = m_16;
        } else if (mat.depth() == CvType.CV_32F) { // CV_32F => CV_8U
            Mat m_32 = new Mat();
            mat.convertTo(m_32, CvType.CV_8U, 255);
            mat = m_32;
        } else {
            return null;
        }
        int type = 0;
        if (mat.channels() == 1)
            type = BufferedImage.TYPE_BYTE_GRAY;
        else if (mat.channels() == 3)
            type = BufferedImage.TYPE_3BYTE_BGR;
        else if (mat.channels() == 4)
            type = BufferedImage.TYPE_4BYTE_ABGR;
        else {
            return null;
        }
        byte[] buf = new byte[mat.channels() * mat.cols() * mat.rows()];
        mat.get(0, 0, buf);
        byte temp = 0;
        if (mat.channels() == 4) { // BGRA => ABGR
            for (int i = 0; i < buf.length; i += 4) {
                temp = buf[i + 3];
                buf[i + 3] = buf[i + 2];
                buf[i + 2] = buf[i + 1];
                buf[i + 1] = buf[i];
                buf[i] = temp;
            }
        }
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        byte[] data =
                ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buf, 0, data, 0, buf.length);
        return image;
    }

    //  Преобразование BufferedImage в Mat.

    public static Mat BufferedImageToMat(BufferedImage img) {

        if (img == null) {
            return new Mat();
        }
        int type = 0;
        if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            type = CvType.CV_8UC1;
        } else if (img.getType() == BufferedImage.TYPE_3BYTE_BGR) {
            type = CvType.CV_8UC3;
        } else if (img.getType() == BufferedImage.TYPE_4BYTE_ABGR) {
            type = CvType.CV_8UC4;
        } else {
            return new Mat();
        }
        Mat mat = new Mat(img.getHeight(), img.getWidth(), type);
        byte[] data =
                ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        if (type == CvType.CV_8UC1 || type == CvType.CV_8UC3) {
            mat.put(0, 0, data);
            return mat;
        }
        byte[] buf = Arrays.copyOf(data, data.length);
        byte temp = 0;
        for (int i = 0; i < buf.length; i += 4) { // ABGR => BGRA
            temp = buf[i];
            buf[i] = buf[i + 1];
            buf[i + 1] = buf[i + 2];
            buf[i + 2] = buf[i + 3];
            buf[i + 3] = temp;
        }
        mat.put(0, 0, buf);
        return mat;
    }

    // Сохранение Mat в бинарный файл.

    public static boolean saveMat(Mat mat, String path) {

        if (mat == null || mat.empty()) {
            return false;
        }
        if (path == null || path.length() < 5 || !path.endsWith(".mat")) {
            return false;
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
            return false;
        }
        if (mat.channels() == 2 || mat.channels() > 4) return false;
        byte[] buf = new byte[mat.channels() * mat.cols() * mat.rows()];
        mat.get(0, 0, buf);
        try (
                OutputStream out = new FileOutputStream(path);
                BufferedOutputStream bout = new BufferedOutputStream(out);
                DataOutputStream dout = new DataOutputStream(bout);
        ) {
            dout.writeInt(mat.rows());
            dout.writeInt(mat.cols());
            dout.writeInt(mat.channels());
            dout.write(buf);
            dout.flush();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // Загрузка Mat из бинарного файла.

    public static Mat loadMat(String path) {

        if (path == null || path.length() < 5 || !path.endsWith(".mat")) {
            return new Mat();
        }
        File f = new File(path);
        if (!f.exists() || !f.isFile()) {
            return new Mat();
        }
        try (
                InputStream in = new FileInputStream(path);
                BufferedInputStream bin = new BufferedInputStream(in);
                DataInputStream din = new DataInputStream(bin);
        ) {
            int rows = din.readInt();
            if (rows < 1) {
                return new Mat();
            }
            int cols = din.readInt();
            if (cols < 1) {
                return new Mat();
            }
            int ch = din.readInt();
            int type = 0;
            if (ch == 1) {
                type = CvType.CV_8UC1;
            } else if (ch == 3) {
                type = CvType.CV_8UC3;
            } else if (ch == 4) {
                type = CvType.CV_8UC4;
            } else {
                return new Mat();
            }
            int size = ch * cols * rows;
            byte[] buf = new byte[size];
            int resize = din.read(buf);
            if (size != resize) {
                return new Mat();
            }
            Mat m = new Mat(rows, cols, type);
            m.put(0, 0, buf);
            return m;
        } catch (Exception e) {
        }
        return new Mat();
    }


    public static void showImage(Mat img, String title) {
        BufferedImage im = MatToBufferedImage(img);
        if (im == null) {
            return;
        }
        int w = 1000, h = 600;
        JFrame window = new JFrame(title);
        window.setSize(w, h);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon imageIcon = new ImageIcon(im);
        JLabel label = new JLabel(imageIcon);
        JScrollPane pane = new JScrollPane(label);
        window.setContentPane(pane);
        if (im.getWidth() < w && im.getHeight() < h) {
            window.pack();
        }
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

}


