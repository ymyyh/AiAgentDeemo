package com.zhn.aiagent.agent;

import com.zhn.aiagent.skill.BaseSkill;
import lombok.Getter;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SkillRegistry {
    @Getter
    private final List<BaseSkill> skillList;
    public SkillRegistry(List<BaseSkill> skillList) {
        this.skillList = skillList;
    }
}