package org.sopt.makers.operation.admin.repository;

import java.util.Optional;

import org.sopt.makers.operation.admin.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
	Optional<Admin> findByEmail(String email);
	boolean existsByEmail(String email);

}
