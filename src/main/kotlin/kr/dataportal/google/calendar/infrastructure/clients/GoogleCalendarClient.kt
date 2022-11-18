package kr.dataportal.google.calendar.infrastructure.clients

import feign.HeaderMap
import feign.Param
import feign.RequestLine

/**
 * @author Heli
 */
interface GoogleCalendarClient {
    @RequestLine("GET /calendar/v3/users/me/calendarList?key={apiKey}")
    fun calendarList(
        @Param("apiKey") apiKey: String,
        @HeaderMap header: Map<String, Any>
    ): CalendarList

    @RequestLine("GET /calendar/v3/calendars/{calendarId}/events?timeMin={timeMin}&timeMax={timeMax}&key={apiKey}")
    fun eventList(
        @Param("apiKey") apiKey: String,
        @Param("calendarId") calendarId: String,
        @Param("timeMin") timeMin: String,
        @Param("timeMax") timeMax: String,
        @HeaderMap header: Map<String, Any>
    ): EventList
}
