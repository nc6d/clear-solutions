package com.task.test.clearsolutions.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DatePeriodDto {

    @JsonFormat(pattern = "dd.MM.yyyy")
    @JsonProperty("dateFrom")
    private LocalDate dateFrom;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @JsonProperty("dateTo")
    private LocalDate dateTo;
}
