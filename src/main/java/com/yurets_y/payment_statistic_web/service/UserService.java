package com.yurets_y.payment_statistic_web.service;



import com.yurets_y.payment_statistic_web.entity.User;
import com.yurets_y.payment_statistic_web.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    private PasswordEncoder passEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passEncoder) {
        this.userRepository = userRepository;
    }

    public UserService() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        TODO Заменить шаблонные фразы
        if ((username == null) || (username.equals(""))) {
            throw new UsernameNotFoundException("User not found!");
        }
        UserDetails user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }

        return user;
    }

    public void addNewUser(User user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        confirmUser(newUser);
        userRepository.save(newUser);

    }

    public void deleteUser(User user) {
        if (user != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            userRepository.delete(user);
        }
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void confirmUser(User user) {
        user.setActive(true);
    }

    public boolean nameIsBusy(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean emailIsBusy(String email) {
        return userRepository.existsByEmail(email);
    }

    @Autowired
    public void setPassEncoder(PasswordEncoder passEncoder) {
        this.passEncoder = passEncoder;
    }
}
