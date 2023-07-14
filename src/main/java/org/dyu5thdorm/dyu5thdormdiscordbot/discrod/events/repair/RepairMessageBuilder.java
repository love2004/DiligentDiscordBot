package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.repair;

import org.springframework.stereotype.Component;

@Component
public class RepairMessageBuilder {
    private String basic(RepairMessageModel model) {
        return  "\n" + "學號：" + model.studentId() + "\n" +
                "姓名：" + model.studentName() + "\n" +
                "房號：" + model.studentBedId() + "\n" +
                "電話：" + model.studentPhoneNumber() + "\n";
    }

    public String normal(RepairMessageModel model) {
        return basic(model) +
                "報修物品：" + model.repairItems() + "\n" +
                "報修原因：" + model.repairReason() + "\n" +
                "可配合時間：" + model.repairTime();
    }

    public String washAndDryMachine(RepairMessageModel model) {
        return basic(model) +
                "洗、烘衣機編號：" + model.repairItems() + "\n" +
                "報修原因：" + model.repairReason();
    }

    public String vending(RepairMessageModel model) {
        return basic(model) + "販賣機編號：" + model.repairItems() + "\n" +
                "報修原因：" + model.repairReason();
    }

    public String waterDispenser(RepairMessageModel model) {
        return basic(model) + "飲水機編號：" + model.repairItems() + "\n" +
                "報修原因：" + model.repairReason();
    }
}
