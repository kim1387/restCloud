1. 연관 클래스 및 API 엔드포인트 Prefix등의 자료를 멤버 변수로 선언한 추상 클래스 작성.
2. (1) 클래스를 상속받는, 실질적으로 적용되는 추상 클래스 작성. 이 클래스들이 플러그인 역할을 함.
3. (2) 클래스에 AOP 방식으로 클래스가 yml에 명시되어있는 경우만 스프링 부트에 Bean으로 적용하는 코드 작성.
4. 개별 (2) 클래스에 연결된 '연관 클래스' 라고 언급한 RestController 클래스 작성.


### (3)의 구현 방법 설계

* RestCloudApplication에서 storage 패키지의 컴포넌트 스캔을 제외함.
* 커스텀 AOP Annotation으로 yml을 파싱 후 키값이 존재할 경우에만 클래스에 @ComponentScan 어노테이션을 달아줌.