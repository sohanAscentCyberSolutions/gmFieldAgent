package com.example.md3.data.model.home

import com.example.md3.utils.CASETYPE
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter


data class CalendarCases(
    val time: LocalDateTime,
    val caseType: CASETYPE,
    val caseId: String,
)


fun generateCalendarCasess(): List<CalendarCases> = buildList {
    val currentMonth = YearMonth.now()

    currentMonth.atDay(8).also { date ->
        add(
            CalendarCases(
                date.atTime(14, 0),
                CASETYPE.COMMISSIONING,
                "1234567"
            ),
        )
        add(
            CalendarCases(
                date.atTime(21, 30),
                CASETYPE.BREAKDOWN,
                "1234567"
            ),
        )
    }

    currentMonth.atDay(22).also { date ->
        add(
            CalendarCases(
                date.atTime(13, 20),
                CASETYPE.BREAKDOWN,
                "1234567"
            ),
        )
        add(
            CalendarCases(
                date.atTime(17, 40),
                CASETYPE.BREAKDOWN,
                "1234567"
            ),
        )
    }

    currentMonth.atDay(3).also { date ->
        add(
            CalendarCases(
                date.atTime(20, 0),
                CASETYPE.BREAKDOWN,
                "1234567"
            ),
        )
    }

}

val CalendarCasesDateTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("EEE'\n'dd MMM'\n'HH:mm")


