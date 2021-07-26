package org.apache.ibatis.reflection.patterns;

import org.apache.ibatis.reflection.patterns.domain.Phone;
import org.apache.ibatis.reflection.patterns.domain.RecordTelPhone;
import org.apache.ibatis.reflection.patterns.domain.TelPhone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecordPhoneTest {

    public Phone phone;


    @BeforeEach
    public void setUp() {
        phone = new TelPhone();
    }

    @Test
    void testCallIn() {
        RecordTelPhone recordTelPhone = new RecordTelPhone(phone);
        String result = recordTelPhone.callIn();
        assertEquals(result,phone.callIn());
    }

    @Test
    void testCallOut() {
        String info = "hello";
        RecordTelPhone recordTelPhone = new RecordTelPhone(phone);
        Boolean result = recordTelPhone.callOut(info);
        assertEquals(result,phone.callOut(info));
    }

}