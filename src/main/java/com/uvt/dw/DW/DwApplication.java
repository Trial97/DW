package com.uvt.dw.DW;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;


@EnableAutoConfiguration(exclude = { CassandraDataAutoConfiguration.class })
@SpringBootApplication
public class DwApplication {
	public static void main(String[] args) {
		SpringApplication.run(DwApplication.class, args);
	}
}
