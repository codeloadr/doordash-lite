package com.nsoni.doordash;

import com.nsoni.doordash.network.DoorDashRepository;
import com.nsoni.doordash.network.ServiceGenerator;

import java.lang.reflect.Field;

public class TestUtils {

    public static void setMockedRepository(DoorDashRepository mockedRepository) throws Exception {
        Field field = DoorDashRepository.class.getDeclaredField("doorDashRepositorySingleInstance");
        field.setAccessible(true);
        field.set(null, mockedRepository);
    }

    public static void setMockedRepository(ServiceGenerator mockedServiceGenerator) throws Exception {
        Field field = ServiceGenerator.class.getDeclaredField("serviceGeneratorInstance");
        field.setAccessible(true);
        field.set(null, mockedServiceGenerator);
    }
}
