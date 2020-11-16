package com.example.seccharge;

public class Authinfo {

    private String token;
    private Long userId;
    private boolean emailVerified;
    private boolean introOpened = false;

    public Authinfo(String token, Long userId, boolean emailVerified) {
        this.token = token;
        this.userId = userId;
        this.emailVerified = emailVerified;
    }

    public String getToken() { return token; }
    public void setToken(String token) {
        this.token = token;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public boolean getEmailVerified() {
        return emailVerified;
    }
    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
    public boolean isIntroOpened() { return introOpened; }
    public void setIntroOpened(boolean introOpened) { this.introOpened = introOpened; }

}
