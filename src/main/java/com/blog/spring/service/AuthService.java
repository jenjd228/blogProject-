package com.blog.spring.service;

import com.blog.spring.DTO.CaptchaDTO;
import com.blog.spring.DTO.UserLoginDTO;
import com.blog.spring.Email.SendEmail;
import com.blog.spring.model.CaptchaCodes;
import com.blog.spring.model.Posts;
import com.blog.spring.model.Users;
import com.blog.spring.repository.CaptchaCodesRepository;
import com.blog.spring.repository.PostsRepository;
import com.blog.spring.repository.UserRepository;
import com.github.cage.Cage;
import com.github.cage.GCage;
import net.minidev.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthService {

    private final CaptchaCodesRepository captchaCodesRepository;

    public final UserRepository userRepository;

    public final SendEmail sendEmail;

    private final ModelMapper modelMapperToUserLoginDTO;

    private final PostsRepository postsRepository;

    private HashMap<String, Integer> currentUsers;

    AuthService(PostsRepository postsRepository, CaptchaCodesRepository captchaCodesRepository, UserRepository userRepository, SendEmail sendEmail, ModelMapper modelMapperToUserLoginDTO) {
        this.postsRepository = postsRepository;
        this.modelMapperToUserLoginDTO = modelMapperToUserLoginDTO;
        this.captchaCodesRepository = captchaCodesRepository;
        this.userRepository = userRepository;
        this.sendEmail = sendEmail;
        currentUsers = new HashMap<>();
    }

    public Users findUserBySession(String sessionId) {
        Integer userId = currentUsers.get(sessionId);

        if (userId == null) {
            return null;
        }
        return userRepository.findUsersById(userId);
    }

    public Integer findUserIdBySession(String sessionId){
        return currentUsers.get(sessionId);
    }

    public boolean isModeratorBySessionId(String sessionId) {
        Users user = findUserBySession(sessionId);

        if (user == null) {
            return false;
        }

        return user.getIsModerator() == 1;
    }

    public boolean isWriterBySessionId(Integer postId, String sessionId) {

        Integer userId = currentUsers.get(sessionId);

        if (userId == null) {
            return false;
        }

        Posts post = postsRepository.findPostsById(postId);

        if (post != null) {
            return post.getUserId().equals(userId);
        }

        return false;
    }

    public CaptchaDTO getCaptcha() {
        Cage cage = new GCage();

        String code = cage.getTokenGenerator().next();
        String secretCode = UUID.randomUUID().toString();

        BufferedImage bufferedImage = resize(cage.drawImage(code), 100, 35);
        String resultString = "data:image/png;base64," + imgToBase64String(bufferedImage, "png");

        CaptchaCodes captchaCodes = new CaptchaCodes();
        captchaCodes.setCode(code);
        captchaCodes.setSecretCode(secretCode);
        captchaCodes.setTime(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());

        captchaCodesRepository.save(captchaCodes);

        return new CaptchaDTO(secretCode,resultString);

    }

    public static String imgToBase64String(final BufferedImage img, final String formatName) {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            ImageIO.write(img, formatName, os);
            return Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public void deleteOldCaptcha(){
        Long firstDate = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        captchaCodesRepository.removeOldCaptcha(firstDate);

    }


    public JSONObject register(String email, String password, String name, String captcha, String captchaSecret) {
        JSONObject json = new JSONObject();
        boolean result = true;
        HashMap<String, String> errors = new HashMap<>();
        String code = "";
        CaptchaCodes captchaCodes = captchaCodesRepository.findCaptchaCodesBySecretCode(captchaSecret);

        if (captchaCodes != null) {
            code = captchaCodes.getCode();
        }

        if (userRepository.findByEmail(email) != null) {
            result = false;
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }

        if (!checkPassword(password)) {
            result = false;
            errors.put("password", "Пароль короче 6-ти символов");
        }

        if (!checkName(name)) {
            result = false;
            errors.put("name", "Имя указано неверно");
        }

        if (!captcha.equals(code)) {
            result = false;
            errors.put("captcha", "Код с картинки введён неверно");
        }


        json.put("result", result);

        if (!result) {
            json.put("errors", errors);
        } else {
            Users user = new Users();
            user.setEmail(email);
            user.setName(name);
            user.setPassword(String.valueOf(password.hashCode()));
            user.setIsModerator(0);
            user.setPhoto(null);
            user.setRegTime(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
            user.setCode(null);
            userRepository.save(user);
        }
        // что делать с капчей после успешной регистрации
        return json;
    }

    public JSONObject password(String code, String password, String captcha, String captchaSecret) {
        JSONObject json = new JSONObject();
        Users user = userRepository.findUsersByCode(code);
        CaptchaCodes captchaCodes = captchaCodesRepository.findCaptchaCodesBySecretCode(captchaSecret);

        boolean result = true;
        HashMap<String, String> errors = new HashMap<>();
        String codeCaptcha = "";

        if (captchaCodes != null) {
            codeCaptcha = captchaCodes.getCode();
        }

        if (user == null) {
            result = false;
            errors.put("code", "Ссылка для восстановления пароля устарела.\n" +
                    "<a href=\"/auth/restore\">Запросить ссылку снова</a>");
        }

        if (!captcha.equals(codeCaptcha)) {
            result = false;
            errors.put("captcha", "Код с картинки введён неверно");
        }

        if (!checkPassword(password)) {
            result = false;
            errors.put("password", "Пароль короче 6-ти символов");
        }


        json.put("result", result);
        if (!result) {
            json.put("errors", errors);
        } else {
            user.setCode(null);
            user.setPassword(password);
            userRepository.save(user);
        }

        return json;
    }

    private boolean checkPassword(String password) {
        String passwordPattern = "^.{6,}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    private boolean checkName(String name) {
        String namePattern = "^[a-zа-яA-ZА-Я]+$";
        Pattern pattern = Pattern.compile(namePattern);
        Matcher matcher = pattern.matcher(name);
        return matcher.find();
    }

    public JSONObject restore(String email) {
        JSONObject json = new JSONObject();
        Users user = userRepository.findByEmail(email);
        if (user != null) {
            String code = UUID.randomUUID().toString();

            user.setCode(code);
            userRepository.save(user);

            sendEmail.sendEmail("Ваша ссылка : /login/change-password/" + code, email);
            json.put("result", true);
        } else {
            json.put("result", false);
        }
        return json;
    }

    public JSONObject login(String email, String password) {
        JSONObject json = new JSONObject();
        Users user = userRepository.findByEmail(email);

        if (user == null) {
            json.put("result", false);
            return json;
        }

        if (user.getPassword().equals(password)) {
            json.put("result", true);
            currentUsers.put(RequestContextHolder.currentRequestAttributes().getSessionId(), user.getId());
            json.put("user", getUserLoginDTOByUserConverting(user));
            return json;
        }

        json.put("result", false);
        return json;
    }

    public JSONObject check() {
        JSONObject json = new JSONObject();
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        Users user = findUserBySession(sessionId);

        if (user == null) {
            json.put("result", false);
            return json;
        }

        json.put("result", true);
        json.put("user", getUserLoginDTOByUserConverting(user));

        return json;
    }

    private UserLoginDTO getUserLoginDTOByUserConverting(Users user) {
        UserLoginDTO userLoginDTO = modelMapperToUserLoginDTO.map(user, UserLoginDTO.class);
        if (userLoginDTO.isModeration()) {
            userLoginDTO.setModerationCount(postsRepository.getPostCount());
        } else {
            userLoginDTO.setModerationCount(0);
        }
        return userLoginDTO;
    }

    public JSONObject logout(String sessionId){
        currentUsers.remove(sessionId);
        JSONObject json = new JSONObject();
        json.put("result",true);
        return json;
    }

}
