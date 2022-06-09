package com.example.gocook_;

/**
 * 사용자 계정 정보 모델 클래스
 */

public class UserAccount {

    private String idToken;         // Firebase Uid (고유 토큰정보)
    private String emailId;         // 이메일 아이디
    private String password;        // 비밀번호
    private String name;            // 이름
    private String phone_number;    // 전화번호
    private String gender;          // 성별

    public UserAccount() { }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
