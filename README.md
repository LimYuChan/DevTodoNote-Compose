# **DevTodoNote**

DevTodoNote는 개발자를 위한 작업 관리 도구입니다. GitHub 프로젝트와 연동되어 작업(todo) 및 브랜치 상태를 체계적으로 관리하고, 작업에 필요한 참고 정보(링크 및 이미지)를 추가할 수 있습니다. 이 앱은 개발 워크플로우를 개선하고 프로젝트 진행 상황을 효과적으로 파악할 수 있도록 설계되었습니다.

---

## **주요 기능**

### 1. **GitHub 로그인**
- GitHub 계정을 사용하여 로그인.
- GitHub 프로젝트와 연동하여 사용자 데이터 불러오기.

### 2. **프로젝트 관리**
- **프로젝트 리스트**:
  - GitHub에서 불러온 프로젝트를 한눈에 확인.
  - 프로젝트 이름, 설명, 기본 브랜치 정보, 공개/비공개 상태, 마지막 업데이트 날짜 표시.
- 프로젝트 클릭 시 해당 프로젝트의 작업(todo) 목록으로 이동.

### 3. **작업 관리**
- 프로젝트별 작업(todo)를 관리.
- 작업 상태 필터링 기능:
  - **All**: 모든 작업 보기 (None, Commit, Merge).
  - **Todo**: 아직 완료되지 않은 작업 보기 (None, Commit).
  - **Done**: 완료된 작업 보기 ( Merge ).
- 플로팅 버튼을 눌러 새로운 작업 추가.

### 4. **노트 작성 및 관리**
- 작업에 대한 상세 노트를 작성하거나 수정 가능.
- **참고 정보 추가**:
  - **참고 링크**: 작업 관련 링크를 추가하여 추가적인 자료를 연결.
  - **참고 이미지**: 작업에 필요한 이미지를 업로드.
- 브랜치 정보와 작업 상태를 연동하여 작업 관리.
- Merge된 브렌치는 수정 불가능

### 5. **브랜치 기반 작업 관리**
- 작업이 어떤 브랜치에서 진행 중인지 명확히 확인.
- 브랜치별 작업 상태(todo/commit/merge 등)를 시각적으로 확인 가능.
- Merge 시 자동으로 Done 처리.

---

## **화면 구성**

### **1. 로그인 화면**
- GitHub 계정으로 로그인 가능.
- "GitHub 로그인" 버튼을 제공하여 간단히 로그인 처리.

### **2. 메인 화면 (프로젝트 리스트)**
- 사용자의 GitHub 프로젝트 목록 표시.
- 프로젝트의 기본 정보:
  - 프로젝트 이름, 기본 브랜치, 공개/비공개 상태, 마지막 업데이트 날짜, 프로젝트 설명.

### **3. 프로젝트 상세 화면**
- 프로젝트의 작업(todo/done)을 확인.
- 작업 상태를 필터링하여 진행 중인 작업만 볼 수 있음.
- 플로팅 버튼으로 새로운 작업 추가 가능.

### **4. 노트 작성/편집 화면**
- 작업의 상세 내용을 작성하거나 수정 가능.
- **참고 링크**와 **참고 이미지** 추가 기능 제공.
- 현재 작업 중인 브랜치 정보 표시.

### **5. 작업 상태 화면**
- 브랜치별 작업(todo, commit, merge)을 확인.
- 작업 상태를 한눈에 확인하고 관리 가능.

---

## **기술 스택**

- **프론트엔드**:
  - Jetpack Compose
- **백엔드 연동**:
  - Retrofit, OkHttp
- **상태 관리**:
  - ViewModel, Coroutine, Flow
- **의존성 관리**:
  - Dagger hilt
- **구조**:
  - Clean Architecture
  - Multi-Module 구조
- **데이터베이스**:
  - Room Database
- **기타**:
  - Material Design
 
---

## **local.properties**

- baseUrl="https://github.com"
- githubClientId="Your client Id"
- githubSecretKey="Your Secret Key"
- githubRedirectUri="Your Redirect Uri"
- authApiHost="github.com"
- apiHost="api.github.com"

---

# **스크린샷**

|로그인 화면|로그인 실행 화면|
|:---:|:---:|
|![Screenshot_20241118-165301_DevTodoNote-Compose](https://github.com/user-attachments/assets/d84d6d50-e9d9-47d0-bba3-a0efdf33dcf4)|![login](https://github.com/user-attachments/assets/759188ce-2953-4921-a99e-da79bd874c29)|



|Repository 화면|Repository 실행 화면|
|:---:|:---:|
|![Screenshot_20241118-165334_DevTodoNote-Compose](https://github.com/user-attachments/assets/4bac0602-e410-4791-85e8-bd9d496158b3)|![Repository](https://github.com/user-attachments/assets/f276424e-2276-44f3-9480-0ded094e8856)|


|노트 내용 입력|노트 이미지 추가|
|:---:|:---:|
|![Content](https://github.com/user-attachments/assets/211bb399-cd51-40f1-b580-4c9cd8cadcfc)|![Image](https://github.com/user-attachments/assets/e5477448-a8e2-4716-aa4b-5b52372b1022)|


|노트 참고 링크 추가|노트 브렌치 설정|
|:---:|:---:|
|![Link](https://github.com/user-attachments/assets/aaae4c5f-f8ed-459c-b79f-ade7650e3413)|![Branch](https://github.com/user-attachments/assets/297014b0-79a0-4892-8cb7-50cae920d5b7)|


|노트 저장|노트 수정|
|:---:|:---:|
|![Save](https://github.com/user-attachments/assets/606aa3d3-8ffa-457a-be4d-ec0350ee4ebd)|![Edit](https://github.com/user-attachments/assets/b5c7e110-1d77-4998-9916-06934d1c58e8)|


|브렌치 생성 후 커밋|브렌치 생성 후 커밋 상태 변경|
|:---:|:---:|
|![work230_test](https://github.com/user-attachments/assets/3de23e8d-440c-4fbb-9924-3909a3f054da)|![Commit](https://github.com/user-attachments/assets/2ae766eb-e5b6-4803-adfd-82db0f2d22b9)|


|브렌치 머지|브렌치 머지 상태 변경 및 자동 Done 처리|
|:---:|:---:|
![work230_merge](https://github.com/user-attachments/assets/5f743887-7895-4450-8cd9-056328fcaaca)|![Merge](https://github.com/user-attachments/assets/d6f4f371-6e4b-488e-b87a-c87c4cbe4c8c)|
