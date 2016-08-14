package org.chernatkin.spring.boot;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class UserControllerTest {

    @Autowired
    private UserRepository userRepository;
    
    @Value("${local.server.port}")
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();
    
    
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
    public void getByLastNamePageTest(){
        
        ResponseEntity<User[]> response = restTemplate.getForEntity(getBaseUrl() + "/user?lastName=Doe&page=0&size=2", User[].class);
        Assert.assertEquals(200, response.getStatusCode().value());
        Assert.assertEquals("John", response.getBody()[0].getFirstName());
        Assert.assertEquals("John1", response.getBody()[1].getFirstName());
        
        response = restTemplate.getForEntity(getBaseUrl() +  "/user?lastName=Doe&page=1&size=2", User[].class);
        Assert.assertEquals(200, response.getStatusCode().value());
        Assert.assertEquals("John2", response.getBody()[0].getFirstName());
    }
    
    @Test
    public void postUserTest(){
        ResponseEntity<User> response = restTemplate.postForEntity(getBaseUrl() + "/user", new User("Sam", "Scott"), User.class);
        Assert.assertEquals(200, response.getStatusCode().value());
        Assert.assertNotNull(response.getBody());
        Assert.assertNotNull(response.getBody().getId());
        Assert.assertEquals("Sam", response.getBody().getFirstName());
        Assert.assertEquals("Scott", response.getBody().getLastName());
        
        Assert.assertNotNull(userRepository.findOne(response.getBody().getId()));
    }
    
    
    private void saveUser(String firstName, String lastName){
        User user = userRepository.save(new User(firstName, lastName));
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
        Assert.assertEquals(firstName, user.getFirstName());
        Assert.assertEquals(lastName, user.getLastName());
    }
    
    private String getBaseUrl(){
        return "http://localhost:" + port;
    }
}
