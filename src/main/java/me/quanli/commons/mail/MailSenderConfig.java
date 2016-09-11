package me.quanli.commons.mail;

public class MailSenderConfig {

    /**
     * email sending protocol
     */
    private String protocol;

    /**
     * mail server port
     */
    private Integer port;

    /**
     * email server
     */
    private String host;

    /**
     * email user
     */
    private String user;

    /**
     * password for the user
     */
    private String password;

    /**
     * use security connection
     */
    private Boolean useSSL = false;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean usingSSL() {
        return useSSL;
    }

    public void setUseSSL(Boolean useSSL) {
        this.useSSL = useSSL;
    }

    @Override
    public String toString() {
        return "MailSenderConfig [protocol=" + protocol + ", host=" + host
                + ", user=" + user + ", port=" + port + ", useSSL=" + useSSL
                + "]";
    }

}
