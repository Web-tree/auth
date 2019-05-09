package org.webtree.auth;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Created by Udjin Skobelev on 05.12.2018.
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringTestConfig.class)
@ActiveProfiles("test")
public abstract class AbstractSpringTest {}