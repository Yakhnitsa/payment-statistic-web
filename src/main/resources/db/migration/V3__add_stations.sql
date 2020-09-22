create table railroad_stations (
  code integer not null,
  administration varchar(255),
  created_date datetime(6),
  last_modified_date datetime(6),
  location_x double precision,
  location_y double precision,
  node integer not null,
  rail_department varchar(255),
  rus_name varchar(255),
  ukr_name varchar(255),
  user_id bigint,
  last_modified_by bigint,
  primary key (code)) engine=InnoDB;

