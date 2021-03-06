package org.elasticsearch.plugin.zhbwang.executors;

import org.elasticsearch.client.Client;
import org.elasticsearch.rest.RestChannel;
import com.zhbwang.bigdata.es.es4sql.query.QueryAction;

import java.util.Map;

/**
 * Created by Eliran on 26/12/2015.
 */
public interface RestExecutor {
    public void execute(Client client, Map<String, String> params, QueryAction queryAction, RestChannel channel) throws Exception;

    public String execute(Client client, Map<String, String> params, QueryAction queryAction) throws Exception;
}
