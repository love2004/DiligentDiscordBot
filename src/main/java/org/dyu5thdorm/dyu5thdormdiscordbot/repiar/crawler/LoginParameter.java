package org.dyu5thdorm.dyu5thdormdiscordbot.repiar.crawler;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@PropertySource("classpath:repair.properties")
public class LoginParameter {
    @Value("${account.id}") String userId;
    @Value("${account.password}")
    String password;
    @Value("${account.type}")
    Integer loginType;

    public String getRequestBody() {
        return String.format(
                "txt_userid=%s&pwd_pwd=%s&hdn_flag=%s"
                ,this.userId,this.password,this.loginType
        );
    }
}
