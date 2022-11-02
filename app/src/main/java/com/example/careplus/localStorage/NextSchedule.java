package com.example.careplus.localStorage;

public class NextSchedule extends DaySchedule{
    public DaySchedule Monday;
    public DaySchedule Tuesday;
    public DaySchedule Wednesday;
    public DaySchedule Thursday;
    public DaySchedule Friday;
    public DaySchedule Saturday;
    public DaySchedule Sunday;

    public NextSchedule(DaySchedule monday, DaySchedule tuesday, DaySchedule wednesday, DaySchedule thursday, DaySchedule friday, DaySchedule saturday, DaySchedule sunday) {
        super();
        Monday = monday;
        Tuesday = tuesday;
        Wednesday = wednesday;
        Thursday = thursday;
        Friday = friday;
        Saturday = saturday;
        Sunday = sunday;
    }
}
