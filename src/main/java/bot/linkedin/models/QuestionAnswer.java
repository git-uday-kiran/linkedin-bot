package bot.linkedin.models;

import jakarta.persistence.*;

@Entity
public class QuestionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true, columnDefinition = "text(6000)")
    String question;

    @Column(nullable = false, columnDefinition = "text(6000)")
    String answer;

    @Enumerated(EnumType.STRING)
    Tag tag;

    public QuestionAnswer(String question, String answer, Tag tag) {
        this.question = question;
        this.answer = answer;
        this.tag = tag;
    }

    public QuestionAnswer(Long id, String question, String answer, Tag tag) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.tag = tag;
    }

    public QuestionAnswer() {
    }

    public Long getId() {
        return this.id;
    }

    public String getQuestion() {
        return this.question;
    }

    public String getAnswer() {
        return this.answer;
    }

    public Tag getTag() {
        return this.tag;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
