package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Entity
@Table(name = "ANSWER" , schema = "public")
@NamedQueries(
        {
                @NamedQuery(name = "getAnswerByUuid" , query = "select ans from Answer ans where ans.uuid = :uuid"),
                @NamedQuery(name = "getAnswersToQuestion" , query = "select ans from Answer ans where ans.question.uuid = :questionId")
        }

)
public class Answer {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "ANS")
    @Size(max = 500)
    @NotNull
    private String ans;

    @Column(name = "DATE")
    @NotNull
    private ZonedDateTime date;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
