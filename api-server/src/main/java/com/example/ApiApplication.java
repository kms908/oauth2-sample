package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@EnableResourceServer
@SpringBootApplication
public class ApiApplication {

	@Bean
	public ResourceServerConfigurerAdapter resourceServerConfigurerAdapter() {
		return new ResourceServerConfigurerAdapter() {
			@Override
			public void configure(HttpSecurity http) throws Exception {
				http.headers().frameOptions().disable();
				http.authorizeRequests()
						.antMatchers("/members", "/members/**").access("#oauth2.hasScope('read')")
						.anyRequest().authenticated();
			}
		};
	}

	/**
	 * API를 조회시 출력될 테스트 데이터
	 * @param memberRepository
	 * @return
	 */
	@Bean
	public CommandLineRunner commandLineRunner(MemberRepository memberRepository) {
		return args -> {
			memberRepository.save(new Member("강민수", "mskang7", "test111"));
			memberRepository.save(new Member("윤태한", "yoongth", "test222"));
			memberRepository.save(new Member("정은솔", "siri", "test333"));
			memberRepository.save(new Member("류성호", "chris", "test444"));
			memberRepository.save(new Member("테스트", "test", "test555"));

		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
}