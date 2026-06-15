package org.cardoza.sec03;

public record JsonPerson( String name,
                          int age,
                          String email,
                          boolean employed,
                          double salary,
                          long bankAccountNumber,
                          int balance) {

}
