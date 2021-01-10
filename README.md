<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-%3E%3D%205.5.0-blue">
  <img alt="node" src="https://img.shields.io/badge/node-%3E%3D%209.3.0-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/next-step/atdd-subway-service">
</p>

<br>

# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```
<br>


## [STEP1] 인수 테스트 기반 리팩터링
* LineSectionAcceptanceTest 리팩터링
    -[X] 인수 테스트 통합
        * 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
        * 하나 이상의 시나리오가 필요한 경우 여러개의 시나리오를 만들어 인수 테스트를 작성할 수 있음
```
    Scenario: 지하철 구간을 관리
        When 지하철 구간 등록 요청
        Then 지하철 구간 등록됨
        When 지하철 노선에 등록된 역 목록 조회 요청
        Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
        When 지하철 구간 삭제 요청
        Then 지하철 구간 삭제됨
        When 지하철 노선에 등록된 역 목록 조회 요청
        Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
```

* LineService 리팩터링
    * 인수 테스트 기반 리팩터링
        * Domain으로 옮길 로직을 찾기
            -[X] 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 이동 
    
        * Domain의 단위 테스트를 작성하기
            * Sections(지하철 구간) 클래스
                - [X] 역 사이에 새로운 역을 등록할 경우 : 기존 상행역 - 새로운 하행역 관계
                - [X] 역 사이에 새로운 역을 등록할 경우 : 새로운 상행역 - 기존 하행역 관계
                - [X] 새로운 역을 상행 종점으로 등록할 경우
                - [X] 새로운 역을 하행 종점으로 등록할 경우
                - [X] 역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없음
                - [X] 상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없음
                - [X] 상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없음
                - [X] 역과 역사이에 중간역 삭제
                - [X] 노선에서 상행 종점 제거
                - [X] 노선에서 하행 종점 제거
                - [X] 노선에 등록되지 않은 역 제거할 수 없음
                - [X] 등록된 구간이 1개일 때, 제거할 수 없음

          * Distance(지하철 구간 길이) 클래스
                - [X] 길이 더하기
                - [X] 길이 빼기
                - [X] 음수값으로 길이 생성시 예외 발생
              
          * Line(지하철 라인) 클래스
                - [X] 지하철 라인에 구간을 등록
                - [X] 지하철 라인의 중간역 삭제
        
        * 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링하기


## [STEP2] 경로 조회 기능
    
- [X] 최단 경로 조회 인수 테스트 만들기
  
- [X] 최단 경로 조회 기능 구현하기
    * 출발역과 도착역을 입력
    * 하나의 노선에서 지하철역 최단거리를 구함
    * 여러개의 노선에서 지하철역 최단거리를 구함
    * 출발역과 도착역의 경로 정보 리턴 (정차역, 최단거리)

- [X] 예외 및 Side 케이스 처리
    * 출발역과 도착역이 같은 경우
    * 출발역과 도착역이 연결이 되어 있지 않은 경우
    * 존재하지 않은 출발역이나 도착역을 조회 할 경우

## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/next-step/atdd-subway-service/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-service/blob/master/LICENSE.md) licensed.
