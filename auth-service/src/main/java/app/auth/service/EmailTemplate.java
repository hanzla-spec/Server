package app.auth.service;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class EmailTemplate {

    private String template;

    public EmailTemplate(String customTemplate) {

        try {
            this.template = loadTemplate(customTemplate);
        } catch (Exception e) {
            this.template = "findAll service says sorry for not able to generate OTP";
        }

    }
    private String loadTemplate(String customTemplate) throws Exception {

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(customTemplate)).getFile());
        String content = "findAll service says sorry for not able to generate OTP";
        try {
            content = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new Exception("Could not read template  = " + customTemplate);
        }
        return content;

    }
    public String getTemplate(Map<String,String> replacements) {

        String cTemplate = this.template;
        Set<Map.Entry<String,String>> s = replacements.entrySet();
        for (Map.Entry<String,String> entry: s) {
            cTemplate = cTemplate.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return cTemplate;
    }
}
