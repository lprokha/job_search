package kg.attractor.job_search.model;

import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private String surname;
    private int age;
    private String email;
    private String password;
    private String phoneNumber;
    private String avatar;
    private AccountType accountType;

    public User(int id, String name, String surname, int age, String email, String password, String phoneNumber, String avatar, AccountType accountType) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.avatar = avatar;
        this.accountType = accountType;
    }
}
