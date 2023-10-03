package org.dyu5thdorm.dyu5thdormdiscordbot.repiar.crawler;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.dyu5thdorm.dyu5thdormdiscordbot.DormWebClient;
import org.dyu5thdorm.dyu5thdormdiscordbot.repiar.RepairModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
@PropertySource("classpath:repair.properties")
public class RepairCrawler {
    @Value("${api.repair}")
    String repairApiURL;
    @Value("${api.login}")
    String loginApiURL;
    @Value("${api.logout}")
    String logoutApiURL;
    final
    DormWebClient webClient;
    final
    LoginParameter loginParameter;

    public RepairCrawler(DormWebClient webClient, LoginParameter loginParameter) {
        this.webClient = webClient;
        this.loginParameter = loginParameter;
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

    public void login(LoginParameter loginParameter) throws IOException {
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

    public WebResponse logout() throws IOException {
        return webClient.getPage(new URL(logoutApiURL)).getWebResponse();
    }
}