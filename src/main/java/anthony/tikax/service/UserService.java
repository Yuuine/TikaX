package anthony.tikax.service;

import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void register(String username, String password);
}
