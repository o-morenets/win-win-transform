package travel.winwin.authapi.domain.processing;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import travel.winwin.authapi.domain.user.User;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "processing_log")
@Getter
@NoArgsConstructor
public class ProcessingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "input_text", nullable = false)
    private String inputText;

    @Column(name = "output_text", nullable = false)
    private String outputText;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public ProcessingLog(User user, String inputText, String outputText) {
        this.user = user;
        this.inputText = inputText;
        this.outputText = outputText;
    }
}