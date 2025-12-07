package kr.co.inwoo6325.WebGame.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterCreateRequest {
    
    @NotBlank(message = "캐릭터 이름은 필수입니다.")
    @Size(min = 2, max = 12, message = "캐릭터 이름은 2자 이상 12자 이하이어야 합니다.")
    private String name;
    
    @NotBlank(message = "직업은 필수입니다.")
    private String job;
}