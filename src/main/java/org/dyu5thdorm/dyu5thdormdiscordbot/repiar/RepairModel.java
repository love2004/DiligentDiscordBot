package org.dyu5thdorm.dyu5thdormdiscordbot.repiar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
                this.type.getId(),
                this.building.getId(),
                this.location,
                this.item,
                this.unit.getId(),
                this.amount,
                description + (this.repairTime != null ? ", \n可配合維修時間：" + repairTime : ""),
                reporter.getPhoneNumber(),
                this.reportUnit.getId()
        );
        return URLEncoder.encode(format, "BIG5");
    }

    @Getter
    public enum Building {
        Diligent("05"), LokKwan("23"), FourWilling("07");
        private final String id;
        Building(String id) {
            this.id = id;
        }
    }

    @Getter
    public enum Unit {
        Normal("06");
        private final String id;
        Unit(String id) {
            this.id = id;
        }
    }

    @Getter
    public enum ReportUnit {
        Normal("2700");
        private final String id;
        ReportUnit(String id) {
            this.id = id;
        }
    }
}
