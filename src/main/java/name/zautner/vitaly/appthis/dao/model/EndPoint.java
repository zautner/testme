package name.zautner.vitaly.appthis.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * End-point is a named URL entity, as we keep it on DB
 * We assume that the users may share entries (named URLs),
 * which is ManyToMany relation.
 * URLs are indexed, as we need to find duplicates quickly.
 */
@Entity
@Table(name = "end_point",
        indexes = {@Index(name = "end_point_url_idx", columnList = "url", unique = true)})
public class EndPoint implements Serializable {
    @Transient
    final private int NAME_MAXLEN = 255;
    @Transient
    final private int URL_MAXLEN = 2083; //as per IE RFC

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;

    @NotNull
    @Column(name = "url", length = URL_MAXLEN, nullable = false, updatable = true)
    private String url;

    @JsonIgnore
    @ManyToMany(targetEntity = User.class)
    @JoinTable(name = "end_point_user",
            indexes = {@Index(name = "end_point_user_user_id_idx", columnList = "user_id"),
                    @Index(name = "end_point_user_end_point_id_idx", columnList = "end_point_id")},
            joinColumns = {@JoinColumn(name = "end_point_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private java.util.Set<User> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
