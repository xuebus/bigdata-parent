package com.zhbwang.bigdata.es.clog;

import com.google.common.io.ByteStreams;
import com.zhbwang.bigdata.es.es4sql.SearchDao;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.deletebyquery.DeleteByQueryAction;
import org.elasticsearch.action.deletebyquery.DeleteByQueryRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.plugin.deletebyquery.DeleteByQueryPlugin;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import com.zhbwang.bigdata.es.es4sql.exception.SqlParseException;
import com.zhbwang.bigdata.es.es4sql.query.SqlElasticSearchRequestBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.zhbwang.bigdata.es.es4sql.TestsConstants.TEST_INDEX;

/**
 * Created by wangzb on 2017-1-20.
 */
public class ESTest {


    private static TransportClient client;
    private static SearchDao searchDao;

    public static void setUp() throws Exception {

//        Settings settings = Settings.builder().put("client.transport.ignore_cluster_name",true).build();
//
//        client = new PreBuiltTransportClient(settings).
//                addTransportAddress(getTransportAddress());


//        NodesInfoResponse nodeInfos = client.admin().cluster().prepareNodesInfo().get();
//        String clusterName = nodeInfos.getClusterName().value();
//        System.out.println(String.format("Found cluster... cluster name: %s", clusterName));


        Settings settings = Settings.builder().put("client.transport.ignore_cluster_name", true).build();
        client = TransportClient.builder().addPlugin(DeleteByQueryPlugin.class).settings(settings).
                build().addTransportAddress(getTransportAddress());


        NodesInfoResponse nodeInfos = client.admin().cluster().prepareNodesInfo().get();
        String clusterName = nodeInfos.getClusterName().value();
        System.out.println(String.format("Found cluster... cluster name: %s", clusterName));

        // Load test data.
        if (client.admin().indices().prepareExists(TEST_INDEX).execute().actionGet().isExists()) {
            client.admin().indices().prepareDelete(TEST_INDEX).get();
        }
            loadBulk("src/test/resources/accounts.json");
            loadBulk("src/test/resources/online.json");
            preparePhrasesIndex();
            loadBulk("src/test/resources/phrases.json");
            loadBulk("src/test/resources/dogs.json");
            loadBulk("src/test/resources/peoples.json");
            loadBulk("src/test/resources/game_of_thrones_complex.json");
            loadBulk("src/test/resources/systems.json");

            prepareOdbcIndex();
            loadBulk("src/test/resources/odbc-date-formats.json");

            prepareSpatialIndex("location");
            loadBulk("src/test/resources/locations.json");

            prepareSpatialIndex("location2");
            loadBulk("src/test/resources/locations2.json");

            prepareNestedTypeIndex();
            loadBulk("src/test/resources/nested_objects.json");

            prepareChildrenTypeIndex();
            prepareParentTypeIndex();
            loadBulk("src/test/resources/parent_objects.json");
            loadBulk("src/test/resources/children_objects.json");

        searchDao = new SearchDao(client);

        //refresh to make sure all the docs will return on queries
        client.admin().indices().prepareRefresh(TEST_INDEX).execute().actionGet();

        System.out.println("Finished the setup process...");
    }


    private static void preparePhrasesIndex() {
        String dataMapping = "{  \"phrase\": {" +
                " \"properties\": {\n" +
                "          \"phrase\": {\n" +
                "            \"type\": \"string\",\n" +
                "            \"store\": true\n" +
                "          }" +
                "       }" +
                "   }" +
                "}";
        client.admin().indices().preparePutMapping(TEST_INDEX).setType("phrase").setSource(dataMapping).execute().actionGet();
    }

