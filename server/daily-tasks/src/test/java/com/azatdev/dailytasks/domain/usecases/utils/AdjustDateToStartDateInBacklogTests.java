package com.azatdev.dailytasks.domain.usecases.utils;

import java.time.LocalDate;

import org.springframework.cglib.core.Local;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.usecases.utils.AdjustDateToStartOfBacklog;


class AdjustDateToStartDateInBacklogTests {

    private AdjustDateToStartOfBacklog createSUT() {
        return new AdjustDateToStartDateInBacklogImpl();
    }
    
    @Test
    void calculateDayTest() {
        // Given
        final var sut = createSUT();
        final var date = LocalDate.of(2021, 1, 1);
        final var duration = Backlog.Duration.DAY;
        final var expectedDate = date;
        
        // When
        var result = sut.calculate(date, duration);
        
        // Then
        assertThat(result).isEqualTo(expectedDate);
    }

    @Test
    void calculateWeekShouldAdjustToMondayTest() {
        // Given
        final var sut = createSUT();
        final var wednesdayDate = LocalDate.of(2023, 11, 22);
        final var duration = Backlog.Duration.WEEK;
        final var expectedDate = LocalDate.of(2023, 11, 20);
        
        // When
        var result = sut.calculate(wednesdayDate, duration);
        
        // Then
        assertThat(result).isEqualTo(expectedDate);
    }
}