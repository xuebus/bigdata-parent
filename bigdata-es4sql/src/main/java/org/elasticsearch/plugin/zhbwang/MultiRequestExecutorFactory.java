package org.elasticsearch.plugin.zhbwang;

import org.elasticsearch.client.Client;
import com.zhbwang.bigdata.es.es4sql.exception.SqlParseException;
import com.zhbwang.bigdata.es.es4sql.query.multi.MultiQueryRequestBuilder;

/**
 * Created by Eliran on 21/8/2016.
 */
public class MultiRequestExecutorFactory {
     public static ElasticHitsExecutor createExecutor(Client client,MultiQueryRequestBuilder builder) throws SqlParseException {
         switch (builder.getRelation()){
             case UNION_ALL:
             case UNION:
                 return new UnionExecutor(client,builder);
             case MINUS:
                 return new MinusExecutor(client,builder);
             default:
                 throw new SqlParseException("only supports union, and minus operations");
         }
     }
}
