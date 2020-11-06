package org.talend.tql.maplang;

import org.junit.Test;
import org.talend.maplang.mql.parser.MqlParser;
import org.talend.maplang.mql.parser.model.ScriptNode;

import static org.junit.Assert.assertEquals;

public class TqlMapLangBuilderTest {

    @Test
    public void shouldMixMQLandTQL() {
        // Given
        ScriptNode builtScriptNode = TqlMapLangBuilder.builder()
                .fromScript("FROM order SELECT {id = order.id}")
                .where("age = 0")
                .build();

        ScriptNode scriptNode = new MqlParser().parse("FROM order WHERE age = 0 SELECT {id = order.id}");

        // Then
        String expected = scriptNode.getSelectNode().getFromNode().getWhereNode().getELNode().toString();
        String actual = builtScriptNode.getSelectNode().getFromNode().getWhereNode().getELNode().toString();
        assertEquals(expected, actual);
    }
}