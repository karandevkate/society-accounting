package com.fqts.mysociety;

import com.fqts.mysociety.repository.FlatRepository;
import com.fqts.mysociety.service.FlatService;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MysocietyApplicationTests {

	@Autowired
	FlatRepository flatRepository;

	@Autowired
	FlatService flatService;

	@Test
	void showProxyClass() {
		System.out.println("Repository proxy class: " + flatRepository.getClass().getName());
		System.out.println("Service proxy class: " + flatService.getClass().getName());

        try {
			System.out.println(getClass());
				System.out.println( Class.forName("com.fqts.mysociety.service.impl.FlatServiceImpl"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
