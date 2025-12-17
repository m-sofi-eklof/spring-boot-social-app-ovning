package se.jensen.sofi_n.social_app.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.jensen.sofi_n.social_app.model.User;
import se.jensen.sofi_n.social_app.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    //repo för att hämta user objekten
    private final UserRepository userRepository;

    //dependency injection
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //spring anropar vid inloggning
    //username får från login formuläret
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        //hämta user från db
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Användare med användarnamn: " + username +"hittades ej."));
        //wrappa klass User i MyUserDetails
        return new MyUserDetails(user);
    }
}
