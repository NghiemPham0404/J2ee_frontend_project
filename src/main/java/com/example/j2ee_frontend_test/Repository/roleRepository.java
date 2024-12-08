package com.example.j2ee_frontend_test.Repository;

import com.example.j2ee_frontend_test.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface roleRepository extends JpaRepository<Role, Integer> {
    /**
     * Tìm kiếm Role dựa trên tên (unique).
     *
     * @param name tên của Role.
     * @return Optional chứa Role nếu tìm thấy.
     */
    static Optional<Role> findByName(String name) {
        return null;
    }

    /**
     * Tìm tất cả các Role theo trạng thái.
     *
     * @param active trạng thái của Role.
     * @return Danh sách các Role khớp với trạng thái.
     */
    List<Role> findByActive(boolean active);

    /**
     * Tìm Role theo một danh sách tên.
     *
     * @param names danh sách tên cần tìm.
     * @return Danh sách các Role khớp với tên.
     */
    List<Role> findByNameIn(List<String> names);
}
