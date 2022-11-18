package kr.dataportal.google.calendar.infrastructure.clients

import feign.Headers

@Headers(
    "x-referer: https://explorer.apis.google.com",
    "Accept: application/json",
    "Accept-Encoding: gzip",
    "User-Agent: heli-test (gzip)"
)
interface GzipGoogleCalendarClient : GoogleCalendarClient {

}
