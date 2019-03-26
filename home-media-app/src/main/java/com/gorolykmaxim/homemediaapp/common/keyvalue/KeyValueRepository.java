package com.gorolykmaxim.homemediaapp.common.keyvalue;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeyValueRepository extends CrudRepository<KeyValue, String> {
    Optional<KeyValue> findByKey(String key);
}
