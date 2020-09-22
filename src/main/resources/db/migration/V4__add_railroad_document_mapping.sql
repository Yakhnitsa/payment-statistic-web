CREATE TABLE railroad_document (
	date_stamp dateStamp not null,
	doc_number integer not null,
	created_date datetime(6),
	last_modified_date datetime(6),
	cargo_code varchar(10),
	cargo_name varchar(60),
	column15info varchar(255),
	column7info varchar(255),
	cred_date datetime(6),
	del_date datetime(6),
	doc_date datetime(6),
	notf_date datetime(6),
	payment integer not null,
	pdf_backup_file_path varchar(255),
	tarif_distance integer not null,
	xml_backup_file_path varchar(255),
	user_id bigint,
	last_modified_by bigint,
	cargo_receiver_id bigint,
	cargo_sender_id bigint,
	receive_station_code integer,
	send_station_code integer,
	tarif_payer_id bigint,
	primary key (date_stamp, doc_number)) engine=InnoDB;

CREATE TABLE client (
	id bigint not null,
	address varchar(255),
	edrpu_code integer,
	name varchar(255),
	railroad_code integer,
	primary key (id)) engine=InnoDB;

CREATE TABLE vagon (
	id bigint not null,
	created_date datetime(6),
	last_modified_date datetime(6),
	carrying_capacity double not null,
	gross_weight integer not null,
	net_weight integer not null,
	number integer not null,
	payment integer not null,
	tare_weight integer not null,
	user_id bigint,
	last_modified_by bigint,
	date_stamp dateStamp not null,
	doc_number integer not null,
	primary key (id)) engine=InnoDB;

CREATE TABLE railroad_document_stamps (
	railroad_document_date_stamp dateStamp not null,
	railroad_document_doc_number integer not null,
	stamps varchar(255),
	stamps_key varchar(10) not null,
	primary key (railroad_document_date_stamp, railroad_document_doc_number, stamps_key)) engine=InnoDB;

CREATE TABLE vagon_zpu_list (
	vagon_id bigint not null,
	zpu_list varchar(255)) engine=InnoDB;


ALTER TABLE railroad_document ADD CONSTRAINT RAIL_DOC_USER_CREATOR_FK FOREIGN KEY (user_id) REFETENCES app_user;

ALTER TABLE railroad_document ADD CONSTRAINT RAIL_DOC_USER_EDITOR_FK FOREIGN KEY (last_modified_by) REFETENCES app_user;

ALTER TABLE railroad_document ADD CONSTRAINT RAIL_DOC_CARGO_SENDER_FK FOREIGN KEY (cargo_sender_id) REFETENCES client;

ALTER TABLE railroad_document ADD CONSTRAINT RAIL_DOC_CARGO_RECEIVER_FK FOREIGN KEY (cargo_receiver_id) REFETENCES client;

ALTER TABLE railroad_document ADD CONSTRAINT RAIL_DOC_TARIF_PAYER_FK FOREIGN KEY (tarif_payer_id) REFETENCES client;

ALTER TABLE railroad_document ADD CONSTRAINT RAIL_DOC_SEND_STATION_FK FOREIGN KEY (send_station_code) REFETENCES railroad_stations;

ALTER TABLE railroad_document ADD CONSTRAINT RAIL_DOC_RECEIVE_STATION_FK FOREIGN KEY (receive_station_code) REFETENCES railroad_stations;

ALTER TABLE railroad_document_stamps ADD CONSTRAINT DOCUMENT_STAMP_RAIL_DOC_FK FOREIGN KEY (railroad_document_date_stamp, railroad_document_doc_number) REFETENCES railroad_document;

ALTER TABLE vagon ADD CONSTRAINT VAGON_USER_CREATOR_FK FOREIGN KEY (user_id) REFETENCES app_user;

ALTER TABLE vagon ADD CONSTRAINT VAGON_USER_EDITOR_FK FOREIGN KEY (last_modified_by) REFETENCES app_user;

ALTER TABLE vagon ADD CONSTRAINT VAGON_RAILROAD_DOCUMENT_FK FOREIGN KEY (date_stamp, doc_number) REFETENCES railroad_document;

ALTER TABLE vagon_zpu_list ADD CONSTRAINT VAGON_ZPU_VAGON_FK FOREIGN KEY (vagon_id) REFETENCES vagon;