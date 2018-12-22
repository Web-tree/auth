package org.webtree.auth;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Udjin Skobelev on 05.12.2018.
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringTestConfig.class)
public abstract class AbstractSpringTest {}