package cse308.caramel.caramelkitchen.user.service;

import cse308.caramel.caramelkitchen.game.persistence.Game;
import cse308.caramel.caramelkitchen.game.persistence.Recipe;
import cse308.caramel.caramelkitchen.user.persistence.Role;
import cse308.caramel.caramelkitchen.user.persistence.User;
import cse308.caramel.caramelkitchen.user.storage.RoleRepository;
import cse308.caramel.caramelkitchen.user.storage.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class UserDomainService implements UserDetailsService { //removed id because when you put in credentials for login, they don't know your id .

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserDomainService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUsername(String username) {
        return userRepository.findById(username).get();
    }

    public boolean userExists (User user){
        return userRepository.findById(user.getUsername()).isPresent();  // if username is present, true
    }

    public List<User> getAllCreators(){
        return (List<User>)userRepository.findAllCreators();
    }

    public void saveNewUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        Role userRole = roleRepository.findByRole("USER");
        user.getRoles().add(userRole);
        userRepository.save(user);
    }
    public void saveUser(User user){
        userRepository.save(user);
    }
    public void addRecipeToUser(String username, Recipe recipe){
        User user=getUserByUsername(username);
        user.getRecipesCreated().add(recipe);
        saveUser(user);
    }
    public void deleteRecipeFromUser(Recipe recipe){
        User user=getUserByUsername(recipe.getCreator());
        user.getRecipesCreated().remove(recipe);
        saveUser(user);
    }
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findById(username).get();
        if(user != null) {
            List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
            return buildUserForAuthentication(user, authorities);
        } else {
            throw new UsernameNotFoundException("username not found");
        }
    }
    public void saveInProgressGameToUser(String username, Game game){
        User user=getUserByUsername(username);
        user.getGamesInProgress().add(game);
        userRepository.save(user);
    }
    public void saveFinishedGameToUser(String username, Game game){
        //remove from in progress if exist
        User user=getUserByUsername(username);
        for(Game g:user.getGamesInProgress()){
            if (g.getId()==game.getId()){
                user.getGamesInProgress().remove(g);
            }
        }
        //add game to finished
        user.getGamesPlayed().add(game);
        userRepository.save(user);
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach((role) -> {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        });

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }
    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }


}
