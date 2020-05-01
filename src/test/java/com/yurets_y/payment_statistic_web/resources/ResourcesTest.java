package com.yurets_y.payment_statistic_web.resources;

import com.yurets_y.payment_statistic_web.repo.RepositoryConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.lang.annotation.*;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@Import({TestFilesConfig.class})
public class ResourcesTest {

    @Resource(name="test-html-file")
    private File testFileLocation;

    @Resource(name="test-html-directory")
    private File testDirectoryLocation;

    @Test
    public void autowiredRepositoryConfig() {
        System.out.println("test html file: " + testFileLocation);
        assertNotNull(testFileLocation);

        System.out.println("test html directory: " + testDirectoryLocation);
        assertNotNull(testDirectoryLocation);
    }
}
