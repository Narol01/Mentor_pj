package ait.cohort34.mentor_project.accounting.dao;

import ait.cohort34.mentor_project.accounting.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByLogin(String login);
    Boolean existsByLogin(String login);
}
