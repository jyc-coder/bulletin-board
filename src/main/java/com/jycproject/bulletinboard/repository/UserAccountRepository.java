package com.jycproject.bulletinboard.repository;

import com.jycproject.bulletinboard.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
}
