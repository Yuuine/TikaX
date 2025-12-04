package anthony.tikax.service;

import anthony.tikax.entity.Result;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void register(String username, String password);

    void login(String username, String password);
}
