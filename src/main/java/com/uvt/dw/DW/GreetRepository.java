package com.uvt.dw.DW;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import java.util.UUID;
public interface GreetRepository extends CassandraRepository<Greeting,Object> {
    @Query("SELECT*FROM greetings WHERE user=?0 LIMIT ?1")
    Iterable<Greeting> findByUser(String user,Integer limit);
    @Query("SELECT*FROM greetings WHERE user=?0 AND id<?1 LIMIT ?2")
    Iterable<Greeting> findByUserFrom(String user,UUID from,Integer limit);
}