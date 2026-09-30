-- best_route_problem.START_STATION 의 UNIQUE 제약을 일반 인덱스로 바꾼다.
--
-- BestRouteProblemAnswer.startStation 이 @OneToOne 이라 Hibernate 가 UNIQUE 를 걸었고,
-- 그 탓에 출발역 하나당 문제 하나로 제한되어 655건이 상한이었다.
-- 엔티티는 @ManyToOne 으로 고쳤지만 ddl-auto: update 는 기존 제약을 지우지 않으므로
-- 이미 만들어진 DB 에는 이 스크립트를 한 번 돌려야 한다.
--
-- FK 가 이 인덱스를 참조하고 있어 인덱스만 먼저 지울 수 없다. FK 해제 -> 인덱스 교체 -> FK 복구.
--
-- 적용 확인:
--   SHOW CREATE TABLE best_route_problem;   -- UNIQUE KEY (START_STATION) 이 없어야 한다

ALTER TABLE best_route_problem DROP FOREIGN KEY FKk70t7f6hu432b9rhmgv2qqp8e;
ALTER TABLE best_route_problem DROP INDEX UK_dq45caf9dujyrjxb9t28rwi0u;
ALTER TABLE best_route_problem ADD INDEX IDX_best_route_start_station (START_STATION);
ALTER TABLE best_route_problem
    ADD CONSTRAINT FKk70t7f6hu432b9rhmgv2qqp8e
    FOREIGN KEY (START_STATION) REFERENCES station (ID);
