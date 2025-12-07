package kr.co.inwoo6325.WebGame.model.entity;

public interface Combatant {
    String getName();
    int getAttack();
    int getDefense();
    int getCurrentHp();
    void setCurrentHp(int hp);
    int getAgility();
}
