package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.Answer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Answer createAnswer(Answer answer) {
        entityManager.persist(answer);
        return answer;
    }

    public Answer getAnswerByUuid(final String uuid) {
        try {
           return entityManager.createNamedQuery("getAnswerByUuid" , Answer.class).setParameter("uuid" , uuid).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Answer editAnswer(Answer answer) {
        entityManager.persist(answer);
        return answer;
    }

    public void deleteAnswer(Answer answer) {
        entityManager.remove(answer);
    }

    public List<Answer> getAnswersToQuestion(final String questionId) {
        return entityManager.createNamedQuery("getAnswersToQuestion" , Answer.class).setParameter("questionId" , questionId).getResultList();
    }

}
