package sk.fri.ballay10.caloriepal.objects

import java.util.Calendar

object CalendarProvider {
    val calendar: Calendar = Calendar.getInstance()
    // Calculate unique ID fur current day
    val todayId: Int = calendar.get(android.icu.util.Calendar.DAY_OF_MONTH) + ((calendar.get(android.icu.util.Calendar.MONTH)+1)*100) + (calendar.get(
        android.icu.util.Calendar.YEAR)*10000)
}