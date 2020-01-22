package com.musthafa.springboot.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.musthafa.springboot.models.File;

@Repository
public interface FileRepository extends CrudRepository<File, Integer> {

}
