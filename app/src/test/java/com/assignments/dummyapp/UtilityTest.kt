package com.assignments.dummyapp

import com.assignments.dummyapp.utils.Utility
import org.hamcrest.CoreMatchers.equalTo
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.util.*
import java.util.Calendar.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UtilityTest {

    private val jsonResponse = JSONArray()

    @Before
    fun setUp() {
        val obj = JSONObject()
        obj.put("author", "Alex Wilhelm")
        obj.put("title", "Is this what an early-stage slowdown looks like?")
        obj.put(
            "description",
            "Hello and welcome back to our regular morning look at private companies, public markets and the gray space in between. Today we’re exploring some fascinating data from Silicon Valley Bank markets report for Q1 2020. We’re digging into two charts that deal wit…"
        )
        obj.put(
            "url",
            "http://techcrunch.com/2020/02/10/is-this-what-an-early-stage-slowdown-looks-like/"
        )
        obj.put(
            "urlToImage",
            "https://techcrunch.com/wp-content/uploads/2020/02/GettyImages-dv1637047.jpg?w=556"
        )
        obj.put("publishedAt", "2020-02-10T17:06:42Z")
        obj.put(
            "content",
            "Hello and welcome back to our regular morning look at private companies, public markets and the gray space in between.\r\nToday we’re exploring some fascinating data from Silicon Valley Bank markets report for Q1 2020. We’re digging into two charts that deal wi… [+648 chars]"
        )
        jsonResponse.put(obj)
    }

    @Test
    fun test_json_to_article_conversion() {
        val articles = Utility.convertJsonArrToArticleList(jsonResponse)
        articles.forEachIndexed { index, articleModel ->
            val jsonObject = jsonResponse[index] as JSONObject
            assertEquals(jsonObject["author"], articleModel.author)
            assertEquals(jsonObject["title"], articleModel.title)
            assertEquals(jsonObject["description"], articleModel.description)
            assertEquals(jsonObject["url"], articleModel.url)
            assertEquals(jsonObject["urlToImage"], articleModel.imageUrl)
            assertEquals(jsonObject["publishedAt"], articleModel.publishedAt)
            assertEquals(jsonObject["content"], articleModel.content)
        }
    }

    @Test
    fun test_getFormattedDateTime() {

        val oldStr = "13-01-1994:12:14:34.123"
        val oldPattern = "dd-MM-yyyy:HH:mm:ss.SSS"
        val newPattern = "MMM dd yyyy:HH:mm:ss"
        val newStr = Utility.getFormattedDateTime(oldStr, oldPattern, newPattern)
        assertThat("Jan 13 1994:12:14:34", equalTo(newStr))
    }

    /*@Test
    fun text_getDateFromString() {

        val dateStr1 = "21-01-1995"
        val pattern1 = "dd-MM-yyyy"
        val date1 = Utility.getDateFromString(dateStr1, pattern1)

        val dateStr2 = "14-12-1993:20:50"
        val pattern2 = "dd-MM-yyyy:HH:mm"
        val date2 = Utility.getDateFromString(dateStr2, pattern2)

        val dateStr3 = "13-07-1994:12:14:34.123"
        val pattern3 = "dd-MM-yyyy:HH:mm:ss.SSS"
        val date3 = Utility.getDateFromString(dateStr3, pattern3)

        assertYMD(date1, 21, 1, 1995)
        assertYMD(date2, 14, 12, 1993, 20, 50)
        assertYMD(date3, 13, 7, 1994, 12, 14, 34, 123)
    }

    private fun assertYMD(
        date: Date?,
        day: Int,
        month: Int,
        year: Int,
        hour: Int? = null,
        minute: Int? = null,
        second: Int? = null,
        millis: Int? = null
    ) {

        val calendar = getInstance()
        calendar.time = date as Date
        assertEquals(day, calendar.get(DAY_OF_MONTH))
        assertEquals(month, calendar.get(MONTH) + 1)
        assertEquals(year, calendar.get(YEAR))
        hour?.let {
            assertEquals(hour, calendar.get(HOUR_OF_DAY))
            assertEquals(minute, calendar.get(MINUTE))
            second?.let {
                assertEquals(second, calendar.get(SECOND))
                assertEquals(millis, calendar.get(MILLISECOND))
            }
        }
    }*/
}
