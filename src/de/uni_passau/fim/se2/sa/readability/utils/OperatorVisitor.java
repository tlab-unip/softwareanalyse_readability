package de.uni_passau.fim.se2.sa.readability.utils;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.Map;

public class OperatorVisitor extends VoidVisitorAdapter<Void> {

    public enum OperatorType {
        ASSIGNMENT, // x=y
        BINARY, // x+y
        UNARY, // -x, ++x
        CONDITIONAL, // ==, <=, &&, ||
        TYPE_COMPARISON, // instanceof
    }

    private final Map<OperatorType, Integer> operatorsPerMethod;

    public OperatorVisitor() {
        operatorsPerMethod = new HashMap<>();
    }

    public Map<OperatorType, Integer> getOperatorsPerMethod() {
        return operatorsPerMethod;
    }

    @Override
    public void visit(VariableDeclarationExpr n, Void arg) {
        super.visit(n, arg);
        operatorsPerMethod.merge(OperatorType.ASSIGNMENT, 1, Integer::sum);
    }

    @Override
    public void visit(AssignExpr n, Void arg) {
        super.visit(n, arg);
        operatorsPerMethod.merge(OperatorType.ASSIGNMENT, 1, Integer::sum);
    }

    @Override
    public void visit(BinaryExpr n, Void arg) {
        super.visit(n, arg);
        operatorsPerMethod.merge(OperatorType.BINARY, 1, Integer::sum);
    }

    @Override
    public void visit(UnaryExpr n, Void arg) {
        super.visit(n, arg);
        operatorsPerMethod.merge(OperatorType.UNARY, 1, Integer::sum);
    }

    @Override
    public void visit(ConditionalExpr n, Void arg) {
        super.visit(n, arg);
        operatorsPerMethod.merge(OperatorType.CONDITIONAL, 1, Integer::sum);
    }

    @Override
    public void visit(InstanceOfExpr n, Void arg) {
        super.visit(n, arg);
        operatorsPerMethod.merge(OperatorType.TYPE_COMPARISON, 1, Integer::sum);
    }
}
