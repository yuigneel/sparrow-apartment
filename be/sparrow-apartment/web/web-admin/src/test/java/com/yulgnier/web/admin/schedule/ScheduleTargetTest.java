package com.yulgnier.web.admin.schedule;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScheduleTargetTest {
    @Autowired
    private ScheduleTarget scheduleTarget;

    @Test
    public void test() {
        scheduleTarget.checkLeaseAgreement();
    }
}