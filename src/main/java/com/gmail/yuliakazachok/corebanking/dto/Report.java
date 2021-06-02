package com.gmail.yuliakazachok.corebanking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Date;

@Data
public class Report {

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date date;

    @JsonProperty("volume_issue")
    private Float volumeIssue;

    private Float percent;

    @JsonProperty("total_profit")
    private Float totalProfit;

    public Report(Date date, Float volumeIssue, Float percent, Float totalProfit) {
        this.date = date;
        this.volumeIssue = volumeIssue;
        this.percent = percent;
        this.totalProfit = totalProfit;
    }

    public Report() {}
}
