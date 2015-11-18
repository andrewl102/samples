# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table other (
  id                            bigserial not null,
  name                          varchar(255),
  constraint pk_other primary key (id)
);

create table task (
  id                            bigserial not null,
  name                          varchar(255),
  other_id                      bigint,
  constraint uq_task_other_id unique (other_id),
  constraint pk_task primary key (id)
);

alter table task add constraint fk_task_other_id foreign key (other_id) references other (id) on delete restrict on update restrict;


# --- !Downs

alter table task drop constraint if exists fk_task_other_id;

drop table if exists other cascade;

drop table if exists task cascade;

