package com.blog.spring.repository;


import com.blog.spring.model.GlobalSettings;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface GlobalSettingsRepository extends CrudRepository<GlobalSettings, Long> {
    @Override
    List<GlobalSettings> findAll();

    @Query("select e.value from GlobalSettings e where e.code = 'STATISTICS_IS_PUBLIC'")
    String isStatisticsPublic();

    @Transactional
    @Modifying
    @Query(value = "insert into global_settings(code,value) values ('MULTIUSER_MODE',?1),('STATISTICS_IS_PUBLIC',?2),('POST_PREMODERATION',?3) on duplicate key update value = values(value);",nativeQuery = true)
    void updateSettings(String multiuserMode,String statisticsIsPublic,String postPremoderation);
}
