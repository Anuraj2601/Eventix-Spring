<<<<<<< HEAD
package com.example.eventix.service;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpUtil {

    public String generateOtp() {
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        String output = Integer.toString(randomNumber);
        while (output.length() < 6) {
            output = "0" + output;
        }
        return output;
    }
}
=======
package com.example.eventix.service;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpUtil {

    public String generateOtp() {
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        String output = Integer.toString(randomNumber);
        while (output.length() < 6) {
            output = "0" + output;
        }
        return output;
    }
}
>>>>>>> 4f912b739ea1ddc3a83484fa6247a275f99e1b3b
