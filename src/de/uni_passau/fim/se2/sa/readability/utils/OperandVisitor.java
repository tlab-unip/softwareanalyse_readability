package de.uni_passau.fim.se2.sa.readability.utils;

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.HashMap;
import java.util.Map;

public class OperandVisitor extends VoidVisitorAdapter<Void> {


    private final Map<String, Integer> operandsPerMethod;

    public OperandVisitor() {
        operandsPerMethod = new HashMap<>();
    }

    public Map<String, Integer> getOperandsPerMethod() {
        return operandsPerMethod;
    }

    @Override
    public void visit(SimpleName n, Void arg) {
        super.visit(n, arg);

        operandsPerMethod.merge(n.getIdentifier(), 1, Integer::sum);
        // System.out.println(
        // n.getClass().getSimpleName() + n.getBegin().get() + "\t" + n.getIdentifier());
    }

    @Override
    public void visit(Parameter n, Void arg) {
        super.visit(n, arg);

        operandsPerMethod.merge(n.getNameAsString(), 1, Integer::sum);
        // System.out.println(
        // n.getClass().getSimpleName() + n.getBegin().get() + "\t" + n.getNameAsString());
    }

    @Override
    public void visit(CharLiteralExpr n, Void arg) {
        super.visit(n, arg);

        operandsPerMethod.merge(n.getValue(), 1, Integer::sum);
        // System.out.println(n.getClass().getSimpleName() + n.getBegin().get() + "\t" +
        // n.getValue());
    }

    @Override
    public void visit(BooleanLiteralExpr n, Void arg) {
        super.visit(n, arg);

        operandsPerMethod.merge(String.valueOf(n.getValue()), 1, Integer::sum);
        // System.out.println(n.getClass().getSimpleName() + n.getBegin().get() + "\t"
        // + String.valueOf(n.getValue()));
    }

    @Override
    public void visit(IntegerLiteralExpr n, Void arg) {
        super.visit(n, arg);

        operandsPerMethod.merge(n.getValue(), 1, Integer::sum);
        // System.out.println(n.getClass().getSimpleName() + n.getBegin().get() + "\t" +
        // n.getValue());
    }

    @Override
    public void visit(StringLiteralExpr n, Void arg) {
        super.visit(n, arg);

        operandsPerMethod.merge(n.getValue(), 1, Integer::sum);
        // System.out.println(n.getClass().getSimpleName() + n.getBegin().get() + "\t" +
        // n.getValue());
    }

    @Override
    public void visit(NullLiteralExpr n, Void arg) {
        super.visit(n, arg);

        operandsPerMethod.merge(n.toString(), 1, Integer::sum);
        // System.out.println(n.getClass().getSimpleName() + n.getBegin().get() + "\t" +
        // n.toString());
    }

    @Override
    public void visit(DoubleLiteralExpr n, Void arg) {
        super.visit(n, arg);

        operandsPerMethod.merge(n.getValue(), 1, Integer::sum);
    }

    @Override
    public void visit(LongLiteralExpr n, Void arg) {
        super.visit(n, arg);

        operandsPerMethod.merge(n.getValue(), 1, Integer::sum);
    }
}
