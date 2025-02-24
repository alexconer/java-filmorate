insert into genre(name)
select 'Комедия' where not exists (select * from genre where name = 'Комедия');

insert into genre(name)
select 'Драма' where not exists (select * from genre where name = 'Драма');

insert into genre(name)
select 'Мультфильм' where not exists (select * from genre where name = 'Мультфильм');

insert into genre(name)
select 'Триллер' where not exists (select * from genre where name = 'Триллер');

insert into genre(name)
select 'Документальный' where not exists (select * from genre where name = 'Документальный');

insert into genre(name)
select 'Боевик' where not exists (select * from genre where name = 'Боевик');

insert into mpa_rating(name, description)
select 'G', 'Нет возрастных ограничений' where not exists (select * from mpa_rating where name = 'G');

insert into mpa_rating(name, description)
select 'PG', 'Детям рекомендуется смотреть фильм с родителями' where not exists (select * from mpa_rating where name = 'PG');

insert into mpa_rating(name, description)
select 'PG-13', 'Детям до 13 лет просмотр не желателен' where not exists (select * from mpa_rating where name = 'PG-13');

insert into mpa_rating(name, description)
select 'R', 'Лицам до 17 лет просматривать фильм можно только в присутствии взрослого' where not exists (select * from mpa_rating where name = 'R');

insert into mpa_rating(name, description)
select 'NC-17', 'Лицам до 18 лет просмотр запрещён' where not exists (select * from mpa_rating where name = 'NC-17');
