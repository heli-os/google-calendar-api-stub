package kr.dataportal.google.calendar

import feign.Feign
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import kr.dataportal.google.calendar.infrastructure.benchmark.API_KEY
import kr.dataportal.google.calendar.infrastructure.benchmark.benchmark
import kr.dataportal.google.calendar.infrastructure.benchmark.header
import kr.dataportal.google.calendar.infrastructure.clients.GzipGoogleCalendarClient
import kr.dataportal.google.calendar.infrastructure.clients.GzipOptionalFieldGoogleCalendarClient
import kr.dataportal.google.calendar.infrastructure.clients.NonGzipGoogleCalendarClient
import kr.dataportal.google.calendar.infrastructure.clients.OptionalFieldGoogleCalendarClient

/**
 * @author Heli
 */
suspend fun main() {
    val client = Feign.builder()
        .encoder(JacksonEncoder())
        .decoder(JacksonDecoder())
        .target(NonGzipGoogleCalendarClient::class.java, "https://www.googleapis.com")

    val calendarList = client.calendarList(
        apiKey = API_KEY,
        header = header
    )
    val calendarItems = calendarList["items"] as List<*>

    client.benchmark(calendarItems) { println("===== non gzip =====") }

    Feign.builder()
        .encoder(JacksonEncoder())
        .decoder(JacksonDecoder())
        .target(GzipGoogleCalendarClient::class.java, "https://www.googleapis.com")
        .benchmark(calendarItems) { println("===== gzip =====") }

    Feign.builder()
        .encoder(JacksonEncoder())
        .decoder(JacksonDecoder())
        .target(OptionalFieldGoogleCalendarClient::class.java, "https://www.googleapis.com")
        .benchmark(calendarItems) { println("===== optional field =====") }

    Feign.builder()
        .encoder(JacksonEncoder())
        .decoder(JacksonDecoder())
        .target(GzipOptionalFieldGoogleCalendarClient::class.java, "https://www.googleapis.com")
        .benchmark(calendarItems) { println("===== gzip optional field  =====") }
}

