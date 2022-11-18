package kr.dataportal.google.calendar.infrastructure.clients

import feign.Headers

/**
 * @author Heli
 */
typealias CalendarList = Map<String, Any>
typealias EventList = Map<String, Any>

@Headers(
    "x-referer: https://explorer.apis.google.com",
    "Accept: application/json"
)
interface NonGzipGoogleCalendarClient : GoogleCalendarClient {

}

