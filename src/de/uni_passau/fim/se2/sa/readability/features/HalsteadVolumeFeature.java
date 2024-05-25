package de.uni_passau.fim.se2.sa.readability.features;

import com.github.javaparser.ParseException;
import de.uni_passau.fim.se2.sa.readability.utils.OperandVisitor;
import de.uni_passau.fim.se2.sa.readability.utils.OperatorVisitor;

public class HalsteadVolumeFeature extends FeatureMetric {

    /**
     * Computes the Halstead Volume metric based on the given code snippet.
     *
     * @return Halstead Volume of the given code snippet.
     */
    @Override
    public double computeMetric(String codeSnippet) {
        try {
            var parsed = parseJavaSnippet(codeSnippet);
            // parsed.walk(node -> {
            // System.out.println(node.getClass().getSimpleName() + "\t" + node.getBegin().get());
            // });
            // System.out.println();

            OperatorVisitor operatorVisitor = new OperatorVisitor();
            parsed.accept(operatorVisitor, null);
            var operatorsPerMethod = operatorVisitor.getOperatorsPerMethod();
            var totalOperators = operatorsPerMethod.values().stream().reduce(0, Integer::sum);
            var uniqueOperators = operatorsPerMethod.size();

            OperandVisitor operandVisitor = new OperandVisitor();
            parsed.accept(operandVisitor, null);
            var operandsPerMethod = operandVisitor.getOperandsPerMethod();
            var totalOperands = operandsPerMethod.values().stream().reduce(0, Integer::sum);
            var uniqueOperands = operandsPerMethod.size();

            // System.out.println(String.format("%d, %d, %d, %d", totalOperators, uniqueOperators,
            // totalOperands, uniqueOperands));

            double volume = (totalOperators + totalOperands)
                    * (Math.log(uniqueOperators + uniqueOperands) / Math.log(2));
            return volume;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public String getIdentifier() {
        return "HalsteadVolume";
    }
}
