package ru.rtln.authservice.redis.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rtln.authservice.redis.entity.RedisUser;

public interface RedisUserRepository extends CrudRepository<RedisUser, Long> {
}