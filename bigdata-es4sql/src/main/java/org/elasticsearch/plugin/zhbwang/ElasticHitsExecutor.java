package org.elasticsearch.plugin.zhbwang;

import org.elasticsearch.search.SearchHits;
import com.zhbwang.bigdata.es.es4sql.exception.SqlParseException;

import java.io.IOException;

/**
 * Created by Eliran on 21/8/2016.
 */
public interface ElasticHitsExecutor {
    public void run() throws IOException, SqlParseException;
    public SearchHits getHits();
}
