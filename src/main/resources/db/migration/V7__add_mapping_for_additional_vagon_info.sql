CREATE TABLE aditional_vagon_info (
	id bigint not null,
	vagon_id bigint,
	has_cert TINYINT(1),
	primary key (id)) engine=InnoDB;

ALTER TABLE vagon
  ADD COLUMN 	vagon_info_id bigint;

ALTER TABLE vagon
  ADD CONSTRAINT VAGON_ADITIONAL_VAGON_INFO_FK
  FOREIGN KEY (vagon_info_id)
  REFERENCES aditional_vagon_info (id);
