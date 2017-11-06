package name.zautner.vitaly.appthis.dao.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * JPA entity definition for User.
 */
@Entity
@Table(name = "user")
public class User implements Serializable {
    @Transient
    final private int ACCOUNT_MAXLEN = 50;
    @Transient
    final private int PASSWORD_MAXLEN = 50;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;

    @NotNull
    @Column(name = "account", length = ACCOUNT_MAXLEN, unique = true, updatable = false, nullable = false)
    private String account;

    @NotNull
    @Column(name = "password", length = PASSWORD_MAXLEN, updatable = true, nullable = false)
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
