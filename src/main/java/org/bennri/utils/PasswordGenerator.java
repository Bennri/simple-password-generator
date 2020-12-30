package org.bennri.utils;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PasswordGenerator {
    private Logger logger;

    public PasswordGenerator() {
        this.logger = Logger.getLogger(PasswordGenerator.class.toString());
    }

    // letters: 65 - 90 = 26
    // capital letters: 97 - 122 = 36
    // symbols: 33 - 47, 58 - 64, 91 - 96, 123 - 126  == 15 + 7 + 6 + 4 = 32
    // numbers: 48 - 57 = 10
    // -> 33 - 126

    public double computeEntropy(int length) {
        double entropy = Math.log(Math.pow(32 + 26 + 26 + 10, length)) / Math.log(2);
        logger.log(Level.INFO, "{0} Bit Entropy", new Object[]{entropy});
        return entropy;
    }


    /**
     * Scale values to get the ASCII representation later.
     * @param vals the computed values which will be scaled.
     * @return Integer array of scaled values.
     */
    private Integer[] scaleValuesToRange(List<Integer> vals) {

        // use explicit float type and later convert back to int
        final float r_min = Collections.min(vals);
        final float r_max = Collections.max(vals);
        final float t_min = 33.0f;
        final float t_max = 126.0f;
        // scaling function
        Function<Integer, Float> scaler = i -> (i - r_min) / (r_max - r_min) * (t_max - t_min) + t_min;
        // scale values and return
        return vals.stream().map(scaler).map(Math::round).toArray(Integer[]::new);
    }


    public String generatePassword(List<Point> coords) {
        // store the result of XOR of the coordinates
        List<Integer> values = new LinkedList<>();
        for (int i = 0; i < coords.size(); i++) {
            Point p = coords.get(i);
            // XOR of the coordinates
            values.add(p.x^p.y);
        }
        // scale the values
        Integer[] scaledValues = scaleValuesToRange(values);

        // create String representation
        StringBuilder builder = new StringBuilder();
        for (int i : scaledValues) {
            // ASCII
            builder.append((char) i);
        }
        return builder.toString();
    }

    public void knuthShuffle() {
        // TODO
    }

}
