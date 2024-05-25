package de.uni_passau;

import org.junit.jupiter.api.Test;
import de.uni_passau.fim.se2.sa.readability.features.FeatureMetric;
import de.uni_passau.fim.se2.sa.readability.features.HalsteadVolumeFeature;
import de.uni_passau.fim.se2.sa.readability.features.TokenEntropyFeature;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class FeaturesTest {
    private File snippetsDir = new File("resources/snippets");

    private double computeFile(Path path, FeatureMetric featureMetric) throws Exception {
        var content = new String(Files.readAllBytes(path));
        return featureMetric.computeMetric(content);
    }

    @Test
    public void TokenEntropyTest() throws Exception {
        Comparator<Path> cmp =
                (o1, o2) -> Integer.parseInt(o1.getFileName().toString().replace(".jsnp", ""))
                        - Integer.parseInt(o2.getFileName().toString().replace(".jsnp", ""));
        var paths = Files.walk(snippetsDir.toPath()).filter(path -> path.toFile().isFile())
                .sorted(cmp).toList();

        var tokenEntropyFeature = new TokenEntropyFeature();
        for (var path : paths.subList(0, 10)) {
            computeFile(path, tokenEntropyFeature);
        }
    }

    @Test
    public void HalsteadVolumeTest() throws Exception {
        Comparator<Path> cmp =
                (o1, o2) -> Integer.parseInt(o1.getFileName().toString().replace(".jsnp", ""))
                        - Integer.parseInt(o2.getFileName().toString().replace(".jsnp", ""));
        var paths = Files.walk(snippetsDir.toPath()).filter(path -> path.toFile().isFile())
                .sorted(cmp).toList();

        var halsteadVolumeFeature = new HalsteadVolumeFeature();
        for (var path : paths.subList(0, 10)) {
            computeFile(path, halsteadVolumeFeature);
        }
    }
}
