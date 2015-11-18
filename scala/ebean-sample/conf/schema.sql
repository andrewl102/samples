create table task (
  id                        bigint not null,
  name                     varchar(100),
  other_id                 bigint)
;

create table other (
  id                        bigint not null,
  name                     varchar(100))
;