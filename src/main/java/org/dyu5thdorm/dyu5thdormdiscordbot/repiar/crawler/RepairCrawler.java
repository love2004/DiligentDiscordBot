package org.dyu5thdorm.dyu5thdormdiscordbot.repiar.crawler;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.dyu5thdorm.dyu5thdormdiscordbot.DormWebClient;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.RepairModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
@PropertySource("classpath:repair.properties")
@RequiredArgsConstructor
public class RepairCrawler {
    final
    DormWebClient webClient;
    final
    LoginParameter loginParameter;

    @Value("${api.repair.table}")
    String repairApiURL;
    @Value("${api.repair.login}")
    String loginApiURL;
    @Value("${api.repair.logout}")
    String logoutApiURL;

    @PostConstruct
    void postConstruct() {
        this.webClient.getOptions().setCssEnabled(false);
    }

    public boolean repair(RepairModel parameter) throws IOException {
        if (!logged()) {
            login(loginParameter);
        }
        WebRequest request = new WebRequest(new URL(repairApiURL), HttpMethod.POST);
        request.setRequestBody(
                parameter.getRequestBody()
        );
        return webClient.getPage(request).getWebResponse().getStatusCode() == 200;
    }

    public void login(@NotNull LoginParameter loginParameter) throws IOException {
        WebRequest request = new WebRequest(
                new URL(loginApiURL)
        );
        request.setHttpMethod(HttpMethod.POST);
        String requestBody = loginParameter.getRequestBody();
        request.setRequestBody(requestBody);
        webClient.getPage(request).getWebResponse();
    }

    public boolean logged() throws IOException {
        HtmlPage page = webClient.getPage(repairApiURL);
        var result = page.querySelectorAll("[name=txt_userid]");
        return result.isEmpty();
    }
}