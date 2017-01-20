package com.zhbwang.bigdata.es4sql.query.join;

import org.elasticsearch.client.Client;
import com.zhbwang.bigdata.es4sql.domain.Condition;
import com.zhbwang.bigdata.es4sql.domain.JoinSelect;
import com.zhbwang.bigdata.es4sql.domain.hints.Hint;
import com.zhbwang.bigdata.es4sql.domain.hints.HintType;
import com.zhbwang.bigdata.es4sql.query.QueryAction;

import java.util.List;

/**
 * Created by Eliran on 15/9/2015.
 */
public class ESJoinQueryActionFactory {
    public static QueryAction createJoinAction(Client client, JoinSelect joinSelect) {
        List<Condition> connectedConditions = joinSelect.getConnectedConditions();
        boolean allEqual = true;
        for (Condition condition : connectedConditions) {
            if (condition.getOpear() != Condition.OPEAR.EQ) {
                allEqual = false;
                break;
            }

        }
        if (!allEqual)
            return new ESNestedLoopsQueryAction(client, joinSelect);

        boolean useNestedLoopsHintExist = false;
        for (Hint hint : joinSelect.getHints()) {
            if (hint.getType() == HintType.USE_NESTED_LOOPS) {
                useNestedLoopsHintExist = true;
                break;
            }
        }
        if (useNestedLoopsHintExist)
            return new ESNestedLoopsQueryAction(client, joinSelect);

        return new ESHashJoinQueryAction(client, joinSelect);

    }
}
