package dev.rost.frameworks;

import dev.rost.frameworks.persist.entity.Person;
import dev.rost.frameworks.persist.repository.PersonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


class PersonRepositoryTest extends AbstractDataJpaTest {

    @Autowired PersonRepository repository;
    @Autowired TestEntityManager entityManager;


    @Test
    @DisplayName("when: uses Repository#save without `flushing` & `clearing persistence ctx` ==> then: autogenerated value is null")
    void notClearingPersistenceContext1() {
        var person = Person.builder()
                .name("Rasta")
                .build();

        Person saved = repository.save(person);
        assertNull(saved.getRandomUUID());

        Person found = repository.findById(saved.getId()).orElseThrow();
        assertNull(found.getRandomUUID());
    }


    @Test
    @DisplayName("when: uses Repository#save with `flushing`, but without `clearing persistence ctx` ==> then: autogenerated value is null")
    void notClearingPersistenceContext2() {
        var person = Person.builder()
                .name("Rasta")
                .build();

        Person saved = repository.saveAndFlush(person);
        assertNull(saved.getRandomUUID());

        Person found = repository.findById(saved.getId()).orElseThrow();
        assertNull(found.getRandomUUID());
    }


    @Test
    @DisplayName("when: uses Repository#save with `flushing` and `clearing persistence ctx` ==> then: autogenerated value is not null")
    void clearingPersistenceContext() {
        var person = Person.builder()
                .name("Rasta")
                .build();

        Person saved = repository.saveAndFlush(person);
        entityManager.clear();

        assertNull(saved.getRandomUUID());

        Person found = repository.findById(saved.getId()).orElseThrow();
        assertNotNull(found.getRandomUUID()); //Bingo!
    }
}
