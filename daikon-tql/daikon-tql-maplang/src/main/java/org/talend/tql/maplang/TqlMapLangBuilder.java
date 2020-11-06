package org.talend.tql.maplang;

import org.talend.maplang.mql.parser.MqlParser;
import org.talend.maplang.mql.parser.model.ScriptNode;
import org.talend.maplang.mql.parser.model.WhereNode;
import org.talend.tql.model.Expression;
import org.talend.tql.parser.Tql;

public class TqlMapLangBuilder {

    private ScriptNode mql;

    private Expression tqlWhere;

    private TqlMapLangBuilder() {
    }

    public static TqlMapLangBuilder builder() {
        return new TqlMapLangBuilder();
    }

    public TqlMapLangBuilder fromScript(String mql) {
        return fromEL(new MqlParser().parse(mql));
    }

    public TqlMapLangBuilder fromEL(ScriptNode scriptNode) {
        this.mql = scriptNode;
        return this;
    }

    public TqlMapLangBuilder where(String tql) {
        return whereExpression(Tql.parse(tql));
    }

    public TqlMapLangBuilder whereExpression(Expression expression) {
        this.tqlWhere = expression;
        return this;
    }

    public ScriptNode build() {
        final WhereNode whereNode = new WhereNode();
        whereNode.setELNode(tqlWhere.accept(new MapLangVisitor()));
        mql.getSelectNode().getFromNode().setWhereNode(whereNode);

        return mql;
    }
}
