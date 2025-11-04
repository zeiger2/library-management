package com.artzver.library.repository;

import com.artzver.library.entity.Loan;
import com.artzver.library.entity.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserId(Long userId);
    List<Loan> findByUserIdAndStatus(Long userId, LoanStatus status);
    List<Loan> findByBookId(Long bookId);

    @Query("SELECT l FROM Loan l WHERE l.dueDate < :currentDate AND l.status = 'ACTIVE'")
    List<Loan> findOverdueLoans(@Param("currentDate") LocalDateTime currentDate);

    List<Loan> findByStatus(LoanStatus status);

    @Query("SELECT l FROM Loan l WHERE l.book.id = :bookId AND l.user.id = :userId AND l.status = 'ACTIVE'")
    Optional<Loan> findActiveLoanByBookAndUser(@Param("bookId") Long bookId, @Param("userId") Long userId);
}