package com.yurets_y.payment_statistic_web.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity implements Serializable {
	@CreatedDate
	@Column(name = "CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date createdDate;

	@CreatedBy
	@ManyToOne
	@JoinColumn(name="USER_ID")
	protected User createdBy;

	@LastModifiedBy
	@ManyToOne
	@JoinColumn(name = "LAST_MODIFIED_BY")
	protected User lastModifiedBy;

	@LastModifiedDate
	@Column(name = "LAST_MODIFIED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date lastModifiedDate;

	public Optional<User> getCreatedBy() {
		return Optional.of(createdBy);
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Optional<Date> getCreatedDate() {
		return Optional.of(createdDate);
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Optional<User> getLastModifiedBy() {
		return Optional.of(lastModifiedBy);
	}

	public void setLastModifiedBy(User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Optional<Date> getLastModifiedDate() {
		return Optional.of(lastModifiedDate);
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}
