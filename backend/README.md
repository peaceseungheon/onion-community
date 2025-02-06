# Onion 서비스

## 엔티티 설계 시 고려사항
```java
public class SomeEntity {
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;
}
```
외래키 제약 조건을 지정할 수도 있지만 성능 상 이점을 위해 외래키 지정을 하지 않음.

## 댓글 기능 요구사항

- 게시글이 존재하고 삭제되지 않았는지를 확인하고 댓글이 작성되어야 함
  - 트랜잭선 처리가 필요
- 댓글은 1분에 한번만 작성 가능    