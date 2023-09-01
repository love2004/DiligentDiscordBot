package org.dyu5thdorm.dyu5thdormdiscordbot.repiar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class RepairModel {
    Repair.Type type;
    Student reporter;
    Building building;
    String location;
    String item;
    Unit unit;
    Integer amount;
    String description;
    String repairTime;
    ReportUnit reportUnit;

    public String getRequestBody() throws UnsupportedEncodingException {
        return "htmldw1_action=" + "Update" +
                "&" +
                "htmldw1_context=" + getRepairEncoding() +
                "&" +
                "htmldw1_extradata=" + "F0860";
    }

    private String getRepairEncoding() throws UnsupportedEncodingException {
        String builder = "(htmldw1 0)(18621 (InsertRow 0 ((1 1 )(2 1 )(3 1 )(4 0 '%s')(5 1 )(6 1 )(7 1 )(8 1 )(9 1 )(10 1 )(11 1 )(12 1 )(13 1 )(14 1 )(15 1 )(16 1 )(17 1 )(18 1 )(19 1 )(20 1 ))))" +
                "((ModifyRow 0 ((1 1 )) ((3 0 '%s')(5 0 '%s')(6 0 '%s')(7 0 '%s')(8 0 '%s')(9 0 '%s')(10 0 '%s')(12 0 '%s')(17 0 '%s')))(row 0))";
        String format = String.format(
                builder,
                "F0860",
                getTypeCode(this.type),
                getBuildingCode(this.building),
                this.location,
                this.item,
                getUnitCode(this.unit),
                this.amount,
                description + (this.repairTime != null ? ", \n可配合維修時間：" + repairTime : ""),
                reporter.getPhoneNumber(),
                getReportUnitCode(this.reportUnit)
        );
        return URLEncoder.encode(format, "BIG5");
    }

    public enum Building {
        Diligent,
        LokKwan,
        FourWilling
    }

    public enum Unit {
        Normal
    }

    public enum ReportUnit {
        Normal
    }

    String getTypeCode(Repair.Type type) {
        switch (type) {
            case CIVIL -> {
                return "001";
            }
            case HYDRO -> {
                return "002";
            }
            case DOOR -> {
                return "014";
            }
            case AIR_COND -> {
                return "005";
            }
            default -> {
                return "006";
            }
        }
    }

    String getBuildingCode(Building building) {
        switch (building) {
            case LokKwan -> {
                return  "23";
            }
            case FourWilling -> {
                return "07";
            }
            default -> {
                return "05";
            }
        }
    }

    String getUnitCode(Unit unit) {
        return "06";
    }

    String getReportUnitCode(ReportUnit unit) {
        return "2700";
    }
}
