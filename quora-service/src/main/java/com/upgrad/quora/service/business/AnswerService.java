package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.entity.Answer;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerService {

    @Autowired
    private AnswerDao answerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public Answer createAnswer(Answer answer) {
        return answerDao.createAnswer(answer);
    }

    public Answer getAnswerByUuid(final String uuid) throws AnswerNotFoundException {
        Answer answer = answerDao.getAnswerByUuid(uuid);
        if (answer == null) {
            throw new AnswerNotFoundException("ANS-001" , "Entered answer uuid does not exist");
        }
        return answer;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Answer editAnswer(Answer answer) {
        answerDao.editAnswer(answer);
        return answer;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAnswer(Answer answer) {
        answerDao.deleteAnswer(answer);
    }

    public List<Answer> getAnswersToQuestion(final String questionId) {
        return answerDao.getAnswersToQuestion(questionId);
    }
}
