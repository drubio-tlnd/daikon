package org.talend.daikon.content;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public abstract class DeletableLoaderResourceTests {

    @Autowired
    protected ResourceResolver resolver;

    @Configuration
    @ComponentScan(basePackages = "org.talend.daikon")
    public static class ResourceLoaderTestConfiguration {
    }
}
