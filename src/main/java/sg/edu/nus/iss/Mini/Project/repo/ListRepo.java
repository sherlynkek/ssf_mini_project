package sg.edu.nus.iss.Mini.Project.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class ListRepo {
    
    @Autowired
    RedisTemplate<String, String> template;

    public void leftPush(String key, String value) {
        template.opsForList().leftPush(key, value);
    }

    public void rightPush(String key, String value) {
        template.opsForList().rightPush(key, value);
    }

    // Slide 30 , 34
    public void leftPop(String key) {
        template.opsForList().leftPop(key, 1);
    }

    public void rightPop(String key) {
        template.opsForList().rightPop(key, 1);
    }

    // Slide 32
    public String get(String key, Integer index) {
        return template.opsForList().index(key, index).toString();
    }

    // Slide 33
    public Long size(String key) {
        return template.opsForList().size(key);
    }

    public List<String> getList(String key) {
        List<String> list = template.opsForList().range(key, 0, -1);

        return list;
    }

    public Boolean deleteItem(String key, String valueToDelete) {
        Boolean isDeleted = false;

        // Long iFound = template.opsForList().indexOf(key, valueToDelete);

        // if (iFound >= 0) {
            template.opsForList().remove(key, 1, valueToDelete);
            isDeleted = true;
        // }

        return isDeleted;
    }

    public void editItem(String key, String valueToEdit, String newValue) {
        Long indexToEdit = template.opsForList().indexOf(key, valueToEdit);
        template.opsForList().set(key, indexToEdit, newValue);
    }
}