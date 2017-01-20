package com.zhbwang.bigdata.es.clog;

import com.zhbwang.bigdata.es.ESProperties;
import com.zhbwang.bigdata.es.es4sql.SearchDao;
import com.zhbwang.bigdata.es.es4sql.TestsConstants;
import com.zhbwang.bigdata.es.es4sql.exception.SqlParseException;
import com.zhbwang.bigdata.es.es4sql.query.SqlElasticSearchRequestBuilder;
import com.zhbwang.bigdata.lib.tools.LogUtil;
import com.zhbwang.bigdata.lib.tools.StringTools;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wangzb on 2017-1-20.
 */
public class ESTestClog {


    private static TransportClient client;
    private static SearchDao searchDao;

    public static void setUp() throws Exception {

        Settings settings = Settings.builder().put("client.transport.ignore_cluster_name", true).build();
        client = TransportClient.builder().settings(settings).
                build();
        String [] clusterHostsArray = ESProperties.getValue("es.cluster.hosts").split(",");
        for (String clusterHost : clusterHostsArray) {
            if (StringTools.isNotBlank(clusterHost)) {
                client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(clusterHost.split(":")[0]), Integer.parseInt(clusterHost.split(":")[1])));
            }
        }
        NodesInfoResponse nodeInfos = client.admin().cluster().prepareNodesInfo().get();
        String clusterName = nodeInfos.getClusterName().value();
        LogUtil.info(String.format("Found cluster... cluster name: %s", clusterName));

        // Load test data.
        if (client.admin().indices().prepareExists(TestsConstants.TEST_INDEX).execute().actionGet().isExists()) {
            LogUtil.info("inde {0} exist", TestsConstants.TEST_INDEX);
        }

        searchDao = new SearchDao(client);

        //refresh to make sure all the docs will return on queries
//        client.admin().indices().prepareRefresh(TEST_INDEX).execute().actionGet();

        LogUtil.info("Finished the setup process...");
    }


    public static SearchDao getSearchDao() {
        return searchDao;
    }

    public static TransportClient getClient() {
        return client;
    }

    public static void searchTypeTest() throws IOException, SqlParseException, SQLFeatureNotSupportedException{
        SearchHits response = query(String.format("SELECT * FROM %s/103005 LIMIT 10", TestsConstants.TEST_INDEX));
        LogUtil.info("response.getTotalHits(): {0}", response.getTotalHits());
    }

    public static void selectSpecificFields() throws IOException, SqlParseException, SQLFeatureNotSupportedException {
        String[] arr = new String[]{"commType3Code_s", "mapCommStr3_s"};
        Set expectedSource = new HashSet(Arrays.asList(arr));

        SearchHits response = query(String.format("SELECT commType3Code_s, mapCommStr3_s FROM %s/103005 limit 20", TestsConstants.TEST_INDEX));
        SearchHit[] hits = response.getHits();
        LogUtil.info("response.getTotalHits(): {0}", response.getTotalHits());
        for (SearchHit hit : hits) {
            LogUtil.info(hit.getSource());
        }
    }
    public static void likeTest() throws IOException, SqlParseException, SQLFeatureNotSupportedException {
        SearchHits response = query(String.format("SELECT * FROM %s WHERE attrStr5_sn LIKE '%%购物车管理%%' LIMIT 10", TestsConstants.TEST_INDEX));
        SearchHit[] hits = response.getHits();
        LogUtil.info("response.getTotalHits(): {0}", response.getTotalHits());
        for (SearchHit hit : hits) {
            LogUtil.info(hit.getSource());
        }
    }

    private static SearchHits query(String query) throws SqlParseException, SQLFeatureNotSupportedException, SQLFeatureNotSupportedException {
        SearchDao searchDao = ESTestClog.getSearchDao();
        SqlElasticSearchRequestBuilder select = (SqlElasticSearchRequestBuilder) searchDao.explain(query).explain();
        return ((SearchResponse) select.get()).getHits();
    }


    private static SqlElasticSearchRequestBuilder getRequestBuilder(String query) throws SqlParseException, SQLFeatureNotSupportedException, SQLFeatureNotSupportedException {
        SearchDao searchDao = ESTestClog.getSearchDao();
        return (SqlElasticSearchRequestBuilder) searchDao.explain(query).explain();
    }

    private static SearchResponse getSearchResponse(String query) throws SqlParseException, SQLFeatureNotSupportedException, SQLFeatureNotSupportedException {
        SearchDao searchDao = ESTestClog.getSearchDao();
        SqlElasticSearchRequestBuilder select = (SqlElasticSearchRequestBuilder) searchDao.explain(query).explain();
        return ((SearchResponse) select.get());
    }

    public static void main(String[] args) throws Exception {
        ESTestClog.setUp();
        ESTestClog.searchTypeTest();
        ESTestClog.selectSpecificFields();
        ESTestClog.likeTest();
    }

}
