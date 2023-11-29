# sopt-operation-backend
**메이커스 운영팀 서버** : 출석 관리 어드민 서비스, 회원 출석 체크 서비스

<br/>

## 🛠 Used Stacks

- Java 17
- Gradle
- Spring Boot 2.7.4
- Spring Data JPA
- PostgreSQL

<br/>

## 👥 팀원

| [이용택](https://github.com/dragontaek-lee)| [김소현](https://github.com/thguss)|
|:-----:|:------:|
| <img width="120" height="120" src="https://user-images.githubusercontent.com/55437339/236619788-8e1ec0be-9158-452c-88b9-fe18e227586c.png"> | <img width="120" height="120" src="https://user-images.githubusercontent.com/55437339/236619930-7cad7853-1eb8-45a7-88f7-8ca196124b42.png"> |
|- 프로젝트 초기 세팅 <br/> - HTTPS 설정<br/> - 회원 출석 체크 서비스|- 프로젝트 초기 세팅<br/> - CICD 환경 구축<br/> - 출석 관리 어드민 서비스|


<br/>

## 📏 Process
1. 개발 전에 `github issue`를 생성해주세요!
    1. 템플릿에 맞게 내용을 작성한다
    2. Assignees, Label을 단다
    3. label의 경우 타입과 본인의 이름을 할당한다 (`chore` `yongtaek`)
2. `feature branch`를 생성해주세요!
    1. 브랜치명은 `이름_#이슈번호` 로 한다
    2. 예시는 다음과 같다 **브랜치명: yongtaek_#32**
3. 로컬에서 작업하고 `기능단위로 쪼개서` 커밋을 해주세요!
4. 해당 issue에 대한 작업이 완료되었다면 github에 `해당 브랜치를` 올려주세요!
5. `PR`을 템플릿에 맞춰서 올려주세요!
6. 리뷰어가 `approve`할 때까지 기능을 수정하면서 개발해주세요!
7. `approve`가 완료되었다면 `merge`를 진행해주세!요

> `코드 외적인 부분`(환경변수, db 필드 및 테이블 수정, 인프라 세팅 등) 수정사항이 있다면 팀원에게 먼저 물어보고 진행하거나, 그러지 못하였더라면 빠르게 전달해주세요!(카톡, 슬랙, 디코 등)
>


<br/>

## 🌴 Commit Convention
| 태그 이름 | 설명 |
| --- | --- |
| [CHORE] | 코드 수정, 내부 파일 수정 |
| [FEAT] | 새로운 기능 구현 |
| [ADD] | FEAT 이외의 부수적인 코드 추가, 라이브러리 추가, 새로운 파일 생성 |
| [HOTFIX] | issue나 QA에서 급한 버그 수정에 사용 |
| [FIX] | 버그, 오류 해결 |
| [DEL] | 쓸모 없는 코드 삭제 |
| [DOCS] | README나 WIKI 등의 문서 개정 |
| [CORRECT] | 주로 문법의 오류나 타입의 변경, 이름 변경에 사용 |
| [MOVE] | 프로젝트 내 파일이나 코드의 이동 |
| [RENAME] | 파일 이름 변경이 있을 때 사용 |
| [IMPROVE] | 향상이 있을 때 사용 |
| [REFACTOR] | 전면 수정이 있을 때 사용 |

<br/>

## ✨ Branch Strategy
- `main`, `develop`, `feature` 브랜치가 있습니다!
- **main**은 production용 브랜치입니다
    - 실서비스용 ec2(**makers.operation.prod)**로 배포되도록 파이프라인이 구축되어 있습니다
- **develop**은 development용 브랜치입니다!
    - 테스트용 ec2(**makers.operation)**로 배포되도록 파이프라인이 구축되어 있습니다
    - default 브랜치 입니다
- **feature**은 `이름_#이슈번호` 로 되어있는 브랜치입니다!
    - 각자 이슈에 대한 작업물의 브랜치입니다
    - develop에 PR을 거쳐 merge 해주세요

<br/>

## 🗂 프로젝트 폴더 구조

```
📁 src
|_ 📁 main
|_ |_ 📁 common
|_ |_ 📁 config
|_ |_ 📁 controller
|_ |_ 📁 dto
|_ |_ 📁 entity
|_ |_ 📁 exception
|_ |_ 📁 repository
|_ |_ 📁 security
|_ |_ 📁 service
|_ |_ 📁 util

```

<br/>

## 🌴 Dependencies Module
<b>build.gradle</b>
```
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation "com.querydsl:querydsl-jpa:${queryDslVersion}"
    implementation "com.querydsl:querydsl-apt:${queryDslVersion}"

    compileOnly 'org.projectlombok:lombok'
    runtimeOnly 'com.h2database:h2'
    runtimeOnly 'org.postgresql:postgresql'
    annotationProcessor 'org.projectlombok:lombok'

    // jwt
    implementation group: 'io.jsonwebtoken', name: 'jjwt-api', version: '0.11.2'
    runtimeOnly group: 'io.jsonwebtoken', name: 'jjwt-impl', version: '0.11.2'
    runtimeOnly group: 'io.jsonwebtoken', name: 'jjwt-jackson', version: '0.11.2'

    testImplementation 'org.springframework.boot:spring-boot-starter-test'

    // swagger
    implementation 'io.springfox:springfox-boot-starter:3.0.0'
    implementation 'io.springfox:springfox-swagger-ui:3.0.0'
}

```

<br/>


## 🏗 Architecture
![image](https://user-images.githubusercontent.com/55437339/236621230-8d2dd581-c68d-44e9-bc0d-ea35dee08ebe.png)

