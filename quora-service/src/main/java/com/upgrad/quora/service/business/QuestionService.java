package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public Question createQuestion(Question question) {
        return questionDao.createQuestion(question);
    }

    public List<Question> getAllQuestions() {
        return questionDao.getAllQuestions();
    }

    public List<Question> getAllQuestionsByUser(final String userId) throws UserNotFoundException {
        User user = userDao.getUserByUuid(userId);
        if (user == null) {
            throw new UserNotFoundException("USR-001" , "User with entered uuid whose question details are to be seen does not exist");
        }
        return questionDao.getAllQuestionsByUser(userId);
    }

    public Question getQuestionByUuid(String uuid) throws InvalidQuestionException {
        Question question = questionDao.getQuestionByUuid(uuid);
        if (question == null) {
            throw new InvalidQuestionException("QUES-001" , "Entered question uuid does not exist");
        }
        return question;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Question editQuestion(Question question) {
        questionDao.editQuestion(question);
        return question;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteQuestion(Question question) {
        questionDao.deleteQuestion(question);
    }
}
