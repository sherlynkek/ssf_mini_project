package sg.edu.nus.iss.Mini.Project.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class ValueRepo {
    
    // Slide 20
    @Autowired
    RedisTemplate<String, String> template;

    // Slide 24: Create/update a value
    public void createValue(String key, String value) {
        template.opsForValue().set(key, value);

        // setIfPresent() or setIfAbsent() also available
    }

    // Slide 25: Retrieve a value
    public String getValue(String key) {
        return template.opsForValue().get(key);
    }

    // Slide 27: Delete a value
    public Boolean deleteValue(String key) {
        return template.delete(key);
    }

    // Slide 26: Increment only works for key with int value
    public void incrementValue(String key) {
        template.opsForValue().increment(key);
    }
    
    public void decrementValue(String key) {
        template.opsForValue().decrement(key);
    }

    public void incrementValueBy(String key, Integer value) {
        template.opsForValue().increment(key, value);
    }
    
    public void decrementValueBy(String key, Integer value) {
        template.opsForValue().decrement(key);
    }

    // Slide 28
    public Boolean checkExists(String key) {
        return template.hasKey(key);
    }
}
