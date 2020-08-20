create table app_user (
  id bigint not null,
  active bit not null,
  email varchar(255),
  password varchar(255) not null,
  username varchar(255) not null,
  primary key (id)
  ) engine=InnoDB;

create table hibernate_sequence (next_val bigint) engine=InnoDB;

insert into hibernate_sequence values ( 1 );
insert into hibernate_sequence values ( 1 );

create table payment_details (
  id bigint not null,
  additional_payment bigint not null,
  date date,
  document_number varchar(255),
  income_type integer,
  payment bigint not null,
  payment_code varchar(255),
  payment_description varchar(255),
  station_code integer not null,
  station_name varchar(255),
  tax_payment bigint not null,
  total_payment bigint not null,
  type varchar(255),
  number integer,
  payer_code integer,
  primary key (id)
) engine=InnoDB;

create table payment_list (
  number integer not null,
  payer_code integer not null,
  created_date datetime(6),
  last_modified_date datetime(6),
  backup_file_path varchar(255),
  closing_balance bigint not null,
  contract_number varchar(255),
  date date,
  opening_balance bigint not null,
  payer_name varchar(255),
  payment_taxes bigint not null,
  payment_vs_taxes bigint not null,
  payments bigint not null,
  tax_code integer not null,
  test_passed bit not null,
  user_id bigint,
  last_modified_by bigint,
  primary key (number, payer_code)
) engine=InnoDB;

create table user_role (
  user_id bigint not null,
  roles varchar(255)
) engine=InnoDB;

alter table payment_details
  add constraint payment_details__payment_list_fk
  foreign key (number, payer_code)
  references payment_list (number, payer_code);

alter table payment_list
  add constraint payment_list__app_user_creator_fk
  foreign key (user_id)
  references app_user (id);

alter table payment_list
  add constraint payment_list__app_user_modifier_fk
  foreign key (last_modified_by)
  references app_user (id);

alter table user_role
  add constraint user_role__user_fk
  foreign key (user_id)
  references app_user (id);