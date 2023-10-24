# SpringBootBoardPractice

1. 개발환경
   - OpenJDK 17.0.2
   - IntelliJ IDEA Community
   - MySQL Server 8.0
   - Spring Boot 3.1.4
     
2. 라이브러리
   - Spring Data JPA
   - Thymeleaf
   - lombok
   - gradle


---
23.10.20
- 게시글 생성 및 게시글 목록 조회 기능 구현

23.10.21
- 게시글 수정 및 삭제 기능 구현

23.10.23
- 게시판 페이징 기능 구현

23.10.24
- 게시판 파일 업로드 구현(단일 및 다중)
- 게시글 수정 시 파일이 사라지는 문제 수정
   - 게시글을 수정하면, file_attached를 0으로 바꿔버리는 문제가 있었음.
   - DB 상에서 강제로 값을 1로 수정을 하면 정상적으로 나오는 것을 확인하여,
   - BoardDTO에서 board_file_table상에 이미지가 저장되어있는지 여부를 확인하는 if문을 수정함.
   - else if (!boardEntity.getBoardFileEntityList().isEmpty()) { ... }




참고영상) https://www.youtube.com/playlist?list=PLV9zd3otBRt7jmXvwCkmvJ8dH5tR_20c0
