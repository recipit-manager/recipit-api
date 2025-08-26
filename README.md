```plain

╭━━━╮          ╭╮     ╭━━━┳━━━┳━━╮
┃╭━╮┃         ╭╯╰╮    ┃╭━╮┃╭━╮┣┫┣╯
┃╰━╯┣━━┳━━┳┳━━╋╮╭╯    ┃┃ ┃┃╰━╯┃┃┃
┃╭╮╭┫┃━┫╭━╋┫╭╮┣┫┃ ╭━━╮┃╰━╯┃╭━━╯┃┃
┃┃┃╰┫┃━┫╰━┫┃╰╯┃┃╰╮╰━━╯┃╭━╮┃┃  ╭┫┣╮
╰╯╰━┻━━┻━━┻┫╭━┻┻━╯    ╰╯ ╰┻╯  ╰━━╯
           ┃┃
           ╰╯
```

## Recipit-API

## Project Convention
### Environment
1. Java Version : 17
2. Spring Boot Version : v3.5.4
3. Default Encoding : UTF-8
4. Default File System : Windows

### IDE
1. File Encoding
* `File -> Settings -> Editor -> File Encodings -> Project Encoding -> UTF-8`
* `File -> Settings -> Editor -> File Encodings -> Default encoding for properties files -> UTF-8`
2. Lombok Plugin
* `File -> Settings -> Build, Excution, Deployment -> Compiler -> Annotation Processors -> Enable Annotation Processing check`
3. Tomcat VM Option
* 민감정보는 구글 드라이브 참조 
```properties
 -DSPRING_DATASOURCE_URL={RDB host url}
 -DSPRING_DATASOURCE_USERNAME={RDB username}
 -DSPRING_DATASOURCE_PASSWORD={RDB password}
`-DSPRING_DATA_REDIS_HOST={Redis host url}
`-DSPRING_DATA_REDIS_PORT={Redis port}
`-DSPRING_DATA_REDIS_PASSWORD={Redit password}
``` 

### Source
* [GitHub](https://github.com/recipit-manager/recipit-api)

```bash
git clone https://github.com/recipit-manager/recipit-api.git 
```

### git 사용자 정보 변경
* git bash 쉘을 이용하여, Recipit-API 루트 디렉토리로 이동 후 아래 명령 실행
```bash
git config --local user.name 본인성명
git config --local user.email 메일주소(GitHub 계정에 등록된 주소)
```