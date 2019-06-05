package com.uvt.dw.Data;

import java.util.Date;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "attribute")
public class Attribute {

	public Attribute() {
	}

	public Attribute(String ID, Date SystemTime, String Name, Integer DataType) {
		this.ID = ID;
		this.SystemTime = SystemTime;
		this.Name = Name;
		this.DataType = DataType;
	}

	@PrimaryKeyColumn(name = "id", ordinal = 1)
	public String ID;

	@PrimaryKeyColumn(name = "system_time", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	public Date SystemTime;

	@Column("data_type")
	public Integer DataType;

	@Column("description")
	public String Description;

	@Column("name")
	public String Name;
}
