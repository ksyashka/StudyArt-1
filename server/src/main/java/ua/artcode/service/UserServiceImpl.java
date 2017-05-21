package ua.artcode.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import ua.artcode.dao.repositories.SessionRepository;
import ua.artcode.dao.repositories.StudentRepository;
import ua.artcode.dao.repositories.TeacherRepository;
import ua.artcode.exceptions.*;
import ua.artcode.model.Session;
import ua.artcode.model.Student;
import ua.artcode.model.Teacher;
import ua.artcode.model.User;
import ua.artcode.utils.MailUtils;
import ua.artcode.utils.ResultChecker;
import ua.artcode.utils.SecurityUtils;
import ua.artcode.utils.ValidationUtils;


@Service
public class UserServiceImpl implements UserService{

    private final TeacherRepository teacherDB;
    private final StudentRepository studentDB;
    private final ValidationUtils validationUtils;
    private final SecurityUtils securityUtils;
    private final SessionRepository sessionDB;
    private final MailUtils mu;
    private final ResultChecker resultChecker;

    @Value("${email.user}")
    private String emailUsername;

    private static Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(TeacherRepository teacherDB, StudentRepository studentDB, ValidationUtils validationUtils,
                           SecurityUtils securityUtils, SessionRepository sessionDB, MailUtils mu,
                           ResultChecker resultChecker) {
        this.teacherDB = teacherDB;
        this.studentDB = studentDB;
        this.validationUtils = validationUtils;
        this.securityUtils = securityUtils;
        this.sessionDB = sessionDB;
        this.mu = mu;
        this.resultChecker = resultChecker;
    }

    @Override
    public User register(String login, String pass, String email, String type)
            throws InvalidUserLoginException, InvalidUserEmailException, InvalidUserPassException {
        User newUser;

        LOGGER.debug(String.format("Registration params: %s, %s, %s, %s", login, pass, email, type));

        // checking is parameters valid
        validationUtils.validateAllUserFields(login, email, pass);

        // checking does user with this parameters already exist
        validationUtils.checkOriginality(login, email, teacherDB, studentDB);

        if("teacher".equals(type))
            newUser = teacherDB.save(new Teacher(login, securityUtils.encryptPass(pass), email));
        else
            newUser = studentDB.save(new Student(login, securityUtils.encryptPass(pass), email));

        mu.sendEmail(emailUsername, newUser.getEmail(), "Registration", mu.getActivationLink(newUser));

        return newUser;
    }

    @Override
    public User activate(int userId) throws UnexpectedNullException {
        User user = teacherDB.findOne(userId);

        user = user == null ? studentDB.findOne(userId) : user;

        resultChecker.checkNull(user, "User with this id doesn't exists");

        user.activate();

        return user;
    }

    @Override
    public String login(String login, String pass) throws InvalidLoginInfo {
        User loginResult;

        boolean validatedPass = validationUtils.passValidation(pass);

        if(validationUtils.emailValidation(login) && validatedPass) {
            pass = securityUtils.encryptPass(pass);

            User tmp = teacherDB.findByEmail(login);
            loginResult = tmp == null ? studentDB.findByEmail(login) : tmp;

            if(loginResult != null && loginResult.getPass().equals(pass)) {
                Session session = sessionDB.findByUser(loginResult);

                if(session == null)
                    return sessionDB.save(new Session(loginResult)).getAccessKey();
                else
                    return session.getAccessKey();
            }

            throw new InvalidLoginInfo("User doesn't exists");
        }

        if(validationUtils.loginValidation(login) && validatedPass) {
            pass = securityUtils.encryptPass(pass);

            User tmp = teacherDB.findByLogin(login);
            loginResult = tmp == null ? studentDB.findByLogin(login) : tmp;

            if(loginResult != null && loginResult.getPass().equals(pass)) {
                Session session = sessionDB.findByUser(loginResult);

                if(session == null)
                    return sessionDB.save(new Session(loginResult)).getAccessKey();
                else
                    return session.getAccessKey();
            }

            throw new InvalidLoginInfo("User doesn't exists");
        }

        throw new InvalidLoginInfo("User doesn't exists");
    }
}