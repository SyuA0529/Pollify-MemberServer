package dku.cloudcomputing.memberserver.repository;

import dku.cloudcomputing.memberserver.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public Optional<Member> findByEmail(String email);

    public boolean existsByEmail(String email);
}
