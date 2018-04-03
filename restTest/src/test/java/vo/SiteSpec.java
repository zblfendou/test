package vo;

import java.io.Serializable;

public class SiteSpec implements Serializable{
    private static final long serialVersionUID = 8913904617554239737L;
    private String terminal;
    private String language;
    private String functions;
    private String service;

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFunctions() {
        return functions;
    }

    public void setFunctions(String functions) {
        this.functions = functions;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
