<div align=center>

# 모두의 입맛, 모입(molip)
**메뉴 선정의 애매한 기준을 명확한 메뉴판으로 바꿔줄 서비스, [모입] 입니다.** <br>
매일 점심 메뉴 선정은 언제나 어렵고, 모두가 만족하는 메뉴를 고르기는 더 어렵습니다. 메뉴 선정에 있어 애매한 심리도 항상 존재합니다.  <br><br>
모입은 나만의, 동료와의, 혹은 우리 팀만의 전용 메뉴판을 제공하여 맞춤 메뉴로 선택지를 확연히 줄여줍니다.<br>
애매한 기준을 반영해 다양한 옵션을 선정하고, 필터링 기능과 투표, 그리고 지도 검색을 통해 메뉴를 결정할 수 있도록 도와줍니다.
</div>



## 개요
- 프로젝트 이름 : 모입 <br>
- 프로젝트 기간 : 2024.06 ~ 2024.08 <br>
- 프로젝트 참여 멤버 <br>
  Server : 박민수, 박현지, 이재우 <br>
- Repo Link <br>
  Web : https://github.com/Swyp-team10/molip-front  <br>
  Server : https://github.com/Swyp-team10/molip-backend <br>
- 배포 페이지 : https://www.molip.site/
  <br><br>

[//]: # (#### 서버 역할분담)

[//]: # (- 박민수 : )

[//]: # (- 박현지 :)

[//]: # (- 이재우 : )


## Tech Stack


#### Back-End
- 언어 및 프레임워크 <br>
Java
Spring Boot 3.3.6
Spring Data JPA

- 데이터베이스 <br>
MySQL (AWS RDS)

- 서버 및 배포 <br>
AWS EC2 (서버 호스팅)
AWS S3 (파일 저장)
AWS Route 53 (도메인 관리)

- 버전 관리 및 기타 도구 <br>
Git (GitHub를 통해 버전 관리)
Postman (API 테스트)
KaKao API (OAuth2 JWT 인증)

<br>


[//]: # (## 프로젝트 설명)


## 📑 프로젝트 규칙

>작업해야할 내용들은 issue 생성 후 브랜치 생성하여 진행<Br>
> main / develop 브랜치 기본 생성(main은 배포 branch, develop는 개발 브랜치)<br>
> develop 브랜치를 Source로 하는 작업 별 브랜치 생성하여 작업 진행<br>
> 작업 브랜치 명은 개발할 주제에 맞게 (ex. feature/login)
> <br><br>
> 각자 작업 브랜치에서 PR 생성 후 충돌 체크하고 develop으로 merge<br>
> merge 후에는 알려주기


### Git Convention
> 1. 적절한 커밋 접두사 작성
> 2. 커밋 메시지 내용 작성

> | 접두사        | 설명                           |
> | ------------- | ------------------------------ |
> | Feat :     | 새로운 기능 구현               |
> | Add :      | 에셋 파일 추가                 |
> | Fix :      | 버그 수정                      |
> | Docs :     | 문서 추가 및 수정              |
> | Style :    | 스타일링 작업                  |
> | Refactor : | 코드 리팩토링 (동작 변경 없음) |
> | Test :     | 테스트                         |
> | Deploy :   | 배포                           |
> | Conf :     | 빌드, 환경 설정                |
> | Chore :    | 기타 작업                      |
<br/>
