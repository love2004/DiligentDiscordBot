package org.dyu5thdorm.dyu5thdormdiscordbot.discrod;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.OnReadyEvent;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.development.*;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.search_student.SearchByDiscord;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.search_student.SearchByName;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.search_student.SearchByRoom;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.admin.search_student.SearchByStudentId;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.auth.OnAuthButtonInteractionEvent;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.auth.OnAuthModalInteractionEvent;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.auth.OnAuthedUserLeaveEvent;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.repair.OnRepairBtnItnEvent;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.repair.OnRepairMenuEvent;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.repair.OnRepairModalEvent;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.req_lev.ReqLevBtnEvent;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.req_lev.ReqLevModalEvent;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.req_lev.cadre.ReqLevCadreBtnEvent;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.took_coin.TookCoinBtnEvent;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.took_coin.TookMoneySearch;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.took_coin.confirm.TookCoinGetAllBtn;
import org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.took_coin.confirm.TookCoinGetBackBtn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor
public class DiscordAPI {
    @Value("${token}")
    String token;
    JDA jda;
    Guild guild;
    @Autowired
    OnReadyEvent onReadyEvent;
    @Autowired
    DevelopmentOperationEvent developmentOperationEvent;
    @Autowired
    GenerateRules generateRules;
    @Autowired
    GenerateRequest generateRequestButton;
    @Autowired
    ShutdownButton shutdownButton;
    @Autowired
    OnAuthButtonInteractionEvent onAuthButtonInteraction;
    @Autowired
    OnAuthModalInteractionEvent onAuthModalInteraction;
    @Autowired
    OnAuthedUserLeaveEvent onAuthedUserLeaveEvent;
    @Autowired
    SearchByDiscord searchByDiscord;
    @Autowired
    SearchByRoom searchByRoom;
    @Autowired
    SearchByStudentId searchByStudentId;
    @Autowired
    SearchByName searchByName;
    @Autowired
    OnRepairBtnItnEvent onRepairBtnItnEvent;
    @Autowired
    OnRepairMenuEvent onRepairMenuEvent;
    @Autowired
    OnRepairModalEvent onRepairModalEvent;
    @Autowired
    GenerateRepair generateRepair;
    @Autowired
    MaintenanceModeButton maintenanceModeButton;
    @Autowired
    GenerateAdmin generateAdmin;
    @Autowired
    TookCoinBtnEvent tookCoinBtnEvent;
    @Autowired
    GenerateAuth generateAuth;
    @Autowired
    TookMoneySearch tookMoneySearch;
    @Autowired
    FloorRoleCorBtn floorRoleCorBtn;
    @Autowired
    ReqLevBtnEvent reqLevBtnEvent;
    @Autowired
    ReqLevModalEvent reqLevModalEvent;
    @Autowired
    GenerateReqLevCadre generateReqLevCadre;
    @Autowired
    ReqLevCadreBtnEvent reqLevCadreBtnEvent;
    @Autowired
    TookCoinGetBackBtn tookCoinGetBackBtn;
    @Autowired
    TookCoinGetAllBtn tookCoinGetAllBtn;
    @Autowired
    TookCoinReturnDate tookCoinReturnDate;

    @PostConstruct
    void init() {
        buildJDA();
    }

    void buildJDA() {
        jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(
                        onReadyEvent,
                        developmentOperationEvent,
                        generateRules,
                        generateRequestButton,
                        generateRepair,
                        shutdownButton,
                        onAuthModalInteraction,
                        onAuthButtonInteraction,
                        onAuthedUserLeaveEvent,
                        searchByDiscord,
                        searchByRoom,
                        searchByStudentId,
                        searchByName,
                        onRepairBtnItnEvent,
                        onRepairMenuEvent,
                        onRepairModalEvent,
                        maintenanceModeButton,
                        generateAdmin,
                        tookCoinBtnEvent,
                        generateAuth,
                        tookMoneySearch,
                        floorRoleCorBtn,
                        reqLevBtnEvent,
                        reqLevModalEvent,
                        generateReqLevCadre,
                        reqLevCadreBtnEvent,
                        tookCoinGetBackBtn,
                        tookCoinGetAllBtn,
                        tookCoinReturnDate
                )
                .build();
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
