package hello.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A") // 이 어노테이션이 있으면 DTYPE을 클래스명이 아닌 다른 이름으로 변경할 수 있다.
public class Album extends Item {

  private String artist;
}
