package com.uvt.dw.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "timeseriesdefinition")
public class TimeSeriesDefinition {
	
	public TimeSeriesDefinition() {}
	public TimeSeriesDefinition(String ID, Date SystemTime, String Name, Integer Type, String Description) {
		this.ID = ID;
		this.SystemTime = SystemTime;
		this.Name = Name;
		this.Type = Type;
		this.Description = Description;
		this.Attributes=new HashSet<String>();
	}

	@PrimaryKeyColumn(name = "id", ordinal = 1)
	public String ID ;

	@PrimaryKeyColumn(name = "system_time", ordinal = 0,type = PrimaryKeyType.PARTITIONED)
	public Date SystemTime;

	@Column("attributes")
	public Set<String> Attributes;

	@Column("description")
	public String Description;

	@Column("name")
	public String Name;

	@Column("publisher")
	public String Publisher;

	@Column("type")
	public Integer Type;
}