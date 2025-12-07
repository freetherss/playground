package kr.co.inwoo6325.WebGame;

import kr.co.inwoo6325.WebGame.model.entity.Character;
import kr.co.inwoo6325.WebGame.model.entity.Monster;
import kr.co.inwoo6325.WebGame.model.entity.UserAccount;
import kr.co.inwoo6325.WebGame.repository.CharacterRepository;
import kr.co.inwoo6325.WebGame.repository.MonsterRepository;
import kr.co.inwoo6325.WebGame.repository.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class WebGameApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebGameApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(
			UserAccountRepository userRepo,
			CharacterRepository charRepo,
			MonsterRepository monsterRepo,
			PasswordEncoder passwordEncoder
	) {
		return args -> {
			// Create a test user if not exists
			if (userRepo.findByUsername("testuser").isEmpty()) {
				UserAccount user = UserAccount.builder()
						.username("testuser")
						.password(passwordEncoder.encode("password"))
						.build();
				userRepo.save(user);

				// Create a test character for the user if they have no characters
				if (charRepo.findByUser(user).isEmpty()) {
					Character character = Character.builder()
							.user(user)
							.name("용사")
							.job("전사")
							.level(1)
							.maxHp(150)
							.currentHp(150)
							.attack(15)
							.defense(8)
							.agility(10)
							.build();
					charRepo.save(character);
				}
			}

			// Create a test monster if not exists
			if (monsterRepo.findByName("고블린").isEmpty()) {
				Monster monster = Monster.builder()
						.name("고블린")
						.level(1)
						.maxHp(50)
						.currentHp(50)
						.attack(8)
						.defense(3)
						.agility(4)
						.expDrop(10)
						.goldDrop(5)
						.build();
				monsterRepo.save(monster);
			}
		};
	}
}
