package com.fendou.gmall.search;

import io.searchbox.client.JestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallSearchServiceApplicationTests {


    @Autowired
    JestClient jestClient;
    @Test
    public void contextLoads() throws IOException {
        jestClient.execute(null);
    }

}
