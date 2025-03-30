//package com.splitwise.repository;
//
//import com.splitwise.model.Group;
//import com.splitwise.model.Settlement;
//import org.springframework.data.jpa.repository.JpaRepository;
//import java.util.List;
//
//public interface SettlementRepository extends JpaRepository<Settlement, Long> {
//    List<Settlement> findByPayerIdOrPayeeId(Long payerId, Long payeeId);
//    List<Settlement> findByGroupId(Long groupId);
//    List<Settlement> findByGroup(Group group);
//}


//30-03

package com.splitwise.repository;

import com.splitwise.model.Group;
import com.splitwise.model.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
    List<Settlement> findByPayerIdOrPayeeId(Long payerId, Long payeeId);

    // 🔄 Changed to use Group instead of Long
    List<Settlement> findByGroup(Group group);
    List<Settlement> findByGroupId(Long groupId);
}
