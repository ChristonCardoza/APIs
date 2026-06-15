package org.cardoza.sec03;

import org.cardoza.models.sec03.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec07DefaultValues {

    private static final Logger log = LoggerFactory.getLogger(Lec07DefaultValues.class);

    public static void main(String[] args) {

        var school = School.newBuilder().build();

        log.info("{}", school.getId());
        log.info("{}", school.getName());
        log.info("{}", school.getAddress());
        log.info("{}", school.getAddress().getCity());

        log.info("is default? :{}", school.getAddress().equals(Address.getDefaultInstance()));

        log.info("has address? :{}", school.hasAddress());

        var lib = Library.newBuilder().build();
        log.info("{}", lib.getBooksList());

        var dealer = Dealer.newBuilder().build();
        log.info("{}", dealer.getInventoryMap());

        var car = Car.newBuilder().build();
        log.info("{}", car.getBodyStyle());
    }
}
