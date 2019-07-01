package com.kubernetes.serviceMaster.model;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by bambihui on 2019/6/29.
 */

@Transactional
public interface StoryDao extends CrudRepository<Story,Long> {
    public Story findById(long id);
    public List<Story> findAll();
}

