package kr.dataportal.google.calendar.infrastructure.benchmark

import kotlinx.coroutines.*
import kr.dataportal.google.calendar.infrastructure.clients.EventList
import kr.dataportal.google.calendar.infrastructure.clients.GoogleCalendarClient
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.system.measureTimeMillis

/**
 * @author Heli
 */
private const val AUTHORIZATION_HEADER_KEY = "Authorization"
private const val AUTHORIZATION_HEADER_VALUE_PREFIX = "Bearer "
private const val AUTHORIZATION_HEADER_VALUE_TOKEN = ""
private const val AUTHORIZATION_HEADER_VALUE = AUTHORIZATION_HEADER_VALUE_PREFIX + AUTHORIZATION_HEADER_VALUE_TOKEN

internal const val API_KEY = ""
internal val header = mapOf(
    AUTHORIZATION_HEADER_KEY to AUTHORIZATION_HEADER_VALUE
)

internal suspend fun GoogleCalendarClient.benchmark(calendarItems: List<*>, identifier: () -> (Any)) {
    identifier()

    val now = ZonedDateTime.now()

    val start = System.currentTimeMillis()
    val eventItems = calendarItems.flatMap {
        val calendarItem = it as? Map<*, *> ?: return@flatMap emptyList()
        calendarSummaries(calendarItem, this, now, header)
    }
    val end = System.currentTimeMillis()


    val scope = CoroutineScope(Dispatchers.IO)
    val ms = calendarItems.map {
        scope.async {
            val calendarItem = it as? Map<*, *> ?: return@async emptyList()
            val eventList = withContext(scope.coroutineContext) {
                eventList(calendarItem, this@benchmark, now, header)
            }
            val items = eventList["items"] as List<*>
            items.mapNotNull {
                val item = it as Map<*, *>
                item["summary"]
            }
        }
    }

    val nonblockingIo = measureTimeMillis { ms.awaitAll() }

    println("Blocking I/O [size=${eventItems.size}]: ${end - start}")
    println("Nonblocking I/O [size=${eventItems.size}]: $nonblockingIo")
}

private fun calendarSummaries(
    calendarItem: Map<*, *>,
    client: GoogleCalendarClient,
    now: ZonedDateTime,
    header: Map<String, Any>
): List<Any> {
    val eventList = eventList(calendarItem, client, now, header)
    val items = eventList["items"] as List<*>
    return items.mapNotNull {
        val item = it as Map<*, *>
        item["summary"]
    }
}

private fun eventList(
    calendarItem: Map<*, *>,
    client: GoogleCalendarClient,
    now: ZonedDateTime,
    header: Map<String, Any>
): EventList {
    val start = System.currentTimeMillis()
    return client.eventList(
        apiKey = API_KEY,
        calendarId = calendarItem["id"].toString(),
        timeMin = now.minusDays(30).truncatedTo(ChronoUnit.SECONDS)
            .format(DateTimeFormatter.ISO_INSTANT),
        timeMax = now.plusDays(30).truncatedTo(ChronoUnit.SECONDS)
            .format(DateTimeFormatter.ISO_INSTANT),
        header = header
    ).apply {
        val end = System.currentTimeMillis()
        println("currentThread: ${Thread.currentThread()} time: ${end - start}")
    }
}
