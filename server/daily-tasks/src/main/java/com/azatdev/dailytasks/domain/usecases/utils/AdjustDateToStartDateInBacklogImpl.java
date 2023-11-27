package com.azatdev.dailytasks.domain.usecases.utils;

import java.time.LocalDate;

import com.azatdev.dailytasks.domain.models.Backlog;

public class AdjustDateToStartDateInBacklogImpl implements AdjustDateToStartOfBacklog {

    private LocalDate adjustToMonday(LocalDate date) {

        var dayOfWeek = date.getDayOfWeek()
            .getValue();
        var daysToAdjust = dayOfWeek - 1;
        return date.minusDays(daysToAdjust);
    }

    @Override
    public LocalDate calculate(
        LocalDate date,
        Backlog.Duration duration
    ) {

        switch (duration) {
        case DAY:
            return date;
        case WEEK:
            return adjustToMonday(date);
        }

        return date;
    }
}
