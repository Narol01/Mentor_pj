package ait.cohort34.mentor_project.accounting.dao;

import ait.cohort34.mentor_project.accounting.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByTitle(String title);
}
