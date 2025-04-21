package InventoryManagement.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Component
public class OrderNumberGenerator {
    public static String generateOrderNumber() {
        // Format: ORD-YYYYMMDDHHMMSS-XXXX (XXXX is a random 4-digit number)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());
        int randomNum = new Random().nextInt(9000) + 1000; // ensures a 4-digit number
        return "ORD-" + timestamp + "-" + randomNum;
    }
}
