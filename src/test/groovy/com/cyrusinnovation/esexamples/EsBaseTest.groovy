package com.cyrusinnovation.esexamples

import io.searchbox.client.JestClient
import io.searchbox.client.JestClientFactory
import io.searchbox.client.config.HttpClientConfig
import io.searchbox.client.http.JestHttpClient
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest
import org.elasticsearch.test.ElasticsearchIntegrationTest
import org.junit.AfterClass
import org.junit.Before

import java.util.concurrent.TimeUnit

abstract class EsBaseTest extends ElasticsearchIntegrationTest {
    private static JestHttpClient JestHttpClientHolder
    JestClient jestHttpClient

    void refreshIndices(String... indices) {
        client().admin().indices().refresh(new RefreshRequest(indices)).actionGet();
    }

    private static JestHttpClient getJestClient() {
        if(JestHttpClientHolder == null) {
            def factory = new JestClientFactory()
            factory.setHttpClientConfig(new HttpClientConfig
                    .Builder("http://localhost:9200")
                    .discoveryEnabled(false)
                    .discoveryFrequency(500l, TimeUnit.MILLISECONDS).build())
            JestHttpClientHolder = factory.getObject() as JestHttpClient
        }
        return JestHttpClientHolder
    }

    private static void stopJestClient() {
        JestHttpClientHolder.shutdownClient()
        JestHttpClientHolder = null
    }

    @Before
    private void createJestClientBeforeClass() {
        this.jestHttpClient = getJestClient()
    }

    @AfterClass
    private static void shutdownJestClientAfterClass() {
        stopJestClient()
    }
}

