package sg.edu.nus.iss.Mini.Project.repo;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MapRepo {
    
    @Autowired
    @Qualifier("template")
    RedisTemplate<String, String> redisTemplate;

    // day 15: slide 36
    public void create(String redisKey, String hashKey, String hashValue) {
        redisTemplate.opsForHash().put(redisKey, hashKey, hashValue);
    }

    // day 15: slide 37
    public Object get(String redisKey, String hashKey) {
        return redisTemplate.opsForHash().get(redisKey, hashKey);
    }

    // day 15: slide 38
    public Long delete(String redisKey, String hashKey) {
        // returns the number of entries deleted
        return redisTemplate.opsForHash().delete(hashKey, hashKey);
    }

    // day 15: slide 39
    public Boolean keyExists(String redisKey, String hashKey) {
        return redisTemplate.opsForHash().hasKey(redisKey, hashKey);
    }

    // day 15: slide 40 (with slight change because slide shows get keys and get values)
    // <Object, Object> = <hasKeys, hasValues>
    public Map<Object, Object> getEntries(String redisKey) {
        return redisTemplate.opsForHash().entries(redisKey);
    }

    // day 15: slide 40
    public Set<Object> getKeys(String redisKey) {
        return redisTemplate.opsForHash().keys(redisKey);
    }

    public List<Object> getValues(String redisKey) {
        return redisTemplate.opsForHash().values(redisKey);
    }

    // day 15: slide 41
    public Long size(String redisKey) {
        return redisTemplate.opsForHash().size(redisKey);
    }

    // expire duration
    public void expire(String redisKey, Long expireValue) {
        Duration expireDuration = Duration.ofSeconds(expireValue);
        redisTemplate.expire(redisKey, expireDuration);
    }
}