    private static void prepareNestedTypeIndex() {

        String dataMapping = "{ \"nestedType\": {\n" +
                "        \"properties\": {\n" +
                "          \"message\": {\n" +
                "            \"type\": \"nested\",\n" +
                "            \"properties\": {\n" +
                "              \"info\": {\n" +
                "                \"type\": \"string\",\n" +
                "                \"index\": \"not_analyzed\"\n" +
                "              },\n" +
                "              \"author\": {\n" +
                "                \"type\": \"string\",\n" +
                "                \"index\": \"not_analyzed\"\n" +
                "              },\n" +
                "              \"dayOfWeek\": {\n" +
                "                \"type\": \"long\"\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          \"comment\": {\n" +
                "            \"type\": \"nested\",\n" +
                "            \"properties\": {\n" +
                "              \"data\": {\n" +
                "                \"type\": \"string\",\n" +
                "                \"index\": \"not_analyzed\"\n" +
                "              },\n" +
                "              \"likes\": {\n" +
                "                \"type\": \"long\"\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          \"myNum\": {\n" +
                "            \"type\": \"long\"\n" +
                "          },\n" +
                "          \"someField\": {\n" +
                "                \"type\": \"string\",\n" +
                "                \"index\": \"not_analyzed\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }}";

        client.admin().indices().preparePutMapping(TEST_INDEX).setType("nestedType").setSource(dataMapping).execute().actionGet();
    }

    private static void prepareChildrenTypeIndex() {

        String dataMapping = "{\n" +
                "	\"childrenType\": {\n" +
                "		\"_routing\": {\n" +
                "			\"required\": true\n" +
                "		},\n" +
                "		\"_parent\": {\n" +
                "			\"type\": \"parentType\"\n" +
                "		},\n" +
                "		\"properties\": {\n" +
                "			\"dayOfWeek\": {\n" +
                "				\"type\": \"long\"\n" +
                "			},\n" +
                "			\"author\": {\n" +
                "				\"index\": \"not_analyzed\",\n" +
                "				\"type\": \"string\"\n" +
                "			},\n" +
                "			\"info\": {\n" +
                "				\"index\": \"not_analyzed\",\n" +
                "				\"type\": \"string\"\n" +
                "			}\n" +
                "		}\n" +
                "	}" +
                "}\n";

        client.admin().indices().preparePutMapping(TEST_INDEX).setType("childrenType").setSource(dataMapping).execute().actionGet();
    }

    private static void prepareParentTypeIndex() {

        String dataMapping = "{\n" +
                "	\"parentType\": {\n" +
                "		\"properties\": {\n" +
                "			\"parentTile\": {\n" +
                "				\"index\": \"not_analyzed\",\n" +
                "				\"type\": \"string\"\n" +
                "			}\n" +
                "		}\n" +
                "	}\n" +
                "}\n";

        client.admin().indices().preparePutMapping(TEST_INDEX).setType("parentType").setSource(dataMapping).execute().actionGet();
    }

    public static void tearDown() throws InterruptedException {
        System.out.println("teardown process...");
        client.close();
    }


    /**
     * Delete all data inside specific index
     *
     * @param indexName the documents inside this index will be deleted.
     */
    public static void deleteQuery(String indexName) {
        deleteQuery(indexName, null);
    }

    /**
     * Delete all data using DeleteByQuery.
     *
     * @param indexName the index to delete
     * @param typeName  the type to delete
     */
    public static void deleteQuery(String indexName, String typeName) {

        DeleteByQueryRequestBuilder deleteQuery = new DeleteByQueryRequestBuilder(client, DeleteByQueryAction.INSTANCE);
        deleteQuery.setIndices(indexName);
        if (typeName != null) {
            deleteQuery.setTypes(typeName);
        }
        deleteQuery.setQuery(QueryBuilders.matchAllQuery());

        deleteQuery.get();
        System.out.println(String.format("Deleted index %s and type %s", indexName, typeName));


    }


    /**
     * Loads all data from the json into the test
     * elasticsearch cluster, using TEST_INDEX
     *
     * @param jsonPath the json file represents the bulk
     * @throws Exception
     */
    public static void loadBulk(String jsonPath) throws Exception {
        System.out.println(String.format("Loading file %s into elasticsearch cluster", jsonPath));

        BulkRequestBuilder bulkBuilder = client.prepareBulk();
        byte[] buffer = ByteStreams.toByteArray(new FileInputStream(jsonPath));
        bulkBuilder.add(buffer, 0, buffer.length, TEST_INDEX, null);
        BulkResponse response = bulkBuilder.get();

        if (response.hasFailures()) {
            throw new Exception(String.format("Failed during bulk load of file %s. failure message: %s", jsonPath, response.buildFailureMessage()));
        }
    }

