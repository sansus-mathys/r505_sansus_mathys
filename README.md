# TP Framework – API Blog (Spring Boot) - SANSUS MATHYS

## Anonyme
POST /users + {"username": "Jean", "password": "pw", "role": "PUBLISHER"}
GET /articles
GET /articles/{id}
POST /auth/login => {"username": "Jean","password": "pw"}

## Publisher
GET /articles
GET /articles/{id}
POST /articles => {"content": "mon articles pref"}
PUT /articles/{id} => {"content": "mon articles pref"}
DELETE /articles/{id}
PUT /articles/{id}/likes
PUT /articles/{id}/dislikes
DELETE /articles/{id}/reactions

## Moderator
GET /articles
GET /articles/{id}
DELETE /articles/{id}