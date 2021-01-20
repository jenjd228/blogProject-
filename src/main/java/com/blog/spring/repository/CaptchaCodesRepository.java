package com.blog.spring.repository;

import com.blog.spring.model.CaptchaCodes;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CaptchaCodesRepository extends CrudRepository<CaptchaCodes,Long> {

    @Query("select e from CaptchaCodes e where ?2 < e.time+3600 and e.secretCode = ?1")
    CaptchaCodes findCaptchaCodesBySecretCode(String secretCode,Long time);

    @Modifying
    @Transactional
    @Query(value = "delete from captcha_codes where ?1 > captcha_codes.time+3600 and captcha_codes.id > 0;",nativeQuery = true)
    void removeOldCaptcha(Long time);
}
