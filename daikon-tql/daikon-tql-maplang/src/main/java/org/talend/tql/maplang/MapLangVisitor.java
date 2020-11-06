package org.talend.tql.maplang;

import org.apache.commons.lang3.NotImplementedException;
import org.talend.maplang.el.parser.model.ELNode;
import org.talend.maplang.el.parser.model.ELNodeType;
import org.talend.tql.model.*;
import org.talend.tql.visitor.IASTVisitor;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapLangVisitor implements IASTVisitor<ELNode> {

    @Override
    public ELNode visit(TqlElement elt) {
        throw new NotImplementedException("Support for " + elt);
    }

    @Override
    public ELNode visit(ComparisonOperator elt) {
        ComparisonOperator.Enum operator = elt.getOperator();
        switch (operator) {
            case EQ:
                return new ELNode(ELNodeType.EQUAL, "=");
            case LT:
                return new ELNode(ELNodeType.LOWER_THAN, "<");
            case GT:
                return new ELNode(ELNodeType.GREATER_THAN, ">");
            case NEQ:
                return new ELNode(ELNodeType.NOT, "!");
            case LET:
                return new ELNode(ELNodeType.LOWER_OR_EQUAL, "<=");
            case GET:
                return new ELNode(ELNodeType.GREATER_OR_EQUAL, ">=");
            default:
                throw new NotImplementedException("No support for " + operator);
        }
    }

    @Override
    public ELNode visit(LiteralValue elt) {
        LiteralValue.Enum literal = elt.getLiteral();
        switch (literal) {
            case QUOTED_VALUE:
                return new ELNode(ELNodeType.STRING_LITERAL, "'" + elt.getValue() + "'");
            case INT:
            case DECIMAL:
                return new ELNode(ELNodeType.DECIMAL_LITERAL, elt.getValue());
            case BOOLEAN:
                return new ELNode(ELNodeType.BOOLEAN_LITERAL, elt.getValue());
            default:
                throw new NotImplementedException("No support for " + literal);
        }
    }

    @Override
    public ELNode visit(FieldReference elt) {
        return new ELNode(ELNodeType.HPATH, elt.getPath());
    }

    @Override
    public ELNode visit(Expression elt) {
        throw new NotImplementedException("Can't handle generic expressions.");
    }

    @Override
    public ELNode visit(AndExpression elt) {
        if (elt.getExpressions().length == 1) {
            return elt.getExpressions()[0].accept(this);
        }

        final ELNode andNode = new ELNode(ELNodeType.AND, "&&");
        andNode.addChildren(Stream.of(elt.getExpressions())
                .map(e -> e.accept(this))
                .collect(Collectors.toList()));
        return andNode;
    }

    @Override
    public ELNode visit(OrExpression elt) {
        if (elt.getExpressions().length == 1) {
            return elt.getExpressions()[0].accept(this);
        }

        final ELNode andNode = new ELNode(ELNodeType.OR, "||");
        andNode.addChildren(Stream.of(elt.getExpressions())
                .map(e -> e.accept(this))
                .collect(Collectors.toList()));
        return andNode;
    }

    @Override
    public ELNode visit(ComparisonExpression elt) {
        ELNode comparisonNode = elt.getOperator().accept(this);
        comparisonNode.addChild(elt.getField().accept(this));
        comparisonNode.addChild(elt.getValueOrField().accept(this));

        return comparisonNode;
    }

    @Override
    public ELNode visit(FieldInExpression elt) {
        // Not support for IN in MapLang
        throw new UnsupportedOperationException();
    }

    @Override
    public ELNode visit(FieldIsEmptyExpression elt) {
        ELNode isEmptyNode = new ELNode(ELNodeType.OR);
        isEmptyNode.addChild(ELNodeType.EQUAL, "==");
        isEmptyNode.addChild(ELNodeType.EQUAL, null);
        return isEmptyNode;
    }

    @Override
    public ELNode visit(FieldIsValidExpression elt) {
        // Not support for isValid in MapLang
        throw new UnsupportedOperationException();
    }

    @Override
    public ELNode visit(FieldIsInvalidExpression elt) {
        // Not support for isValid in MapLang
        throw new UnsupportedOperationException();
    }

    @Override
    public ELNode visit(FieldMatchesRegex elt) {
        throw new NotImplementedException("Support for " + elt);
    }

    @Override
    public ELNode visit(FieldCompliesPattern elt) {
        throw new NotImplementedException("Support for " + elt);
    }

    @Override
    public ELNode visit(FieldWordCompliesPattern elt) {
        throw new NotImplementedException("Support for " + elt);
    }

    @Override
    public ELNode visit(FieldBetweenExpression elt) {
        throw new NotImplementedException("Support for " + elt);
    }

    @Override
    public ELNode visit(NotExpression elt) {
        ELNode node = new ELNode(ELNodeType.NOT);
        node.addChild(elt.getExpression().accept(this));
        return node;
    }

    @Override
    public ELNode visit(FieldContainsExpression elt) {
        throw new NotImplementedException("Support for " + elt);
    }

    @Override
    public ELNode visit(AllFields allFields) {
        throw new NotImplementedException("Support for " + allFields);
    }
}
