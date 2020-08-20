create sequence hibernate_sequence start with 1 increment by 1;

create table app_user(
  id bigint not null,
  active boolean not null,
  email varchar(255),
  password varchar(255),
  username varchar(255),
  primary key (id)
);

create table payment_details(
  id bigint not null,
  additional_payment bigint not null,
  date date, document_number varchar(255),
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
);

create table payment_list (
  number integer not null,
  payer_code integer not null,
  created_date timestamp,
  last_modified_date timestamp,
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
  test_passed boolean not null,
  user_id bigint,
  last_modified_by bigint,
  primary key (number, payer_code)
);

create table user_role (
  user_id bigint not null,
  roles varchar(255)
);

alter table payment_details add constraint FK_paymDetail_paymList foreign key (number, payer_code) references payment_list;

alter table payment_list add constraint FK_payList_appUser_createdBy foreign key (user_id) references app_user;

alter table payment_list add constraint FK_payList_appUser_modifiedBy foreign key (last_modified_by) references app_user;

alter table user_role add constraint FK_appUser_userRole foreign key (user_id) references app_user;