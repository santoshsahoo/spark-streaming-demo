/**
 * Created by ssahoo on 7/14/15.
 */

package com.foo.datainsights;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportHeader implements Serializable {

    private static final DateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

    private int entity;
    private int reportId;
    private String title;
    private Date createdDate;
    private String userId;
    private long total;

    public ReportHeader() {

    }

    public ReportHeader(int entity, int reportId, String title, Date createdDate, String userId, long total) {
        this.entity = entity;
        this.reportId = reportId;
        this.title = title;
        this.createdDate = createdDate;
        this.userId = userId;
        this.total = total;
    }

    public String toCsv() {
        return entity + "," + reportId + "," + title + "," + dt.format(createdDate) + "," + userId + "," + total;
    }

    public static ReportHeader parseCsv(String row) {
        ReportHeader rpt = new ReportHeader();
        try {
            String[] vals = row.split(",");
            rpt.entity = Integer.parseInt(vals[0]);
            rpt.reportId = Integer.parseInt(vals[1]);
            rpt.title = vals[2];
            rpt.createdDate = dt.parse(vals[3]);
            rpt.userId = vals[4];
            rpt.total = Long.parseLong(vals[5]);
        } catch (Exception ex) {

        }

        return rpt;
    }

    public int getEntity() {
        return entity;
    }

    public void setEntity(int entity) {
        this.entity = entity;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
