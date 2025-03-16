# 지하철 게임 서버
지하철 게임(https://zeehacheol.com)의 서버입니다. Spring Boot 3 + Kotlin 으로 작성되었습니다. 웹 서비스, 보안, 데이터 영속성 등의 기능을 지원하기 위해 다양한 의존성을 포함하고 있습니다.

## 기능
- **Spring Boot**: 빠른 개발을 위한 애플리케이션 빌딩
- **Spring Data JPA**: 데이터베이스 상호작용
- **Spring Security**: 애플리케이션 보안, 세션 기반 어드민 페이지 로그인
- **AWS SDK**: Amazon S3와 상호작용
- **Thymleaf**: 어드민 페이지 서빙
- **Spring Actuator**: 성능 모니터링 - Prometheus - Granfana를 이용한 모니터링

## 설치 방법

1. **레포지토리 클론**:
    ```sh
    git clone https://github.com/SKKU-Capston-Project-2024/server.git
    cd server
    ```

2. **도커 빌드**:
    ```sh
    docker build .
    ```

3. **애플리케이션 실행**:
    ```sh
    docker run
    ```

## 설정
애플리케이션은 `build.gradle` 파일에 정의된 여러 의존성과 설정을 사용합니다. 애플리케이션이 정상적으로 작동하려면 데이터베이스 설정 및 AWS 자격 증명, Spotify Key를 설정해야합니다.

## 사용법
API 사용 예제는 다음 Repository를 확인해주세요.
https://github.com/seongyunlee/subway-web

## 라이선스
이 프로젝트는 MIT 라이선스 하에 라이선스됩니다.

---

프로젝트와 사용법에 대한 더 구체적인 세부 사항을 추가하고 싶은 부분이 있으면 말씀해 주세요.
