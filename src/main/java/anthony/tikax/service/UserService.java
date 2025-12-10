package anthony.tikax.service;


public interface UserService {
    void register(String username, String password);

    void login(String username, String password);
}
