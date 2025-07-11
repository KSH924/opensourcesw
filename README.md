Index Study App - Java Project

소개
이 애플리케이션은 사용자가 인덱스 카드를 생성하고, 각 페이지에 텍스트 및 이미지를 입력하거나 가릴 수 있으며,
암기 학습 기능까지 제공하는 학습 보조 앱입니다.
기능으로는 로그인, 회원가입, 인덱스 추가/삭제, 페이지 생성/수정/삭제, 이미지 추가, 단어 가리기, 저장 등이 포함됩니다.

실행 방법

1. Java IDE(Eclipse, IntelliJ 등) 또는 터미널을 통해 프로젝트를 실행합니다.
2. `IndexStudyApp.java`가 메인 클래스입니다.
3. 애플리케이션 실행 시 로그인 화면이 뜨며, 로그인 또는 회원가입을 선택할 수 있습니다.
4. 회원 가입 후 로그인
5. 메인화면에서 인덱스를 추가하거나 선택하여 편집할 수 있습니다

주요 기능 및 클래스 설명

1) **IndexStudyApp.java**
   - 애플리케이션의 메인 클래스입니다.
   - 로그인 화면, 인덱스 선택, 페이지 보기 및 편집 등의 주요 UI 흐름을 담당합니다.
   - 사용자 데이터 및 인덱스 데이터를 저장/불러오기 처리합니다.

2) **Login.java**
   - 사용자 로그인 및 회원가입 화면 UI를 제공합니다.
   - 사용자 ID/PW 입력, 인증 처리, 회원가입 등록 기능이 포함됩니다.

3) **User.java**
   - 사용자 정보를 저장하는 클래스입니다.
   - ID, 비밀번호 정보를 보유하며, 로그인 인증에 사용됩니다.

4) **IndexData.java**
   - 인덱스의 메타 정보를 저장합니다.
   - 제목, 생성 날짜, 해당 인덱스의 내용(IndexContent)을 포함합니다.

5) **IndexContent.java**
   - 하나의 인덱스가 가지는 페이지 정보를 관리합니다.
   - 페이지 추가, 삭제, 순서 변경 등의 로직을 포함합니다.

6) **Page.java**
   - 인덱스 내 개별 페이지를 나타냅니다.
   - 텍스트, 이미지 경로를 저장하며 텍스트 입력 및 이미지 삽입 기능을 제공합니다.

7) **TextHighlighter.java**
   - 페이지 내 텍스트에서 특정 단어를 강조하거나 가릴 수 있는 기능을 제공합니다.
   - 암기 모드에서 단어 가리기/보이기 기능을 수행합니다.

8) **IndexSelection.java**
   - 사용자가 만든 인덱스를 선택하거나 새로 생성할 수 있는 UI 화면입니다.
   - 인덱스 목록을 보여주고 삭제, 열기 기능을 제공합니다.

9) **FontSize.java**
    - 텍스트 크기를 조절하는 옵션을 제공하는 클래스입니다.
    - 텍스트 크기를 선택하여 크기 조절이 가능합니다.

주요 기능 

- 로그인 / 회원가입
- 인덱스 카드 생성, 삭제
- 페이지 생성, 삭제, 수정
- 이미지 추가
- 텍스트 입력 및 강조/가리기
- 인덱스 저장 및 불러오기
- 인덱스 전체 이미지로 저장
- 텍스트 크기 조절


