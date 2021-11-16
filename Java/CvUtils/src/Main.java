import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs; // 3.3
// import org.opencv.highgui.Highgui; // 2.4
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    public void start(Stage stage) {
        VBox root = new VBox(15.0);
        root.setAlignment(Pos.CENTER);
        Button button = new Button("Выполнить");
        button.setOnAction(this::onClickButton);
        root.getChildren().add(button);
        Scene scene = new Scene(root, 400.0, 150.0);
        stage.setTitle("OpenCV " + Core.VERSION);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.show();
    }

    private void onClickButton(ActionEvent event) {
        // Загружаем изображение из файла
        Mat image = Imgcodecs.imread("F:\\png\\java.png");
        // 2.4
        // Mat image = Highgui.imread("C:\Users\vagif\OneDrive\Документы\GitHub\opencv\Java\CvUtils\res\java.png");
        if (image.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        // Обрабатываем изображение
        // Отображаем в отдельном окне
        CvUtilsFX.showImage(image, "Java!");
    }
}