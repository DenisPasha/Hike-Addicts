package bg.softuni.pathfinder.model.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.prefs.BackingStoreException;

@Entity(name = "tokens")
public class EmailConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token ;

    @OneToOne
    private User user;

    private LocalDateTime expireAt;

    public EmailConfirmationToken() {
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(LocalDateTime expireAt) {
        this.expireAt = expireAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
