package com.test.ishop.repos;

import java.util.List;

public interface OrderCustomRepo {
    List findByNativeQuery(String sql);
}
