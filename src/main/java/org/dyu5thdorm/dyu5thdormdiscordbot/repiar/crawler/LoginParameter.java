package org.dyu5thdorm.dyu5thdormdiscordbot.repiar.crawler;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@PropertySource("classpath:repair.properties")
public class LoginParameter {
    @Value("${account.id}")
    String userId;
    @Value("${account.password}")
    String password;
    @Value("${account.type}")
    Integer loginType;

    public String getRequestBody() {
        return "txt_userid=" + this.userId +
                "&" +
                "pwd_pwd=" + this.password +
                "&" +
                "hdn_flag=" + this.loginType;
    }
}
