package org.chernatkin.spring.boot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping(params={"lastName"})
    public List<User> getByLastName(@RequestParam("lastName") String name, 
                                    @RequestParam(required = true) int page, 
                                    @RequestParam(required = true) int size) {
        return userRepository.findByLastName(name, new PageRequest(page, size, new Sort(Direction.ASC, "firstName"))).getContent();
    }
    
    @PostMapping
    public User save(@RequestBody User user) {
        return userRepository.save(user);
    }
}
