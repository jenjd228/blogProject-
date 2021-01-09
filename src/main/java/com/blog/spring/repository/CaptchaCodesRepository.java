package com.blog.spring.repository;

import com.blog.spring.model.CaptchaCodes;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CaptchaCodesRepository extends CrudRepository<CaptchaCodes,Long> {

    CaptchaCodes findCaptchaCodesBySecretCode(String secretCode);

    @Modifying
    @Transactional
    @Query(value = "delete from captcha_codes where captcha_codes.time < ?1 AND captcha_codes.id > 0;",nativeQuery = true)
    void removeOldCaptcha(Long firstDate);
}
