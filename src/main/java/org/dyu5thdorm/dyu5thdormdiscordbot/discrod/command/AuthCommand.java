package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.command;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class AuthCommand implements Command {
    private String studentId;
    private String bedId;

    @Override
    public void execute() {

    }
}
