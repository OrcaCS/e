package summative;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import javafx.scene.control.Slider;

// import javafx.scene.input.MouseDragEvent;
// import javafx.scene.input.MouseEvent; // OY HEY OI ORCA. REMOVE THIS IF DRAW FAILS
// import javafx.scene.input.MouseButton;

public class PrimaryController {

    private Stage stage;
    private Image originalImage; // Use this to keep track of the original image

    private double brightnessValue; // Get the value from the slider

    @FXML
    private ImageView imageView; // OY HEY OI ORCA. NOTE TO SELF ADD THESE FOR NEW METHODS

    @FXML
    private MenuItem openImage;

    @FXML
    private MenuItem saveImage;

    @FXML
    private MenuItem exit;

    @FXML
    private MenuItem horizontalFlip;

    @FXML
    private MenuItem verticalFlip;

    @FXML
    private MenuItem rotation;

    @FXML
    private MenuItem grayScale;

    @FXML
    private MenuItem sepiaTone;

    @FXML
    private MenuItem invertColor;

    // @FXML
    // private MenuItem brightness;

    @FXML
    private Slider brightnessSlider;

    @FXML
    private MenuItem onBrightnessSlider;

    @FXML
    private MenuItem bulge;

    @FXML
    private MenuItem colorOverlay;

    @FXML
    private MenuItem pixelation;

    @FXML
    private MenuItem vignette;

    @FXML
    private MenuItem edgeDetection;

    @FXML
    private MenuItem emboss;

    @FXML
    void onOpenImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif"));

