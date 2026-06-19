create table FILMS
(
    FILM_ID      INTEGER auto_increment
        primary key,
    NAME         CHARACTER VARYING(255) not null,
    DESCRIPTION  CHARACTER VARYING,
    RELEASE_DATE DATE,
    DURATION     INTEGER                not null
);

create table GENRE_TYPE
(
    GENRE_ID INTEGER               not null,
    NAME     CHARACTER VARYING(20) not null,
    constraint ID
        primary key (GENRE_ID)
);

create table MPA_TYPE
(
    RATING_MPA_ID INTEGER not null
        primary key,
    NAME          CHARACTER VARYING(10)
);

create table USERS
(
    USER_ID  INTEGER auto_increment
        primary key,
    NAME     CHARACTER VARYING(255) not null,
    EMAIL    CHARACTER VARYING(255) not null
        unique,
    LOGIN    CHARACTER VARYING(255) not null
        unique,
    BIRTHDAY DATE                   not null
);

create table FRIENDS
(
    USER_ID   INTEGER not null
        references USERS,
    FRIEND_ID INTEGER not null
        references USERS,
    CONFIRMED BOOLEAN,
    primary key (USER_ID, FRIEND_ID)
);

create table LIKES
(
    USER_ID INTEGER not null
        references USERS,
    FILM_ID INTEGER not null
        references FILMS,
    primary key (USER_ID, FILM_ID)
);

create table POPULAR_USERS
(
    USER_ID       INTEGER not null
        primary key
        references USERS,
    FRIENDS_COUNT INTEGER default 0
);


