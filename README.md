# Back-End
![image](https://github.com/user-attachments/assets/e4a7583b-a166-4c5b-9b99-ae1dd59402ae)

현재는 54.180.146.236을 배스천 인스턴스로 하고, Local의 33308 포트를 로컬 포트 포워딩을 하여 실행할 수 있도록 설정 하였습니다.      
도커 이미지 빌드 및 실행 방법은,        
1. BackEnd\Capstone 디렉토리에서 ./gradlew -x test build 명령어 입력
2. docker build -t designovel_backend .
3. docker run --network="host" -p 8000:8000 --restart=always --name designovel_backend designovel_backend

     


순서대로 입력 시 Docker로 실행됩니다.      
추가로 DockerFile에 FAST_API_URL 이 있습니다. 이미지 검색 서버 URL을 환경변수로 빼 놓았습니다 


2024-10-14 update
새롭게 인스턴스를 주셔서 새로운 인스턴스에 로컬포트포워딩으로 33308 포트를 베스천 인스턴스를 통해 MySQL에 연결하도록 했습니다.
