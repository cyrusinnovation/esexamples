package com.cyrusinnovation.esexamples

import com.cyrusinnovation.esexamples.entities.Airport
import io.searchbox.core.Bulk
import io.searchbox.core.Index
import io.searchbox.core.Search
import org.junit.After
import org.junit.Before
import org.junit.Test

public class SampleEsTest extends EsBaseTest {
    private static final String path = SampleEsTest.getClass().getResource("/AirportCodesUs.csv").path
    private static final String IndexName = "locations"
    private static final String IndexTypeName = "location"

    @Test
    def "can insert documents and query for them"() {
        println(path)
        def airportLocations = new File(path).readLines()
                                  .drop(1) //kill header
                                  .take(100)
                                  .collect {
            def line = it.split(",")
            new Airport(line[0], line[1], line[2])
        }
        def indexAction = new Bulk.Builder()
        airportLocations.each {
            indexAction.addAction(new Index.Builder(it).index(IndexName).type(IndexTypeName).build())
        }
        def indexResult = jestHttpClient.execute(indexAction.build())
        assertTrue(indexResult.errorMessage, indexResult.isSucceeded())

        def searchAction = new Search.Builder("""
{
    "query" : {
        "match_all" : { }
    }
}
""").build()
        refreshIndices(IndexName)
        def searchResult = jestHttpClient.execute(searchAction)
        println(searchResult.jsonString)
        assertTrue(searchResult.errorMessage, searchResult.succeeded)
        searchResult.getHits(Airport).each {
            println(it.source)
        }
    }

    @Before
    private void createTestIndex() {
        def mapping = """
{
    {
        "properties" : {
            "coordinates" : {
                "type" : "geo_point"
            }
        }
    }
}
"""
        this.prepareCreate(IndexName)
            .addMapping(IndexTypeName, mapping)
            .execute()
    }

    @After
    private void destroyTestIndex() {
        cluster().wipeIndices(IndexName)
    }
}


