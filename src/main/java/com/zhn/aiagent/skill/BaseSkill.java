package com.zhn.aiagent.skill;

public interface BaseSkill {
    String getSkillName();
    String getDescription();
    String execute(String userQuery);
}