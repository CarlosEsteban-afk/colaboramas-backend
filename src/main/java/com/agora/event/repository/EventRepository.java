package com.agora.event.repository;

import com.agora.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Devuelve lista de [EventType, count] para agrupar por tipo
    @Query("SELECT e.type, COUNT(e) FROM Event e GROUP BY e.type")
    List<Object[]> countEventsByType();

}