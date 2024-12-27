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

    public void create(String redisKey, String hashKey, String hashValue) {
        redisTemplate.opsForHash().put(redisKey, hashKey, hashValue);
    }

    // Retrieve an individual event by eventId
    public String get(String redisKey, String hashKey) {
        return (String)redisTemplate.opsForHash().get(redisKey, hashKey);
    }
    
    // Delete an event by eventId
    public Long delete(String redisKey, String hashKey) {
        return redisTemplate.opsForHash().delete(redisKey, hashKey); 
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }
    
    // Check if a key exists in Redis
    public Boolean keyExists(String redisKey, String hashKey) {
        return redisTemplate.opsForHash().hasKey(redisKey, hashKey);
    }

    public boolean isEmailTaken(String setKey, String email) {
        return redisTemplate.opsForSet().isMember(setKey, email);
    }

    // Retrieve all events for a particular key
    public Map<Object, Object> getAllEvents(String redisKey) {
        return redisTemplate.opsForHash().entries(redisKey);
    }

    public Set<Object> getKeys(String redisKey) {
        return redisTemplate.opsForHash().keys(redisKey);
    }

    public List<Object> getValues(String redisKey) {
        return redisTemplate.opsForHash().values(redisKey);
    }

    // Store an individual event (it could be by event ID, for example)
    public void addEvent(String redisKey, String eventId, String eventDetails) {
        redisTemplate.opsForHash().put(redisKey, eventId, eventDetails);
    }

    // Store a list of events (copying them individually)
    public void addEvents(String redisKey, List<String> eventIds, List<String> eventDetails) {
        for (int i = 0; i < eventIds.size(); i++) {
            addEvent(redisKey, eventIds.get(i), eventDetails.get(i));
        }
    }

    // Check the size of a hash
    public Long size(String redisKey) {
        return redisTemplate.opsForHash().size(redisKey);
    }

    // Expire a key
    public void expire(String redisKey, Long expireValue) {
        Duration expireDuration = Duration.ofSeconds(expireValue);
        redisTemplate.expire(redisKey, expireDuration);
    }

    

}