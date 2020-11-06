package org.talend.tql.maplang;

import org.junit.Test;
import org.talend.maplang.el.parser.model.ELNode;
import org.talend.maplang.mql.parser.MqlParser;
import org.talend.maplang.mql.parser.model.ScriptNode;
import org.talend.tql.model.Expression;
import org.talend.tql.parser.Tql;

import static org.junit.Assert.*;

public class MapLangVisitorTest {

    public static void assertMQLEquals(final String where) {
        assertMQLEquals(where, where);
    }

    public static void assertMQLEquals(final String mqlWhere, final String tqlWhere) {
        // Given
        final MqlParser parser = new MqlParser();
        final ScriptNode node = parser.parse("FROM order WHERE " + mqlWhere + " SELECT {id = order.id}");

        final Expression expression = Tql.parse(tqlWhere);
        final ELNode parsedTqlNode = expression.accept(new MapLangVisitor());

        // Then
        ELNode parsedMqlNode = node.getSelectNode().getFromNode().getWhereNode().getELNode();
        assertEquals(parsedMqlNode.toString(), parsedTqlNode.toString());
    }

    @Test
    public void shouldHandleEquals() {
        assertMQLEquals("age = 10");
    }

    @Test
    public void shouldHandleLT() {
        assertMQLEquals("age < 10");
    }

    @Test
    public void shouldHandleLTE() {
        assertMQLEquals("age <= 10");
    }

    @Test
    public void shouldHandleGT() {
        assertMQLEquals("age > 10");
    }

    @Test
    public void shouldHandleGTE() {
        assertMQLEquals("age >= 10");
    }

    @Test
    public void shouldHandleAnd() {
        assertMQLEquals("age = 10 && gender = 'M'", "age = 10 and gender = 'M'");
    }

    @Test
    public void shouldHandleOr() {
        assertMQLEquals("age = 10 || gender = 'M'", "age = 10 or gender = 'M'");
    }
}