# SOPT 메이커스 운영 프로덕트 서버
> SOPT 활동 기수 회원과 임원진의 편리한 운영을 위한 서비스를 만들어요.

### 웹 어드민 (임원진 대상)
- 세미나, 행사 등 **세션 생성**
- 활동 기수 회원의 **출석 내역 관리**
- 푸시 알림을 전송할 **공지 및 소식 작성, 푸시알림 전송**

### 출석 앱 (활동 기수 회원 대상)
- 참여한 세션 **출석 체크**, **자신의 출석 내역 조회**

<br/>

## Server Acrchitecture

<img width="694" alt="image" src="https://github.com/sopt-makers/sopt-operation-backend/assets/55437339/af911c45-30b7-4f92-b096-5c0ad27fbe4c">

<br/>

## Used Stacks
<img width="554" alt="image" src="https://github.com/sopt-makers/sopt-operation-backend/assets/55437339/a31ac432-ded8-4db0-acf0-f1c653067a33">

<br/>

## 프로젝트 폴더 구조
### 멀티모듈 구조
```
📁 operation-api # Controller, Service
📁 operation-auth # Authentication 관련 기능
📁 operation-common # 공통 기능
📁 operation-domain # Entity
📁 operation-external # 외부 API 기능(SOPT 메이커스 내 플레이그라운드, 알림TF)
```

### 모듈 내 구조
```
📁 src
|_ 📁 main
|_ |_ 📁 app # 앱 기능
|_ |_ 📁 common # 공통 기능
|_ |_ 📁 web # 웹 기능
```

<br/>

## Member

| [이용택](https://github.com/dragontaek-lee)| [김소현](https://github.com/thguss)|
|:-----:|:------:|
| <img width="120" height="120" src="https://user-images.githubusercontent.com/55437339/236619788-8e1ec0be-9158-452c-88b9-fe18e227586c.png"> | <img width="120" height="120" src="https://user-images.githubusercontent.com/55437339/236619930-7cad7853-1eb8-45a7-88f7-8ca196124b42.png"> |
|- 프로젝트 초기 세팅 <br/> - HTTPS 설정<br/> - (App)회원 출석 체크 기능<br/> - 알림TF|- 프로젝트 초기 세팅<br/> - CICD 환경 구축<br/> - (Web)세션 출석 관리 기능<br/> - 알림 관리 기능|


<br/>

## Process
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

<br/>

## Commit Convention
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

## Branch Strategy
- `main`, `develop`, `feature` 브랜치가 있습니다!
- **main**은 production용 브랜치입니다
    - 실서비스용 ec2(**makers.operation.prod)**로 배포되도록 파이프라인이 구축되어 있습니다
- **develop**은 development용 브랜치입니다!
    - 테스트용 ec2(**makers.operation)**로 배포되도록 파이프라인이 구축되어 있습니다
    - default 브랜치 입니다
- **feature**은 `이름_#이슈번호` 로 되어있는 브랜치입니다!
    - 각자 이슈에 대한 작업물의 브랜치입니다
    - develop에 PR을 거쳐 merge 해주세요