        try {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                Image image = new Image(file.toURI().toString());
                originalImage = image;
                imageView.setImage(image);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Image Load Failed");
            alert.setContentText("There was a problem opening your image");
            alert.showAndWait();
        }
    }

    @FXML
    public void onSaveImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files", "*.png"));
        File file = fileChooser.showSaveDialog(imageView.getScene().getWindow());

        if (file != null) {
            WritableImage writableImage = imageView.snapshot(null, null);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Image Save Failed");
                alert.setContentText("There was a problem saving your image");
                alert.showAndWait();
            }
        }
    }

    @FXML
    void onRestore(ActionEvent event) {
        imageView.setImage(originalImage);
    }

    @FXML
    void onExit(ActionEvent event) {
        System.exit(1);
    }

    @FXML
    void onHorizontalFlip(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                writer.setColor(width - i - 1, j, reader.getColor(i, j));
            }
        }
        imageView.setImage(writableImage);
    }

    @FXML
    void onVerticalFlip(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                writer.setColor(i, height - j - 1, reader.getColor(i, j));
            }
        }
        imageView.setImage(writableImage);
    }

    @FXML
    void onRotation(ActionEvent event) {
        double rotationAngle = Math.toRadians(90);

        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();
        double cx = width / 2;
        double cy = height / 2;

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double dx = x - cx;
                double dy = y - cy;
                int xPrime = (int) (dx * Math.cos(rotationAngle) - dy * Math.sin(rotationAngle) + cx);
                int yPrime = (int) (dx * Math.sin(rotationAngle) - dy * Math.cos(rotationAngle) + cy);

                if (xPrime >= 0 && xPrime < width) {
                    if (yPrime >= 0 && yPrime < height) {
                        writer.setColor(x, y, reader.getColor(xPrime, yPrime));
                    }
                }
            }
        }
        imageView.setImage(writableImage);
    }

    @FXML
    void onGrayScale(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = reader.getColor(x, y);
                double red = color.getRed();
                double green = color.getGreen();
                double blue = color.getBlue();
                double gray = red * 0.21 + green * 0.71 + blue * 0.07;

                Color newColor = new Color(gray, gray, gray,
                        color.getOpacity());
                writer.setColor(x, y, newColor);
            }
        }
        imageView.setImage(writableImage);
    }

    @FXML
    void onSepiaTone(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = reader.getColor(x, y);
                double red = color.getRed();
                double green = color.getGreen();
                double blue = color.getBlue();

                double newRed = 0.393 * red + 0.769 * green + 0.189 * blue;
                double newGreen = 0.394 * red + 0.686 * green + 0.168 * blue;
                double newBlue = 0.272 * red + 0.534 * green + 0.131 * blue;

                Color newColor = new Color(Math.min(newRed, 1.0), Math.min(newGreen, 1.0), Math.min(newBlue, 1.0),
                        color.getOpacity());
                writer.setColor(x, y, newColor);
            }
        }
        imageView.setImage(writableImage);
    }

    @FXML
    void onInvertColor(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = reader.getColor(x, y);
                double red = color.getRed();
                double green = color.getGreen();
                double blue = color.getBlue();

                Color newColor = new Color(1.0 - red, 1.0 - green, 1.0 - blue,
                        color.getOpacity());
                writer.setColor(x, y, newColor);
            }
        }
        imageView.setImage(writableImage);
    }

    // @FXML
    // void onBrightness(ActionEvent event) {
    //     int width = (int) imageView.getImage().getWidth();
    //     int height = (int) imageView.getImage().getHeight();
    //     double brightness = 0.2; // brightness addition between 0 and 1

    //     WritableImage writableImage = new WritableImage(width, height);
    //     PixelReader reader = imageView.getImage().getPixelReader();
    //     PixelWriter writer = writableImage.getPixelWriter();

    //     for (int x = 0; x < width; x++) {
    //         for (int y = 0; y < height; y++) {
    //             Color color = reader.getColor(x, y);
    //             double red = color.getRed();
    //             double green = color.getGreen();
    //             double blue = color.getBlue();

    //             Color newColor = new Color(Math.min(red + brightness, 1.0), Math.min(green + brightness, 1.0),
    //                     Math.min(blue + brightness, 1.0),
    //                     color.getOpacity());
    //             writer.setColor(x, y, newColor);
    //         }
    //     }
    //     imageView.setImage(writableImage);
    // }

    @FXML
    private void handleSliderChange(MouseEvent event) {
        brightnessValue = brightnessSlider.getValue();
    }

    @FXML
    void onBrightnessSlider(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = reader.getColor(x, y);
                double red = color.getRed();
                double green = color.getGreen();
                double blue = color.getBlue();

                Color newColor = new Color(Math.max(0, Math.min(red + brightnessValue, 1.0)),
                        Math.max(0, Math.min(green + brightnessValue, 1.0)),
                        Math.max(0, Math.min(blue + brightnessValue, 1.0)),
                        color.getOpacity());
                writer.setColor(x, y, newColor);
            }
        }
        imageView.setImage(writableImage);
    }

    @FXML
    void onBulge(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();
        double cx = width / 2;
        double cy = height / 2;

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double p = 1.6;
                double s = 20;

                double dx = x - cx;
                double dy = y - cy;
                double r = Math.sqrt(dx * dx + dy * dy);
                double theta = Math.atan2(dy, dx);
                double rPrime = Math.pow(r, p) / s;
                int xPrime = (int) (cx + rPrime * Math.cos(theta));
                int yPrime = (int) (cy + rPrime * Math.sin(theta));

                if (xPrime >= 0 && xPrime < width) {
                    if (yPrime >= 0 && yPrime < height) {
                        writer.setColor(x, y, reader.getColor(xPrime, yPrime));
                    }
                }
            }
        }
        imageView.setImage(writableImage);
    }

    @FXML
    void onColorOverlay(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = reader.getColor(x, y);
                Color overlay = Color.BLUEVIOLET;

                writer.setColor(x, y, color.interpolate(overlay, 0.5));
            }
        }
        imageView.setImage(writableImage);
    }

    @FXML
    void onPixelation(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();
        int blockSize = 10;

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int x = 0; x < width; x += blockSize) {
            for (int y = 0; y < height; y += blockSize) {
                Color color = reader.getColor(x, y);

                for (int i = x; i < i + blockSize && i + blockSize < height; i++) {
                    for (int j = y; j < j + blockSize && j + blockSize < width; j++) {
                        writer.setColor(i, j, color);
                    }
                }
            }
        }
        imageView.setImage(writableImage);
    }

    @FXML
    void onVignette(ActionEvent event) {
        double minimumBrightness = 0.3;

        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();
        double cx = width / 2;
        double cy = height / 2;
        double max = Math.sqrt(cx * cx + cy * cy);

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = reader.getColor(x, y);

                double distance = Math.sqrt(Math.pow(x - cx, 2) + Math.pow(y - cy, 2));
                double brightness = Math.max(1 - distance / max, minimumBrightness);
                Color derivedColor = color.deriveColor(0.0, 1.0, 1.0, brightness);
                writer.setColor(x, y, derivedColor);
            }
        }
        imageView.setImage(writableImage);
    }

    @FXML
    void onEdgeDetection(ActionEvent event) {
        double[][] kernel = { { 1, 1, 1 }, { 1, -7, 1 }, { 1, 1, 1 } };
        final int KERNEL_SIZE = 3;
        final int OFFSET = 0;

        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int x = OFFSET; x < width; x++) {
            for (int y = OFFSET; y < height; y++) {
                double red = 0;
                double green = 0;
                double blue = 0;

                for (int kx = 0; kx < KERNEL_SIZE && x + kx < width; kx++) {
                    for (int ky = 0; ky < KERNEL_SIZE && y + ky < height; ky++) {
                        Color color = reader.getColor(x + kx - OFFSET, y + ky - OFFSET);
                        red += color.getRed() * kernel[kx][ky];
                        green += color.getGreen() * kernel[kx][ky];
                        blue += color.getBlue() * kernel[kx][ky];
                    }
                }

                red = Math.max(0, Math.min(red, 1));
                green = Math.max(0, Math.min(green, 1));
                blue = Math.max(0, Math.min(blue, 1));
                Color newColor = Color.color(red, green, blue);

                writer.setColor(x, y, newColor);
                imageView.setImage(writableImage);
            }
        }
    }

    @FXML
    void onEmboss(ActionEvent event) {
        double[][] kernel = { { -2, -1, 0 }, { -1, 1, 1 }, { 0, 1, 2 } };
        final int KERNEL_SIZE = 3;
        final int OFFSET = 0;

        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int x = OFFSET; x < width; x++) {
            for (int y = OFFSET; y < height; y++) {
                double red = 0;
                double green = 0;
                double blue = 0;

                for (int kx = 0; kx < KERNEL_SIZE && x + kx < width; kx++) {
                    for (int ky = 0; ky < KERNEL_SIZE && y + ky < height; ky++) {
                        Color color = reader.getColor(x + kx - OFFSET, y + ky - OFFSET);
                        red += color.getRed() * kernel[kx][ky];
                        green += color.getGreen() * kernel[kx][ky];
                        blue += color.getBlue() * kernel[kx][ky];
                    }
                }

                red = Math.max(0, Math.min(red, 1));
                green = Math.max(0, Math.min(green, 1));
                blue = Math.max(0, Math.min(blue, 1));
                Color newColor = Color.color(red, green, blue);

                writer.setColor(x, y, newColor);
                imageView.setImage(writableImage);
            }
        }
    }

    /*
     * Accessing a pixels colors
     * 
     * Color color = reader.getColor(x, y);
     * double red = color.getRed();
     * double green = color.getGreen();
     * double blue = color.getBlue();
     */

    /*
     * Modifying a pixels colors
     * 
     * Color newColor = new Color(1.0 - red, 1.0 - green, 1.0 - blue,
     * color.getOpacity());
     */

    // @FXML
    // void onDraw(ActionEvent event) {
    // final int RADIUS = 5;

    // int width = (int) imageView.getImage().getWidth();
    // int height = (int) imageView.getImage().getHeight();

    // WritableImage writableImage = new WritableImage(width, height);
    // PixelReader reader = imageView.getImage().getPixelReader();
    // PixelWriter writer = writableImage.getPixelWriter();
    // MouseEvent mouse = new MouseEvent(true, 0, 0, 0, 0, PRIMARY, 1, false, false,
    // false, false, true, false, false, false, false, false, false);
    // // ^^ left click
    // if (mouse.MOUSE_PRESSED) { // when mouse is clciked (button down) && maybe
    // right click to stop??
    // mouse.getSceneX();
    // mouse.getSceneY();

    // for (int x = 0 - RADIUS; x < RADIUS; x++) {
    // for (int y = 0 - RADIUS; y < RADIUS; y++) {
    // Color color = Color.BLUEVIOLET;
    // writer.setColor(x, y, color);
    // }
    // }
    // imageView.setImage(writableImage);
    // }
    // }

    // @FXML
    // void onMouseBulge(ActionEvent event) {
    // on mouse click
    // grab location
    // set a radius using slider
    // slap in bulge code but mini (for each pixel in radius/circle)
    // }

    // @FXML
    // void onSpotRemove(ActionEvent event) {
    // on mouse right click
    // grab location and radius of thing to duplicate
    // set a radius using slider
    // on left click, in a radius, print out the right click spot
    // for bonus, make it airbrush??
    // }

    // DO NOT REMOVE THIS METHOD!
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
