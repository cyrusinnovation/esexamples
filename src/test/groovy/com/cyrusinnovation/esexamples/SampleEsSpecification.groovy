package com.cyrusinnovation.esexamples

import io.searchbox.client.JestClientFactory
import io.searchbox.client.config.HttpClientConfig
import io.searchbox.client.http.JestHttpClient
import io.searchbox.indices.IndicesExists
import org.junit.Test

import java.util.concurrent.TimeUnit

public class SampleEsSpecification extends EsBaseSpecification {
    private static final String TestIndexName = "test-index"

    @Test
    def "can insert documents and query for them"() {
        this.createIndex(TestIndexName)

        def factory = new JestClientFactory()
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder("http://localhost:9200")
                .discoveryEnabled(false)
                .discoveryFrequency(500l, TimeUnit.MILLISECONDS).build())
        def jestClient = (JestHttpClient) factory.getObject()

        def result = jestClient.execute(new IndicesExists.Builder(TestIndexName).build())
        def result2 = jestClient.execute(new IndicesExists.Builder(TestIndexName + "2").build())

        def asBoolean = result.jsonObject.get('found').asBoolean
        def asBoolean2 = result2.jsonObject.get('found').asBoolean

        assertTrue("Could not execute index exists query", asBoolean)
        assertFalse("Could not execute index exists query", asBoolean2)
        jestClient.shutdownClient()
    }
}

