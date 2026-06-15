package org.cardoza.sec03;


import org.cardoza.models.sec03.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec01Scalar {

    private static final Logger log = LoggerFactory.getLogger(Lec01Scalar.class);

    public static void main(String[] args) {

        var person1 = Person.newBuilder()
                .setName("sam")
                .setAge(12)
                .setEmail("sam@gmail.com")
                .setEmployed(true)
                .setSalary(1000.2345)
                .setBankAccountNumber(123456789012L)
                .setBalance(-1000)
                .build();

        log.info("{}", person1);

        var person2 = Person.newBuilder()
                .setName("man")
                .build();

        log.info("{}", person2);



    }
}
