package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.templete.took_coin.embed;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.TookCoin;
import org.dyu5thdorm.dyu5thdormdiscordbot.took_coin.TookCoinHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Component
public class TookCoinEmbed {
    public enum SyntaxWrong {
        MONEY,
        DATE,
        FLOOR,
        FLOOR_AREA
    }

    @Value("${datetime.format}")
    String dateTimeFormat;
    @Value("${date.format}")
    String dateFormat;
    DateTimeFormatter dateFormatter;
    DateTimeFormatter dateTimeFormatter;

    @PostConstruct
    void init() {
        dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
        dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);
    }

    public EmbedBuilder getBySyntaxWrong(@NotNull SyntaxWrong syntaxWrong) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        switch (syntaxWrong) {
            case MONEY -> {
                embedBuilder.setTitle("金錢格式錯誤。");
                embedBuilder.addField("正確格式", "**[1~2000]**", false);
                embedBuilder.addField("正確範例", "500", true);
                embedBuilder.addField("正確範例", "100", true);
                embedBuilder.addField("正確範例", "50", true);
            }
            case DATE -> {
                embedBuilder.setTitle("日期格式錯誤。");
                embedBuilder.addField("正確格式(24小時制)", "**[月+日 時+分]**", false);
                embedBuilder.addField("正確範例", "0807 2007", true);
                embedBuilder.addField("正確範例", "1031 0807", true);
            }
            case FLOOR -> {
                embedBuilder.setTitle("樓層格式錯誤。");
                embedBuilder.addField("正確格式", "**[1~6]**", false);
                embedBuilder.addField("正確範例", "5", true);
                embedBuilder.addField("正確範例", "1", true);
            }
            case FLOOR_AREA -> {
                embedBuilder.setTitle("樓層區域格式錯誤。");
                embedBuilder.addField("正確格式", "**[1~6]+[AB或CD]**", false);
                embedBuilder.addField("正確範例", "1AB", true);
                embedBuilder.addField("正確範例", "3CD", true);
            }
        }
        embedBuilder.setColor(Color.RED);
        return embedBuilder;
    }

    public EmbedBuilder getByReason(TookCoinHandler.FailReason reason) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("登記失敗");

        switch (reason) {
            case DATE_AFTER_NOW -> embedBuilder.setDescription("發生時間不可在未來。請填寫正確的時間格式。");
            case TIME_REPEAT -> embedBuilder.setDescription("此時間段已被您登記過，無法登記。");
        }

        embedBuilder.setColor(Color.RED);
        return embedBuilder;
    }

    public EmbedBuilder getBySearchResult(org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.TookCoin coin) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("吃錢登記紀錄 (未退回)");
        embedBuilder.setDescription("登記紀錄無法自行修改，若要修改請找宿舍幹部。");
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.addField("樓層區域", coin.getFloor().getFloor() + coin.getFloor().getAreaId(), true);
        embedBuilder.addField("機器", getMachineName(coin.getMachine()), true);
        embedBuilder.addField("故障情況說明", coin.getDescription(), true);
        embedBuilder.addField("金額", coin.getCoinAmount().toString(), true);
        embedBuilder.addField("發生時間", coin.getEventTime().format(dateTimeFormatter), true);
        embedBuilder.addField("登記時間", coin.getRecordTime().format(dateTimeFormatter), true);
        embedBuilder.setFooter(coin.getId().toString());
        return embedBuilder;
    }

    public String getMachineName(String machine) {
        if (machine.equalsIgnoreCase("VENDING")) {
            return "販賣機";
        } else if (machine.equalsIgnoreCase("WASH_MACHINE")) {
            return "洗衣機";
        } else if (machine.equalsIgnoreCase("DRYER")) {
            return "烘衣機";
        }
        return "";
    }

    public EmbedBuilder getBackCadreMessage(List<org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.TookCoin> tookCoinList, String discordId) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        var record = tookCoinList.get(0);
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setTitle(tookCoinList.size() == 1 ? "吃錢簽收通知" : "吃錢合併簽收通知");
        embedBuilder.setDescription(
                String.format("<@%s> 已簽收，退款資訊如下：", discordId)
        );
        int returnCoinAmount = tookCoinList.stream().mapToInt(e -> e.getCoinAmount()).sum();
        embedBuilder.addField("應退總額", String.format(
                "**__%d__**", returnCoinAmount
        ), false);
        embedBuilder.addField("學號", record.getStudent().getStudentId(), true);
        embedBuilder.addField("姓名", record.getStudent().getName(), true);
        embedBuilder.addField("簽收時間", LocalDateTime.now().format(dateTimeFormatter), true);
        embedBuilder.setFooter(tookCoinList.size() == 1 ?
                record.getId().toString() :
                tookCoinList.stream().map(TookCoin::getId).toList().toString()
        );

        return embedBuilder;
    }

    public EmbedBuilder getMentionGetCoinMessage(LocalDate returnDate) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(new Color(0x98FB98));
        embedBuilder.setTitle("機器吃錢退費通知");
        embedBuilder.setDescription("您登記的吃錢金額廠商已退費，請您**__攜帶手機__**找值班幹部領取退費。");
        embedBuilder.addField("領取期限", String.format(
                "即日起至 __**%s**__ 以前", returnDate.plusDays(7L)
        ),true);
        embedBuilder.setFooter("若領取時間無法配合，請另與值班幹部另約時間領取。");
        return embedBuilder;
    }

    public EmbedBuilder getBackCadreMessage(org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.TookCoin tookCoin, String discordId) {
        return getBackCadreMessage(
                List.of(tookCoin), discordId
        );
    }
}
