import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Arrays;

public class MatToBufferedImage {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {

    }


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

    // Преобразование BufferedImage в Mat.

    public static Mat BufferedImageToMat(BufferedImage img) {
        if (img == null) return new Mat();
        int type = 0;
        if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            type = CvType.CV_8UC1;
        } else if (img.getType() == BufferedImage.TYPE_3BYTE_BGR) {
            type = CvType.CV_8UC3;
        } else if (img.getType() == BufferedImage.TYPE_4BYTE_ABGR) {
            type = CvType.CV_8UC4;
        } else return new Mat();
        Mat mat = new Mat(img.getHeight(), img.getWidth(), type);
        byte[] data =
                ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        if (type == CvType.CV_8UC1 || type == CvType.CV_8UC3) {
            mat.put(0, 0, data);
            return mat;
        }
        byte[] buf = Arrays.copyOf(data, data.length);
        byte tmp = 0;
        for (int i = 0; i < buf.length; i += 4) { // ABGR => BGRA
            tmp = buf[i];
            buf[i] = buf[i + 1];
            buf[i + 1] = buf[i + 2];
            buf[i + 2] = buf[i + 3];
            buf[i + 3] = tmp;
        }
        mat.put(0, 0, buf);
        return mat;
    }

    // Преобразование Mat в WritableImage.

}
