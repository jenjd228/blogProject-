package com.blog.spring.repository;


import com.blog.spring.model.GlobalSettings;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GlobalSettingsRepository extends CrudRepository<GlobalSettings, Integer> {
    @Override
    List<GlobalSettings> findAll();

    @Query("SELECT e.value FROM GlobalSettings e where e.code = 'STATISTICS_IS_PUBLIC'")
    String isStatisticsPublic();

    //@Query()
   // void settingsUpdate();
}
