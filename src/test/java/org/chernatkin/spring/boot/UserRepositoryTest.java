package org.chernatkin.spring.boot;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    
    @Before
    public void before(){
        saveUser("John", "Doe");
        saveUser("John1", "Doe");
        saveUser("John2", "Doe");
        saveUser("John", "Smith");
        saveUser("John1", "Smith");
        saveUser("John2", "Smith");
        Assert.assertEquals(6, userRepository.count());
    }
    
    @After
    public void after(){
        userRepository.deleteAll();
        Assert.assertEquals(0, userRepository.count());
    }
    
    @Test
    public void getByLastNamePageDescTest(){
        
        Page<User> page = userRepository.findByLastName("Doe", new PageRequest(0, 2, Direction.DESC, "firstName"));
        Assert.assertEquals(3, page.getTotalElements());
        Assert.assertEquals(2, page.getNumberOfElements());
        Assert.assertEquals(2, page.getTotalPages());
        
        Assert.assertEquals("John2" ,page.getContent().get(0).getFirstName());
        Assert.assertEquals("John1" ,page.getContent().get(1).getFirstName());
        
        
        page = userRepository.findByLastName("Doe", new PageRequest(1, 2, Direction.DESC, "firstName"));
        Assert.assertEquals(3, page.getTotalElements());
        Assert.assertEquals(1, page.getNumberOfElements());
        Assert.assertEquals(2, page.getTotalPages());
        
        Assert.assertEquals("John" ,page.getContent().get(0).getFirstName());
    }
    
    @Test
    public void getByLastNamePageAscTest(){
        
        Page<User> page = userRepository.findByLastName("Doe", new PageRequest(0, 2, Direction.ASC, "firstName"));
        Assert.assertEquals(3, page.getTotalElements());
        Assert.assertEquals(2, page.getNumberOfElements());
        Assert.assertEquals(2, page.getTotalPages());
        
        Assert.assertEquals("John" ,page.getContent().get(0).getFirstName());
        Assert.assertEquals("John1" ,page.getContent().get(1).getFirstName());
        
        
        page = userRepository.findByLastName("Doe", new PageRequest(1, 2, Direction.ASC, "firstName"));
        Assert.assertEquals(3, page.getTotalElements());
        Assert.assertEquals(1, page.getNumberOfElements());
        Assert.assertEquals(2, page.getTotalPages());
        
        Assert.assertEquals("John2" ,page.getContent().get(0).getFirstName());
    }
    
    private void saveUser(String firstName, String lastName){
        User user = userRepository.save(new User(firstName, lastName));
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
        Assert.assertEquals(firstName, user.getFirstName());
        Assert.assertEquals(lastName, user.getLastName());
    }
}
