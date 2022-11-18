package kr.dataportal.google.calendar.infrastructure.clients

import feign.HeaderMap
import feign.Headers
import feign.Param
import feign.RequestLine

@Headers(
    "x-referer: https://explorer.apis.google.com",
    "Accept: application/json"
)
interface OptionalFieldGoogleCalendarClient : GoogleCalendarClient {

    @RequestLine("GET /calendar/v3/calendars/{calendarId}/events?timeMin={timeMin}&timeMax={timeMax}&key={apiKey}&fields=items(summary)")
    override fun eventList(
        @Param("apiKey") apiKey: String,
        @Param("calendarId") calendarId: String,
        @Param("timeMin") timeMin: String,
        @Param("timeMax") timeMax: String,
        @HeaderMap header: Map<String, Any>
    ): EventList
}