    public static void prepareSpatialIndex(String type) {
        String dataMapping = "{\n" +
                "\t\"" + type + "\" :{\n" +
                "\t\t\"properties\":{\n" +
                "\t\t\t\"place\":{\n" +
                "\t\t\t\t\"type\":\"geo_shape\",\n" +
                "\t\t\t\t\"tree\": \"quadtree\",\n" +
                "\t\t\t\t\"precision\": \"10km\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"center\":{\n" +
                "\t\t\t\t\"type\":\"geo_point\",\n" +
                "\t\t\t\t\"geohash\":true,\n" +
                "\t\t\t\t\"geohash_prefix\":true,\n" +
                "\t\t\t\t\"geohash_precision\":17\n" +
                "\t\t\t},\n" +
                "\t\t\t\"description\":{\n" +
                "\t\t\t\t\"type\":\"string\"\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";

        client.admin().indices().preparePutMapping(TEST_INDEX).setType(type).setSource(dataMapping).execute().actionGet();
    }

    public static void prepareOdbcIndex() {
        String dataMapping = "{\n" +
                "\t\"odbc\" :{\n" +
                "\t\t\"properties\":{\n" +
                "\t\t\t\"odbc_time\":{\n" +
                "\t\t\t\t\"type\":\"date\",\n" +
                "\t\t\t\t\"format\": \"{'ts' ''yyyy-MM-dd HH:mm:ss.SSS''}\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"docCount\":{\n" +
                "\t\t\t\t\"type\":\"string\"\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";

        client.admin().indices().preparePutMapping(TEST_INDEX).setType("odbc").setSource(dataMapping).execute().actionGet();
    }

    public static SearchDao getSearchDao() {
        return searchDao;
    }

    public static TransportClient getClient() {
        return client;
    }

    protected static InetSocketTransportAddress getTransportAddress() throws UnknownHostException {
        String host = System.getenv("ES_TEST_HOST");
        String port = System.getenv("ES_TEST_PORT");

        if (host == null) {
            host = "localhost";
            System.out.println("ES_TEST_HOST enviroment variable does not exist. choose default 'localhost'");
        }

        if (port == null) {
            port = "9300";
            System.out.println("ES_TEST_PORT enviroment variable does not exist. choose default '9300'");
        }

        System.out.println(String.format("Connection details: host: %s. port:%s.", host, port));
        return new InetSocketTransportAddress(InetAddress.getByName(host), Integer.parseInt(port));
    }
    public static void searchTypeTest() throws IOException, SqlParseException, SQLFeatureNotSupportedException{
        SearchHits response = query(String.format("SELECT * FROM %s/phrase LIMIT 1000", TEST_INDEX));
        System.out.println(response.getTotalHits());
    }

    public static void selectSpecificFields() throws IOException, SqlParseException, SQLFeatureNotSupportedException {
        String[] arr = new String[]{"age", "account_number"};
        Set expectedSource = new HashSet(Arrays.asList(arr));

        SearchHits response = query(String.format("SELECT age, account_number FROM %s/account", TEST_INDEX));
        SearchHit[] hits = response.getHits();
        for (SearchHit hit : hits) {
//            Assert.assertEquals(expectedSource, hit.getSource().keySet());
            System.out.println(hit.getSource());
        }
    }

    private static SearchHits query(String query) throws SqlParseException, SQLFeatureNotSupportedException, SQLFeatureNotSupportedException {
        SearchDao searchDao = ESTest.getSearchDao();
        SqlElasticSearchRequestBuilder select = (SqlElasticSearchRequestBuilder) searchDao.explain(query).explain();
        return ((SearchResponse) select.get()).getHits();
    }


    private static SqlElasticSearchRequestBuilder getRequestBuilder(String query) throws SqlParseException, SQLFeatureNotSupportedException, SQLFeatureNotSupportedException {
        SearchDao searchDao = ESTest.getSearchDao();
        return (SqlElasticSearchRequestBuilder) searchDao.explain(query).explain();
    }

    private static SearchResponse getSearchResponse(String query) throws SqlParseException, SQLFeatureNotSupportedException, SQLFeatureNotSupportedException {
        SearchDao searchDao = ESTest.getSearchDao();
        SqlElasticSearchRequestBuilder select = (SqlElasticSearchRequestBuilder) searchDao.explain(query).explain();
        return ((SearchResponse) select.get());
    }

    public static void main(String[] args) throws Exception {
        ESTest.setUp();
        ESTest.searchTypeTest();
    }

}
