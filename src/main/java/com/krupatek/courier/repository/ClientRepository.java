package com.krupatek.courier.repository;

import com.krupatek.courier.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    List<Client> findByOrderByClientName();
    List<Client> findAllByClientName(String clientName);
    List<Client> findByClientNameStartsWith(String clientName);
    Page<Client> findByClientNameStartsWith(String clientName, Pageable pageable);
    long countByClientNameStartsWith(String clientName);
}
