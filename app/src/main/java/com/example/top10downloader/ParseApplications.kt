package com.example.top10downloader


import android.util.Log

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.lang.Exception

class ParseApplications {
    private val TAG = "ParseApplications"
    val applications = ArrayList<FeedEntry>()
    var feedName: String = ""


    fun parse(xmlData: String): Boolean {
//        Log.d(TAG, "parse called with $xmlData")
        var status = true
        var inEntry = false
        var textValue = ""
        var getFeedTitle = true


        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType = xpp.eventType
            var currentrecord = FeedEntry()
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = xpp.name?.toLowerCase()
                when (eventType) {
                    XmlPullParser.START_TAG -> {
//                        Log.d(TAG, "parse: Starting tag for $tagName")
                        if (tagName == "entry") {
                            inEntry = true
                        }

                    }
                    XmlPullParser.TEXT -> textValue = xpp.text
                    XmlPullParser.END_TAG -> {
//                        Log.d(TAG, "parse: Ending tag for $tagName")
                        if (getFeedTitle) {
                            if(tagName == "title"){
                                feedName = textValue
                                Log.d(TAG, feedName)
                                getFeedTitle = false
                            }


                        }
                        if (inEntry) {
                            when (tagName) {
                                "entry" -> {
                                    applications.add(currentrecord)
                                    currentrecord = FeedEntry()
                                }

                                "name" -> currentrecord.name = textValue
                                "artist" -> currentrecord.artist = textValue
                                "releasedate" -> currentrecord.releaseDate = textValue
                                "summary" -> currentrecord.summary = textValue
                                "image" -> currentrecord.imageURL = textValue
                                "id" -> currentrecord.appStoreUrl = textValue

                            }
                        }
                    }
                }
                eventType = xpp.next()
            }
//            for (app in applications) {
//                Log.d(TAG, "***********************")
//                Log.d(TAG, app.toString())
//            }
        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }
        Log.d(TAG, "=-=-=-=-=-=-=-=-=-=-=-=-=-=")

        for (item in applications){
            Log.d(TAG, item.toString())
        }
        Log.d(TAG, "=-=-=-=-=-=-=-=-=-=-=-=-=-=")
        return status
    }
}