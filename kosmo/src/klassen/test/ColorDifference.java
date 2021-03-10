package klassen.test;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ColorDifference {

    public static void main(String[] args) {
        ConcurrentMap<Color, Double> colorDifferenceMap = new ConcurrentHashMap<>();
        ExecutorService executorService = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors());

        BufferedImage inputImage;

        try {
            // Read in the input image
            inputImage = ImageIO.read(Paths.get("input.png").toFile());
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to read input image!", e);
        }

        generateInfiniteColors().distinct().limit(40).forEach(color -> {
            executorService.execute(() -> {
                CIELab cieLabColor = CIELab.from(color);

                double sum = 0d;

                for (int y = 0; y < inputImage.getHeight(); y++) {
                    for (int x = 0; x < inputImage.getWidth(); x++) {
                        Color pixelColor = new Color(inputImage.getRGB(x, y));
                        CIELab pixelCIELabColor = CIELab.from(pixelColor);
                        sum += cieLabColor.difference(pixelCIELabColor);
                    }
                }

                colorDifferenceMap.put(color, sum);
            });
        });

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // The 12 solution colors are held in this list
        List<Color> colorSolutions = colorDifferenceMap.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue())
            .limit(50)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        colorSolutions.forEach(System.out::println);

        for (int y = 0; y < inputImage.getHeight(); y++) {
            for (int x = 0; x < inputImage.getWidth(); x++) {
                CIELab cieLabColor = CIELab.from(new Color(inputImage.getRGB(x, y)));

                int finalX = x;
                int finalY = y;

                colorSolutions.stream()
                    .min(Comparator.comparingDouble(color -> 
                        cieLabColor.difference(CIELab.from(color))))
                    .ifPresent(closestColor -> 
                        inputImage.setRGB(finalX, finalY, closestColor.getRGB()));
            }
        }

        try {
            ImageIO.write(inputImage, "png", new File("output.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inspiration taken from https://stackoverflow.com/a/20032024/7294647
    private static Stream<Color> generateInfiniteColors() {
        return Stream.generate(() -> 
            new Color(ThreadLocalRandom.current().nextInt(0x1000000)));
    }

    static class CIELab {

        private final double L, a, b;

        public CIELab(double L, double a, double b) {
            this.L = L;
            this.a = a;
            this.b = b;
        }

        public double difference(CIELab cieLab) {
            return Math.sqrt(Math.pow(cieLab.L - L, 2) + Math.pow(cieLab.a - a, 2) +
                Math.pow(cieLab.b - b, 2));
        }

        public static CIELab from(Color color) {
            int sR = color.getRed();
            int sG = color.getGreen();
            int sB = color.getBlue();

            // Convert Standard-RGB to XYZ (http://www.easyrgb.com/en/math.php)
            double var_R = ( sR / 255d );
            double var_G = ( sG / 255d );
            double var_B = ( sB / 255d );

            if ( var_R > 0.04045 ) var_R = Math.pow( ( var_R + 0.055 ) / 1.055, 2.4 );
            else                   var_R = var_R / 12.92;
            if ( var_G > 0.04045 ) var_G = Math.pow( ( var_G + 0.055 ) / 1.055, 2.4 );
            else                   var_G = var_G / 12.92;
            if ( var_B > 0.04045 ) var_B = Math.pow( ( var_B + 0.055 ) / 1.055, 2.4 );
            else                   var_B = var_B / 12.92;

            var_R = var_R * 100;
            var_G = var_G * 100;
            var_B = var_B * 100;

            double X = var_R * 0.4124 + var_G * 0.3576 + var_B * 0.1805;
            double Y = var_R * 0.2126 + var_G * 0.7152 + var_B * 0.0722;
            double Z = var_R * 0.0193 + var_G * 0.1192 + var_B * 0.9505;

            // Convert XYZ to CIELAB (http://www.easyrgb.com/en/math.php
            double var_X = X / 96.422;
            double var_Y = Y / 100.000;
            double var_Z = Z / 82.521;

            if ( var_X > 0.008856 ) var_X = Math.pow( var_X, 1D / 3D );
            else                    var_X = ( 7.787 * var_X ) + ( 16D / 116 );
            if ( var_Y > 0.008856 ) var_Y = Math.pow( var_Y, 1D / 3D );
            else                    var_Y = ( 7.787 * var_Y ) + ( 16D / 116 );
            if ( var_Z > 0.008856 ) var_Z = Math.pow( var_Z, 1D / 3D );
            else                    var_Z = ( 7.787 * var_Z ) + ( 16D / 116 );

            double L = ( 116 * var_Y ) - 16;
            double a = 500 * ( var_X - var_Y );
            double b = 200 * ( var_Y - var_Z );

            return new CIELab(L, a, b);
        }
    }
}
