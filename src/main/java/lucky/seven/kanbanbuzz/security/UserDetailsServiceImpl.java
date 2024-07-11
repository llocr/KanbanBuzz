package lucky.seven.kanbanbuzz.security;

import lombok.RequiredArgsConstructor;
import lucky.seven.kanbanbuzz.entity.User;
import lucky.seven.kanbanbuzz.exception.ErrorType;
import lucky.seven.kanbanbuzz.exception.UserException;
import lucky.seven.kanbanbuzz.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username).orElseThrow(()->new UserException(ErrorType.DUPLICATE_ACCOUNT_ID));
        return new UserDetailsImpl(user);
    }
}
