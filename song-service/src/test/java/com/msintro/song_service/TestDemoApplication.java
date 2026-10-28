package com.msintro.song_service;

import org.springframework.boot.SpringApplication;

public class TestDemoApplication {

	public static void main(String[] args) {
		SpringApplication.from(SongServiceApplication::main).with(SongServiceTestcontainersConfiguration.class).run(args);
	}

}
