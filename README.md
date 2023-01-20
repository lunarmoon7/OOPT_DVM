# 2022-3학년1학기 객체지향 개발방법론 팀프로젝트
## OOPT_DVM
##  분산 자판기 시스템의 자판기 controller SW 개발

## Overall Description

### 1.1 Project Description

> 분산 자판기란 서버를 두지 않고 각 자판기 시스템이 서로 통신하여 각 자판기의 재고와 위치를 파악하고 안내하는 것이 가능한 분산형 자판기 시스템이다. 분산형 자판기 시스템에서 사용자는 현재 자판기에서 판매하지 않는 제품이 어느 자판기에서 판매되고 있는지 파악하거나 현재 자판기에서 다른 자판기의 상품을 구매할 수 있다.

<이미지>

### 1.2 Rquirements
> - 총 음료의 개수는 20 종류이다.
> - 한 자판기는 랜덤하게 7 종류의 음료를 판매한다.
>   - 판매하지 않는 음료도 메뉴 자체는 선택할 수 있도록 제공한다.
> - 사용자가 음료를 선택 후 결제하면 음료가 제공된다.
> - 결제는 카드로 한다.
>   - 잔액이 부족한 경우 결제되지 않는다.
> - 재고가 없는 음료를 구매하려고 하는 경우 다른 자판기에 해당 음료의 재고를 확인한 후 위치를 안내한다.
>   - 네트워크 상의 자판기에 broadcast msg를 통해 재고 확인을 요청하여 확인한다.
>   - 네트워크 상의 응답 msg를 통해 대상 자판기의 id를 확인하여 위치를 안내한다.
>   - 사용자가 선택한 자판기에서 가장 가까운 위치에 있는, 해당 음료의 재고가 있는 자판기의 위치를 안내한다.
> - 현재 자판기에서 판매하지 않는 음료를 구매하려는 경우 판매하는 다른 자판기를 확인 후 위치를 안내한다.
>   - 네트워크 상의 자판기에 broadcast msg를 통해 판매 음료 확인을 요청하여 확인한다.
>   - 네트워크 msg를 통해 대상 자판기의 id와 좌표를 확인하여 위치를 안내한다.   
> - 사용자가 선택한 자판기에서 가장 가까운 위치에 있는, 해당 음료의 재고가 있는 자판기의 위치를 안내한다. 
> - 다른자판기의음료구매에대해선결제를할수있다.
>   - 선결제를 요청하는 경우 현재 자판기에서 결제 후 인증 코드를 발급한다.
>   - 인증코드는 알파벳 소문자와 숫자를 모두 포함하는 10자리의 문자열이어야 한다.     
>   - 사용자가 선택한 자판기에서 가장 가까운 위치에 있는, 해당 음료의 재고가 있는 자 판기의 위치를 안내한다.
>   - 다른 자판기로 가서 인증 코드를 입력하면 음료가 나온다.
>   - 결제하는 사이에 다른 자판기의 음료가 소진된 경우, 결제를 취소할 수 있도록 한다.    
>   - 결제가 완료된 음료의 경우, 재고가 소진되지 않도록 다른 자판기에서 이미 판매된 것으로 처리한다.
> - 매번 통신을 주고받을 때마다 자판기의 좌표를 받아와 거리를 계산한다.
>   - 자판기의 좌표 정보는 (x,y)의 형태이고, x와 y의 범위는 0~99이다.
>   - 만일 거리가 같은 자판기에 동일한 음료가 있고, 두 자판기에 모두 재고가 있다면 id의 숫자가  작은 자판기로 안내한다.
>   - 각 자판기의 좌표 정보는 서로 겹칠 수 없다.

### 1.3 Constraints
> - 자판기 사이의 msg 전달은 TCP 소켓 통신을 사용하여 구현한다.
> - 각 자판기는 메시지를 주고받기 위해 server socket과 cllient socket을 모두 가지고 있어야 한다.
### 1.4 Assumptions and dependencies
> - 각 자판기는 모두 네트워크에 연결되어 있고 각자의 네트워크 연결 정보는 미리 알고 있다고 가정한다.
> - 각 자판기의 판매 음료 종류는 사전에 결정된다.
> - 각 자판기에서 가지고 있는 각 음료의 최대 재고는 999개로 한정한다.
> - 자판기 사이의 msg protocol은 다음과 같다.
> 
|src id|dst id|msg type|msg description|msg direction| 
|--|--|--|--|--|
|현재 자판기의 id|대상 자판기의 id|msg 타입|msg 내용|msg 방향|
|||재고 확인 요청|음료 코드_음료 개수|Broadcast|
|||재고 확인 응답|음료 코드_음료 개수_dst id_dst 좌표|dst -> src|
|||선결제 확인|음료 코드_음료 개수_인증 코드|src -> dst|
|||음료 판매 확인|음료 코드_음료 개수|Broadcast|
|||음료 판매 응답|음료 코드_dst id_dst 좌표|dst -> src|
> - 각 팀별 자판기의 id는 Team + 숫자로 한다.
>   - ex) 1조의 자판기 id는 Team1
>   - Broadcast msg의 경우 id는 0이다.
> - 주고받는 msg의 데이터 타입은 String이다.
> - 주고받는 msg에서 음료 개수는 10진수의 세 자리 숫자로 표기한다.
> - 주고받는 msg는 위 표에서와 동일하게 문자 '_'를 통해서 각 값을 구분한다.
> - 판매할 수 있는 음료의 종류는 다음과 같다.

<이미지>

## Development Env
-  IDE : Intellij IDEA Ultimate
- Language: Java
- GUI : Swing
- Diagram tools: StarUML

## Proccess
### OOA
---
- ~ 2022.3.28 : OOPT_1000 (Planning)
	- ACT 1001 : Define A Draft Plan
	- ACT 1002 : Preliminary Investigation Plan
	- ACT 1003 : Define Requirements
		- Functional Requirements
		- Non-Functional Requirements
	- ACT 1004 : Terms in Glossary
	- ACT 1006 : Define Business Use-Case
	- ACT 1008 : Draft System Architecture
	- ACT 1009 : Define System Test Case
	- ACT 1010 : Refine Plan 

- ~ 2022.4.18 : OOPT_2030 (Analyze)
	- ACT 2031 : Define Essential Use-Cases
	- ACT 2033 : Define System Sequence Diagram
	- ACT 2035 : Define Domain Model
	- ACT 2038 : Refine System Test Case
	- ACT 2039 : Analyze Traceability Analysis
---
### OOD
---
- ~ 2022.5.16 : OOPT_2040 (Design)
	- ACT 2141 : Design Real Use-Case
	- ACT 2143 : Refine System Architecture
	- ACT 2144 : Design Sequence Diagram
	- ACT 2145 : Define Design Class Diagrams
	- ACT 2146 : Design Traceability Analysis

- ~ 2022.6.7 : OOPT_1st Cycle (Implementation & Unit Test)

- ~ 2022.6.20 : OOPT_2nd Cycle (Implementation)
	- Applied 2 Design Patterns 
		- Singleton Pattern
		- Template Method Pattern
	- Applied Clean Code & PMD
---
## Project Documentation
<파일들>
## DVM Flow Chart
<이미지>
## What referenced
<파일들>
