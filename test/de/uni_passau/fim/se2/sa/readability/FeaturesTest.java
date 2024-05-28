package de.uni_passau.fim.se2.sa.readability;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import de.uni_passau.fim.se2.sa.readability.features.FeatureMetric;
import de.uni_passau.fim.se2.sa.readability.features.HalsteadVolumeFeature;
import de.uni_passau.fim.se2.sa.readability.features.NumberLinesFeature;
import de.uni_passau.fim.se2.sa.readability.features.TokenEntropyFeature;
import picocli.CommandLine;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FeaturesTest {
    private Path sourceDir = new File("resources/snippets").toPath();
    private File truth = new File("resources/truth_scores.csv");
    private File targetFile = new File("resources/target.csv");
    private List<Path> paths;

    private String computeFile(Path path, FeatureMetric featureMetric) throws Exception {
        var content = new String(Files.readAllBytes(path));
        var df = new DecimalFormat("#.00");
        var value = featureMetric.computeMetric(content);
        return df.format(value);
    }

    @BeforeEach
    private void loadPaths() throws Exception {
        Comparator<Path> cmp =
                (o1, o2) -> Integer.parseInt(o1.getFileName().toString().replace(".jsnp", ""))
                        - Integer.parseInt(o2.getFileName().toString().replace(".jsnp", ""));
        paths = Files.walk(sourceDir).filter(path -> path.toFile().isFile()).sorted(cmp).toList();
    }

    @Test
    public void NumberLinesTestTrivial() throws Exception {
        var numberLinesFeature = new NumberLinesFeature();
        var result = numberLinesFeature.computeMetric("");
        assertEquals(result, 1.0);
    }

    @Test
    public void NumberLinesTest() throws Exception {
        String[] values = {"16.00", "30.00", "33.00", "24.00", "27.00", "18.00", "36.00", "33.00",
                "32.00", "25.00"};
        var value = Arrays.asList(values).iterator();
        var numberLinesFeature = new NumberLinesFeature();
        for (var path : paths.subList(0, 10)) {
            var result = computeFile(path, numberLinesFeature);
            assertEquals(result, value.next());
        }
    }

    @Test
    public void TokenEntropyTest() throws Exception {
        String[] values =
                {"2.03", "2.95", "3.15", "1.67", "2.19", "3.42", "3.52", "2.61", "2.98", "3.52"};
        var value = Arrays.asList(values).iterator();
        var tokenEntropyFeature = new TokenEntropyFeature();
        for (var path : paths.subList(0, 10)) {
            var result = computeFile(path, tokenEntropyFeature);
            assertEquals(result, value.next());
        }
    }

    @Test
    public void HalsteadVolumeTest() throws Exception {
        String[] values = {"62.27", "501.48", "554.48", "58.81", "213.62", "59.79", "446.25",
                "459.83", "237.19", "354.63"};
        var value = Arrays.asList(values).iterator();
        var halsteadVolumeFeature = new HalsteadVolumeFeature();
        for (var path : paths.subList(0, 10)) {
            var result = computeFile(path, halsteadVolumeFeature);
            assertEquals(result, value.next());
        }
    }

    @Test
    public void MainTest() throws Exception {
        var main = new ReadabilityAnalysisMain();
        var cli = new CommandLine(main);
        assertEquals(main.call(), 0);

        String[] preprocessArgs = {"preprocess", "-g", truth.getPath(), "-s", sourceDir.toString(),
                "-t", targetFile.getPath(), "LINES", "TOKEN_ENTROPY", "H_VOLUME"};
        assertEquals(cli.execute(preprocessArgs), 0);
        assertEquals(targetFile.exists(), true);

        try (var reader = new BufferedReader(new FileReader(targetFile))) {
            var line = reader.readLine();
            assertEquals(line.trim(), "File,NumberLines,TokenEntropy,HalsteadVolume,Truth");
        }

        String[] classifyArgs = {"classify", "-d", targetFile.getPath()};
        assertEquals(cli.execute(classifyArgs), 0);
    }
}
