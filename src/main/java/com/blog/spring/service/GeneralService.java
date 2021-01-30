package com.blog.spring.service;

import com.blog.spring.DTO.*;
import com.blog.spring.model.*;
import com.blog.spring.repository.*;
import net.minidev.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.mail.internet.InternetAddress;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class GeneralService {

    @Value("${domain}")
    private String domain;

    @Qualifier("modelMapperToTagForTagsDTO")
    private final ModelMapper modelMapperToTagForTagsDTO;

    private final ModelMapper modelMapperToStatisticDTO;

    private final Tag2PostRepository tag2PostRepository;

    private final PostsRepository postsRepository;

    private final UserRepository userRepository;

    private final GlobalSettingsRepository globalSettingsRepository;

    private final AuthService authService;

    private final PostVotersRepository postVotersRepository;

    private final PostCommentsRepository postCommentsRepository;

    private final TagsRepository tagsRepository;

    private final PasswordEncoder passwordEncoder;

    public GeneralService(PasswordEncoder passwordEncoder, UserRepository userRepository, TagsRepository tagsRepository, PostCommentsRepository postCommentsRepository, PostVotersRepository postVotersRepository, ModelMapper modelMapperToStatisticDTO, AuthService authService, GlobalSettingsRepository globalSettingsRepository, ModelMapper modelMapperToTagForTagsDTO, PostsRepository postsRepository, Tag2PostRepository tag2PostRepository) {
        this.passwordEncoder = passwordEncoder;
        this.tagsRepository = tagsRepository;
        this.postCommentsRepository = postCommentsRepository;
        this.postVotersRepository = postVotersRepository;
        this.modelMapperToStatisticDTO = modelMapperToStatisticDTO;
        this.authService = authService;
        this.globalSettingsRepository = globalSettingsRepository;
        this.modelMapperToTagForTagsDTO = modelMapperToTagForTagsDTO;
        this.tag2PostRepository = tag2PostRepository;
        this.postsRepository = postsRepository;
        this.userRepository = userRepository;
    }

    public List<TagForTagsDTO> findTagsByQuery(String query) {
        List<TagNameAndWeight> lists;
        List<TagForTagsDTO> tags;

        Long postCount = postsRepository.activePostCount(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
        Double maxWeight;

        if (query == null || query.isEmpty()) {
            lists = tag2PostRepository.findTagsAndSortByCountOfPosts();
        } else {
            lists = tag2PostRepository.findTagsByQueryAndSortByCountOfPosts(query);
        }

        tags = lists.stream().map(this::convertToDto).collect(toList());

        if (tags.size() != 0) {
            for (TagForTagsDTO tag : tags) {
                tag.setNormalWeight(postCount);
            }
            maxWeight = tags.get(0).getWeight();
            double x = (1 / maxWeight);

            for (TagForTagsDTO tag : tags) {
                if (tag.getWeight().equals(maxWeight)) {
                    tag.setWeight(1.);
                } else {
                    tag.setWeight(tag.getWeight() * x);
                    if (tag.getWeight() < 0.3) {
                        tag.setWeight(0.3);
                    }
                }
            }
        }

        return tags;
    }

    private TagForTagsDTO convertToDto(TagNameAndWeight post) {
        return Objects.isNull(post) ? null : modelMapperToTagForTagsDTO.map(post, TagForTagsDTO.class);
    }

    public CalendarDTO getCalendar(String year) {
        List<String> years = postsRepository.getCalendar();
        years.remove(null);

        long dateForFind1;
        long dateForFind2;

        if (year == null || year.isEmpty()) {

            java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getDefault(), java.util.Locale.getDefault());
            calendar.setTime(new java.util.Date());

            String currentYear = String.valueOf(calendar.get(java.util.Calendar.YEAR));

            LocalDate dateForFind = LocalDate.parse(currentYear + "-01-01");

            dateForFind1 = dateForFind.atStartOfDay(ZoneOffset.UTC).toInstant().getEpochSecond();
            dateForFind2 = dateForFind.plusYears(1).atStartOfDay(ZoneOffset.UTC).toInstant().getEpochSecond();

        } else {
            LocalDate dateForFind = LocalDate.parse(year + "-01-01");

            dateForFind1 = dateForFind.atStartOfDay(ZoneOffset.UTC).toInstant().getEpochSecond();
            dateForFind2 = dateForFind.plusYears(1).atStartOfDay(ZoneOffset.UTC).toInstant().getEpochSecond();

        }

        List<List<String>> posts = postsRepository.getCalendarByYear(dateForFind1, dateForFind2);
        HashMap<String, String> postsForDTO = new HashMap<>();

        posts.forEach(para -> postsForDTO.put(para.get(0), para.get(1)));

        return new CalendarDTO(years, postsForDTO);
    }

    public JSONObject getSettings() {
        List<GlobalSettings> settingsList = globalSettingsRepository.findAll();
        JSONObject json = new JSONObject();

        json.put(settingsList.get(0).getCode(), settingsList.get(0).getValue().equals("YES"));
        json.put(settingsList.get(1).getCode(), settingsList.get(1).getValue().equals("YES"));
        json.put(settingsList.get(2).getCode(), settingsList.get(2).getValue().equals("YES"));
        return json;
    }

    public StatisticDTO getAllStatistics() {
        Statistics statistics = postVotersRepository.getStatistics();
        return modelMapperToStatisticDTO.map(statistics, StatisticDTO.class);
    }

    public StatisticDTO getMyStatistics(String sessionId) {
        Integer id = authService.findUserIdBySession(sessionId);
        if (id != null) {
            Statistics statistics = postVotersRepository.getMyStatistics(id);

            return modelMapperToStatisticDTO.map(statistics, StatisticDTO.class);
        }
        return null;
    }

    public void putSettings(SettingsDTO settingsDTO) {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        Users user = authService.findUserBySession(sessionId);

        if (user != null && user.getIsModerator() == 1) {
            String multiuserMode = settingsDTO.isMultiUserMode() ? "YES" : "NO";
            String statisticsIsPublic = settingsDTO.isStatisticIsPublic() ? "YES" : "NO";
            String postPremoderation = settingsDTO.isPostPreModeration() ? "YES" : "NO";
            globalSettingsRepository.updateSettings(multiuserMode, statisticsIsPublic, postPremoderation);
        }
    }

    public boolean isStatisticPublic() {
        return globalSettingsRepository.isStatisticsPublic().equals("YES");
    }

    public ResponseEntity<JSONObject> comment(AddCommentDTO addCommentDTO) {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        Users user = authService.findUserBySession(sessionId);

        if (user != null) {
            JSONObject json = new JSONObject();
            HashMap<String, String> errors = new HashMap<>();

            if (addCommentDTO.getText().isEmpty() || addCommentDTO.getText().length() < 2) {
                json.put("result", false);
                errors.put("text", "Текст комментария не задан или слишком короткий");
                json.put("errors", errors);
                return new ResponseEntity<>(json, HttpStatus.OK);
            }

            Posts post = postsRepository.findPostsById(addCommentDTO.getPostId());
            PostComments postComments = new PostComments();

            if (post != null) {
                postComments.setParentId(null);
                if (addCommentDTO.getParentId() != null) {
                    if (postCommentsRepository.findPostCommentsById(addCommentDTO.getParentId()) != null) {
                        postComments.setParentId(addCommentDTO.getParentId());
                    } else {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                }
                postComments.setPostId(addCommentDTO.getPostId());
                postComments.setTime(LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond());
                postComments.setText(addCommentDTO.getText());
                postComments.setUserId(user.getId());

                PostComments postComments1 = postCommentsRepository.save(postComments);

                json.put("id", postComments1.getId());
                return new ResponseEntity<>(json, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return null;
    }


    public JSONObject updatePost(Integer id, AddPostDTO addPostDTO) {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        Integer userId = authService.findUserIdBySession(sessionId);

        if (userId != null) {
            JSONObject json = new JSONObject();
            HashMap<String, String> errors = new HashMap<>();

            boolean result = true;

            Posts post = postsRepository.findPostsById(id);

            if (addPostDTO.getText().length() < 50) {
                result = false;
                errors.put("text", "Текст публикации слишком короткий");
            }

            if (addPostDTO.getTitle().length() < 3) {
                result = false;
                errors.put("title", "Заголовок слишком короткий");
            }

            json.put("result", result);

            if (result) {

                post.setIsActive(addPostDTO.getActive());
                post.setModerationStatus(ModerationStatus.NEW);
                post.setTitle(addPostDTO.getTitle());
                post.setText(addPostDTO.getText());
                post.setTime(Math.max(addPostDTO.getTimestamp(), LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond()));

                if (addPostDTO.getTags().size() != 0) {
                    addPostDTO.getTags().forEach(tagsRepository::saveIgnoreDuplicateKey);
                    post.setTags(tagsRepository.findTagsIdByNameIn(addPostDTO.getTags()));
                }
                postsRepository.save(post);

                return json;
            }

            json.put("errors", errors);
            return json;
        }
        return null;
    }

    public JSONObject updateProfileWithoutPhoto(UpdateProfileDTO updateProfileDTO) {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        Users user = authService.findUserBySession(sessionId);

        if (user != null) {
            JSONObject json = new JSONObject();
            HashMap<String, String> errors = new HashMap<>();
            boolean result = true;

            if (!checkName(updateProfileDTO.getName())) {
                result = false;
                errors.put("name", "Имя указано неверно");
            } else {
                user.setName(updateProfileDTO.getName());
            }

            if (!user.getEmail().equals(updateProfileDTO.getEmail()) && userRepository.findByEmail(updateProfileDTO.getEmail()) != null) {
                result = false;
                errors.put("email", "Этот e-mail уже зарегистрирован");
            } else {
                user.setEmail(updateProfileDTO.getEmail());
            }

            if (updateProfileDTO.getPassword() != null && !updateProfileDTO.getPassword().isEmpty() && !passwordEncoder.matches(updateProfileDTO.getPassword(), user.getPassword())) {
                if (!checkPassword(updateProfileDTO.getPassword())) {
                    result = false;
                    errors.put("password", "Пароль короче 6-ти символов");
                } else {
                    user.setPassword(passwordEncoder.encode(updateProfileDTO.getPassword()));
                }
            }

            if (updateProfileDTO.getRemovePhoto() != null && updateProfileDTO.getRemovePhoto() == 1) {
                File file = new File(user.getPhoto());
                if (file.exists()) {
                    try {
                        FileUtils.forceDelete(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                user.setPhoto(null);
            }

            json.put("result", result);

            if (result) {
                userRepository.save(user);
                return json;
            }

            json.put("errors", errors);
            return json;
        }
        return null;
    }

    private boolean checkName(String userName) {
        String passwordPravilo = "^[a-zA-Zа-яА-Я]+$";
        Pattern pattern = Pattern.compile(passwordPravilo);
        Matcher matcher = pattern.matcher(userName);
        return matcher.find();
    }

    private boolean checkPassword(String password) {
        String passwordPattern = "^.{6,}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    public JSONObject image(MultipartFile file) {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        Integer userId = authService.findUserIdBySession(sessionId);

        if (userId != null) {

            JSONObject json = new JSONObject();
            HashMap<String, String> errors = new HashMap<>();

            if (file.getSize() > 5242880) {
                errors.put("image", "Размер файла превышает допустимый размер");
                json.put("result", false);
                json.put("errors", errors);
                return json;
            }

            String extension = FilenameUtils.getExtension(file.getOriginalFilename());

            if (extension != null) {
                boolean jpg = extension.equals("jpg");
                boolean png = extension.equals("png");
                if (!(jpg || png)) {
                    json.put("result", false);
                    return json;
                }
            } else {
                json.put("result", false);
                return json;
            }

            String symbols = "abcdefghijklmnopqrstuvwxyz";
            String random = new Random().ints(6, 0, symbols.length())
                    .mapToObj(symbols::charAt)
                    .map(Object::toString)
                    .collect(Collectors.joining());

            String first = random.substring(0, 2);
            String second = random.substring(2, 4);
            String third = random.substring(4, 6);

            String dirStr = "upload/" + first + "/" + second + "/" + third;
            String imageLocalPath = dirStr + "/" + file.getOriginalFilename();
            File dir = new File(dirStr);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            try {
                File img = new File(imageLocalPath);
                img.createNewFile();
                file.transferTo(img.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            json.put("imageLocalPath", domain + "/" + imageLocalPath);
            return json;
        }
        return null;
    }

    public JSONObject updateProfileWithPhoto(UpdateProfileWithPhotoDTO updateProfileWithPhotoDTO) {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        Users user = authService.findUserBySession(sessionId);

        if (user != null) {
            JSONObject json = new JSONObject();
            HashMap<String, String> errors = new HashMap<>();
            boolean result = true;

            if (!user.getName().equals(updateProfileWithPhotoDTO.getName())) {
                if (!checkName(updateProfileWithPhotoDTO.getName())) {
                    result = false;
                    errors.put("name", "Имя указано неверно");
                }
            }

            if (!user.getEmail().equals(updateProfileWithPhotoDTO.getEmail())) {
                if (!user.getEmail().equals(updateProfileWithPhotoDTO.getEmail()) && userRepository.findByEmail(updateProfileWithPhotoDTO.getEmail()) != null) {
                    result = false;
                    errors.put("email", "Этот e-mail уже зарегистрирован");
                }
            }

            if (updateProfileWithPhotoDTO.getPassword() != null) {
                if (!passwordEncoder.matches(updateProfileWithPhotoDTO.getPassword(), user.getPassword())) {
                    if (!checkPassword(updateProfileWithPhotoDTO.getPassword())) {
                        result = false;
                        errors.put("password", "Пароль короче 6-ти символов");
                    } else {
                        user.setPassword(passwordEncoder.encode(updateProfileWithPhotoDTO.getPassword()));
                    }
                }
            }


            if (updateProfileWithPhotoDTO.getRemovePhoto() != 1) {

                String resultImagePath = getPathIfSuccess(updateProfileWithPhotoDTO.getPhoto());

                if (resultImagePath == null) {
                    result = false;
                    errors.put("photo", "Фото слишком большое, нужно не более 5 Мб");
                } else {
                    user.setPhoto(resultImagePath);
                }
            }

            json.put("result", result);

            if (result) {
                user.setEmail(updateProfileWithPhotoDTO.getEmail());
                user.setName(updateProfileWithPhotoDTO.getName());
                userRepository.save(user);
                return json;
            }

            json.put("errors", errors);
            return json;
        }
        return null;
    }

    private BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    private BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getPathIfSuccess(MultipartFile file) {

        if (file.getSize() > 5242880) {
            return null;
        }

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        boolean jpg;
        boolean png;

        if (extension != null) {
            jpg = extension.equals("jpg");
            png = extension.equals("png");
            if (!(jpg || png)) {
                return null;
            }
        } else {
            return null;
        }

        String symbols = "abcdefghijklmnopqrstuvwxyz";
        String random = new Random().ints(6, 0, symbols.length())
                .mapToObj(symbols::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());

        String first = random.substring(0, 2);
        String second = random.substring(2, 4);
        String third = random.substring(4, 6);

        String dirStr ="upload/" + first + "/" + second + "/" + third;
        String imageLocalPath = dirStr + "/" + file.getOriginalFilename();
        File dir = new File(dirStr);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            BufferedImage bufferedImage = resize(createImageFromBytes(file.getBytes()), 36, 36);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (jpg) {
                ImageIO.write(bufferedImage, "jpg", baos);
            } else {
                ImageIO.write(bufferedImage, "png", baos);
            }
            baos.flush();
            baos.close();

            FileOutputStream fileOutputStream = new FileOutputStream(imageLocalPath);
            fileOutputStream.write(baos.toByteArray());

            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageLocalPath;
    }

}
