package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.sche;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.DiscordAPI;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.Identity.ChannelIdSet;
import org.dyu5thdorm.dyu5thdormdiscordbot.took_coin.TookCoinHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoDumpTookCoin {
    final DiscordAPI discordAPI;
    final ChannelIdSet channelIdSet;
    final TookCoinHandler tookCoinHandler;

    @Value("${path-vending}")
    String vendingPath;
    @Value("${path-washing-and-dryer}")
    String washingAndDryerPath;
    @Value("${date.format}")
    String dateFormat;

    @PostConstruct
    void setup() {
        String currentDir = System.getProperty("user.dir");
        vendingPath = currentDir + vendingPath;
        washingAndDryerPath = currentDir + washingAndDryerPath;
    }

//    @Scheduled(cron = "0 0 10 * * THU")
    @Scheduled(cron = "*/5 * * * * *")
    void dump() {
        Optional<TextChannel> channelOptional = Optional.ofNullable(
                discordAPI.getJda().getTextChannelById(
                        channelIdSet.getLeader()
                )
        );

        if (channelOptional.isEmpty()) {
            log.error("Can not find leader channel");
            return;
        }

        TextChannel channel = channelOptional.get();

        try {
            tookCoinHandler.dumpFile();
        } catch (IOException e) {
            e.printStackTrace();
            channel.sendMessage("在生成檔案時發生錯誤，請檢查").queue();
        }

        List<FileUpload> fileUploads = this.getFileUpload();

        LocalDateTime now = LocalDateTime.now();
        if (fileUploads.isEmpty()) {
            channel.sendMessage(
                    String.format(
                            "# %s 無吃錢登記記錄", DateTimeFormatter.ofPattern(dateFormat).format(now)
                    )
            ).queue();
        } else {
            channel.sendMessage(
                    String.format(
                            """
                            # %s 吃錢登記記錄如附檔：
                            > 請下載後使用 Excel 檔案檢視
                            """, DateTimeFormatter.ofPattern(dateFormat).format(now)
                    )
            ).setFiles(fileUploads).queue();
        }
    }

    List<FileUpload> getFileUpload() {
        List<File> files = List.of(
                this.getFile(TookCoinHandler.MachineType.VENDING),
                this.getFile(TookCoinHandler.MachineType.WASH_MACHINE)
        );

        List<FileUpload> fileUploads = new ArrayList<>();

        for (File file : files) {
            if (!file.exists()) continue;
            fileUploads.add(
                    FileUpload.fromData(file)
            );
        }
        return fileUploads;
    }

    File getFile(TookCoinHandler.MachineType type) {
        File file;
        Path path;

        LocalDateTime now = LocalDateTime.now();
        if (type == TookCoinHandler.MachineType.VENDING) {
             path = Path.of(
                     vendingPath + "v-"+
                             DateTimeFormatter.ofPattern(dateFormat).format(now) +
                             ".csv"
             );
        } else if (type == TookCoinHandler.MachineType.WASH_MACHINE || type == TookCoinHandler.MachineType.DRYER) {
            path = Path.of(
                    washingAndDryerPath + "wd-" +
                            DateTimeFormatter.ofPattern(dateFormat).format(now) +
                            ".csv"
            );
        } else {
            throw new RuntimeException("can not find file");
        }
        file = path.toFile();
        return file;
    }
}
