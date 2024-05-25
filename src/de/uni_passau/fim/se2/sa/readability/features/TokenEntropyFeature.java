package de.uni_passau.fim.se2.sa.readability.features;

import java.util.HashMap;
import com.github.javaparser.ParseException;

public class TokenEntropyFeature extends FeatureMetric {

    /**
     * Computes the entropy metric based on the tokens of the given code snippet. Since we are
     * interested in the readability of code as perceived by a human, tokens also include
     * whitespaces and the like.
     *
     * @return token entropy of the given code snippet.
     */
    @Override
    public double computeMetric(String codeSnippet) {
        try {
            var tokenMap = new HashMap<String, Integer>();
            var tokens = parseJavaSnippet(codeSnippet).getTokenRange().get();
            for (var token : tokens) {
                tokenMap.merge(token.getText(), 1, Integer::sum);
            }

            double entropy = 0d;
            int totalCount = tokenMap.values().stream().reduce(0, Integer::sum);
            for (var count : tokenMap.values()) {
                double p = count.doubleValue() / totalCount;
                entropy -= p * (Math.log(p) / Math.log(2));
            }
            return entropy;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public String getIdentifier() {
        return "TokenEntropy";
    }
}
